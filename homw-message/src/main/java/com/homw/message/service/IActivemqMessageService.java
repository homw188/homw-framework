package com.homw.message.service;

/**
 * @description activemq队列消息服务接口
 * @author Hom
 * @version 1.0
 * @since 2020-04-16
 */
public interface IActivemqMessageService {

	/**
	 * 发送消息
	 * @param message
	 */
	void sendMessage(Object message);
}
