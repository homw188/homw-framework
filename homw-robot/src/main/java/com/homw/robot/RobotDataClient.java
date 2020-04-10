package com.homw.robot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.robot.handler.RobotChannelInitialer;
import com.homw.robot.handler.RobotClientMsgHandler;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.util.BootstrapFactory;
import com.homw.robot.util.ProtocolConstant;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Client bootstrap.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotDataClient {
	protected static final Logger logger = LoggerFactory.getLogger(RobotDataClient.class);

	private Channel channel;
	private EventLoopGroup eventLoop;
	private RobotChannelInitialer channelIntialezer;

	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

	/**
	 * connect to server.
	 * 
	 * @param ip
	 * @param port
	 */
	public void connect(final String ip, final int port) throws InterruptedException {
		try {
			eventLoop = new NioEventLoopGroup();
			channelIntialezer = new RobotChannelInitialer();
			Bootstrap bootstrap = BootstrapFactory.getBootstrap(eventLoop, channelIntialezer);

			ChannelFuture future = bootstrap.connect(ip, port).sync();
			channel = future.channel();
			logger.info("服务[ " + ip + ", " + port + " ]连接成功，客户端程序启动...");

			BootstrapFactory.addShutdownHook(eventLoop);
			future.channel().closeFuture().sync();
		} finally {
			logger.info("服务[ " + ip + ", " + port + " ]连接断开，" + ProtocolConstant.RE_CONNECT_TIME + " 毫秒后将重试连接");

			// release
			eventLoop.shutdownGracefully();

			reconnect(ip, port);
		}
	}

	/**
	 * reconnect to server.
	 * 
	 * @param ip
	 * @param port
	 */
	private void reconnect(final String ip, final int port) {
		executor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					connect(ip, port);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, ProtocolConstant.RE_CONNECT_TIME, TimeUnit.MILLISECONDS);
	}

	/**
	 * get socket channel.
	 * 
	 * @return
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * send command to server.
	 * 
	 * @param cmd
	 * @return
	 */
	public boolean sendCmd(MsgPacket cmd) {
		if (channel == null) {
			return false;
		}

		RobotClientMsgHandler packetHandler = channel.pipeline().get(RobotClientMsgHandler.class);
		if (packetHandler != null) {
			return packetHandler.sendCmd(cmd);
		}
		return false;
	}

}
