package com.homw.modbus.handler.tcp;

import com.homw.modbus.handler.ModbusClientHandler;
import com.homw.modbus.handler.ModbusEncoder;
import com.homw.modbus.handler.ModbusServerHandler;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFrame;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Modbus处理器链初始化，基于TCP
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusTCPChannelInitializer extends ChannelInitializer<SocketChannel> {
	private final SimpleChannelInboundHandler<ModbusFrame> handler;

	public ModbusTCPChannelInitializer(SimpleChannelInboundHandler<ModbusFrame> handler) {
		this.handler = handler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

        /*
         * Modbus TCP Frame Description
         *  - max. 260 Byte (ADU = 7 Byte MBAP + 253 Byte PDU)
         *  - Length field includes Unit Identifier + PDU
         *
         * <----------------------------------------------- ADU -------------------------------------------------------->
         * <---------------------------- MBAP -----------------------------------------><------------- PDU ------------->
         * +------------------------+---------------------+----------+-----------------++---------------+---------------+
         * | Transaction Identifier | Protocol Identifier | Length   | Unit Identifier || Function Code | Data          |
         * | (2 Byte)               | (2 Byte)            | (2 Byte) | (1 Byte)        || (1 Byte)      | (1 - 252 Byte |
         * +------------------------+---------------------+----------+-----------------++---------------+---------------+
         */
		pipeline.addLast("framer", new LengthFieldBasedFrameDecoder(ModbusConstant.ADU_MAX_LENGTH, 4, 2));

		// Modbus encoder, decoder
		pipeline.addLast("encoder", new ModbusEncoder());
		pipeline.addLast("decoder", new ModbusTCPDecoder(handler instanceof ModbusServerHandler));

		if (handler != null) {
			if (handler instanceof ModbusServerHandler) {
				pipeline.addLast("requestHandler", handler);
			} else if (handler instanceof ModbusClientHandler) {
				pipeline.addLast("responseHandler", handler);
			}
		}
	}
}
