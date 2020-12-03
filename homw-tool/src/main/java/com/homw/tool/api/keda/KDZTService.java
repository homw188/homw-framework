package com.homw.tool.api.keda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KDZTService {

	private static final Logger logger = LoggerFactory.getLogger(KDZTService.class);
	
	// private SocketTool socketTool = new SocketTool();
	private SocketToolThreadSafe socketTool = new SocketToolThreadSafe();
	private Protocol xyInfo = new Protocol(); // 集中式表
	private WaterProtocol waterxyInfo = new WaterProtocol();// 水表协议
	private Protocol_645 xyInfo_db = new Protocol_645(); // 单表协议

	private KDZTService() {
	}

	private static class SingletonHolder {
		private static KDZTService instance = new KDZTService();
	}

	public static KDZTService getSingleInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * 通断控制
	 * 
	 * @param serverIP   通讯服务器IP
	 * @param serverPort 通讯端口
	 * @param txadd      电表通讯地址
	 * @param line       线号(-1代表单表)
	 * @param tdTag      通断类型 0断 1通
	 * @return
	 */
	public String eleOnOff(String serverIP, int serverPort, long txadd, int line, int tdTag) {
		String[] txcs = new String[3];
		txcs[0] = Long.toString(txadd);
		txcs[1] = Integer.toString(line);
		txcs[2] = Integer.toString(tdTag);

		byte[] sendContent = null;
		Object backData = null;
		if (line == -1) { // 单表通断
			sendContent = xyInfo_db.getCommand("2", txcs);
			backData = socketTool.sendData(serverIP, serverPort, sendContent, 221);
		} else {
			sendContent = xyInfo.getCommand("28", txcs);
			backData = socketTool.sendData(serverIP, serverPort, sendContent, 223);
		}

		if (backData instanceof String) {
			String result = "";
			if (backData.equals("")) {
				return "Err,0";
			}
			try {
				if (line == -1) {
					result = xyInfo_db.analysisBack((String) backData);
				} else {
					result = xyInfo.analysisBack((String) backData);
				}
			} catch (Exception e) {
				socketTool.setExit(true);
				logger.error("解析响应结果异常", e);
			}
			if (result.equals("")) {
				return "Err,0"; // 无返回数据
			} else {
				return result;
			}
		} else {
			return "Err,-1";
		}
	}

	/**
	 * 读取通断状态
	 * 
	 * @param serverIP   服务器IP
	 * @param serverPort 服务器端口号
	 * @param txadd      电表通讯地址
	 * @param line       线号(-1代表单表)
	 * @return
	 */
	public String readOnOffState(String serverIP, int serverPort, long txadd, int line) {
		String[] txcs = new String[2];
		txcs[0] = Long.toString(txadd);
		txcs[1] = Integer.toString(line);

		byte[] sendContent = null;
		Object backData = null;
		if (line == -1) {
			sendContent = xyInfo_db.getCommand("3", txcs);
			backData = socketTool.sendData(serverIP, serverPort, sendContent, 221);
		} else {
			sendContent = xyInfo.getCommand("30", txcs);
			backData = socketTool.sendData(serverIP, serverPort, sendContent, 223);
		}

		if (backData instanceof String) {
			String result = "";
			try {
				if (line == -1) {
					result = xyInfo_db.analysisBack((String) backData);
				} else {
					result = xyInfo.analysisBack((String) backData);
				}
			} catch (Exception e) {
				socketTool.setExit(true);
				logger.error("解析响应结果异常", e);
			}
			if (result.equals("")) {
				return "Err,0";
			} else {
				return result;
			}
		} else {
			return "Err,-1";
		}
	}

	/**
	 * 读取电量
	 * 
	 * @param serverIP   服务器IP
	 * @param serverPort 服务器端口号
	 * @param txadd      通讯地址
	 * @param line       线号 线号0 代表整表 -1代表645单表
	 * @return 线号,电量|线号,电量|.....
	 */
	public String readPowerValue(String serverIP, int serverPort, long txadd, int line) {
		byte[] sendContent = null;
		Object backData = null;
		String[] txcs = new String[2];
		txcs[0] = Long.toString(txadd); // 表地址
		txcs[1] = Integer.toString(line);

		if (line == -1) {
			sendContent = xyInfo_db.getCommand("1", txcs);
			backData = socketTool.sendData(serverIP, serverPort, sendContent, 221);
		} else {
			sendContent = xyInfo.getCommand("36", txcs);
			backData = socketTool.sendData(serverIP, serverPort, sendContent, 223);
		}

		if (backData instanceof String) {
			String result = "";
			try {
				if (line == -1) {
					result = xyInfo_db.analysisBack((String) backData);
				} else {
					result = xyInfo.analysisBack((String) backData);
				}
			} catch (Exception e) {
				socketTool.setExit(true);
				logger.error("解析响应结果异常", e);
			}

			if (result.equals("")) {
				return "Err,0";
			} else {
				return result;
			}
		} else {
			return "Err,-1";
		}
	}

	/**
	 * 根据错误码返回错误提示
	 * 
	 * @param errCode 错误码
	 * @return 错误描述
	 */
	public String errInfo(int errCode) {
		String errInfo = "";
		switch (errCode) {
			case -1:
				errInfo = "socket连接异常";
				break;
			case 0:
				errInfo = "解析数据为空";
				break;
			case 1:
				errInfo = "数据校验错误！";
				break;
			case 2:
				errInfo = "数据长度错误！";
				break;
			case 3:
				errInfo = "接收数据不完整.有起始符.无结束符.等待定时时间到！";
				break;
			case 4:
				errInfo = "无效售电.售电次数不同；表现为重复售电或跨次购电！";
				break;
			case 5:
				errInfo = "线号超出范围（超电表允许使用户数）！";
				break;
			case 6:
				errInfo = "售电使剩余电量溢出.超99999.9度！";
				break;
			case 7:
				errInfo = "命令无效.可能用户不存在或指令针对的电表不接受该指令！";
				break;
			case 8:
				errInfo = "通讯密码不正确！";
				break;
			case 14:
				errInfo = "设备无正确返回数据！";
				break;
			case 15:
				errInfo = "通讯服务软件发送失败";
				break;
			default:
				errInfo = "未知错误";
				break;
		}
		return errInfo;
	}

	/**
	 * 读取水表值
	 * 
	 * @param serverIP   服务器IP
	 * @param serverPort 服务器端口号
	 * @param txadd      水表通讯地址
	 * @return
	 */
	public String readWaterValue(String serverIP, int serverPort, long txadd) {
		String[] txcs = new String[1];
		txcs[0] = "" + txadd;

		byte[] sendContent = waterxyInfo.fzxy(waterxyInfo.getCommand("01", txcs), 1);
		Object backData = socketTool.sendData(serverIP, serverPort, sendContent, 255);
		if (backData instanceof String) {
			String result = waterxyInfo.analysisBack((String) backData);
			if (result.equals("")) {
				return "Err,0";
			} else {
				return result;
			}
		} else {
			return "Err,-1";
		}
	}

	/**
	 * 水表开关阀
	 * 
	 * @param serverIP   服务器IP
	 * @param serverPort 服务器端口号
	 * @param txadd      水表通讯地址
	 * @param onOffTag   开关阀标志 55开阀 99关阀
	 * @return
	 */
	public String waterOnOff(String serverIP, int serverPort, long txadd, String onOffTag) {
		String[] txcs = new String[2];
		txcs[0] = Long.toString(txadd);
		txcs[1] = onOffTag;
		byte[] sendContent = null;
		Object backData = null;
		sendContent = waterxyInfo.fzxy(waterxyInfo.getCommand("04", txcs), 4);
		backData = socketTool.sendData(serverIP, serverPort, sendContent, 255);

		if (backData instanceof String) {
			String result = "";
			if (backData.equals("")) {
				return "Err,0";
			}
			result = waterxyInfo.analysisBack((String) backData);
			if (result.equals("")) {
				return "Err,0"; // 无返回数据
			} else {
				return result;
			}
		} else {
			return "Err,-1";
		}
	}

	/**
	 * 读取水表阀门状态
	 * 
	 * @param serverIP   服务器IP
	 * @param serverPort 服务器端口号
	 * @param txadd      水表地址
	 * @return
	 */
	public String readOnOffState_Water(String serverIP, int serverPort, long txadd) {
		String[] txcs = new String[2];
		txcs[0] = Long.toString(txadd);
		txcs[1] = "66";
		byte[] sendContent = null;
		Object backData = null;
		sendContent = waterxyInfo.fzxy(waterxyInfo.getCommand("04", txcs), 4);
		backData = socketTool.sendData(serverIP, serverPort, sendContent, 255);

		if (backData instanceof String) {
			String result = "";

			if (backData.equals("")) {
				return "Err,0";
			}
			result = waterxyInfo.analysisBack((String) backData);
			if (result.equals("")) {
				return "Err,0"; // 无返回数据
			} else {
				return result;
			}
		} else {
			return "Err,-1";
		}
	}
}
