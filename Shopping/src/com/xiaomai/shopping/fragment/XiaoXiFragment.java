package com.xiaomai.shopping.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.adapter.MessageAdapter;
import com.xiaomai.shopping.base.BaseFragment;
import com.xiaomai.shopping.bean.Message;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.biz.DBUtil;
import com.xiaomai.shopping.listener.OnLoginOutListener;
import com.xiaomai.shopping.module.HomeActivity;
import com.xiaomai.shopping.module.LoginActivity;
import com.xiaomai.shopping.module.MessageXiangQingActivity;
import com.xiaomai.shopping.receiver.MyPushMessageReceiver;
import com.xiaomai.shopping.receiver.MyPushMessageReceiver.onReceiveMessageListener;
import com.xiaomai.shopping.utils.GetDate;
import com.xiaomai.shopping.view.MyDialog;

/**
 * 消息页面
 * 
 * @author XiaoMai
 *
 */
public class XiaoXiFragment extends BaseFragment implements
		onReceiveMessageListener, OnLoginOutListener {

	private View back;
	private TextView title;
	private ImageView share;

	private List<Message> list;
	private ListView listView;
	private MessageAdapter adapter;

	private User user;
	private Uri uri = Uri.parse("content://" + "com.xiaomai.message");
	private boolean loadHistoryMessage;

	private ContentValues values;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_xiaoxi, container, false);
		initView(view);
		fillData();
		MyPushMessageReceiver.listener = this;
		HomeActivity.listener = this;
		return view;
	}

	private void fillData() {
		// TODO Auto-generated method stub
		user = BmobUser.getCurrentUser(context, User.class);
		if (user != null) {
			Cursor cursor = context.getContentResolver().query(uri, null,
					DBUtil.MESSAGE_COLUMN_MESSAGE_UID + "=?",
					new String[] { user.getObjectId() }, "_time");
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Message message = null;
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					String objId = cursor.getString(cursor
							.getColumnIndex(DBUtil.MESSAGE_OBJ_ID));
					String uid = cursor.getString(cursor
							.getColumnIndex(DBUtil.MESSAGE_COLUMN_MESSAGE_UID));
					String type = cursor.getString(cursor
							.getColumnIndex(DBUtil.MESSAGE_COLUMN_TYPE));
					String content = cursor
							.getString(cursor
									.getColumnIndex(DBUtil.MESSAGE_COLUMN_MESSAGE_CONTENT));
					String time = cursor.getString(cursor
							.getColumnIndex(DBUtil.MESSAGE_COLUMN_TIME));
					int state = cursor.getInt(cursor
							.getColumnIndex(DBUtil.MESSAGE_COLUMN_STATE));
					message = new Message(uid, type, content, time);
					message.setObjectId(objId);
					message.setState(state);
				}
				list.add(message);
			}
			adapter.setList(list);
			adapter.notifyDataSetChanged();
			loadHistoryMessage = true;
		}
	}

	private void initView(View view) {

		context = getContext();
		LoginActivity.listener = this;
		// 隐藏返回
		back = view.findViewById(R.id.title_back);
		back.setVisibility(View.INVISIBLE);
		// 设置标题
		title = (TextView) view.findViewById(R.id.title_title);
		title.setText("消息列表");
		// 隐藏分享
		share = (ImageView) view.findViewById(R.id.title_share);
		share.setImageResource(R.drawable.clear);

		// 消息列表
		listView = (ListView) view.findViewById(R.id.listView);
		View emptyView = view.findViewById(R.id.emptyView);
		listView.setEmptyView(emptyView);

		list = new ArrayList<Message>();
		adapter = new MessageAdapter(list, context);
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				MyDialog.showDialog(context, "", "确认删除",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Message message = list.get(list.size()
										- position - 1);
								context.getContentResolver().delete(uri,
										DBUtil.MESSAGE_OBJ_ID + "=?",
										new String[] { message.getObjectId() });
								list.remove(list.size() - position - 1);
								adapter.setList(list);
								adapter.notifyDataSetChanged();
							}
						}, null);
				return true;
			}

		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				Message message = list.get(list.size() - position - 1);
				Intent intent = new Intent(context,
						MessageXiangQingActivity.class);
				intent.putExtra("message", message);
				startActivity(intent);

			}
		});

		setOnClick(share);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_share:
			MyDialog.showDialog(context, "提示消息", "确定清空消息列表",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							context.getContentResolver().delete(uri, "", null);
							list = new ArrayList<Message>();
							adapter.setList(list);
							adapter.notifyDataSetChanged();
						}
					}, null);
			break;

		default:
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveMessage(String json) {
		showLog("json", 0, json);
		try {
			JSONObject jsonObject = new JSONObject(json);
			String type = jsonObject.getString("type");
			String content = jsonObject.getString("content");
			String time = GetDate.currentTime().replace(" ", "\n");
			Message message = new Message("", type, content, time);
			list.add(message);
			adapter.setList(list);
			adapter.notifyDataSetChanged();

			values = new ContentValues();
			values.put(DBUtil.MESSAGE_OBJ_ID, "");
			values.put(DBUtil.MESSAGE_COLUMN_MESSAGE_UID, user.getObjectId());
			values.put(DBUtil.MESSAGE_COLUMN_MESSAGE_CONTENT,
					message.getContent());
			values.put(DBUtil.MESSAGE_COLUMN_TYPE, message.getType());
			values.put(DBUtil.MESSAGE_COLUMN_STATE, Message.STATE_YIDU);
			values.put(DBUtil.MESSAGE_COLUMN_TIME, message.getTime());
			context.getContentResolver().insert(uri, values);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onReceiveMessage(List<Message> list) {

		if (!loadHistoryMessage) {
			loadData();
		}
		for (Message message : list) {
			message.setState(Message.STATE_YIDU);
			message.update(context);
			values = new ContentValues();
			values.put(DBUtil.MESSAGE_OBJ_ID, message.getObjectId());
			values.put(DBUtil.MESSAGE_COLUMN_MESSAGE_UID, message.getUid());
			values.put(DBUtil.MESSAGE_COLUMN_MESSAGE_CONTENT,
					message.getContent());
			values.put(DBUtil.MESSAGE_COLUMN_TYPE, message.getType());
			values.put(DBUtil.MESSAGE_COLUMN_STATE, message.getState());
			values.put(DBUtil.MESSAGE_COLUMN_TIME, message.getTime());
			context.getContentResolver().insert(uri, values);
		}
		this.list.addAll(list);
		adapter.setList(this.list);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		fillData();
	}

	@Override
	public void onLogOut() {
		// TODO Auto-generated method stub
		if (adapter != null) {
			list = new ArrayList<Message>();
			adapter.setList(list);
			adapter.notifyDataSetChanged();
		}
	}

}
