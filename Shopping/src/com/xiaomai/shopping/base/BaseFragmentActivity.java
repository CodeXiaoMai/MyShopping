package com.xiaomai.shopping.base;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class BaseFragmentActivity extends FragmentActivity implements
		OnClickListener {

	// 请求的当前页数
	public int currentPage = 0;
	// 是否在下拉
	public boolean isPullDown;

	/**
	 * 监听事件
	 * 
	 * @param view
	 */
	public void setOnClick(View... view) {
		for (View v : view) {
			v.setOnClickListener(this);
		}
	}

	public void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	public void showErrorToast(int arg0, String arg1) {
		Toast.makeText(this, "错误代码：" + arg0 + ",错误信息：" + arg1,
				Toast.LENGTH_SHORT).show();
	}

	public void showLog(String tag, int arg0, String arg1) {
		Log.i(tag, "errorCode:" + arg0 + ",msg:" + arg1);

	}
}
