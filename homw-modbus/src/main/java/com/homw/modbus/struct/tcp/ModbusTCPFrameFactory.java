package com.homw.modbus.struct.tcp;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;
import com.homw.modbus.struct.pdu.ModbusUnitFacotry;

/**
 * Modbus TCP数据帧创建工厂
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月8日
 *
 */
public class ModbusTCPFrameFactory {
	private static AtomicInteger transIdCounter = new AtomicInteger(1);

	/**
	 * 创建空的Modbus数据帧
	 * 
	 * @param func    功能码
	 * @param isReply 是否应答报文
	 * @return
	 */
	public static ModbusTCPFrame createEmpty(short func, boolean isReply) {
		return new ModbusTCPFrame(new ModbusTCPHead(), ModbusUnitFacotry.createEmpty(func, isReply));
	}

	/**
	 * 写单个保持寄存器，字节指令操作。请求响应报文一样，均调用该方法
	 * 
	 * @param address 地址
	 * @param value   输出值
	 * @return
	 */
	public static ModbusFrame writeSingleRegister(int address, int value) {
		return createWithUnit(ModbusUnitFacotry.writeSingleRegister(address, value));
	}

	/**
	 * 写单个线圈，位操作。请求响应报文一样，均调用该方法
	 * 
	 * @param devAddr 设备地址
	 * @param address 地址
	 * @param state   状态（true:0xFF00; false:0x0000）
	 * @return
	 */
	public static ModbusFrame writeSingleCoil(int address, boolean state) {
		return createWithUnit(ModbusUnitFacotry.writeSingleCoil(address, state));
	}

	/**
	 * 写多个保持寄存器，字节指令操作
	 * 
	 * @param address   开始地址
	 * @param quantity  输出数量
	 * @param registers 输出值
	 * @return
	 */
	public static ModbusFrame writeMultiRegister(int address, int quantity, int[] registers) {
		return createWithUnit(ModbusUnitFacotry.writeMultiRegister(address, quantity, registers));
	}

	/**
	 * 写多个保持寄存器响应
	 * 
	 * @param head      请求头
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame writeMultiRegisterResp(ModbusTCPHead head, int startAddr, int quantity) {
		return createWithHead(head, ModbusUnitFacotry.writeMultiRegisterResp(startAddr, quantity));
	}

	/**
	 * 写多个线圈
	 * 
	 * @param address      开始地址
	 * @param quantity     输出数量
	 * @param outputsValue 输出值
	 * @return
	 */
	public static ModbusFrame writeMultiCoil(int address, int quantity, BitSet outputsValue) {
		return createWithUnit(ModbusUnitFacotry.writeMultiCoil(address, quantity, outputsValue));
	}

	/**
	 * 写多个线圈响应
	 * 
	 * @param head      请求头
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame writeMultiCoilResp(ModbusTCPHead head, int startAddr, int quantity) {
		return createWithHead(head, ModbusUnitFacotry.writeMultiCoilResp(startAddr, quantity));
	}

	/**
	 * 读输入寄存器，字节指令操作。类似读保持寄存器
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame readInputRegister(int startAddr, int quantity) {
		return createWithUnit(ModbusUnitFacotry.readInputRegister(startAddr, quantity));
	}

	/**
	 * 读输入寄存器响应
	 * 
	 * @param head           请求头
	 * @param inputRegisters 输出寄存器值
	 * @return
	 */
	public static ModbusFrame readInputRegisterResp(ModbusTCPHead head, int[] inputRegisters) {
		return createWithHead(head, ModbusUnitFacotry.readInputRegisterResp(inputRegisters));
	}

	/**
	 * 读保持寄存器，字节指令操作
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame readHoldingRegister(int startAddr, int quantity) {
		return createWithUnit(ModbusUnitFacotry.readHoldingRegister(startAddr, quantity));
	}

	/**
	 * 读保持寄存器响应
	 * 
	 * @param head      请求头
	 * @param registers 输出寄存器值
	 * @return
	 */
	public static ModbusFrame readHoldingRegisterResp(ModbusTCPHead head, int[] registers) {
		return createWithHead(head, ModbusUnitFacotry.readHoldingRegisterResp(registers));
	}

	/**
	 * 读线圈寄存器，位操作
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame readCoil(int startAddr, int quantity) {
		return createWithUnit(ModbusUnitFacotry.readCoil(startAddr, quantity));
	}

	/**
	 * 读线圈响应
	 * 
	 * @param head       请求头
	 * @param coilStatus 输出状态
	 * @return
	 */
	public static ModbusFrame readCoilResp(ModbusTCPHead head, BitSet coilStatus) {
		return createWithHead(head, ModbusUnitFacotry.readCoilResp(coilStatus));
	}

	/**
	 * 读离散输入寄存器，位操作。类似读线圈寄存器
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusFrame readDiscreteInput(int startAddr, int quantity) {
		return createWithUnit(ModbusUnitFacotry.readDiscreteInput(startAddr, quantity));
	}

	/**
	 * 读离散输入响应
	 * 
	 * @param head        请求头
	 * @param inputStatus 输出状态
	 * @return
	 */
	public static ModbusFrame readDiscreteInputResp(ModbusTCPHead head, BitSet inputStatus) {
		return createWithHead(head, ModbusUnitFacotry.readDiscreteInputResp(inputStatus));
	}

	/**
	 * 根据{@link ModbusProtoUnit}创建数据帧
	 * 
	 * @param unit
	 * @return
	 */
	public static ModbusFrame createWithUnit(ModbusProtoUnit unit) {
		int transactionId = calcTransId();
		int pduLength = unit.calcLength();

		ModbusTCPHead header = new ModbusTCPHead(transactionId, ModbusConstant.DEFAULT_PROTOCOL_IDENTIFIER, pduLength,
				ModbusConstant.DEFAULT_UNIT_IDENTIFIER);
		return new ModbusTCPFrame(header, unit);
	}

	/**
	 * 根据{@link ModbusProtoUnit}创建数据帧
	 * 
	 * @param unit
	 * @return
	 */
	private static ModbusFrame createWithHead(ModbusTCPHead head, ModbusProtoUnit unit) {
		return new ModbusTCPFrame(
				new ModbusTCPHead(head.getTransId(), head.getProtoId(), unit.calcLength(), head.getUnitId()), unit);
	}

	/**
	 * 计算transactionId
	 * 
	 * @return
	 */
	private static int calcTransId() {
		transIdCounter.compareAndSet(ModbusConstant.TRANSACTION_IDENTIFIER_MAX, 1);
		return transIdCounter.getAndIncrement();
	}
}
