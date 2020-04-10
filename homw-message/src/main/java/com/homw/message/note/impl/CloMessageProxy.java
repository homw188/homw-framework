package com.homw.message.note.impl;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.homw.common.util.CodecUtil;
import com.homw.common.util.HttpClientUtil;
import com.homw.common.util.Platform;
import com.homw.message.note.INoteProxy;

/**
 * @description 短信发送
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
@Component
public class CloMessageProxy implements INoteProxy {

	private static final Logger logger = LoggerFactory.getLogger(CloMessageProxy.class);

	@Value("${clo.sid}")
	private String sid;
	@Value("${clo.authToken}")
	private String authToken;
	@Value("${clo.app.id}")
	private String appId;

	@Value("${clo.prod.sms.url}")
	private String smsProdUrl;// 生产环境地址
	@Value("${clo.sandbox.sms.url}")
	private String smsSandboxUrl;

	@Override
	public String sendNote(String mobile, String templateId, String[] args) {
		try {
			logger.info("url:sendSms\tparams:mobile=" + mobile + "&templateId=" + templateId);
			String content = getNoteData(mobile, templateId, args);
			String url = Platform.isProdEnv() ? smsProdUrl : smsSandboxUrl;

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String requestTime = simpleDateFormat.format(new Date());
			String authorization = new String(CodecUtil.encodeBase64((sid + ":" + requestTime).getBytes("UTF-8")));
			Map<String, String> headMap = new HashMap<String, String>();
			headMap.put("Accept", "application/json");
			headMap.put("Authorization", authorization);

			String sign = createSign(requestTime);
			String requestUrl = url + "?sig=" + sign;
			logger.info("requestUrl:" + requestUrl);
			String response = HttpClientUtil.post(requestUrl, content,
					ContentType.create("text/html", Charset.forName("UTF-8")), headMap);
			logger.info(response);
			return response;
		} catch (Exception e) {
			logger.error("send sms error", e);
			return e.getMessage();
		}
	}

	/**
	 * 创建签名
	 * 
	 * @param time
	 * @return
	 */
	private String createSign(String time) {
		String data = sid + authToken + time;
		String sign = DigestUtils.md5Hex(data).toUpperCase();
		logger.info("data=" + data + " sign=" + sign);
		return sign;
	}

	/**
	 * 封装短信内容
	 * 
	 * @param mobile
	 * @param templateId
	 * @param args
	 * @return
	 */
	private String getNoteData(String mobile, String templateId, String[] args) {
		StringBuilder sb = new StringBuilder("<?xml version='1.0' encoding='utf-8'?><TemplateSMS>");
		sb.append("<appId>").append(appId).append("</appId>").append("<to>").append(mobile).append("</to>")
				.append("<templateId>").append(templateId).append("</templateId>");
		if (args != null) {
			sb.append("<datas>");
			for (String s : args) {
				sb.append("<data>").append(s).append("</data>");
			}
			sb.append("</datas>");
		}
		sb.append("</TemplateSMS>");
		return sb.toString();
	}
}