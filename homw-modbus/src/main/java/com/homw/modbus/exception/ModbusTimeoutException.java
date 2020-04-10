package com.homw.modbus.exception;

/**
 * Modbus响应超时异常
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月7日
 *
 */
public class ModbusTimeoutException extends ModbusException {
	private static final long serialVersionUID = 1L;

	public ModbusTimeoutException() {
		super();
	}

	public ModbusTimeoutException(String message) {
		super(message);
	}

	public ModbusTimeoutException(Throwable cause) {
		super(cause);
	}

	public ModbusTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

}
