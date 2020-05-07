package com.homw.tool.application.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.api.kede.KedeElecOpenUtil;
import com.homw.tool.application.AbstractApplication;

/**
 * @description 科德电表应用
 * @author Hom
 * @version 1.0
 * @since 2019-11-13
 */
@Controller
@Application("kedeMeterApp")
public class KedeMeterApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length < 5) {
			throw new IllegalArgumentException("args must four items.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ip", args[1]);
		params.put("port", args[2]);
		params.put("addr", args[3]);
		params.put("timeout", args[4]);
		if (args.length > 5) {
			params.put("action", args[5]);
		}
		return params;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0] + " ip port addr timeout");
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		String ip = params.get("ip").toString();
		String port = params.get("port").toString();
		String addr = params.get("addr").toString();
		String timeout = params.get("timeout").toString();
		Object action = params.get("action");

		String backData = null;
		if (action == null) {
			backData = KedeElecOpenUtil.ztcx(ip, Integer.parseInt(port), addr, Integer.parseInt(timeout));
		} else {
			backData = KedeElecOpenUtil.eleAction(ip, Integer.parseInt(port), addr, Integer.parseInt(action.toString()), Integer.parseInt(timeout));
		}
		logger.info("backData is: " + backData);
	}

}
