package com.homw.modbus.struct.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Modbus TCP数据帧头部
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月12日
 *
 */
public class ModbusTCPHead {
	private int transId;
	private int protoId;
	private int length;
	private short unitId;

	public ModbusTCPHead() {
	}

	public ModbusTCPHead(int transId, int protoId, int pduLength, short unitId) {
		this.transId = transId;
		this.protoId = protoId;
		this.length = pduLength + 1; // + unit identifier
		this.unitId = unitId;
	}

	public int getLength() {
		return length;
	}

	public int getProtoId() {
		return protoId;
	}

	public int getTransId() {
		return transId;
	}

	public short getUnitId() {
		return unitId;
	}

	public void decode(ByteBuf buffer) {
		this.transId = buffer.readUnsignedShort();
		this.protoId = buffer.readUnsignedShort();
		this.length = buffer.readUnsignedShort() + 1;
		this.unitId = buffer.readUnsignedByte();
	}

	public ByteBuf encode() {
		ByteBuf buf = Unpooled.buffer();

		buf.writeShort(transId);
		buf.writeShort(protoId);
		buf.writeShort(length);
		buf.writeByte(unitId);

		return buf;
	}

	@Override
	public String toString() {
		return "ModbusHeader{" + "transId=" + transId + ", protoId=" + protoId + ", length=" + length + ", unitId="
				+ unitId + '}';
	}
}
