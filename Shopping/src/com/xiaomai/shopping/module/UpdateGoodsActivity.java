package com.xiaomai.shopping.module;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cn.bmob.v3.listener.UpdateListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.utils.StateCode;
import com.xiaomai.shopping.view.MyDialog;

public class UpdateGoodsActivity extends BaseActivity {

	private Context context;
	// 标题
	private EditText et_title;
	// 描述
	private EditText et_content;
	// 价格
	private EditText et_price;
	// 数量
	private EditText et_count;
	// 交易地点
	private EditText et_address;
	// 手机号
	private EditText et_phone;
	// QQ号
	private EditText et_qq;
	// 发布
	private Button bt_fabu;

	private Spinner spinner;
	private String[] names = { "点击选择分类 ", "交通工具", "电子产品", "体育器材", "学习用品",
			"衣帽鞋子", "其他" };

	private String title2;
	private String content;
	private Double price;
	private Integer count;
	private String address;
	private String phone;
	private String qq;
	private String type;

	private Goods goods;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fabu);
		initView();
		fillData();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		goods = (Goods) getIntent().getSerializableExtra("goods");
		et_title.setText(goods.getTitle());
		et_content.setText(goods.getContent());
		et_price.setText(goods.getPrice() + "");
		et_count.setText(goods.getCount() + "");
		et_address.setText(goods.getAddress());
		et_phone.setText(goods.getPhone());
		et_qq.setText(goods.getQq());
		int i = 1;
		switch (goods.getType()) {
		case "交通工具":
			i = 1;
			break;
		case "电子产品":
			i = 2;
			break;
		case "体育器材":
			i = 3;
			break;
		case "学习用品":
			i = 4;
			break;
		case "衣帽鞋子":
			i = 5;
			break;
		case "其他":
			i = 6;
			break;
		}
		spinner.setSelection(i);
	}

	private void initView() {
		// TODO Auto-generated method stub
		context = this;
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("修改商品信息");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);
		et_title = (EditText) findViewById(R.id.fabu_et_title);
		et_content = (EditText) findViewById(R.id.fabu_et_content);
		et_price = (EditText) findViewById(R.id.fabu_et_price);
		et_count = (EditText) findViewById(R.id.fabu_et_count);
		et_address = (EditText) findViewById(R.id.fabu_et_address);
		et_phone = (EditText) findViewById(R.id.fabu_et_phone);
		et_qq = (EditText) findViewById(R.id.fabu_et_qq);
		bt_fabu = (Button) findViewById(R.id.fabu_bt_fabu);
		findViewById(R.id.tv_add_image).setVisibility(View.GONE);
		bt_fabu.setText("确定修改");
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_item,
				R.id.textView, names));

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				type = names[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		setOnClick(back, bt_fabu);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fabu_bt_fabu:
			checkMessage();
			break;
		case R.id.title_back:
			finish();
			break;
		}
	}

	private void checkMessage() {
		// TODO Auto-generated method stub
		title2 = et_title.getText().toString().trim();
		if (TextUtils.isEmpty(title2)) {
			showToast("标题不能为空！");
			return;
		}
		content = et_content.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			showToast("给你的宝贝添加几句描述吧！");
			return;
		}
		String strprice = et_price.getText().toString().trim();
		if (TextUtils.isEmpty(strprice)) {
			showToast("价格不能为空！");
			return;
		}
		price = Double.parseDouble(strprice);
		String str_count = et_count.getText().toString().trim();
		if (TextUtils.isEmpty(str_count)) {
			showToast("数量不能为空！");
			return;
		}
		if (TextUtils.isEmpty(type) || type.equals(names[0])) {
			showToast("请选择分类");
			return;
		}
		count = Integer.parseInt(str_count);
		address = et_address.getText().toString().trim();
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("留下您的联系方式吧！");
			return;
		}
		qq = et_qq.getText().toString().trim();
		goods.setTitle(title2);
		goods.setContent(str_count);
		goods.setPrice(price);
		goods.setCount(count);
		goods.setType(type);
		goods.setAddress(address);
		goods.setPhone(phone);
		goods.setQq(qq);
		goods.setState(StateCode.GOODS_SHENHE);
		MyDialog.showDialog(context, "", "确定修改",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						showDialog("正在提交数据");
						goods.update(context, new UpdateListener() {

							@Override
							public void onSuccess() {
								// TODO Auto-generated method stub
								showToast("更新成功!");
								hideDialog();
								finish();
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO Auto-generated method stub
								hideDialog();
								showErrorToast(arg0, arg1);
								showLog("修改商品", arg0, arg1);
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
