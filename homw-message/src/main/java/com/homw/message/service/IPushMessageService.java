package com.homw.message.service;

import java.util.Map;

import com.homw.message.constant.PushMessageContentType;

/**
 * @description APP推送服务接口
 * @author Hom
 * @version 1.0
 * @since 2020-04-09
 */
public interface IPushMessageService {
	/**
	 * 推送通知
	 * @param token 标识某个注册用户，目标
	 * @param alert
	 * @param extra
	 */
	void pushNotification(String token, String alert, Map<String, String> extra);

	void pushNotification(String[] token, String alert, Map<String, String> extra);

	void sendNotificationWithTag(String tag, String alert, Map<String, String> extra);

	void sendNotificationWithTag(String[] tags, String alert, Map<String, String> extra);

	/**
	 * 推送消息
	 * @param token 标识某个注册用户，目标
	 * @param content
	 * @param extra
	 * @param messageReferType 消息类型，前后端约定
	 */
	void pushMessage(String token, String content, Map<String, String> extra, PushMessageContentType messageReferType);

	void pushMessageWithTag(String tag, String content, Map<String, String> extra,
			PushMessageContentType messageReferType);

	/**
	 * 将某个用户（token）加入到标签组（tag）
	 * @param token
	 * @param tag
	 */
	void addTag(String token, String tag[]);

	void removeTag(String token, String tag[]);

	void replaceTag(String token, String oldTag[], String newTag[]);

	void registerTag(String tag, String[] token);
}