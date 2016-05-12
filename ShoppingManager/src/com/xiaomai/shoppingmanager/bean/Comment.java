package com.xiaomai.shoppingmanager.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String goodsId;
	private String userId;
	private String userName;
	private String content;

	public Comment(String goodsId, String content, String userId,
			String userName) {
		super();
		this.goodsId = goodsId;
		this.content = content;
		this.userId = userId;
		this.userName = userName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
