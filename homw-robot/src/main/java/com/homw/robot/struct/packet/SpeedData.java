package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Pose2D;

import io.netty.buffer.ByteBuf;

/**
 * 机器人速度
 * 
 * @author Hom
 * @version 1.0
 */
public class SpeedData extends MsgBody {
	public SpeedData() {
		type = MsgType.TYPE_SPEED;
		len = 3 * 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("机器人速度数据为空");
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

	/**
	 * 机器人速度包括3个分量，x 轴向速度分量m/s， y 横向速度分量m/s，r自转角速度rad/s
	 */
	private Pose2D data;

	public SpeedData(Pose2D data) {
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
