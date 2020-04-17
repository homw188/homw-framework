package com.homw.transport.netty;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @description Netty Client
 * @author Hom
 * @version 1.0
 * @since 2020-04-17
 */
public class NettyClient {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String host;
	protected int port;
	protected Channel channel;
	protected ChannelInitializer<SocketChannel> channelIntialezer;

	public static final int SEND_BUFFER = 1024 * 1024;
	public static final int RECEIVE_BUFFER = 1024 * 1024;
	public static final int CONNECT_TIME_OUT = 1000 * 30;

	/**
	 * 初始化
	 * 
	 * @param host
	 * @param port
	 * @param channelIntialezer handler初始化器，不能为空
	 */
	public NettyClient(String host, int port, ChannelInitializer<SocketChannel> channelIntialezer) {
		this.host = host;
		this.port = port;

		Validate.notNull(channelIntialezer, "channelIntialezer must not null");
		this.channelIntialezer = channelIntialezer;
	}

	/**
	 * 建立连接
	 * 
	 * @throws InterruptedException
	 */
	public void connect() throws InterruptedException {
		final NioEventLoopGroup eventLoop = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoop);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.SO_SNDBUF, SEND_BUFFER);
		bootstrap.option(ChannelOption.SO_RCVBUF, RECEIVE_BUFFER);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIME_OUT);
		bootstrap.handler(channelIntialezer);

		ChannelFuture future = bootstrap.connect(host, port).sync();
		channel = future.channel();
		future.channel().closeFuture().addListener(new GenericFutureListener<ChannelFuture>() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				eventLoop.shutdownGracefully();
			}
		});
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 */
	public void sendMessage(Object message) {
		Validate.notNull(channel, "channel is not initialized");
		if (message == null)
			return;

		if (channel.isActive() && channel.isWritable()) {
			channel.writeAndFlush(message);
		} else {
			close();
			throw new IllegalStateException("channel state incorrect, not writable");
		}
	}

	/**
	 * 关闭连接，释放资源
	 */
	public void close() {
		if (channel != null) {
			channel.close();
			channel = null;
		}
	}
}