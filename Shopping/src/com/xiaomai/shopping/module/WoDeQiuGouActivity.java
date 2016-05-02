package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.YiFaBu;

/**
 * 求购页面
 * 
 * @author XiaoMai
 *
 */
public class WoDeQiuGouActivity extends BaseActivity {

	private View back;
	private TextView title;
	private View share;

	// 已经发布的求购
	private ListView lv_yifabuqiugou;
	private MyAdapter adapter;
	private List<YiFaBu> list_yifabuqiugou = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wodeqiugou);
		initView();
	}

	private void initView() {
		// 隐藏返回
		back = findViewById(R.id.title_back);
		// 设置标题
		title = (TextView) findViewById(R.id.title_title);
		title.setText("求购中心");
		// 分享
		share = findViewById(R.id.title_share);

		lv_yifabuqiugou = (ListView) findViewById(R.id.lv_yifabuqiugou);
		adapter = new MyAdapter();
		lv_yifabuqiugou.setAdapter(adapter);
		setOnClick(back, share);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.title_share:
			startActivity(new Intent(this, FaBuQiuGouActivity.class));
			break;
		default:
			break;
		}
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder holder;
			if (view == null) {
				view = View.inflate(WoDeQiuGouActivity.this,
						R.layout.item_qiugou, null);
				holder = new ViewHolder();
				holder.iv_image = (ImageView) view.findViewById(R.id.iv_image);
				holder.tv_name = (TextView) view
						.findViewById(R.id.yifabu_tv_title);
				holder.tv_state = (TextView) view
						.findViewById(R.id.yifabu_tv_state);
				holder.bt_edit = (Button) view.findViewById(R.id.bt_edit);
				holder.bt_xiajia = (Button) view.findViewById(R.id.bt_xiajia);
				holder.bt_shouchu = (Button) view.findViewById(R.id.bt_shouchu);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			return view;
		}

		private class ViewHolder {
			ImageView iv_image;
			TextView tv_name;
			TextView tv_state;
			Button bt_edit;
			Button bt_xiajia;
			Button bt_shouchu;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}
}
