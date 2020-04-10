package com.homw.modbus.struct.pdu.request;

import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnitSupport;

public class ReadInputRegistersRequestUnit extends ModbusProtoUnitSupport {
	public ReadInputRegistersRequestUnit() {
		super(ModbusFuncCode.READ_INPUT_REGISTERS);
	}

	public ReadInputRegistersRequestUnit(int startAddr, int quantity) {
		super(ModbusFuncCode.READ_INPUT_REGISTERS, startAddr, quantity);
	}
}
