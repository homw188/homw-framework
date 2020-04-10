package com.homw.test.rpc.server;

import java.util.Map;

import com.homw.test.rpc.api.bean.RpcMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description 自定义rpc消息处理器
 * @author Hom
 * @version 1.0
 * @since 2019-10-11
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcMessage> {
	private Map<String, Object> providerMap;

	public RpcServerHandler(Map<String, Object> providerMap) {
		this.providerMap = providerMap;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcMessage msg) throws Exception {
		Object result = "未找到rpc接口";
		String className = msg.getClassName();
		Class<?> clazz = Class.forName(className);
		if (providerMap.containsKey(className)) {
			// 执行rpc调用
			result = clazz.getMethod(msg.getMethodName(), msg.getParamTypes()).invoke(
					providerMap.get(className), msg.getParams());
		}

		// 将调用结果发送给客户端
		ctx.writeAndFlush(result);
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
