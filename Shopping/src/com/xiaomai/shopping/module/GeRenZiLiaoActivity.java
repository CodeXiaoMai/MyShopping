package com.xiaomai.shopping.module;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.User;

/**
 * 个人资料页面
 * 
 * @author XiaoMai
 *
 */
public class GeRenZiLiaoActivity extends BaseActivity {

	private View back;
	private TextView title;
	private View share;

	private ImageView iv_head;
	private EditText et_name;
	private EditText et_phone;
	private EditText et_grade;
	private EditText et_num;
	private ImageView iv_nan, iv_nv;
	private TextView tv_nan, tv_nv;
	private Button bt_save;

	private String name = "";
	private String sex = "男";
	private String phone = "";
	private String grade = "";
	private String num = "";
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geren_ziliao);
		initView();
		loadData();
	}

	private void initView() {
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("个人资料");
		share = (View) findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		iv_head = (ImageView) findViewById(R.id.iv_head);
		et_name = (EditText) findViewById(R.id.et_name);
		iv_nan = (ImageView) findViewById(R.id.iv_man);
		iv_nv = (ImageView) findViewById(R.id.iv_nv);
		tv_nan = (TextView) findViewById(R.id.tv_sex_man);
		tv_nv = (TextView) findViewById(R.id.tv_woman);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_grade = (EditText) findViewById(R.id.et_grade);
		et_num = (EditText) findViewById(R.id.et_num);
		bt_save = (Button) findViewById(R.id.bt_save);
		setOnClick(back, iv_head, iv_nan, iv_nv, tv_nan, tv_nv, bt_save);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.iv_man:
		case R.id.tv_sex_man:
			sex = "男";
			setSex(sex);
			break;
		case R.id.iv_nv:
		case R.id.tv_woman:
			sex = "女";
			setSex(sex);
			break;
		case R.id.bt_save:
			checkMessage();
			break;
		}
	}

	private void checkMessage() {
		name = et_name.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			showToast("昵称不能为空！");
			return;
		}
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("手机号不能为空！");
			return;
		}
		grade = et_grade.getText().toString().trim();
		if (TextUtils.isEmpty(grade)) {
			showToast("班级不能为空！");
			return;
		}
		num = et_num.getText().toString().trim();
		if (TextUtils.isEmpty(num)) {
			showToast("学号不能为空");
			return;
		}
		updateUserInfo();
	}

	private void updateUserInfo() {
		user.setNicheng(name);
		user.setSex(sex);
		user.setMobilePhoneNumber(phone);
		user.setGrade(grade);
		user.setNum(num);
		user.setIsNiChengChanged(true);
		user.update(context, new UpdateListener() {

			@Override
			public void onSuccess() {
				showToast("修改成功！");
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				showErrorToast(arg0, arg1);
				showLog("修改个人资料", arg0, arg1);
			}
		});
	}

	@Override
	public void loadData() {
		user = getCurrentUser();
		if (user != null) {
			name = user.getNicheng();
			if (name != null) {
				et_name.setText(name);
			}
			if (user.getIsNiChengChanged()) {
				et_name.setEnabled(false);
			}
			et_phone.setText(user.getMobilePhoneNumber());
			sex = user.getSex();
			setSex(sex);
			et_grade.setText(user.getGrade());
			et_num.setText(user.getNum());
		}
	}

	private void setSex(String sex) {
		if (sex.equals("男")) {
			iv_nan.setImageResource(R.drawable.radiobutton_xuanzhong);
			iv_nv.setImageResource(R.drawable.radiobutton_weixuanzhong);
		} else if (sex.equals("女")) {
			iv_nv.setImageResource(R.drawable.radiobutton_xuanzhong);
			iv_nan.setImageResource(R.drawable.radiobutton_weixuanzhong);
		}
	}

}
