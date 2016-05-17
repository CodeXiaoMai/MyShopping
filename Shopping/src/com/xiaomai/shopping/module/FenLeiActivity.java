package com.xiaomai.shopping.module;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.view.RefreshListView;
import com.xiaomai.shopping.view.RefreshListView.OnRefreshListener;

public class FenLeiActivity extends BaseActivity implements OnRefreshListener {

	private RefreshListView listView;
	private TextView tv_time;
	private TextView tv_price;

	private static final String TIME = "createdAt";
	private static final String PRICE = "price";

	private String orderBy = TIME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fenlei);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("分类");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_price = (TextView) findViewById(R.id.tv_price);
		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setOnRefreshListener(this);
		setOnClick(back, tv_time, tv_price);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.tv_time:
			break;
		case R.id.tv_price:

			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pullDownRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pullUpLoadMore() {
		// TODO Auto-generated method stub

	}

}
