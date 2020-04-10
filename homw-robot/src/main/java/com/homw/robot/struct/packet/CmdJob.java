package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;

import io.netty.buffer.ByteBuf;

/**
 * 机器人任务
 * 
 * @author Hom
 * @version 1.0
 */
public class CmdJob extends MsgBody {
	public CmdJob() {
		type = MsgType.TYPE_CMD_JOB;
		len = 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		buf.writeInt(data);
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		data = buf.readInt();
	}

	/**
	 * 0紧急保护, 1自主返航任务, 2自主充电任务. 注：该命令方便设置一些简易任务，比如一键充电，一键急停，一键返航，
	 * 通过在用户界面放置这些按钮等，用户点击后触发该任务；
	 */
	private int data;

	public CmdJob(int data) {
		this();
		this.data = data;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "(data=" + data + ")";
	}

}
