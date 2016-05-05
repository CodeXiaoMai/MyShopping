package com.xiaomai.shopping.module;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geren_ziliao);
		initView();
	}

	private void initView() {
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("个人资料");
		share = (View) findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		setOnClick(back);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

}
