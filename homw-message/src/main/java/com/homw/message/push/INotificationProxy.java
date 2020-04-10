package com.homw.message.push;

import com.homw.message.bean.PushNotification;
import com.homw.message.bean.PushMessage;

/**
 * @description 消息推送接口
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public interface INotificationProxy {

	/**
	 * 推送通知
	 * @param pushNotification
	 */
	void pushNotification(PushNotification pushNotification);

	/**
	 * 推送消息
	 * @param pushMessage
	 */
	void pushMessage(PushMessage pushMessage);
}
