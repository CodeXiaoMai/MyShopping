package com.xiaomai.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.bean.Order;

public class OrderAdapter extends BaseAdapter {

	private Context context;
	private List<Order> list;
	private ImageLoader imageLoader;
	private ImageLoader loader;

	public OrderAdapter(Context context, List<Order> list,
			ImageLoader imageLoader, ImageLoader loader) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		this.imageLoader = imageLoader;
		this.loader = loader;
	}

	public void setList(List<Order> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null) {
			return list.size();
		}
		return 0;
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
		View view = convertView;
		final ViewHolder holder;
		if (view == null) {
			view = View.inflate(context, R.layout.item_dingdan, null);
			holder = new ViewHolder();
			holder.iv_image = (ImageView) view.findViewById(R.id.iv_image);
			holder.tv_name = (TextView) view
					.findViewById(R.id.dingdan_tv_title);
			holder.tv_price = (TextView) view
					.findViewById(R.id.dingdan_tv_price);
			holder.tv_date = (TextView) view.findViewById(R.id.dingdan_tv_date);
			holder.tv_status = (TextView) view.findViewById(R.id.tv_status);
			holder.bt_shanchu_dingdan = (Button) view
					.findViewById(R.id.dingdan_bt_shanchu_dingdan);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Order order = list.get(position);
		holder.tv_date.setText(order.getCreatedAt());
		holder.tv_name.setText(order.getGoodsName() + "\t\tx" + order.getCount());
		holder.tv_price.setText(order.getMoney() + "");
		String status = order.getStatus();
		if (!TextUtils.isEmpty(status)) {
			holder.tv_status.setText("支付状态:"
					+ (status.equals(Order.ORDER_STATUS_WEIZHIFU) ? "未支付"
							: "已支付"));
		}
		final String imageUri = order.getImageUri();
		if (imageLoader != null) {
			imageLoader.displayImage(imageUri, holder.iv_image);
		} else {
			holder.iv_image.setImageResource(R.drawable.chang_an);
			holder.iv_image
					.setOnLongClickListener(new View.OnLongClickListener() {

						@Override
						public boolean onLongClick(View v) {
							// TODO Auto-generated method stub
							holder.iv_image
									.setImageResource(R.drawable.tupian_jiazaizhong);
							if (imageUri != null)
								if (loader != null)
									loader.displayImage(imageUri,
											holder.iv_image);
							return true;
						}
					});
		}
		holder.bt_shanchu_dingdan
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});
		return view;
	}

	private class ViewHolder {
		TextView tv_date;
		ImageView iv_image;
		TextView tv_name;
		TextView tv_price;
		TextView tv_status;
		Button bt_shanchu_dingdan;

	}

}
