package com.xiaomai.shopping.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

public class Utils {

	// 注册
	public static final int SCORE_REGIST = 50;
	// 登录
	public static final int SCORE_LOGIN = 10;

	public static final String ENCRYPT_KEY = "20120401";

	/**
	 * 设置ListView或GridView的高度
	 * 
	 * @param adapter
	 * @param listView
	 * @param numColumns
	 *            一行中包含多少列
	 */
	public static void setHeight(BaseAdapter adapter, AbsListView listView,
			int numColumns) {
		int totalHeight = 0;
		for (int i = 0; i < adapter.getCount(); i += numColumns) {
			View item = adapter.getView(i, null, listView);
			item.measure(0, 0);
			totalHeight += item.getMeasuredHeight();
		}
		ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
		layoutParams.height = totalHeight;
		listView.setLayoutParams(layoutParams);
	}

	// 每次请求的数据个数
	public static final int REQUEST_COUNT = 10;
}
