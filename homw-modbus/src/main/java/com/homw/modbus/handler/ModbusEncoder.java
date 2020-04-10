package com.homw.modbus.handler;

import com.homw.modbus.struct.ModbusFrame;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Modbus编码器，兼容RTU和TCP
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
public class ModbusEncoder extends ChannelOutboundHandlerAdapter {
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof ModbusFrame) {
			ModbusFrame frame = (ModbusFrame) msg;
			ctx.writeAndFlush(frame.encode());
		} else {
			ctx.writeAndFlush(msg);
		}
	}
}
