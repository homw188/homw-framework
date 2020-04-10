package com.homw.modbus.struct.pdu.response;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ReadHoldingRegistersResponseUnit extends ModbusProtoUnit {
	private short byteCount;
	private int[] registers;// element is unsigned short

	public ReadHoldingRegistersResponseUnit() {
		super(ModbusFuncCode.READ_HOLDING_REGISTERS);
	}

	public ReadHoldingRegistersResponseUnit(int[] registers) {
		super(ModbusFuncCode.READ_HOLDING_REGISTERS);

		if (registers.length > ModbusConstant.REGISTERS_MAX_COUNT) {
			throw new IllegalArgumentException("超过最大寄存器长度：" + registers.length);
		}

		this.byteCount = (short) (registers.length * 2);
		this.registers = registers;
	}

	public int[] getRegisters() {
		return registers;
	}

	public short getByteCount() {
		return byteCount;
	}

	@Override
	public int calcLength() {
		return 1 + 1 + byteCount;
	}

	@Override
	public ByteBuf encode() {
		ByteBuf buf = Unpooled.buffer(calcLength());

		buf.writeByte(getFuncCode());
		buf.writeByte(byteCount);
		for (int i = 0; i < registers.length; i++) {
			buf.writeShort(registers[i]);
		}
		return buf;
	}

	@Override
	public void decode(ByteBuf data) {
		byteCount = data.readUnsignedByte();

		registers = new int[byteCount / 2];
		for (int i = 0; i < registers.length; i++) {
			registers[i] = data.readUnsignedShort();
		}
	}

	@Override
	public byte[] getUnitBytes() {
		byte[] data = new byte[calcLength()];
		data[0] = (byte) funcCode;
		data[1] = (byte) byteCount;

		byte[] bytes = new byte[byteCount];
		for (int i = 0; i < registers.length; i++) {
			byte[] arr = CodecUtil.unsignedShort2Bytes(registers[i]);
			System.arraycopy(arr, 0, bytes, i * 2, arr.length);
		}
		System.arraycopy(bytes, 0, data, 2, byteCount);
		return data;
	}

	@Override
	public String toString() {
		StringBuilder registersStr = new StringBuilder();
		registersStr.append("{");
		for (int i = 0; i < registers.length; i++) {
			registersStr.append("register_");
			registersStr.append(i);
			registersStr.append("=");
			registersStr.append(CodecUtil.encodeHex(registers[i]));
			registersStr.append(", ");
		}
		registersStr.delete(registersStr.length() - 2, registersStr.length());
		registersStr.append("}");

		return this.getClass().getSimpleName() + "{funcCode=" + CodecUtil.encodeHex(funcCode) + ", byteCount="
				+ CodecUtil.encodeHex(byteCount) + ", inputRegisters=" + registersStr + '}';
	}
}
