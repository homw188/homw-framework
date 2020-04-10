package com.homw.modbus.struct.pdu.request;

import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnitSupport;

public class ReadDiscreteInputsRequestUnit extends ModbusProtoUnitSupport {
	public ReadDiscreteInputsRequestUnit() {
		super(ModbusFuncCode.READ_DISCRETE_INPUTS);
	}

	public ReadDiscreteInputsRequestUnit(int startAddr, int quantity) {
		super(ModbusFuncCode.READ_DISCRETE_INPUTS, startAddr, quantity);
	}
}
