package com.homw.message.amq;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractJsonMessageConverter;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.MessageConversionException;

import com.alibaba.fastjson.JSON;

/**
 * @description rabbitmq消息转换器，基于fastjson
 * @author Hom
 * @version 1.0
 * @date 2020-01-13
 */
public class RabbitmqMessageConverter extends AbstractJsonMessageConverter {
	private static ClassMapper classMapper = new DefaultClassMapper();
	private static final Logger logger = LoggerFactory.getLogger(RabbitmqMessageConverter.class);

	@Override
	protected Message createMessage(Object object, MessageProperties messageProperties) {
		byte[] bytes = null;
		try {
			String jsonStr = JSON.toJSONString(object);
			bytes = jsonStr.getBytes(getDefaultCharset());
		} catch (UnsupportedEncodingException e) {
			throw new MessageConversionException("Failed to convert Message content", e);
		}
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setContentEncoding(getDefaultCharset());
		messageProperties.setTimestamp(new Date());
		if (bytes != null) {
			messageProperties.setContentLength(bytes.length);
		}
		classMapper.fromClass(object.getClass(), messageProperties);
		return new Message(bytes, messageProperties);
	}

	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		Object content = null;
		MessageProperties properties = message.getMessageProperties();
		if (properties != null) {
			String contentType = properties.getContentType();
			if (contentType != null && contentType.contains("json")) {
				String encoding = properties.getContentEncoding();
				if (encoding == null) {
					encoding = getDefaultCharset();
				}
				try {
					Class<?> targetClass = getClassMapper().toClass(message.getMessageProperties());
					String contentStr = new String(message.getBody(), encoding);
					content = JSON.parseObject(contentStr, targetClass);
				} catch (IOException e) {
					throw new MessageConversionException("Failed to convert Message content", e);
				}
			} else {
				logger.warn("Could not convert incoming message with content-type [" + contentType + "]");
			}
		}
		if (content == null) {
			content = message.getBody();
		}
		return content;
	}
}
