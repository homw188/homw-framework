package com.homw.modbus.example;

import java.util.BitSet;

import com.homw.modbus.handler.ModbusServerHandlerSupport;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.pdu.request.ReadCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiCoilsRequestUnit;
import com.homw.modbus.struct.rtu.ModbusRTUFrameFactory;

/**
 * Modbus服务端处理器的简单实现，基于RTU
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月9日
 *
 */
public class ModbusRTUServerHandlerExample extends ModbusServerHandlerSupport {
	private static final int process_factor = 800;

	@Override
	protected ModbusFrame readCoilsRequest(ModbusFrame request, ReadCoilsRequestUnit unit) {
		ModbusFrame frame = ModbusRTUFrameFactory.readCoilResp(request.getDevAddr(),
				BitSet.valueOf(new byte[] { (byte) 0xcd, (byte) 0x6b, (byte) 0xb2, (byte) 0x0e, (byte) 0x1b }));
		try {
			Thread.sleep((long) (Math.random() * process_factor));
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		return frame;
	}

	@Override
	protected ModbusFrame writeMultiCoilsRequest(ModbusFrame request, WriteMultiCoilsRequestUnit unit) {
		ModbusFrame frame = ModbusRTUFrameFactory.writeMultiCoilResp(request.getDevAddr(), 0x13, unit.getQuantity());
		try {
			Thread.sleep((long) (Math.random() * process_factor));
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
		return frame;
	}
}
