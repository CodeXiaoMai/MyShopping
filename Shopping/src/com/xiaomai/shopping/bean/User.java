package com.xiaomai.shopping.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 头像地址
	private String imageUri;
	// 等级
	private String level;
	// 积分
	private String score;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String imageUri, String level, String score) {
		super();
		this.imageUri = imageUri;
		this.level = level;
		this.score = score;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

}
