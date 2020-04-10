package com.homw.test.activemq.start;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;

/**
 * @description 消息发布者
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class TopicPublisher {
	public static void main(String[] args) throws JMSException {
		Connection connection = null;
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
			// 设置异步发送消息，可提高发送性能
			factory.setUseAsyncSend(true);
			connection = factory.createConnection();
			// 每个生产者clientID需唯一
			connection.setClientID("publisher-1");
			connection.start();

			Session session = connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
			// 区别于P2P模式
			Destination topic = session.createTopic("hello.topic");

			MessageProducer producer = session.createProducer(topic);
			// 持久化消息
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);

			TextMessage message = session.createTextMessage("Hello, topic.");
			producer.send(topic, message);

			System.out.println("Topic publisher send message:" + message.getText());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
