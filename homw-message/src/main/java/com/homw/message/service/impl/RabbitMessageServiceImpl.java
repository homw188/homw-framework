package com.homw.message.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.homw.message.service.IRabbitMessageService;

@Service
public class RabbitMessageServiceImpl implements IRabbitMessageService {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMessageServiceImpl.class);

	@Autowired(required = false)
	private RabbitTemplate rabbitTemplate;

	@Override
	public void sendMessage(String routingKey, Object message) {
		logger.info("sendMessage message:{}", message);
		logger.info("sendMessage routingKey:{}", routingKey);

		rabbitTemplate.convertAndSend(routingKey, message);
	}
}