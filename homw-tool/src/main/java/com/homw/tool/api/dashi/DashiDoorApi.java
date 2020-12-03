package com.homw.tool.api.dashi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class DashiDoorApi {
	private static final int RECV_TIMEOUT = 3000;
	private static DatagramSocket socket = null;
	static {
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(RECV_TIMEOUT);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送数据报
	 * 
	 * @param host
	 * @param port  默认: 18001
	 * @param bytes
	 * @throws IOException
	 */
	public static void send(String host, int port, byte[] bytes) throws IOException {
		if (port <= 0) {
			port = 18001;
		}
		DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port);
		socket.send(packet);
	}

	public static byte[] hex2Bytes(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		int j = 0;
		for (int i = 0; i < bytes.length; i++) {
			char c0 = hex.charAt(j++);
			char c1 = hex.charAt(j++);
			bytes[i] = (byte) ((hexDigit(c0) << 4) | hexDigit(c1));
		}
		return bytes;
	}

	public static int hexDigit(char c) {
		if (c >= 'a') {
			return (c - 'a' + 10) & 0x0f;
		}
		if (c >= 'A') {
			return (c - 'A' + 10) & 0x0f;
		}
		return (c - '0') & 0x0f;
	}

	public static String bytes2Hex(byte[] bytes) {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		StringBuilder hex = new StringBuilder("");
		for (int i = 0; i < bytes.length; i++) {
			int v = bytes[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				hex.append(0);
			}
			hex.append(hv);
		}
		return hex.toString();
	}

	/**
	 * 校验和
	 * 
	 * @param bytes
	 * @param len
	 * @return
	 */
	public static byte[] checkSum(byte[] bytes, int len) {
		long sum = 0;
		byte[] out = new byte[len];
		/** 逐Byte添加位数和 */
		for (byte b : bytes) {
			long num = ((long) b >= 0) ? (long) b : ((long) b + 256);
			sum += num;
		}
		/** 位数和转化为Byte数组 */
		for (int i = 0; i < len; i++) {
			out[len - i - 1] = (byte) (sum >> (i * 8) & 0xff);
		}
		return out;
	}
}