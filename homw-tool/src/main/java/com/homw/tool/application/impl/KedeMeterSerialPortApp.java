package com.homw.tool.application.impl;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
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
	protected void configArgs(Options options) {
		options.addOption(Option.builder("d").longOpt("addr").hasArg().required().desc("meter addr").build());
		options.addOption(Option.builder("f").longOpt("flag").hasArg().desc("action flag, open: 0(default), close: 1").build());
	}
	
	@Override
	protected void validateArgs(CommandLine params) {}
	
	@Override
	protected void execute(CommandLine params) throws Exception {
		String addr = params.getOptionValue("d");
		String opt = params.getOptionValue("f");

		ICommPortService commPortService = (ICommPortService) SpringContextUtil.getBean("commPortService");
		if (opt == null || "0".equals(opt.toString())) {
			commPortService.sendOpenElecMsg(addr);
		} else {
			commPortService.sendCloseElecMsg(addr);
		}
	}
}
