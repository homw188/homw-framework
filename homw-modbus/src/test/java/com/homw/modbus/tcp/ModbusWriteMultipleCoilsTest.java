package com.homw.modbus.tcp;

import static org.junit.Assert.assertNotNull;

import java.util.BitSet;

import org.junit.Test;

import com.homw.modbus.exception.ModbusException;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

public class ModbusWriteMultipleCoilsTest extends ModbusTestBase {
	@Test
	public void testWriteMultipleCoils() throws ModbusException {
		int quantityOfCoils = 10;

		BitSet coils = new BitSet(quantityOfCoils);
		coils.set(0);
		coils.set(5);
		coils.set(8);

		ModbusFrame writeMultipleCoils = modbusClient
				.send(ModbusTCPFrameFactory.writeMultiCoil(12321, quantityOfCoils, coils));

		assertNotNull(writeMultipleCoils);

		System.out.println(writeMultipleCoils);
	}
}
