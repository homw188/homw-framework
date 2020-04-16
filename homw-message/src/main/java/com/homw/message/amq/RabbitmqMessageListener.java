package com.homw.message.amq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

/**
 * @description rabbitmq消息监听器
 * @author Hom
 * @version 1.0
 * @date 2020-01-14
 */
public class RabbitmqMessageListener implements ChannelAwareMessageListener {
	private static final Logger logger = LoggerFactory.getLogger(RabbitmqMessageListener.class);

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		logMessage(message);
		// TODO: 业务处理
		ack(message, channel);
	}

	/**
	 * 消息确认
	 * @param message
	 * @param channel
	 * @throws IOException
	 */
	protected void ack(Message message, Channel channel) throws IOException {
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}

	/**
	 * 记录消息
	 * @param message
	 */
	protected void logMessage(Message message) {
		logger.info("MessageId:     " + message.getMessageProperties().getMessageId());
		logger.info("Body:          " + message.getBody());
		logger.info("Properties:    " + message.getMessageProperties());
	}
}
