package com.xiaomai.shoppingmanager.bean;

import cn.bmob.v3.BmobObject;

public class IWant extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 待审核
	public static final int STATE_DAISHENHE = 0;
	// 审核失败
	public static final int STATE_SHENHE_SHIBAI = -100;
	// 正常
	public static final int STATE_NORMAL = 1;
	// 取消
	public static final int STATE_CANCEL = -1;

	private String userId;
	private String userName;
	private String userImage;
	private String title;
	private String desc;
	private String minPrice;
	private String maxPrice;
	private String phone;
	private String qq;
	private Integer state;

	public IWant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IWant(String userId, String userName, String userImage,
			String title, String desc, String minPrice, String maxPrice,
			String phone, String qq) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userImage = userImage;
		this.title = title;
		this.desc = desc;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.phone = phone;
		this.qq = qq;
		this.state = STATE_DAISHENHE;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
