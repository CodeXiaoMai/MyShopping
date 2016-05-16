package com.xiaomai.shopping.module;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.ResetPasswordByCodeListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.utils.MD5;

public class UpdatePassWordActivity extends BaseActivity {

	// 下一步
	private Button bt_next;
	// 密码输入
	private EditText et_pass;
	// 密码是否可见
	private ImageView eye;
	private boolean isEye;
	private View back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist3);
		initView();
	}

	private void initView() {
		// 密码
		et_pass = (EditText) findViewById(R.id.et_input_number);
		// 看见密码
		eye = (ImageView) findViewById(R.id.yanjing);
		bt_next = (Button) findViewById(R.id.next);
		bt_next.setText("确认修改");
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("修改密码");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);
		setOnClick(bt_next, back, eye);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.next:
			setPassWord();
			break;
		case R.id.yanjing:
			if (isEye) {
				// 如果现在可见，就设置为不可见
				eye.setImageResource(R.drawable.yanjing_kanbujianmima);
				et_pass.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			} else {
				// 如果现在不可见，就设置为可见
				eye.setImageResource(R.drawable.yanjing_kanjianmima);
				et_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}
			isEye = !isEye;
			break;
		default:
			break;
		}
	}

	private void setPassWord() {
		showDialog("数据提交中");
		String pass = et_pass.getText().toString().trim();
		if (pass.length() < 6) {
			showToast("密码长度不能小于6位");
			return;
		}
		if (TextUtils.isEmpty(pass)) {
			showToast("密码不能为空");
			return;
		}
		pass = MD5.ecode(pass);

		Intent intent = getIntent();
//		User user = (User) intent.getSerializableExtra("user");
		String smsCode = intent.getStringExtra("smsCode");
		BmobUser.resetPasswordBySMSCode(context, smsCode, pass,
				new ResetPasswordByCodeListener() {

					@Override
					public void done(BmobException arg0) {
						// TODO Auto-generated method stub
						hideDialog();
						if (arg0 == null) {
							showToast("密码修改成功！");
							startActivity(new Intent(context,
									LoginActivity.class));
						} else {
							showErrorToast(arg0.getErrorCode(),
									arg0.getMessage());
						}
					}
				});

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
