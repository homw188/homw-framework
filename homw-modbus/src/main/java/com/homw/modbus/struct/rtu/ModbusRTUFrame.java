package com.homw.modbus.struct.rtu;

import com.homw.common.util.CodecUtil;
import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;
import com.homw.modbus.util.ModbusKeyGenerator;
import com.homw.modbus.util.ModbusTypeConverter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 基于RTU的Modebus数据帧
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月6日
 *
 */
public class ModbusRTUFrame implements ModbusFrame {
	/**
	 * 设备地址
	 */
	protected short devAddr;// unsigned byte

	/**
	 * 协议单元
	 */
	protected ModbusProtoUnit unit;

	/**
	 * 校验码
	 */
	protected int crcCode;

	public ModbusRTUFrame(short devAddr, ModbusProtoUnit unit) {
		this.devAddr = devAddr;
		this.unit = unit;
	}

	/**
	 * 编码
	 * 
	 * @return
	 */
	public final ByteBuf encode() {
		ByteBuf buf = Unpooled.buffer();

		buf.writeByte(this.devAddr);
		buf.writeBytes(unit.encode());

		this.crcCode = calcFrameCRC();
		buf.writeShort(crcCode);
		return buf;
	}

	/**
	 * 解码
	 * 
	 * @param data
	 */
	public void decode(ByteBuf data) {
		this.devAddr = data.readUnsignedByte();
		data.skipBytes(1);// function code
		unit.decode(data);
		this.crcCode = data.readUnsignedShort();
	}

	/**
	 * 计算数据帧校验和
	 */
	public int calcFrameCRC() {
		byte[] bytes = unit.getUnitBytes();

		byte[] frame = new byte[1 + bytes.length];
		frame[0] = (byte) devAddr;
		System.arraycopy(bytes, 0, frame, 1, bytes.length);

		return ModbusTypeConverter.calcCRC16LE(frame);
	}

	/**
	 * 基于Modbus CRC算法，校验数据包
	 * 
	 * @return 是否合法
	 */
	public boolean verifyCRC() {
		return this.crcCode == calcFrameCRC();
	}

	public short getFuncCode() {
		return this.unit.getFuncCode();
	}

	public short getDevAddr() {
		return this.devAddr;
	}

	@Override
	public ModbusProtoUnit getProtoUnit() {
		return this.unit;
	}

	@Override
	public String getModbusKey() {
		return ModbusKeyGenerator.build(this);
	}

	@Override
	public String getModbusErrKey() {
		return ModbusKeyGenerator.buildErr(this);
	}

	@Override
	public boolean isError() {
		return getFuncCode() >= ModbusConstant.ERROR_OFFSET;
	}

	public void setDevAddr(byte devAddr) {
		this.devAddr = devAddr;
	}

	public int getCrcCode() {
		return crcCode;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" + "devAddr=" + CodecUtil.encodeHex(devAddr) + ", unit=" + unit
				+ ", CRC=" + CodecUtil.encodeHex(crcCode) + "}";
	}
}
