package com.homw.tool.application.impl;

import java.util.HashMap;
import java.util.Map;

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
	protected Map<String, Object> parseArgs(String[] args) {
		if (args == null || args.length != 4) {
			throw new IllegalArgumentException("args must four items.");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("ip", args[1]);
		params.put("port", args[2]);
		params.put("addr", args[3]);
		return params;
	}

	@Override
	protected void printHint(String[] args) {
		logger.error("Usage:\t" + args[0] + " ip port addr");
	}

	private String apiToken = "59C15FEC07AD1B282ED09137976FA596DF63AF2B09F6265213C53EC421B7A4AC";// hash("test")
	private int holdInterval = 5;// 开门时长，单位：秒
	
	// zkDoorApp 192.168.83.30 8098 192.168.81.200-1（门禁名称参数错误，但接口返回正确）
	// zkDoorApp 192.168.83.30 8098 4028d39e71a0b7510171a0c0ff390144（32位）

	@Override
	protected void execute(Map<String, Object> params) throws Exception {
		String ip = params.get("ip").toString();
		String port = params.get("port").toString();
		String addr = params.get("addr").toString();

		String url = "http://" + ip + ":" + port + "/api/door/remoteOpenById?doorId=" + addr + "&interval="
				+ holdInterval + "&access_token=" + apiToken;
		/*String url = "http://" + ip + ":" + port + "/api/door/remoteOpenByName?doorName=" + addr + "&interval="
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