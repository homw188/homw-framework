package com.homw.modbus.struct.pdu;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusFuncCode;

public class WriteSingleRegisterUnit extends ModbusProtoUnitSupport {
	public WriteSingleRegisterUnit() {
		super(ModbusFuncCode.WRITE_SINGLE_REGISTER);
	}

	public WriteSingleRegisterUnit(int address, int value) {
		super(ModbusFuncCode.WRITE_SINGLE_REGISTER, address, value);
	}

	public int getAddress() {
		return startAddr;
	}

	public int getValue() {
		return quantity;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{funcCode=" + CodecUtil.shortToHex(funcCode) + ", address="
				+ CodecUtil.intToHex(startAddr) + ", value=" + CodecUtil.intToHex(quantity) + '}';
	}
}
