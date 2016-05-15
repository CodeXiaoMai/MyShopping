package com.xiaomai.shopping.module;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import c.b.BP;
import cn.bmob.v3.BmobUser;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.fragment.GeRenZhongXinFragment;
import com.xiaomai.shopping.fragment.QiuGouFragment;
import com.xiaomai.shopping.fragment.ShouYeFragment;
import com.xiaomai.shopping.fragment.XiaoXiFragment;
import com.xiaomai.shopping.utils.Config;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;
import com.xiaomai.shopping.utils.Utils;

/**
 * 这是主页
 * 
 * @author XiaoMai
 *
 */
public class HomeActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {

	private Context context;

	private ViewPager viewPager;

	private LinearLayout ll_shouye;
	private LinearLayout ll_xiaoxi;
	private LinearLayout ll_qiugou;
	private LinearLayout ll_gerenzhongxin;

	private ImageView bt_shouye;
	private ImageView bt_xiaoxi;
	private ImageView bt_qiugou;
	private ImageView bt_gerenzhongxin;

	private TextView tv_shouye;
	private TextView tv_gerenzhongxin;
	private TextView tv_xiaoxi;
	private TextView tv_qiugou;

	private int color_red = Color.rgb(205, 60, 57);
	private int color_black = Color.rgb(89, 89, 89);

	private long mExitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initSdk();
	}

	private void initSdk() {
		// 初始化支付服务
		BP.init(context, Config.APPLICATION_ID);
		// 微信 appid appsecret
		PlatformConfig.setWeixin("wx967daebe835fbeac",
				"5bb696d9ccd75a38c8a0bfe0675559b3");
		// 新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("2259885001",
				"60c2dcb8e3c7e04272e36acdf02483c0");
		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone("2259885001",
				"60c2dcb8e3c7e04272e36acdf02483c0");
		

	}

	private void initView() {
		context = this;
		// viewPager
		viewPager = (ViewPager) findViewById(R.id.main_viewPager);
		viewPager
				.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
		// viewPager.setOffscreenPageLimit(2);
		viewPager.addOnPageChangeListener(this);

		// 个人中心
		bt_gerenzhongxin = (ImageView) findViewById(R.id.main_bt_gerenzhongxin);
		tv_gerenzhongxin = (TextView) findViewById(R.id.main_tv_gerenzhongxin);
		ll_gerenzhongxin = (LinearLayout) findViewById(R.id.ll_gerenzhongxin);
		ll_gerenzhongxin.setOnClickListener(this);
		// 首页
		bt_shouye = (ImageView) findViewById(R.id.main_bt_shouye);
		tv_shouye = (TextView) findViewById(R.id.main_tv_shouye);
		ll_shouye = (LinearLayout) findViewById(R.id.ll_shouye);
		ll_shouye.setOnClickListener(this);
		// 消息
		ll_xiaoxi = (LinearLayout) findViewById(R.id.ll_xiaoxi);
		bt_xiaoxi = (ImageView) findViewById(R.id.main_bt_xiaoxi);
		tv_xiaoxi = (TextView) findViewById(R.id.main_tv_xiaoxi);
		ll_xiaoxi.setOnClickListener(this);
		// 求购
		bt_qiugou = (ImageView) findViewById(R.id.main_bt_qiugou);
		tv_qiugou = (TextView) findViewById(R.id.main_tv_qiugou);
		ll_qiugou = (LinearLayout) findViewById(R.id.ll_qiugou);
		ll_qiugou.setOnClickListener(this);

	}

	private class MyFragmentAdapter extends FragmentPagerAdapter {

		private ArrayList<Fragment> list = new ArrayList<>();

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			list.add(new ShouYeFragment());
			list.add(new QiuGouFragment());
			list.add(new XiaoXiFragment());
			list.add(new GeRenZhongXinFragment());
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			Log.i("阻止销毁", "阻止销毁第" + position + "个Fragment");
			// super.destroyItem(container, position, object);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_shouye:
			shouYe();
			viewPager.setCurrentItem(0);
			break;
		case R.id.ll_qiugou:
			qiugou();
			viewPager.setCurrentItem(1);
			break;
		case R.id.ll_xiaoxi:
			xiaoxi();
			viewPager.setCurrentItem(2);
			break;
		case R.id.ll_gerenzhongxin:
			gerenzhongxin();
			viewPager.setCurrentItem(3);
			break;

		default:
			break;
		}
	}

	private void qiugou() {
		bt_shouye.setImageResource(R.drawable.home_normal);
		bt_gerenzhongxin.setImageResource(R.drawable.wode_normal);
		bt_qiugou.setImageResource(R.drawable.main_qiugou_press);
		bt_xiaoxi.setImageResource(R.drawable.xiaoxi_normal);
		tv_qiugou.setTextColor(color_red);
		tv_xiaoxi.setTextColor(color_black);
		tv_shouye.setTextColor(color_black);
		tv_gerenzhongxin.setTextColor(color_black);
	}

	private void xiaoxi() {
		bt_shouye.setImageResource(R.drawable.home_normal);
		bt_gerenzhongxin.setImageResource(R.drawable.wode_normal);
		bt_qiugou.setImageResource(R.drawable.main_qiugou);
		bt_xiaoxi.setImageResource(R.drawable.xiaoxi_press);
		tv_qiugou.setTextColor(color_black);
		tv_xiaoxi.setTextColor(color_red);
		tv_shouye.setTextColor(color_black);
		tv_gerenzhongxin.setTextColor(color_black);
	}

	private void gerenzhongxin() {
		bt_shouye.setImageResource(R.drawable.home_normal);
		bt_gerenzhongxin.setImageResource(R.drawable.wode_select);
		bt_qiugou.setImageResource(R.drawable.main_qiugou);
		bt_xiaoxi.setImageResource(R.drawable.xiaoxi_normal);
		tv_qiugou.setTextColor(color_black);
		tv_xiaoxi.setTextColor(color_black);
		tv_shouye.setTextColor(color_black);
		tv_gerenzhongxin.setTextColor(color_red);
	}

	private void shouYe() {
		bt_shouye.setImageResource(R.drawable.home_press);
		bt_gerenzhongxin.setImageResource(R.drawable.wode_normal);
		bt_xiaoxi.setImageResource(R.drawable.xiaoxi_normal);
		bt_qiugou.setImageResource(R.drawable.main_qiugou);
		tv_shouye.setTextColor(color_red);
		tv_gerenzhongxin.setTextColor(color_black);
		tv_xiaoxi.setTextColor(color_black);
		tv_qiugou.setTextColor(color_black);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			shouYe();
			break;
		case 1:
			qiugou();
			break;
		case 2:
			xiaoxi();
			break;
		case 3:
			gerenzhongxin();
			break;
		case 4:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BmobUser.logOut(context);
		SharedPrenerencesUtil.setIsLogOut(context, true);
	}

	// 监听手机上的BACK键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 判断菜单是否关闭
			// 判断两次点击的时间间隔（默认设置为2秒）
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

				mExitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
				super.onBackPressed();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
