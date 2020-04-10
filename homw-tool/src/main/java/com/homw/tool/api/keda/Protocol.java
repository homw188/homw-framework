package com.homw.tool.api.keda;

import java.util.Arrays;

public class Protocol {
	// 加33H
	private void encrypt(byte[] cmd, int begIdx, int len) {
		for (int i = 0; i < len; i++) {
			cmd[begIdx + i] = (byte) ((cmd[begIdx + i] + 0x33) & 0xFF);
		}
	}

	// 减33H
	private void decrypt(byte[] cmd, int begIdx, int len) {
		for (int i = 0; i < len; i++) {
			cmd[begIdx + i] -= (byte) 0x33;
		}
	}

	// 获取发送指令
	public byte[] getCommand(String zltype, String[] txcs) {
		byte[] _obj = null;
		byte[] tempZl = null;
		String jy = "";
		String txadd = String.format("%010d", Integer.parseInt(txcs[0])); // 10位
		int line = Integer.parseInt(txcs[1]);

		switch (zltype) {
			case "28": // 断送电
	
				_obj = new byte[25];
	
				_obj[0] = 104;
				_obj[1] = (byte) Integer.parseInt(Integer.toHexString(line), 16);// 线号
				_obj[2] = (byte) Integer.parseInt(txadd.substring(8), 16);
				_obj[3] = (byte) Integer.parseInt(txadd.substring(6, 8), 16);
				_obj[4] = (byte) Integer.parseInt(txadd.substring(4, 6), 16);
				_obj[5] = (byte) Integer.parseInt(txadd.substring(2, 4), 16);
				_obj[6] = (byte) Integer.parseInt(txadd.substring(0, 2), 16);
				_obj[7] = 104;
				_obj[8] = 20;
				_obj[9] = 13;// 数据域长度
				_obj[10] = 40;
				_obj[11] = 0;
				_obj[12] = 0;
				_obj[13] = (byte) 0xDF;
				int index = 0;
				String tempstr = "12345678";
				for (int i = 0; i < 8; i = i + 2) {
					index++;
					_obj[13 + index] = (byte) Integer.parseInt(tempstr.substring(i, i + 2), 16);
				}
				// 固定4个0
				_obj[18] = 0;
				_obj[19] = 0;
				_obj[20] = 0;
				_obj[21] = 0;
	
				String dsdtag = txcs[2];
	
				if (dsdtag.equals("1")) {
					_obj[22] = (byte) 0xFF;
				} else {
					_obj[22] = (byte) 0x00;
				}
	
				encrypt(_obj, 10, 0x0D); // +33H
	
				tempZl = Arrays.copyOfRange(_obj, 0, 23);
				// 校验
				jy = makeChecksum(tempZl);
				_obj[23] = (byte) Integer.parseInt(jy, 16);
				_obj[24] = 22;
				break;
	
			case "30": // 读通断状态
	
				_obj = new byte[16];
				_obj[0] = 104;
				_obj[1] = (byte) Integer.parseInt(Integer.toHexString(line), 16);// 线号
				_obj[2] = (byte) Integer.parseInt(txadd.substring(8), 16);
				_obj[3] = (byte) Integer.parseInt(txadd.substring(6, 8), 16);
				_obj[4] = (byte) Integer.parseInt(txadd.substring(4, 6), 16);
				_obj[5] = (byte) Integer.parseInt(txadd.substring(2, 4), 16);
				_obj[6] = (byte) Integer.parseInt(txadd.substring(0, 2), 16);
				_obj[7] = 104;
				_obj[8] = 17;
				_obj[9] = 4;
				_obj[10] = 99;
				_obj[11] = 51;
				_obj[12] = 51;
				_obj[13] = 18;
				tempZl = Arrays.copyOfRange(_obj, 0, 14);
				jy = makeChecksum(tempZl);
				_obj[14] = (byte) Integer.parseInt(jy, 16);
				_obj[15] = 22;
				break;
			case "36":// 读电量
				_obj = new byte[16];
				_obj[0] = 104;
				if (line == 0)
					_obj[1] = (byte) 0x99;
				else
					_obj[1] = (byte) Integer.parseInt(Integer.toHexString(line), 16);// 线号
				_obj[2] = (byte) Integer.parseInt(txadd.substring(8), 16);
				_obj[3] = (byte) Integer.parseInt(txadd.substring(6, 8), 16);
				_obj[4] = (byte) Integer.parseInt(txadd.substring(4, 6), 16);
				_obj[5] = (byte) Integer.parseInt(txadd.substring(2, 4), 16);
				_obj[6] = (byte) Integer.parseInt(txadd.substring(0, 2), 16);
				_obj[7] = 104;
				_obj[8] = 17;
				_obj[9] = 4;
				_obj[10] = 105;
				_obj[11] = 51;
				_obj[12] = 51;
				_obj[13] = 18;
				tempZl = Arrays.copyOfRange(_obj, 0, 14);
				jy = makeChecksum(tempZl);
				_obj[14] = (byte) Integer.parseInt(jy, 16);
				_obj[15] = 22;
	
				break;
	
			default:
				break;
		}
		return _obj;
	}

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

	// 解析指令返回
	public String analysisBack(String backHexStr) {
		String setBack = "";
		try {
			int startIndex = backHexStr.indexOf("68");
			if (startIndex == 0) // 未找到返回起始符
				return null;
			else {
				backHexStr = backHexStr.substring(8); // 从68开始截取 前面是指令头 CCCC00DF
				String stateTag = backHexStr.substring(16, 18); // 状态指令码
				int lengthCode = Integer.parseInt(backHexStr.substring(18, 20), 16); // 长度

				switch (Integer.parseInt(stateTag, 16)) {
				case 0x94:// 94 设置类指令正确返回
					if (lengthCode == 0)
						setBack = "OK"; // 设置成功
					break;
				case 0x91:// 91读取类指令正常返回

					int zlcode = Integer.parseInt(backHexStr.substring(20, 22), 16) - 51;
					if (zlcode == 0x36) {// 读电量返回
						int lineNo;
						double cbdata;
						String backData = "";
						String dlStr = backHexStr.substring(28, 28 + lengthCode * 2 - 8);
						// System.out.println(backHexStr);
						byte[] tempbyte = null;
						int j = 0;
						for (int i = 0; i < dlStr.length() / 10; i++) {
							lineNo = Integer.parseInt(dlStr.substring(j, j + 2), 16) - 0x33 + 1; // 线号默认从0开始 +1
							tempbyte = HexStrtoBytes(dlStr.substring(j + 2, j + 10));
							decrypt(tempbyte, 0, tempbyte.length);
							cbdata = Double.parseDouble(bytesToHexString(tempbyte)) / 10;
							backData = backData + lineNo + "," + cbdata + "|";
							j += 10;
						}
						if (backData != "")
							setBack = backData.substring(0, backData.length() - 1);

					} else if (zlcode == 0x30) { // 读通断状态返回
						if (Integer.parseInt(backHexStr.substring(30, 32), 16) - 51 == 0)
							setBack = "通"; // 1代表通
						else
							setBack = "断"; // 0代表断
					}
					break;
				case 0xD4:// D4 异常返回
					if (lengthCode == 1) {// D4 长度0
						int errCode = Integer.parseInt(backHexStr.substring(20, 22), 16) - 51; // -33H
						setBack = "Err," + errCode;// 错误返回格式 (Err,表地址,线号,错误信息)
					}
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return setBack;
	}

	// 字节数组转十六进制字符串
	public static final String bytesToHexString(byte[] bArray) {
		String sTemp = "";
		for (int i = 0; i < bArray.length; i++)
			sTemp = sTemp + String.format("%02x", new Integer(0xFF & bArray[i]));
		return sTemp;
	}

	// 十六进制字符串转字节数组
	public static final byte[] HexStrtoBytes(String str) {
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);
			bytes[i] = (byte) Integer.parseInt(subStr, 16);
		}
		return bytes;
	}

}
