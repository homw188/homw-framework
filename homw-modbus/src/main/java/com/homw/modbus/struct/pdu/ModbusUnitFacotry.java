package com.homw.modbus.struct.pdu;

import java.util.BitSet;

import com.homw.modbus.struct.ModbusFuncCode;
import com.homw.modbus.struct.pdu.request.ReadCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadDiscreteInputsRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadHoldingRegistersRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadInputRegistersRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiRegistersRequestUnit;
import com.homw.modbus.struct.pdu.response.ReadCoilsResponseUnit;
import com.homw.modbus.struct.pdu.response.ReadDiscreteInputsResponseUnit;
import com.homw.modbus.struct.pdu.response.ReadHoldingRegistersResponseUnit;
import com.homw.modbus.struct.pdu.response.ReadInputRegistersResponseUnit;
import com.homw.modbus.struct.pdu.response.WriteMultiCoilsResponseUnit;
import com.homw.modbus.struct.pdu.response.WriteMultiRegistersResponseUnit;

/**
 * Modbus协议单元工厂
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月14日
 *
 */
public class ModbusUnitFacotry {
	/**
	 * 创建空的协议单元（PDU）
	 * 
	 * @param func    功能码
	 * @param isReply 是否应答报文
	 * @return
	 */
	public static ModbusProtoUnit createEmpty(short func, boolean isReply) {
		ModbusProtoUnit unit = null;
		switch (func) {
		case ModbusFuncCode.READ_COILS:
			if (!isReply) {
				unit = new ReadCoilsRequestUnit();
			} else {
				unit = new ReadCoilsResponseUnit();
			}
			break;
		case ModbusFuncCode.READ_DISCRETE_INPUTS:
			if (!isReply) {
				unit = new ReadDiscreteInputsRequestUnit();
			} else {
				unit = new ReadDiscreteInputsResponseUnit();
			}
			break;
		case ModbusFuncCode.READ_INPUT_REGISTERS:
			if (!isReply) {
				unit = new ReadInputRegistersRequestUnit();
			} else {
				unit = new ReadInputRegistersResponseUnit();
			}
			break;
		case ModbusFuncCode.READ_HOLDING_REGISTERS:
			if (!isReply) {
				unit = new ReadHoldingRegistersRequestUnit();
			} else {
				unit = new ReadHoldingRegistersResponseUnit();
			}
			break;
		case ModbusFuncCode.WRITE_SINGLE_COIL:
			unit = new WriteSingleCoilUnit();
			break;
		case ModbusFuncCode.WRITE_SINGLE_REGISTER:
			unit = new WriteSingleRegisterUnit();
			break;
		case ModbusFuncCode.WRITE_MULTIPLE_COILS:
			if (!isReply) {
				unit = new WriteMultiCoilsRequestUnit();
			} else {
				unit = new WriteMultiCoilsResponseUnit();
			}
			break;
		case ModbusFuncCode.WRITE_MULTIPLE_REGISTERS:
			if (!isReply) {
				unit = new WriteMultiRegistersRequestUnit();
			} else {
				unit = new WriteMultiRegistersResponseUnit();
			}
			break;
		default:
			unit = createEmptyErr(func);
		}
		return unit;
	}

	/**
	 * 创建空的Modbus异常单元
	 * 
	 * @param funcErr 异常功能码
	 * @return
	 */
	public static ModbusErrorUnit createEmptyErr(short funcErr) {
		short func = ModbusFuncCode.convertNormalCode(funcErr);
		ModbusErrorUnit unit = null;
		switch (func) {
		case ModbusFuncCode.READ_COILS:
		case ModbusFuncCode.READ_DISCRETE_INPUTS:
		case ModbusFuncCode.READ_INPUT_REGISTERS:
		case ModbusFuncCode.READ_HOLDING_REGISTERS:
		case ModbusFuncCode.WRITE_SINGLE_COIL:
		case ModbusFuncCode.WRITE_SINGLE_REGISTER:
		case ModbusFuncCode.WRITE_MULTIPLE_COILS:
		case ModbusFuncCode.WRITE_MULTIPLE_REGISTERS:
			unit = new ModbusErrorUnit(funcErr);
			break;
		default:
			unit = new ModbusErrorUnit(funcErr, (short) 1);
		}
		return unit;
	}

	/**
	 * 读线圈寄存器，位操作
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusProtoUnit readCoil(int startAddr, int quantity) {
		return new ReadCoilsRequestUnit(startAddr, quantity);
	}

	/**
	 * 读线圈响应
	 * 
	 * @param coilStatus 输出状态
	 * @return
	 */
	public static ModbusProtoUnit readCoilResp(BitSet coilStatus) {
		return new ReadCoilsResponseUnit(coilStatus);
	}

	/**
	 * 读离散输入寄存器，位操作。类似读线圈寄存器
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusProtoUnit readDiscreteInput(int startAddr, int quantity) {
		return new ReadDiscreteInputsRequestUnit(startAddr, quantity);
	}

	/**
	 * 读离散输入响应
	 * 
	 * @param inputStatus 输出状态
	 * @return
	 */
	public static ModbusProtoUnit readDiscreteInputResp(BitSet inputStatus) {
		return new ReadDiscreteInputsResponseUnit(inputStatus);
	}

	/**
	 * 读保持寄存器，字节指令操作
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusProtoUnit readHoldingRegister(int startAddr, int quantity) {
		return new ReadHoldingRegistersRequestUnit(startAddr, quantity);
	}

	/**
	 * 读保持寄存器响应
	 * 
	 * @param registers 输出寄存器值
	 * @return
	 */
	public static ModbusProtoUnit readHoldingRegisterResp(int[] registers) {
		return new ReadHoldingRegistersResponseUnit(registers);
	}

	/**
	 * 读输入寄存器，字节指令操作。类似读保持寄存器
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusProtoUnit readInputRegister(int startAddr, int quantity) {
		return new ReadInputRegistersRequestUnit(startAddr, quantity);
	}

	/**
	 * 读输入寄存器响应
	 * 
	 * @param inputRegisters 输出寄存器值
	 * @return
	 */
	public static ModbusProtoUnit readInputRegisterResp(int[] inputRegisters) {
		return new ReadInputRegistersResponseUnit(inputRegisters);
	}

	/**
	 * 写单个线圈，位操作。请求响应报文一样，均调用该方法
	 * 
	 * @param address 地址
	 * @param state   状态（true:0xFF00; false:0x0000）
	 * @return
	 */
	public static ModbusProtoUnit writeSingleCoil(int address, boolean state) {
		return new WriteSingleCoilUnit(address, state);
	}

	/**
	 * 写单个保持寄存器，字节指令操作。请求响应报文一样，均调用该方法
	 * 
	 * @param address 地址
	 * @param value   输出值
	 * @return
	 */
	public static ModbusProtoUnit writeSingleRegister(int address, int value) {
		return new WriteSingleRegisterUnit(address, value);
	}

	/**
	 * 写多个线圈
	 * 
	 * @param address      开始地址
	 * @param quantity     输出数量
	 * @param outputsValue 输出值
	 * @return
	 */
	public static ModbusProtoUnit writeMultiCoil(int address, int quantity, BitSet outputsValue) {
		return new WriteMultiCoilsRequestUnit(address, quantity, outputsValue);
	}

	/**
	 * 写多个线圈响应
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusProtoUnit writeMultiCoilResp(int startAddr, int quantity) {
		return new WriteMultiCoilsResponseUnit(startAddr, quantity);
	}

	/**
	 * 写多个保持寄存器，字节指令操作
	 * 
	 * @param address   开始地址
	 * @param quantity  输出数量
	 * @param registers 输出值
	 * @return
	 */
	public static ModbusProtoUnit writeMultiRegister(int address, int quantity, int[] registers) {
		return new WriteMultiRegistersRequestUnit(address, quantity, registers);
	}

	/**
	 * 写多个保持寄存器响应
	 * 
	 * @param startAddr 开始地址
	 * @param quantity  输出数量
	 * @return
	 */
	public static ModbusProtoUnit writeMultiRegisterResp(int startAddr, int quantity) {
		return new WriteMultiRegistersResponseUnit(startAddr, quantity);
	}
}
