package com.homw.tool.api.keda;

import java.util.Arrays;

public class WaterProtocol {
	public byte[] getCommand(String zltype, String[] txcs) {
		byte[] _obj = null;
		byte[] tempZl = null;
		String jy = "";
		String txadd = String.format("%014d", Long.parseLong(txcs[0])); // 14位

		switch (zltype) {
			case "01": // 读计量数据
				if (txcs.length == 1) // 水表地址
				{
					_obj = new byte[16];
					_obj[0] = 104;
					_obj[1] = 16;
	
					_obj[2] = (byte) Integer.parseInt(txadd.substring(12), 16);
					_obj[3] = (byte) Integer.parseInt(txadd.substring(10, 12), 16);
					_obj[4] = (byte) Integer.parseInt(txadd.substring(8, 10), 16);
					_obj[5] = (byte) Integer.parseInt(txadd.substring(6, 8), 16);
					_obj[6] = (byte) Integer.parseInt(txadd.substring(4, 6), 16);
					_obj[7] = (byte) Integer.parseInt(txadd.substring(2, 4), 16);
					_obj[8] = (byte) Integer.parseInt(txadd.substring(0, 2), 16);
	
					_obj[9] = 1;
					_obj[10] = 3;
					_obj[11] = (byte) 0x90;
					_obj[12] = 31;
					_obj[13] = 0;
	
					tempZl = Arrays.copyOfRange(_obj, 0, 14);
					// 校验
					jy = CommonTool.checkSum(tempZl);
					_obj[14] = (byte) Integer.parseInt(jy, 16);
					_obj[15] = 22;
				}
				break;
			case "04":
				if (txcs.length == 2) {
					_obj = new byte[17];
					_obj[0] = 104;
					_obj[1] = 16;
					String kzStr = txcs[1];// 控制 55开阀 99关阀 66读阀门状态
	
					_obj[2] = (byte) Integer.parseInt(txadd.substring(12), 16);
					_obj[3] = (byte) Integer.parseInt(txadd.substring(10, 12), 16);
					_obj[4] = (byte) Integer.parseInt(txadd.substring(8, 10), 16);
					_obj[5] = (byte) Integer.parseInt(txadd.substring(6, 8), 16);
					_obj[6] = (byte) Integer.parseInt(txadd.substring(4, 6), 16);
					_obj[7] = (byte) Integer.parseInt(txadd.substring(2, 4), 16);
					_obj[8] = (byte) Integer.parseInt(txadd.substring(0, 2), 16);
	
					_obj[9] = 4; // 控制码
					_obj[10] = 4;// 长度
					_obj[11] = (byte) 0xA0;
					_obj[12] = 23;
					_obj[13] = 0;
					_obj[14] = (byte) Integer.parseInt(kzStr, 16);// 开关阀
					// tempZl = Arrays.copyOfRange(_obj, 0, 14);
					tempZl = Arrays.copyOfRange(_obj, 0, 15);
					// 校验
					jy = CommonTool.checkSum(tempZl);
					_obj[15] = (byte) Integer.parseInt(jy, 16);
					_obj[16] = 22;
				}
				break;
		}
		return _obj;
	}

	public byte[] fzxy(byte[] db, int kzm) {
		byte[] _obj = new byte[db.length + 13];
		byte[] tempZl = null;

		_obj[0] = 104;
		_obj[1] = 0;
		_obj[2] = 0;
		_obj[3] = 0;
		_obj[4] = 0;
		_obj[5] = 0;
		_obj[6] = 0;
		_obj[7] = 104;
		_obj[8] = 0;
		_obj[9] = (byte) (db.length + 1);// 数据长度
		_obj[10] = (byte) kzm;

		for (int i = 0; i < db.length; i++) {
			_obj[11 + i] = db[i];
		}

		String jy = "";
		tempZl = Arrays.copyOfRange(_obj, 0, _obj.length - 2);
		// 校验
		jy = CommonTool.checkSum(tempZl);
		_obj[db.length + 11] = (byte) Integer.parseInt(jy, 16);
		_obj[db.length + 12] = 22;

		return _obj;
	}

	// 解析指令返回
	public String analysisBack(String backHexStr) {
		String returnBack = "";
		try {
			if (backHexStr.substring(0, 8).equals("cccc00ff")) {// 水表标识
				backHexStr = backHexStr.substring(30, backHexStr.length() - 4); // 去掉封装字节数
				String sbadd = CommonTool.getWaterAddr(backHexStr.substring(4, 18)).toString(); // 得到水表地址

				int codeTemp = Integer.parseInt(backHexStr.substring(18, 20), 16);
				switch (codeTemp) {
					case 0x81: // 读取数据正常返回
						returnBack = sbadd + ","
								+ Double.parseDouble(backHexStr.substring(34, 36) + backHexStr.substring(32, 34)
										+ backHexStr.substring(30, 32) + "." + backHexStr.substring(28, 30));
						break;
					case 0x84:// 开关阀、读取阀们状态正常返回
						if (backHexStr.length() == 36) // 开关阀
							returnBack = "OK";
						else if (backHexStr.length() == 34)// 读阀门状态
						{
							int k = Integer.parseInt(backHexStr.substring(28, 30), 16);
							String tempstr = Integer.toString(k, 2);
							while (tempstr.length() < 8) {
								tempstr = "0" + tempstr;
							}
	
							if (tempstr.substring(tempstr.length() - 2).equals("00"))
								returnBack = "开";
							else if (tempstr.substring(tempstr.length() - 2).equals("01"))
								returnBack = "关";
							else if (tempstr.substring(tempstr.length() - 2).equals("11"))
								returnBack = "异常";
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnBack;
	}
}
