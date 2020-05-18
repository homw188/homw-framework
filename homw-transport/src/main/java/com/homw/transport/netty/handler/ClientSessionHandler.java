package com.homw.transport.netty.handler;

import org.apache.commons.lang3.StringUtils;

import com.homw.transport.netty.message.Message;
import com.homw.transport.netty.session.Session;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description session handler
 * @author Hom
 * @version 1.0
 * @since 2020-04-21
 */
@ChannelHandler.Sharable
public class ClientSessionHandler extends SimpleChannelInboundHandler<Message> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		Session session = ctx.channel().attr(Session.SESSION_KEY).get();
		// first time setting
		if (session != null && session.getSessionId() == null) {
			String sessionId = msg.getSessionId();
			if (StringUtils.isNotEmpty(sessionId)) {
				session.setSessionId(sessionId);
			}
		}
		
		// fire next handler
		ctx.fireChannelRead(msg);
	}

}
