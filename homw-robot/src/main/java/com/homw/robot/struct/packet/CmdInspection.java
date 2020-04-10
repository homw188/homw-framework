package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;

import io.netty.buffer.ByteBuf;

/**
 * 机器人巡检任务命令
 * 
 * @author Hom
 * @version 1.0
 */
public class CmdInspection extends MsgBody {
	public CmdInspection() {
		type = MsgType.TYPE_CMD_INSPECTION;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("机器人巡检任务命令数据为空");
			buf.writeZero(len);
		} else {
			for (int p : data) {
				buf.writeInt(p);
			}
		}
	}

	@Override
	public int getLength() {
		if (data != null) {
			len = 4 * data.length;
		}
		return super.getLength();
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		if (dataLen > 0) {
			int unit = 4;
			data = new int[dataLen / unit];
			for (int i = 0; i < dataLen; i += unit) {
				data[i / unit] = buf.readInt();
			}
		}
	}

	/**
	 * 巡检任务ID列表
	 */
	private int[] data;

	public CmdInspection(int[] data) {
		this();
		this.data = data;
	}

	public int[] getData() {
		return data;
	}

	public void setData(int[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(data=");
		sb.append("[");
		for (int p : data) {
			sb.append(String.valueOf(p));
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		sb.append(")");
		return sb.toString();
	}

}
