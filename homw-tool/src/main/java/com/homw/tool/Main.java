package com.homw.tool;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.homw.tool.application.AbstractApplication;
import com.homw.tool.application.ApplicationFactory;

/**
 * @description 系统入口
 * @author Hom
 * @version 1.0
 * @since 2019-05-20
 */
public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		ApplicationFactory.init("com.homw.tool.application.impl");
		
		Options options = AbstractApplication.buildCliOptions();
		CommandLine params = null;
		try {
			params = AbstractApplication.parseArgs(args, options, true);
		} catch (Exception e) {
			logger.error("Arguments exception, please check your inputs.", e);
			AbstractApplication.printHint(options);
			System.exit(1);
		}
		
		String appName = params.getOptionValue("a");
		if (StringUtils.isNotEmpty(appName)) {
			ApplicationFactory.create(appName).start(args, options);
		} else if (params.hasOption("ls")) {
			AbstractApplication.printAppList();
		} else {
			AbstractApplication.printHint(options);
		}
	}
}
