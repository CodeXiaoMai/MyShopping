package com.xiaomai.shopping.module;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.DES;

public class ForgetPassWordActivity extends BaseActivity {

	private EditText et_phone;
	private EditText et_yanzhengma;

	private TextView tv_yanzhengma;
	private Button bt_ok;

	private String phone;
	private String yanzhengma;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int second = msg.arg1;
			tv_yanzhengma.setText("重新发送(" + second + "s)");
			if (second == 0) {
				tv_yanzhengma.setText("重新发送");
				tv_yanzhengma.setTextColor(Color.parseColor("#595959"));
				tv_yanzhengma.setEnabled(true);
				tv_yanzhengma.setClickable(true);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("忘记密码");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		et_phone = (EditText) findViewById(R.id.et_phone);
		et_yanzhengma = (EditText) findViewById(R.id.et_yanzhengma);
		tv_yanzhengma = (TextView) findViewById(R.id.tv_yanzhengma);
		bt_ok = (Button) findViewById(R.id.bt_ok);

		setOnClick(back, tv_yanzhengma, bt_ok);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.tv_yanzhengma:
			requestSms();
			break;
		case R.id.bt_ok:
			checkMessage();
			break;
		}
	}

	private void requestSms() {
		// TODO Auto-generated method stub
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("请输入手机号");
			return;
		}
		BmobQuery<User> bmobQuery = new BmobQuery<User>();
		String username = DES.encryptDES(phone);
		bmobQuery.addWhereEqualTo("username", username);
		bmobQuery.findObjects(context, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					showToast("该用户不存在");
				} else {
					if (arg0.size() == 0) {
						showToast("该用户不存在");
					} else {
						BmobSMS.requestSMSCode(context, phone, "Shopping",
								new RequestSMSCodeListener() {

									@Override
									public void done(Integer arg0,
											BmobException arg1) {
										// TODO Auto-generated method stub
										if (arg1 == null) {
											showToast("验证码已经发送到您的手机!");
											duMiao();
										} else {
											showErrorToast(arg1.getErrorCode(),
													arg1.getMessage());
										}
									}
								});
					}
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void checkMessage() {
		// TODO Auto-generated method stub
		yanzhengma = et_yanzhengma.getText().toString().trim();
		if (TextUtils.isEmpty(yanzhengma)) {
			showToast("请输入验证码");
			return;
		}
		Intent intent = new Intent(context, UpdatePassWordActivity.class);
		intent.putExtra("smsCode", yanzhengma);
		startActivity(intent);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}
	/**
	 * 读秒，60秒内不可以重新发送
	 */
	private void duMiao() {
		tv_yanzhengma.setTextColor(Color.GRAY);
		tv_yanzhengma.setEnabled(false);
		tv_yanzhengma.setClickable(false);
		new Thread() {

			@Override
			public void run() {
				for (int i = 60; i >= 0; i--) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.what = 0;
					msg.arg1 = i;
					mHandler.sendMessage(msg);
				}
			}
		}.start();
	}
}
