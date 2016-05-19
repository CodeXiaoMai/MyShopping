package com.xiaomai.shopping.biz;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MessageBiz extends ContentProvider {

	private SQLiteDatabase database;

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		database = new DBHelper(getContext(), DBUtil.DATABASE_NAME, null, 1)
				.getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return database.query(DBUtil.TABLE_NAME_MESSAGE, projection, selection,
				selectionArgs, null, null, null);
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		database.insert(DBUtil.TABLE_NAME_MESSAGE, null, values);
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return database.delete(DBUtil.TABLE_NAME_MESSAGE, selection,
				selectionArgs);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return database.update(DBUtil.TABLE_NAME_MESSAGE, values, selection,
				selectionArgs);
	}

}
