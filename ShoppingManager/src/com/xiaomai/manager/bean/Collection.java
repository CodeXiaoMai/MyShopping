package com.xiaomai.manager.bean;

import cn.bmob.v3.BmobObject;

public class Collection extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	private String goodsId;
	private String goodsName;
	private Double goodsPrice;
	private String imageUri;

	public Collection() {
		super();
	}

	public Collection(String userId, String goodsId, String goodsName,
			Double goodsPrice, String imageUri) {
		super();
		this.userId = userId;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
		this.imageUri = imageUri;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Double getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

}
