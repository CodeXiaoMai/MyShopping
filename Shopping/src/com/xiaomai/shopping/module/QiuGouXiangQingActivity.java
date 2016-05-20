package com.xiaomai.shopping.module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.IWant;
import com.xiaomai.shopping.bean.Message;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.GetDate;

public class QiuGouXiangQingActivity extends BaseActivity {

	private IWant iwant;
	private TextView tv_title;
	private TextView tv_desc;
	private TextView tv_price;
	private TextView tv_date;
	private View phone;
	private View sms;
	private EditText et_comment;
	private View send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qiugouxiangqing);
		initView();
		fillData();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		iwant = (IWant) getIntent().getSerializableExtra("want");
		tv_price.setText(iwant.getMinPrice() + "--" + iwant.getMaxPrice());
		tv_date.setText(iwant.getUpdatedAt());
		tv_title.setText(iwant.getTitle());
		tv_desc.setText(iwant.getDesc());
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("求购详情");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		tv_title = (TextView) findViewById(R.id.title);
		tv_desc = (TextView) findViewById(R.id.desc);
		tv_price = (TextView) findViewById(R.id.qiugou_tv_price);
		tv_date = (TextView) findViewById(R.id.qiugou_tv_date);

		phone = findViewById(R.id.phone);
		sms = findViewById(R.id.chat);

		et_comment = (EditText) findViewById(R.id.et_pinglun);
		send = findViewById(R.id.iv_fasong);
		setOnClick(back, send, phone, sms);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.iv_fasong:
			getCommentContent();
			break;
		case R.id.phone:
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + iwant.getPhone()));
			if (intent.resolveActivity(context.getPackageManager()) != null) {
				startActivity(intent);
			}
			break;
		case R.id.chat:
			String phoneNumber = iwant.getPhone();
			intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
					+ phoneNumber));
			intent.putExtra("sms_body", "你好，我在北苑跳蚤市场看到你发布的[" + iwant.getTitle()
					+ "],我这有你想要的，可以聊聊吗？");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 发送评论
	 */
	private void getCommentContent() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);

		final String content = et_comment.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			showToast("留言内容不能为空！");
			return;
		}
		final User currentUser = getCurrentUser();
		if (currentUser != null) {
			showDialog("正在发送...");
			Message message = new Message(currentUser.getObjectId(), "有求必应",
					content, GetDate.currentTime().replace(" ", "\n"));
			message.save(context, new SaveListener() {

				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					et_comment.setText("");
					hideDialog();
					showToast("留言发送成功");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					hideDialog();
					showErrorToast(arg0, arg1);
					showLog("发送留言", arg0, arg1);
				}
			});
		}

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
