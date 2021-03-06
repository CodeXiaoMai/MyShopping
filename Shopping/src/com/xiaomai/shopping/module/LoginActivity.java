package com.xiaomai.shopping.module;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.BlackNumber;
import com.xiaomai.shopping.bean.MyBmobInstallation;
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

	private void startPush() {
		BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
		query.addWhereEqualTo("installationId",
				BmobInstallation.getInstallationId(this));
		query.findObjects(this, new FindListener<MyBmobInstallation>() {

			@Override
			public void onSuccess(List<MyBmobInstallation> object) {
				// TODO Auto-generated method stub
				hideDialog();
				if (object.size() > 0) {
					MyBmobInstallation mbi = object.get(0);
					mbi.setUid(BmobUser.getCurrentUser(context, User.class)
							.getObjectId());
					mbi.update(context, new UpdateListener() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							Log.i("bmob", "设备信息更新成功");
						}

						@Override
						public void onFailure(int code, String msg) {
							// TODO Auto-generated method stub

							Log.i("bmob", "设备信息更新失败:" + msg);
						}
					});
				} else {
					showToast("没有设备");
				}
			}

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				showErrorToast(code, msg);
				showLog("更新信息", code, msg);
			}
		});

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
			// 判断是不是冻结
			BmobQuery<BlackNumber> bmobQuery = new BmobQuery<BlackNumber>();
			bmobQuery.addWhereEqualTo("userid", userName);
			bmobQuery.findObjects(context, new FindListener<BlackNumber>() {

				@Override
				public void onError(int arg0, String arg1) {
					// TODO Auto-generated method stub
					showErrorToast(arg0, arg1);
				}

				@Override
				public void onSuccess(List<BlackNumber> arg0) {
					// TODO Auto-generated method stub
					if (arg0 != null && arg0.size() > 0) {
						showToast("您的账号已经被冻结!");
					} else {
						// 联网登录
						userName = DES.encryptDES(userName);
						password = MD5.ecode(password);
						user = new User();
						user.setUsername(userName);
						user.setPassword(password);
						showDialog("正在登录");
						InputMethodManager imm = (InputMethodManager) context
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								et_password.getWindowToken(), 0);
						imm.hideSoftInputFromWindow(
								et_username.getWindowToken(), 0);
						user.login(context, new SaveListener() {

							@Override
							public void onSuccess() {
								hideDialog();
								User currentUser = getCurrentUser();
								String lastTimeLogin = currentUser
										.getLastTimeLogin();
								if (!lastTimeLogin.startsWith(GetDate
										.getDateBefore(0))) {
									Score score = new Score(currentUser
											.getObjectId(), Utils.SCORE_LOGIN,
											"登录");
									score.save(context);
									currentUser.setLastTimeLogin(GetDate
											.currentTime());
									currentUser.setScore(currentUser.getScore()
											+ Utils.SCORE_LOGIN);
									currentUser.update(context);
								}
								if (listener != null) {
									listener.onLogin();
								}
								startPush();
								finish();
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								showLog("Login", arg0, arg1);
								hideDialog();
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
