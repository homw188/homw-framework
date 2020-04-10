package com.homw.modbus.struct.pdu.response;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ReadInputRegistersResponseUnit extends ModbusProtoUnit {
	private short byteCount;
	private int[] inputRegisters;

	public ReadInputRegistersResponseUnit() {
		super(ModbusFuncCode.READ_INPUT_REGISTERS);
	}

	public ReadInputRegistersResponseUnit(int[] inputRegisters) {
		super(ModbusFuncCode.READ_INPUT_REGISTERS);

		if (inputRegisters.length > ModbusConstant.REGISTERS_MAX_COUNT) {
			throw new IllegalArgumentException("超过最大输入寄存器长度：" + inputRegisters.length);
		}

		this.byteCount = (short) (inputRegisters.length * 2);
		this.inputRegisters = inputRegisters;
	}

	public int[] getInputRegisters() {
		return inputRegisters;
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
		for (int i = 0; i < inputRegisters.length; i++) {
			buf.writeShort(inputRegisters[i]);
		}
		return buf;
	}

	@Override
	public void decode(ByteBuf data) {
		byteCount = data.readUnsignedByte();

		inputRegisters = new int[byteCount / 2];
		for (int i = 0; i < inputRegisters.length; i++) {
			inputRegisters[i] = data.readUnsignedShort();
		}
	}

	@Override
	public byte[] getUnitBytes() {
		byte[] data = new byte[calcLength()];
		data[0] = (byte) funcCode;
		data[1] = (byte) byteCount;

		byte[] bytes = new byte[byteCount];
		for (int i = 0; i < inputRegisters.length; i++) {
			byte[] arr = CodecUtil.unsignedShort2Bytes(inputRegisters[i]);
			System.arraycopy(arr, 0, bytes, i * 2, arr.length);
		}
		System.arraycopy(bytes, 0, data, 2, byteCount);
		return data;
	}

	@Override
	public String toString() {
		StringBuilder registers = new StringBuilder();
		registers.append("{");
		for (int i = 0; i < inputRegisters.length; i++) {
			registers.append("register_");
			registers.append(i);
			registers.append("=");
			registers.append(CodecUtil.encodeHex(inputRegisters[i]));
			registers.append(", ");
		}
		registers.delete(registers.length() - 2, registers.length());
		registers.append("}");

		return this.getClass().getSimpleName() + "{funcCode=" + CodecUtil.encodeHex(funcCode) + ", byteCount="
				+ CodecUtil.encodeHex(byteCount) + ", inputRegisters=" + registers + '}';
	}
}
