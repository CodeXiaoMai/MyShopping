package com.xiaomai.shopping.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.listener.SaveListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Score;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.listener.OnLoginOutListener;
import com.xiaomai.shopping.utils.DES;
import com.xiaomai.shopping.utils.GetDate;
import com.xiaomai.shopping.utils.MD5;
import com.xiaomai.shopping.utils.Utils;

/**
 * 这是登录的页面
 *
 * @author XiaoMai
 *
 */
public class LoginActivity extends BaseActivity {

	private Context context;
	private View tv_regist;// 注册
	private View tv_forgetPass;// 忘记密码
	private Button bt_login;// 登录
	// 用户名
	private EditText et_username;
	private String userName;
	// 密码
	private EditText et_password;
	private String password;
	private User user;

	public static OnLoginOutListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initTitle();
	}

	/**
	 * 初始化控件
	 */

	private void initView() {
		context = this;
		// 用户名
		et_username = (EditText) findViewById(R.id.denglu_et_userName);
		// 密码
		et_password = (EditText) findViewById(R.id.denglu_et_password);
		// 注册
		tv_regist = findViewById(R.id.denglu_tv_regist);
		// 忘记密码
		tv_forgetPass = findViewById(R.id.denglu_tv_forgetpass);
		// 登录
		bt_login = (Button) findViewById(R.id.denglu_bt_login);
		// 设置监听
		setOnClick(bt_login, tv_regist, tv_forgetPass);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 点击返回按钮，退出
		case R.id.title_back:
			finish();
			break;
		// 注册
		case R.id.denglu_tv_regist:
			startActivity(new Intent(this, RegisterStep1.class));
			break;
		// 忘记密码
		case R.id.denglu_tv_forgetpass:
			startActivity(new Intent(this, ForgetPassWordActivity.class));
			break;
		// 登录按钮
		case R.id.denglu_bt_login:
			login();
			break;
		default:
			break;
		}
	}

	/**
	 * 登录
	 */
	private void login() {
		userName = et_username.getText().toString().trim();
		password = et_password.getText().toString().trim();
		// 如果用户名或密码为空，提示
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
			Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT)
					.show();
			return;
		} else {
			// 联网登录
			userName = DES.encryptDES(userName);
			password = MD5.ecode(password);
			user = new User();
			user.setUsername(userName);
			user.setPassword(password);
			user.login(context, new SaveListener() {

				@Override
				public void onSuccess() {
					User currentUser = getCurrentUser();
					String lastTimeLogin = currentUser.getLastTimeLogin();
					if (!lastTimeLogin.startsWith(GetDate.getDateBefore(0))) {
						Score score = new Score(currentUser.getObjectId(),
								Utils.SCORE_LOGIN, "登录");
						score.save(context);
						currentUser.setLastTimeLogin(GetDate.currentTime());
						currentUser.setScore(currentUser.getScore()
								+ Utils.SCORE_LOGIN);
						currentUser.update(context);
					}
					if (listener != null) {
						listener.onLogin();
					}
					finish();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					showLog("Login", arg0, arg1);
					switch (arg0) {
					case 101:
						showToast("用户名或密码错误！");
						break;
					case 9016:
						showToast(getString(R.string.network_error));
						break;
					}
				}
			});
		}
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
		tv_title.setText("登录");

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
