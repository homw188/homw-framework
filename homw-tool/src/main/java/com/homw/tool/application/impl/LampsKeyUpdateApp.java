package com.homw.tool.application.impl;

import java.util.Map;

import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.service.ILampsKeyUpdateService;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description 数据表更新应用
 * @author Hom
 * @version 1.0
 * @since 2019-07-18
 */
@Controller
@Application("lampsKeyUpdateApp")
public class LampsKeyUpdateApp extends AbstractApplication {
	@Override
	protected Map<String, Object> parseArgs(String[] args) {
		return null;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0]);
	}

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		ILampsKeyUpdateService updateService = (ILampsKeyUpdateService) SpringContextUtil
				.getBean("lampsKeyUpdateService");
		updateService.updateBatch();
	}
}