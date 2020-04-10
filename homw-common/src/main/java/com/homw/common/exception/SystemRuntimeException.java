package com.homw.common.exception;

/**
 * @description 系统运行时异常
 * @author Hom
 * @version 1.0
 * @since 2020-03-26
 */
public class SystemRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public SystemRuntimeException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public SystemRuntimeException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public SystemRuntimeException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public SystemRuntimeException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		Throwable cause = getCause();
		if (cause == null || cause == this) {
			return super.toString();
		}
		return super.toString() + " [nested Exception: " + cause + "]";
	}
}
