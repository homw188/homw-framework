package com.homw.common.exception;

/**
 * @description 系统异常
 * @author Hom
 * @version 1.0
 * @since 2019-07-18
 */
public class SystemException extends Exception {
	private static final long serialVersionUID = -7686999180024846055L;

	public SystemException() {
		super();
	}

	public SystemException(String message) {
		super(message);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
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
