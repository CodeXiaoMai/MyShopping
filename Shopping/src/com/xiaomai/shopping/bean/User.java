package com.xiaomai.shopping.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 等级
	private String level;
	// 积分
	private String score;

	//
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
