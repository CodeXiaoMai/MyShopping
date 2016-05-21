package com.xiaomai.manager;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

import com.xiaomai.manager.adapter.UserAdapter;
import com.xiaomai.manager.base.BaseActivity;
import com.xiaomai.manager.bean.User;
import com.xiaomai.manager.utils.DES;
import com.xiaomai.manager.utils.GetDate;
import com.xiaomai.manager.utils.Utils;
import com.xiaomai.manager.view.RefreshListView;
import com.xiaomai.manager.view.RefreshListView.OnRefreshListener;

public class UserManage extends BaseActivity implements OnRefreshListener {

	private RefreshListView listView;
	private UserAdapter adapter;
	private List<User> list;
	private List<User> list_temp;

	private TextView tv_user_count;
	private TextView tv_today_login;
	private TextView tv_today_regist;

	private int today_login;
	private int today_regist;

	private String search;
	private EditText et_search;
	private Button bt_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_manage);
		initView();
		loadData();
		getUsers();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("用户管理");
		setOnClick(back);

		tv_user_count = (TextView) findViewById(R.id.tv_user_count);
		tv_today_login = (TextView) findViewById(R.id.tv_today_login);
		tv_today_regist = (TextView) findViewById(R.id.tv_today_regist);

		et_search = (EditText) findViewById(R.id.et_search);
		bt_search = (Button) findViewById(R.id.bt_search);

		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setOnRefreshListener(this);
		list = new ArrayList<User>();
		adapter = new UserAdapter(list, context);
		listView.setAdapter(adapter);

		setOnClick(bt_search);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.bt_search:
			loadData();
			break;
		}
	}

	private void getUsers() {
		today_login = 0;
		today_regist = 0;
		BmobQuery<User> bmobQuery = new BmobQuery<User>();
		bmobQuery.findObjects(context, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0 != null) {
					tv_user_count.setText("用户总数\n" + arg0.size());
					for (User user : arg0) {
						if (user.getCreatedAt().startsWith(
								GetDate.getDateBefore(0))) {
							today_regist++;
						}
						if (user.getLastTimeLogin().startsWith(
								GetDate.getDateBefore(0))) {
							today_login++;
						}
					}
					tv_today_login.setText("今日登录\n" + today_login);
					tv_today_regist.setText("今日注册\n" + today_regist);
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		search = et_search.getText().toString().trim();
		String nicheng = DES.encryptDES(search);
		BmobQuery<User> bmobQuery = new BmobQuery<User>();
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.setSkip(list.size());
		if (!TextUtils.isEmpty(search)) {
			String bql = "select * from _User where objectId = ? or nicheng = ?  or mobilePhoneNumber = ?";
			bmobQuery.doSQLQuery(context, bql, new SQLQueryListener<User>() {

				@Override
				public void done(BmobQueryResult<User> arg0, BmobException arg1) {
					// TODO Auto-generated method stub
					if (arg1 == null) {
						list = arg0.getResults();
						adapter.setList(list);
						adapter.notifyDataSetChanged();
					} else {
						showToast(arg1.getMessage());
					}
				}
			}, search, nicheng, search);
		} else {
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

	}

	@Override
	public void pullDownRefresh() {
		list_temp = list;
		adapter.setList(list_temp);
		list = new ArrayList<User>();
		loadData();
		getUsers();
	}

	@Override
	public void pullUpLoadMore() {
		// TODO Auto-generated method stub
		loadData();
	}

}
