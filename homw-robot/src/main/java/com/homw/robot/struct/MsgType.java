package com.homw.robot.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息类型指令码
 * 
 * @author Hom
 * @version 1.0
 */
public enum MsgType {
	/// <<<数据上报类型>>> ///
	/**
	 * 机器人实时状态
	 */
	TYPE_STATE(0),

	/**
	 * 实时位姿
	 */
	TYPE_POSE(1),

	/**
	 * 实时速度
	 */
	TYPE_SPEED(2),

	/**
	 * 规划路线
	 */
	TYPE_PATH(3),

	/**
	 * 电池信息
	 */
	TYPE_BATTERY(4),

	/**
	 * 电机信息
	 */
	TYPE_MOTOR(5),

	/**
	 * 激光数据
	 */
	TYPE_LASER(6),

	/// <<<设置机器人相关消息类型>>> ///
	/**
	 * 设置机器人初始位姿
	 */
	TYPE_SET_POSE(101),

	/**
	 * 设置最大速度
	 */
	TYPE_SET_SPEED(102),

	/**
	 * 其他设置
	 */
	TYPE_SET(103),

	/// <<<实时控制任务机器人相关消息类型，机器人不反馈任务执行结果>>> ///
	/**
	 * 远程遥控的移动速度
	 */
	TYPE_CONTROL_MOVE(201),

	/**
	 * 远程遥控升降和云台
	 */
	TYPE_CONTROL_PTZ(202),

	/// <<<异步任务相关类型，机器人任务完成后反馈>>> ///
	/**
	 * 执行巡检任务
	 */
	TYPE_CMD_INSPECTION(301),

	/**
	 * 去停靠点
	 */
	TYPE_CMD_DOCK(302),

	/**
	 * 点哪去哪
	 */
	TYPE_CMD_GO(303),

	/**
	 * 指哪看哪
	 */
	TYPE_CMD_OBSERVE(304),

	/**
	 * 其他控制指令
	 */
	TYPE_CMD(305),

	/**
	 * 强制执行 自主充电任务
	 */
	TYPE_CMD_CHARGE(306),

	/// <<<心跳、响应>>> ///
	/**
	 * 心跳数据包
	 */
	TYPE_HEART(401),

	/**
	 * 应答反馈
	 */
	TYPE_RESPONSE(501),

	/// <<<过时的类别>>> ///
	/**
	 * 远程遥控的移动速度
	 */
	@Deprecated
	TYPE_CMD_MOVE(-1),

	/**
	 * 紧急保护,自主返航，自主充电等单项任务
	 */
	@Deprecated
	TYPE_CMD_JOB(-2),

	/**
	 * 巡检任务
	 */
	@Deprecated
	TYPE_CMD_JOBS(-3);

	/**
	 * message type
	 */
	private int value;

	/**
	 * type map.
	 */
	private static Map<Integer, MsgType> typeMap;

	static {
		typeMap = new HashMap<Integer, MsgType>();

		/// <<<数据上报类型>>> ///
		typeMap.put(TYPE_STATE.value, TYPE_STATE);
		typeMap.put(TYPE_POSE.value, TYPE_POSE);
		typeMap.put(TYPE_SPEED.value, TYPE_SPEED);
		typeMap.put(TYPE_PATH.value, TYPE_PATH);
		typeMap.put(TYPE_BATTERY.value, TYPE_BATTERY);
		typeMap.put(TYPE_MOTOR.value, TYPE_MOTOR);
		typeMap.put(TYPE_LASER.value, TYPE_LASER);

		/// <<<设置机器人相关消息类型>>> ///
		typeMap.put(TYPE_SET_POSE.value, TYPE_SET_POSE);
		typeMap.put(TYPE_SET_SPEED.value, TYPE_SET_SPEED);
		typeMap.put(TYPE_SET.value, TYPE_SET);

		/// <<<实时控制任务机器人相关消息类型，机器人不反馈任务执行结果>>> ///
		typeMap.put(TYPE_CONTROL_MOVE.value, TYPE_CONTROL_MOVE);
		typeMap.put(TYPE_CONTROL_PTZ.value, TYPE_CONTROL_PTZ);

		/// <<<异步任务相关类型，机器人任务完成后反馈>>> ///
		typeMap.put(TYPE_CMD_INSPECTION.value, TYPE_CMD_INSPECTION);
		typeMap.put(TYPE_CMD_DOCK.value, TYPE_CMD_DOCK);
		typeMap.put(TYPE_CMD_GO.value, TYPE_CMD_GO);
		typeMap.put(TYPE_CMD_OBSERVE.value, TYPE_CMD_OBSERVE);
		typeMap.put(TYPE_CMD.value, TYPE_CMD);
		typeMap.put(TYPE_CMD_CHARGE.value, TYPE_CMD_CHARGE);

		/// <<<心跳、响应>>> ///
		typeMap.put(TYPE_HEART.value, TYPE_HEART);
		typeMap.put(TYPE_RESPONSE.value, TYPE_RESPONSE);

		/// <<<过时的类别>>> ///
		typeMap.put(TYPE_CMD_MOVE.value, TYPE_CMD_MOVE);
		typeMap.put(TYPE_CMD_JOB.value, TYPE_CMD_JOB);
		typeMap.put(TYPE_CMD_JOBS.value, TYPE_CMD_JOBS);
	}

	/**
	 * get message type.
	 * 
	 * @param value
	 * @return
	 */
	public static MsgType getType(int value) {
		return typeMap.get(value);
	}

	private MsgType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}
