package com.homw.modbus.tcp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

public class ModbusReadDiscreteInputsTest extends ModbusTestBase {
	@Test
	public void testReadDiscreteInputs() throws ModbusException {
		ModbusFrame readDiscreteInputs = modbusClient.send(ModbusTCPFrameFactory.readDiscreteInput(12321, 10));

		assertNotNull(readDiscreteInputs);

		System.out.println(readDiscreteInputs);
	}
}
