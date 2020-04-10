package com.homw.tool.api.kede;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KedeElecOpenUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(KedeElecOpenUtil.class);
	
	/**
	 * 电表状态查询
	 * @param ip
	 * @param port
	 * @param addr 电表地址
	 * @param waiting 等待延时，单位秒
	 * @return json字符串：lj ：累计用电; sy:表示剩余电量,cs 表示充值次数({"data":{"lj":"000053.96","sy":"-000000.00","cs":"0000"})
	 */
	public static String ztcx(String ip, int port, String addr, int waiting) { 
		try {
			String dota = KedeDataProtocolUtil.DataAdd33H("078102FF") + "34333333";
			String strr = KedeDataProtocolUtil.GetAllCode(addr, "03", dota);
			String Result = "";
			KedeNettyClient nettyClient = new KedeNettyClient();
			logger.info("send data: " + strr);
			String ReadStr = nettyClient.startClient(ip, port, waiting, strr);
			logger.info("back data: " + ReadStr);
			Result = KedeDataProtocolUtil.ZtcxData(ReadStr);
			return Result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 电表拉合闸
	 * @param ip
	 * @param port
	 * @param addr 电表地址
	 * @param TD 合闸=0 拉闸=1
	 * @param waiting 等待延时，单位秒
	 * @return json字符串：{"data":{"err":"Yes"},"flag":"0"}
	 */
	public static String eleAction(String ip, int port, String addr, int TD, int waiting) { // 状态查询
		try {
			Date da = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
			String dota = "";
			if (TD == 0) // 0合闸
			{
				dota = "CB333333343333334E33";
			} else // 1拉闸
			{
				dota = "CB333333343333334D33";
			}
			dota += KedeDataProtocolUtil.DataAdd33H(sdf.format(da));
			String strr = KedeDataProtocolUtil.GetAllCode(addr, "1C", dota);
			String Result = "";
			KedeNettyClient nettyClient = new KedeNettyClient();
			String ReadStr = nettyClient.startClient(ip, port, waiting, strr);
			Result = KedeDataProtocolUtil.Set94(ReadStr);
			return Result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param ip
	 * @param port
	 * @param addr 电表地址
	 * @param waiting 等待延时，单位秒
	 * @param Ele_C 充电量为整数，当充入1度电时需传入100
	 * @param Cs 充值次数，可根据状态查询获得
	 * @param Fx 充值方向，0正向1负向
	 * @return json字符串
	 */
	public static String eleBuy(String ip, int port, String addr, int waiting, int Ele_C, int Cs, int Fx) {
		try {
			int czcs = Cs + 1;
			String czsl = String.format("%08d", Ele_C);
			String dota = "3235343A34333333" + KedeDataProtocolUtil.DataAdd33H(czsl)
					+ KedeDataProtocolUtil.DataAdd33H(String.format("%08d", czcs));
			if (Fx == 1) // 0正向1负向
			{
				dota += "3433333333333333333333333333";
			} else {
				dota += "3333333333333333333333333333";
			}
			String strr = KedeDataProtocolUtil.GetAllCode(addr, "03", dota);
			String Result = "";
			KedeNettyClient nettyClient = new KedeNettyClient();
			String ReadStr = nettyClient.startClient(ip, port, waiting, strr);
			Result = KedeDataProtocolUtil.Set83(ReadStr);
			return Result;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		// String msg = EleAction("192.168.0.105", 2756,"000511128101",0, 3);
		String msg = ztcx("172.16.252.3", 10001, "000010745109", 3);
		System.out.println("EleAction:" + msg);
		// "data":{"lj":"000053.96","sy":"-000000.00","cs":"0000"}
		// lj ：累计用电; sy:表示剩余电量,cs 表示充值次数

//			String msg = EleAction("172.16.252.3", 10001,"000010745109",0, 10);
//			System.out.println(msg);
		// {"data":{"err":"Yes"},"flag":"0"}
	}

}
