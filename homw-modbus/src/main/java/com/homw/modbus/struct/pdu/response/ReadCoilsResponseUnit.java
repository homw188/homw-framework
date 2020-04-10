package com.homw.modbus.struct.pdu.response;

import java.util.BitSet;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ReadCoilsResponseUnit extends ModbusProtoUnit {
	private short byteCount;
	private BitSet coilStatus;

	public ReadCoilsResponseUnit() {
		super(ModbusFuncCode.READ_COILS);
	}

	public ReadCoilsResponseUnit(BitSet coilStatus) {
		super(ModbusFuncCode.READ_COILS);

		byte[] coils = coilStatus.toByteArray();

		if (coils.length > ModbusConstant.RESPONSE_COILS_MAX_COUNT) {
			throw new IllegalArgumentException("超过最大线圈长度：" + coils.length);
		}

		this.byteCount = (short) coils.length;
		this.coilStatus = coilStatus;
	}

	public BitSet getCoilStatus() {
		return coilStatus;
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
		buf.writeBytes(coilStatus.toByteArray());

		return buf;
	}

	@Override
	public void decode(ByteBuf data) {
		byteCount = data.readUnsignedByte();

		byte[] coils = new byte[byteCount];
		data.readBytes(coils);

		coilStatus = BitSet.valueOf(coils);
	}

	@Override
	public byte[] getUnitBytes() {
		byte[] data = new byte[calcLength()];
		data[0] = (byte) funcCode;
		data[1] = (byte) byteCount;
		byte[] bytes = coilStatus.toByteArray();
		System.arraycopy(bytes, 0, data, 2, bytes.length);
		return data;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{funcCode=" + CodecUtil.encodeHex(funcCode) + ", byteCount="
				+ CodecUtil.encodeHex(byteCount) + ", coilStatus=" + CodecUtil.encodeHex(coilStatus.toByteArray())
				+ '}';
	}
}
