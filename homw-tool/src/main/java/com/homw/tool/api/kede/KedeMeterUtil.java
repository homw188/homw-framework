package com.homw.tool.api.kede;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 科德水电表通信工具类
 * @author Hom
 * @version 1.0
 * @since 2020-07-20
 */
public class KedeMeterUtil {

	private static final Logger logger = LoggerFactory.getLogger(KedeMeterUtil.class);

	/**
	 * 读电表数据
	 * 
	 * @param ip
	 * @param port
	 * @param addr 电表地址
	 * @param wait 等待延时，单位秒
	 * @return {@link KedeProtocolUtil#parseElecData(String)}
	 */
	public static String readElecData(String ip, int port, String addr, int wait) {
		try {
			String data = KedeProtocolUtil.add33H("078102FF") + "34333333";
			String str = KedeProtocolUtil.getDataFrame(addr, "03", data);
			logger.info("send data: " + str);
			
			KedeNettyClient nettyClient = new KedeNettyClient();
			String readStr = null;
			try {
				nettyClient.connect(ip, port);
				readStr = nettyClient.send(str, wait);
			} finally {
				nettyClient.close();
			}
			logger.info("back data: " + readStr);
			return KedeProtocolUtil.parseElecData(readStr);
		} catch (Exception e) {
			logger.error("读数异常", e);
			return null;
		}
	}

	/**
	 * 读水表数据
	 * 
	 * @param ip
	 * @param port
	 * @param addr 水表地址
	 * @param wait 等待延时，单位秒
	 * @return {@link KedeProtocolUtil#parseWaterData(String)}
	 */
	public static String readWaterData(String ip, int port, String addr, int wait) {
		try {
			String data = KedeProtocolUtil.add33H("20000100");
			String str = KedeProtocolUtil.getDataFrame(addr, "11", data);
			logger.info("send data: " + str);
			
			KedeNettyClient nettyClient = new KedeNettyClient();
			String readStr = null;
			try {
				nettyClient.connect(ip, port);
				readStr = nettyClient.send(str, wait);
			} finally {
				nettyClient.close();
			}
			logger.info("back data: " + readStr);
			return KedeProtocolUtil.parseWaterData(readStr);
		} catch (Exception e) {
			logger.error("读数异常", e);
			return null;
		}
	}

	/**
	 * 电表拉合闸
	 * 
	 * @param ip
	 * @param port
	 * @param addr   电表地址
	 * @param action 合闸=0 拉闸=1
	 * @param wait   等待延时，单位秒
	 * @return {@link KedeProtocolUtil#parseElecSwitch(String)}
	 */
	public static String pullElecSwitch(String ip, int port, String addr, int action, int wait) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
			String data = "";
			if (action == 0) {// 0合闸
				data = "CB333333343333334E33";
			} else {// 1拉闸
				data = "CB333333343333334D33";
			}
			data += KedeProtocolUtil.add33H(sdf.format(new Date()));
			String str = KedeProtocolUtil.getDataFrame(addr, "1C", data);
			logger.info("send data: " + str);

			KedeNettyClient nettyClient = new KedeNettyClient();
			String readStr = null;
			try {
				nettyClient.connect(ip, port);
				readStr = nettyClient.send(str, wait);
			} finally {
				nettyClient.close();
			}
			logger.info("back data: " + readStr);
			return KedeProtocolUtil.parseElecSwitch(readStr);
		} catch (Exception e) {
			logger.error("开关异常", e);
			return null;
		}
	}

	/**
	 * 水表开关阀
	 * 
	 * @param ip
	 * @param port
	 * @param addr   水表地址
	 * @param action 合闸=0 拉闸=1
	 * @param wait   等待延时，单位秒
	 * @return {@link KedeProtocolUtil#parseWaterSwitch(String)}
	 */
	public static String pullWaterSwitch(String ip, int port, String addr, int action, int wait) {
		try {
			String data = KedeProtocolUtil.add33H("20000204") + KedeProtocolUtil.add33H("00000002")
					+ KedeProtocolUtil.add33H("00000000");
			if (action == 0) {
				data += KedeProtocolUtil.add33H("00");
			} else {
				data += KedeProtocolUtil.add33H("ff");
			}
			String str = KedeProtocolUtil.getDataFrame(addr, "14", data);
			logger.info("send data: " + str);

			KedeNettyClient nettyClient = new KedeNettyClient();
			String readStr = null;
			try {
				nettyClient.connect(ip, port);
				readStr = nettyClient.send(str, wait);
			} finally {
				nettyClient.close();
			}
			logger.info("back data: " + readStr);
			return KedeProtocolUtil.parseWaterSwitch(readStr);
		} catch (Exception e) {
			logger.error("开关异常", e);
			return null;
		}
	}

	/**
	 * 电费充值
	 * 
	 * @param ip
	 * @param port
	 * @param addr  电表地址
	 * @param wait  等待延时，单位秒
	 * @param power 充电量为整数，当充入1度电时需传入100
	 * @param cs    充值次数，可根据状态查询获得
	 * @param fx    充值方向，0正向1负向
	 * @return {@link KedeProtocolUtil#parseElecCharge(String)}
	 */
	public static String chargeElec(String ip, int port, String addr, int wait, int power, int cs, int fx) {
		try {
			int czcs = cs + 1;
			String czsl = String.format("%08d", power);
			String data = "3235343A34333333" + KedeProtocolUtil.add33H(czsl)
					+ KedeProtocolUtil.add33H(String.format("%08d", czcs));
			if (fx == 1) {// 0正向1负向
				data += "3433333333333333333333333333";
			} else {
				data += "3333333333333333333333333333";
			}
			String str = KedeProtocolUtil.getDataFrame(addr, "03", data);
			logger.info("send data: " + str);

			KedeNettyClient nettyClient = new KedeNettyClient();
			String readStr = null;
			try {
				nettyClient.connect(ip, port);
				readStr = nettyClient.send(str, wait);
			} finally {
				nettyClient.close();
			}
			logger.info("back data: " + readStr);
			return KedeProtocolUtil.parseElecCharge(readStr);
		} catch (Exception e) {
			logger.error("充值异常", e);
			return null;
		}
	}

}
