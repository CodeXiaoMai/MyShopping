package com.xiaomai.shoppingmanager.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Goods extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String title;
	private String content;
	private String price;
	private Integer count;
	private Integer remainCount;
	private String address;
	private String phone;
	private String qq;
	private String state;
	private Integer want;
	private Integer shoucang;
	private List<String> images;
//	private List<String> comments;

	public Goods() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Goods(String userId, String title, String content, String price,
			Integer count, Integer remainCount, String address, String phone,
			String qq, String state, Integer want, Integer shoucang,
			List<String> images) {
		super();
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.price = price;
		this.count = count;
		this.remainCount = remainCount;
		this.address = address;
		this.phone = phone;
		this.qq = qq;
		this.state = state;
		this.want = want;
		this.shoucang = shoucang;
		this.images = images;
//		this.comments = comments;
	}

//	public List<String> getComments() {
//		return comments;
//	}
//
//	public void setComments(List<String> comments) {
//		this.comments = comments;
//	}

	public Integer getShoucang() {
		return shoucang;
	}

	public void setShoucang(Integer shoucang) {
		this.shoucang = shoucang;
	}

	public Integer getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(Integer remainCount) {
		this.remainCount = remainCount;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Integer getWant() {
		return want;
	}

	public void setWant(Integer want) {
		this.want = want;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

}
