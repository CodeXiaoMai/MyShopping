package com.xiaomai.shopping.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.utils.ResultCode;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

/**
 * 设置页面 日期： 2016年3月16日20:23:02
 * 
 * @author XiaoMai
 *
 */
public class SettingActivity extends BaseActivity {

	// 显示省流量是否开启
	private TextView tv_shengliuliang;
	private boolean isShengLiuLiang;
	// 省流量开关
	private ToggleButton tb_shengliuliang;
	// 个人资料
	private View gerenziliao;
	// 关于我们
	private View aboutus;
	// 反馈中心
	private View fankui;
	// 退出登录
	private Button bt_logOut;

	private View back;
	private TextView title;
	private View share;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
		initData();
	}

	/**
	 * 加载配置信息
	 */
	private void initData() {
		isShengLiuLiang = SharedPrenerencesUtil.getShengLiuLiang(context);
		if (isShengLiuLiang) {
			tb_shengliuliang.setToggleOn();
		} else {
			tb_shengliuliang.setToggleOff();
		}
		tv_shengliuliang.setText(isShengLiuLiang ? "已经开启" : "已经关闭");
	}

	private void initView() {
		context = this;
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("设置");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		bt_logOut = (Button) findViewById(R.id.setting_bt_logout);
		tv_shengliuliang = (TextView) findViewById(R.id.setting_shengliuliang);
		tb_shengliuliang = (ToggleButton) findViewById(R.id.setting_tb_shengliuliang);
		tb_shengliuliang.setOnToggleChanged(new OnToggleChanged() {

			@Override
			public void onToggle(boolean on) {
				SharedPrenerencesUtil.setShengLiuLiang(context, on);
				tv_shengliuliang.setText(on ? "已经开启" : "已经关闭");
			}
		});

		gerenziliao = findViewById(R.id.setting_gerenziliao);
		aboutus = findViewById(R.id.setting_about_us);
		fankui = findViewById(R.id.setting_fankui);
		setOnClick(back, bt_logOut, gerenziliao, aboutus, fankui);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.setting_bt_logout:
			BmobUser.logOut(context);
			setResult(ResultCode.RESULT_CODE_SETTING_LOGOUT);
			finish();
			break;
		case R.id.setting_gerenziliao:
			if (getCurrentUser() != null) {
				Intent intent = new Intent(context, GeRenZiLiaoActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.setting_about_us:
			Intent intent = new Intent(context, AboutUsActivity.class);
			startActivity(intent);
			break;
		case R.id.setting_fankui:
			if (getCurrentUser() != null) {
				startActivity(new Intent(context, FanKuiActivity.class));
			}
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
