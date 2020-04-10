package com.homw.test.activemq.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

import com.homw.test.activemq.bean.UserInfo;

/**
 * @description 用户推送消息监听器
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
@Component("userPushListener")
public class UserPushListener implements MessageListener
{
	@Override
	public void onMessage(Message message)
	{
		if (message instanceof TextMessage)
		{
			try {
				System.out.println("receive message: " + ((TextMessage) message).getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else if (message instanceof ObjectMessage)
		{
			try {
				UserInfo user = (UserInfo) ((ObjectMessage) message).getObject();
				System.out.println("receive message: " + user);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
