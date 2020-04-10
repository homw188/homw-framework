package com.homw.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		if (args == null || args.length == 0) {
			logger.error("Arguments exception, please check your inputs. "
					+ "prompt: args must not null, and at least one item.");
			logger.error("Usage:\tappKey");
			System.exit(1);
		}
		ApplicationFactory.init("com.homw.tool.application.impl");
		ApplicationFactory.create(args[0]).start(args);
	}
}
