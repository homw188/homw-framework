package com.homw.robot.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * 应答反馈包状态码
 * 
 * @author Hom
 * @version 1.0
 */
public enum JobStateCode {
	/// <<<JOB_WAIT 和 JOB_SEND任务命令发送端(机器人服务器)内部维护的状态码，占位保留>>> ///
	JOB_WAIT(1), JOB_SEND(2),

	/// <<<任务执行端反馈包的状态码>>> ///
	JOB_RECEIVED(3), JOB_PREEMPTED(4), JOB_SUCCEEDED(5), JOB_FAILED(-1);

	/**
	 * 状态码
	 */
	private int code;

	/**
	 * code map.
	 */
	private static Map<Integer, JobStateCode> codeMap;

	static {
		codeMap = new HashMap<Integer, JobStateCode>();

		/// <<<JOB_WAIT 和 JOB_SEND任务命令发送端(机器人服务器)内部维护的状态码，占位保留>>> ///
		codeMap.put(JOB_WAIT.code, JOB_WAIT);
		codeMap.put(JOB_SEND.code, JOB_SEND);

		/// <<<任务执行端反馈包的状态码>>> ///
		codeMap.put(JOB_RECEIVED.code, JOB_RECEIVED);
		codeMap.put(JOB_PREEMPTED.code, JOB_PREEMPTED);
		codeMap.put(JOB_SUCCEEDED.code, JOB_SUCCEEDED);
		codeMap.put(JOB_FAILED.code, JOB_FAILED);
	}

	/**
	 * get job state.
	 * 
	 * @param code
	 * @return
	 */
	public static JobStateCode getState(int code) {
		return codeMap.get(code);
	}

	private JobStateCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
