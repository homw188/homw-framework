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
		return send(wrapMessage(data));
	}

	public ResultFuture<Message> send(Message message) {
		if (message == null) {
			return null;
		}
		fillMissField(message);
		sendOriginal(message);

		// bind future to channel
		return bindResultFuture(message);
	}

	protected ResultFuture<Message> bindResultFuture(Message message) {
		ResultFuture<Message> future = new ResultFuture<>();
		AttributeKey<ResultFuture<Message>> messageKey = Message.getMessageKey(message.getMessageId());
		Attribute<ResultFuture<Message>> messageAttr = channel.attr(messageKey);
		messageAttr.set(future);
		return future;
	}

	public void sendOriginal(Object data) {
		channel.writeAndFlush(data);
	}
	
	public void sendIgnoreResult(Object data) {
		if (data instanceof Message) {
			sendIgnoreResult((Message) data);
		}
		sendIgnoreResult(wrapMessage(data));
	}

	public void sendIgnoreResult(Message message) {
		if (message == null) {
			return;
		}
		fillMissField(message);
		sendOriginal(message);
	}
	
	protected Message wrapMessage(Object data) {
		Message message = new Message();
		message.setPayload(data);
		message.setMessageType(MessageType.NORMAL);
		return message;
	}
	
	protected void fillMissField(Message message) {
		if (StringUtils.isEmpty(message.getSessionId())) {
			message.setSessionId(sessionId);
		}
		
		if (StringUtils.isEmpty(message.getMessageId())) {
			message.setMessageId(Message.genMessageId());
		}
	}
	
	public final String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public final Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public static String genSessionId(ChannelHandlerContext ctx) {
		if (ctx == null) {
			return null;
		}
		return SecureUtil.md5(ctx.channel().id().asLongText() + sessionIdSalt);
	}
	
	public static Session getSession(ChannelHandlerContext ctx) {
		if (ctx == null) {
			return null;
		}
		return ctx.channel().attr(Session.SESSION_KEY).get();
	}

}
