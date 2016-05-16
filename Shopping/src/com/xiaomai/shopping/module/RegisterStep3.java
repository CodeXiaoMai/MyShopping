package com.xiaomai.shopping.module;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.MyBmobInstallation;
import com.xiaomai.shopping.bean.Score;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.DES;
import com.xiaomai.shopping.utils.MD5;
import com.xiaomai.shopping.utils.Utils;

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
		// 密码
		et_pass = (EditText) findViewById(R.id.et_input_number);
		// 看见密码
		eye = (ImageView) findViewById(R.id.yanjing);
		bt_next = (Button) findViewById(R.id.next);
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("设置密码");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);
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
		showDialog("正在注册");
		String username = getIntent().getStringExtra("username");
		User user = new User();
		pass = MD5.ecode(pass);
		user.setPassword(pass);
		user.setSex("未知");
		user.setScore(Utils.SCORE_REGIST);
		user.setLastTimeLogin("");
		user.setIsNiChengChanged(false);
		user.setMobilePhoneNumber(username);
		username = DES.encryptDES(username);
		user.setUsername(username);
		user.signUp(context, new SaveListener() {

			@Override
			public void onSuccess() {
				addScore();
			}

			private void addScore() {
				Score score = new Score(getCurrentUser().getObjectId(),
						Utils.SCORE_REGIST, "注册");
				score.save(context, new SaveListener() {

					@Override
					public void onSuccess() {
						// 添加推送
						startPush();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						hideDialog();
						showErrorToast(arg0, arg1);
						showLog("积分", arg0, arg1);
					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				hideDialog();
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

	private void startPush() {
		BmobQuery<MyBmobInstallation> query = new BmobQuery<MyBmobInstallation>();
		query.addWhereEqualTo("installationId",
				BmobInstallation.getInstallationId(this));
		query.findObjects(this, new FindListener<MyBmobInstallation>() {

			@Override
			public void onSuccess(List<MyBmobInstallation> object) {
				// TODO Auto-generated method stub
				if (object.size() > 0) {
					MyBmobInstallation mbi = object.get(0);
					mbi.setUid(BmobUser.getCurrentUser(context, User.class)
							.getObjectId());
					mbi.update(context, new UpdateListener() {

						@Override
						public void onSuccess() {

							hideDialog();
							// TODO Auto-generated method stub
							Log.i("bmob", "设备信息更新成功");
							showToast("注册成功,获得50积分^_^");
							Intent intent = new Intent(context,
									LoginActivity.class);
							startActivity(intent);
							finish();
						}

						@Override
						public void onFailure(int code, String msg) {
							// TODO Auto-generated method stub

							hideDialog();
							Log.i("bmob", "设备信息更新失败:" + msg);
						}
					});
				} else {
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

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
