package com.homw.test.activemq.service;

import java.io.Serializable;

/**
 * @description 消息推送服务
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public interface PushService 
{
	/**
	 * 推送消息
	 * @param message
	 */
	void push(Serializable message);
}
