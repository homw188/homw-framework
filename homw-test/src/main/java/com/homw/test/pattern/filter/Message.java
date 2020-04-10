package com.homw.test.pattern.filter;

public class Message 
{
	private int msgId;
	private String content;
	
	public Message() {
	}
	
	public Message(int msgId, String content) {
		super();
		this.msgId = msgId;
		this.content = content;
	}
	
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Override
	public String toString() 
	{
		return "msgId=" + msgId + ", content=" + content;
	}
	
}
