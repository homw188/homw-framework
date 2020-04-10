package com.homw.modbus.example;

import java.util.BitSet;

import com.homw.modbus.handler.ModbusServerHandlerSupport;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.pdu.request.ReadCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadDiscreteInputsRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadHoldingRegistersRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadInputRegistersRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiRegistersRequestUnit;
import com.homw.modbus.struct.tcp.ModbusTCPFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

/**
 * Modbus服务端处理器的简单实现，基于TCP
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusTCPServerHandlerExample extends ModbusServerHandlerSupport {
	@Override
	protected ModbusFrame readCoilsRequest(ModbusFrame request, ReadCoilsRequestUnit unit) {
		BitSet coils = new BitSet(unit.getQuantity());
		coils.set(0);
		coils.set(5);
		coils.set(8);
		return ModbusTCPFrameFactory.readCoilResp(((ModbusTCPFrame) request).getHead(), coils);
	}

	@Override
	protected ModbusFrame readDiscreteInputsRequest(ModbusFrame request, ReadDiscreteInputsRequestUnit unit) {
		BitSet coils = new BitSet(unit.getQuantity());
		coils.set(0);
		coils.set(5);
		coils.set(8);
		return ModbusTCPFrameFactory.readDiscreteInputResp(((ModbusTCPFrame) request).getHead(), coils);
	}

	@Override
	protected ModbusFrame readInputRegistersRequest(ModbusFrame request, ReadInputRegistersRequestUnit unit) {
		int[] registers = new int[unit.getQuantity()];
		registers[0] = 0xFFFF;
		registers[1] = 0xF0F0;
		registers[2] = 0x0F0F;
		return ModbusTCPFrameFactory.readInputRegisterResp(((ModbusTCPFrame) request).getHead(), registers);
	}

	@Override
	protected ModbusFrame readHoldingRegistersRequest(ModbusFrame request, ReadHoldingRegistersRequestUnit unit) {
		int[] registers = new int[unit.getQuantity()];
		registers[0] = 0xFFFF;
		registers[1] = 0xF0F0;
		registers[2] = 0x0F0F;

		return ModbusTCPFrameFactory.readHoldingRegisterResp(((ModbusTCPFrame) request).getHead(), registers);
	}

	@Override
	protected ModbusFrame writeMultiRegistersRequest(ModbusFrame request, WriteMultiRegistersRequestUnit unit) {
		return ModbusTCPFrameFactory.writeMultiRegisterResp(((ModbusTCPFrame) request).getHead(), unit.getStartAddr(),
				unit.getQuantity());
	}

	@Override
	protected ModbusFrame writeMultiCoilsRequest(ModbusFrame request, WriteMultiCoilsRequestUnit unit) {
		return ModbusTCPFrameFactory.writeMultiCoilResp(((ModbusTCPFrame) request).getHead(), unit.getStartAddr(),
				unit.getQuantity());
	}
}
