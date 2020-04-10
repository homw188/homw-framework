package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Pose;
import com.homw.robot.struct.base.Quaternion;
import com.homw.robot.struct.base.Vector3f;

import io.netty.buffer.ByteBuf;

/**
 * 点哪去哪
 * 
 * @author Hom
 * @version 1.0
 */
public class CmdGo extends MsgBody {
	public CmdGo() {
		type = MsgType.TYPE_CMD_GO;
		len = 7 * 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("点哪去哪数据为空");
			buf.writeZero(len);
		} else {
			data.writeToBuffer(buf, "点哪去哪");
		}
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		Vector3f p = new Vector3f();
		p.setX(buf.readFloat());
		p.setY(buf.readFloat());
		p.setZ(buf.readFloat());

		Quaternion q = new Quaternion();
		q.set(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());

		data = new Pose(p, q);
	}

	/**
	 * 点哪去哪，发送机器人到达点及角度
	 */
	private Pose data;

	public CmdGo(Pose data) {
		this();
		this.data = data;
	}

	public Pose getData() {
		return data;
	}

	public void setData(Pose data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "(data=" + data + ")";
	}

}
