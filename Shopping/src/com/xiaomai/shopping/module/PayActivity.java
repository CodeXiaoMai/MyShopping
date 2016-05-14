package com.xiaomai.shopping.module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Goods;

public class PayActivity extends BaseActivity {

	private ImageView image;
	private TextView tv_price;
	private TextView tv_title;
	private View ll_weixin;
	private ImageView iv_weixin;
	private View ll_zhifubao;
	private ImageView iv_zhifubao;
	private Button bt_pay;
	private boolean isZhiFuBao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		initView();
		fillData();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Goods goods = (Goods) intent.getSerializableExtra("goods");
		tv_price.setText("￥" + goods.getPrice());
		tv_title.setText(goods.getTitle());
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("支付订单");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		image = (ImageView) findViewById(R.id.image);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_title = (TextView) findViewById(R.id.tv_name);
		ll_weixin = findViewById(R.id.ll_weixin);
		iv_weixin = (ImageView) findViewById(R.id.iv_weixin);
		ll_zhifubao = findViewById(R.id.ll_zhifubao);
		iv_zhifubao = (ImageView) findViewById(R.id.iv_zhifubao);
		bt_pay = (Button) findViewById(R.id.bt_pay);
		setOnClick(back, ll_weixin, ll_zhifubao, bt_pay);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.ll_zhifubao:
			isZhiFuBao = true;
			iv_zhifubao.setImageResource(R.drawable.select);
			iv_weixin.setImageResource(R.drawable.no_select);
			break;
		case R.id.ll_weixin:
			isZhiFuBao = false;
			iv_zhifubao.setImageResource(R.drawable.no_select);
			iv_weixin.setImageResource(R.drawable.select);
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
