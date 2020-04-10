package com.homw.robot.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.robot.struct.MsgHead;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.MsgType;
import com.homw.robot.util.ProtocolConstant;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ConnectTimeoutException;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;

/**
 * Message packet handler.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotClientMsgHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(RobotClientMsgHandler.class);
	private Channel channel;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.channel = ctx.channel();
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		MsgPacket packet = (MsgPacket) msg;
		if (packet == null) {
			return;
		}

		MsgHead head = packet.getHead();
		if (head.getType() != MsgType.TYPE_HEART) {
			System.out.println(packet);
		} else {
			super.channelRead(ctx, msg);
			System.out.println(packet);
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
		super.channelInactive(ctx);
	}

	/**
	 * send command to server.
	 * 
	 * @param cmd
	 * @return
	 */
	public boolean sendCmd(MsgPacket cmd) {
		if (channel != null && cmd != null) {
			if (channel.isActive()) {
				channel.writeAndFlush(cmd);
				return true;
			}
		}
		return false;
	}

}
