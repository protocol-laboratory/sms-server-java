package io.github.protocol.sms.smpp.server;

import io.github.protocol.codec.smpp.SmppBindReceiver;
import io.github.protocol.codec.smpp.SmppBindTransceiver;
import io.github.protocol.codec.smpp.SmppBindTransmitter;
import io.github.protocol.codec.smpp.SmppDecoder;
import io.github.protocol.codec.smpp.SmppDeliverSm;
import io.github.protocol.codec.smpp.SmppEncoder;
import io.github.protocol.codec.smpp.SmppEnquireLink;
import io.github.protocol.codec.smpp.SmppMessage;
import io.github.protocol.codec.smpp.SmppQuerySm;
import io.github.protocol.codec.smpp.SmppSubmitMulti;
import io.github.protocol.codec.smpp.SmppSubmitSm;
import io.github.protocol.codec.smpp.SmppUnbind;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class SmppServer extends ChannelInboundHandlerAdapter {

    private final SmppConfig config;

    private EventLoopGroup acceptorGroup;

    private EventLoopGroup ioGroup;

    public SmppServer(SmppConfig config) {
        this.config = config;
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
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
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
    }

    private void processBindTransmitter(ChannelHandlerContext ctx, SmppBindTransmitter msg) {
    }

    private void processQuerySm(ChannelHandlerContext ctx, SmppQuerySm msg) {
    }

    private void processSubmitSm(ChannelHandlerContext ctx, SmppSubmitSm msg) {
    }

    private void processDeliverSm(ChannelHandlerContext ctx, SmppDeliverSm msg) {
    }

    private void processUnbind(ChannelHandlerContext ctx, SmppUnbind msg) {
    }

    private void processBindTransceiver(ChannelHandlerContext ctx, SmppBindTransceiver msg) {
    }

    private void processEnquireLink(ChannelHandlerContext ctx, SmppEnquireLink msg) {
    }

    private void processSubmitMulti(ChannelHandlerContext ctx, SmppSubmitMulti msg) {
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
