package com.xiaomai.shopping.bean;

import cn.bmob.v3.BmobObject;

public class Score extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private Integer score;
	private String desc;

	public Score(String userId, Integer score, String desc) {
		super();
		this.userId = userId;
		this.score = score;
		this.desc = desc;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
