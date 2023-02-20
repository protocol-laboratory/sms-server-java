package io.github.protocol.sms.sgip.server;

import io.github.protocol.codec.sgip.SgipDecoder;
import io.github.protocol.codec.sgip.SgipEncoder;
import io.github.protocol.codec.sgip.SgipMessage;
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
public class SgipServer extends ChannelInboundHandlerAdapter {

    private final SgipConfig config;

    private EventLoopGroup acceptorGroup;

    private EventLoopGroup ioGroup;

    public SgipServer(SgipConfig config) {
        this.config = config;
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
        SgipMessage msg = (SgipMessage) message;
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
