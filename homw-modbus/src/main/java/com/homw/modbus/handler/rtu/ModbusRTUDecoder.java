package com.homw.modbus.handler.rtu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.rtu.ModbusRTUFrame;
import com.homw.modbus.struct.rtu.ModbusRTUFrameFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Modbus解码器，基于RTU
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
public class ModbusRTUDecoder extends ByteToMessageDecoder {
	private final boolean serverMode;
	private static Logger log = LoggerFactory.getLogger(ModbusRTUDecoder.class);

	public ModbusRTUDecoder(boolean serverMode) {
		this.serverMode = serverMode;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
		if (buffer != null && buffer.capacity() >= ModbusConstant.RTU_MIN_FRAME_LENGTH) {
			byte funcCode = buffer.getByte(1);
			ModbusRTUFrame frame = ModbusRTUFrameFactory.createEmpty(funcCode, !serverMode);
			if (frame != null) {
				frame.decode(buffer);

				if (frame.verifyCRC()) {
					out.add(frame);
				} else {
					log.warn("该数据包:" + frame + " CRC校验不合法，丢弃");
				}
			}
		}
	}
}
