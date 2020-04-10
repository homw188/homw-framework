package com.homw.modbus.util;

import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.ModbusFuncCode;

/**
 * Modbus数据包key生成器
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月7日
 *
 */
public class ModbusKeyGenerator {
	/**
	 * 生成key
	 * 
	 * @param frame
	 * @return
	 */
	public static String build(ModbusFrame frame) {
		return (frame.getDevAddr() & 0xFF) + "-" + (frame.getFuncCode() & 0xFF);
	}

	/**
	 * 生成Err key
	 * 
	 * @param frame
	 * @return
	 */
	public static String buildErr(ModbusFrame frame) {
		return (frame.getDevAddr() & 0xFF) + "-" + (ModbusFuncCode.convertErrCode(frame) & 0xFF);
	}
}
