package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Score;
import com.xiaomai.shopping.bean.User;

/**
 * 我的积分
 * 
 * @author XiaoMai
 *
 */
public class WoDeJiFenActivity extends BaseActivity {

	private View back;
	private TextView title;
	private View share;

	private ListView listView;
	private MyAdapter adapter;
	private List<Score> list;

	private TextView tv_score;
	private TextView tv_level;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wodejifen);
		initView();
		loadData();
	}

	private void initView() {
		context = this;
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("我的积分");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		tv_score = (TextView) findViewById(R.id.tv_score);
		tv_level = (TextView) findViewById(R.id.tv_level);

		list = new ArrayList<Score>();
		listView = (ListView) findViewById(R.id.listView);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
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

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
			View view = View.inflate(context, R.layout.item_jifen, null);
			View top = view.findViewById(R.id.top);
			View bottom = view.findViewById(R.id.bottom);
			TextView tv_jifen = (TextView) view.findViewById(R.id.tv_jifen);
			TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
			TextView tv_leixing = (TextView) view.findViewById(R.id.tv_leixing);

			Score score = list.get(position);
			tv_jifen.setText("+" + score.getScore());
			tv_leixing.setText(score.getDesc());
			tv_time.setText(score.getUpdatedAt());
			if (position == 0) {
				top.setVisibility(View.INVISIBLE);
			} else if (position == list.size()-1) {
				bottom.setVisibility(View.INVISIBLE);
			}
			return view;
		}

	}

	@Override
	public void loadData() {
		User user = getCurrentUser();
		Integer score = user.getScore();
		tv_score.setText(score + "");
		tv_level.setText(getLevel(score));
		BmobQuery<Score> bmobQuery = new BmobQuery<Score>();
		bmobQuery.addWhereEqualTo("userId", user.getObjectId());
		bmobQuery.order("-updatedAt");
		bmobQuery.findObjects(context, new FindListener<Score>() {

			@Override
			public void onSuccess(List<Score> arg0) {
				if (arg0 != null) {
					list = arg0;
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				showErrorToast(arg0, arg1);
				showLog("获取积分", arg0, arg1);
			}
		});
	}

	private String getLevel(int score) {
		return (score / 100) + 1 + "";
	}
}
