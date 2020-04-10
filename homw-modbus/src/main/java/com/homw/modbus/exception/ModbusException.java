package com.homw.modbus.exception;

/**
 * Modbus异常
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月7日
 *
 */
public class ModbusException extends Exception {
	private static final long serialVersionUID = 1L;

	public ModbusException() {
		super();
	}

	public ModbusException(String message) {
		super(message);
	}

	public ModbusException(Throwable cause) {
		super(cause);
	}

	public ModbusException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String toString() {
		Throwable cause = getCause();
		if (cause == null || cause == this) {
			return super.toString();
		}
		return super.toString() + " [nested exception:" + cause + "]";
	}
}
