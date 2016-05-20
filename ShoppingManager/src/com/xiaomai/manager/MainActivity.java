package com.xiaomai.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

import com.xiaomai.manager.base.BaseActivity;
import com.xiaomai.manager.utils.Config;

public class MainActivity extends BaseActivity {

	// 用户管理
	private View user_manage;
	// 商品管理
	private View goods_manage;
	// 求购管理
	private View want_manage;
	// 意见箱
	private View suggestions;
	// 广告管理
	private View ad_manage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化BmobSdk
		Bmob.initialize(this, Config.APPLICATION_ID);
		// 使用推送服务时的初始化操作
		BmobInstallation.getCurrentInstallation(this).save();
		// 启动推送服务
		BmobPush.startWork(this);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		user_manage = findViewById(R.id.bt_user_manage);
		goods_manage = findViewById(R.id.bt_goods_manage);
		want_manage = findViewById(R.id.bt_want_manage);
		suggestions = findViewById(R.id.bt_suggestions_manage);
		ad_manage = findViewById(R.id.bt_ad_manage);
		setOnClick(user_manage, goods_manage, want_manage, suggestions,
				ad_manage);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_user_manage:
			startActivity(new Intent(context, UserManage.class));
			break;
		case R.id.bt_goods_manage:
			startActivity(new Intent(context, GoodsManage.class));
			break;
		case R.id.bt_want_manage:
			startActivity(new Intent(context, WantManage.class));
			break;
		case R.id.bt_suggestions_manage:
			startActivity(new Intent(context, SuggestionManage.class));
			break;
		case R.id.bt_ad_manage:
			startActivity(new Intent(context, AdManage.class));
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
