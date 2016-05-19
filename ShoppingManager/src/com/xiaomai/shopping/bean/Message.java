package com.xiaomai.shopping.bean;

import cn.bmob.v3.BmobObject;

public class Message extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Integer STATE_WEIDU = 0;
	public static final Integer STATE_YIDU = 1;


	private String uid;
	private String fid;
	private String type;
	private String content;
	private String time;
	private Integer state;

	public Message(String uid, String type, String content, String time) {
		super();
		this.uid = uid;
		this.type = type;
		this.content = content;
		this.time = time;
		this.state = 0;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	@Override
	public String toString() {
		return "Message [type=" + type + ", content=" + content + ", time="
				+ time + "]";
	}

}
