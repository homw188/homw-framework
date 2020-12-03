package com.homw.tool.api.keda;

public class CommonTool {
	public static String checkSum(byte[] bytes) {
		String hexStr = bytes2Hex(bytes);
		int total = 0;
		int len = hexStr.length();
		int num = 0;
		while (num < len) {
			String s = hexStr.substring(num, num + 2);
			total += Integer.parseInt(s, 16);
			num = num + 2;
		}

		int mod = total % 256;
		String hex = Integer.toHexString(mod);
		len = hex.length();
		if (len < 2) {
			hex = "0" + hex;
		}
		return hex;
	}

	public static String bytes2Hex(byte[] bytes) {
		String hex = "";
		for (int i = 0; i < bytes.length; i++)
			hex = hex + String.format("%02x", 0xFF & bytes[i]);
		return hex;
	}

	public static short[] hex2Shorts(String str) {
		short[] bytes = new short[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (short) (Integer.parseInt(subStr, 16) & 0xff);
		}
		return bytes;
	}
	
	public static byte[] hex2Bytes(String str) {
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}
		return bytes;
	}

	public static Long getWaterAddr(String addr) {
		String result = "";
		for (int i = addr.length(); i > 0; i -= 2) {
			result = result + addr.substring(i - 2, i);
		}
		return Long.parseLong(result);
	}

	// 加33H
	public static void encrypt(byte[] cmd, int start, int len) {
		for (int i = 0; i < len; i++) {
			cmd[start + i] = (byte) ((cmd[start + i] + 0x33) & 0xFF);
		}
	}

	// 减33H
	public static void decrypt(short[] cmd, int start, int len) {
		for (int i = 0; i < len; i++) {
			cmd[start + i] -= (byte) 0x33;
		}
	}
	
	public static void decrypt(byte[] cmd, int start, int len) {
		for (int i = 0; i < len; i++) {
			cmd[start + i] -= (byte) 0x33;
		}
	}
}
