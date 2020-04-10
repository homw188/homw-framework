package com.homw.modbus.struct.tcp;

import com.homw.modbus.struct.ModbusConstant;
import com.homw.modbus.struct.ModbusFrame;
import com.homw.modbus.struct.pdu.ModbusProtoUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Modbus TCP数据帧
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月12日
 *
 */
public class ModbusTCPFrame implements ModbusFrame {
	private final ModbusTCPHead head;
	private final ModbusProtoUnit unit;

	public ModbusTCPFrame(ModbusTCPHead header, ModbusProtoUnit unit) {
		this.head = header;
		this.unit = unit;
	}

	public ModbusTCPHead getHead() {
		return head;
	}

	public ModbusProtoUnit getProtoUnit() {
		return unit;
	}

	public ByteBuf encode() {
		ByteBuf buf = Unpooled.buffer();

		buf.writeBytes(head.encode());
		buf.writeBytes(unit.encode());

		return buf;
	}

	@Override
	public void decode(ByteBuf data) {
		head.decode(data);
		data.skipBytes(1);// function code
		unit.decode(data);
	}

	@Override
	public short getDevAddr() {
		return 0;
	}

	@Override
	public short getFuncCode() {
		return this.unit.getFuncCode();
	}

	@Override
	public String getModbusKey() {
		return String.valueOf(head.getTransId());
	}

	@Override
	public String getModbusErrKey() {
		return String.valueOf(head.getTransId());
	}

	@Override
	public boolean isError() {
		return getFuncCode() >= ModbusConstant.ERROR_OFFSET;
	}

	@Override
	public String toString() {
		return "ModbusFrame{" + "head=" + head + ", unit=" + unit + '}';
	}
}
