package com.xiaomai.shopping.biz;

import com.xiaomai.shopping.bean.Message;

public class DBUtil {

	public static Message message;
	// 数据库
	public static final String DATABASE_NAME = "shopping.db";
	// message表
	public static final String TABLE_NAME_MESSAGE = "message";
	public static final String MESSAGE_OBJ_ID = "_objid";
	public static final String MESSAGE_COLUMN_MESSAGE_UID = "_uid";
	public static final String MESSAGE_COLUMN_TYPE = "_type";
	public static final String MESSAGE_COLUMN_MESSAGE_CONTENT = "_content";
	public static final String MESSAGE_COLUMN_TIME = "_time";
	public static final String MESSAGE_COLUMN_STATE = "_state";

}
