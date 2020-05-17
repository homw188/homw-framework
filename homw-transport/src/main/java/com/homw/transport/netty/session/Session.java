package com.homw.transport.netty.session;

import com.homw.common.util.CodecUtil;
import com.homw.transport.netty.ResultFuture;
import com.homw.transport.netty.message.Message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class Session {
	private String sessionId;
	private Channel channel;
	
	private static String sessionIdSalt = "session_priv_salt";
	public static AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session_key");

	public Session(String sessionId, Channel channel) {
		this.sessionId = sessionId;
		this.channel = channel;
	}

	public ResultFuture<Message> send(Object data) {
		Message message = new Message();
		message.setPayload(data);
		message.setMessageId(Message.genMessageId());
		return send(message);
	}

	public ResultFuture<Message> send(Message message) {
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
		return CodecUtil.getMD5(ctx.channel().id().asLongText() + sessionIdSalt);
	}

}
