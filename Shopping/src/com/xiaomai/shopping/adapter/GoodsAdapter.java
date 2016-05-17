package com.xiaomai.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.module.ShangPinXiangQingActivity;

public class GoodsAdapter extends BaseAdapter {

	private ImageLoader imageloader;
	private ImageLoader loader;
	private List<Goods> list;
	private Context context;

	public GoodsAdapter(List<Goods> list, Context context) {
		super();

		this.list = list;
		this.context = context;
	}

	public void setImageLoader(ImageLoader imageloader, ImageLoader loader) {
		this.imageloader = imageloader;
		this.loader = loader;
	}

	public void setList(List<Goods> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Goods goods;
		View view = convertView;
		if (view == null) {
			view = View.inflate(context, R.layout.item_shangpin, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.shangpin_title);
			holder.tv_address = (TextView) view
					.findViewById(R.id.shangpin_jiaoyididian);
			holder.tv_price = (TextView) view.findViewById(R.id.shangpin_price);
			holder.tv_want = (TextView) view
					.findViewById(R.id.shangpin_xiangmai);
			holder.iv_image = (ImageView) view
					.findViewById(R.id.shangpin_image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		goods = list.get(position);
		if (imageloader != null) {
			imageloader.displayImage(goods.getImages().get(0), holder.iv_image);
		} else {
			holder.iv_image.setImageResource(R.drawable.chang_an1);
			holder.iv_image
					.setOnLongClickListener(new View.OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							holder.iv_image
									.setImageResource(R.drawable.tupian_jiazaizhong);
							loader.displayImage(goods.getImages().get(0),
									holder.iv_image);
							return true;
						}
					});
			holder.iv_image.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					holder.tv_name.setTextColor(0xffCBCACA);
					Intent intent = new Intent(context,
							ShangPinXiangQingActivity.class);
					intent.putExtra("goods", goods);
					context.startActivity(intent);

				}
			});
		}
		holder.tv_name.setText(goods.getTitle());
		holder.tv_address.setText(goods.getAddress());
		holder.tv_price.setText("￥" + goods.getPrice());
		holder.tv_want.setText(goods.getWant() + "人想要");
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				holder.tv_name.setTextColor(0xffCBCACA);
				Intent intent = new Intent(context,
						ShangPinXiangQingActivity.class);
				intent.putExtra("goods", goods);
				context.startActivity(intent);

			}
		});
		return view;
	}

	private class ViewHolder {
		ImageView iv_image;
		TextView tv_name;
		TextView tv_address;
		TextView tv_price;
		TextView tv_want;
	}
}
