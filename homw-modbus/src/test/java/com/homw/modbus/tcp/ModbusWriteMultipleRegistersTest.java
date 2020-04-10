package com.homw.modbus.tcp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

public class ModbusWriteMultipleRegistersTest extends ModbusTestBase {
	@Test
	public void testWriteMultipleRegisters() throws ModbusException {
		int quantityOfRegisters = 10;

		int[] registers = new int[quantityOfRegisters];
		registers[0] = 0xFFFF;
		registers[1] = 0xF0F0;
		registers[2] = 0x0F0F;

		ModbusFrame writeMultipleRegisters = modbusClient
				.send(ModbusTCPFrameFactory.writeMultiRegister(12321, quantityOfRegisters, registers));

		assertNotNull(writeMultipleRegisters);

		System.out.println(writeMultipleRegisters);
	}
}
