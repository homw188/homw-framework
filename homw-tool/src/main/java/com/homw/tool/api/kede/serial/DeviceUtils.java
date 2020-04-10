package com.homw.tool.api.kede.serial;

import java.util.ArrayList;
import java.util.List;

public class DeviceUtils {

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static String sub33(String source_1) {
		String source = source_1;
		for (int x = 0; x < source.length(); x++) {
			String sources = String.valueOf(source.substring(x, x + 1)); // 循环获取字符串数据
			if (sources == ".") {
				source = source.replace(".", "");
				// source = source.replaceAll(".", "");
			}
		}
		int result = 0;
		StringBuilder builder = new StringBuilder(30); // 实例化stringbulider对象
		List<String> list = new ArrayList<String>(); // 实例化集合，用于存放加33之后的数据
		int add = 51; // 需要加的33，0x33为16进制 hexStringToBytes("33") --> 51
		for (int i = 0; i < source.length(); i++) {
			String s = source.substring(i, i + 2);
			i++;
			int source_10 = Integer.parseInt(s, 16);
			/* Convert.ToInt16(s, 16) */;
			result = source_10 - add;
			String results = Integer.toHexString(result); // java 中10进制转16进制
			if (results.length() < 2 && result == 0) {
				StringBuilder b = new StringBuilder();
				b.append(result);
				b.append("0");
				list.add(Integer.toHexString(Integer.parseInt(b.toString(), 16)));
				// list.add(b.toString());
			} else {
				list.add(results);
			}
		}
		for (int j = 0; j < list.size(); j++) {
			if (String.valueOf(list.get(j)).equals("00")) {
				builder.append(list.get(j));
			} else {
				int temp = Integer.parseInt(list.get(j), 16);
				String temps = Integer.toHexString(temp);
				if (temps.length() == 1) {
					StringBuilder sb1 = new StringBuilder(temps);
					String ss = sb1.insert(0, "0").toString();
					builder.append(ss);
				} else {
					String _s = Integer.toHexString(temp); // _s = String.valueOf(temp, 16);
					if (_s.length() == 1) {
						StringBuilder sb2 = new StringBuilder(_s);
						String _sb2 = sb2.insert(0, "0").toString();
						builder.append(_sb2);
					} else {
						builder.append(_s);
					}
				}
			}
		}
		return builder.toString();
	}

	/**
	 * 断电代码解析 返回数据
	 * 
	 * @return 断电代码
	 */
	public static String chooseState(String state) {
		String result = null;
		try {
			switch (Integer.parseInt(state, 16)) {
				case 0:
					result = "合闸";
					break;
				case 1:
					result = "跳闸";
					break;
				case 2:
					result = "零电量断电";
					break;
				case 3:
					result = "超功率断电";
					break;
				case 4:
					result = "报警断电";
					break;
				case 5:
					result = "超最大无功断电";
					break;
				case 6:
					result = "定时断电";
					break;
				case 7:
					result = "超特许负载断电";
					break;
				case 8:
					result = "超无功步进值";
					break;
				case 9:
					result = "超夜间小功率断电";
					break;
				case 10:
					result = "纯阻性负载且超过步进功率跳闸";
					break;
				case 11:
					result = "禁用负载跳闸";
					break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return result;
		}
		return result;
	}

	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static byte[] byteMerge(byte[] byte_1, byte[] byte_2, int numBytes) {
		byte[] byte_3 = new byte[byte_1.length + numBytes];

		for (int i = 0; i < byte_1.length; ++i) {
			byte_3[i] += byte_1[i];
		}
		for (int i = byte_1.length; i < byte_3.length; ++i) {
			byte_3[i] += byte_2[i - byte_1.length];
		}
		return byte_3;
	}

	public static String lowToTop(String source) {
		StringBuilder builder = new StringBuilder();
		for (int i = source.length() - 1; i >= 0; i -= 2) {
			if (source.length() % 2 != 0 && i == 0) {
				builder.append(source.substring(i, i + 1/* 1 */));
			} else {
				String sub = source.substring(i - 1, i + 1/* 2 */);
				builder.append(sub);
			}
		}
		return builder.toString();
	}

	public static String checkData(String order) {
		int x = 0;
		for (int i = 0; i < order.length() - 1; i++) {
			String s = order.substring(i, i + 2/* 2 */);
			i++;
			x += Integer.parseInt(s, 16)/* Convert.ToInt16(s, 16) */;
		}
		String _check = Integer.toHexString(x)/* x.toString("X") */;
		String _checkFinal = "";
		switch (_check.length()) {
			case 2: {
				break;
			}
			case 3: {
				_checkFinal = _check.substring(1, 3);
				break;
			}
			case 4: {
				_checkFinal = _check.substring(2, 4);
				break;
			}
		}
		return _checkFinal.toUpperCase(); // 方法返回一个字符串，该字符串中的所有字母都被转化为大写字母
	}

	/**
	 * 编写十六进制的字符串转换为byte数组的函数：public byte[] hexStringToBytes(String hexString);
	 * 每两个字符表示转化为一个字节，返回字节数组。例：字符串"ABCDEF" 转化为byte数组 {0xAB,0xCD,0xEF} 字符串"01"
	 * 转化为byte数组 {0x01}
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
}