package com.homw.transport.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @description 异常处理器
 * @author Hom
 * @version 1.0
 * @since 2020-05-18
 */
@ChannelHandler.Sharable
public class ExceptionHandler extends ChannelDuplexHandler {
	private static Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.warn("caught exception: ", cause);
		// super.exceptionCaught(ctx, cause);
		ctx.close();
	}
}
