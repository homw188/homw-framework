package com.homw.modbus.struct.pdu.response;

import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnitSupport;

public class WriteMultiCoilsResponseUnit extends ModbusProtoUnitSupport {
	public WriteMultiCoilsResponseUnit() {
		super(ModbusFuncCode.WRITE_MULTIPLE_COILS);
	}

	public WriteMultiCoilsResponseUnit(int startAddr, int quantity) {
		super(ModbusFuncCode.WRITE_MULTIPLE_COILS, startAddr, quantity);
	}
}
