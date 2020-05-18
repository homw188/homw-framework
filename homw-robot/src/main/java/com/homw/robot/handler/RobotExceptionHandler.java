package com.homw.robot.handler;

import com.homw.robot.util.ProtocolConstant;
import com.homw.transport.netty.handler.ExceptionHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;

/**
 * Exception handler.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotExceptionHandler extends ExceptionHandler {
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof WriteTimeoutException) {
			logger.info("写数据超时：" + ProtocolConstant.WRITE_TIME_OUT + " 毫秒，服务端连接可能已断开");
		} else if (cause instanceof ReadTimeoutException) {
			logger.info("读数据超时：" + ProtocolConstant.READ_TIME_OUT + " 毫秒，服务端连接可能已断开");
		} else if (cause instanceof ConnectTimeoutException) {
			logger.info("TCP连接超时：" + ProtocolConstant.CONNECT_TIME_OUT + " 毫秒，服务端可能已断开");
		} else {
			cause.printStackTrace();
		}
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("连接失效，断开");
		ctx.close();
	}

}
