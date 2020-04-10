package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;

import io.netty.buffer.ByteBuf;

/**
 * 心跳包
 * 
 * @author Hom
 * @version 1.0
 */
public class Heart extends MsgBody {
	public Heart() {
		type = MsgType.TYPE_HEART;
		len = 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		buf.writeInt(seq);
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		seq = buf.readInt();
	}

	/**
	 * 序列号，每次发送自动加1，达到最大值后置0
	 */
	private int seq;

	public Heart(int seq) {
		this();
		this.seq = seq;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "(seq=" + seq + ")";
	}

}
