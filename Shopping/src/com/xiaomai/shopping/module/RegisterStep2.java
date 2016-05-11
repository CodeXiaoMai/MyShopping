package com.xiaomai.shopping.module;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;

/**
 * 注册第二步
 * 
 * @author Tech
 *
 */
public class RegisterStep2 extends BaseActivity {

	private TextView tv_PhoneNumber;// 手机号码变色
	// 下一步
	private Button btn_second_next;
	private View back;

	// 手机号
	private String phoneNumber;
	// 重新发送
	private Button bt_reset_yanzhengma;
	// 输入验证码
	private EditText et_input_yanzhengma;
	// 验证码
	private Integer smsCode;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int second = msg.arg1;
			bt_reset_yanzhengma.setText("重新发送(" + second + "s)");
			if (second == 0) {
				bt_reset_yanzhengma.setText("重新发送(60s)");
				bt_reset_yanzhengma.setTextColor(Color.parseColor("#595959"));
				bt_reset_yanzhengma.setEnabled(true);
				bt_reset_yanzhengma.setClickable(true);
			}
		};
	};

	// 修改重新发送倒计时

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist2);

		initView();
		initData();
		duMiao();

	}

	private void initData() {
		String area_number = "86";// 地区号码
		Intent intent = getIntent();
		phoneNumber = intent.getExtras().getString("phoneNumber");
		smsCode = intent.getExtras().getInt("smsCode");
		String showNumber = "我们已给你的手机号码+" + area_number + " -" + phoneNumber
				+ "发送了一条验证短信";
		SpannableStringBuilder builder = new SpannableStringBuilder(showNumber);
		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		int myColor = Color.rgb(60, 187, 223);// 自定义颜色
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(myColor);

		builder.setSpan(blueSpan, "我们已给你的手机号码".length(), "我们已给你的手机号码".length()
				+ area_number.length() + phoneNumber.length() + 3,
				Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tv_PhoneNumber.setText(builder);

	}

	/**
	 * 关联控件
	 */
	private void initView() {
		et_input_yanzhengma = (EditText) findViewById(R.id.et_input_number);
		et_input_yanzhengma.requestFocus();
		bt_reset_yanzhengma = (Button) findViewById(R.id.bt_chongxinfasong);
		tv_PhoneNumber = (TextView) findViewById(R.id.regist2_tv_number);
		btn_second_next = (Button) findViewById(R.id.btn_next);
		back = findViewById(R.id.title_back);
		setOnClick(btn_second_next, back, bt_reset_yanzhengma);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.btn_next:
			yanZheng();
			break;
		// 重新发送验证码
		case R.id.bt_chongxinfasong:
			resetYanZhengMa();
			break;
		default:
			break;
		}

	}

	/**
	 * 重新发送验证码
	 */
	private void resetYanZhengMa() {
		BmobSMS.requestSMSCode(context, phoneNumber, "Shopping",
				new RequestSMSCodeListener() {

					@Override
					public void done(Integer arg0, BmobException arg1) {
						if (arg1 == null) {
							showToast("发送成功！");
							smsCode = arg0;
							duMiao();
						} else {
							showErrorToast(arg1.getErrorCode(),
									arg1.getMessage());
							showLog("获取验证码", arg1.getErrorCode(),
									arg1.getMessage());
						}
					}
				});
	}

	/**
	 * 读秒，60秒内不可以重新发送
	 */
	private void duMiao() {
		bt_reset_yanzhengma.setTextColor(Color.GRAY);
		bt_reset_yanzhengma.setEnabled(false);
		bt_reset_yanzhengma.setClickable(false);
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

	/**
	 * 验证验证码
	 */
	private void yanZheng() {
		BmobSMS.verifySmsCode(context, phoneNumber, smsCode + "",
				new VerifySMSCodeListener() {

					@Override
					public void done(BmobException arg0) {
						if (arg0 == null) {
							Intent intent = new Intent(context,
									RegisterStep3.class);
							intent.putExtra("username", phoneNumber);
						} else {
							showLog("验证验证码", 1, arg0.toString());
							showToast(arg0.toString());
						}
					}
				});
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
