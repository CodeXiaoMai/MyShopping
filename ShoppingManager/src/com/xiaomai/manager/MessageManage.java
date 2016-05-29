package com.xiaomai.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.manager.adapter.MessageAdapter;
import com.xiaomai.manager.base.BaseActivity;
import com.xiaomai.manager.bean.Message;
import com.xiaomai.manager.utils.Utils;
import com.xiaomai.manager.view.MyDialog;
import com.xiaomai.manager.view.RefreshListView;
import com.xiaomai.manager.view.RefreshListView.OnRefreshListener;

public class MessageManage extends BaseActivity implements OnRefreshListener {
	private RefreshListView listView;

	private List<Message> list;
	private List<Message> list_temp;
	private MessageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_manage);
		initView();
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		context = this;
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("消息查看");

		listView = (RefreshListView) findViewById(R.id.listView);
		listView.setOnRefreshListener(this);
		list = new ArrayList<Message>();
		adapter = new MessageAdapter(list, context);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				try {
					Message message = list.get(position - 1);
					Intent intent = new Intent(context,
							MessageXiangQingActivity.class);
					intent.putExtra("message", message);
					startActivity(intent);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				MyDialog.showDialog(context, "", "删除消息",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Message message = list.get(position - 1);
								list.remove(position - 1);
								adapter.setList(list);
								adapter.notifyDataSetChanged();
								message.delete(context);
							}
						}, null);
				return true;

			}
		});
		setOnClick(back);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		}

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		BmobQuery<Message> bmobQuery = new BmobQuery<Message>();
		bmobQuery.addWhereEqualTo("uid", Utils.MANAGERID);
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.setSkip(list.size());
		bmobQuery.order("-updatedAt");
		bmobQuery.findObjects(context, new FindListener<Message>() {

			@Override
			public void onSuccess(List<Message> arg0) {
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
		list = new ArrayList<Message>();
		loadData();
	}

	@Override
	public void pullUpLoadMore() {
		// TODO Auto-generated method stub
		loadData();
	}

}
