package com.homw.transport.netty;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.transport.netty.session.Session;

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
public class TcpClient {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected String host;
	protected int port;
	protected Channel channel;
	protected ChannelInitializer<SocketChannel> channelInitializer;

	public static final int CONNECT_TIME_OUT = 1000 * 30;

	/**
	 * 初始化
	 * 
	 * @param host
	 * @param port
	 * @param channelInitializer handler初始化器，不能为空
	 */
	private TcpClient(String host, int port, ChannelInitializer<SocketChannel> channelInitializer) {
		this.host = host;
		this.port = port;

		Validate.notNull(channelInitializer, "channelIntialezer must not null");
		this.channelInitializer = channelInitializer;
	}

	/**
	 * 建立连接
	 * 
	 * @throws InterruptedException
	 */
	protected void connect() throws InterruptedException {
		final NioEventLoopGroup eventLoop = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoop);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIME_OUT);
		bootstrap.handler(channelInitializer);

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
	 * 创建会话
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public Session openSession() throws InterruptedException {
		connect();
		Session session = new Session(null, channel);
		// bind session
		channel.attr(Session.SESSION_KEY).set(session);
		return session;
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

	public static class Builder {
		String host;
		int port;
		ChannelInitializer<SocketChannel> initializer;

		public TcpClient build() {
			return new TcpClient(host, port, initializer);
		}

		public Builder host(String host) {
			this.host = host;
			return this;
		}

		public Builder port(int port) {
			this.port = port;
			return this;
		}

		public Builder handler(ChannelInitializer<SocketChannel> initializer) {
			this.initializer = initializer;
			return this;
		}
	}

	public static Builder builder() {
		return new Builder();
	}
}