package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;

import io.netty.buffer.ByteBuf;

/**
 * 机器人设置
 * 
 * @author Hom
 * @version 1.0
 */
public class Set extends MsgBody {
	public Set() {
		type = MsgType.TYPE_SET;
		len = size;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (str == null) {
			logger.info("机器人设置数据为空");
			buf.writeZero(size);
		} else {
			if (str.length != size) {
				logger.info("机器人设置数据长度不一致，固定长度：" + size);

				if (str.length > size) {
					for (int i = 0; i < size; i++)// truncate
					{
						buf.writeByte(str[i]);
					}
				} else {
					for (byte c : str) {
						buf.writeByte(c);
					}
					buf.writeZero(size - str.length);
				}
			} else {
				for (byte c : str) {
					buf.writeByte(c);
				}
			}
		}
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		if (dataLen > 0) {
			int unit = 1;
			int idx = 0;
			str = new byte[size];
			for (int i = 0; i < dataLen; i += unit) {
				idx = i / unit;
				if (idx > (size - 1)) {
					break;
				}
				str[idx] = buf.readByte();
			}
		}
	}

	/**
	 * 根据字符串命令设置相关项
	 */
	private byte[] str;
	public static final int size = 128;

	public Set(byte[] str) {
		this();
		this.str = str;
	}

	public byte[] getStr() {
		return str;
	}

	public void setStr(byte[] str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return "(str=" + new String(str).trim() + ")";
	}

}
