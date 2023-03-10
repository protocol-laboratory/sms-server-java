package io.github.protocol.sms.smgp.server;

import io.github.protocol.codec.smgp.SmgpDecoder;
import io.github.protocol.codec.smgp.SmgpEncoder;
import io.github.protocol.codec.smgp.SmgpLogin;
import io.github.protocol.codec.smgp.SmgpMessage;
import io.github.protocol.codec.smgp.SmgpSubmit;
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
public class SmgpServer extends ChannelInboundHandlerAdapter {

    private final SmgpConfig config;

    private final Optional<SslContext> sslContextOp;

    private EventLoopGroup acceptorGroup;

    private EventLoopGroup ioGroup;

    public SmgpServer(SmgpConfig config) {
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
            throw new IllegalStateException("smgp server already started");
        }
        log.info("begin start smgp server, config is {}", config);
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
                p.addLast(new SmgpDecoder());
                p.addLast(SmgpEncoder.INSTANCE);
                p.addLast(SmgpServer.this);
            }
        });
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        if (channelFuture.isSuccess()) {
            log.info("smgp server started");
        } else {
            log.error("smgp server start failed", channelFuture.cause());
            throw new Exception("smgp server start failed", channelFuture.cause());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object message) {
        if (!(message instanceof SmgpMessage)) {
            return;
        }
        if (message instanceof SmgpLogin) {
            processLogin(ctx, (SmgpLogin) message);
        } else if (message instanceof SmgpSubmit) {
            processSubmit(ctx, (SmgpSubmit) message);
        }
    }

    private void processLogin(ChannelHandlerContext ctx, SmgpLogin msg) {
    }

    private void processSubmit(ChannelHandlerContext ctx, SmgpSubmit msg) {
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
