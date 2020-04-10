package com.homw.message.constant;

/**
 * @description 推送消息内容类型，需与前端约定
 * @author Hom
 * @version 1.0
 * @since 2020-04-09
 */
public enum PushMessageContentType {
	START_NOW, END_NOW;

	public String getReferType() {
		return this.name().toLowerCase().replaceAll("_", ":");
	}

	public static PushMessageContentType value(String referType) {
		String referTypeStr = referType.toUpperCase().replaceAll(":", "_");
		return PushMessageContentType.valueOf(referTypeStr);
	}
}