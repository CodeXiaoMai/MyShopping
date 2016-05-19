package com.xiaomai.shopping.module;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.listener.OnLoginOutListener;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;
import com.zcw.togglebutton.ToggleButton;
import com.zcw.togglebutton.ToggleButton.OnToggleChanged;

/**
 * 设置页面 日期： 2016年3月16日20:23:02
 * 
 * @author XiaoMai
 *
 */
public class SettingActivity extends BaseActivity implements UMShareListener,
		UMAuthListener {

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
	// 分享
	private View fenxiang;
	// 退出登录
	private Button bt_logOut;

	private View back;
	private TextView title;
	private View share;
	private Context context;

	public static OnLoginOutListener listener;

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
		fenxiang = findViewById(R.id.setting_share);
		setOnClick(back, bt_logOut, gerenziliao, aboutus, fankui, fenxiang);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.setting_bt_logout:
			BmobUser.logOut(context);
			listener.onLogOut();
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
		case R.id.setting_share:
			share();
			break;

		}
	}

	private void share() {
		// TODO Auto-generated method stub
		mShareAPI = UMShareAPI.get(context);
		if (mShareAPI.isAuthorize(this, SHARE_MEDIA.SINA)) {
			toShare();
		} else {
			authSina();
		}
	}

	private void toShare() {
//		UMImage image = new UMImage(
//				context,
//				"http://www.bmob.cn/uploads/attached/app/logo/20160514/fb68c095-24c9-984f-e9a3-8abc861873cc.png");
		new ShareAction(this)
				.setPlatform(SHARE_MEDIA.SINA)
				.setCallback(this)
				.withText("我在北苑跳蚤市场发现了新大陆，大家快来下载吧,http://xiaomai1993.bmob.cn/")
				.share();
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}

	private void authSina() {
		// TODO Auto-generated method stub
		mShareAPI.doOauthVerify(this, SHARE_MEDIA.SINA, this);
	}

	@Override
	public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		showToast("授权失败");
	}

	@Override
	public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> arg2) {
		// TODO Auto-generated method stub
		showToast("授权成功");
		toShare();
	}

	@Override
	public void onCancel(SHARE_MEDIA arg0, int arg1) {
		// TODO Auto-generated method stub
		showToast("取消授权");
	}

	@Override
	public void onCancel(SHARE_MEDIA arg0) {
		// TODO Auto-generated method stub
		showToast("取消分享");
	}

	@Override
	public void onError(SHARE_MEDIA arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		showToast("分享失败");
	}

	@Override
	public void onResult(SHARE_MEDIA arg0) {
		// TODO Auto-generated method stub
		showToast("分享成功");
	}
}
