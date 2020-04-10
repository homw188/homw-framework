package com.homw.test.pattern.filter;

/**
 * @description 执行消息
 * @author Hom
 * @version 1.0
 * @since 2020-03-18
 */
public class MessageContext implements Context<Message> {
	private Message msg;

	public MessageContext(Message msg) {
		this.msg = msg;
	}

	@Override
	public Message get() {
		return msg;
	}

	@Override
	public void set(Message ctx) {
		this.msg = ctx;
	}

}
