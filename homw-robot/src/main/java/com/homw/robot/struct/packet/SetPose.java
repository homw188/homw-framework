package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Pose;
import com.homw.robot.struct.base.Quaternion;
import com.homw.robot.struct.base.Vector3f;

import io.netty.buffer.ByteBuf;

/**
 * 设置机器人位姿
 * 
 * @author Hom
 * @version 1.0
 */
public class SetPose extends MsgBody {
	public SetPose() {
		type = MsgType.TYPE_POSE;
		len = 7 * 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("设置机器人位姿数据为空");
			buf.writeZero(len);
		} else {
			data.writeToBuffer(buf, "设置机器人位姿");
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

	private Pose data;

	public SetPose(Pose data) {
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
