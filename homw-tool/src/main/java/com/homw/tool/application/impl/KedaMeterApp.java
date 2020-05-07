package com.homw.tool.application.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.api.keda.KDZTService;
import com.homw.tool.application.AbstractApplication;

/**
 * @description 科大水电表应用
 * @author Hom
 * @version 1.0
 * @since 2020-02-25
 */
@Controller
@Application("kedaMeterApp")
public class KedaMeterApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length < 4) {
			throw new IllegalArgumentException("args must three or four items.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ip", args[1]);
		params.put("port", args[2]);
		params.put("addr", args[3]);
		// 电表需要线号参数
		if (args.length == 5) {
			params.put("readno", args[4]);
		}
		return params;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0] + " ip port addr [readno]");
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		String ip = params.get("ip").toString();
		String port = params.get("port").toString();
		String addr = params.get("addr").toString();

		String msg = null;
		Object readno = params.get("readno");
		if (readno == null) {
			msg = KDZTService.getSingleInstance().readWaterValue(ip, Integer.parseInt(port), Long.valueOf(addr));
		} else {
			msg = KDZTService.getSingleInstance().readPowerValue(ip, Integer.parseInt(port), Integer.parseInt(addr),
					Integer.parseInt(readno.toString()));
		}
		logger.info("back data is: " + msg);
		
		Double readNum = 0.0;
		if (msg.contains(",") && msg.split(",")[0].equals(addr)) { 
			readNum = Double.parseDouble(msg.split(",")[1]);
		}
		logger.info("ammeter value is: " + readNum.intValue());
	}

}
