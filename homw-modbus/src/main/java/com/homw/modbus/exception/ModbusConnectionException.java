package com.homw.modbus.exception;

/**
 * Modbus连接异常
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月7日
 *
 */
public class ModbusConnectionException extends ModbusException {
	private static final long serialVersionUID = 1L;

	public ModbusConnectionException() {
		super();
	}

	public ModbusConnectionException(String message) {
		super(message);
	}

	public ModbusConnectionException(Throwable cause) {
		super(cause);
	}

	public ModbusConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
