package com.homw.modbus.struct.rtu;

import java.util.BitSet;

import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;
import com.homw.modbus.struct.pdu.ModbusUnitFacotry;

/**
 * Modbus RTU数据帧创建工厂
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月8日
 *
 */
public class ModbusRTUFrameFactory {
	/**
	 * 创建空的Modbus数据帧
	 * 
	 * @param func    功能码
	 * @param isReply 是否应答报文
	 * @return
	 */
	public static ModbusRTUFrame createEmpty(short func, boolean isReply) {
		return new ModbusRTUFrame((short) 0, ModbusUnitFacotry.createEmpty(func, isReply));
	}

	/**
	 * 写单个保持寄存器，字节指令操作。请求响应报文一样，均调用该方法
	 * 
	 * @param devAddr 设备地址
	 * @param address 地址
	 * @param value   输出值
	 * @return
	 */
	public static ModbusFrame writeSingleRegister(short devAddr, int address, int value) {
		return createWithUnit(devAddr, ModbusUnitFacotry.writeSingleRegister(address, value));
	}

	/**
	 * 写单个线圈，位操作。请求响应报文一样，均调用该方法
	 * 
	 * @param devAddr 设备地址
	 * @param address 地址
	 * @param state   状态（true:0xFF00; false:0x0000）
	 * @return
	 */
	public static ModbusFrame writeSingleCoil(short devAddr, int address, boolean state) {
		return createWithUnit(devAddr, ModbusUnitFacotry.writeSingleCoil(address, state));
	}

	/**
	 * 写多个保持寄存器，字节指令操作
	 * 
	 * @param devAddr   设备地址
	 * @param address   开始地址
	 * @param quantity  输出数量
	 * @param registers 输出值
	 * @return
	 */
	public static ModbusFrame writeMultiRegister(short devAddr, int address, int quantity, int[] registers) {
		return createWithUnit(devAddr, ModbusUnitFacotry.writeMultiRegister(address, quantity, registers));
	}

	/**
	 * 写多个保持寄存器响应
	 * 
	 * @param devAddr   设备地址
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame writeMultiRegisterResp(short devAddr, int startAddr, int quantity) {
		return createWithUnit(devAddr, ModbusUnitFacotry.writeMultiRegisterResp(startAddr, quantity));
	}

	/**
	 * 写多个线圈
	 * 
	 * @param devAddr      设备地址
	 * @param address      开始地址
	 * @param quantity     输出数量
	 * @param outputsValue 输出值
	 * @return
	 */
	public static ModbusFrame writeMultiCoil(short devAddr, int address, int quantity, BitSet outputsValue) {
		return createWithUnit(devAddr, ModbusUnitFacotry.writeMultiCoil(address, quantity, outputsValue));
	}

	/**
	 * 写多个线圈响应
	 * 
	 * @param devAddr   设备地址
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame writeMultiCoilResp(short devAddr, int startAddr, int quantity) {
		return createWithUnit(devAddr, ModbusUnitFacotry.writeMultiCoilResp(startAddr, quantity));
	}

	/**
	 * 读输入寄存器，字节指令操作。类似读保持寄存器
	 * 
	 * @param devAddr   设备地址
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame readInputRegister(short devAddr, int startAddr, int quantity) {
		return createWithUnit(devAddr, ModbusUnitFacotry.readInputRegister(startAddr, quantity));
	}

	/**
	 * 读输入寄存器响应
	 * 
	 * @param devAddr        设备地址
	 * @param inputRegisters 输出寄存器值
	 * @return
	 */
	public static ModbusFrame readInputRegisterResp(short devAddr, int[] inputRegisters) {
		return createWithUnit(devAddr, ModbusUnitFacotry.readInputRegisterResp(inputRegisters));
	}

	/**
	 * 读保持寄存器，字节指令操作
	 * 
	 * @param devAddr   设备地址
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame readHoldingRegister(short devAddr, int startAddr, int quantity) {
		return createWithUnit(devAddr, ModbusUnitFacotry.readHoldingRegister(startAddr, quantity));
	}

	/**
	 * 读保持寄存器响应
	 * 
	 * @param devAddr   设备地址
	 * @param registers 输出寄存器值
	 * @return
	 */
	public static ModbusFrame readHoldingRegisterResp(short devAddr, int[] registers) {
		return createWithUnit(devAddr, ModbusUnitFacotry.readHoldingRegisterResp(registers));
	}

	/**
	 * 读线圈寄存器，位操作
	 * 
	 * @param devAddr   设备地址
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame readCoil(short devAddr, int startAddr, int quantity) {
		return createWithUnit(devAddr, ModbusUnitFacotry.readCoil(startAddr, quantity));
	}

	/**
	 * 读线圈响应
	 * 
	 * @param devAddr    设备地址
	 * @param coilStatus 输出状态
	 * @return
	 */
	public static ModbusFrame readCoilResp(short devAddr, BitSet coilStatus) {
		return createWithUnit(devAddr, ModbusUnitFacotry.readCoilResp(coilStatus));
	}

	/**
	 * 读离散输入寄存器，位操作。类似读线圈寄存器
	 * 
	 * @param devAddr   设备地址
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame readDiscreteInput(short devAddr, int startAddr, int quantity) {
		return createWithUnit(devAddr, ModbusUnitFacotry.readDiscreteInput(startAddr, quantity));
	}

	/**
	 * 读离散输入响应
	 * 
	 * @param devAddr     设备地址
	 * @param inputStatus 输出状态
	 * @return
	 */
	public static ModbusFrame readDiscreteInputResp(short devAddr, BitSet inputStatus) {
		return createWithUnit(devAddr, ModbusUnitFacotry.readDiscreteInputResp(inputStatus));
	}

	/**
	 * 根据{@link ModbusProtoUnit}创建数据帧
	 * 
	 * @param devAddr
	 * @param unit
	 * @return
	 */
	public static ModbusFrame createWithUnit(short devAddr, ModbusProtoUnit unit) {
		return new ModbusRTUFrame(devAddr, unit);
	}
}
