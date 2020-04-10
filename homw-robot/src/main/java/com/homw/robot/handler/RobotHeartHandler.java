package com.homw.robot.handler;

import java.util.concurrent.TimeUnit;

import com.homw.robot.struct.MsgHead;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.MsgType;
import com.homw.robot.task.RobotHeartTask;
import com.homw.robot.util.ProtocolConstant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Heart-Beat handler.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotHeartHandler extends ChannelInboundHandlerAdapter {
	private RobotHeartTask heartTask;

	public RobotHeartHandler() {
		heartTask = new RobotHeartTask();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		heartTask.setChannel(ctx.channel());
		super.channelRegistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.executor().scheduleAtFixedRate(heartTask, 0, ProtocolConstant.HEART_BEAT_RATIO, TimeUnit.MILLISECONDS);
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MsgPacket packet = (MsgPacket) msg;
		if (packet == null) {
			return;
		}

		MsgHead head = packet.getHead();
		if (head.getType() == MsgType.TYPE_HEART) {
			heartTask.setTimer(System.currentTimeMillis());
		} else {
			super.channelRead(ctx, msg);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		heartTask.clear();
		super.channelInactive(ctx);
	}

}
