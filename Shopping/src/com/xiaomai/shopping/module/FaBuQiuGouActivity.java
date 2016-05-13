package com.xiaomai.shopping.module;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.listener.SaveListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.IWant;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.MyDialog;

/**
 * 发布求购
 * 
 * @author XiaoMai
 *
 */
public class FaBuQiuGouActivity extends BaseActivity {

	// 待审核
	private static final int STATE_DAISHENHE = 0;
	// 审核失败
	private static final int STATE_SHENHE_SHIBAI = -100;
	// 正常
	private static final int STATE_NORMAL = 1;
	// 取消
	private static final int STATE_CANCEL = -1;

	private View back;
	private TextView title;
	private View share;

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

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fabuqiugou);
		initView();
	}

	private void initView() {
		context = this;
		user = getCurrentUser();
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
		et_phone.setText(user.getMobilePhoneNumber());
		et_phone.clearFocus();
		et_qq = (EditText) findViewById(R.id.et_qq);
		bt_fabu = (Button) findViewById(R.id.bt_fabu);
		et_title.requestFocus();
		setOnClick(back, bt_fabu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.bt_fabu:
			checkMessage();
			break;
		default:
			break;
		}
	}

	private void checkMessage() {
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
		String userId = user.getObjectId();
		String userName;
		if (user.getIsNiChengChanged()) {
			userName = user.getNicheng();
		} else {
			userName = user.getUsername();
		}
		String imageUri = user.getImageUri();
		if (imageUri == null) {
			imageUri = "";
		}
		final IWant iWant = new IWant(userId, userName, imageUri, strTitle,
				desc, minPrice, maxPrice, phone, qq);
		MyDialog.showDialog(context, "提示", "确认发布",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						saveData(iWant);
					}
				}, null);

	}

	private void saveData(IWant iWant) {
		// TODO Auto-generated method stub
		iWant.save(context, new SaveListener() {

			@Override
			public void onSuccess() {
				addScore(user, Utils.SCORE_ADD_IWANT, "发布求购");
				MyDialog.showDialog(context, "发布成功", "您还要继续发布新的求购吗？",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								et_title.setText("");
								et_desc.setText("");
								et_minPrice.setText("");
								et_maxPrice.setText("");
							}

						}, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								finish();
							}
						});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				showErrorToast(arg0, arg1);
				showLog("发布", arg0, arg1);

			}
		});
	}

	@Override
	public void loadData() {

	}

}
