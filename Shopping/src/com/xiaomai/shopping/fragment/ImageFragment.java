package com.xiaomai.shopping.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseFragment;
import com.xiaomai.shopping.module.ImagePagerActivity;

public class ImageFragment extends BaseFragment {

	private ImageView imageview;
	private int position;
	private List<String> images;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_image, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		checkNetWorkState();
		imageview = (ImageView) view.findViewById(R.id.imageview);
		if (images != null) {
			final String uri = images.get(position);
			if (imageloader != null) {
				imageloader.displayImage(uri, imageview);
				imageview.setOnClickListener(this);
			} else {
				imageview.setImageResource(R.drawable.chang_an);
				imageview
						.setOnLongClickListener(new View.OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								// TODO Auto-generated method stub
								imageview
										.setImageResource(R.drawable.tupian_jiazaizhong);
								loader.displayImage(uri, imageview);
								return true;
							}
						});
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview:
			imageBrower(context, position, (ArrayList<String>) images);
			break;

		default:
			break;
		}
	}

	public void init(Context context, int position, List<String> images) {
		this.context = context;
		this.position = position;
		this.images = images;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(Context context, int position,
			ArrayList<String> urls2) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}
}
