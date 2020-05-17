package com.homw.transport.netty.session;

import com.homw.transport.netty.message.Message;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;

/**
 * @description session handler
 * @author Hom
 * @version 1.0
 * @since 2020-04-21
 */
@ChannelHandler.Sharable
public class ServerSessionHandler extends SimpleChannelInboundHandler<Message> {
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		Attribute<Session> attr = ctx.channel().attr(Session.SESSION_KEY);
		Session session = attr.get();
		// first time
		if (session == null) {
			String sessionId = Session.genSessionId(ctx);
			session = new Session(sessionId, ctx.channel());
			attr.set(session);
			SessionManager.getInstance().addSession(session);
		}
		
		// fire next handler
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SessionManager.getInstance().removeSession(Session.genSessionId(ctx));
		ctx.channel().close();
	}

}
