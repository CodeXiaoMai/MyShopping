package com.xiaomai.shopping.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.LazyFragment;
import com.xiaomai.shopping.bean.IWant;
import com.xiaomai.shopping.utils.DES;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.MyDialog;

/**
 * 求购页面
 * 
 * @author XiaoMai
 *
 */
public class QiuGouFragment extends LazyFragment implements
		OnRefreshListener2<ScrollView> {

	private boolean isPrepared;
	private boolean isFirstTime = true;

	private View back;
	private TextView title;
	private View share;

	private PullToRefreshScrollView scrollView;
	private ListView listView;
	private List<IWant> list_qiugou;
	private List<IWant> list_temp;
	private MyAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_qiugou, container, false);
		initView(view);
		isPrepared = true;
		lazyLoad();
		return view;
	}

	private void initView(View view) {
		context = getContext();
		// 隐藏返回
		back = view.findViewById(R.id.title_back);
		back.setVisibility(View.GONE);
		// 设置标题
		title = (TextView) view.findViewById(R.id.title_title);
		title.setText("求购列表");
		// 隐藏分享
		share = view.findViewById(R.id.title_share);
		share.setVisibility(View.GONE);

		scrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		scrollView.setOnRefreshListener(this);
		listView = (ListView) view.findViewById(android.R.id.list);
		list_qiugou = new ArrayList<IWant>();
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				MyDialog.showDialog(context, "提示信息", "您有Ta需要的宝贝？快联系Ta吧！",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								String phone = (String) adapter
										.getItem(position);
								Intent intent = new Intent(Intent.ACTION_DIAL);
								intent.setData(Uri.parse("tel:" + phone));
								if (intent.resolveActivity(context
										.getPackageManager()) != null) {
									startActivity(intent);
								}
							}
						}, null);
			}
		});

		View emptyView = view.findViewById(R.id.emptyView);
		listView.setEmptyView(emptyView);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadData() {
		BmobQuery<IWant> bmobQuery = new BmobQuery<IWant>();
		bmobQuery.order("-updateAt");
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.addWhereEqualTo("state", IWant.STATE_NORMAL);
		bmobQuery.setSkip(list_qiugou.size());
		bmobQuery.findObjects(context, new FindListener<IWant>() {

			@Override
			public void onSuccess(List<IWant> arg0) {
				hideDialog();
				if (list_qiugou.size() == 0) {
					if (arg0.size() == 0) {
						showToast("没有数据");
						scrollView.onRefreshComplete();
						return;
					} else {
						list_qiugou = arg0;
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
						scrollView.onRefreshComplete();
						return;
					}
					list_qiugou.addAll(arg0);
				}
				adapter.setList(list_qiugou);
				adapter.notifyDataSetChanged();
				scrollView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				scrollView.onRefreshComplete();
				hideDialog();
				showErrorToast(arg0, arg1);
				showLog("我的求购", arg0, arg1);
			}
		});
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
			return list.get(position).getPhone();
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
				view = View.inflate(context, R.layout.item_qiugou, null);
				holder = new ViewHolder();
				holder.iv_head = (ImageView) view.findViewById(R.id.iv_head);
				holder.tv_name = (TextView) view
						.findViewById(R.id.qiugou_tv_name);
				holder.tv_title = (TextView) view
						.findViewById(R.id.qiugou_tv_title);
				holder.tv_desc = (TextView) view
						.findViewById(R.id.qiugou_tv_desc);
				holder.tv_price = (TextView) view
						.findViewById(R.id.qiugou_tv_price);
				holder.tv_date = (TextView) view
						.findViewById(R.id.qiugou_tv_date);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			final IWant iWant = list.get(position);
			if (imageloader != null) {
				imageloader.displayImage(iWant.getUserImage(), holder.iv_head);
			}

			holder.tv_name.setText(DES.decryptDES(iWant.getUserName(),
					Utils.ENCRYPT_KEY));
			holder.tv_title.setText("求购:" + iWant.getTitle());
			holder.tv_desc.setText("描述：\n\t\t" + iWant.getDesc());
			holder.tv_price.setText(iWant.getMinPrice() + " - "
					+ iWant.getMaxPrice());
			holder.tv_date.setText(iWant.getUpdatedAt());
			return view;
		}

		private class ViewHolder {
			ImageView iv_head;
			TextView tv_name;
			TextView tv_title;
			TextView tv_desc;
			TextView tv_date;
			TextView tv_price;
		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		list_temp = list_qiugou;
		adapter.setList(list_temp);
		list_qiugou = new ArrayList<IWant>();
		checkNetWorkState();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		checkNetWorkState();
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		if (isPrepared && isVisible && isFirstTime) {
			showDialog("数据加载中");
			isFirstTime = false;
			checkNetWorkState();
		}
	}

}
