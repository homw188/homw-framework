package com.homw.transport.netty.message;

import java.io.Serializable;

import com.homw.transport.netty.session.ResultFuture;

import cn.hutool.core.util.IdUtil;
import io.netty.util.AttributeKey;

/**
 * @description 消息
 * @author Hom
 * @version 1.0
 * @since 2020-05-17
 */
public class Message implements Serializable {
	
	private static final long serialVersionUID = -4729343578743693411L;
	
	protected String messageId;
	protected byte messageType;
	protected String sessionId;
	protected Object payload;
	
	public String getMessageId() {
		return this.messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public short getMessageType() {
		return this.messageType;
	}
	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}
	
	public Object getPayload() {
		return payload;
	}
	public void setPayload(Object data) {
		this.payload = data;
	}
	
	public String getSessionId() {
		return this.sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	@Override
	public String toString() {
		return "[Message] messageId=" + messageId + ", messageType=" + messageType + ", sessionId=" + sessionId
				+ ", payload=" + payload;
	}
	
	public static String genMessageId() {
		return IdUtil.fastSimpleUUID();
	}
	
	public static AttributeKey<ResultFuture<Message>> getMessageKey(String messageId) {
		return AttributeKey.valueOf(messageId);
	}
	
}
