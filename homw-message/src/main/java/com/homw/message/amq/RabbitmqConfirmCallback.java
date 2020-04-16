package com.homw.message.amq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * @description rabbitmq消息投递至exchange确认回调
 * @author Hom
 * @version 1.0
 * @date 2020-01-14
 */
public class RabbitmqConfirmCallback implements ConfirmCallback {
	private static Logger logger = LoggerFactory.getLogger(RabbitmqConfirmCallback.class);

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		if (!ack) {
			logger.error("消息发送失败，correlationId:" + correlationData.getId() + "，case:" + cause);
		}
	}
}
