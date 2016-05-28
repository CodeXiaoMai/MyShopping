package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.IWant;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.MyDialog;
import com.xiaomai.shopping.view.RefreshListView;
import com.xiaomai.shopping.view.RefreshListView.OnRefreshListener;

/**
 * 求购页面
 * 
 * @author XiaoMai
 *
 */
public class WoDeQiuGouActivity extends BaseActivity implements
		OnRefreshListener {

	private View back;
	private TextView title;
	private View share;

	// 已经发布的求购
	private RefreshListView lv_yifabuqiugou;
	private MyAdapter adapter;
	private List<IWant> list_yifabuqiugou = new ArrayList<>();
	private List<IWant> list_temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wodeqiugou);
		initView();
		loadData();
	}

	private void initView() {
		// 隐藏返回
		back = findViewById(R.id.title_back);
		// 设置标题
		title = (TextView) findViewById(R.id.title_title);
		title.setText("求购中心");
		// 分享
		share = findViewById(R.id.title_share);

		lv_yifabuqiugou = (RefreshListView) findViewById(R.id.lv_yifabuqiugou);
		lv_yifabuqiugou.setOnRefreshListener(this);
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

		private List<IWant> list;

		public void setList(List<IWant> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			}
			return 0;
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
			ViewHolder holder;
			if (view == null) {
				view = View.inflate(WoDeQiuGouActivity.this,
						R.layout.item_wode_qiugou, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) view
						.findViewById(R.id.qiugou_tv_title);
				holder.tv_state = (TextView) view.findViewById(R.id.tv_state);
				holder.tv_price = (TextView) view
						.findViewById(R.id.qiugou_tv_price);
				holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
				holder.bt_quxiao = (Button) view
						.findViewById(R.id.qiugou_bt_quxiao);
				holder.bt_edit = (Button) view.findViewById(R.id.bt_edit);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			final IWant iWant = list.get(position);
			holder.tv_name.setText(iWant.getTitle());
			int state = iWant.getState();
			String zt = "";
			switch (state) {
			case IWant.STATE_DAISHENHE:
				zt = "待审核";
				break;
			case IWant.STATE_NORMAL:
				zt = "审核通过";
				break;
			case IWant.STATE_SHENHE_SHIBAI:
				zt = "审核失败";
				break;
			}
			holder.tv_state.setText(zt);
			holder.tv_time.setText(iWant.getCreatedAt());
			holder.tv_price.setText(iWant.getMinPrice() + " - "
					+ iWant.getMaxPrice());
			holder.bt_quxiao.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					MyDialog.showDialog(context, "提示", "确认取消您的求购吗",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									quXiao();
									dialog.dismiss();
								}

							}, null);
				}

				private void quXiao() {
					iWant.delete(context, new DeleteListener() {

						@Override
						public void onSuccess() {
							showToast("取消求购成功");
							list.remove(position);
							adapter.notifyDataSetChanged();
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							showErrorToast(arg0, arg1);
							showLog("取消求购", arg0, arg1);
						}
					});
				}
			});
			holder.bt_edit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,
							UpdateWantActivity.class);
					intent.putExtra("want", iWant);
					startActivity(intent);
				}
			});
			return view;
		}

		private class ViewHolder {
			TextView tv_name;
			TextView tv_price;
			TextView tv_state;
			TextView tv_time;
			Button bt_quxiao;
			Button bt_edit;
		}
	}

	@Override
	public void loadData() {
		showDialog("数据加载中");
		User user = getCurrentUser();
		BmobQuery<IWant> bmobQuery = new BmobQuery<IWant>();
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.setSkip(list_yifabuqiugou.size());
		bmobQuery.order("-updatedAt");
		bmobQuery.addWhereEqualTo("userId", user.getObjectId());
		bmobQuery.findObjects(context, new FindListener<IWant>() {

			@Override
			public void onSuccess(List<IWant> arg0) {
				lv_yifabuqiugou.onRefreshFinish();
				hideDialog();
				if (list_yifabuqiugou.size() == 0) {
					if (arg0.size() == 0) {
						showToast("您还没有发布过任何求购！");
						return;
					} else {
						list_yifabuqiugou = arg0;
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
						return;
					}
					list_yifabuqiugou.addAll(arg0);
				}
				adapter.setList(list_yifabuqiugou);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int arg0, String arg1) {
				lv_yifabuqiugou.onRefreshFinish();
				hideDialog();
				showErrorToast(arg0, arg1);
				showLog("我的求购", arg0, arg1);
			}
		});
	}

	@Override
	public void pullDownRefresh() {
		list_temp = list_yifabuqiugou;
		if (list_temp != null) {
			adapter.setList(list_temp);
		}
		list_yifabuqiugou = new ArrayList<IWant>();
		loadData();
	}

	@Override
	public void pullUpLoadMore() {
		loadData();
	}
}
