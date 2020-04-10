package com.homw.tool.application.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.api.kede.KedeElecOpenUtil;
import com.homw.tool.application.AbstractApplication;

/**
 * @description 科德抄表应用
 * @author Hom
 * @version 1.0
 * @since 2019-11-13
 */
@Controller
@Application("kedeMeterReadApp")
public class KedeMeterReadApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length != 5) {
			throw new IllegalArgumentException("args must four items.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ip", args[1]);
		params.put("port", args[2]);
		params.put("addr", args[3]);
		params.put("timeout", args[4]);
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

		String msg = KedeElecOpenUtil.ztcx(ip, Integer.parseInt(port), addr, Integer.parseInt(timeout));
		logger.info("ammeter value is: " + msg);
	}

}
