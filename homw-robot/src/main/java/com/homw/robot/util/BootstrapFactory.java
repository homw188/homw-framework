package com.homw.robot.util;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Bootstrap factory.
 * 
 * @author Hom
 * @version 1.0
 */
public final class BootstrapFactory {
	private BootstrapFactory() {
	}

	/**
	 * get configured bootstrap.
	 * 
	 * @param eventLoop
	 * @param channelIntialer
	 * @return
	 */
	public static Bootstrap getBootstrap(EventLoopGroup eventLoop, ChannelInitializer<SocketChannel> channelIntialer) {
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(eventLoop);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024));
		bootstrap.option(ChannelOption.SO_SNDBUF, ProtocolConstant.SEND_BUFFER);
		bootstrap.option(ChannelOption.SO_RCVBUF, ProtocolConstant.RECEIVE_BUFFER);
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ProtocolConstant.CONNECT_TIME_OUT);

		if (channelIntialer != null) {
			bootstrap.handler(channelIntialer);
		}

		return bootstrap;
	}

	/**
	 * get configured server bootstrap.
	 * 
	 * @param eventLoop
	 * @param channelIntialer
	 * @return
	 */
	public static ServerBootstrap getServerBootstrap(EventLoopGroup parentGroup, EventLoopGroup childGroup,
			ChannelInitializer<SocketChannel> channelIntialer) {
		ServerBootstrap bootstrap = new ServerBootstrap();

		if (parentGroup == null) {
			bootstrap.group(childGroup);
		} else {
			bootstrap.group(parentGroup, childGroup);
		}
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);

		if (channelIntialer != null) {
			bootstrap.childHandler(channelIntialer);
		}
		return bootstrap;
	}

	/**
	 * get configured UDP bootstrap.
	 * 
	 * @param eventLoop
	 * @param channelIntialer
	 * @return
	 */
	public static Bootstrap getUdpBootstrap(EventLoopGroup eventLoop,
			ChannelInitializer<NioDatagramChannel> channelIntialer) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoop);
		bootstrap.channel(NioDatagramChannel.class);
		bootstrap.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(60024));
		bootstrap.option(ChannelOption.SO_RCVBUF, ProtocolConstant.RECEIVE_BUFFER);
		bootstrap.option(ChannelOption.SO_SNDBUF, ProtocolConstant.SEND_BUFFER);
		bootstrap.option(ChannelOption.SO_BROADCAST, true);

		if (channelIntialer != null) {
			bootstrap.handler(channelIntialer);
		}

		return bootstrap;
	}

	/**
	 * shut down event loop, while exit JVM.
	 * 
	 * @param groups
	 */
	public static void addShutdownHook(final EventLoopGroup... groups) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// release resources
				for (EventLoopGroup group : groups) {
					group.shutdownGracefully();
				}
			}
		});
	}

}
