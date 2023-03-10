package io.github.protocol.sms.sgip.server;

import io.github.protocol.codec.sgip.SgipBind;
import io.github.protocol.codec.sgip.SgipDecoder;
import io.github.protocol.codec.sgip.SgipEncoder;
import io.github.protocol.codec.sgip.SgipMessage;
import io.github.protocol.codec.sgip.SgipReport;
import io.github.protocol.codec.sgip.SgipSubmit;
import io.github.protocol.codec.sgip.SgipTrace;
import io.github.protocol.codec.sgip.SgipUnbind;
import io.github.protocol.codec.sgip.SgipUserRpt;
import io.github.protocol.sms.server.util.SslContextUtil;
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

@ChannelHandler.Sharable
@Slf4j
public class SgipServer extends ChannelInboundHandlerAdapter {

    private final SgipConfig config;

    private final Optional<SslContext> sslContextOp;

    private EventLoopGroup acceptorGroup;

    private EventLoopGroup ioGroup;

    public SgipServer(SgipConfig config) {
        this.config = config;
        if (config.useSsl) {
            sslContextOp = Optional.of(SslContextUtil.buildFromJks(config.keyStorePath, config.keyStorePassword,
                    config.trustStorePath, config.trustStorePassword, config.skipSslVerify,
                    config.ciphers));
        } else {
            sslContextOp = Optional.empty();
        }
    }

    public void start() throws Exception {
        if (acceptorGroup != null) {
            throw new IllegalStateException("sgip server already started");
        }
        log.info("begin start sgip server, config is {}", config);
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
                if (config.useSsl) {
                    if (!sslContextOp.isPresent()) {
                        throw new IllegalStateException("ssl context not present");
                    }
                    p.addLast(sslContextOp.get().newHandler(ch.alloc()));
                }
                p.addLast(new SgipDecoder());
                p.addLast(SgipEncoder.INSTANCE);
                p.addLast(SgipServer.this);
            }
        });
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        if (channelFuture.isSuccess()) {
            log.info("sgip server started");
        } else {
            log.error("sgip server start failed", channelFuture.cause());
            throw new Exception("sgip server start failed", channelFuture.cause());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        if (!(message instanceof SgipMessage)) {
            return;
        }
        if (message instanceof SgipBind) {
            processBind(ctx, (SgipBind) message);
        } else if (message instanceof SgipUnbind) {
            processUnbind(ctx, (SgipUnbind) message);
        } else if (message instanceof SgipSubmit) {
            processSubmit(ctx, (SgipSubmit) message);
        } else if (message instanceof SgipReport) {
            processReport(ctx, (SgipReport) message);
        } else if (message instanceof SgipUserRpt) {
            processUserRpt(ctx, (SgipUserRpt) message);
        } else if (message instanceof SgipTrace) {
            processTrace(ctx, (SgipTrace) message);
        }
    }

    private void processBind(ChannelHandlerContext ctx, SgipBind msg) {
    }

    private void processUnbind(ChannelHandlerContext ctx, SgipUnbind msg) {
    }

    private void processSubmit(ChannelHandlerContext ctx, SgipSubmit msg) {
    }

    private void processReport(ChannelHandlerContext ctx, SgipReport msg) {
    }

    private void processUserRpt(ChannelHandlerContext ctx, SgipUserRpt msg) {
    }

    private void processTrace(ChannelHandlerContext ctx, SgipTrace msg) {
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
