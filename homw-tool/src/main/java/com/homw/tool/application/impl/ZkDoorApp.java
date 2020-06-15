package com.homw.tool.application.impl;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.homw.tool.annotation.Application;
import com.homw.tool.application.AbstractApplication;

import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;

/**
 * @description 中控门禁应用
 * @author Hom
 * @version 1.0
 * @since 2020-05-26
 */
@Controller
@Application("zkDoorApp")
public class ZkDoorApp extends AbstractApplication {
	
	@Override
	protected void configArgs(Options options) {
		options.addOption(Option.builder("h").longOpt("host").hasArg().required().desc("door hostname").build());
		options.addOption(Option.builder("p").longOpt("port").hasArg().required().desc("door port").build());
		options.addOption(Option.builder("d").longOpt("addr").hasArg().required().desc("door mac addr").build());
	}
	
	@Override
	protected void validateArgs(CommandLine params) {}
	
	private String apiToken = "59C15FEC07AD1B282ED09137976FA596DF63AF2B09F6265213C53EC421B7A4AC";// hash("test")
	private int holdInterval = 5;// 开门时长，单位：秒
	
	// zkDoorApp 192.168.83.30 8098 192.168.81.200-1（门禁名称参数错误，但接口返回正确）
	// zkDoorApp 192.168.83.30 8098 4028d39e71a0b7510171a0c0ff390144（32位）

	@Override
	protected void execute(CommandLine params) throws Exception {
		String host = params.getOptionValue("h");
		String port = params.getOptionValue("p");
		String addr = params.getOptionValue("d");

		String url = "http://" + host + ":" + port + "/api/door/remoteOpenById?doorId=" + addr + "&interval="
				+ holdInterval + "&access_token=" + apiToken;
		/*String url = "http://" + host + ":" + port + "/api/door/remoteOpenByName?doorName=" + addr + "&interval="
				+ holdInterval + "&access_token=" + apiToken;*/
		logger.info("url:{}", url);

		HttpResponse response = HttpUtil.createPost(url).timeout(HttpGlobalConfig.getTimeout()).execute();
		if (response.isOk()) {
			String result = response.body();
			JSONObject obj = JSON.parseObject(result);
			int code = obj.getIntValue("code");
			if (code >= 0) {
				logger.info("open door success, code: {}", code);
			} else {
				String message = obj.getString("message");
				logger.error("open door fail: {}-{}", code, message);
			}
		} else {
			logger.error("open door fail: without reponse");
		}
	}
}