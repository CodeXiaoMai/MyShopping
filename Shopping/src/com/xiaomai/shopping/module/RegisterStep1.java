package com.xiaomai.shopping.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.DES;

public class RegisterStep1 extends BaseActivity {

	// 手机号输入框
	private EditText et_phoneNumber;
	// 下一步
	private Button bt_next;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist1);
		initTitle();
		initView();
	}

	private void initView() {
		context = this;
		et_phoneNumber = (EditText) findViewById(R.id.regist1_et_number);
		bt_next = (Button) findViewById(R.id.regist_bt_next);
		setOnClick(bt_next);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.regist_bt_next:
			String userName = et_phoneNumber.getText().toString().trim();
			if (userName.length() < 11) {
				showToast("请输入正确的手机号码！");
				return;
			}
			User user = new User();
			user.setUsername(userName);
			queryName(userName);
			break;
		default:
			break;
		}
	}

	/**
	 * 查询是否已经存在此用户名
	 * 
	 * @param userName
	 */
	private void queryName(final String userName) {
		showDialog("正在验证手机号");
		BmobQuery<User> query = new BmobQuery<User>();
		final String username = DES.encryptDES(userName);
		query = query.addWhereEqualTo("username", username);
		query.count(this, User.class, new CountListener() {

			@Override
			public void onSuccess(int count) {
				hideDialog();
				Log.i("count:", count + "");
				// 不存在此用户，可以注册
				// 为了防止输入密码的过程中别人注册，所以向数据库中添加此用户名，若用户放弃注册怎么办,采用验证码
				if (count <= 0) {
					// 发送验证码
					showDialog("正在发送验证码");
					final String phoneNumber = userName;
					BmobSMS.requestSMSCode(context, phoneNumber, "Shopping",
							new RequestSMSCodeListener() {

								@Override
								public void done(Integer arg0,
										BmobException arg1) {

									hideDialog();
									if (arg1 == null) {
										Intent intent = new Intent(context,
												RegisterStep2.class);
										intent.putExtra("phoneNumber",
												phoneNumber);
										startActivity(intent);
									} else {
										arg1.getErrorCode();
										showErrorToast(arg1.getErrorCode(),
												arg1.getMessage());
									}
								}

							});

				} else {
					showToast("此手机号已经注册！");
				}
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				hideDialog();
				showErrorToast(arg0, arg1);
				showLog("RegisterStep1", arg0, arg1);
			}
		});
	}

	private void initTitle() {
		// 标题的控件
		TextView tv_title;
		View back;
		View share;

		// 返回按钮
		back = findViewById(R.id.title_back);

		// 设置标题
		tv_title = (TextView) findViewById(R.id.title_title);
		tv_title.setText("注册");

		// 隐藏分享
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		setOnClick(back);
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}
}
