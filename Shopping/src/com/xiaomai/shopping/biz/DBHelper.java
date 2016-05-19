package com.xiaomai.shopping.biz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLE_MESSAGE_CMD = "create table "
				+ DBUtil.TABLE_NAME_MESSAGE
				+ "(_objId varchar not null, "
				+ " _uid varchar not null, "
				+ " _type varchar not null, "
				+ " _content varchar not null ,"
				+ " _time varchar not null,"
				+ " _state integer not null);";
		db.execSQL(CREATE_TABLE_MESSAGE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
