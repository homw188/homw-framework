package com.homw.robot.handler;

import java.util.concurrent.TimeUnit;

import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgPacket;
import com.homw.transport.netty.handler.HeartbeatHandler;
import com.homw.transport.netty.session.Session;

import io.netty.channel.ChannelHandlerContext;

/**
 * Heart-Beat handler.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotHeartHandler extends HeartbeatHandler {
	private int seq;
	
	public RobotHeartHandler(long idleTime, TimeUnit unit) {
		super(idleTime, unit);
	}

	@Override
	protected void sendHeartbeatMessage(ChannelHandlerContext ctx) {
		if (seq == Integer.MAX_VALUE) {
			seq = 0;
		}
		MsgPacket heart = MsgFactory.getHeartPacket(seq++);
		Session session = Session.getSession(ctx);
		if (session != null) {
			session.sendOriginal(heart);
		} else {
			// not found session
			ctx.channel().writeAndFlush(heart);
		}
	}
	
}
