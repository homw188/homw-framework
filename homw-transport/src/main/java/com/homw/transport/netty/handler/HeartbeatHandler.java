package com.homw.transport.netty.handler;

import java.util.concurrent.TimeUnit;

import com.homw.transport.netty.message.Message;
import com.homw.transport.netty.message.MessageType;
import com.homw.transport.netty.session.Session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @description 心跳处理器，当进出通道均处于空闲状态时发送心跳
 * @author Hom
 * @version 1.0
 * @since 2020-05-18
 */
public class HeartbeatHandler extends IdleStateHandler {

	public HeartbeatHandler(long idleTime, TimeUnit unit) {
		super(0, 0, idleTime, unit);
	}
	
	@Override
	protected void channelIdle(final ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
		if (IdleState.ALL_IDLE == evt.state()) {
			ctx.executor().execute(new Runnable() {
				@Override
				public void run() {
					sendHeartbeatMessage(ctx);
				}
			});
		} else {
			super.channelIdle(ctx, evt);
		}
	}
	
	protected void sendHeartbeatMessage(ChannelHandlerContext ctx) {
		Message message = new Message();
		message.setMessageId(Message.genMessageId());
		message.setMessageType(MessageType.HEARTBEAT);
		
		Session session = Session.getSession(ctx);
		if (session != null) {
			session.send(message);
		} else {
			// not found session
			ctx.channel().writeAndFlush(message);
		}
	}

}
