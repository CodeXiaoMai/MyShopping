package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.NetWorkUtil;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.RefreshListView;
import com.xiaomai.shopping.view.RefreshListView.OnRefreshListener;

/**
 * 发布页面
 * 
 * @author XiaoMai
 *
 */
public class FaBuZhongXinActivity extends BaseActivity implements
		OnRefreshListener {

	private Context context;

	private View back;
	private TextView title;
	private View share;

	// 已经发布
	private RefreshListView lv_yifabu;
	private MyAdapter adapter;
	private List<Goods> list_yifabu;
	private List<Goods> list_temp;

	private BmobQuery<Goods> query;

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fabuzhongxin);
		initView();
		checkNetWorkState();
	}


	public void loadData() {
		if (user != null) {
			query.setLimit(Utils.REQUEST_COUNT);
			query.setSkip(list_yifabu.size());
			query.addWhereEqualTo("userId", user.getObjectId());
			query.findObjects(context, new FindListener<Goods>() {

				@Override
				public void onSuccess(List<Goods> arg0) {
					if (list_yifabu.size() == 0) {
						if (arg0.size() == 0) {
							showToast("您还没有发布过，赶快去发布吧！");
							return;
						} else {
							list_yifabu = arg0;
						}
					} else {
						if (arg0.size() == 0) {
							showToast("没有更多数据");
							return;
						}
						list_yifabu.addAll(arg0);
					}
					adapter.addList(list_yifabu);
					adapter.notifyDataSetChanged();
				}

				@Override
				public void onError(int arg0, String arg1) {
					showErrorToast(arg0, arg1);
					showLog("", arg0, arg1);
				}
			});
		} else {
			showToast("您还未登录，请先登录！");
			Intent intent = new Intent(context, LoginActivity.class);
			startActivity(intent);
		}

	}

	private void initView() {

		context = this;
		query = new BmobQuery<Goods>();
		user = BmobUser.getCurrentUser(context, User.class);
		// 隐藏返回
		back = findViewById(R.id.title_back);
		// 设置标题
		title = (TextView) findViewById(R.id.title_title);
		title.setText("发布中心");
		// 隐藏分享
		share = findViewById(R.id.title_share);

		lv_yifabu = (RefreshListView) findViewById(R.id.lv_yifabu);
		list_yifabu = new ArrayList<>();
		adapter = new MyAdapter();
		adapter.addList(list_yifabu);
		lv_yifabu.setAdapter(adapter);
		lv_yifabu.setOnRefreshListener(this);
		setOnClick(back, share);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.title_share:
			startActivity(new Intent(this, FaBuShangPinActivity.class));
			break;
		default:
			break;
		}
	}

	private class MyAdapter extends BaseAdapter {

		private List<Goods> list;

		public void addList(List<Goods> list) {
			this.list = list;
		}

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (view == null) {
				view = View.inflate(FaBuZhongXinActivity.this,
						R.layout.item_yifabu, null);
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
			Goods goods = list.get(position);

			String objectId = goods.getObjectId();
			final String uri = goods.getImages().get(0);
			String state = goods.getState();
			String title = goods.getTitle();
			holder.tv_name.setText(title);
			holder.tv_state.setText(state);
			if (imageloader != null) {
				imageloader.displayImage(uri, holder.iv_image);
			} else {
				holder.iv_image
						.setOnLongClickListener(new View.OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								loader.displayImage(uri, holder.iv_image);
								return true;
							}
						});
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
	public void pullDownRefresh() {
		list_temp = list_yifabu;
		adapter.addList(list_temp);
		list_yifabu = new ArrayList<Goods>();
		loadData();
	}

	@Override
	public void pullUpLoadMore() {
		loadData();
	}

}
