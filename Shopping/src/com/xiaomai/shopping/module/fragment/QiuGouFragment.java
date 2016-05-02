package com.xiaomai.shopping.module.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseFragment;

/**
 * 求购页面
 * 
 * @author XiaoMai
 *
 */
public class QiuGouFragment extends BaseFragment {
	
	private View back;
	private TextView title;
	private View share;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_qiugou, container, false);
		initView(view);
		return view;
	}
	private void initView(View view) {
		//隐藏返回
		back = view.findViewById(R.id.title_back);
		back.setVisibility(View.GONE);
		//设置标题
		title = (TextView)view.findViewById(R.id.title_title);
		title.setText("求购列表");
		//隐藏分享
		share = view.findViewById(R.id.title_share);
		share.setVisibility(View.GONE);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
