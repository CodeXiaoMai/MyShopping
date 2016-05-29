package com.xiaomai.shopping.module;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;

public class WebActivity extends BaseActivity {

	private View taobao;
	private View tianmao;
	private View vip;
	private View onehaodian;
	private View youlu;
	private View dangdang;
	private View tongcheng;
	private View suning;

	private static final String TAOBAO = "https://ai.m.taobao.com/index.html?pid=mm_32549094_7052631_23486504";
	private static final String TIANMAO = "http://jx.tmall.com/?ali_trackid=2:mm_26632357_8426500_28318607:1464529428_265_1500964391&e=NTihrqQOUfpw4vFB6t2Z2iperVdZeJviK7Vc7tFgwiFRAdhuF14FMdG1qk4zRQOZt4hWD5k2kjPy80H7gHL6a4ddreqVkCMUpBN_LcdyCX4-z0jsbgbe5aUuZxIcp9pfcV_yRUpq-QyOTe7uzUz56eup4Whmf9f1vLZA9qmklo3eClVzOscp1gF-oVVa2fkXVZirzcuwtvwPEuRXsM6b3g&type=2&tkFlag=1";
	private static final String VIP = "http://m.vip.com/?f=mxmgge";
	private static final String ONENUMBER = "http://m.yhd.com/1?tracker_u=104876243381";
	private static final String YOULU = "http://m.youlu.net/?fromType=pc";
	private static final String DANGDANG = "http://m.dangdang.com/?ref=mibrowsergs&back=true";
	private static final String TONGCHENG = "http://m.58.com/zjk/?utm_source=market&spm=b-31580022738699-me-f-807.xiaomi_gg";
	private static final String SUNINGYIGOU = "http://m.suning.com/?utm_source=yd-uc&utm_medium=jptb&utm_campaign=730";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("世外\"淘园\"");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		taobao = findViewById(R.id.taobao);
		tianmao = findViewById(R.id.tmao);
		vip = findViewById(R.id.vph);
		onehaodian = findViewById(R.id.yihaodian);
		youlu = findViewById(R.id.youlu);
		dangdang = findViewById(R.id.dangdang);
		tongcheng = findViewById(R.id.tc);
		suning = findViewById(R.id.sning);

		setOnClick(back, taobao, tianmao, vip, onehaodian, youlu, dangdang,
				tongcheng, suning);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, MoreActivity.class);
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		case R.id.taobao:
			intent.putExtra("uri", TAOBAO);
			startActivity(intent);
			break;
		case R.id.tmao:
			intent.putExtra("uri", TIANMAO);
			startActivity(intent);
			break;

		case R.id.vph:
			intent.putExtra("uri", VIP);
			startActivity(intent);
			break;
		case R.id.yihaodian:
			intent.putExtra("uri", ONENUMBER);
			startActivity(intent);
			break;

		case R.id.dangdang:
			intent.putExtra("uri", DANGDANG);
			startActivity(intent);
			break;
		case R.id.youlu:
			intent.putExtra("uri", YOULU);
			startActivity(intent);
			break;

		case R.id.tc:
			intent.putExtra("uri", TONGCHENG);
			startActivity(intent);
			break;
		case R.id.sning:
			intent.putExtra("uri", SUNINGYIGOU);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
