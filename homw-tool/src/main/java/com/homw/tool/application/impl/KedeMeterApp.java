package com.homw.tool.application.impl;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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
	protected void configArgs(Options options) {
		options.addOption(Option.builder("h").longOpt("host").hasArg().required().desc("meter hostname").build());
		options.addOption(Option.builder("p").longOpt("port").hasArg().required().desc("meter port").build());
		options.addOption(Option.builder("d").longOpt("addr").hasArg().required().desc("meter mac addr").build());
		options.addOption(Option.builder("t").longOpt("timeout").hasArg().required().desc("read meter timeout").build());
		// 电表拉合闸参数（合闸=0 拉闸=1）
		options.addOption(Option.builder("f").longOpt("flag").hasArg().desc("action flag, open: 0, close: 1").build());
	}
	
	@Override
	protected void validateArgs(CommandLine params) {}
	
	@Override
	protected void execute(CommandLine params) throws Exception {
		String host = params.getOptionValue("h");
		String port = params.getOptionValue("p");
		String addr = params.getOptionValue("d");
		String timeout = params.getOptionValue("t");
		String action = params.getOptionValue("f");

		String backData = null;
		if (action == null) {
			backData = KedeElecOpenUtil.ztcx(host, Integer.parseInt(port), addr, Integer.parseInt(timeout));
		} else {
			backData = KedeElecOpenUtil.eleAction(host, Integer.parseInt(port), addr, Integer.parseInt(action.toString()), Integer.parseInt(timeout));
		}
		logger.info("backData is: {}", backData);
	}
}
