package com.homw.test.activemq.start;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * @description 消息队列spring集成生产端
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
@Component("queueSender")
public class SpringQueueProducer {
	@Autowired(required=false)
	private JmsTemplate jmsQueueTemplate;

	public void send() {
		jmsQueueTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("Hello, spring activemq.");
			}
		});
	}
}
