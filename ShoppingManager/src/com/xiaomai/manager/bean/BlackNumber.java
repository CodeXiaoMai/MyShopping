package com.xiaomai.manager.bean;

import cn.bmob.v3.BmobObject;

public class BlackNumber extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userid;

	public BlackNumber() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BlackNumber(String userid) {
		super();
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
