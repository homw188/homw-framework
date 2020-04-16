package com.homw.modbus.struct.pdu;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusFuncCode;

import io.netty.buffer.ByteBuf;

public class WriteSingleCoilUnit extends ModbusProtoUnitSupport {
	private boolean state;

	public WriteSingleCoilUnit() {
		super(ModbusFuncCode.WRITE_SINGLE_COIL);
	}

	public WriteSingleCoilUnit(int adress, boolean state) {
		super(ModbusFuncCode.WRITE_SINGLE_COIL, adress, state ? 0xFF00 : 0x0000);
		this.state = state;
	}

	public int getAddress() {
		return startAddr;
	}

	@Override
	public void decode(ByteBuf data) {
		super.decode(data);
		state = quantity == 0xFF00;
	}

	public boolean isState() {
		return state;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{funcCode=" + CodecUtil.shortToHex(funcCode) + ", address="
				+ CodecUtil.intToHex(startAddr) + ", value=" + CodecUtil.intToHex(quantity) + ", state=" + state
				+ '}';
	}
}
