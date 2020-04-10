package com.homw.modbus.tcp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

public class ModbusReadCoilsTest extends ModbusTestBase {
	@Test
	public void testReadCoils() throws ModbusException {
		ModbusFrame readCoils = modbusClient.send(ModbusTCPFrameFactory.readCoil(12321, 10));

		assertNotNull(readCoils);

		System.out.println(readCoils);
	}
}
