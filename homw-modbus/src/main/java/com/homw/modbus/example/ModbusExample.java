package com.homw.modbus.example;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.homw.modbus.ModbusClient;
import com.homw.modbus.ModbusServer;
import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

/**
 * 简单测试
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusExample {
	public static void main(String[] args) {
		ModbusServer modbusServer = ModbusServerSingleton.getInstance().getModbusServer();
		ModbusClient modbusClient = ModbusClientSingleton.getInstance().getModbusClient();

		ModbusFrame readCoils = null;
		try {
			readCoils = modbusClient.send(ModbusTCPFrameFactory.readCoil(12321, 10));
		} catch (ModbusException ex) {
			Logger.getLogger(ModbusExample.class.getName()).log(Level.SEVERE, ex.getLocalizedMessage());
		}
		System.out.println(readCoils);

		modbusClient.shutdown();
		modbusServer.shutdown();
	}
}
