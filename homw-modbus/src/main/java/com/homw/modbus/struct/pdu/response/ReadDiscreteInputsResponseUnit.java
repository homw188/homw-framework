package com.homw.modbus.struct.pdu.response;

import java.util.BitSet;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ReadDiscreteInputsResponseUnit extends ModbusProtoUnit {
	private short byteCount;
	private BitSet inputStatus;

	public ReadDiscreteInputsResponseUnit() {
		super(ModbusFuncCode.READ_DISCRETE_INPUTS);
	}

	public ReadDiscreteInputsResponseUnit(BitSet inputStatus) {
		super(ModbusFuncCode.READ_DISCRETE_INPUTS);

		byte[] inputs = inputStatus.toByteArray();

		if (inputs.length > ModbusConstant.RESPONSE_COILS_MAX_COUNT) {
			throw new IllegalArgumentException("超过最大输入长度：" + inputs.length);
		}

		this.byteCount = (short) inputs.length;
		this.inputStatus = inputStatus;
	}

	public BitSet getInputStatus() {
		return inputStatus;
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
		buf.writeBytes(inputStatus.toByteArray());

		return buf;
	}

	@Override
	public void decode(ByteBuf data) {
		byteCount = data.readUnsignedByte();

		byte[] inputs = new byte[byteCount];
		data.readBytes(inputs);

		inputStatus = BitSet.valueOf(inputs);
	}

	@Override
	public byte[] getUnitBytes() {
		byte[] data = new byte[calcLength()];
		data[0] = (byte) funcCode;
		data[1] = (byte) byteCount;
		byte[] bytes = inputStatus.toByteArray();
		System.arraycopy(bytes, 0, data, 2, bytes.length);
		return data;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{funcCode=" + CodecUtil.encodeHex(funcCode) + ", byteCount="
				+ CodecUtil.encodeHex(byteCount) + ", coilStatus=" + CodecUtil.encodeHex(inputStatus.toByteArray())
				+ '}';
	}
}
