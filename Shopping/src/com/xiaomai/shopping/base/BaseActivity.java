package com.xiaomai.shopping.base;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.utils.NetWorkUtil;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class BaseActivity extends Activity implements OnClickListener {

	// 加载全部图片
	public ImageLoader imageloader;
	// 长按加载图片
	public ImageLoader loader;

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

	public void checkNetWorkState() {
		boolean shengLiuLiang = SharedPrenerencesUtil.getShengLiuLiang(this);
		if (NetWorkUtil.isNetwork(this) && NetWorkUtil.isMobileNetWork(this)
				&& shengLiuLiang) {
			loader = ImageLoader.getInstance();
			loader.init(ImageLoaderConfiguration.createDefault(this));
		} else {
			imageloader = ImageLoader.getInstance();
			imageloader.init(ImageLoaderConfiguration.createDefault(this));
		}
		loadData();
	}

	public abstract void loadData();

}
