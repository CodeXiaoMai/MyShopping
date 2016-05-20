package com.xiaomai.manager.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaomai.manager.R;
import com.xiaomai.manager.bean.Suggestion;

public class SuggestionAdapter extends BaseAdapter {

	private List<Suggestion> list;
	private Context context;

	public void setList(List<Suggestion> list) {
		this.list = list;
	}

	public SuggestionAdapter(List<Suggestion> list, Context context) {
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
			view = View.inflate(context, R.layout.item_suggestion, null);
			holder.tv_uid = (TextView) view.findViewById(R.id.tv_uid);
			holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
			holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		Suggestion suggestion = list.get(position);
//		holder.tv_uid.setText(suggestion.getUid());
		holder.tv_phone.setText(suggestion.getPhone());
		holder.tv_desc.setText(suggestion.getContent());
		holder.tv_time.setText(suggestion.getUpdatedAt());
		return view;
	}

	private class ViewHolder {
		TextView tv_uid;
		TextView tv_phone;
		TextView tv_desc;
		TextView tv_time;
	}
}
