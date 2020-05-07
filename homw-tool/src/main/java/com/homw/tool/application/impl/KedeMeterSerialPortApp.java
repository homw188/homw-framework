package com.homw.tool.application.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.api.kede.serial.ICommPortService;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description 科德串口表应用
 * @author Hom
 * @version 1.0
 * @since 2020-03-13
 */
@Controller
@Application("kedeMeterSerialPortApp")
public class KedeMeterSerialPortApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length < 2) {
			throw new IllegalArgumentException("args at least two items.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("addr", args[1]);
		if (args.length > 2) {
			params.put("opt", args[2]);
		}
		return params;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0] + " addr opt");
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		String addr = params.get("addr").toString();
		Object opt = params.get("opt");

		ICommPortService commPortService = (ICommPortService) SpringContextUtil.getBean("commPortService");
		if (opt == null || "open".equals(opt.toString())) {
			commPortService.sendOpenElecMsg(addr);
		} else {
			commPortService.sendCloseElecMsg(addr);
		}
	}

}
