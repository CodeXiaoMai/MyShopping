package com.xiaomai.shopping.module.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseFragment;
import com.xiaomai.shopping.module.ImagePagerActivity;

public class ImageFragment extends BaseFragment {

	private Context context;
	private ImageView imageview;
	private ImageLoader imageloader;
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
		imageloader = ImageLoader.getInstance();
		imageloader.init(ImageLoaderConfiguration.createDefault(getContext()));
		imageview = (ImageView) view.findViewById(R.id.imageview);
		if (images != null) {
			String uri = images.get(position);
			imageloader.displayImage(uri, imageview);
			imageview.setOnClickListener(this);
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
