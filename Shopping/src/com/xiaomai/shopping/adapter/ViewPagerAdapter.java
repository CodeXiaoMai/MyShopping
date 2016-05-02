package com.xiaomai.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.bean.Ad;

public class ViewPagerAdapter extends PagerAdapter {

	private List<Ad> list;
	private Context context;
	private ImageLoader imageLoader;

	private ImageLoader loader;

	public ViewPagerAdapter(Context context, List<Ad> list,
			ImageLoader imageLoader) {
		loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(context));
		this.context = context;
		this.list = list;
		this.imageLoader = imageLoader;
	}

	/**
	 * 返回多少page
	 */
	@Override
	public int getCount() {
		return list.size();
	}

	/**
	 * 判断当前滑动view等不等进来的对象
	 * 
	 * true: 表示不去创建，使用缓存 false:去重新创建 view： 当前滑动的view
	 * object：将要进入的新创建的view，由instantiateItem方法创建
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	/**
	 * 类似于BaseAdapger的getView方法 用了将数据设置给view 由于它最多就3个界面，不需要viewHolder
	 */

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = View.inflate(context, R.layout.adapter_ad, null);
		final ImageView imageView = (ImageView) view.findViewById(R.id.image);

		final Ad ad = list.get(position % list.size());
		if (imageLoader != null) {
			imageLoader.displayImage(ad.getImage_url(), imageView);
		} else {
			imageView.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					loader.displayImage(ad.getImage_url(), imageView);
					return true;
				}
			});
		}
		container.addView(view);// 一定不能少，将view加入到viewPager中
		return view;
	}

	/**
	 * 销毁page position： 当前需要消耗第几个page object:当前需要消耗的page
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		container.removeView((View) object);
	}

}
