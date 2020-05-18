package com.homw.robot.handler;

import com.homw.robot.struct.MsgPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Message packet handler.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotClientMsgHandler extends SimpleChannelInboundHandler<MsgPacket> {

	@Override
	public void channelRead0(ChannelHandlerContext ctx, MsgPacket msg) throws Exception {
		if (msg == null) {
			return;
		}
		System.out.println(msg);
	}

}
