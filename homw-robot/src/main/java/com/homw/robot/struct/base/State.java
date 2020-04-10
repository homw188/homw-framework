package com.homw.robot.struct.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

/**
 * 机器人状态
 * 
 * @author Hom
 * @version 1.0
 */
public class State {
	private static final Logger logger = LoggerFactory.getLogger(State.class);

	private int stateCode;
	private byte[] str;
	public static final int size = 128;

	public int getStateCode() {
		return stateCode;
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	public byte[] getStr() {
		return str;
	}

	public void setStr(byte[] str) {
		this.str = str;
	}

	/**
	 * write to byte buffer.
	 * 
	 * @param buf
	 * @param type
	 */
	public void writeToBuffer(ByteBuf buf, String type) {
		buf.writeInt(stateCode);

		if (str == null) {
			logger.info(type + "状态描述信息为空");
			buf.writeZero(State.size);
		} else {
			if (str.length != State.size) {
				logger.info(type + "状态描述信息长度不一致，固定长度：" + State.size);

				if (str.length > State.size) {
					for (int i = 0; i < State.size; i++)// truncate
					{
						buf.writeByte(str[i]);
					}
				} else {
					for (byte c : str) {
						buf.writeByte(c);
					}
					buf.writeZero(State.size - str.length);
				}
			} else {
				for (byte c : str) {
					buf.writeByte(c);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "(stateCode=" + stateCode + ",str=" + new String(str).trim() + ")";
	}

}
