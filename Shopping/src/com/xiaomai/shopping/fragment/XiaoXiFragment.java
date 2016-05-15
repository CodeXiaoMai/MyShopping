package com.xiaomai.shopping.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.adapter.MessageAdapter;
import com.xiaomai.shopping.base.LazyFragment;
import com.xiaomai.shopping.bean.Message;
import com.xiaomai.shopping.receiver.MyPushMessageReceiver;
import com.xiaomai.shopping.receiver.MyPushMessageReceiver.onReceiveMessageListener;
import com.xiaomai.shopping.utils.GetDate;

/**
 * 消息页面
 * 
 * @author XiaoMai
 *
 */
public class XiaoXiFragment extends LazyFragment implements
		onReceiveMessageListener {

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;

	private View back;
	private TextView title;
	private View share;

	private List<Message> list;
	private ListView listView;
	private MessageAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_xiaoxi, container, false);
		initView(view);
		isPrepared = true;
		lazyLoad();
		MyPushMessageReceiver.listener = this;
		return view;
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
	public void onReceiveMessage(Intent intent) {
		String json = intent.getStringExtra("msg");
		showLog("json", 0, json);
		try {
			JSONObject jsonObject = new JSONObject(json);
			String type = jsonObject.getString("type");
			String content = jsonObject.getString("content");
			String time = GetDate.currentTime().replace(" ", "\n");
			Message message = new Message(type, content, time);
			list.add(message);
			adapter.setList(list);
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		if (!isPrepared || !isVisible) {
			return;
		}
		
	}

}
