package com.homw.message.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.homw.message.service.IActivemqMessageService;

@Service
public class ActivemqMessageServiceImpl implements IActivemqMessageService {
	
	@Autowired(required = false)
    JmsTemplate jmsTemplate;

	@Override
	public void sendMessage(Object message) {
		jmsTemplate.convertAndSend(message);
	}
}