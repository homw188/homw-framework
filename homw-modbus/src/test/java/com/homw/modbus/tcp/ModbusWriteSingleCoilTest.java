package com.homw.modbus.tcp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

public class ModbusWriteSingleCoilTest extends ModbusTestBase {
	@Test
	public void testWriteCoil() throws ModbusException {
		boolean state = true;
		for (int i = 0; i < 20; i++) {
			ModbusFrame writeCoil = modbusClient.send(ModbusTCPFrameFactory.writeSingleCoil(12321, state));

			assertNotNull(writeCoil);

			System.out.println(writeCoil);

			state = state ? false : true;
		}
	}
}
