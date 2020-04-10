package com.homw.robot.codec;

import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.robot.struct.MsgPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Message packet encode.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotMsgEncoder extends MessageToByteEncoder<MsgPacket> {
	private static final Logger logger = LoggerFactory.getLogger(RobotMsgEncoder.class);

	@Override
	@SuppressWarnings("deprecation")
	protected void encode(ChannelHandlerContext ctx, MsgPacket msg, ByteBuf out) throws Exception {
		if (msg == null) {
			logger.error("发送报文为空");
			return;
		}

		// set little endian mode to compatible width c/c++ server.
		out = out.order(ByteOrder.LITTLE_ENDIAN);
		msg.writeToBuffer(out);

		if (out.readableBytes() != msg.getHead().getLen()) {
			logger.warn("发送数据包长度不一致: " + msg.getHead().getLen());
		}
	}
}
