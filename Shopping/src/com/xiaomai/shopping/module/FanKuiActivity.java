package com.xiaomai.shopping.module;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Suggestion;
import com.xiaomai.shopping.bean.User;

public class FanKuiActivity extends BaseActivity {

	private View back;
	private TextView title;
	private View share;

	private EditText et_content;
	private EditText et_phone;
	private Button bt_commit;

	private String content;
	private String phone;

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fankui);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		user = getCurrentUser();
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("反馈中心");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		et_content = (EditText) findViewById(R.id.et_content);
		et_phone = (EditText) findViewById(R.id.et_phone);
		bt_commit = (Button) findViewById(R.id.bt_commit);
		setOnClick(back, bt_commit);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.bt_commit:
			checkMessage();
			break;
		default:
			break;
		}
	}

	/**
	 * 检查用户输入的内容
	 */
	private void checkMessage() {
		content = et_content.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			showToast("您想说点什么...");
			return;
		}
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("留下您的联系方式吧!");
			return;
		}
		saveData();
	}

	/**
	 * 上传数据
	 */
	private void saveData() {
		// TODO Auto-generated method stub
		showToast("正在提交，请稍候!");
		Suggestion suggestion = new Suggestion(user.getObjectId(), content,
				phone);
		suggestion.save(context, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("提交成功！");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showErrorToast(arg0, arg1);
				showLog("提交", arg0, arg1);
			}
		});
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
