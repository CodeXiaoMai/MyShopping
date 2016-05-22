package com.xiaomai.manager;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.manager.adapter.GoodsAdapter;
import com.xiaomai.manager.adapter.GoodsAdapter.onGoodsUpdateListener;
import com.xiaomai.manager.base.BaseActivity;
import com.xiaomai.manager.bean.Goods;
import com.xiaomai.manager.utils.StateCode;
import com.xiaomai.manager.utils.Utils;
import com.xiaomai.manager.view.RefreshListView;
import com.xiaomai.manager.view.RefreshListView.OnRefreshListener;

public class GoodsManage extends BaseActivity implements OnRefreshListener,
		onGoodsUpdateListener {

	private TextView tv_all;
	private TextView tv_weishenhe;
	private RefreshListView listView;
	private GoodsAdapter adapter;
	private List<Goods> list;
	private List<Goods> list_temp;
	private String state = StateCode.GOODS_SHENHE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_manage);
		initView();
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("商品管理");
		setOnClick(back);

		tv_all = (TextView) findViewById(R.id.all);
		tv_weishenhe = (TextView) findViewById(R.id.weishenhe);

		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setOnRefreshListener(this);
		list = new ArrayList<Goods>();
		adapter = new GoodsAdapter(list, context);
		adapter.setOnGoodsUpdateListener(this);
		listView.setAdapter(adapter);

		setOnClick(tv_all, tv_weishenhe);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.all:
			state = StateCode.GOODS_OK;
			list = new ArrayList<Goods>();
			adapter.setList(list);
			adapter.notifyDataSetChanged();
			loadData();
			break;
		case R.id.weishenhe:
			state = StateCode.GOODS_SHENHE;
			list = new ArrayList<Goods>();
			adapter.setList(list);
			adapter.notifyDataSetChanged();
			loadData();
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		BmobQuery<Goods> bmobQuery = new BmobQuery<Goods>();
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.setSkip(list.size());
		bmobQuery.addWhereEqualTo("state", state);
		bmobQuery.findObjects(context, new FindListener<Goods>() {

			@Override
			public void onSuccess(List<Goods> arg0) {
				if (list.size() == 0) {
					if (arg0.size() == 0) {
						showToast("没有数据！");
						return;
					} else {
						list = arg0;
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
						return;
					}
					list.addAll(arg0);
				}
				adapter.setList(list);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void pullDownRefresh() {
		// TODO Auto-generated method stub
		list_temp = list;
		adapter.setList(list_temp);
		list = new ArrayList<Goods>();
		loadData();
	}

	@Override
	public void pullUpLoadMore() {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public void onGoodsUpdate(int position) {
		// TODO Auto-generated method stub
		list.remove(position);
		adapter.setList(list);
		adapter.notifyDataSetChanged();
	}

}
