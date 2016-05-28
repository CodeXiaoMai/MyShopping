package com.xiaomai.manager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaomai.manager.base.BaseActivity;
import com.xiaomai.manager.bean.Message;

public class MessageXiangQingActivity extends BaseActivity {

	private TextView tv_title;
	private TextView tv_content;
	private TextView tv_date;

	private Message message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_xiangqing);
		initView();
		fillData();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		message = (Message) getIntent().getSerializableExtra("message");
		tv_title.setText(message.getType());
		tv_content.setText(message.getContent());
		tv_date.setText(message.getTime());
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("消息详情");

		tv_title = (TextView) findViewById(R.id.tv_message_type);
		tv_content = (TextView) findViewById(R.id.tv_message_content);
		tv_date = (TextView) findViewById(R.id.tv_message_time);
		setOnClick(back);
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

	}

}
