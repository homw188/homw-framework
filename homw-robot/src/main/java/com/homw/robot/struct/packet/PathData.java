package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Pose;
import com.homw.robot.struct.base.Quaternion;
import com.homw.robot.struct.base.Vector3f;

import io.netty.buffer.ByteBuf;

/**
 * 机器人规划路径
 * 
 * @author Hom
 * @version 1.0
 */
public class PathData extends MsgBody {
	public PathData() {
		type = MsgType.TYPE_PATH;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("机器人规划路径数据为空");
			buf.writeZero(len);
		} else {
			for (Pose p : data) {
				if (p == null) {
					logger.info("机器人规划路径中发现为空数据");
					buf.writeZero(7 * 4);
				} else {
					p.writeToBuffer(buf, "机器人规划路径");
				}
			}
		}
	}

	@Override
	public int getLength() {
		if (data != null) {
			len = 7 * 4 * data.length;
		}
		return super.getLength();
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		if (dataLen > 0) {
			int unit = 28;
			Vector3f p;
			Quaternion q;
			data = new Pose[dataLen / unit];
			for (int i = 0; i < dataLen; i += unit) {
				p = new Vector3f();
				p.setX(buf.readFloat());
				p.setY(buf.readFloat());
				p.setZ(buf.readFloat());

				q = new Quaternion();
				q.set(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());

				data[i / unit] = new Pose(p, q);
			}
		}
	}

	/**
	 * 规划路径包含一系列位姿点，由于SIZE 大小不固定，运行时确定结构体类型或字节组装
	 */
	private Pose[] data;

	public PathData(Pose[] data) {
		this();
		this.data = data;
	}

	public Pose[] getData() {
		return data;
	}

	public void setData(Pose[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(data=");
		sb.append("[");
		for (Pose p : data) {
			sb.append(p.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		sb.append(")");
		return sb.toString();
	}

}
