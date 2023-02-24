package io.github.protocol.sms.cngp.server;

import io.github.protocol.codec.cngp.CngpDecoder;
import io.github.protocol.codec.cngp.CngpEncoder;
import io.github.protocol.codec.cngp.CngpExit;
import io.github.protocol.codec.cngp.CngpLogin;
import io.github.protocol.codec.cngp.CngpSubmit;
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
public class CngpServer extends ChannelInboundHandlerAdapter {

    private final CngpConfig config;

    private EventLoopGroup acceptorGroup;

    private EventLoopGroup ioGroup;

    public CngpServer(CngpConfig config) {
        this.config = config;
    }

    public void start() throws Exception {
        if (acceptorGroup != null) {
            throw new IllegalStateException("cngp server already started");
        }
        log.info("begin start cngp server, config is {}", config);
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
                p.addLast(new CngpDecoder());
                p.addLast(CngpEncoder.INSTANCE);
                p.addLast(CngpServer.this);
            }
        });
        ChannelFuture channelFuture = serverBootstrap.bind().sync();
        if (channelFuture.isSuccess()) {
            log.info("cngp server started");
        } else {
            log.error("cngp server start failed", channelFuture.cause());
            throw new Exception("cngp server start failed", channelFuture.cause());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof CngpLogin) {
            processLogin(ctx, (CngpLogin) msg);
        }  else if (msg instanceof CngpSubmit) {
            processSubmit(ctx, (CngpSubmit) msg);
        } else if (msg instanceof CngpExit) {
            processExit(ctx, (CngpExit) msg);
        }
    }

    private void processLogin(ChannelHandlerContext ctx, CngpLogin msg) {
    }

    private void processSubmit(ChannelHandlerContext ctx, CngpSubmit msg) {
    }

    private void processExit(ChannelHandlerContext ctx, CngpExit msg) {
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
