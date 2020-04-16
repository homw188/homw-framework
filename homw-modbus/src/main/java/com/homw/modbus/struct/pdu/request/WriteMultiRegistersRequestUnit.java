package com.homw.modbus.struct.pdu.request;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnitSupport;

import io.netty.buffer.ByteBuf;

public class WriteMultiRegistersRequestUnit extends ModbusProtoUnitSupport {
	private short byteCount;
	private int[] registers;

	public WriteMultiRegistersRequestUnit() {
		super(ModbusFuncCode.WRITE_MULTIPLE_REGISTERS);
	}

	public WriteMultiRegistersRequestUnit(int startAddr, int quantity, int[] registers) {
		super(ModbusFuncCode.WRITE_MULTIPLE_REGISTERS, startAddr, quantity);

		if (registers.length > ModbusConstant.REGISTERS_MAX_COUNT) {
			throw new IllegalArgumentException("超过最大寄存器长度：" + registers.length);
		}

		this.byteCount = (short) (registers.length * 2);
		this.registers = registers;
	}

	public short getByteCount() {
		return byteCount;
	}

	public int[] getRegisters() {
		return registers;
	}

	@Override
	public int calcLength() {
		return super.calcLength() + 1 + byteCount;
	}

	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();

		buf.writeByte(byteCount);

		for (int i = 0; i < registers.length; i++) {
			buf.writeShort(registers[i]);
		}
		return buf;
	}

	@Override
	public void decode(ByteBuf data) {
		super.decode(data);

		byteCount = data.readUnsignedByte();

		registers = new int[byteCount / 2];
		for (int i = 0; i < registers.length; i++) {
			registers[i] = data.readUnsignedShort();
		}
	}

	@Override
	public byte[] getUnitBytes() {
		byte[] data = super.getUnitBytes();

		data[super.calcLength()] = (byte) byteCount;

		byte[] bytes = new byte[byteCount];
		for (int i = 0; i < registers.length; i++) {
			byte[] arr = CodecUtil.unsignedShortToBytes(registers[i]);
			System.arraycopy(arr, 0, bytes, i * 2, arr.length);
		}
		System.arraycopy(bytes, 0, data, super.calcLength() + 1, byteCount);
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
			registersStr.append(CodecUtil.intToHex(registers[i]));
			registersStr.append(", ");
		}
		registersStr.delete(registersStr.length() - 2, registersStr.length());
		registersStr.append("}");

		String spr = super.toString();
		return spr.substring(0, spr.length() - 1) + ", byteCount=" + CodecUtil.shortToHex(byteCount) + ", registers="
				+ registersStr + '}';
	}
}
