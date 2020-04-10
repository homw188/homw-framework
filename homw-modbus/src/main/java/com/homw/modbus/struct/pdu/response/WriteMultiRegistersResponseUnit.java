package com.homw.modbus.struct.pdu.response;

import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnitSupport;

public class WriteMultiRegistersResponseUnit extends ModbusProtoUnitSupport {
	public WriteMultiRegistersResponseUnit() {
		super(ModbusFuncCode.WRITE_MULTIPLE_REGISTERS);
	}

	public WriteMultiRegistersResponseUnit(int startAddr, int quantity) {
		super(ModbusFuncCode.WRITE_MULTIPLE_REGISTERS, startAddr, quantity);
	}
}
