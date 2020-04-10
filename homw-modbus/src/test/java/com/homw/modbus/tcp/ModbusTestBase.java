package com.homw.modbus.tcp;

import org.junit.Before;

import com.homw.modbus.ModbusClient;
import com.homw.modbus.ModbusServer;
import com.homw.modbus.example.ModbusClientSingleton;
import com.homw.modbus.example.ModbusServerSingleton;

public class ModbusTestBase {
	protected ModbusClient modbusClient;
	protected ModbusServer modbusServer;

	@Before
	public void setUp() throws Exception {
		modbusServer = ModbusServerSingleton.getInstance().getModbusServer();
		modbusClient = ModbusClientSingleton.getInstance().getModbusClient();
	}
}
