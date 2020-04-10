package com.homw.robot.struct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

/**
 * Message Body.
 * 
 * @author Home
 * @version 1.0
 */
public abstract class MsgBody {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected MsgType type;
	protected int len;

	public final MsgType getType() {
		return type;
	}

	/**
	 * write to byte buffer.
	 * 
	 * @param buf
	 */
	public abstract void writeToBuffer(ByteBuf buf);

	/**
	 * read from byte buffer.
	 * 
	 * @param buf
	 * @param dataLen
	 */
	public abstract void readFromBuffer(ByteBuf buf, int dataLen);

	/**
	 * get body data length.
	 * 
	 * @return
	 */
	public int getLength() {
		return len;
	}

}
