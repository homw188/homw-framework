package com.homw.modbus.handler.rtu;

import com.homw.modbus.handler.ModbusEncoder;
import com.homw.modbus.handler.ModbusServerHandler;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFrame;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

/**
 * Modbus处理器链初始化，基于RTU
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
public class ModbusRTUChannelInitializer extends ChannelInitializer<SocketChannel> {
	private boolean serverMode = false;
	private final SimpleChannelInboundHandler<ModbusFrame> handler;

	public ModbusRTUChannelInitializer(SimpleChannelInboundHandler<ModbusFrame> handler) {
		this.handler = handler;
		this.serverMode = handler instanceof ModbusServerHandler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

    	 /*
         * Modbus RTU Frame Description
         *
         * <-------------------------------- ADU ------------------------------>
         *                   <------------- PDU ------------->
         * +----------------++---------------+---------------++----------------+
         * | Device Address || Function Code | Data          || CRC            |
         * | (1 Byte)       || (1 Byte)      | (N Byte)      || (2 Byte)       |
         * +----------------++---------------+---------------++----------------+
         */
		pipeline.addLast("framer", new ModbusRTUFrameDecoder(ModbusConstant.ADU_MAX_LENGTH, serverMode));

		// Modbus encoder, decoder
		pipeline.addLast("encoder", new ModbusEncoder());
		pipeline.addLast("decoder", new ModbusRTUDecoder(serverMode));

		if (handler != null) {
			if (serverMode) {
				// server
				pipeline.addLast("requestHandler", handler);
			} else {
				// async client
				pipeline.addLast("responseHandler", handler);
			}
		}
	}
}
