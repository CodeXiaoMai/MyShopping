package com.xiaomai.shoppingmanager;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.shoppingmanager.adapter.GoodsAdapter;
import com.xiaomai.shoppingmanager.adapter.GoodsAdapter.onGoodsUpdateListener;
import com.xiaomai.shoppingmanager.base.BaseActivity;
import com.xiaomai.shoppingmanager.bean.Goods;
import com.xiaomai.shoppingmanager.utils.Utils;
import com.xiaomai.shoppingmanager.view.RefreshListView;
import com.xiaomai.shoppingmanager.view.RefreshListView.OnRefreshListener;

public class GoodsManage extends BaseActivity implements OnRefreshListener,
		onGoodsUpdateListener {
	private RefreshListView listView;
	private GoodsAdapter adapter;
	private List<Goods> list;
	private List<Goods> list_temp;

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
		
		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setOnRefreshListener(this);
		list = new ArrayList<Goods>();
		adapter = new GoodsAdapter(list, context);
		adapter.setOnGoodsUpdateListener(this);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		BmobQuery<Goods> bmobQuery = new BmobQuery<Goods>();
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.setSkip(list.size());
		// bmobQuery.addWhereEqualTo("state", StateCode.GOODS_SHENHE);
		bmobQuery.findObjects(context, new FindListener<Goods>() {

			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
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
