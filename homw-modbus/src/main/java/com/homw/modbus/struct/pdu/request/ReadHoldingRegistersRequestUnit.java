package com.homw.modbus.struct.pdu.request;

import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnitSupport;

public class ReadHoldingRegistersRequestUnit extends ModbusProtoUnitSupport {
	public ReadHoldingRegistersRequestUnit() {
		super(ModbusFuncCode.READ_HOLDING_REGISTERS);
	}

	public ReadHoldingRegistersRequestUnit(int startAddr, int quantity) {
		super(ModbusFuncCode.READ_HOLDING_REGISTERS, startAddr, quantity);
	}
}
