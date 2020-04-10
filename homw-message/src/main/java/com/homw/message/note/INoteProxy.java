package com.homw.message.note;

/**
 * @description 短信发送接口
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public interface INoteProxy {

	/**
	 * 发送短信
	 * @param mobile 手机号
	 * @param templateId 短信模板id
	 * @param args 模板参数
	 * @return
	 */
    String sendNote(String mobile, String templateId, String[] args);
}
