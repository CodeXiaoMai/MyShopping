package com.xiaomai.shoppingmanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomai.shoppingmanager.R;
import com.xiaomai.shoppingmanager.bean.Goods;
import com.xiaomai.shoppingmanager.bean.IWant;

public class WantAdapter extends BaseAdapter {

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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		View view = convertView;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.item_goods, null);
			holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
			holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
			// holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		IWant iwant = list.get(position);
		holder.tv_title.setText(iwant.getTitle());
		holder.tv_desc.setText(iwant.getDesc());
		// holder.tv_phone.setText(goods.getState());
		return view;
	}

	private class ViewHolder {
		TextView tv_title;
		TextView tv_desc;
		TextView tv_phone;
	}
}
