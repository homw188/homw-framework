package com.homw.modbus.handler.rtu;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.modbus.struct.ModbusFuncCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;

/**
 * Modbus拆包、粘包解码器，基于RTU协议，适用于常用功能包结构
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 * 
 * @see LengthFieldBasedFrameDecoder
 *
 */
public class ModbusRTUFrameDecoder extends ByteToMessageDecoder {
	private final int maxFrameLength;
	private final boolean failFast;
	private final boolean serverMode;

	private boolean discardingTooLongFrame;
	private long bytesToDiscard;
	private long tooLongFrameLength;

	private final int funcFieldOffset = 1;
	private final int funcFieldEndOffset = funcFieldOffset + 1; // 1: function code length

	private static Logger log = LoggerFactory.getLogger(ModbusRTUFrameDecoder.class);

	public ModbusRTUFrameDecoder(int maxFrameLength) {
		this(maxFrameLength, false);
	}

	public ModbusRTUFrameDecoder(int maxFrameLength, boolean serverMode) {
		this(maxFrameLength, serverMode, true);
	}

	public ModbusRTUFrameDecoder(int maxFrameLength, boolean serverMode, boolean failFast) {
		this.maxFrameLength = maxFrameLength;
		this.failFast = failFast;
		this.serverMode = serverMode;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		Object decoded = decode(ctx, in);
		if (decoded != null) {
			out.add(decoded);
		}
	}

	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if (discardingTooLongFrame) {
			long bytesToDiscard = this.bytesToDiscard;
			int localBytesToDiscard = (int) Math.min(bytesToDiscard, in.readableBytes());
			in.skipBytes(localBytesToDiscard);
			bytesToDiscard -= localBytesToDiscard;
			this.bytesToDiscard = bytesToDiscard;

			failIfNecessary(false);
		}

		if (in.readableBytes() < funcFieldEndOffset) {
			return null;
		}

		int actualFuncFieldOffset = in.readerIndex() + funcFieldOffset;
		int frameLength = calcFrameLength(in, actualFuncFieldOffset);

		if (frameLength == -1) {
			return null;
		}

		if (frameLength < 0) {
			in.skipBytes(funcFieldEndOffset);
			throw new CorruptedFrameException("negative pre-adjustment length field: " + frameLength);
		}

		if (frameLength < funcFieldEndOffset) {
			in.skipBytes(funcFieldEndOffset);
			throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less "
					+ "than lengthFieldEndOffset: " + funcFieldEndOffset);
		}

		if (frameLength > maxFrameLength) {
			long discard = frameLength - in.readableBytes();
			tooLongFrameLength = frameLength;

			log.warn("数据帧长度：" + frameLength + "超过最大长度：" + maxFrameLength);

			if (discard < 0) {
				// buffer contains more bytes then the frameLength so we can discard all now
				in.skipBytes((int) frameLength);
			} else {
				// Enter the discard mode and discard everything received so far.
				discardingTooLongFrame = true;
				bytesToDiscard = discard;
				in.skipBytes(in.readableBytes());
			}
			failIfNecessary(true);
			return null;
		}

		if (in.readableBytes() < frameLength) {
			return null;
		}

		// extract frame
		int readerIndex = in.readerIndex();
		ByteBuf frame = extractFrame(ctx, in, readerIndex, frameLength);

		// move pointer
		in.readerIndex(readerIndex + frameLength);
		return frame;
	}

	private void failIfNecessary(boolean firstDetectionOfTooLongFrame) {
		if (bytesToDiscard == 0) {
			// Reset to the initial state and tell the handlers that
			// the frame was too large.
			long tooLongFrameLength = this.tooLongFrameLength;
			this.tooLongFrameLength = 0;
			discardingTooLongFrame = false;
			if (!failFast || failFast && firstDetectionOfTooLongFrame) {
				fail(tooLongFrameLength);
			}
		} else {
			// Keep discarding and notify handlers if necessary.
			if (failFast && firstDetectionOfTooLongFrame) {
				fail(tooLongFrameLength);
			}
		}
	}

	private void fail(long frameLength) {
		if (frameLength > 0) {
			throw new TooLongFrameException(
					"Adjusted frame length exceeds " + maxFrameLength + ": " + frameLength + " - discarded");
		} else {
			throw new TooLongFrameException("Adjusted frame length exceeds " + maxFrameLength + " - discarding");
		}
	}

	/**
	 * 提取数据帧
	 * 
	 * @param ctx
	 * @param buffer
	 * @param index
	 * @param length
	 * @return
	 */
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
		ByteBuf frame = ctx.alloc().buffer(length);
		frame.writeBytes(buffer, index, length);
		return frame;
	}

	/**
	 * 计算Modbus数据帧长度
	 * 
	 * @param buf
	 * @param offset 功能码偏移量
	 * @return
	 */
	protected int calcFrameLength(ByteBuf buf, int offset) {
		short funcCode = buf.getUnsignedByte(offset);

		int frameLength = 0;
		switch (funcCode) {
			case ModbusFuncCode.READ_COILS:
			case ModbusFuncCode.READ_DISCRETE_INPUTS:
			case ModbusFuncCode.READ_HOLDING_REGISTERS:
			case ModbusFuncCode.READ_INPUT_REGISTERS:
				if (serverMode) {
					frameLength = 8;
				} else {
	        		/*+----------------++---------------+-------------+----------++---------+
	                * | Device Address || Function Code | Data Length | Data     || CRC     |
	                * | (1 Byte)       || (1 Byte)      | (1 Byte)    | (N Byte) || (2 Byte)|
	                * +----------------++---------------+-------------+----------++---------+
	        		*/
					if (buf.readableBytes() < funcFieldEndOffset + 1) {
						return -1;
					}
					byte dataLen = buf.getByte(offset + 1);
					frameLength = 3 + dataLen + 2;
				}
				break;
			case ModbusFuncCode.WRITE_SINGLE_COIL:
			case ModbusFuncCode.WRITE_SINGLE_REGISTER:
				frameLength = 8;
				break;
			case ModbusFuncCode.WRITE_MULTIPLE_COILS:
			case ModbusFuncCode.WRITE_MULTIPLE_REGISTERS:
				if (serverMode) {
					if (buf.readableBytes() < funcFieldEndOffset + 5) {
						return -1;
					}
					byte dataLen = buf.getByte(offset + 5);
					frameLength = 7 + dataLen + 2;
				} else {
					frameLength = 8;
				}
				break;
			default:
				frameLength = calcErrFrameLength(funcCode);
		}
		return frameLength;
	}

	/**
	 * 计算Modbus异常数据帧长度
	 * 
	 * @param funcErr
	 * @return
	 */
	private int calcErrFrameLength(short funcErr) {
		short func = ModbusFuncCode.convertNormalCode(funcErr);
		int frameLength = 0;
		switch (func) {
			case ModbusFuncCode.READ_COILS:
			case ModbusFuncCode.READ_DISCRETE_INPUTS:
			case ModbusFuncCode.READ_HOLDING_REGISTERS:
			case ModbusFuncCode.READ_INPUT_REGISTERS:
			case ModbusFuncCode.WRITE_SINGLE_COIL:
			case ModbusFuncCode.WRITE_SINGLE_REGISTER:
			case ModbusFuncCode.WRITE_MULTIPLE_COILS:
			case ModbusFuncCode.WRITE_MULTIPLE_REGISTERS:
				frameLength = 5;
				break;
			default:
				throw new DecoderException("不支持Modbus功能码: " + funcErr);
		}
		return frameLength;
	}
}
