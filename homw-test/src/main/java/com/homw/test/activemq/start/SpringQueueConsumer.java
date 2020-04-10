package com.homw.test.activemq.start;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * @description 消息队列spring集成消费端
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
@Component("queueReceiver")
public class SpringQueueConsumer implements MessageListener {
	@Autowired(required=false)
	private JmsTemplate jmsQueueTemplate;

	public void receive() throws JMSException {
		while (true) {
			TextMessage receive = (TextMessage) jmsQueueTemplate.receive();
			System.out.println("receive message: " + receive.getText());
		}
	}

	@Override
	public void onMessage(Message message) {
		TextMessage receive = (TextMessage) message;
		try {
			System.out.println("receive message: " + receive.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
