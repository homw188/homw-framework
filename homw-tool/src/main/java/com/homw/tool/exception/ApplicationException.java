package com.homw.tool.exception;

/**
 * @description 应用异常
 * @author Hom
 * @version 1.0
 * @since 2019-07-18
 */
public class ApplicationException extends Exception {
	private static final long serialVersionUID = -7686999180024846055L;

	public ApplicationException() {
		super();
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

	public ApplicationException(String message, Throwable cause) {
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
