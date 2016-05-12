package com.xiaomai.shoppingmanager.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomai.shoppingmanager.R;
import com.xiaomai.shoppingmanager.bean.User;

public class UserAdapter extends BaseAdapter {

	private List<User> list;
	private Context context;

	public void setList(List<User> list) {
		this.list = list;
	}

	public UserAdapter(List<User> list, Context context) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		View view = convertView;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.item_user, null);
			holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
			holder.tv_userName = (TextView) view.findViewById(R.id.tv_userName);
			holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		User user = list.get(position);
		holder.tv_id.setText(user.getObjectId());
		holder.tv_userName.setText(user.getUsername());
		holder.tv_phone.setText(user.getMobilePhoneNumber());
		return view;
	}

	private class ViewHolder {
		TextView tv_id;
		TextView tv_userName;
		TextView tv_phone;
	}
}
