package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Vector3f;

import io.netty.buffer.ByteBuf;

/**
 * 指哪看哪
 * 
 * @author Hom
 * @version 1.0
 */
public class CmdObserve extends MsgBody {
	public CmdObserve() {
		type = MsgType.TYPE_CMD_OBSERVE;
		len = size * 3 * 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("指哪看哪数据为空");
			buf.writeZero(len);
		} else {
			if (data.length != size) {
				logger.info("指哪看哪数据长度不一致，固定长度：" + size);

				if (data.length > size) {
					for (int i = 0; i < size; i++)// truncate
					{
						buf.writeFloat(data[i].getX());
						buf.writeFloat(data[i].getY());
						buf.writeFloat(data[i].getZ());
					}
				} else {
					for (Vector3f p : data) {
						buf.writeFloat(p.getX());
						buf.writeFloat(p.getY());
						buf.writeFloat(p.getZ());
					}
					buf.writeZero((size - data.length) * 3 * 4);
				}
			} else {
				for (Vector3f p : data) {
					buf.writeFloat(p.getX());
					buf.writeFloat(p.getY());
					buf.writeFloat(p.getZ());
				}
			}
		}
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		if (dataLen > 0) {
			int unit = 12;
			int idx = 0;
			Vector3f p;
			data = new Vector3f[size];
			for (int i = 0; i < dataLen; i += unit) {
				idx = i / unit;
				if (idx > (size - 1)) {
					break;
				}

				p = new Vector3f();
				p.setX(buf.readFloat());
				p.setY(buf.readFloat());
				p.setZ(buf.readFloat());

				data[idx] = p;
			}
		}
	}

	/**
	 * 哪看哪，发送机器人需观测目标位置(X,Y,Z)及用户操作时的视角相机位置(X,Y,Z), 同一坐标系下；
	 * 注：因为只知道目标位置的情况下，机器人不知道应该从哪个方向看，比如前面还是后面，而知道用户视角的位置后，
	 * 可以判断从前面看还是后面，方便计算机器人车载相机的准确放置位置和方向。
	 */
	private Vector3f[] data;
	public static final int size = 2;

	public CmdObserve(Vector3f[] data) {
		this();
		this.data = data;
	}

	public Vector3f[] getData() {
		return data;
	}

	public void setData(Vector3f[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(data=");
		sb.append("[");
		for (Vector3f p : data) {
			sb.append(p.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		sb.append(")");
		return sb.toString();
	}

}
