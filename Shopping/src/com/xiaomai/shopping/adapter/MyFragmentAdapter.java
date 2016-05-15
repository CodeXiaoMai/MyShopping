package com.xiaomai.shopping.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xiaomai.shopping.fragment.ImageFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MyFragmentAdapter extends FragmentPagerAdapter {

	private List<ImageFragment> list_fragment;

	public MyFragmentAdapter(FragmentManager fm, List<String> images,
			Context context) {
		super(fm);
		list_fragment = new ArrayList<ImageFragment>();
		for (int i = 0; i < images.size(); i++) {
			ImageFragment fragment = new ImageFragment();
			fragment.init(context, i, images);
			list_fragment.add(fragment);
		}
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

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// super.destroyItem(container, position, object);
	}

}
