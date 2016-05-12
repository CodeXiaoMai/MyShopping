package com.xiaomai.shoppingmanager;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shoppingmanager.base.BaseActivity;
import com.xiaomai.shoppingmanager.bean.Ad;

public class AdManage extends BaseActivity {

	private ImageView iv1, iv2, iv3, iv4, iv5;
	private Button bt_commit;
	private List<Ad> images;
	private ImageView iv_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_manage);
		initView();
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("广告管理");

		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv4 = (ImageView) findViewById(R.id.iv4);
		iv5 = (ImageView) findViewById(R.id.iv5);

		iv_add = (ImageView) findViewById(R.id.add_image);
		bt_commit = (Button) findViewById(R.id.bt_commit);
		setOnClick(back, bt_commit, iv_add);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.bt_commit:

			break;
		default:
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		BmobQuery<Ad> bmobQuery = new BmobQuery<Ad>();
		bmobQuery.findObjects(context, new FindListener<Ad>() {

			@Override
			public void onSuccess(List<Ad> arg0) {
				// TODO Auto-generated method stub
				images = arg0;
				download();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void download() {
		// TODO Auto-generated method stub
		imageloader = ImageLoader.getInstance();
		imageloader.init(ImageLoaderConfiguration.createDefault(this));
		switch (images.size()) {
		case 5:
			iv5.setVisibility(View.VISIBLE);
			imageloader.displayImage(images.get(4).getImage_url(), iv5);
		case 4:
			iv4.setVisibility(View.VISIBLE);
			imageloader.displayImage(images.get(3).getImage_url(), iv4);
		case 3:
			iv3.setVisibility(View.VISIBLE);
			imageloader.displayImage(images.get(2).getImage_url(), iv3);
		case 2:
			iv2.setVisibility(View.VISIBLE);
			imageloader.displayImage(images.get(1).getImage_url(), iv2);
		case 1:
			imageloader.displayImage(images.get(0).getImage_url(), iv1);
			break;
		}
	}

}
