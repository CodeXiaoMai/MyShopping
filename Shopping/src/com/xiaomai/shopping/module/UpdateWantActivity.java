package com.xiaomai.shopping.module;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.IWant;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.view.MyDialog;

public class UpdateWantActivity extends BaseActivity {

	// 待审核
	private static final int STATE_DAISHENHE = 0;
	// 审核失败
	private static final int STATE_SHENHE_SHIBAI = -100;
	// 正常
	private static final int STATE_NORMAL = 1;
	// 取消
	private static final int STATE_CANCEL = -1;

	private EditText et_title;
	private String strTitle;
	private EditText et_desc;
	private String desc;
	private EditText et_minPrice;
	private String minPrice;
	private EditText et_maxPrice;
	private String maxPrice;
	private EditText et_phone;
	private String phone;
	private EditText et_qq;
	private String qq;

	private Button bt_fabu;
	private Context context;

	private IWant want;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fabuqiugou);
		initView();
		fillData();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		want = (IWant) getIntent().getSerializableExtra("want");
		et_title.setText(want.getTitle());
		et_desc.setText(want.getDesc());
		et_minPrice.setText(want.getMinPrice() + "");
		et_maxPrice.setText(want.getMaxPrice() + "");
		et_phone.setText(want.getPhone());
		et_qq.setText(want.getQq());
	}

	private void initView() {
		// TODO Auto-generated method stub
		context = this;
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("发布求购");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);
		et_title = (EditText) findViewById(R.id.et_title);
		et_desc = (EditText) findViewById(R.id.et_desc);
		et_minPrice = (EditText) findViewById(R.id.et_minPrice);
		et_maxPrice = (EditText) findViewById(R.id.et_maxPrice);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_qq = (EditText) findViewById(R.id.et_qq);
		bt_fabu = (Button) findViewById(R.id.bt_fabu);
		bt_fabu.setText("确定修改");
		et_title.requestFocus();

		setOnClick(back, bt_fabu);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		case R.id.bt_fabu:
			checkMessage();
			break;
		}
	}

	private void checkMessage() {
		// TODO Auto-generated method stub
		strTitle = et_title.getText().toString().trim();
		if (TextUtils.isEmpty(strTitle)) {
			showToast("标题不能为空");
			return;
		}
		desc = et_desc.getText().toString().trim();
		if (TextUtils.isEmpty(desc)) {
			showToast("描述不能为空");
			return;
		}
		minPrice = et_minPrice.getText().toString().trim();
		if (TextUtils.isEmpty(minPrice)) {
			showToast("最低价格不能为空");
			return;
		}
		maxPrice = et_maxPrice.getText().toString().trim();
		if (TextUtils.isEmpty(maxPrice)) {
			showToast("最高价格不能为空");
			return;
		}
		float min = Float.parseFloat(minPrice);
		float max = Float.parseFloat(maxPrice);
		if (max - min < 0) {
			showToast("价格区间有误");
			return;
		}
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("留个电话儿吧，亲！");
			return;
		}
		qq = et_qq.getText().toString().trim();
		fabu();
	}

	private void fabu() {
		user = getCurrentUser();
		want.setTitle(strTitle);
		want.setDesc(desc);
		want.setMinPrice(minPrice);
		want.setMaxPrice(maxPrice);
		want.setPhone(phone);
		want.setQq(qq);
		want.setState(STATE_DAISHENHE);
		want.setUserImage(user.getImageUri());
		MyDialog.showDialog(context, "", "确认修改",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						showDialog("正在提交数据");
						want.update(context, new UpdateListener() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								hideDialog();
								showToast("更新成功");
								finish();
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								hideDialog();
								showErrorToast(arg0, arg1);
								showLog("更新求购", arg0, arg1);
							}
						});
					}
				}, null);

	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
