package com.homw.modbus.tcp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

public class ModbusReadHoldingRegistersTest extends ModbusTestBase {
	@Test
	public void testReadHoldingRegisters() throws ModbusException {
		ModbusFrame readHoldingRegisters = modbusClient.send(ModbusTCPFrameFactory.readHoldingRegister(12321, 10));

		assertNotNull(readHoldingRegisters);

		System.out.println(readHoldingRegisters);
	}
}
