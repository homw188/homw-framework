package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Pose;
import com.homw.robot.struct.base.Quaternion;
import com.homw.robot.struct.base.Vector3f;

import io.netty.buffer.ByteBuf;

/**
 * 机器人任务
 * 
 * @author Hom
 * @version 1.0
 */
public class CmdJobs extends MsgBody {
	public CmdJobs() {
		type = MsgType.TYPE_CMD_JOBS;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("机器人任务数据为空");
			buf.writeZero(len);
		} else {
			for (Pose p : data) {
				if (p == null) {
					logger.info("机器人任务中发现为空数据");
					buf.writeZero(7 * 4);
				} else {
					p.writeToBuffer(buf, "机器人任务");
				}
			}
		}
	}

	@Override
	public int getLength() {
		if (data != null) {
			len = data.length * 7 * 4;
		}
		return super.getLength();
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		if (dataLen > 0) {
			int unit = 28;
			data = new Pose[dataLen / unit];
			for (int i = 0; i < dataLen; i += unit) {
				Vector3f p = new Vector3f();
				p.setX(buf.readFloat());
				p.setY(buf.readFloat());
				p.setZ(buf.readFloat());

				Quaternion q = new Quaternion();
				q.set(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());

				data[i / unit] = new Pose(p, q);
			}
		}
	}

	/**
	 * 巡检任务发送的要检测的设备或位置数量同样不固定
	 */
	private Pose[] data;

	public CmdJobs(Pose[] data) {
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
