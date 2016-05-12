package com.xiaomai.shoppingmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import cn.bmob.v3.Bmob;

import com.xiaomai.shoppingmanager.base.BaseActivity;
import com.xiaomai.shoppingmanager.utils.Config;

public class MainActivity extends BaseActivity {

	// 用户管理
	private View user_manage;
	// 商品管理
	private View goods_manage;
	// 求购管理
	private View want_manage;
	// 意见箱
	private View suggestions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化BmobSdk
		Bmob.initialize(this, Config.APPLICATION_ID);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		user_manage = findViewById(R.id.bt_user_manage);
		goods_manage = findViewById(R.id.bt_goods_manage);
		want_manage = findViewById(R.id.bt_want_manage);
		suggestions = findViewById(R.id.bt_suggestions_manage);
		setOnClick(user_manage, goods_manage, want_manage, suggestions);
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
		default:
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
