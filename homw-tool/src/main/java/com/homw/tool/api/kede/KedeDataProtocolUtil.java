package com.homw.tool.api.kede;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class KedeDataProtocolUtil {
	public static byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}

	public static Object toObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public static final String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static String toHexString1(byte[] b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; ++i) {
			buffer.append(toHexString1(b[i]));
		}
		return buffer.toString();
	}

	public static String toHexString1(byte b) {
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1) {
			return "0" + s;
		} else {
			return s;
		}
	}

	public static String hexStr2Str(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return new String(bytes);
	}

	public static String toStringHex(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	public static String DataAdd33H(String str) {
		String ss = "";
		String s2 = "";
		for (int i = 0; i < str.length() - 1; i = i + 2) {
			int s1 = Integer.parseInt(str.substring(i, i + 2), 16);
			s1 = s1 + 51;
			s2 = "0000" + Integer.toHexString(s1 & 0xFF);
			ss = s2.substring(s2.length() - 2) + ss;
		}
		ss = ss.toUpperCase();
		return ss;
	}

	public static String DataSub33H(String str) {
		String ss = "";
		String s2 = "";
		for (int i = 0; i < str.length() - 1; i = i + 2) {
			int s1 = Integer.parseInt(str.substring(i, i + 2), 16);
			s1 = s1 - 51;
			s2 = "0000" + Integer.toHexString(s1 & 0xFF);
			ss = s2.substring(s2.length() - 2) + ss;
		}
		ss = ss.toUpperCase();
		return ss;
	}

	public static String Datarrd(String str) {
		String str1 = "";
		for (int i = str.length(); i > 0; i = i - 2) {
			str1 = str1 + str.substring(i - 2, i);
		}
		str1 = str1.toUpperCase();
		return str1;
	}

	public static String Checknum(String str) {
		int result = 0;
		for (int i = 0; i < str.length() - 1; i = i + 2) {
			String s1 = str.substring(i, i + 2);
			result = result + Integer.parseInt(s1, 16);
		}
		String s2 = "0000" + Integer.toHexString(result & 0xFF);
		String s3 = s2.substring(s2.length() - 2);
		s3 = s3.toUpperCase();
		return s3;
	}

	public static String GetAllCode(String sAdd, String sCon, String sData) {
		int l = sData.length() / 2;
		String SendStr = "68"; // 起始
		SendStr = SendStr + Datarrd(sAdd); // 加入地址域
		SendStr = SendStr + "68"; // 加入地址结束标志
		SendStr = SendStr + sCon; // 加入控制域
		SendStr = SendStr + String.format("%02x", l);// 加入数据域长度
		SendStr = SendStr + sData; // 加入数据域
		SendStr = SendStr + Checknum(SendStr);// 加入校验和
		SendStr = SendStr + "16"; // 加入结束标志
		return SendStr;
	}

	public static String ZtcxData(String Rstr) {
		String RetStr;
		String strr = Rstr.toUpperCase();
		if (strr.substring(0, 2).equals("68") && strr.substring(strr.length() - 2).equals("16")) {
			if (strr.substring(14, 16).equals("68") && strr.substring(16, 18).equals("83")) {
				String bs = strr.substring(18, 20);
				int l = Integer.parseInt(bs, 16) - 4; // 数据长度
				String dota = strr.substring(28, l * 2 + 28); // 数据域
				bs = DataSub33H(strr.substring(20, 28));// 数据标识
				if (bs.equals("078102FF")) {
					String lj, sy, cs;
					if (dota.substring(0, 2).equals("33")) // 判断是否为负数
					{
						sy = DataSub33H(dota.substring(4, 10)) + "." + DataSub33H(dota.substring(2, 4));
					} else {
						sy = "-" + DataSub33H(dota.substring(4, 10)) + "." + DataSub33H(dota.substring(2, 4));
					}
					lj = DataSub33H(dota.substring(12, 18)) + "." + DataSub33H(dota.substring(10, 12));
					cs = DataSub33H(dota.substring(18, 22));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("flag", "0");
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("lj", lj);
					map2.put("sy", sy);
					map2.put("cs", cs);
					map.put("data", map2);
					// JSONObject json = JSONObject.fromBean(map);
					// RetStr= json.toString();
					RetStr = JSON.toJSON(map).toString();
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("flag", "-3");
					Map<String, Object> map2 = new HashMap<String, Object>();
					map2.put("err", "NO078102FF");
					map.put("data", map2);
					// JSONObject json = JSONObject.fromBean(map);
					// RetStr= json.toString();
					RetStr = JSON.toJSON(map).toString();
				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "-2");
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("err", "NO83");
				map.put("data", map2);
				// JSONObject json = JSONObject.fromBean(map);
				// RetStr= json.toString();
				RetStr = JSON.toJSON(map).toString();
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "-1");
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("err", "NO6816");
			map.put("data", map2);
			// JSONObject json = JSONObject.fromBean(map);
			// RetStr= json.toString();
			RetStr = JSON.toJSON(map).toString();
		}
		return RetStr;
	}

	public static String Set83(String Rstr) {
		String RetStr;
		String strr = Rstr.toUpperCase();
		if (strr.substring(0, 2).equals("68") && strr.substring(strr.length() - 2).equals("16")) {
			if (strr.substring(14, 16).equals("68") && strr.substring(16, 18).equals("83")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "0");
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("err", "Yes");
				map.put("data", map2);
				// JSONObject json = JSONObject.fromBean(map);
				// RetStr= json.toString();
				RetStr = JSON.toJSON(map).toString();
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "-2");
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("err", "NO83");
				map.put("data", map2);
				// JSONObject json = JSONObject.fromBean(map);
				// RetStr= json.toString();
				RetStr = JSON.toJSON(map).toString();
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "-1");
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("err", "NO6816");
			map.put("data", map2);
			// JSONObject json = JSONObject.fromBean(map);
			// RetStr= json.toString();
			RetStr = JSON.toJSON(map).toString();
		}
		return RetStr;
	}

	public static String Set94(String Rstr) {
		String RetStr;
		String strr = Rstr.toUpperCase();
		if (strr.substring(0, 2).equals("68") && strr.substring(strr.length() - 2).equals("16")) {
			if (strr.substring(16, 18).equals("9c") || strr.substring(16, 18).equals("9C")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "0");
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("err", "Yes");
				map.put("data", map2);
				// JSONObject json = JSONObject.fromBean(map);
				// RetStr= json.toString();
				RetStr = JSON.toJSON(map).toString();
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "-2");
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("err", "NO9C");
				map.put("data", map2);
				// JSONObject json = JSONObject.fromBean(map);
				// RetStr= json.toString();
				RetStr = JSON.toJSON(map).toString();
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "-1");
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("err", "NO6816");
			map.put("data", map2);
			// JSONObject json = JSONObject.fromBean(map);
			// RetStr= json.toString();
			RetStr = JSON.toJSON(map).toString();
		}
		return RetStr;
	}
}
