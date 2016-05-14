package com.xiaomai.shopping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;

import com.xiaomai.shopping.module.HomeActivity;
import com.xiaomai.shopping.module.welcome.WelcomeActivity;
import com.xiaomai.shopping.utils.Config;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;

public class SplashActivity extends Activity {

	private Handler handler = new Handler();
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		context = this;
		// 初始化BmobSdk
		Bmob.initialize(this, Config.APPLICATION_ID);
		// 使用推送服务时的初始化操作
		BmobInstallation.getCurrentInstallation(context).save();
		// 启动推送服务
		BmobPush.startWork(context);
		// 检查程序是否是第一次运行

		boolean isLogOut = SharedPrenerencesUtil.getIsLogOut(context);
		if (!isLogOut) {
			BmobUser.logOut(context);
		}

		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				boolean isFirstTimeRun = SharedPrenerencesUtil
						.getIsFirstTimeRun(context);
				if (isFirstTimeRun) {
					// 2秒后进入引导页面
					startActivity(new Intent(SplashActivity.this,
							WelcomeActivity.class));
				} else {
					// 2秒后进入主页面
					startActivity(new Intent(SplashActivity.this,
							HomeActivity.class));
				}
				finish();
			}
		}, 2000);

	}

}
