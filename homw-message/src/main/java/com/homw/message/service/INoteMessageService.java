package com.homw.message.service;

/**
 * @description 短信服务
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public interface INoteMessageService {
	/**
	 * 发送短信
	 * @param mobile 手机号
	 * @param templateId 短信平台模板id
	 * @param args 替换模板参数
	 */
	void sendNote(String mobile, String templateId, String[] args);
}
