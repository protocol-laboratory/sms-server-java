package io.github.protocol.sms.smpp.server;

import io.github.protocol.codec.smpp.SmppBindReceiver;
import io.github.protocol.codec.smpp.SmppBindReceiverBody;
import io.github.protocol.codec.smpp.SmppBindReceiverResp;
import io.github.protocol.codec.smpp.SmppBindReceiverRespBody;
import io.github.protocol.codec.smpp.SmppBindTransceiver;
import io.github.protocol.codec.smpp.SmppBindTransceiverBody;
import io.github.protocol.codec.smpp.SmppBindTransceiverResp;
import io.github.protocol.codec.smpp.SmppBindTransceiverRespBody;
import io.github.protocol.codec.smpp.SmppBindTransmitter;
import io.github.protocol.codec.smpp.SmppBindTransmitterBody;
import io.github.protocol.codec.smpp.SmppBindTransmitterResp;
import io.github.protocol.codec.smpp.SmppBindTransmitterRespBody;
import io.github.protocol.codec.smpp.SmppConst;
import io.github.protocol.codec.smpp.SmppDecoder;
import io.github.protocol.codec.smpp.SmppDeliverSm;
import io.github.protocol.codec.smpp.SmppDeliverSmResp;
import io.github.protocol.codec.smpp.SmppDeliverSmRespBody;
import io.github.protocol.codec.smpp.SmppEncoder;
import io.github.protocol.codec.smpp.SmppEnquireLink;
import io.github.protocol.codec.smpp.SmppEnquireLinkResp;
import io.github.protocol.codec.smpp.SmppHeader;
import io.github.protocol.codec.smpp.SmppMessage;
import io.github.protocol.codec.smpp.SmppQuerySm;
import io.github.protocol.codec.smpp.SmppQuerySmBody;
import io.github.protocol.codec.smpp.SmppQuerySmResp;
import io.github.protocol.codec.smpp.SmppQuerySmRespBody;
import io.github.protocol.codec.smpp.SmppSubmitMulti;
import io.github.protocol.codec.smpp.SmppSubmitMultiBody;
import io.github.protocol.codec.smpp.SmppSubmitMultiResp;
import io.github.protocol.codec.smpp.SmppSubmitMultiRespBody;
import io.github.protocol.codec.smpp.SmppSubmitSm;
import io.github.protocol.codec.smpp.SmppSubmitSmResp;
import io.github.protocol.codec.smpp.SmppSubmitSmRespBody;
import io.github.protocol.codec.smpp.SmppUnbind;
import io.github.protocol.sms.server.util.SslContextUtil;
import io.github.protocol.codec.smpp.UnsuccessfulDelivery;
import io.github.protocol.sms.server.util.MessageIdUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@ChannelHandler.Sharable
@Slf4j
public class SmppServer extends ChannelInboundHandlerAdapter {

    private final SmppConfig config;

    private final Optional<SslContext> sslContextOp;

    private EventLoopGroup acceptorGroup;

    private EventLoopGroup ioGroup;

    private SmppListener listener;

    public SmppServer(SmppConfig config) {
        this.config = config;
        if (config.useSsl) {
            sslContextOp = Optional.of(SslContextUtil.buildFromJks(config.keyStorePath, config.keyStorePassword,
                    config.trustStorePath, config.trustStorePassword, config.skipSslVerify,
                    config.ciphers));
        } else {
            sslContextOp = Optional.empty();
        }
    }

    public SmppServer(SmppConfig config, SmppListener listener) {
        this(config);
        this.listener = listener;
    }

    public void start() throws Exception {
        if (acceptorGroup != null) {
            throw new IllegalStateException("smpp server already started");
        }
        log.info("begin start smpp server, config is {}", config);
        if (config.acceptorThreadsNum > 0) {
            acceptorGroup = new NioEventLoopGroup(config.acceptorThreadsNum);
        } else {
            acceptorGroup = new NioEventLoopGroup();
        }
        if (config.ioThreadsNum > 0) {
            ioGroup = new NioEventLoopGroup(config.ioThreadsNum);
        } else {
            ioGroup = new NioEventLoopGroup();
        }
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(acceptorGroup, ioGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.localAddress(new InetSocketAddress(config.host, config.port));
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ChannelPipeline p = ch.pipeline();
                if (config.useSsl) {
                    if (!sslContextOp.isPresent()) {
                        throw new IllegalStateException("ssl context not present");
                    }
                    p.addLast(sslContextOp.get().newHandler(ch.alloc()));
                }
                p.addLast(new SmppDecoder());
                p.addLast(SmppEncoder.INSTANCE);
                p.addLast(SmppServer.this);
            }
        });
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        if (channelFuture.isSuccess()) {
            log.info("smpp server started");
        } else {
            log.error("smpp server start failed", channelFuture.cause());
            throw new Exception("smpp server start failed", channelFuture.cause());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        if (!(message instanceof SmppMessage)) {
            return;
        }
        if (message instanceof SmppBindReceiver) {
            processBindReceiver(ctx, (SmppBindReceiver) message);
        } else if (message instanceof SmppBindTransmitter) {
            processBindTransmitter(ctx, (SmppBindTransmitter) message);
        } else if (message instanceof SmppQuerySm) {
            processQuerySm(ctx, (SmppQuerySm) message);
        } else if (message instanceof SmppSubmitSm) {
            processSubmitSm(ctx, (SmppSubmitSm) message);
        } else if (message instanceof SmppDeliverSm) {
            processDeliverSm(ctx, (SmppDeliverSm) message);
        } else if (message instanceof SmppUnbind) {
            processUnbind(ctx, (SmppUnbind) message);
        } else if (message instanceof SmppBindTransceiver) {
            processBindTransceiver(ctx, (SmppBindTransceiver) message);
        } else if (message instanceof SmppEnquireLink) {
            processEnquireLink(ctx, (SmppEnquireLink) message);
        } else if (message instanceof SmppSubmitMulti) {
            processSubmitMulti(ctx, (SmppSubmitMulti) message);
        }
    }

    private void processBindReceiver(ChannelHandlerContext ctx, SmppBindReceiver msg) {
        if (listener != null) {
            listener.onBindReceiver(msg);
        }
        SmppHeader header = new SmppHeader(SmppConst.BIND_RECEIVER_ID, 0, msg.header().sequenceNumber());
        SmppBindReceiverBody body = msg.body();
        SmppBindReceiverRespBody respBody = new SmppBindReceiverRespBody(body.systemId());
        ctx.writeAndFlush(new SmppBindReceiverResp(header, respBody));
    }

    private void processBindTransmitter(ChannelHandlerContext ctx, SmppBindTransmitter msg) {
        if (listener != null) {
            listener.onBindTransmitter(msg);
        }
        SmppHeader header = new SmppHeader(SmppConst.BIND_TRANSMITTER_RESP_ID, 0, msg.header().sequenceNumber());
        SmppBindTransmitterBody body = msg.body();
        SmppBindTransmitterRespBody respBody = new SmppBindTransmitterRespBody(body.systemId());
        ctx.writeAndFlush(new SmppBindTransmitterResp(header, respBody));
    }

    private void processQuerySm(ChannelHandlerContext ctx, SmppQuerySm msg) {
        if (listener != null) {
            listener.onQuerySm(msg);
        }
        SmppHeader header = new SmppHeader(SmppConst.QUERY_SM_RESP_ID, 0, msg.header().sequenceNumber());
        SmppQuerySmBody body = msg.body();
        // ?
        SmppQuerySmRespBody respBody = new SmppQuerySmRespBody(body.messageId(), null, (byte) 6, (byte) 0);
        ctx.writeAndFlush(new SmppQuerySmResp(header, respBody));
    }

    private void processSubmitSm(ChannelHandlerContext ctx, SmppSubmitSm msg) {
        if (listener != null) {
            listener.onSubmitSm(msg);
        }
        SmppHeader header = new SmppHeader(SmppConst.SUBMIT_SM_RESP_ID, 0, msg.header().sequenceNumber());
        SmppSubmitSmRespBody respBody = new SmppSubmitSmRespBody(MessageIdUtil.generateMessageId());
        ctx.writeAndFlush(new SmppSubmitSmResp(header, respBody));
    }

    private void processDeliverSm(ChannelHandlerContext ctx, SmppDeliverSm msg) {
        if (listener != null) {
            listener.onDeliverSm(msg);
        }
        SmppHeader header = new SmppHeader(SmppConst.DELIVER_SM_RESP_ID, 0, msg.header().sequenceNumber());
        SmppDeliverSmRespBody respBody = new SmppDeliverSmRespBody(MessageIdUtil.generateMessageId());
        ctx.writeAndFlush(new SmppDeliverSmResp(header, respBody));
    }

    private void processUnbind(ChannelHandlerContext ctx, SmppUnbind msg) {
        if (listener != null) {
            listener.onUnbind(msg);
        }
        SmppHeader header = new SmppHeader(SmppConst.UNBIND_RESP_ID, 0, msg.header().sequenceNumber());
        ctx.writeAndFlush(new SmppUnbind(header));
    }

    private void processBindTransceiver(ChannelHandlerContext ctx, SmppBindTransceiver msg) {
        if (listener != null) {
            listener.onBindTransceiver(msg);
        }
        SmppHeader smppHeader = new SmppHeader(SmppConst.BIND_TRANSCEIVER_RESP_ID, 0, msg.header().sequenceNumber());
        SmppBindTransceiverBody body = msg.body();
        SmppBindTransceiverRespBody respBody = new SmppBindTransceiverRespBody(body.systemId());
        ctx.writeAndFlush(new SmppBindTransceiverResp(smppHeader, respBody));
    }

    private void processEnquireLink(ChannelHandlerContext ctx, SmppEnquireLink msg) {
        if (listener != null) {
            listener.onEnquireLink(msg);
        }
        SmppHeader smppHeader = new SmppHeader(SmppConst.ENQUIRE_LINK_RESP_ID, 0, msg.header().sequenceNumber());
        ctx.writeAndFlush(new SmppEnquireLinkResp(smppHeader));
    }

    private void processSubmitMulti(ChannelHandlerContext ctx, SmppSubmitMulti msg) {
        if (listener != null) {
            listener.onSubmitMulti(msg);
        }
        SmppHeader smppHeader = new SmppHeader(SmppConst.SUBMIT_MULTI_RESP_ID, 0, msg.header().sequenceNumber());
        SmppSubmitMultiBody body = msg.body();
        List<UnsuccessfulDelivery> unsuccessSmes = body.destAddresses().stream().map(address ->
                new UnsuccessfulDelivery(address.destAddrTon(), address.destAddrNpi(), address.destinationAddr(), 0)
        ).collect(Collectors.toList());
        SmppSubmitMultiRespBody respBody = new SmppSubmitMultiRespBody(MessageIdUtil.generateMessageId(),
                (byte) 0, unsuccessSmes);
        ctx.writeAndFlush(new SmppSubmitMultiResp(smppHeader, respBody));
    }

    public void stop() {
        if (acceptorGroup != null) {
            acceptorGroup.shutdownGracefully();
        }
        if (ioGroup != null) {
            ioGroup.shutdownGracefully();
        }
    }
}
