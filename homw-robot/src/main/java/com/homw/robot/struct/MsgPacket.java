package com.homw.robot.struct;

import com.homw.common.util.CodecUtil;

import io.netty.buffer.ByteBuf;

/**
 * 数据包
 * 
 * @author Hom
 * @version 1.0
 */
public class MsgPacket {
	/**
	 * 报文头
	 */
	private MsgHead head;

	/**
	 * 数据域
	 */
	private MsgBody body;

	/**
	 * 帧尾
	 */
	private byte[] end;

	/**
	 * 帧尾长度
	 */
	public static final int PACKET_END_LEN = 4;

	/**
	 * 非数据域长度（head + end）
	 */
	public static final int NOT_DATA_LEN = 24;

	public MsgPacket() {
		super();
	}

	public MsgPacket(MsgHead head, MsgBody body, int end) {
		super();
		this.head = head;
		this.body = body;
	}

	public MsgHead getHead() {
		return head;
	}

	public void setHead(MsgHead head) {
		this.head = head;
	}

	public MsgBody getBody() {
		return body;
	}

	public void setBody(MsgBody body) {
		this.body = body;
	}

	public byte[] getEnd() {
		return end;
	}

	public void setEnd(byte[] end) {
		this.end = end;
	}

	/**
	 * write packet to byte buffer.
	 * 
	 * @param buf
	 */
	public void writeToBuffer(ByteBuf buf) {
		writeHeadToBuffer(buf);

		body.writeToBuffer(buf);

		// write packet end to buffer
		buf.writeBytes(this.end);
	}

	/**
	 * write packet head to byte buffer.
	 * 
	 * @param msg
	 * @param out
	 */
	private void writeHeadToBuffer(ByteBuf out) {
		out.writeBytes(head.getHead());
		out.writeShort(head.getLen());
		out.writeInt(head.getRobot_id());
		out.writeInt(head.getTime().getSecs());
		out.writeInt(head.getTime().getNsecs());
		out.writeInt(head.getType().getValue());
	}

	/**
	 * refresh packet length.
	 */
	public void refreshLength() {
		head.setLen((short) (NOT_DATA_LEN + body.getLength()));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[MsgPacket] -> Head=");
		sb.append(head);
		sb.append(" & body=");
		sb.append(body);
		sb.append(" & end=");
		sb.append(CodecUtil.encodeHex(end));
		return sb.toString();
	}

}
