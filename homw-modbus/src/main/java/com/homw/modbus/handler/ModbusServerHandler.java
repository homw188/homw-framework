package com.homw.modbus.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.ModbusServer;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;
import com.homw.modbus.struct.pdu.WriteSingleCoilUnit;
import com.homw.modbus.struct.pdu.WriteSingleRegisterUnit;
import com.homw.modbus.struct.pdu.request.ReadCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadDiscreteInputsRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadHoldingRegistersRequestUnit;
import com.homw.modbus.struct.pdu.request.ReadInputRegistersRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiCoilsRequestUnit;
import com.homw.modbus.struct.pdu.request.WriteMultiRegistersRequestUnit;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Modbus服务端处理器抽象，兼容RTU和TCP
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
@Sharable
public abstract class ModbusServerHandler extends SimpleChannelInboundHandler<ModbusFrame> {
	protected ModbusServer server;
	private static final Logger log = LoggerFactory.getLogger(ModbusServerHandler.class);

	/**
	 * 设置服务端引用，在服务启动 {@code ModbusServer#start(int, ModbusServerHandler)} 前调用
	 * 
	 * @param server
	 */
	public void setServer(ModbusServer server) {
		this.server = server;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ModbusFrame frame) throws Exception {
		log.debug("服务端收到数据包:" + frame);

		ModbusFrame response = dispatchRequest(frame);

		handleRequest(ctx, response);
	}

	/**
	 * 分发请求，路由
	 * 
	 * @param request
	 * @return 响应
	 */
	private ModbusFrame dispatchRequest(ModbusFrame request) {
		ModbusFrame response;
		ModbusProtoUnit unit = request.getProtoUnit();
		if (unit instanceof WriteSingleCoilUnit) {
			response = writeSingleCoil(request, (WriteSingleCoilUnit) unit);
		} else if (unit instanceof WriteSingleRegisterUnit) {
			response = writeSingleRegister(request, (WriteSingleRegisterUnit) unit);
		} else if (unit instanceof ReadCoilsRequestUnit) {
			response = readCoilsRequest(request, (ReadCoilsRequestUnit) unit);
		} else if (unit instanceof ReadDiscreteInputsRequestUnit) {
			response = readDiscreteInputsRequest(request, (ReadDiscreteInputsRequestUnit) unit);
		} else if (unit instanceof ReadInputRegistersRequestUnit) {
			response = readInputRegistersRequest(request, (ReadInputRegistersRequestUnit) unit);
		} else if (unit instanceof ReadHoldingRegistersRequestUnit) {
			response = readHoldingRegistersRequest(request, (ReadHoldingRegistersRequestUnit) unit);
		} else if (unit instanceof WriteMultiRegistersRequestUnit) {
			response = writeMultiRegistersRequest(request, (WriteMultiRegistersRequestUnit) unit);
		} else if (unit instanceof WriteMultiCoilsRequestUnit) {
			response = writeMultiCoilsRequest(request, (WriteMultiCoilsRequestUnit) unit);
		} else {
			throw new UnsupportedOperationException("该功能码不支持：" + unit.getFuncCode());
		}
		return response;
	}

	/**
	 * 写单个线圈
	 * 
	 * @param request
	 * @param unit
	 * @return
	 */
	protected abstract ModbusFrame writeSingleCoil(ModbusFrame request, WriteSingleCoilUnit unit);

	/**
	 * 写单个寄存器
	 * 
	 * @param request
	 * @param unit
	 * @return
	 */
	protected abstract ModbusFrame writeSingleRegister(ModbusFrame request, WriteSingleRegisterUnit unit);

	/**
	 * 读线圈
	 * 
	 * @param request
	 * @param unit
	 * @return
	 */
	protected abstract ModbusFrame readCoilsRequest(ModbusFrame request, ReadCoilsRequestUnit unit);

	/**
	 * 读离散输入寄存器
	 * 
	 * @param request
	 * @param unit
	 * @return
	 */
	protected abstract ModbusFrame readDiscreteInputsRequest(ModbusFrame request, ReadDiscreteInputsRequestUnit unit);

	/**
	 * 读离散输入保持寄存器
	 * 
	 * @param request
	 * @param unit
	 * @return
	 */
	protected abstract ModbusFrame readInputRegistersRequest(ModbusFrame request, ReadInputRegistersRequestUnit unit);

	/**
	 * 读保持寄存器
	 * 
	 * @param request
	 * @param unit
	 * @return
	 */
	protected abstract ModbusFrame readHoldingRegistersRequest(ModbusFrame request,
			ReadHoldingRegistersRequestUnit unit);

	/**
	 * 写多个寄存器
	 * 
	 * @param request
	 * @param unit
	 * @return
	 */
	protected abstract ModbusFrame writeMultiRegistersRequest(ModbusFrame request, WriteMultiRegistersRequestUnit unit);

	/**
	 * 写多个线圈
	 * 
	 * @param request
	 * @param unit
	 * @return
	 */
	protected abstract ModbusFrame writeMultiCoilsRequest(ModbusFrame request, WriteMultiCoilsRequestUnit unit);

	/**
	 * 处理客户端请求
	 * 
	 * @param ctx
	 * @param response
	 */
	protected abstract void handleRequest(ChannelHandlerContext ctx, ModbusFrame response);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.warn(cause.getLocalizedMessage());
		// super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if (server != null) {
			server.removeClient(ctx.channel());
		} else {
			log.warn("服务端引用为空，请在服务启动前设置");
		}
		super.channelInactive(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if (server != null) {
			server.addClient(ctx.channel());
		} else {
			log.warn("服务端引用为空，请在服务启动前设置");
		}
		super.channelActive(ctx);
	}
}
