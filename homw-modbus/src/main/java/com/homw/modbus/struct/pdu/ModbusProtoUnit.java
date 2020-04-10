package com.homw.modbus.struct.pdu;

import io.netty.buffer.ByteBuf;

/**
 * Mobus协议数据单元（PDU）抽象
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月12日
 *
 */
public abstract class ModbusProtoUnit {
	protected final short funcCode;

	public ModbusProtoUnit(short funcCode) {
		this.funcCode = funcCode;
	}

	/**
	 * 获取功能码
	 * 
	 * @return
	 */
	public short getFuncCode() {
		return funcCode;
	}

	/**
	 * 计算协议单元字节长度
	 * 
	 * @return
	 */
	public abstract int calcLength();

	/**
	 * 编码，包含功能码
	 * 
	 * @return
	 */
	public abstract ByteBuf encode();

	/**
	 * 解码，不包含功能码
	 * 
	 * @param data
	 */
	public abstract void decode(ByteBuf data);

	/**
	 * 获取协议单元字节数组，用于计算CRC
	 * 
	 * @param buf
	 * @return
	 */
	public abstract byte[] getUnitBytes();
}
