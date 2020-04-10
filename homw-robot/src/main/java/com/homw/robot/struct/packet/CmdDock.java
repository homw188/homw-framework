package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;

import io.netty.buffer.ByteBuf;

/**
 * 去停靠点命令
 * 
 * @author Hom
 * @version 1.0
 */
public class CmdDock extends MsgBody {
	public CmdDock() {
		type = MsgType.TYPE_CMD_DOCK;
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
	 * 停靠点ID
	 */
	private int data;

	public CmdDock(int data) {
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
