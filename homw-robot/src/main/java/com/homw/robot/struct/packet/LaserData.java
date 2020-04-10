package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.Vector3f;

import io.netty.buffer.ByteBuf;

/**
 * 点云数据
 * 
 * @author Hom
 * @version 1.0
 */
public class LaserData extends MsgBody {
	public LaserData() {
		type = MsgType.TYPE_LASER;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("点云数据为空");
			buf.writeZero(len);
		} else {
			for (Vector3f p : data) {
				buf.writeFloat(p.getX());
				buf.writeFloat(p.getY());
				buf.writeFloat(p.getZ());
			}
		}
	}

	@Override
	public int getLength() {
		if (data != null) {
			len = data.length * 3 * 4;
		}
		return len;
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		if (dataLen > 0) {
			int unit = 12;
			Vector3f p;
			data = new Vector3f[dataLen / unit];
			for (int i = 0; i < dataLen; i += unit) {
				p = new Vector3f();
				p.setX(buf.readFloat());
				p.setY(buf.readFloat());
				p.setZ(buf.readFloat());

				data[dataLen / unit] = p;
			}
		}
	}

	/**
	 * 为减少点云传输量，处理后点云发送大小不固定
	 */
	private Vector3f[] data;

	public LaserData(Vector3f[] data) {
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
		return "(data=" + data + ")";
	}

}
