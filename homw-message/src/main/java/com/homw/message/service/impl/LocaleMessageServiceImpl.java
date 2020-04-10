package com.homw.message.service.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import com.homw.message.constant.LocaleMessageKey;
import com.homw.message.service.ILocaleMessageService;

/**
 * @description 消息服务
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
@Service
public class LocaleMessageServiceImpl implements ILocaleMessageService {
	@Autowired
	@Qualifier("messageSource")
	protected ReloadableResourceBundleMessageSource messageSource;
	@Autowired
	@Qualifier("errorMessageSource")
	protected ReloadableResourceBundleMessageSource errorMessageSource;

	@Override
	public String getMessage(LocaleMessageKey messageConstant, Object[] args, Locale locale) {
		return messageSource.getMessage(messageConstant.name(), args, locale);
	}

	@Override
	public String getErrorMessage(String errorCode, Object[] args, String defaultMessage, Locale locale) {
		return errorMessageSource.getMessage("error.code." + errorCode, args, defaultMessage, locale);
	}
}