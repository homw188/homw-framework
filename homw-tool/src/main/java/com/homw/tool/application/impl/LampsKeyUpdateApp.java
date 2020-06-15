package com.homw.tool.application.impl;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
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
	protected void configArgs(Options options) {}
	
	@Override
	protected void validateArgs(CommandLine params) {}

	@Override
	protected void execute(CommandLine params) throws Exception {
		ILampsKeyUpdateService updateService = (ILampsKeyUpdateService) SpringContextUtil
				.getBean("lampsKeyUpdateService");
		updateService.updateBatch();
	}
}