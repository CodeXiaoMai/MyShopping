package com.xiaomai.shoppingmanager.bean;

public class Message {

	private String type;
	private String content;
	private String time;

	public Message(String type, String content, String time) {
		super();
		this.type = type;
		this.content = content;
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
