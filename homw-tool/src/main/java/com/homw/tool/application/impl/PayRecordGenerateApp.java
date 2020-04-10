package com.homw.tool.application.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.service.IPayRecordGenerateService;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description 支付记录生成应用
 * @author Hom
 * @version 1.0
 * @since 2019-09-23
 */
@Controller
@Application("payRecordGenApp")
public class PayRecordGenerateApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length != 2) {
			throw new IllegalArgumentException("args must four items.");
		}
		Map<String, Object> params = new HashMap<>();
		try {
			params.put("spaceId", Long.valueOf(args[1]));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("space id must be number.", e);
		}
		return params;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0] + " spaceId");
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		IPayRecordGenerateService updateService = (IPayRecordGenerateService) SpringContextUtil
				.getBean("payRecordGenerateService");
		updateService.generate((Long) params.get("spaceId"));
	}
}