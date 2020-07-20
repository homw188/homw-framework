package com.homw.tool.api.kede;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @description 科德水电表645协议工具类
 * @author Hom
 * @version 1.0
 * @since 2020-07-20
 */
public class KedeProtocolUtil {
	/**
	 * 按字节分别减0x33，字节序倒转
	 * 
	 * @param str
	 * @return 16进制字符
	 */
	public static String sub33H(String str) {
		String ret = "";
		String hex = "";
		for (int i = 0; i < str.length() - 1; i = i + 2) {
			int bs = Integer.parseInt(str.substring(i, i + 2), 16);
			bs = bs - 51; // 51=0x33
			hex = "0000" + Integer.toHexString(bs & 0xFF);
			ret = hex.substring(hex.length() - 2) + ret;
		}
		return ret.toUpperCase();
	}
	
	/**
	 * 按字节分别加0x33，字节序倒转
	 * 
	 * @param str
	 * @return 16进制字符
	 */
	public static String add33H(String str) {
		String ret = "";
		String hex = "";
		for (int i = 0; i < str.length() - 1; i = i + 2) {
			int bs = Integer.parseInt(str.substring(i, i + 2), 16);
			bs = bs + 51; // 51=0x33
			hex = "0000" + Integer.toHexString(bs & 0xFF);
			ret = hex.substring(hex.length() - 2) + ret;
		}
		return ret.toUpperCase();
	}

	/**
	 * 字节序转换<br>
	 * 大端（big endian）转小端(little endian)
	 * 
	 * @param be big endian
	 * @return little endian
	 */
	public static String revertEndian(String be) {
		String le = "";
		for (int i = be.length(); i > 0; i = i - 2) {
			le = le + be.substring(i - 2, i);
		}
		le = le.toUpperCase();
		return le;
	}

	/**
	 * 计算校验码<br>
	 * 算法：按字节求和，溢出256，则丢弃
	 * 
	 * @param frame 数据帧
	 * @return 16进制单字节
	 */
	public static String checknum(String frame) {
		int ret = 0;
		for (int i = 0; i < frame.length() - 1; i = i + 2) {
			String bs = frame.substring(i, i + 2);
			ret = ret + Integer.parseInt(bs, 16);
		}
		String s2 = "0000" + Integer.toHexString(ret & 0xFF);
		String csh = s2.substring(s2.length() - 2);
		return csh.toUpperCase();
	}

	/**
	 * 封装数据帧
	 * 
	 * @param addr 设备地址
	 * @param ctrl 控制码
	 * @param data 数据
	 * @return 16进制字符
	 */
	public static String getDataFrame(String addr, String ctrl, String data) {
		int len = data.length() / 2;
		String frame = "68"; // 帧起始符
		frame = frame + revertEndian(addr); // 地址域
		frame = frame + "68"; // 帧起始符
		frame = frame + ctrl; // 控制域
		frame = frame + String.format("%02x", len);// 数据域长度
		frame = frame + data; // 数据域
		frame = frame + checknum(frame);// 校验和
		frame = frame + "16"; // 结束符
		return frame;
	}

	/**
	 * 解析电表读数
	 * 
	 * @param data
	 * @return lj(累计)，sy(剩余)，cs(次数) <br>
	 * eg.{"data":{"lj":"000053.96","sy":"-000000.00","cs":"0000"},"flag":"0"}
	 */
	public static String parseElecData(String data) {
		String ret;
		String str = data.toUpperCase();
		if (str.substring(0, 2).equals("68") && str.substring(str.length() - 2).equals("16")) {
			// 控制码: 0x83
			if (str.substring(14, 16).equals("68") && str.substring(16, 18).equals("83")) {
				String bs = str.substring(18, 20);
				int len = Integer.parseInt(bs, 16) - 4; // 数据长度
				String dataField = str.substring(28, len * 2 + 28); // 数据域
				bs = sub33H(str.substring(20, 28)); // 数据标识
				if (bs.equals("078102FF")) {
					String lj, sy, cs;
					// 判断是否为负数
					if (dataField.substring(0, 2).equals("33")) {
						sy = sub33H(dataField.substring(4, 10)) + "." + sub33H(dataField.substring(2, 4));
					} else {
						sy = "-" + sub33H(dataField.substring(4, 10)) + "." + sub33H(dataField.substring(2, 4));
					}
					lj = sub33H(dataField.substring(12, 18)) + "." + sub33H(dataField.substring(10, 12));
					cs = sub33H(dataField.substring(18, 22));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("flag", "0");
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("lj", lj);
					dataMap.put("sy", sy);
					dataMap.put("cs", cs);
					map.put("data", dataMap);
					ret = JSON.toJSON(map).toString();
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("flag", "-3");
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("err", "NO078102FF");
					map.put("data", dataMap);
					ret = JSON.toJSON(map).toString();
				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "-2");
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("err", "NO83");
				map.put("data", dataMap);
				ret = JSON.toJSON(map).toString();
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "-1");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("err", "NO6816");
			map.put("data", dataMap);
			ret = JSON.toJSON(map).toString();
		}
		return ret;
	}
	
	/**
	 * 解析水表读数
	 * 
	 * @param data
	 * @return lj(累计)，sy(剩余)，tz(透支)，cs(次数)，zt1(状态1)，zt2(状态2)，sj(时间) <br>
	 * eg.{"data":{"tz":"0000000.0","lj":"0000019.7","zt2":"00","zt1":"00","sy":"0000000.0","sj":"200720150800","cs":"0000"},"flag":"0"}
	 */
	public static String parseWaterData(String data) {
		String ret;
		String str = data.toUpperCase();
		if (str.substring(0, 2).equals("68") && str.substring(str.length() - 2).equals("16")) {
			// 控制码: 0x91
			if (str.substring(14, 16).equals("68") && str.substring(16, 18).equals("91")) {
				String bs = str.substring(18, 20);
				int len = Integer.parseInt(bs, 16) - 4; // 数据长度
				String dataField = str.substring(28, len * 2 + 28); // 数据域
				bs = sub33H(str.substring(20, 28)); // 数据标识
				if (bs.equals("20000100")) {
					// 累计，剩余，透支，次数，状态1，状态2，时间
					String lj, sy, tz, cs, zt1, zt2, sj;
					
					// 仅1位小数处理
					String decimal = sub33H(dataField.substring(0, 2));
					lj = sub33H(dataField.substring(2, 8)) + decimal.substring(0, 1) + "." + decimal.substring(1);
					decimal = sub33H(dataField.substring(8, 10));
					sy = sub33H(dataField.substring(10, 16)) + decimal.substring(0, 1) + "." + decimal.substring(1);
					decimal = sub33H(dataField.substring(16, 18));
					tz = sub33H(dataField.substring(18, 24)) + decimal.substring(0, 1) + "." + decimal.substring(1);
					
					cs = sub33H(dataField.substring(24, 28));
					zt1 = sub33H(dataField.substring(28, 30));
					zt2 = sub33H(dataField.substring(30, 32));
					sj = sub33H(dataField.substring(32, 44));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("flag", "0");
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("lj", lj);
					dataMap.put("sy", sy);
					dataMap.put("tz", tz);
					dataMap.put("cs", cs);
					dataMap.put("zt1", zt1);
					dataMap.put("zt2", zt2);
					dataMap.put("sj", sj);
					map.put("data", dataMap);
					ret = JSON.toJSON(map).toString();
				} else {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("flag", "-3");
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("err", "NO20000100");
					map.put("data", dataMap);
					ret = JSON.toJSON(map).toString();
				}
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "-2");
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("err", "NO91");
				map.put("data", dataMap);
				ret = JSON.toJSON(map).toString();
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "-1");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("err", "NO6816");
			map.put("data", dataMap);
			ret = JSON.toJSON(map).toString();
		}
		return ret;
	}
	
	/**
	 * 解析电表充值数据
	 * 
	 * @param str
	 * @return json字符串，eg.{"data":{"err":"Yes"},"flag":"0"}
	 */
	public static String parseElecCharge(String str) {
		String ret;
		String strr = str.toUpperCase();
		if (strr.substring(0, 2).equals("68") && strr.substring(strr.length() - 2).equals("16")) {
			if (strr.substring(14, 16).equals("68") && strr.substring(16, 18).equals("83")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "0");
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("err", "Yes");
				map.put("data", dataMap);
				ret = JSON.toJSON(map).toString();
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "-2");
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("err", "NO83");
				map.put("data", dataMap);
				ret = JSON.toJSON(map).toString();
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "-1");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("err", "NO6816");
			map.put("data", dataMap);
			ret = JSON.toJSON(map).toString();
		}
		return ret;
	}

	/**
	 * 解析电表开关动作数据
	 * 
	 * @param str
	 * @return json字符串，eg.{"data":{"err":"Yes"},"flag":"0"}
	 */
	public static String parseElecSwitch(String str) {
		String ret;
		String strr = str.toUpperCase();
		if (strr.substring(0, 2).equals("68") && strr.substring(strr.length() - 2).equals("16")) {
			if (strr.substring(16, 18).equals("9c") || strr.substring(16, 18).equals("9C")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "0");
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("err", "Yes");
				map.put("data", dataMap);
				ret = JSON.toJSON(map).toString();
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "-2");
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("err", "NO9C");
				map.put("data", dataMap);
				ret = JSON.toJSON(map).toString();
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "-1");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("err", "NO6816");
			map.put("data", dataMap);
			ret = JSON.toJSON(map).toString();
		}
		return ret;
	}
	
	/**
	 * 解析水表开关动作数据
	 * 
	 * @param str
	 * @return json字符串，eg.{"data":{"err":"Yes"},"flag":"0"}
	 */
	public static String parseWaterSwitch(String str) {
		String ret;
		String strr = str.toUpperCase();
		if (strr.substring(0, 2).equals("68") && strr.substring(strr.length() - 2).equals("16")) {
			if (strr.substring(16, 18).equals("94")) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "0");
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("err", "Yes");
				map.put("data", dataMap);
				ret = JSON.toJSON(map).toString();
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("flag", "-2");
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("err", "NO94");
				map.put("data", dataMap);
				ret = JSON.toJSON(map).toString();
			}
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", "-1");
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("err", "NO6816");
			map.put("data", dataMap);
			ret = JSON.toJSON(map).toString();
		}
		return ret;
	}
	
	/**
	 * 序列化
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			return bos.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				bos.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 反序列化
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 16进字符转字节数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStrToBytes(String hex) {
		int len = (hex.length() / 2);
		byte[] ret = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			ret[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return ret;
	}

	private static int toByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	/**
	 * 字节数组转16进制字符
	 * 
	 * @param bArr
	 * @return
	 */
	public static final String bytesToHexStr(byte[] bArr) {
		StringBuffer sb = new StringBuffer(bArr.length);
		String sTemp;
		for (int i = 0; i < bArr.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArr[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
}
