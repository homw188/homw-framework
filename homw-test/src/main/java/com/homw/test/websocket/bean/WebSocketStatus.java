package com.homw.test.websocket.bean;

/**
 * @description websocket状态
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class WebSocketStatus 
{
	private Long userId;
	private String userName;
	private Long bizId;// 业务id
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	
	@Override
	public String toString() {
		return "WebSocketStatus [userId=" + userId + ", userName=" + userName + ", lotId=" + bizId + "]";
	}
}