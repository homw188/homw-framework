package com.homw.tool.application.impl;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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
	protected void configArgs(Options options) {
		options.addOption(Option.builder("h").longOpt("host").hasArg().required().desc("meter hostname").build());
		options.addOption(Option.builder("p").longOpt("port").hasArg().required().desc("meter port").build());
		options.addOption(Option.builder("d").longOpt("addr").hasArg().required().desc("meter mac addr").build());
		// 电表需要线号参数
		options.addOption(Option.builder("n").longOpt("line").hasArg().desc("meter line number").build());
	}

	@Override
	protected void validateArgs(CommandLine params) {}

	@Override
	protected void execute(CommandLine params) throws Exception {
		String host = params.getOptionValue("h");
		String port = params.getOptionValue("p");
		String addr = params.getOptionValue("d");

		String msg = null;
		Object readno = params.getOptionValue("n");
		if (readno == null) {
			msg = KDZTService.getSingleInstance().readWaterValue(host, Integer.parseInt(port), Long.valueOf(addr));
		} else {
			msg = KDZTService.getSingleInstance().readPowerValue(host, Integer.parseInt(port), Integer.parseInt(addr),
					Integer.parseInt(readno.toString()));
		}
		logger.info("back data is: {}", msg);

		Double readNum = 0.0;
		if (msg.contains(",") && msg.split(",")[0].equals(addr)) {
			readNum = Double.parseDouble(msg.split(",")[1]);
		}
		logger.info("ammeter value is: {}", readNum.intValue());
	}
}
