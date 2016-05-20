package com.xiaomai.manager.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.manager.bean.User;
import com.xiaomai.manager.utils.NetWorkUtil;
import com.xiaomai.manager.utils.RequestCode;
import com.xiaomai.manager.utils.SharedPrenerencesUtil;
import com.xiaomai.manager.view.MyDialog;

public abstract class BaseFragment extends Fragment implements OnClickListener {
	// 加载全部图片
	public ImageLoader imageloader;
	// 长按加载图片
	public ImageLoader loader;
	public Context context;

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
//							Intent intent = new Intent(context,
//									LoginActivity.class);
//							startActivityForResult(intent,
//									RequestCode.REQUEST_CODE_LOGIN);
							dialog.dismiss();
						}
					}, null);
		}
		return user;
	}
}
