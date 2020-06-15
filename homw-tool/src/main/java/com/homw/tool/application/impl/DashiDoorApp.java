package com.homw.tool.application.impl;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Controller;

import com.homw.tool.annotation.Application;
import com.homw.tool.api.dashi.DashiDoorApi;
import com.homw.tool.application.AbstractApplication;

/**
 * @description 达石门禁应用
 * @author Hom
 * @version 1.0
 * @since 2020-3-10
 */
@Controller
@Application("dashiDoorApp")
public class DashiDoorApp extends AbstractApplication {

	@Override
	protected void configArgs(Options options) {
		options.addOption(Option.builder("h").longOpt("host").hasArg().required().desc("door hostname").build());
		options.addOption(Option.builder("p").longOpt("port").hasArg().required().desc("door port").build());
		options.addOption(Option.builder("d").longOpt("addr").hasArg().required().desc("door mac addr").build());
		options.addOption(Option.builder("n").longOpt("line").hasArg().required().desc("door line number").build());
	}

	@Override
	protected void validateArgs(CommandLine params) {}

	@Override
	protected void execute(CommandLine params) throws Exception {
		String host = params.getOptionValue("h");
		String port = params.getOptionValue("p");
		String addr = params.getOptionValue("d");
		String readno = params.getOptionValue("n");

		Integer readNo = Integer.valueOf(readno);
		readNo = (int) Math.pow(2, readNo);
		addr = addr.substring(6);

		StringBuffer strBuf = new StringBuffer();
		strBuf.append("55").append("04").append(addr).append("2D").append("0001").append("0").append(readNo)
				.append("00");
		byte[] sumBytes = DashiDoorApi.checkSum(DashiDoorApi.hexStr2Bytes(strBuf.toString()), 2);
		strBuf.append(DashiDoorApi.bytes2HexString(sumBytes));
		logger.info("checksum: {}", strBuf.toString());

		byte[] data = DashiDoorApi.hexStr2Bytes(strBuf.toString());
		logger.info("send packet: {}", DashiDoorApi.bytesToHexStr(data));

		DashiDoorApi.send(host, Integer.valueOf(port), data);
		logger.info("open door success.");
	}
}