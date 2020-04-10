package com.homw.message.bean;

import java.util.Map;

import com.homw.message.constant.PushMessageContentType;
import com.homw.message.constant.PushTargetType;

/**
 * @description APP推送消息
 * @author Hom
 * @version 1.0
 * @since 2020-04-03
 */
public class PushMessage {
	private String title;
	/**
	 * @see PushMessageContentType
	 */
	private String contentType;
	private String msgContent;
	/**
	 * @see PushTargetType
	 */
	private String targetType;
	private Map<String, String> extra;
	private String[] target;// 推送目标ID集合

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String[] getTarget() {
		return target;
	}

	public void setTarget(String[] target) {
		this.target = target;
	}

}
