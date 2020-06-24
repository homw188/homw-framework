package com.homw.modbus;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Modbus启动类
 * 
 * @author Hom
 * @version 1.0
 * @since 2018年11月16日
 *
 */
public class Main {

	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption(Option.builder("s").longOpt("server").desc("server mode").build());
		options.addOption(Option.builder("c").longOpt("client").desc("client mode").build());
		options.addOption(Option.builder("f").longOpt("file").hasArg().desc("config properties file path").build());
		CommandLine params = new DefaultParser().parse(options, args, true);
		
		String cfg = params.getOptionValue("f");
		if (params.hasOption("s")) {
			if (cfg == null) {
				ModbusServerFactory.create();
			} else {
				ModbusServerFactory.create(cfg);
			}
		} else if (params.hasOption("c")) {
			if (cfg == null) {
				ModbusClientFactory.create();
			} else {
				ModbusClientFactory.create(cfg);
			}
		} else {
			new HelpFormatter().printHelp("-s | -c [-f]", options);
		}
	}
}
