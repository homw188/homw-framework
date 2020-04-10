package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.State;

import io.netty.buffer.ByteBuf;

/**
 * 机器人电量
 * 
 * @author Hom
 * @version 1.0
 */
public class BatteryData extends MsgBody {
	public BatteryData() {
		type = MsgType.TYPE_BATTERY;
		len = (State.size + 4) + 3 * 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (data == null) {
			logger.info("机器人电量数据为空");
			buf.writeZero(State.size + 4);
		} else {
			data.writeToBuffer(buf, "电量");
		}

		buf.writeFloat(power);
		buf.writeFloat(time);
		buf.writeFloat(voltage);
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		data = new State();
		data.setStateCode(buf.readInt());

		int size = dataLen - 16;
		if (size > 0) {
			int unit = 1;
			int idx = 0; // array index
			byte[] str = new byte[State.size];
			for (int i = 0; i < size; i += unit) {
				idx = i / unit;
				if (idx > (State.size - 1)) // truncate
				{
					break;
				}
				str[idx] = buf.readByte();
			}
			data.setStr(str);
		}

		power = buf.readFloat();
		time = buf.readFloat();
		voltage = buf.readFloat();
	}

	/**
	 * stateCode：0无充电， 1充电中， -1电池异常； str[] “附加描述信息”
	 */
	private State data;

	/**
	 * 剩余容量百分比 0～100
	 */
	private float power;

	/**
	 * 剩余电量可工作时长估计 单位分钟
	 */
	private float time;

	/**
	 * 电压
	 */
	private float voltage;

	public BatteryData(State data, float power, float time, float voltage) {
		this();
		this.data = data;
		this.power = power;
		this.time = time;
		this.voltage = voltage;
	}

	public State getData() {
		return data;
	}

	public void setData(State data) {
		this.data = data;
	}

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		this.power = power;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getVoltage() {
		return voltage;
	}

	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}

	@Override
	public String toString() {
		return "(data=" + data + ",power=" + power + ",time=" + time + ",voltage=" + voltage + ")";
	}

}
