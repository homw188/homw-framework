package com.homw.modbus.handler;

import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.pdu.WriteSingleCoilUnit;
import com.homw.modbus.struct.pdu.WriteSingleRegisterUnit;
import com.homw.modbus.struct.pdu.request.ReadCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadDiscreteInputsRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadHoldingRegistersRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadInputRegistersRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiRegistersRequestUnit;

import io.netty.channel.ChannelHandlerContext;

/**
 * Modbus服务端handler适配器
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月14日
 *
 */
public class ModbusServerHandlerSupport extends ModbusServerHandler {
	/**
	 * 写单个线圈请求响应报文一样，默认返回请求报文
	 */
	@Override
	protected ModbusFrame writeSingleCoil(ModbusFrame request, WriteSingleCoilUnit unit) {
		return request;
	}

	/**
	 * 写单个寄存器请求响应报文一样，默认返回请求报文
	 */
	@Override
	protected ModbusFrame writeSingleRegister(ModbusFrame request, WriteSingleRegisterUnit unit) {
		return request;
	}

	@Override
	protected ModbusFrame readCoilsRequest(ModbusFrame request, ReadCoilsRequestUnit unit) {
		return null;
	}

	@Override
	protected ModbusFrame readDiscreteInputsRequest(ModbusFrame request, ReadDiscreteInputsRequestUnit unit) {
		return null;
	}

	@Override
	protected ModbusFrame readInputRegistersRequest(ModbusFrame request, ReadInputRegistersRequestUnit unit) {
		return null;
	}

	@Override
	protected ModbusFrame readHoldingRegistersRequest(ModbusFrame request, ReadHoldingRegistersRequestUnit unit) {
		return null;
	}

	@Override
	protected ModbusFrame writeMultiRegistersRequest(ModbusFrame request, WriteMultiRegistersRequestUnit unit) {
		return null;
	}

	@Override
	protected ModbusFrame writeMultiCoilsRequest(ModbusFrame request, WriteMultiCoilsRequestUnit unit) {
		return null;
	}

	/**
	 * 默认输出响应到客户端
	 */
	@Override
	protected void handleRequest(ChannelHandlerContext ctx, ModbusFrame response) {
		if (response != null) {
			ctx.channel().writeAndFlush(response);
		}
	}

}
