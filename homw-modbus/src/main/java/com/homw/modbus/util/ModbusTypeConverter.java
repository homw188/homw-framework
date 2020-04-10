package com.homw.modbus.util;

import java.util.BitSet;

import com.homw.modbus.struct.ModbusConstant;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 数据类型转换器
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月7日
 *
 */
public class ModbusTypeConverter {
	/**
	 * 读取字节数组
	 * 
	 * @param buf
	 * @param len
	 * @return
	 */
	public static byte[] readBytes(ByteBuf buf, int len) {
		byte[] arr = new byte[len];
		for (int i = 0; i < len; i++) {
			arr[i] = buf.readByte();
		}
		return arr;
	}

	/**
	 * 获取验证码byte数组，基于Modbus CRC16的校验算法
	 * 
	 * @param arr
	 * @return unsigned short
	 */
	public static int calcCRC16(byte[] arr) {
		// 预置 1 个 16 位的寄存器为十六进制FFFF, 称此寄存器为 CRC寄存器。
		int crc = 0xFFFF; // unsigned short
		int i, j;
		for (i = 0; i < arr.length; i++) {
			// 把第一个 8 位二进制数据 与 16 位的 CRC寄存器的低 8 位相异或, 把结果放于 CRC寄存器
			crc = ((crc & 0xFF00) | (crc & 0x00FF) ^ (arr[i] & 0xFF));
			for (j = 0; j < 8; j++) {
				// 把 CRC 寄存器的内容右移一位( 朝低位)用 0 填补最高位, 并检查右移后的移出位
				if ((crc & 0x0001) > 0) {
					// 如果移出位为 1, CRC寄存器与多项式A001进行异或
					crc = crc >> 1;
					crc = crc ^ 0xA001;
				} else {
					// 如果移出位为 0,再次右移一位
					crc = crc >> 1;
				}
			}
		}
		return crc;
	}

	/**
	 * 获取验证码byte数组，基于Modbus CRC16的校验算法
	 * 
	 * @param arr
	 * @return unsigned short, little endian
	 */
	public static int calcCRC16LE(byte[] arr) {
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(calcCRC16(arr));
		return buf.readUnsignedShortLE();
	}

	/**
	 * 是否模拟量地址
	 * 
	 * @param addr
	 * @return
	 */
	public static boolean isAnalogAddr(int addr) {
		return addr >= ModbusConstant.ANALOG_ADDR_OFFSET;
	}

	/**
	 * 转换模拟量地址
	 * 
	 * @param analogAddr
	 * @return
	 */
	public static int toRealAddr(int analogAddr) {
		if (isAnalogAddr(analogAddr)) {
			return analogAddr - ModbusConstant.ANALOG_ADDR_OFFSET;
		} else {
			return analogAddr;
		}
	}

	public static String getBinaryStr(short byteCount, BitSet coilStatus) {
		StringBuilder bitString = new StringBuilder();
		int bitCount = 0;
		for (int i = byteCount * 8 - 1; i >= 0; i--) {
			boolean state = coilStatus.get(i);
			bitString.append(state ? '1' : '0');

			bitCount++;
			if (bitCount == 8 && i > 0) {
				bitCount = 0;
				bitString.append("#");
			}
		}
		return bitString.toString();
	}
}