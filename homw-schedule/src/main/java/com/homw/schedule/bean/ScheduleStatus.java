package com.homw.schedule.bean;

/**
 * @description 任务状态
 * @author Hom
 * @version 1.0
 * @since 2020-04-02
 */
public enum ScheduleStatus {
	/**
	 * 正常
	 */
	NORMAL(0),
	/**
	 * 暂停
	 */
	PAUSE(1);

	private int value;

	private ScheduleStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
