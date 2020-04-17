package com.homw.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description Netty Server
 * @author Hom
 * @version 1.0
 * @since 2020-04-17
 */
public class NettyServer {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected int port;
    protected EventLoopGroup parentGroup, childGroup;
    protected ChannelInitializer<SocketChannel> channelInitializer;

    public static final int SO_BACKLOG = 1024;

    /**
     * 初始化
     * @param port
     * @param channelInitializer handler初始化器，不能为空
     */
    public NettyServer(int port, ChannelInitializer<SocketChannel> channelInitializer) {
        this.port = port;

        Validate.notNull(channelInitializer, "channelInitializer must not null");
        this.channelInitializer = channelInitializer;
    }

    /**
     * 启动服务
     * @throws InterruptedException
     */
    public void start() throws InterruptedException {
        parentGroup = new NioEventLoopGroup();
        childGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(parentGroup, childGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, SO_BACKLOG);
        bootstrap.childHandler(channelInitializer);
        bootstrap.bind(port).sync();
    }

    /**
     * 关闭服务，释放资源
     */
    public void shutdown() {
        if (parentGroup != null) {
            parentGroup.shutdownGracefully();
        }

        if (childGroup != null) {
            childGroup.shutdownGracefully();
        }
    }
}
