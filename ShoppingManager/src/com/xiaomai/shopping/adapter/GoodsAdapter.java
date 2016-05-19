package com.xiaomai.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.bean.Message;
import com.xiaomai.shopping.bean.MyBmobInstallation;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.GetDate;
import com.xiaomai.shopping.utils.StateCode;

public class GoodsAdapter extends BaseAdapter {

	public interface onGoodsUpdateListener {
		public void onGoodsUpdate(int position);
	}

	private onGoodsUpdateListener listener;

	public void setOnGoodsUpdateListener(onGoodsUpdateListener listener) {
		this.listener = listener;
	}

	private List<Goods> list;
	private Context context;

	private ImageLoader imageLoader;

	public void setList(List<Goods> list) {
		this.list = list;
	}

	public GoodsAdapter(List<Goods> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null) {
			return list.size();
		}
		Toast.makeText(context, "list:null", 0).show();
		return 10;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		View view = convertView;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.item_goods, null);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
			holder.tv_uid = (TextView) view.findViewById(R.id.tv_uid);
			holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.iv1 = (ImageView) view.findViewById(R.id.iv1);
			holder.iv2 = (ImageView) view.findViewById(R.id.iv2);
			holder.iv3 = (ImageView) view.findViewById(R.id.iv3);
			holder.iv4 = (ImageView) view.findViewById(R.id.iv4);
			holder.iv5 = (ImageView) view.findViewById(R.id.iv5);
			holder.bt_tongguo = (Button) view.findViewById(R.id.bt_tongguo);
			holder.bt_shibai = (Button) view.findViewById(R.id.bt_delete);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final Goods goods = list.get(position);
		holder.tv_title.setText("标题:\t" + goods.getTitle());
		holder.tv_desc.setText("描述:\n\t" + goods.getContent());
		holder.tv_uid.setText("用户id:" + goods.getUserId());
		holder.tv_phone.setText("Tel:" + goods.getPhone());
		holder.tv_time.setText("Time:" + goods.getUpdatedAt());
		List<String> images = goods.getImages();
		switch (images.size()) {
		case 5:
			holder.iv5.setVisibility(View.VISIBLE);
			imageLoader.displayImage(images.get(4), holder.iv5);
		case 4:
			holder.iv4.setVisibility(View.VISIBLE);
			imageLoader.displayImage(images.get(3), holder.iv4);
		case 3:
			holder.iv3.setVisibility(View.VISIBLE);
			imageLoader.displayImage(images.get(2), holder.iv3);
		case 2:
			holder.iv2.setVisibility(View.VISIBLE);
			imageLoader.displayImage(images.get(1), holder.iv2);
		case 1:
			imageLoader.displayImage(images.get(0), holder.iv1);
			break;
		}
		holder.bt_tongguo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goods.setState(StateCode.GOODS_OK);
				goods.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						if (listener != null) {
							listener.onGoodsUpdate(position);
							String type = "系统消息";
							String content = "恭喜,您发布的商品\"" + goods.getTitle()
									+ "\"通过审核";
							String time = GetDate.currentTime().replace(" ",
									"\n");
							final Message message = new Message(goods
									.getUserId(), type, content, time);
							message.save(context);
						}
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(context,
								"操作失败,errcode:" + arg0 + "message:" + arg1, 0)
								.show();
					}
				});
			}
		});
		holder.bt_shibai.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String type = "系统消息";
				String content = "很遗憾,您发布的商品\"" + goods.getTitle() + "\"未能通过审核";
				String time = GetDate.currentTime().replace(" ", "\n");
				final Message message = new Message(goods.getUserId(), type,
						content, time);

				goods.setState(StateCode.GOODS_SHENHE_SHIBAI);
				goods.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						if (listener != null) {
							listener.onGoodsUpdate(position);
							message.save(context);
						}
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(context,
								"操作失败,errcode:" + arg0 + "message:" + arg1, 0)
								.show();
					}
				});

			}
		});
		return view;
	}

	private class ViewHolder {
		TextView tv_title;
		TextView tv_desc;
		TextView tv_uid;
		TextView tv_phone;
		TextView tv_time;
		ImageView iv1, iv2, iv3, iv4, iv5;
		Button bt_tongguo;
		Button bt_shibai;
	}
}
