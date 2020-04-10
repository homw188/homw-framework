package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;

import io.netty.buffer.ByteBuf;

/**
 * 机器人命令
 * 
 * @author Hom
 * @version 1.0
 */
public class Cmd extends MsgBody {
	public Cmd() {
		type = MsgType.TYPE_CMD;
		len = size;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (str == null) {
			logger.info("机器人命令为空");
			buf.writeZero(size);
		} else {
			if (str.length != size) {
				logger.info("机器人命令长度不一致，固定长度：" + size);

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
			int idx = 0; // array index
			str = new byte[size];
			for (int i = 0; i < dataLen; i += unit) {
				idx = i / unit;
				if (idx > (size - 1)) // truncate
				{
					break;
				}
				str[idx] = buf.readByte();
			}
		}
	}

	/**
	 * 其他字符串指令, c++[char]=java[byte], (unsigned char=char)
	 */
	private byte[] str;
	public static final int size = 128;

	public Cmd(byte[] str) {
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
