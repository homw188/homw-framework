package com.homw.tool.api.dashi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DashiDoorApi {
	private final static byte[] hex = "0123456789ABCDEF".getBytes();
	private static DatagramSocket ds = null;
	private static final int timeout = 3000;

	static {
		try {
			ds = new DatagramSocket();
			ds.setSoTimeout(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向指定的服务端发送数据信息.
	 * 
	 * @param host  服务器主机地址
	 * @param port  服务端端口
	 * @param bytes 发送的数据信息
	 * @return 
	 * @throws IOException
	 */
	public static DatagramPacket send(final String host, int port, final byte[] bytes) throws IOException {
		if (port == 0) {
			port = 18001;
		}
		DatagramPacket dp = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port);
		ds.send(dp);
		return dp;
	}

	public static byte[] hexStr2Bytes(String hexstr) {
		byte[] b = new byte[hexstr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexstr.charAt(j++);
			char c1 = hexstr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	private static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f;
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

	public static String bytesToHexStr(byte[] src) {
		StringBuilder sb = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append(0);
			}
			sb.append(hv);
		}
		return sb.toString();
	}

	public static String bytes2HexString(byte[] b) {
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}

	/**
	 * 校验和
	 * 
	 * @param msg    需要计算校验和的byte数组
	 * @param length 校验和位数
	 * @return 计算出的校验和数组
	 */
	public static byte[] checkSum(byte[] msg, int length) {
		long mSum = 0;
		byte[] mByte = new byte[length];

		/** 逐Byte添加位数和 */
		for (byte byteMsg : msg) {
			long mNum = ((long) byteMsg >= 0) ? (long) byteMsg : ((long) byteMsg + 256);
			mSum += mNum;
		} /** end of for (byte byteMsg : msg) */

		/** 位数和转化为Byte数组 */
		for (int liv_Count = 0; liv_Count < length; liv_Count++) {
			mByte[length - liv_Count - 1] = (byte) (mSum >> (liv_Count * 8) & 0xff);
		} /** end of for (int liv_Count = 0; liv_Count < length; liv_Count++) */

		return mByte;
	}
}