package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Collection;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.MyDialog;
import com.xiaomai.shopping.view.RefreshListView;
import com.xiaomai.shopping.view.RefreshListView.OnRefreshListener;

/**
 * 我的收藏页面
 * 
 * @author XiaoMai
 *
 */
public class WoDeShouCangActivity extends BaseActivity implements
		OnRefreshListener {

	private View back;
	private TextView title;
	private View share;

	private Context context;

	// 我的收藏
	private RefreshListView lv_shoucang;
	private MyAdapter adapter;
	private List<Collection> list_shoucang = new ArrayList<>();
	private List<Collection> list_temp;
	private BmobQuery<Collection> bmobQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wodeshoucang);
		initView();
		checkNetWorkState();
	}

	private void initView() {
		context = this;
		bmobQuery = new BmobQuery<Collection>();
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("我的收藏");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		lv_shoucang = (RefreshListView) findViewById(R.id.listView);
		lv_shoucang.setOnRefreshListener(this);
		adapter = new MyAdapter();
		adapter.addList(list_shoucang);
		lv_shoucang.setAdapter(adapter);
		setOnClick(back);
	}

	@Override
	public void loadData() {
		User user = BmobUser.getCurrentUser(context, User.class);
		if (user != null) {
			bmobQuery.setLimit(Utils.REQUEST_COUNT);
			bmobQuery.setSkip(list_shoucang.size());
			bmobQuery.addWhereEqualTo("userId", user.getObjectId());
			bmobQuery.findObjects(context, new FindListener<Collection>() {

				@Override
				public void onSuccess(List<Collection> arg0) {
					if (list_shoucang.size() == 0) {
						if (arg0.size() == 0) {
							showToast("您的藏宝阁是空的！");
							return;
						} else {
							list_shoucang = arg0;
						}
					} else {
						if (arg0.size() == 0) {
							showToast("没有更多数据");
							return;
						}
						list_shoucang.addAll(arg0);
					}
					adapter.addList(list_shoucang);
					adapter.notifyDataSetChanged();
				}

				@Override
				public void onError(int arg0, String arg1) {
					showErrorToast(arg0, arg1);
					showLog("收藏", arg0, arg1);
				}
			});
		}
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

		private List<Collection> list;

		public void addList(List<Collection> list) {
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
				view = View.inflate(context, R.layout.item_shoucang, null);
				holder = new ViewHolder();
				holder.iv_image = (ImageView) view.findViewById(R.id.iv_image);
				holder.tv_name = (TextView) view
						.findViewById(R.id.shoucang_tv_title);
				holder.tv_price = (TextView) view
						.findViewById(R.id.shoucang_tv_price);
				holder.bt_quxiaoshouchu = (Button) view
						.findViewById(R.id.bt_quxiaoshouchu);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			final Collection collection = list.get(position);
			final String uri = collection.getImageUri();
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
			holder.tv_name.setText(collection.getGoodsName());
			holder.tv_price.setText(collection.getGoodsPrice());
			holder.bt_quxiaoshouchu
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							MyDialog.showDialog(context, "提示", "确认取消收藏？",
									new OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
											Collection coll = new Collection();
											coll.setObjectId(collection
													.getObjectId());
											coll.delete(context,
													new DeleteListener() {

														@Override
														public void onSuccess() {
															list_shoucang
																	.remove(position);
															adapter.addList(list_shoucang);
															adapter.notifyDataSetChanged();
														}

														@Override
														public void onFailure(
																int arg0,
																String arg1) {
															showErrorToast(
																	arg0, arg1);
															showLog("取消收藏",
																	arg0, arg1);
														}
													});
										}
									}, null);
						}
					});
			return view;
		}

		private class ViewHolder {
			ImageView iv_image;
			TextView tv_name;
			TextView tv_price;
			Button bt_quxiaoshouchu;
		}
	}

	@Override
	public void pullDownRefresh() {
		list_temp = list_shoucang;
		adapter.addList(list_temp);
		list_shoucang = new ArrayList<Collection>();
		loadData();
	}

	@Override
	public void pullUpLoadMore() {
		loadData();
	}

}
