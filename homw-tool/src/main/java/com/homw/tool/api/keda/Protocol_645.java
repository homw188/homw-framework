package com.homw.tool.api.keda;

import java.util.Arrays;

public class Protocol_645 {
	// 获取发送指令
	public byte[] getCommand(String zltype, String[] txcs) {
		byte[] _obj = null;
		byte[] tempZl = null;
		String jy = "";
		String txadd = String.format("%012d", Long.parseLong(txcs[0])); // 10位

		switch (zltype) {
			case "1": // 读数据
				_obj = new byte[16];
	
				_obj[0] = 104;
				_obj[1] = (byte) Integer.parseInt(txadd.substring(10), 16);
				_obj[2] = (byte) Integer.parseInt(txadd.substring(8, 10), 16);
				_obj[3] = (byte) Integer.parseInt(txadd.substring(6, 8), 16);
				_obj[4] = (byte) Integer.parseInt(txadd.substring(4, 6), 16);
				_obj[5] = (byte) Integer.parseInt(txadd.substring(2, 4), 16);
				_obj[6] = (byte) Integer.parseInt(txadd.substring(0, 2), 16);
				_obj[7] = 104;
				_obj[8] = 17;
				_obj[9] = 4;
				_obj[10] = 51;
				_obj[11] = 51;
				_obj[12] = 52;
				_obj[13] = 51;
	
				tempZl = Arrays.copyOfRange(_obj, 0, 14);
				// 校验
				jy = CommonTool.checkSum(tempZl);
				_obj[14] = (byte) Integer.parseInt(jy, 16);
				_obj[15] = 22;
				break;
	
			case "2": // 通断控制
				if (txcs.length == 3) { // 表地址
					_obj = new byte[28];
	
					_obj[0] = 104;
					_obj[1] = (byte) Integer.parseInt(txadd.substring(10), 16);
					_obj[2] = (byte) Integer.parseInt(txadd.substring(8, 10), 16);
					_obj[3] = (byte) Integer.parseInt(txadd.substring(6, 8), 16);
					_obj[4] = (byte) Integer.parseInt(txadd.substring(4, 6), 16);
					_obj[5] = (byte) Integer.parseInt(txadd.substring(2, 4), 16);
					_obj[6] = (byte) Integer.parseInt(txadd.substring(0, 2), 16);
					_obj[7] = 104;
					_obj[8] = (byte) 0x1C; // 控制码1C
					_obj[9] = (byte) 0x10;
					_obj[10] = (byte) 0x35; // 密级02
					_obj[11] = (byte) 0x89;
					_obj[12] = (byte) 0x67;
					_obj[13] = (byte) 0x45;
					_obj[14] = (byte) 0xB8;
					_obj[15] = (byte) 0x73;
					_obj[16] = (byte) 0x39;
					_obj[17] = (byte) 0xA3;
					if (txcs[2].equals("0")) // 断 4D
						_obj[18] = (byte) 0x4D;
					else
						_obj[18] = (byte) 0x4F;
	
					_obj[19] = 51;
					_obj[20] = 51;
					_obj[21] = (byte) 0x8c;
					_obj[22] = (byte) 0x56;
					_obj[23] = (byte) 0x64;
					_obj[24] = (byte) 0x45;
					_obj[25] = (byte) 0x9b;
	
					tempZl = Arrays.copyOfRange(_obj, 0, 26);
					// 校验
					jy = CommonTool.checkSum(tempZl);
					_obj[26] = (byte) Integer.parseInt(jy, 16);
					_obj[27] = 22;
				}
				break;
			case "3":
				_obj = new byte[16];
	
				_obj[0] = 104;
				_obj[1] = (byte) Integer.parseInt(txadd.substring(10), 16);
				_obj[2] = (byte) Integer.parseInt(txadd.substring(8, 10), 16);
				_obj[3] = (byte) Integer.parseInt(txadd.substring(6, 8), 16);
				_obj[4] = (byte) Integer.parseInt(txadd.substring(4, 6), 16);
				_obj[5] = (byte) Integer.parseInt(txadd.substring(2, 4), 16);
				_obj[6] = (byte) Integer.parseInt(txadd.substring(0, 2), 16);
				_obj[7] = 104;
				_obj[8] = 17;
				_obj[9] = 4;
				_obj[10] = 0x36;
				_obj[11] = 0x38;
				_obj[12] = 0x33;
				_obj[13] = 0x37;
				tempZl = Arrays.copyOfRange(_obj, 0, 14);
				// 校验
				jy = CommonTool.checkSum(tempZl);
				_obj[14] = (byte) Integer.parseInt(jy, 16);
				_obj[15] = 22;
				break;
		}
		return _obj;
	}

	public String analysisBack(String backHexStr) throws Exception {
		String returnBack = "";
		try {
			if (backHexStr.substring(0, 8).equals("cccc00dd")) { // 单表标识
				backHexStr = backHexStr.substring(8);
				// String sbadd = CommonTool.getSBAddr(backHexStr.substring(2, 14)).toString();
				// // 得到电表地址
				int codeTemp = Integer.parseInt(backHexStr.substring(16, 18), 16);
				switch (codeTemp) {
				case 0x91: // 读取数据正常返回
					if (backHexStr.substring(20, 28).equals("33333433")) // 读电量
					{
						short[] tempbyte = CommonTool.hex2Shorts(backHexStr.substring(28, 36));
						CommonTool.decrypt(tempbyte, 0, tempbyte.length);
						returnBack = "-1,"
								+ Double.parseDouble(Integer.toHexString(tempbyte[3]) + Integer.toHexString(tempbyte[2])
										+ Integer.toHexString(tempbyte[1]) + Integer.toHexString(tempbyte[0])) / 100;
					} else // 读通断
					{
						int k = Integer.parseInt(backHexStr.substring(28, 30), 16) - 51;
						String tempstr = Integer.toString(k, 2);
						while (tempstr.length() < 8) {
							tempstr = "0" + tempstr;
						}

						if (tempstr.substring(1, 2).equals("1"))
							returnBack = "断"; // 1代表断
						else
							returnBack = "通";
					}
					break;
				case 0x9C:// 通断正常返回
					returnBack = "OK";
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("数据解析异常：" + backHexStr, e);
		}
		return returnBack;
	}
}
