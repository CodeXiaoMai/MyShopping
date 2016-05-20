package com.xiaomai.shopping.utils;

import com.umeng.socialize.UMShareAPI;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

public class Utils {

	public static UMShareAPI mShareAPI;

	public static final String MANAGERID = "7f08d80a7f";
	// 注册
	public static final int SCORE_REGIST = 50;
	// 登录
	public static final int SCORE_LOGIN = 10;
	// 发布商品
	public static final int SCORE_ADD_GOODS = 10;
	// 发布求购
	public static final int SCORE_ADD_IWANT = 5;
	// 收藏商品
	public static final int SCORE_ADD_COLLECTION = 5;
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
