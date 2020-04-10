package com.homw.modbus.handler.tcp;

import java.util.List;

import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.tcp.ModbusTCPFrame;
import com.homw.modbus.struct.tcp.ModbusTCPFrameFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Modbus解码器，基于TCP
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月13日
 *
 */
public class ModbusTCPDecoder extends ByteToMessageDecoder {
	private final boolean serverMode;

	public ModbusTCPDecoder(boolean serverMode) {
		this.serverMode = serverMode;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer.capacity() < ModbusConstant.MBAP_LENGTH + 1 /* Function Code */) {
			return;
		}

		short func = buffer.getUnsignedByte(ModbusConstant.MBAP_LENGTH);
		ModbusTCPFrame frame = ModbusTCPFrameFactory.createEmpty(func, !serverMode);
		frame.decode(buffer);

		out.add(frame);
	}
}
