package com.homw.tool.api.kede;

import com.homw.transport.netty.session.ResultFuture;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.ReferenceCountUtil;

public class KedeClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			String data = KedeProtocolUtil.bytesToHexStr((byte[]) msg);
			Attribute<ResultFuture<String>> attr = ctx.channel().attr(KedeNettyClient.DATA_KEY);
			ResultFuture<String> future = attr.get();
			if (future != null) {
				future.set(data);
			}
			ctx.close();
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
