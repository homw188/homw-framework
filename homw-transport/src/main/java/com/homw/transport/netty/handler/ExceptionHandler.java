package com.homw.transport.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description 异常处理器
 * @author Hom
 * @version 1.0
 * @since 2020-05-18
 */
public class ExceptionHandler extends ChannelDuplexHandler {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("caught exception: ", cause);
		// super.exceptionCaught(ctx, cause);
		ctx.close();
	}
}
