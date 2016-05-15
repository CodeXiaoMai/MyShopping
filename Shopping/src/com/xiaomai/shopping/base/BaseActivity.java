package com.xiaomai.shopping.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.UMShareAPI;
import com.xiaomai.shopping.bean.Score;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.module.LoginActivity;
import com.xiaomai.shopping.utils.NetWorkUtil;
import com.xiaomai.shopping.utils.RequestCode;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;
import com.xiaomai.shopping.view.MyDialog;

public abstract class BaseActivity extends Activity implements OnClickListener {

	public UMShareAPI mShareAPI;
	public View back;
	public TextView title;
	public View share;

	// 加载全部图片
	public ImageLoader imageloader;
	// 长按加载图片
	public ImageLoader loader;

	public Context context = this;

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
		switch (arg0) {
		case 101:
			arg1 = "宝贝已失效，或已下架";
			break;
		case 10010:
			arg1 = "您的操作太频繁,请稍后再试!";
			break;
		default:
			break;
		}
		Toast.makeText(this, arg1, Toast.LENGTH_SHORT).show();
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

	/**
	 * 添加积分
	 * 
	 * @param user
	 * @param jifen
	 * @param desc
	 */
	public void addScore(User user, Integer jifen, String desc) {
		Score score = new Score(user.getObjectId(), jifen, desc);
		score.save(context);
		user.setScore(user.getScore() + jifen);
		user.update(context);
	}
}
