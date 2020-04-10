package com.homw.modbus.tcp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

public class ModbusReadInputRegistersTest extends ModbusTestBase {
	@Test
	public void testReadInputRegisters() throws ModbusException {
		ModbusFrame readInputRegisters = modbusClient.send(ModbusTCPFrameFactory.readHoldingRegister(12321, 10));

		assertNotNull(readInputRegisters);

		System.out.println(readInputRegisters);
	}
}
