package com.homw.message.service;

import java.util.Locale;

import com.homw.message.constant.LocaleMessageKey;

/**
 * @description Locale消息服务
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public interface ILocaleMessageService {
	/**
	 * 根据locale获取消息
	 * @param messageKey
	 * @param args
	 * @param locale 语言环境
	 * @return
	 */
	String getMessage(LocaleMessageKey messageKey, Object[] args, Locale locale);
	
	/**
	 * 根据locale获取异常消息
	 * @param errorCode 异常码
	 * @param args
	 * @param defaultMessage 未找到，默认消息
	 * @param locale 语言环境
	 * @return
	 */
	String getErrorMessage(String errorCode, Object[] args, String defaultMessage, Locale locale);
}
