package com.xiaomai.shopping.bean;

import cn.bmob.v3.BmobObject;

public class Order extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ORDER_STATUS_WEIZHIFU = "NOTPAY";
	public static final String ORDER_STATUS_ZHIFUCHENGGONG = "SUCCESS";
	// 订单号
	private String orderId;
	// 用户id
	private String uid;
	// 商家id
	private String shangjiaId;
	// 商品id
	private String goodsid;
	// 商品名称
	private String goodsName;
	// 商品图片
	private String imageUri;
	// 数量
	private Integer count;
	// 金额
	private Double money;
	// 状态
	private String status;

	public Order(String orderId, String uid, String shangjiaId, String goodsid,
			String goodsName, String imageUri, Integer count, Double money,
			String status) {
		super();
		this.orderId = orderId;
		this.uid = uid;
		this.shangjiaId = shangjiaId;
		this.goodsid = goodsid;
		this.goodsName = goodsName;
		this.imageUri = imageUri;
		this.count = count;
		this.money = money;
		this.status = status;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getShangjiaId() {
		return shangjiaId;
	}

	public void setShangjiaId(String shangjiaId) {
		this.shangjiaId = shangjiaId;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
