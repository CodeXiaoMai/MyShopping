package com.xiaomai.shopping.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.module.HomeActivity;

public class WelcomeFragment4 extends Fragment {

	private ImageView imageView_goto_app;
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_welcome4, container,
				false);
		initView(view);
		return view;
	}

	/**
	 * 初始化控件
	 * 
	 * @param view
	 */
	private void initView(View view) {
		imageView_goto_app = (ImageView) view
				.findViewById(R.id.fragment_welcome4_goto_app);
		imageView_goto_app.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getContext(), HomeActivity.class));
				sp = getContext().getSharedPreferences("config",
						Context.MODE_PRIVATE);
				Editor edit = sp.edit();
				edit.putBoolean("isFirstTimeRun", false);
				edit.commit();
				WelcomeFragment4.this.getActivity().finish();
			}
		});
	}
}
