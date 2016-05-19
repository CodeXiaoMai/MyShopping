package com.xiaomai.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.bean.Message;

public class MessageAdapter extends BaseAdapter {

	private List<Message> list;
	private Context context;

	public MessageAdapter(List<Message> list, Context context) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;
	}

	public void setList(List<Message> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null) {
			return list.size();
		}
		Toast.makeText(context, "list：null", 0).show();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = View.inflate(context, R.layout.item_message, null);
			holder = new ViewHolder();
			holder.iv_head = (ImageView) view.findViewById(R.id.iv_head);
			holder.tv_message_type = (TextView) view
					.findViewById(R.id.tv_message_type);
			holder.tv_message_content = (TextView) view
					.findViewById(R.id.tv_message_content);
			holder.tv_message_time = (TextView) view
					.findViewById(R.id.tv_message_time);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Message message = list.get(list.size()-position-1);
		// holder.iv_head
		holder.tv_message_type.setText(message.getType());
		holder.tv_message_content.setText(message.getContent());
		holder.tv_message_time.setText(message.getTime());
		return view;
	}

	private class ViewHolder {
		ImageView iv_head;
		TextView tv_message_type;
		TextView tv_message_content;
		TextView tv_message_time;
	}
}
