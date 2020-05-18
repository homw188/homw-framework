package com.homw.transport.netty.session;

import org.apache.commons.lang3.StringUtils;

import com.homw.transport.netty.message.Message;
import com.homw.transport.netty.message.MessageType;

import cn.hutool.crypto.SecureUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * @description 会话
 * @author Hom
 * @version 1.0
 * @since 2020-05-17
 */
public class Session {
	private Channel channel;
	private String sessionId;

	private static String sessionIdSalt = "session_priv_salt";
	public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session_attr_key");

	public Session(String sessionId, Channel channel) {
		this.sessionId = sessionId;
		this.channel = channel;
	}

	public ResultFuture<Message> send(Object data) {
		if (data instanceof Message) {
			return send((Message) data);
		}
		Message message = new Message();
		message.setPayload(data);
		message.setMessageType(MessageType.NORMAL);
		return send(message);
	}

	public ResultFuture<Message> send(Message message) {
		if (message == null) {
			return null;
		}
		if (StringUtils.isEmpty(message.getSessionId())) {
			message.setSessionId(sessionId);
		}
		if (StringUtils.isEmpty(message.getMessageId())) {
			message.setMessageId(Message.genMessageId());
		}
		channel.writeAndFlush(message);

		// bind future to channel
		ResultFuture<Message> future = new ResultFuture<>();
		Attribute<ResultFuture<Message>> messageAttr = channel.attr(Message.getMessageKey(message.getMessageId()));
		messageAttr.set(future);
		return future;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public static String genSessionId(ChannelHandlerContext ctx) {
		return SecureUtil.md5(ctx.channel().id().asLongText() + sessionIdSalt);
	}

}
