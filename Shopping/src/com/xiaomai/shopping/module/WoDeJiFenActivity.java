package com.xiaomai.shopping.module;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;

/**
 * 我的积分
 * 
 * @author XiaoMai
 *
 */
public class WoDeJiFenActivity extends BaseActivity {

	private View back;
	private TextView title;
	private View share;

	private ListView listView;
	private MyAdapter adapter;
	private List<String> list;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wodejifen);
		initView();
	}

	private void initView() {
		context = this;
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("我的积分");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		listView = (ListView) findViewById(R.id.listView);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		setOnClick(back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		default:
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
			View view = View.inflate(context, R.layout.item_jifen, null);
			View top = view.findViewById(R.id.top);
			View bottom = view.findViewById(R.id.bottom);
			TextView tv_jifen = (TextView) view.findViewById(R.id.tv_jifen);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			TextView tv_leixing = (TextView) view.findViewById(R.id.tv_leixing);

			if (position == 0) {
				top.setVisibility(View.INVISIBLE);
			} else if (position == 9) {
				bottom.setVisibility(View.INVISIBLE);
			}
			return view;
		}

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}
}
