package com.homw.robot.struct.packet;

import com.homw.robot.struct.MsgBody;
import com.homw.robot.struct.MsgType;
import com.homw.robot.struct.base.State;

import io.netty.buffer.ByteBuf;

/**
 * 机器人状态
 * 
 * @author Hom
 * @version 1.0
 */
public class StateData extends MsgBody {
	public StateData() {
		type = MsgType.TYPE_STATE;
		len = (State.size + 4) * 4;
	}

	@Override
	public void writeToBuffer(ByteBuf buf) {
		if (runState == null) {
			buf.writeZero(State.size + 4);
		} else {
			runState.writeToBuffer(buf, "移动");
		}

		if (taskState == null) {
			buf.writeZero(State.size + 4);
		} else {
			taskState.writeToBuffer(buf, "任务");
		}

		if (locState == null) {
			buf.writeZero(State.size + 4);
		} else {
			locState.writeToBuffer(buf, "定位");
		}

		if (chargeTaskState == null) {
			buf.writeZero(State.size + 4);
		} else {
			chargeTaskState.writeToBuffer(buf, "充电");
		}
	}

	@Override
	public void readFromBuffer(ByteBuf buf, int dataLen) {
		runState = new State();
		runState.setStateCode(buf.readInt());
		byte[] rs = new byte[State.size];
		for (int i = 0; i < State.size; i++) {
			rs[i] = buf.readByte();
		}
		runState.setStr(rs);

		taskState = new State();
		taskState.setStateCode(buf.readInt());
		byte[] ts = new byte[State.size];
		for (int i = 0; i < State.size; i++) {
			ts[i] = buf.readByte();
		}
		taskState.setStr(ts);

		locState = new State();
		locState.setStateCode(buf.readInt());
		byte[] ls = new byte[State.size];
		for (int i = 0; i < State.size; i++) {
			ls[i] = buf.readByte();
		}
		locState.setStr(ls);

		chargeTaskState = new State();
		chargeTaskState.setStateCode(buf.readInt());
		byte[] ns = new byte[State.size];
		for (int i = 0; i < State.size; i++) {
			ns[i] = buf.readByte();
		}
		chargeTaskState.setStr(ns);
	}

	/**
	 * stateCode：0 正常，1 电量不足， -1检测到异常； str[] “附加描述信息”
	 */
	private State runState;

	/**
	 * stateCode： 0 任务待机中， 1 执行任务中， -1任务异常中断； str[] “附加描述信息”
	 */
	private State taskState;

	/**
	 * stateCode： 0 定位状态良好， 1 定位状态一般， -1定位较差； str[] “附加描述信息”
	 */
	private State locState;

	/**
	 * stateCode：0 放电， 1充电中， 2充电完成， -1 充电异常； str[] “附加描述信息”
	 */
	private State chargeTaskState;

	public StateData(State runState, State taskState, State locState, State naviState) {
		this();
		this.runState = runState;
		this.taskState = taskState;
		this.locState = locState;
		this.chargeTaskState = naviState;
	}

	public State getRunState() {
		return runState;
	}

	public void setRunState(State runState) {
		this.runState = runState;
	}

	public State getTaskState() {
		return taskState;
	}

	public void setTaskState(State taskState) {
		this.taskState = taskState;
	}

	public State getLocState() {
		return locState;
	}

	public void setLocState(State locState) {
		this.locState = locState;
	}

	public State getNaviState() {
		return chargeTaskState;
	}

	public void setNaviState(State naviState) {
		this.chargeTaskState = naviState;
	}

	@Override
	public String toString() {
		return "(runState=" + runState + ",taskState=" + taskState + ",locState=" + locState + ",chargeTaskState="
				+ chargeTaskState + ")";
	}

}
