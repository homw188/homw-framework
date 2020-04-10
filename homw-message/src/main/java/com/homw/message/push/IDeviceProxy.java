package com.homw.message.push;

/**
 * @description 消息推送设备接口
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public interface IDeviceProxy {

	/**
	 * 添加设备token->标签组tag
	 * 
	 * @param pushToken
	 * @param tag
	 */
	void addTag(String pushToken, String[] tag);

	void removeTag(String pushToken, String[] tag);

	void replaceTag(String pushToken, String[] oldTag, String[] newTag);

	void registerPushToken(String tag, String[] pushToken);
}
