package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.adapter.GoodsAdapter;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.utils.StateCode;
import com.xiaomai.shopping.utils.Utils;

public class FenLeiActivity extends BaseActivity {

	private PullToRefreshGridView listView;
	private GoodsAdapter adapter;
	private List<Goods> list;
	private TextView tv_time;
	private TextView tv_price;

	private static final String TIME1 = "createdAt";
	private static final String TIME2 = "-createdAt";
	private static final String PRICE1 = "price";
	private static final String PRICE2 = "-price";

	private String order = TIME2;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fenlei);
		initView();
		type = getIntent().getStringExtra("type");
		title.setText(type);
		checkNetWorkState();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_price = (TextView) findViewById(R.id.tv_price);

		list = new ArrayList<Goods>();
		adapter = new GoodsAdapter(list, context);
		listView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				list = new ArrayList<Goods>();
				loadData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				loadData();
			}
		});
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
			list = new ArrayList<Goods>();
			order = (order.equals(TIME1)) ? TIME2 : TIME1;
			loadData();
			break;
		case R.id.tv_price:
			list = new ArrayList<Goods>();
			order = (order.equals(PRICE1)) ? PRICE2 : PRICE1;
			loadData();
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		showDialog("数据加载中...");
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.order(order);
		query.addWhereEqualTo("type", type);
		query.addWhereEqualTo("state", StateCode.GOODS_OK);
		query.setLimit(Utils.REQUEST_COUNT);
		query.setSkip(list.size());
		query.findObjects(context, new FindListener<Goods>() {

			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				hideDialog();
				if (list.size() == 0) {
					adapter.setImageLoader(imageloader, loader);
					if (arg0.size() == 0) {
						showToast("没有任何数据！");
						listView.onRefreshComplete();
						return;
					} else {
						list = arg0;
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
						listView.onRefreshComplete();
						return;
					} else {
						list.addAll(arg0);
					}
				}
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				hideDialog();
				showErrorToast(arg0, arg1);
				showLog("shouye_goods", arg0, arg1);
			}
		});
	}

}
