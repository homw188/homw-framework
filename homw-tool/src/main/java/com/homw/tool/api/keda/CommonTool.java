package com.homw.tool.api.keda;

public class CommonTool {
	// 计算和校验
	public static String makeChecksum(byte[] bytes) {
		String hexStr = bytesToHexString(bytes);
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

	// 字节数组转十六进制字符串
	public static final String bytesToHexString(byte[] bArray) {
		String sTemp = "";
		for (int i = 0; i < bArray.length; i++)
			sTemp = sTemp + String.format("%02x", new Integer(0xFF & bArray[i]));
		return sTemp;
	}

	// 十六进制字符串转字节数组
	public static final short[] HexStrtoBytes(String str) {
		short[] bytes = new short[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (short) (Integer.parseInt(subStr, 16) & 0xff);
		}

		return bytes;
	}

	// 得到水表地址
	public static Long getSBAddr(String dbAddr) {
		String tempS = "";
		for (int i = dbAddr.length(); i > 0; i -= 2) {
			tempS = tempS + dbAddr.substring(i - 2, i);
		}
		return Long.parseLong(tempS);
	}

	// 加33H
	public static void encrypt(byte[] cmd, int begIdx, int len) {
		for (int i = 0; i < len; i++) {
			cmd[begIdx + i] = (byte) ((cmd[begIdx + i] + 0x33) & 0xFF);
		}
	}

	// 减33H
	public static void decrypt(short[] cmd, int begIdx, int len) {
		for (int i = 0; i < len; i++) {
			cmd[begIdx + i] -= (byte) 0x33;
		}
	}
}
