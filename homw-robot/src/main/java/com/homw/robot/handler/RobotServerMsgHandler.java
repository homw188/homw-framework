package com.homw.robot.handler;

import com.homw.robot.struct.JobStateCode;
import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgHead;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.packet.Response;
import com.homw.transport.netty.session.Session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Message packet server handler.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotServerMsgHandler extends SimpleChannelInboundHandler<MsgPacket> {
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, MsgPacket msg) throws Exception {
		if (msg == null) {
			return;
		}

		MsgHead head = msg.getHead();
		Session session = Session.getSession(ctx);
		if (head.getType() != MsgType.TYPE_HEART) {
			// 根据类别获取数据包
			MsgPacket resp = MsgFactory.getPacket(MsgType.TYPE_RESPONSE);

			// 填充包头数据域
			resp.getHead().setRobot_id(0);

			// 填充包体数据域
			Response body = (Response) resp.getBody();
			body.setRes_type(head.getType());
			body.setStamp(head.getTime());
			body.setState(JobStateCode.JOB_SUCCEEDED);

			// 刷新数据包长度
			resp.refreshLength();

			if (session != null) {
				session.sendOriginal(resp);
			} else {
				ctx.writeAndFlush(resp);
			}
		} else if (head.getType() == MsgType.TYPE_HEART) {
			// echo
			if (session != null) {
				session.sendOriginal(msg);
			} else {
				ctx.writeAndFlush(msg);
			}
		}
	}

}
