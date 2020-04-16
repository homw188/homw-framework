package com.homw.message.amq;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;

/**
 * @description rabbitmq消息投递至queue失败回调
 * @author Hom
 * @version 1.0
 * @date 2020-01-14
 */
public class RabbitmqReturnCallback implements ReturnCallback {
	private static Logger logger = LoggerFactory.getLogger(RabbitmqReturnCallback.class);

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		try {
			logger.error("消息发送失败，exchange:" + exchange + "，routingKey:" + routingKey + "，replyCode:" + replyCode
					+ "，replyText:" + replyText + "，message:" + new String(message.getBody(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
