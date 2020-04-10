package com.homw.robot.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * 普通消息状态码
 * 
 * @author Hom
 * @version 1.0
 */
public enum StateCode {
	STATE_WAIT(0), STATE_NORMAL(1), STATE_PAUSE(2), STATE_WARN(3), STATE_ERROR(4), STATE_FATAL(5);

	/**
	 * 状态码
	 */
	private int code;

	/**
	 * code map.
	 */
	private static Map<Integer, StateCode> codeMap;

	static {
		codeMap = new HashMap<Integer, StateCode>();

		codeMap.put(STATE_WAIT.code, STATE_WAIT);
		codeMap.put(STATE_NORMAL.code, STATE_NORMAL);
		codeMap.put(STATE_PAUSE.code, STATE_PAUSE);
		codeMap.put(STATE_WARN.code, STATE_WARN);
		codeMap.put(STATE_ERROR.code, STATE_ERROR);
		codeMap.put(STATE_FATAL.code, STATE_FATAL);
	}

	/**
	 * get state.
	 * 
	 * @param code
	 * @return
	 */
	public static StateCode getState(int code) {
		return codeMap.get(code);
	}

	private StateCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
