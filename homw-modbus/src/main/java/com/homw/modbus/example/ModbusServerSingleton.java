package com.homw.modbus.example;

import com.homw.modbus.ModbusServer;
import com.homw.modbus.ModbusServerFactory;
import com.homw.modbus.exception.ModbusException;

/**
 * 用于测试的服务端单例
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusServerSingleton {
	private ModbusServer modbusServer;

	private ModbusServerSingleton() {
		try {
			modbusServer = ModbusServerFactory.create("modbus-tcp.properties");
		} catch (ModbusException ex) {
			ex.printStackTrace();
		}
	}

	public ModbusServer getModbusServer() {
		return modbusServer;
	}

	public static ModbusServerSingleton getInstance() {
		return ServerForTestsHolder.INSTANCE;
	}

	private static class ServerForTestsHolder {
		private static final ModbusServerSingleton INSTANCE = new ModbusServerSingleton();
	}
}
