package com.homw.test.activemq.start;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

/**
 * @description 消息订阅者
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class TopicSubscriber {
	public static void main(String[] args) throws JMSException, IOException {
		Connection connection = null;
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			factory.setUseAsyncSend(true);
			connection = factory.createConnection();
			connection.setClientID("subscriber-1");
			connection.start();

			Session session = connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
			// 区别于P2P模式
			Topic topic = session.createTopic("hello.topic");

			// MessageConsumer consumer = session.createConsumer(topic);
			javax.jms.TopicSubscriber consumer = session.createDurableSubscriber(topic, "subscriber-1");

			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						try {
							System.out.println("Topic subscriber received message:" 
									+ ((TextMessage) message).getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			System.in.read();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
