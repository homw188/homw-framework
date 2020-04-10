package com.homw.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.robot.codec.RobotMsgDecoder;
import com.homw.robot.codec.RobotMsgEncoder;
import com.homw.robot.handler.RobotServerMsgHandler;
import com.homw.robot.util.BootstrapFactory;
import com.homw.robot.util.ProtocolConstant;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * Server bootstrap.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotDataServer {
	protected static final Logger logger = LoggerFactory.getLogger(RobotDataServer.class);

	private EventLoopGroup parentGroup;
	private EventLoopGroup childGroup;

	/**
	 * start server.
	 * 
	 * @param port
	 */
	public void bind(int port) throws InterruptedException {
		parentGroup = new NioEventLoopGroup();
		childGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = BootstrapFactory.getServerBootstrap(parentGroup, childGroup,
				getDefaultChannelInitialer());

		ChannelFuture future = bootstrap.bind(port).sync();
		logger.info("服务[ " + port + " ]启动成功...");

		BootstrapFactory.addShutdownHook(parentGroup, childGroup);
		future.channel().closeFuture().sync();
	}

	/**
	 * create default channel initializer.
	 * 
	 * @return
	 */
	private static ChannelInitializer<SocketChannel> getDefaultChannelInitialer() {
		return new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast("messageDecoder", new RobotMsgDecoder(ProtocolConstant.MAX_FRAME_LENGTH, 2, 2));
				ch.pipeline().addLast("messageEncoder", new RobotMsgEncoder());
				ch.pipeline().addLast("msgPacketHandler", new RobotServerMsgHandler());
			}
		};
	}
}