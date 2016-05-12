package com.xiaomai.shoppingmanager;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.shoppingmanager.adapter.UserAdapter;
import com.xiaomai.shoppingmanager.base.BaseActivity;
import com.xiaomai.shoppingmanager.bean.User;
import com.xiaomai.shoppingmanager.utils.Utils;
import com.xiaomai.shoppingmanager.view.RefreshListView;
import com.xiaomai.shoppingmanager.view.RefreshListView.OnRefreshListener;

public class UserManage extends BaseActivity implements OnRefreshListener {

	private RefreshListView listView;
	private UserAdapter adapter;
	private List<User> list;
	private List<User> list_temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_manage);
		initView();
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setOnRefreshListener(this);
		list = new ArrayList<User>();
		adapter = new UserAdapter(list, context);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		BmobQuery<User> bmobQuery = new BmobQuery<User>();
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.setSkip(list.size());
		bmobQuery.findObjects(context, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (list.size() == 0) {
					if (arg0.size() == 0) {
						showToast("没有用户！");
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
		list_temp = list;
		adapter.setList(list_temp);
		list = new ArrayList<User>();
		loadData();
	}

	@Override
	public void pullUpLoadMore() {
		// TODO Auto-generated method stub
		loadData();
	}

}
