package com.xiaomai.shopping;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.shopping.adapter.WantAdapter;
import com.xiaomai.shopping.adapter.WantAdapter.onWantUpdateListener;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.IWant;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.RefreshListView;
import com.xiaomai.shopping.view.RefreshListView.OnRefreshListener;

public class WantManage extends BaseActivity implements OnRefreshListener,
		onWantUpdateListener {

	private RefreshListView listView;
	private WantAdapter adapter;
	private List<IWant> list;
	private List<IWant> list_temp;

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
		title.setText("求购管理");
		setOnClick(back);
		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setOnRefreshListener(this);
		list = new ArrayList<IWant>();
		adapter = new WantAdapter(list, context);
		adapter.setOnGoodsUpdateListener(this);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		BmobQuery<IWant> bmobQuery = new BmobQuery<IWant>();
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.setSkip(list.size());
		bmobQuery.addWhereEqualTo("state", IWant.STATE_DAISHENHE);
		bmobQuery.findObjects(context, new FindListener<IWant>() {

			@Override
			public void onSuccess(List<IWant> arg0) {
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
		list = new ArrayList<IWant>();
		loadData();
	}

	@Override
	public void pullUpLoadMore() {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public void onWantUpdate(int position) {
		// TODO Auto-generated method stub
		list.remove(position);
		adapter.setList(list);
		adapter.notifyDataSetChanged();
	}

}
