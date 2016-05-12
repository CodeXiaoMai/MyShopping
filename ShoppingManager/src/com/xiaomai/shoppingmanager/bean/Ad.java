package com.xiaomai.shoppingmanager.bean;

import cn.bmob.v3.BmobObject;

public class Ad extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String image_url;
	private String action;
	private String introduce;

	public Ad(String image_url, String action, String introduce) {
		super();
		this.image_url = image_url;
		this.action = action;
		this.introduce = introduce;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String intro) {
		this.introduce = intro;
	}

	@Override
	public String toString() {
		return "Ad [image_uri=" + image_url + ", action=" + action
				+ ", introduce=" + introduce + "]";
	}

}
