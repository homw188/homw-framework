package com.homw.modbus.struct;

import com.homw.modbus.struct.rtu.ModbusRTUFrame;

/**
 * Modbus功能码
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
public class ModbusFuncCode {
	/**
	 * 读线圈寄存器，位操作
	 */
	public static final short READ_COILS = 0x01;

	/**
	 * 读离散输入寄存器，位操作。类似读线圈寄存器
	 */
	public static final short READ_DISCRETE_INPUTS = 0x02;

	/**
	 * 读保持寄存器，字节指令操作
	 */
	public static final short READ_HOLDING_REGISTERS = 0x03;

	/**
	 * 读输入寄存器，字节指令操作。类似读保持寄存器
	 */
	public static final short READ_INPUT_REGISTERS = 0x04;

	/**
	 * 写单个线圈，位操作
	 */
	public static final short WRITE_SINGLE_COIL = 0x05;

	/**
	 * 写单个保持寄存器，字节指令操作
	 */
	public static final short WRITE_SINGLE_REGISTER = 0x06;

	/**
	 * 写多个线圈
	 */
	public static final short WRITE_MULTIPLE_COILS = 0x0F;

	/**
	 * 写多个保持寄存器，字节指令操作
	 */
	public static final short WRITE_MULTIPLE_REGISTERS = 0x10;

	public static final short READ_FILE_RECORD = 0x14;
	public static final short WRITE_FILE_RECORD = 0x15;
	public static final short MASK_WRITE_REGISTER = 0x16;
	public static final short READ_WRITE_MULTIPLE_REGISTERS = 0x17;
	public static final short READ_FIFO_QUEUE = 0x18;
	public static final short ENCAPSULATED_INTERFACE_TRANSPORT = 0x2B;

	/**
	 * 转换异常功能码
	 * 
	 * @param frame
	 * @return
	 */
	public static short convertErrCode(ModbusFrame frame) {
		short func = (short) (frame.getFuncCode() + 0x80);
		if (func == 0x81 || func == 0x82) {
			func = 0x80;
		}
		return func;
	}

	/**
	 * 转换正常功能码
	 * 
	 * @param frame
	 * @return
	 */
	public static short convertNormalCode(ModbusRTUFrame frame) {
		return convertNormalCode(frame.getFuncCode());
	}

	/**
	 * 转换正常功能码
	 * 
	 * @param funcErr
	 * @return
	 */
	public static short convertNormalCode(short funcErr) {
		short func = (short) (funcErr - 0x80);
		if (func == 0x00) {
			func++;
		}
		return func;
	}
}
