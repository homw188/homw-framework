package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.State;

import io.netty.buffer.ByteBuf;

/**
 * 机器人电机信息
 * 
 * @author Hom
 * @version 1.0
 */
public class MotorData extends MsgBody {
	public MotorData() {
		type = MsgType.TYPE_MOTOR;
		len = (4 + State.size) * size + 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		for (State s : motor) {
			if (s == null) {
				buf.writeZero(State.size + 4);
			} else {
				s.writeToBuffer(buf, "电机");
			}
		}
		buf.writeFloat(lift);
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		if (dataLen > 0) {
			int len = dataLen - 4;
			int unit = 132;
			int idx = 0;
			State s;
			motor = new State[size];
			for (int i = 0; i < len; i += unit) {
				idx = i / unit;
				if (idx > (size - 1)) {
					break;
				}
				s = new State();
				s.setStateCode(buf.readInt());

				byte[] str = new byte[State.size];
				for (int j = 0; i < State.size; j++) {
					str[j] = buf.readByte();
				}
				motor[idx] = s;
			}
		}
		lift = buf.readFloat();
	}

	private State[] motor;// 0 左前， 1 左后， 2 右前， 3 右后, 4 升降
	private float lift;// 升降高度
	public static final int size = 5;

	public MotorData(State[] motor, float lift) {
		this();
		this.motor = motor;
		this.lift = lift;
	}

	public State[] getMotor() {
		return motor;
	}

	public void setMotor(State[] motor) {
		this.motor = motor;
	}

	public float getLift() {
		return lift;
	}

	public void setLift(float lift) {
		this.lift = lift;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(data=");
		sb.append("[");
		for (State p : motor) {
			sb.append(p.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		sb.append(",lift=");
		sb.append(lift);
		sb.append(")");
		return sb.toString();
	}

}
