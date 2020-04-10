package com.homw.message.bean;

import java.util.Map;

import com.homw.message.constant.PushTargetType;

/**
 * @description APP通知消息
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public class PushNotification {
	private String alert;
	private String title;
	private String[] target;// 推送目标ID集合
	/**
	 * @see PushTargetType
	 */
	private String targetType;
	private Map<String, String> extra;

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String[] getTarget() {
		return target;
	}

	public void setTarget(String[] target) {
		this.target = target;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
