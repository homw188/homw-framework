package com.homw.modbus.struct.pdu.request;

import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnitSupport;

public class ReadCoilsRequestUnit extends ModbusProtoUnitSupport {
	public ReadCoilsRequestUnit() {
		super(ModbusFuncCode.READ_COILS);
	}

	public ReadCoilsRequestUnit(int startAddr, int quantity) {
		super(ModbusFuncCode.READ_COILS, startAddr, quantity);
	}
}
