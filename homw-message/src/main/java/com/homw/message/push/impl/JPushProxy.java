package com.homw.message.push.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.homw.message.bean.PushNotification;
import com.homw.message.bean.PushMessage;
import com.homw.message.constant.PushTargetType;
import com.homw.message.push.IDeviceProxy;
import com.homw.message.push.INotificationProxy;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * @description 消息推送实现
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
@Component
public class JPushProxy implements INotificationProxy, IDeviceProxy {

	@Autowired(required = false)
	private JPushClient jpushClient;

	@Override
	public void pushNotification(PushNotification notification) {
		PushPayload.Builder payloadBuilder = new PushPayload.Builder();
		payloadBuilder.setPlatform(Platform.all())
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(IosNotification.newBuilder().setAlert(notification.getAlert())
								.addExtras(notification.getExtra()).setSound("default").build())
						.addPlatformNotification(AndroidNotification.newBuilder().setAlert(notification.getAlert())
								.setTitle(notification.getTitle()).addExtras(notification.getExtra()).build())
						.build());

		Options options = Options.sendno();
		options.setApnsProduction(com.homw.common.util.Platform.isProdEnv());
		switch (PushTargetType.valueOf(notification.getTargetType())) {
			case TAG:
				options.setTimeToLive(0L);
				payloadBuilder.setAudience(Audience.tag(notification.getTarget()));
				break;
			case PUSH_TOKEN:
				payloadBuilder.setAudience(Audience.registrationId(notification.getTarget()));
				break;
			default:
				return;
		}

		try {
			payloadBuilder.setOptions(options);
			PushResult pushResult = jpushClient.sendPush(payloadBuilder.build());
			System.out.println(pushResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void pushMessage(PushMessage message) {
		PushPayload.Builder payloadBuilder = new PushPayload.Builder();
		payloadBuilder.setPlatform(Platform.all())
				.setMessage(Message.newBuilder().addExtras(message.getExtra()).setTitle(message.getTitle())
						// .setContentType(message.getContentType())
						.setMsgContent(message.getMsgContent()).build());

		Options options = Options.sendno();
		options.setApnsProduction(com.homw.common.util.Platform.isProdEnv());
		switch (PushTargetType.valueOf(message.getTargetType())) {
			case TAG:
				options.setTimeToLive(0L);
				payloadBuilder.setAudience(Audience.tag(message.getTarget()));
				break;
			case PUSH_TOKEN:
				payloadBuilder.setAudience(Audience.registrationId(message.getTarget()));
				break;
			default:
				return;
		}

		try {
			payloadBuilder.setOptions(options);
			jpushClient.sendPush(payloadBuilder.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addTag(String pushToken, String tag[]) {
		try {
			jpushClient.updateDeviceTagAlias(pushToken, null, Sets.newHashSet(tag), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeTag(String pushToken, String tag[]) {
		try {
			jpushClient.updateDeviceTagAlias(pushToken, null, null, Sets.newHashSet(tag));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void replaceTag(String pushToken, String oldTag[], String newTag[]) {
		try {
			jpushClient.updateDeviceTagAlias(pushToken, null, Sets.newHashSet(newTag), Sets.newHashSet(oldTag));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerPushToken(String tag, String[] pushToken) {
		try {
			jpushClient.addRemoveDevicesFromTag(tag, Sets.newHashSet(pushToken), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}