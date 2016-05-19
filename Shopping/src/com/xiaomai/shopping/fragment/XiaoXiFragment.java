package com.xiaomai.shopping.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.adapter.MessageAdapter;
import com.xiaomai.shopping.base.BaseFragment;
import com.xiaomai.shopping.bean.Message;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.biz.DBUtil;
import com.xiaomai.shopping.module.HomeActivity;
import com.xiaomai.shopping.receiver.MyPushMessageReceiver;
import com.xiaomai.shopping.receiver.MyPushMessageReceiver.onReceiveMessageListener;
import com.xiaomai.shopping.utils.BackMessage;
import com.xiaomai.shopping.utils.GetDate;

/**
 * 消息页面
 * 
 * @author XiaoMai
 *
 */
public class XiaoXiFragment extends BaseFragment implements
		onReceiveMessageListener {

	private View back;
	private TextView title;
	private View share;

	private List<Message> list;
	private ListView listView;
	private MessageAdapter adapter;

	private User user;
	private Uri uri = Uri.parse("content://" + "com.xiaomai.message");
	private boolean loadHistoryMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_xiaoxi, container, false);
		initView(view);
		user = BmobUser.getCurrentUser(context, User.class);
		if (user != null) {
			fillData();
		}
		MyPushMessageReceiver.listener = this;
		HomeActivity.listener = this;
		return view;
	}

	private void fillData() {
		// TODO Auto-generated method stub
		Cursor cursor = context.getContentResolver().query(uri, null,
				DBUtil.MESSAGE_COLUMN_MESSAGE_UID + "=?",
				new String[] { user.getObjectId() }, "_time");
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Message message = null;
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				String objId = cursor.getString(0);
				String uid = cursor.getString(1);
				String type = cursor.getString(2);
				String content = cursor.getString(3);
				String time = cursor.getString(4);
				int state = cursor.getInt(5);
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

	private void initView(View view) {

		context = getContext();
		// 隐藏返回
		back = view.findViewById(R.id.title_back);
		back.setVisibility(View.GONE);
		// 设置标题
		title = (TextView) view.findViewById(R.id.title_title);
		title.setText("消息列表");
		// 隐藏分享
		share = view.findViewById(R.id.title_share);
		share.setVisibility(View.GONE);

		// 消息列表
		listView = (ListView) view.findViewById(R.id.listView);
		View emptyView = view.findViewById(R.id.emptyView);
		listView.setEmptyView(emptyView);

		list = new ArrayList<Message>();
		adapter = new MessageAdapter(list, context);
		listView.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {

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
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onReceiveMessage(List<Message> list) {

		if (!loadHistoryMessage) {
			loadData();
		}
		ContentValues values;
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
			values.put(DBUtil.MESSAGE_COLUMN_TIME, message.getUpdatedAt());
			context.getContentResolver().insert(uri, values);
		}
		this.list.addAll(list);
		adapter.setList(this.list);
		adapter.notifyDataSetChanged();
	}

}
