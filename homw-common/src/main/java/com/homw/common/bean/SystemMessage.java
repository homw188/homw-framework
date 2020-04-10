package com.homw.common.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 系统消息
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class SystemMessage extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public SystemMessage() {
		put("code", 0);
	}

	public static SystemMessage error() {
		return error(500, "未知异常，请联系管理员");
	}

	public static SystemMessage error(String msg) {
		return error(500, msg);
	}

	public static SystemMessage error(int code, String msg) {
		SystemMessage r = new SystemMessage();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static SystemMessage ok(String msg) {
		SystemMessage r = new SystemMessage();
		r.put("msg", msg);
		return r;
	}

	public static SystemMessage ok(Map<String, Object> map) {
		SystemMessage r = new SystemMessage();
		r.putAll(map);
		return r;
	}

	public static SystemMessage ok() {
		return new SystemMessage();
	}

	public SystemMessage put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
