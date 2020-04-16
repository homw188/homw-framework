package com.homw.modbus.struct.pdu;

import com.homw.common.util.CodecUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Modbus PDU适配器
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月14日
 *
 */
public abstract class ModbusProtoUnitSupport extends ModbusProtoUnit {
	protected int startAddr; // startAddress = 0x0000 to 0xFFFF
	protected int quantity; // quantityOfCoils = 1 - 2000 (0x07D0)

	public ModbusProtoUnitSupport(short funcCode) {
		super(funcCode);
	}

	public ModbusProtoUnitSupport(short funcCode, int startAddr, int quantity) {
		super(funcCode);

		this.startAddr = startAddr;
		this.quantity = quantity;
	}

	@Override
	public int calcLength() {
		// function code + address + quantity
		return 1 + 2 + 2;
	}

	@Override
	public ByteBuf encode() {
		ByteBuf buf = Unpooled.buffer(calcLength());
		buf.writeByte(getFuncCode());
		buf.writeShort(startAddr);
		buf.writeShort(quantity);
		return buf;
	}

	@Override
	public void decode(ByteBuf data) {
		startAddr = data.readUnsignedShort();
		quantity = data.readUnsignedShort();
	}

	@Override
	public byte[] getUnitBytes() {
		byte[] data = new byte[calcLength()];
		data[0] = (byte) funcCode;
		System.arraycopy(CodecUtil.unsignedShortToBytes(startAddr), 0, data, 1, 2);
		System.arraycopy(CodecUtil.unsignedShortToBytes(quantity), 0, data, 3, 2);
		return data;
	}

	public int getStartAddr() {
		return startAddr;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{funcCode=" + CodecUtil.shortToHex(funcCode) + ", startAddr="
				+ CodecUtil.intToHex(startAddr) + ", quantity=" + CodecUtil.intToHex(quantity) + '}';
	}
}
