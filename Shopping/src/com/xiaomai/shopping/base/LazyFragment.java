package com.xiaomai.shopping.base;

import cn.bmob.v3.BmobUser;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.module.LoginActivity;
import com.xiaomai.shopping.utils.NetWorkUtil;
import com.xiaomai.shopping.utils.RequestCode;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;
import com.xiaomai.shopping.view.MyDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public abstract class LazyFragment extends Fragment implements OnClickListener {

	// 是否可见
	protected boolean isVisible;

	// 加载全部图片
	public ImageLoader imageloader;
	// 长按加载图片
	public ImageLoader loader;
	public Context context;
	public ProgressDialog dialog;

	public void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(context);
				dialog.setCancelable(true);
			}
			dialog.setMessage(message);
			dialog.show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
		}
	}

	public void hideDialog() {
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
			}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		// ui可见
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			// Ui不可见
			isVisible = false;
			onInvisible();
		}
	}

	private void onInvisible() {
		// TODO Auto-generated method stub

	}

	private void onVisible() {
		// TODO Auto-generated method stub
		lazyLoad();
	}

	protected abstract void lazyLoad();

	public void setOnClick(View... view) {
		for (View v : view) {
			v.setOnClickListener(this);
		}
	}

	public void showErrorToast(int arg0, String arg1) {
		Toast.makeText(context, "错误代码：" + arg0 + ",错误信息：" + arg1,
				Toast.LENGTH_SHORT).show();
	}

	public void showLog(String tag, int arg0, String arg1) {
		Log.i(tag, "errorCode:" + arg0 + ",msg:" + arg1);

	}

	public void showToast(String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	public void checkNetWorkState() {
		boolean shengLiuLiang = SharedPrenerencesUtil.getShengLiuLiang(context);
		if (NetWorkUtil.isNetwork(context)
				&& NetWorkUtil.isMobileNetWork(context) && shengLiuLiang) {
			if (loader == null) {
				loader = ImageLoader.getInstance();
				loader.init(ImageLoaderConfiguration.createDefault(context));
			}
		} else {
			if (imageloader == null) {
				imageloader = ImageLoader.getInstance();
				imageloader.init(ImageLoaderConfiguration
						.createDefault(context));
			}
		}
		loadData();
	}

	public abstract void loadData();

	public User getCurrentUser() {
		User user = BmobUser.getCurrentUser(context, User.class);
		if (user == null) {
			MyDialog.showDialog(context, "提示", "您还没有登录，现在登录",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(context,
									LoginActivity.class);
							startActivityForResult(intent,
									RequestCode.REQUEST_CODE_LOGIN);
							dialog.dismiss();
						}
					}, null);
		}
		return user;
	}
}
