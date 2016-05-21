package com.xiaomai.manager.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xiaomai.manager.R;
import com.xiaomai.manager.bean.BlackNumber;
import com.xiaomai.manager.bean.User;

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
		final ViewHolder holder;
		View view = convertView;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.item_user, null);
			holder.tv_id = (TextView) view.findViewById(R.id.tv_id);
			holder.tv_userName = (TextView) view.findViewById(R.id.tv_userName);
			holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			holder.bt_dongjie = (Button) view.findViewById(R.id.bt_delete);
			holder.bt_vip = (Button) view.findViewById(R.id.bt_vip);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final User user = list.get(position);
		holder.tv_id.setText("用户id\n" + user.getObjectId());
		holder.tv_userName.setText("最后登录\n" + user.getLastTimeLogin());
		holder.tv_phone.setText("手机号\n" + user.getMobilePhoneNumber());
		if (user.isDongJie()) {
			holder.bt_dongjie.setVisibility(View.GONE);
			holder.bt_vip.setVisibility(View.VISIBLE);
		} else {
			holder.bt_dongjie.setVisibility(View.VISIBLE);
			holder.bt_vip.setVisibility(View.GONE);
		}
		holder.bt_dongjie.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BlackNumber blackNumber = new BlackNumber(user.getObjectId());
				blackNumber.save(context, new SaveListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						holder.bt_dongjie.setVisibility(View.GONE);
						holder.bt_vip.setVisibility(View.VISIBLE);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(context, arg1, 0).show();
						Toast.makeText(context, "操作失败", 0).show();
					}
				});
			}
		});
		holder.bt_vip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				BlackNumber blackNumber = new BlackNumber(user.getObjectId());
				blackNumber.delete(context, new DeleteListener() {

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						holder.bt_dongjie.setVisibility(View.VISIBLE);
						holder.bt_vip.setVisibility(View.GONE);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(context, arg1, 0).show();
						Toast.makeText(context, "操作失败", 0).show();
					}
				});
			}
		});
		return view;
	}

	private class ViewHolder {
		TextView tv_id;
		TextView tv_userName;
		TextView tv_phone;
		Button bt_dongjie;
		Button bt_vip;
	}
}
