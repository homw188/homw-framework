package com.homw.test.activemq.service.impl;

import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.homw.test.activemq.service.PushService;

/**
 * @description 推送用户消息
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
@Service("pushUserService")
public class PushUserServiceImpl implements PushService {
	@Autowired(required = false)
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTmplate;

	private ExecutorService excutor = Executors.newCachedThreadPool();

	@Override
	public void push(final Serializable message) {
		excutor.execute(new Runnable() {
			public void run() {
				jmsTmplate.send(new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(message);
					}
				});
			}
		});
	}
}
