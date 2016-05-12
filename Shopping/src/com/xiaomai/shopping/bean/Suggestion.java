package com.xiaomai.shopping.bean;

import cn.bmob.v3.BmobObject;

public class Suggestion extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String uid;
	private String content;
	private String phone;

	public Suggestion(String uid, String content, String phone) {
		super();
		this.uid = uid;
		this.content = content;
		this.phone = phone;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
