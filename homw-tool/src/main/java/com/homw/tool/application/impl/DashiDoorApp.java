package com.homw.tool.application.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.api.dashi.DashiDoorApi;
import com.homw.tool.application.AbstractApplication;

/**
 * @description 达石门禁应用
 * @author Hom
 * @version 1.0
 * @since 2020-3-10
 */
@Controller
@Application("dashiDoorApp")
public class DashiDoorApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length != 5) {
			throw new IllegalArgumentException("args must four items.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ip", args[1]);
		params.put("port", args[2]);
		params.put("addr", args[3]);
		params.put("readno", args[4]);
		return params;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0] + " ip port addr readno");
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		String ip = params.get("ip").toString();
		String port = params.get("port").toString();
		String addr = params.get("addr").toString();
		String readno = params.get("readno").toString();

		Integer readNo = Integer.valueOf(readno);
		readNo = (int) Math.pow(2, readNo);
		addr = addr.substring(6);

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("55").append("04").append(addr).append("2D").append("0001").append("0").append(readNo)
				.append("00");
		byte[] sumBytes = DashiDoorApi.checkSum(DashiDoorApi.hexStr2Bytes(strBuf.toString()), 2);
		strBuf.append(DashiDoorApi.bytes2HexString(sumBytes));
		logger.info("checksum: {}", strBuf.toString());

		byte[] data = DashiDoorApi.hexStr2Bytes(strBuf.toString());
		logger.info("send packet: {}", DashiDoorApi.bytesToHexStr(data));

		DashiDoorApi.send(ip, Integer.valueOf(port), data);
		logger.info("open door success.");
	}
}