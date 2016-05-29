package com.xiaomai.shopping.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.adapter.MyFragmentAdapter;
import com.xiaomai.shopping.base.BaseFragment;
import com.xiaomai.shopping.bean.Ad;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.module.FenLeiActivity;
import com.xiaomai.shopping.module.ShangPinXiangQingActivity;
import com.xiaomai.shopping.module.WebActivity;
import com.xiaomai.shopping.utils.NetWorkUtil;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;
import com.xiaomai.shopping.utils.StateCode;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.MyDialog;
import com.xiaomai.shopping.view.MyGridView;

/**
 * 首页
 * 
 * @author XiaoMai
 *
 */
public class ShouYeFragment extends BaseFragment implements TextWatcher,
		OnEditorActionListener, OnPageChangeListener,
		OnRefreshListener2<ScrollView> {

	private View more;

	private PullToRefreshScrollView scrollView;
	// 加载全部
	private static final int LOAD_GOODS = 1;
	// 加载搜索
	private static final int LOAD_SEARCH = 2;
	// 当前状态
	private int currentState = LOAD_GOODS;
	protected static final long AUTO_PLAY_TIME = 5000;
	private EditText et_search;
	private View quxiao;
	// 图片轮播
	private ViewPager viewPager;
	// private TextView tv_intro;
	private LinearLayout dotLayout;
	// private ViewPagerAdapter adapter_viewPager;
	private List<Ad> list_ad;
	private MyFragmentAdapter pagerAdapter;

	private MyGridView gv_shangpin;
	private int[] images = { R.drawable.bicycle, R.drawable.shouji,
			R.drawable.soccerball, R.drawable.book, R.drawable.cloth,
			R.drawable.more };
	private String[] names = { "交通工具", "电子产品", "体育器材", "学习用品", "衣帽鞋子", "其他" };
	private MyAdapter adapter;
	private List<Goods> list;
	// 默认加载的商品
	private List<Goods> list_goods;
	// 用户搜索的商品
	private List<Goods> list_search_result;
	// private List<Goods> list_temp;

	private GridView gv_fenlei;
	private SimpleAdapter simple_adapter;
	private List<Map<String, Object>> data;

	// 加载全部图片
	private ImageLoader imageloader;
	// 长按加载图片
	private ImageLoader loader;

	/**
	 * handler定时任务
	 */

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem((viewPager.getCurrentItem() + 1)
					% list_ad.size());
			myHandler.sendEmptyMessageDelayed(0, AUTO_PLAY_TIME);
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_shouye, container, false);
		initView(view);
		checkNetWorkState();
		loadPager();
		return view;
	}

	/**
	 * 检查网络状态
	 */
	public void checkNetWorkState() {

		boolean shengLiuLiang = SharedPrenerencesUtil.getShengLiuLiang(context);
		if (NetWorkUtil.isNetwork(context)
				&& NetWorkUtil.isMobileNetWork(context) && !shengLiuLiang) {
			MyDialog.showDialog(context, "提示",
					"您现在是非WIFI状态，将消耗数据，为了避免不必要的损失，建议您开启省流量模式！",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SharedPrenerencesUtil.setShengLiuLiang(context,
									true);
							Toast.makeText(context, "成功开启省流量模式",
									Toast.LENGTH_SHORT).show();
							showDialog("数据加载中");
							loadData();
						}
					}, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							loadData();
							showDialog("数据加载中");
						}
					});
		} else {
			loadData();
			showDialog("数据加载中");
		}
	}

	@Override
	public void loadData() {
		boolean shengLiuLiang = SharedPrenerencesUtil.getShengLiuLiang(context);
		// 如果是数据状态，并且未开启省流量模式，或者是Wifi状态就加载图片，否则不加载图片
		if (!NetWorkUtil.isNetwork(context, false)) {
			scrollView.onRefreshComplete();
			hideDialog();
			return;
		}
		/*
		 * if (!shengLiuLiang && NetWorkUtil.isMobileNetWork(context) ||
		 * NetWorkUtil.isWifiNetWork(context)) { imageloader =
		 * ImageLoader.getInstance();
		 * imageloader.init(ImageLoaderConfiguration.createDefault(context)); }
		 * else { imageloader = null; }
		 */
		if (!(shengLiuLiang && NetWorkUtil.isMobileNetWork(context))) {
			imageloader = ImageLoader.getInstance();
			imageloader.init(ImageLoaderConfiguration.createDefault(context));
		} else {
			imageloader = null;
		}
		loadGoods();
	}

	private void initView(View view) {
		context = getContext();

		more = view.findViewById(R.id.more);
		scrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		scrollView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		scrollView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"加载中...");
		scrollView.getLoadingLayoutProxy(false, true).setReleaseLabel("释放加载");
		scrollView.setOnRefreshListener(this);
		loader = ImageLoader.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(context));
		viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		viewPager.addOnPageChangeListener(this);
		list_ad = new ArrayList<Ad>();
		// tv_intro = (TextView) view.findViewById(R.id.tv_intro);
		dotLayout = (LinearLayout) view.findViewById(R.id.dot_layout);
		gv_fenlei = (GridView) view.findViewById(R.id.gv_fenlei);
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 6; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("image_url", images[i]);
			map.put("name", names[i]);
			data.add(map);
		}
		simple_adapter = new SimpleAdapter(getContext(), data,
				R.layout.item_fenlei, new String[] { "image_url", "name" },
				new int[] { R.id.item_fenlei_image, R.id.item_fenlei_name });
		gv_fenlei.setAdapter(simple_adapter);
		gv_fenlei.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, FenLeiActivity.class);
				switch (position) {
				case 0:
					intent.putExtra("type", names[0]);
					break;
				case 1:
					intent.putExtra("type", names[1]);
					break;
				case 2:
					intent.putExtra("type", names[2]);
					break;
				case 3:
					intent.putExtra("type", names[3]);
					break;
				case 4:
					intent.putExtra("type", names[4]);
					break;
				case 5:
					intent.putExtra("type", names[5]);
					break;
				}
				startActivity(intent);
			}
		});
		Utils.setHeight(simple_adapter, gv_fenlei, 5);

		list_goods = new ArrayList<Goods>();
		list_search_result = new ArrayList<Goods>();
		adapter = new MyAdapter();
		list = new ArrayList<Goods>();
		adapter.setList(list);
		gv_shangpin = (MyGridView) view.findViewById(R.id.gv_shangpin);
		gv_shangpin.setAdapter(adapter);
		View emptyView = view.findViewById(R.id.emptyView);
		gv_shangpin.setEmptyView(emptyView);

		Utils.setHeight(adapter, gv_shangpin, 2);

		quxiao = view.findViewById(R.id.shouye_iv_quxiao);

		et_search = (EditText) view.findViewById(R.id.shouye_et_search);
		et_search.addTextChangedListener(this);
		et_search.setOnEditorActionListener(this);

		setOnClick(quxiao, more);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shouye_iv_quxiao:
			et_search.setText("");
			currentState = LOAD_GOODS;
			list = list_goods;
			adapter.notifyDataSetChanged();
			break;
		case R.id.more:
			Intent intent = new Intent(context, WebActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private class MyAdapter extends BaseAdapter {

		private List<Goods> list;

		public void setList(List<Goods> list) {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			final Goods goods;
			View view = convertView;
			if (view == null) {
				view = View.inflate(getContext(), R.layout.item_shangpin, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) view
						.findViewById(R.id.shangpin_title);
				holder.tv_address = (TextView) view
						.findViewById(R.id.shangpin_jiaoyididian);
				holder.tv_price = (TextView) view
						.findViewById(R.id.shangpin_price);
				holder.tv_want = (TextView) view
						.findViewById(R.id.shangpin_xiangmai);
				holder.iv_image = (ImageView) view
						.findViewById(R.id.shangpin_image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			goods = list.get(position);
			if (imageloader != null) {
				imageloader.displayImage(goods.getImages().get(0),
						holder.iv_image);
			} else {
				holder.iv_image.setImageResource(R.drawable.chang_an1);
				holder.iv_image
						.setOnLongClickListener(new View.OnLongClickListener() {

							@Override
							public boolean onLongClick(View v) {
								holder.iv_image
										.setImageResource(R.drawable.tupian_jiazaizhong1);
								loader.displayImage(goods.getImages().get(0),
										holder.iv_image);
								return true;
							}
						});
				holder.iv_image.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						holder.tv_name.setTextColor(0xffCBCACA);
						BmobQuery<Goods> bmobQuery = new BmobQuery<Goods>();
						bmobQuery.getObject(context, goods.getObjectId(),
								new GetListener<Goods>() {

									@Override
									public void onSuccess(Goods arg0) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(
												getContext(),
												ShangPinXiangQingActivity.class);
										intent.putExtra("goods", arg0);
										startActivity(intent);

									}

									@Override
									public void onFailure(int arg0, String arg1) {
										// TODO Auto-generated method stub
										showErrorToast(arg0, arg1);
										showLog("商品", arg0, arg1);
									}
								});
					}
				});
			}
			holder.tv_name.setText(goods.getTitle());
			holder.tv_address.setText(goods.getAddress());
			holder.tv_price.setText("￥" + goods.getPrice());
			holder.tv_want.setText(goods.getWant() + "人想要");
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					holder.tv_name.setTextColor(0xffCBCACA);
					Intent intent = new Intent(getContext(),
							ShangPinXiangQingActivity.class);
					intent.putExtra("goods", goods);
					startActivity(intent);

				}
			});
			return view;
		}

	}

	private class ViewHolder {
		ImageView iv_image;
		TextView tv_name;
		TextView tv_address;
		TextView tv_price;
		TextView tv_want;
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 当输入框中的内容不为空时，显示“取消”，否则隐藏“取消”。
		if (s.toString().length() > 0) {
			quxiao.setVisibility(View.VISIBLE);
			more.setVisibility(View.GONE);
		} else {
			currentState = LOAD_GOODS;
			quxiao.setVisibility(View.INVISIBLE);
			more.setVisibility(View.VISIBLE);
			list = list_goods;
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
		currentState = LOAD_SEARCH;
		list_search_result = new ArrayList<Goods>();
		loadSearchGoods(v);
		return true;
	}

	private void loadSearchGoods(TextView v) {
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		boolean shengLiuLiang = SharedPrenerencesUtil.getShengLiuLiang(context);
		if (!shengLiuLiang && NetWorkUtil.isMobileNetWork(context)
				|| NetWorkUtil.isWifiNetWork(context)) {
			imageloader = ImageLoader.getInstance();
			imageloader.init(ImageLoaderConfiguration
					.createDefault(getContext()));
		}
		String value = v.getText().toString().trim();
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.setLimit(Utils.REQUEST_COUNT);
		query.order("-updatedAt");
		query.setSkip(list_search_result.size());
		query.addWhereContains("title", value);
		query.findObjects(context, new FindListener<Goods>() {

			@Override
			public void onSuccess(List<Goods> arg0) {
				if (list_search_result.size() == 0) {
					if (arg0.size() == 0) {
						showToast("没有您要搜索的商品");
						scrollView.onRefreshComplete();
						return;
					} else {
						list_search_result = arg0;
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
						scrollView.onRefreshComplete();
						return;
					} else {
						list_search_result.addAll(arg0);
					}
				}
				showLog("", 1, "查询成功");
				list = list_search_result;
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				scrollView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				showErrorToast(arg0, arg1);
				showLog("shouye_goods", arg0, arg1);
			}
		});
	}

	private void loadGoods() {
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.order("-updatedAt");
		query.addWhereEqualTo("state", StateCode.GOODS_OK);
		query.addWhereGreaterThan("count", 0);
		query.setLimit(Utils.REQUEST_COUNT);
		query.setSkip(list_goods.size());
		query.findObjects(context, new FindListener<Goods>() {

			@Override
			public void onSuccess(List<Goods> arg0) {
				hideDialog();
				if (list.size() == 0) {
					if (arg0.size() == 0) {
						showToast("没有任何数据！");
						scrollView.onRefreshComplete();
						return;
					} else {
						list_goods = arg0;
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
						scrollView.onRefreshComplete();
						return;
					} else {
						list_goods.addAll(arg0);
					}
				}
				list = list_goods;
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				scrollView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				scrollView.onRefreshComplete();
				hideDialog();
				showErrorToast(arg0, arg1);
				showLog("shouye_goods", arg0, arg1);
			}
		});
	}

	private void loadPager() {
		BmobQuery<Ad> bmobQuery = new BmobQuery<Ad>();
		bmobQuery.findObjects(context, new FindListener<Ad>() {

			@Override
			public void onError(int arg0, String arg1) {

			}

			@Override
			public void onSuccess(List<Ad> arg0) {
				Log.i("广告加载成功！", arg0.size() + "");
				for (Ad ad : arg0) {
					Log.i("ad", ad.toString());
				}
				list_ad = arg0;
				initDots();

			}
		});
	}

	/**
	 * 更新文本
	 */
	private void updateIntroAndDot() {
		int currentPage = viewPager.getCurrentItem() % list_ad.size();
		// tv_intro.setText(list_ad.get(currentPage).getIntroduce());

		for (int i = 0; i < dotLayout.getChildCount(); i++) {
			dotLayout.getChildAt(i).setEnabled(i == currentPage);// 设置setEnabled为true的话
			// 在选择器里面就会对应的使用白色颜色
		}
	}

	/**
	 * 初始化dot
	 */
	private void initDots() {
		// 为了防止每更新一次都发送一次handler导致轮播混乱
		int dotSize = dotLayout.getChildCount();
		int num = list_ad.size() - dotSize;

		if (num > 0) {
			for (int i = 0; i < num; i++) {
				View view = new View(context);
				LayoutParams params = new LayoutParams(15, 15);
				if (i != 0) {
					params.leftMargin = 10;
				}
				view.setLayoutParams(params);
				view.setBackgroundResource(R.drawable.selector_dot);
				dotLayout.addView(view);
			}
			if (dotSize == 0) {
				// adapter_viewPager = new ViewPagerAdapter(context, list_ad,
				// imageloader);
				List<String> images = new ArrayList<String>();
				for (Ad ad : list_ad) {
					images.add(ad.getImage_url());
				}
				pagerAdapter = new MyFragmentAdapter(getFragmentManager(),
						images, context, false);
				// viewPager.setAdapter(adapter_viewPager);
				viewPager.setAdapter(pagerAdapter);
				myHandler.sendEmptyMessageDelayed(0, AUTO_PLAY_TIME);
				updateIntroAndDot();
			}
		} else if (num < 0) {
			for (int i = 0; i > num; i--) {
				dotLayout.removeViewAt(-i);
			}
		}

		// for (int i = 0; i < list_ad.size(); i++) {
		// View view = new View(context);
		// LayoutParams params = new LayoutParams(8, 8);
		// if (i != 0) {
		// params.leftMargin = 5;
		// }
		// view.setLayoutParams(params);
		// view.setBackgroundResource(R.drawable.selector_dot);
		// dotLayout.addView(view);
		// }
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// Log.i("pager", "arg0:" + arg0 + ",arg1:" + arg1 + ",arg2:" + arg2);
		if (arg0 == list_ad.size() - 1) {

		}
	}

	@Override
	public void onPageSelected(int arg0) {
		// if (arg0 == list_ad.size() - 1) {
		// viewPager.setCurrentItem(0);
		// }
		updateIntroAndDot();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (currentState == LOAD_GOODS) {
			list_goods = new ArrayList<Goods>();
			list = list_goods;
			loadData();
		} else {
			list_search_result = new ArrayList<Goods>();
			list = list_search_result;
			loadSearchGoods(et_search);
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (currentState == LOAD_GOODS) {
			loadGoods();
		} else {
			loadSearchGoods(et_search);
		}
	}

}
