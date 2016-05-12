package com.xiaomai.shopping.module;

import com.xiaomai.shopping.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class AboutUsActivity extends Activity{

	private View back;
	private TextView tv_title;
	private View share;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		initView();
	}

	private void initView() {
		back = findViewById(R.id.title_back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title = (TextView)findViewById(R.id.title_title);
		tv_title.setText("关于我们");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);
	}
}
