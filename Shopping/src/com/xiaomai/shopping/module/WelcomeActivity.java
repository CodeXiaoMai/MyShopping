package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.fragment.WelcomeFragment1;
import com.xiaomai.shopping.fragment.WelcomeFragment2;
import com.xiaomai.shopping.fragment.WelcomeFragment3;
import com.xiaomai.shopping.fragment.WelcomeFragment4;

/**
 * 这是欢迎引导页面的Activity
 * 
 * @author XiaoMai
 *
 */
@SuppressLint("NewApi")
public class WelcomeActivity extends FragmentActivity {

	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		initView();

	}

	/**
	 * 关联控件
	 */
	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.activity_welcome_view_pager);
		viewPager
				.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
	}

	/**
	 * ViewPager的适配器
	 * 
	 * @author XiaoMai
	 *
	 */
	private class MyFragmentAdapter extends FragmentPagerAdapter {

		private List<Fragment> list_fragment = new ArrayList<>();

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			list_fragment.add(new WelcomeFragment1());
			list_fragment.add(new WelcomeFragment2());
			list_fragment.add(new WelcomeFragment3());
			list_fragment.add(new WelcomeFragment4());
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list_fragment.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_fragment.size();
		}

	}
}
