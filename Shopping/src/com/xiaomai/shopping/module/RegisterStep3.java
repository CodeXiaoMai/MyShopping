package com.xiaomai.shopping.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.bmob.v3.listener.SaveListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.DES;
import com.xiaomai.shopping.utils.MD5;

/**
 * 注册第三步
 * 
 * @author Tech
 *
 */
public class RegisterStep3 extends BaseActivity {

	private Context context;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist3);
		initView();
	}

	private void initView() {
		context = this;
		// 密码
		et_pass = (EditText) findViewById(R.id.et_input_number);
		// 看见密码
		eye = (ImageView) findViewById(R.id.yanjing);
		bt_next = (Button) findViewById(R.id.next);
		back = findViewById(R.id.title_back);
		setOnClick(bt_next, back, eye);
	}

	@Override
	public void onClick(View v) {
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
		String pass = et_pass.getText().toString().trim();
		if (pass.length() < 6) {
			showToast("密码长度不能小于6位");
			return;
		}
		if (TextUtils.isEmpty(pass)) {
			showToast("密码不能为空");
			return;
		}
		String username = getIntent().getStringExtra("username");
		pass = MD5.ecode(pass);
		User user = new User();
		user.setUsername(username);
		user.setPassword(pass);
		try {
			user.setMobilePhoneNumber(DES.decryptDES(username, "20120401"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.signUp(context, new SaveListener() {

			@Override
			public void onSuccess() {
				showToast("注册成功");
				Intent intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				showLog("Register3", arg0, arg1);
				switch (arg0) {
				case 301:
					showToast("您的手机号不是有效的号码！");
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

}
