package com.homw.test.activemq.start;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @description 消息队列消费端
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class QueueConsumer {
	public static void main(String[] args) throws JMSException, IOException {
		Connection connection = null;
		try {
			// 1.初始化connection工厂
			ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, ActiveMQConnection.DEFAULT_BROKER_URL);

			// 2.创建Connection
			connection = factory.createConnection();

			// 3.打开连接
			connection.start();

			// 4.创建session
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 5.创建消息目标
			Destination queue = session.createQueue("hello.queue");

			// 6.创建消费者
			MessageConsumer consumer = session.createConsumer(queue);

			// 7.配置监听
			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					if (message instanceof TextMessage) {
						TextMessage txtMsg = (TextMessage) message;
						try {
							System.out.println("Queue consumer receive message: " + txtMsg.getText());
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			System.in.read();

			/*while (true) {
				TextMessage message = (TextMessage) consumer.receive(10000);
				if (message != null) {
					System.out.println("receive message: " + message.getText());
				} else {
					break;
				}
			}*/
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
}
