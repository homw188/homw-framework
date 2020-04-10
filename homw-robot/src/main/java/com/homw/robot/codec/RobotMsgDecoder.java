package com.homw.robot.codec;

import java.nio.ByteOrder;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.common.util.CodecUtil;
import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgFactory;
import com.homw.robot.struct.MsgHead;
import com.homw.robot.struct.MsgPacket;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Stamp;
import com.homw.robot.util.ProtocolConstant;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * Message packet decode.
 * 
 * @author Hom
 * @version 1.0
 */
public class RobotMsgDecoder extends LengthFieldBasedFrameDecoder {
	private static final Logger logger = LoggerFactory.getLogger(RobotMsgDecoder.class);

	public RobotMsgDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(ByteOrder.LITTLE_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, -4, 0, true);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf originBuf = (ByteBuf) super.decode(ctx, in);
		if (originBuf == null) {
			return null;
		}

		ByteBuf littleBuf = null;
		try {
			// set little endian mode to compatible width c/c++ server.
			littleBuf = originBuf.order(ByteOrder.LITTLE_ENDIAN);
			littleBuf.retain();
		} finally {
			if (originBuf.refCnt() > 0) {
				ReferenceCountUtil.release(originBuf);
			}
		}

		MsgPacket msg = null;
		try {
			MsgHead head = getMsgHead(littleBuf);
			msg = new MsgPacket();
			msg.setHead(head);
			msg.setBody(getMsgBody(littleBuf, head));

			byte[] frameEnd = new byte[4];
			for (int i = 0; i < 4; i++) {
				frameEnd[i] = littleBuf.readByte();
			}
			msg.setEnd(frameEnd);
			if (!Arrays.equals(ProtocolConstant.DATA_FRAME_END, frameEnd)) {
				logger.warn("非法数据帧尾: " + CodecUtil.encodeHex(frameEnd));
			}
		} finally {
			if (littleBuf.refCnt() > 0) {
				ReferenceCountUtil.release(littleBuf);
			}
		}
		return msg;
	}

	/**
	 * get message body.
	 * 
	 * @param frame
	 * @param head
	 * @return
	 */
	private MsgBody getMsgBody(ByteBuf frame, MsgHead head) {
		MsgBody body = MsgFactory.getBody(head.getType());

		int dataLen = head.getLen() - MsgPacket.NOT_DATA_LEN;
		if (frame.readableBytes() != (dataLen + 4)) {
			logger.error("数据包长度不一致: " + dataLen);
			return null;
		}
		body.readFromBuffer(frame, dataLen);
		return body;
	}

	/**
	 * get message head.
	 * 
	 * @param frame
	 * @return
	 */
	private MsgHead getMsgHead(ByteBuf frame) {
		MsgHead head = new MsgHead();

		byte[] frameHead = new byte[2];
		for (int i = 0; i < 2; i++) {
			frameHead[i] = frame.readByte();
		}
		head.setHead(frameHead);
		if (!Arrays.equals(ProtocolConstant.DATA_FRAME_HEAD, frameHead)) {
			logger.warn("非法数据帧头: " + CodecUtil.encodeHex(frameHead));
		}

		head.setLen(frame.readShort());
		head.setRobot_id(frame.readInt());
		head.setTime(new Stamp(frame.readInt(), frame.readInt()));

		int type = frame.readInt();
		MsgType msgType = MsgType.getType(type);
		if (msgType == null) {
			logger.error("非法报文类别: " + type);
			return null;
		}
		head.setType(msgType);
		return head;
	}
}
