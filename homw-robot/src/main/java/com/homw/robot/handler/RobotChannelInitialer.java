package com.homw.robot.handler;

import java.util.concurrent.TimeUnit;

import com.homw.robot.codec.RobotMsgDecoder;
import com.homw.robot.codec.RobotMsgEncoder;
import com.homw.robot.util.ProtocolConstant;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

/**
 * Initialize channel handler.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotChannelInitialer extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("messageDecoder", new RobotMsgDecoder(ProtocolConstant.MAX_FRAME_LENGTH, 2, 2));
		ch.pipeline().addLast("messageEncoder", new RobotMsgEncoder());

		ch.pipeline().addLast("readTimeoutHandler",
				new ReadTimeoutHandler(ProtocolConstant.READ_TIME_OUT, TimeUnit.MILLISECONDS));
		ch.pipeline().addLast("writeTimeoutHandler",
				new WriteTimeoutHandler(ProtocolConstant.WRITE_TIME_OUT, TimeUnit.MILLISECONDS));
		
		ch.pipeline().addLast("exceptionHandler", new RobotExceptionHandler());
		ch.pipeline().addLast("heartBeatHandler", new RobotHeartHandler(60, TimeUnit.SECONDS));
		ch.pipeline().addLast("msgPacketHandler", new RobotClientMsgHandler());
	}
}
