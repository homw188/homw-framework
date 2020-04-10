package com.homw.message.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.homw.message.bean.PushNotification;
import com.homw.message.bean.PushMessage;
import com.homw.message.constant.PushTargetType;
import com.homw.message.constant.PushMessageContentType;
import com.homw.message.push.IDeviceProxy;
import com.homw.message.push.INotificationProxy;
import com.homw.message.service.IPushMessageService;

/**
 * @description 消息推送服务
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
@Service
public class PushMessageServiceImpl implements IPushMessageService {

	@Autowired
	private IDeviceProxy deviceProxy;
	@Autowired
	private INotificationProxy notificationProxy;

	@Override
	public void pushNotification(String token, String alert, Map<String, String> extra) {
		if (StringUtils.isEmpty(token)) {
			return;
		}
		notificationProxy.pushNotification(
				createPushNotification(new String[] { token }, PushTargetType.PUSH_TOKEN, alert, extra));
	}

	@Override
	public void pushNotification(String[] deviceTokens, String alert, Map<String, String> extra) {
		notificationProxy
				.pushNotification(createPushNotification(deviceTokens, PushTargetType.PUSH_TOKEN, alert, extra));
	}

	@Override
	public void sendNotificationWithTag(String tag, String alert, Map<String, String> extra) {
		if (StringUtils.isEmpty(tag)) {
			return;
		}
		notificationProxy
				.pushNotification(createPushNotification(new String[] { tag }, PushTargetType.TAG, alert, extra));
	}

	@Override
	public void sendNotificationWithTag(String[] tags, String alert, Map<String, String> extra) {
		notificationProxy.pushNotification(createPushNotification(tags, PushTargetType.TAG, alert, extra));
	}

	@Override
	public void pushMessage(String token, String content, Map<String, String> extra,
			PushMessageContentType conentType) {
		if (StringUtils.isEmpty(token)) {
			return;
		}
		notificationProxy.pushMessage(createPushMessage(new String[] { token }, conentType.getReferType(),
				PushTargetType.PUSH_TOKEN, content, extra));
	}

	@Override
	public void pushMessageWithTag(String tag, String content, Map<String, String> extra,
			PushMessageContentType conentType) {
		if (StringUtils.isEmpty(tag)) {
			return;
		}
		notificationProxy.pushMessage(
				createPushMessage(new String[] { tag }, conentType.getReferType(), PushTargetType.TAG, content, extra));
	}

	@Override
	public void addTag(String token, String[] tag) {
		if (StringUtils.isEmpty(token)) {
			return;
		}
		deviceProxy.addTag(token, tag);
	}

	@Override
	public void removeTag(String token, String[] tag) {
		if (StringUtils.isEmpty(token)) {
			return;
		}
		deviceProxy.removeTag(token, tag);
	}

	public void replaceTag(String token, String[] oldTag, String[] newTag) {
		if (StringUtils.isEmpty(token)) {
			return;
		}
		deviceProxy.replaceTag(token, oldTag, newTag);
	}

	@Override
	public void registerTag(String tag, String[] token) {
		if (StringUtils.isEmpty(tag)) {
			return;
		}
		deviceProxy.registerPushToken(tag, token);
	}

	private PushNotification createPushNotification(String[] targets, PushTargetType targetType, String alert,
			Map<String, String> extra) {
		PushNotification notificationMessage = new PushNotification();
		notificationMessage.setAlert(alert);
		notificationMessage.setExtra(extra);
		notificationMessage.setTarget(targets);
		notificationMessage.setTargetType(targetType.name());
		return notificationMessage;
	}

	private PushMessage createPushMessage(String[] targets, String contentType, PushTargetType targetType,
			String content, Map<String, String> extra) {
		PushMessage pushMessage = new PushMessage();
		pushMessage.setContentType(contentType);
		pushMessage.setExtra(extra);
		pushMessage.setTarget(targets);
		pushMessage.setTargetType(targetType.name());
		pushMessage.setMsgContent(content);
		return pushMessage;
	}
}