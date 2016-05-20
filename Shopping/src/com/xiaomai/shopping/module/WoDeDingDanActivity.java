package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.adapter.OrderAdapter;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Order;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.RefreshListView;
import com.xiaomai.shopping.view.RefreshListView.OnRefreshListener;

/**
 * 我的订单页面
 * 
 * @author XiaoMai
 *
 */
public class WoDeDingDanActivity extends BaseActivity implements
		OnRefreshListener {
	private View back;
	private TextView title;
	private View share;

	private Context context;

	// 我的订单
	private RefreshListView lv_dingdan;
	private OrderAdapter adapter;
	private List<Order> list_order = new ArrayList<>();
	private List<Order> list_temp;
	private BmobQuery<Order> bmobQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wodedingdan);
		initView();
		checkNetWorkState();
	}

	private void initView() {
		context = this;
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("我的订单");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		lv_dingdan = (RefreshListView) findViewById(R.id.listView);
		lv_dingdan.setOnRefreshListener(this);
		bmobQuery = new BmobQuery<Order>();
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

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		showDialog("数据加载中");
		User user = getCurrentUser();
		if (user != null) {
			bmobQuery.setLimit(Utils.REQUEST_COUNT);
			bmobQuery.setSkip(list_order.size());
			bmobQuery.addWhereEqualTo("uid", user.getObjectId());
			bmobQuery.findObjects(context, new FindListener<Order>() {

				@Override
				public void onSuccess(List<Order> arg0) {
					lv_dingdan.onRefreshFinish();
					hideDialog();
					// TODO Auto-generated method stub
					if (list_order.size() == 0) {
						adapter = new OrderAdapter(context, list_order,
								imageloader, loader);
						lv_dingdan.setAdapter(adapter);
						if (arg0.size() > 0) {
							list_order = arg0;
						} else {
							showToast("你还没有任何详单数据!");
							return;
						}
					} else {
						if (arg0.size() == 0) {
							showToast("没有更多数据");
							return;
						}
						list_order.addAll(arg0);
					}
					adapter.setList(list_order);
					adapter.notifyDataSetChanged();
				}

				@Override
				public void onError(int arg0, String arg1) {
					lv_dingdan.onRefreshFinish();
					hideDialog();
					showErrorToast(arg0, arg1);
					showLog("订单", arg0, arg1);
				}
			});
		}
	}

	@Override
	public void pullDownRefresh() {
		// TODO Auto-generated method stub
		list_temp = list_order;
		if (list_temp != null) {
			adapter.setList(list_temp);
		}
		list_order = new ArrayList<Order>();
		loadData();
	}

	@Override
	public void pullUpLoadMore() {
		// TODO Auto-generated method stub
		loadData();
	}

}
