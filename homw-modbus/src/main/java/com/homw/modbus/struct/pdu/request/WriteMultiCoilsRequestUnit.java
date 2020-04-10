package com.homw.modbus.struct.pdu.request;

import java.util.BitSet;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnitSupport;

import io.netty.buffer.ByteBuf;

public class WriteMultiCoilsRequestUnit extends ModbusProtoUnitSupport {
	private short byteCount;
	private BitSet outputsValue;

	public WriteMultiCoilsRequestUnit() {
		super(ModbusFuncCode.WRITE_MULTIPLE_COILS);
	}

	public WriteMultiCoilsRequestUnit(int startAddr, int quantity, BitSet outputsValue) {
		super(ModbusFuncCode.WRITE_MULTIPLE_COILS, startAddr, quantity);

		byte[] coils = outputsValue.toByteArray();

		if (coils.length > ModbusConstant.REQUEST_COILS_MAX_COUNT) {
			throw new IllegalArgumentException("超过最大线圈长度：" + coils.length);
		}

		this.byteCount = (short) coils.length;
		this.outputsValue = outputsValue;
	}

	public short getByteCount() {
		return byteCount;
	}

	public BitSet getOutputsValue() {
		return outputsValue;
	}

	@Override
	public int calcLength() {
		return super.calcLength() + 1 + byteCount;
	}

	@Override
	public ByteBuf encode() {
		ByteBuf buf = super.encode();

		buf.writeByte(byteCount);
		buf.writeBytes(outputsValue.toByteArray());

		return buf;
	}

	@Override
	public void decode(ByteBuf data) {
		super.decode(data);

		byteCount = data.readUnsignedByte();

		byte[] coils = new byte[byteCount];
		data.readBytes(coils);

		outputsValue = BitSet.valueOf(coils);
	}

	@Override
	public byte[] getUnitBytes() {
		byte[] data = super.getUnitBytes();

		data[super.calcLength()] = (byte) byteCount;

		byte[] valBytes = outputsValue.toByteArray();
		System.arraycopy(valBytes, 0, data, super.calcLength() + 1, valBytes.length);
		return data;
	}

	@Override
	public String toString() {
		String spr = super.toString();
		return spr.substring(0, spr.length() - 1) + ", byteCount=" + CodecUtil.encodeHex(byteCount) + ", outputsValue="
				+ CodecUtil.encodeHex(outputsValue.toByteArray()) + '}';
	}
}
