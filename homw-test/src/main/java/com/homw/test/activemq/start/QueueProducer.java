package com.homw.test.activemq.start;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @description 消息队列生产端
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class QueueProducer {
	public static void main(String[] args) throws JMSException {
		Connection connection = null;
		try {
			// 1.初始化connection工厂,使用默认的URL
			// failover://tcp://localhost:61616
			ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);

			// 2.创建Connection
			connection = factory.createConnection();

			// 3.打开连接
			connection.start();

			// 4.创建Session，(是否支持事务)
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

			// 5.创建消息目标
			Destination queue = session.createQueue("hello.queue");

			// 6.创建生产者
			MessageProducer producer = session.createProducer(queue);

			// 7.初始化要发送的消息
			TextMessage message = session.createTextMessage("Hello, queue.");

			// 8.发送消息
			producer.send(message);
			System.out.println("Queue producer send message: " + message.getText());

			// 9.提交事务
			session.commit();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
