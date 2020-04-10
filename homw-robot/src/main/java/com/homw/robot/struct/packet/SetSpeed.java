package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Pose2D;

import io.netty.buffer.ByteBuf;

/**
 * 设置机器人速度
 * 
 * @author Hom
 * @version 1.0
 */
public class SetSpeed extends MsgBody {
	public SetSpeed() {
		type = MsgType.TYPE_SPEED;
		len = 3 * 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("设置机器人速度数据为空");
			buf.writeZero(len);
		} else {
			buf.writeFloat(data.getX());
			buf.writeFloat(data.getY());
			buf.writeFloat(data.getR());
		}
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		data = new Pose2D();
		data.setX(buf.readFloat());
		data.setY(buf.readFloat());
		data.setR(buf.readFloat());
	}

	private Pose2D data;

	public SetSpeed(Pose2D data) {
		this();
		this.data = data;
	}

	public Pose2D getData() {
		return data;
	}

	public void setData(Pose2D data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "(data=" + data + ")";
	}

}
