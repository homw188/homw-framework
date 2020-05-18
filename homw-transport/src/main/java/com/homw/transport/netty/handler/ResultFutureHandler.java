package com.homw.transport.netty.handler;

import org.apache.commons.lang3.StringUtils;

import com.homw.transport.netty.message.Message;
import com.homw.transport.netty.session.ResultFuture;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * @description This handler use to notify future result. <br>
 *              <b>Base on the same message id which bind to channel.</b>
 * @author Hom
 * @version 1.0
 * @since 2020-05-18
 */
@ChannelHandler.Sharable
public class ResultFutureHandler extends SimpleChannelInboundHandler<Message> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		if (msg != null && StringUtils.isNotEmpty(msg.getMessageId())) {
			AttributeKey<ResultFuture<Message>> messageKey = Message.getMessageKey(msg.getMessageId());
			ResultFuture<Message> future = ctx.channel().attr(messageKey).get();
			if (future != null) {
				future.set(msg);
			}
		}
		
		// fire next handler
		ctx.fireChannelRead(msg);
	}

}
