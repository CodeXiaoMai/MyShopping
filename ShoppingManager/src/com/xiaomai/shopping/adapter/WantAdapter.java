package com.xiaomai.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.google.gson.Gson;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.bean.IWant;
import com.xiaomai.shopping.bean.Message;
import com.xiaomai.shopping.bean.MyBmobInstallation;
import com.xiaomai.shopping.utils.GetDate;

public class WantAdapter extends BaseAdapter {

	public interface onWantUpdateListener {
		public void onWantUpdate(int position);
	}

	private onWantUpdateListener listener;

	public void setOnGoodsUpdateListener(onWantUpdateListener listener) {
		this.listener = listener;
	}

	private List<IWant> list;
	private Context context;

	public void setList(List<IWant> list) {
		this.list = list;
	}

	public WantAdapter(List<IWant> list, Context context) {
		super();
		this.list = list;
		this.context = context;
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
			view = View.inflate(context, R.layout.item_want, null);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
			holder.tv_uid = (TextView) view.findViewById(R.id.tv_uid);
			holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			holder.bt_tongguo = (Button) view.findViewById(R.id.bt_tongguo);
			holder.bt_shibai = (Button) view.findViewById(R.id.bt_delete);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final IWant iwant = list.get(position);
		holder.tv_title.setText("标题:\t" + iwant.getTitle());
		holder.tv_desc.setText("描述:\n\t" + iwant.getDesc());
		holder.tv_uid.setText("用户id:" + iwant.getUserId());
		holder.tv_phone.setText("Tel:" + iwant.getPhone());
		holder.tv_time.setText("Time:" + iwant.getUpdatedAt());
		holder.bt_tongguo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iwant.setState(IWant.STATE_NORMAL);
				iwant.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						if (listener != null) {
							listener.onWantUpdate(position);
							String type = "系统消息";
							String content = "恭喜,您发布的求购\"" + iwant.getTitle() + "\"通过审核";
							String time = GetDate.currentTime().replace(" ", "\n");
							final Message message = new Message(iwant.getUserId(), type,
									content, time);
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
				String content = "很遗憾,您发布的求购\"" + iwant.getTitle() + "\"未能通过审核";
				String time = GetDate.currentTime().replace(" ", "\n");
				final Message message = new Message(iwant.getUserId(), type,
						content, time);

				iwant.setState(IWant.STATE_SHENHE_SHIBAI);
				iwant.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						if (listener != null) {
							listener.onWantUpdate(position);
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
		TextView tv_phone;
		TextView tv_uid;
		TextView tv_time;
		Button bt_tongguo;
		Button bt_shibai;
	}
}
