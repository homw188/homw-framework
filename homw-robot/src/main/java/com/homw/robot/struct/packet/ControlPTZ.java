package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Pose;
import com.homw.robot.struct.base.Quaternion;
import com.homw.robot.struct.base.Vector3f;

import io.netty.buffer.ByteBuf;

/**
 * 远程遥控升降和云台控制
 * 
 * @author Hom
 * @version 1.0
 */
public class ControlPTZ extends MsgBody {
	public ControlPTZ() {
		type = MsgType.TYPE_CONTROL_PTZ;
		len = 7 * 4 + 2;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("云台控制数据为空");
			buf.writeZero(len);
		} else {
			data.writeToBuffer(buf, "云台控制");
			buf.writeChar(mode);
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
		mode = buf.readByte();
	}

	/**
	 * 使用pose类型存储云台升降控制量 pose中orientation代表云台姿态角控制含义，目前室内型不支持这个控制，因此不会响应命令。
	 * 室外型机器人如果安装了云台则可以响应该命令，实现姿态控制。 pose中posiztion 代表
	 * 升降机构控制含义，其中定义z升降高度，x伸缩结构深度，y，机构平移距离，目前仅室内机器装有升降机构时支持响应该命令，目前只支持升降高度z控制，其他设置不响应
	 */
	private Pose data;

	/**
	 * ‘a 代表 绝对控制(absolute control),升降机构在参考坐标系下的绝对高度坐标。 ‘r’代表增量控制(relative
	 * control),升降机构相对当前位置的运动高度，+代表上运动，-代表向下运动。
	 */
	private byte mode;

	public ControlPTZ(Pose data, byte mode) {
		this();
		this.data = data;
		this.mode = mode;
	}

	public Pose getData() {
		return data;
	}

	public void setData(Pose data) {
		this.data = data;
	}

	public byte getMode() {
		return mode;
	}

	public void setMode(byte mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return "(data=" + data + ",mode=" + mode + ")";
	}

}
