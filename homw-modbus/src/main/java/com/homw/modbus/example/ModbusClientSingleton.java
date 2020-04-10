package com.homw.modbus.example;

import com.homw.modbus.ModbusClient;
import com.homw.modbus.ModbusClientFactory;
import com.homw.modbus.exception.ModbusException;

/**
 * 用于测试的客户端单例
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusClientSingleton {
	private ModbusClient modbusClient;

	private ModbusClientSingleton() {
		try {
			modbusClient = ModbusClientFactory.create("modbus-tcp.properties");
		} catch (ModbusException ex) {
			ex.printStackTrace();
		}
	}

	public ModbusClient getModbusClient() {
		return modbusClient;
	}

	public static ModbusClientSingleton getInstance() {
		return ClientAndServerHolder.INSTANCE;
	}

	private static class ClientAndServerHolder {
		private static final ModbusClientSingleton INSTANCE = new ModbusClientSingleton();
	}
}