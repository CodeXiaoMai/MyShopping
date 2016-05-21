package com.xiaomai.manager.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 昵称
	private String nicheng;
	// 真实姓名
	private String realName;
	// 头像地址
	private String imageUri;
	// 性别
	private String sex;
	// 班级
	private String grade;
	// 学号
	private String num;
	// 等级
	private Integer level;
	// 积分
	private Integer score;
	// 昵称修改过
	private Boolean isNiChengChanged;
	// 最后一次登录的时间
	private String lastTimeLogin;

	private boolean isDongJie;

	public boolean isDongJie() {
		return isDongJie;
	}

	public void setDongJie(boolean isDongJie) {
		this.isDongJie = isDongJie;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLastTimeLogin() {
		return lastTimeLogin;
	}

	public void setLastTimeLogin(String lastTimeLogin) {
		this.lastTimeLogin = lastTimeLogin;
	}

	public String getNicheng() {
		return nicheng;
	}

	public void setNicheng(String nicheng) {
		this.nicheng = nicheng;
	}

	public Boolean getIsNiChengChanged() {
		return isNiChengChanged;
	}

	public void setIsNiChengChanged(Boolean isNiChengChanged) {
		this.isNiChengChanged = isNiChengChanged;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}
