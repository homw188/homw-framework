package com.homw.modbus.tcp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

public class ModbusWriteSingleRegisterTest extends ModbusTestBase {
	@Test
	public void testWriteRegister() throws ModbusException {
		boolean state = true;
		int value = 0x0000;
		for (int i = 0; i < 20; i++) {
			ModbusFrame writeRegister = modbusClient.send(ModbusTCPFrameFactory.writeSingleRegister(12321, value));

			assertNotNull(writeRegister);

			System.out.println(writeRegister);

			value = state ? 0x0000 : 0xFFFF;
			state = state ? false : true;
		}
	}
}
