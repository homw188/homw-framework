package com.homw.message.service;

/**
 * @description rabbitmq队列消息服务接口
 * @author Hom
 * @version 1.0
 * @date 2020-01-13
 */
public interface IRabbitMessageService {
	/**
	 * 发送消息
	 * @param routingKey 路由键
	 * @param message
	 */
	void sendMessage(String routingKey, Object message);
}