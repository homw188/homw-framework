package com.homw.tool.application.impl;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;
import com.homw.tool.service.IPayRecordGenService;
import com.homw.tool.util.SpringContextUtil;

/**
 * @description 支付记录生成应用
 * @author Hom
 * @version 1.0
 * @since 2019-09-23
 */
@Controller
@Application(value = "payRecordGenApp", importDataSource = true)
public class PayRecordGenApp extends AbstractApplication {
	
	@Override
	protected void configArgs(Options options) {
		options.addOption(Option.builder("s").longOpt("space").hasArg().required().desc("space id").build());
	}
	
	@Override
	protected void validateArgs(CommandLine params) {}
	
	@Override
	protected void execute(CommandLine params) throws Exception {
		IPayRecordGenService updateService = (IPayRecordGenService) SpringContextUtil
				.getBean("payRecordGenService");
		updateService.generate(Long.valueOf(params.getOptionValue("s")));
	}
}