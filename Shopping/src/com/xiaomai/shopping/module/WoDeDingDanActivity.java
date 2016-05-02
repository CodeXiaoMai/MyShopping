package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.WoDeShouCang;

/**
 * 我的收藏页面
 * 
 * @author XiaoMai
 *
 */
public class WoDeDingDanActivity extends BaseActivity {
	private View back;
	private TextView title;
	private View share;

	private Context context;

	// 我的收藏
	private ListView lv_dingdan;
	private MyAdapter adapter;
	private List<WoDeShouCang> list_dingdan = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wodedingdan);
		initView();
		context = this;
	}

	private void initView() {
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("我的订单");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		lv_dingdan = (ListView) findViewById(R.id.listView);
		adapter = new MyAdapter();
		lv_dingdan.setAdapter(adapter);
		setOnClick(back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		}

	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
			View view = convertView;
			ViewHolder holder;
			if (view == null) {
				view = View.inflate(context, R.layout.item_dingdan, null);
				holder = new ViewHolder();
				holder.iv_image = (ImageView) view.findViewById(R.id.iv_image);
				holder.tv_name = (TextView) view
						.findViewById(R.id.dingdan_tv_title);
				holder.tv_price = (TextView) view
						.findViewById(R.id.dingdan_tv_price);
				holder.tv_date = (TextView)view.findViewById(R.id.dingdan_tv_date);
				holder.bt_pingjia = (Button) view
						.findViewById(R.id.dingdan_bt_pingjia);
				holder.bt_shanchu_dingdan = (Button) view
						.findViewById(R.id.dingdan_bt_shanchu_dingdan);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			return view;
		}

		private class ViewHolder {
			TextView tv_date;
			ImageView iv_image;
			TextView tv_name;
			TextView tv_price;
			Button bt_shanchu_dingdan;
			Button bt_pingjia;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

}
