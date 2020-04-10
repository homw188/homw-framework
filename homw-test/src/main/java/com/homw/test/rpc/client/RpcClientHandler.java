package com.homw.test.rpc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description 自定义rpc消息处理器
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<Object> {
	private Object result = null;
	
	public Object getResult() {
		return result;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		result = msg;
		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
