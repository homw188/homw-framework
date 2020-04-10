package com.homw.modbus.struct;

import com.homw.modbus.struct.pdu.ModbusProtoUnit;

import io.netty.buffer.ByteBuf;

/**
 * Modbus数据包接口，基于RTU和TCP的抽象
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月12日
 *
 */
public interface ModbusFrame {
	/**
	 * 编码数据包
	 * 
	 * @return
	 */
	ByteBuf encode();

	/**
	 * 解码数据包
	 * 
	 * @param data
	 */
	void decode(ByteBuf data);

	/**
	 * 获取功能码
	 * 
	 * @return
	 */
	short getFuncCode();

	/**
	 * 获取设备地址
	 * 
	 * @return
	 */
	short getDevAddr();

	/**
	 * 获取协议单元
	 * 
	 * @return
	 */
	ModbusProtoUnit getProtoUnit();

	/**
	 * 获取数据包key，用于缓存响应包
	 * 
	 * @return
	 */
	String getModbusKey();

	/**
	 * 获取数据异常包key，用于缓存响应包
	 * 
	 * @return
	 */
	String getModbusErrKey();

	/**
	 * 是否异常数据包
	 * 
	 * @return
	 */
	boolean isError();
}
