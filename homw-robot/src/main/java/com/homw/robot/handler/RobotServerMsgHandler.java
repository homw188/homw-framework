package com.homw.robot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.robot.struct.JobStateCode;
import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgHead;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.packet.Response;
import com.homw.robot.util.ProtocolConstant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;

/**
 * Message packet server handler.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotServerMsgHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(RobotServerMsgHandler.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MsgPacket packet = (MsgPacket) msg;
		if (packet == null) {
			return;
		}

		MsgHead head = packet.getHead();
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

			ctx.writeAndFlush(resp);
		} else if (head.getType() == MsgType.TYPE_HEART) {
			ctx.writeAndFlush(msg);// echo
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof WriteTimeoutException) {
			logger.info("写数据超时：" + ProtocolConstant.WRITE_TIME_OUT + " 毫秒，服务端连接可能已断开");
		} else if (cause instanceof ReadTimeoutException) {
			logger.info("读数据超时：" + ProtocolConstant.READ_TIME_OUT + " 毫秒，服务端连接可能已断开");
		} else if (cause instanceof ConnectTimeoutException) {
			logger.info("TCP连接超时：" + ProtocolConstant.CONNECT_TIME_OUT + " 毫秒，服务端可能已断开");
		} else {
			cause.printStackTrace();
		}
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
	}

}
