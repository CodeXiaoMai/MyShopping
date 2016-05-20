package com.xiaomai.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.push.autobahn.Utf8Validator;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.UpdateListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.bean.Message;
import com.xiaomai.shopping.bean.Order;
import com.xiaomai.shopping.module.ShangPinXiangQingActivity;
import com.xiaomai.shopping.utils.GetDate;
import com.xiaomai.shopping.utils.Utils;

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
			holder.tv_status = (TextView) view.findViewById(R.id.tv_status);
			holder.bt_shanchu_dingdan = (Button) view
					.findViewById(R.id.dingdan_bt_shanchu_dingdan);
			holder.bt_pingjia = (Button) view
					.findViewById(R.id.dingdan_bt_pingjia);
			holder.bt_shouhuo = (Button) view
					.findViewById(R.id.dingdan_bt_shouhuo);
			holder.bt_zhifu = (Button) view
					.findViewById(R.id.dingdan_bt_fukuan);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final Order order = list.get(position);
		holder.tv_name.setText(order.getGoodsName() + "\t\tx"
				+ order.getCount());
		holder.tv_price.setText(order.getMoney() + "");
		String status = order.getStatus();
		String zt = "";
		if (!TextUtils.isEmpty(status)) {
			switch (status) {
			case Order.ORDER_STATUS_CLOSE:
				zt = "交易关闭";
				break;
			case Order.ORDER_STATUS_PINGJIA:
			case Order.ORDER_STATUS_SHOUHUO:
				zt = "交易成功";
				break;
			case Order.ORDER_STATUS_ZHIFUCHENGGONG:
				zt = "已支付";
				break;
			case Order.ORDER_STATUS_WEIZHIFU:
				zt = "未支付";
				break;
			default:
				break;
			}
		}
		holder.tv_status.setText(zt);
		if (status.equals(Order.ORDER_STATUS_WEIZHIFU)) {
			holder.bt_pingjia.setVisibility(View.GONE);
			holder.bt_shouhuo.setVisibility(View.GONE);
		} else if (status.equals(Order.ORDER_STATUS_ZHIFUCHENGGONG)) {
			holder.bt_pingjia.setVisibility(View.GONE);
			holder.bt_zhifu.setVisibility(View.GONE);
			holder.bt_shanchu_dingdan.setVisibility(View.GONE);
		} else if (status.equals(Order.ORDER_STATUS_SHOUHUO)) {
			holder.bt_shouhuo.setVisibility(View.GONE);
			holder.bt_zhifu.setVisibility(View.GONE);
		} else if (status.equals(Order.ORDER_STATUS_PINGJIA)
				|| status.equals(Order.ORDER_STATUS_CLOSE)) {
			holder.bt_pingjia.setVisibility(View.GONE);
			holder.bt_shouhuo.setVisibility(View.GONE);
			holder.bt_zhifu.setVisibility(View.GONE);
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

		holder.bt_zhifu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message message = new Message(order.getShangjiaId(), "订单消息",
						"有人想买你的【" + order.getGoodsName() + "】,请到商家订单查看",
						GetDate.currentTime().replace(" ", "\n"));
				holder.bt_zhifu.setVisibility(View.GONE);
				message.setFid(order.getUid());
				message.save(context);
			}
		});

		holder.bt_shanchu_dingdan
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BmobQuery<Goods> bmobQuery = new BmobQuery<Goods>();
						bmobQuery.getObject(context, order.getGoodsid(),
								new GetListener<Goods>() {

									@Override
									public void onSuccess(Goods arg0) {
										// TODO Auto-generated method stub
										arg0.setCount(arg0.getCount()
												+ order.getCount());
										arg0.update(context);
									}

									@Override
									public void onFailure(int arg0, String arg1) {
										// TODO Auto-generated method stub

									}
								});
						order.delete(context, new DeleteListener() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								holder.bt_shanchu_dingdan
										.setVisibility(View.GONE);
								holder.bt_zhifu.setVisibility(View.GONE);
								holder.bt_pingjia.setVisibility(View.GONE);
								holder.tv_status.setText("订单已删除");
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								Toast.makeText(context, "操作失败", 0).show();
							}
						});
					}
				});
		holder.bt_shouhuo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				order.setStatus(Order.ORDER_STATUS_SHOUHUO);
				order.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						holder.bt_shouhuo.setVisibility(View.GONE);
						holder.bt_zhifu.setVisibility(View.GONE);
						holder.bt_pingjia.setVisibility(View.VISIBLE);
						holder.bt_shanchu_dingdan.setVisibility(View.VISIBLE);
						Message message = new Message(Utils.MANAGERID, "提款消息",
								"用户:[" + order.getShangjiaId() + "]申请提款 ￥:"
										+ order.getMoney() + "元", GetDate
										.currentTime().replace(" ", "\n"));
						message.setFid(order.getShangjiaId());
						message.save(context);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(context, "操作失败", 0).show();
					}
				});
			}
		});
		holder.bt_pingjia.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BmobQuery<Goods> bmobQuery = new BmobQuery<Goods>();
				bmobQuery.getObject(context, order.getGoodsid(),
						new GetListener<Goods>() {

							@Override
							public void onSuccess(Goods arg0) {
								Intent intent = new Intent(context,
										ShangPinXiangQingActivity.class);
								intent.putExtra("goods", arg0);
								context.startActivity(intent);
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								if (arg0 == 101) {
									Toast.makeText(context, "宝贝已失效，或已下架", 0)
											.show();
								} else {
									Toast.makeText(context, "网络异常", 0).show();
								}
							}
						});
			}
		});
		return view;
	}

	private class ViewHolder {
		ImageView iv_image;
		TextView tv_name;
		TextView tv_price;
		TextView tv_status;
		Button bt_shanchu_dingdan;
		Button bt_zhifu;
		Button bt_pingjia;
		Button bt_shouhuo;
	}

}
