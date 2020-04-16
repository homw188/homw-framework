package com.homw.robot.struct;

import com.homw.common.util.CodecUtil;
import com.homw.robot.struct.base.Stamp;

/**
 * 报文头
 * 
 * @author Hom
 * @version 1.0
 */
public class MsgHead {
	/**
	 * 帧头
	 */
	private byte[] head;

	/**
	 * 数据包总长度：(帧头+数据域+帧尾)的字节长度
	 */
	private short len;

	/**
	 * 机器人ID
	 */
	private int robot_id;

	/**
	 * 时间戳/命令ID
	 */
	private Stamp time;

	/**
	 * 数据类型
	 */
	private MsgType type;

	public MsgHead() {
		super();
	}

	public MsgHead(short len, int robot_id, Stamp time, MsgType type) {
		super();
		this.len = len;
		this.robot_id = robot_id;
		this.time = time;
		this.type = type;
	}

	public byte[] getHead() {
		return head;
	}

	public void setHead(byte[] head) {
		this.head = head;
	}

	public short getLen() {
		return len;
	}

	public void setLen(short len) {
		this.len = len;
	}

	public int getRobot_id() {
		return robot_id;
	}

	public void setRobot_id(int robot_id) {
		this.robot_id = robot_id;
	}

	public MsgType getType() {
		return type;
	}

	public void setType(MsgType type) {
		this.type = type;
	}

	public Stamp getTime() {
		return time;
	}

	public void setTime(Stamp time) {
		this.time = time;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(head=");
		sb.append(CodecUtil.bytesToHex(head));
		sb.append(",len=");
		sb.append(len);
		sb.append(",robot_id=");
		sb.append(robot_id);
		sb.append(",time=");
		sb.append(time);
		sb.append(",type=");
		sb.append(type);
		sb.append(")");
		return sb.toString();
	}

}
