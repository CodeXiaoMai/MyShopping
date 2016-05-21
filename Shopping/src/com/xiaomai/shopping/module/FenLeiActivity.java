package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.adapter.GoodsAdapter;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.utils.NetWorkUtil;
import com.xiaomai.shopping.utils.SharedPrenerencesUtil;
import com.xiaomai.shopping.utils.StateCode;
import com.xiaomai.shopping.utils.Utils;

public class FenLeiActivity extends BaseActivity implements TextWatcher,
		OnEditorActionListener {

	private PullToRefreshGridView listView;
	private GoodsAdapter adapter;
	private List<Goods> list;
	private TextView tv_time;
	private TextView tv_price;
	private EditText et_search;
	private View quxiao;
	// 加载全部
	private static final int LOAD_GOODS = 1;
	// 加载搜索
	private static final int LOAD_SEARCH = 2;
	// 当前状态
	private int currentState = LOAD_GOODS;

	// 默认加载的商品
	private List<Goods> list_goods;
	// 用户搜索的商品
	private List<Goods> list_search_result;

	private static final String TIME1 = "createdAt";
	private static final String TIME2 = "-createdAt";
	private static final String PRICE1 = "price";
	private static final String PRICE2 = "-price";

	private String order = TIME2;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fenlei);
		initView();
		type = getIntent().getStringExtra("type");
		title.setText(type);
		checkNetWorkState();
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		quxiao = findViewById(R.id.shouye_iv_quxiao);

		et_search = (EditText) findViewById(R.id.shouye_et_search);
		et_search.addTextChangedListener(this);
		et_search.setOnEditorActionListener(this);

		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_price = (TextView) findViewById(R.id.tv_price);
		list_search_result = new ArrayList<Goods>();
		list = new ArrayList<Goods>();
		adapter = new GoodsAdapter(list, context);
		listView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);
		listView.setAdapter(adapter);
		listView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
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
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				if (currentState == LOAD_GOODS) {
					loadData();
				} else {
					loadSearchGoods(et_search);
				}
			}
		});
		setOnClick(back, tv_time, tv_price, quxiao);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.shouye_iv_quxiao:
			et_search.setText("");
			currentState = LOAD_GOODS;
			list = list_goods;
			adapter.notifyDataSetChanged();
			break;
		case R.id.title_back:
			finish();
			break;
		case R.id.tv_time:
			order = (order.equals(TIME1)) ? TIME2 : TIME1;
			if (currentState == LOAD_GOODS) {
				list_goods = new ArrayList<Goods>();
				list = list_goods;
				loadData();
			} else {
				list_search_result = new ArrayList<Goods>();
				list = list_search_result;
				loadSearchGoods(et_search);
			}
			break;
		case R.id.tv_price:
			order = (order.equals(PRICE1)) ? PRICE2 : PRICE1;
			if (currentState == LOAD_GOODS) {
				list_goods = new ArrayList<Goods>();
				list = list_goods;
				loadData();
			} else {
				list_search_result = new ArrayList<Goods>();
				list = list_search_result;
				loadSearchGoods(et_search);
			}
			break;
		}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		showDialog("数据加载中...");
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.order(order);
		query.addWhereEqualTo("type", type);
		query.addWhereEqualTo("state", StateCode.GOODS_OK);
		query.addWhereGreaterThan("count", 0);
		query.setLimit(Utils.REQUEST_COUNT);
		query.setSkip(list.size());
		query.findObjects(context, new FindListener<Goods>() {

			@Override
			public void onSuccess(List<Goods> arg0) {
				// TODO Auto-generated method stub
				hideDialog();
				if (list.size() == 0) {
					adapter.setImageLoader(imageloader, loader);
					if (arg0.size() == 0) {
						showToast("没有任何数据！");
						listView.onRefreshComplete();
						return;
					} else {
						list_goods = arg0;
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
						listView.onRefreshComplete();
						return;
					} else {
						list_goods.addAll(arg0);
					}
				}
				list = list_goods;
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				hideDialog();
				showErrorToast(arg0, arg1);
				showLog("shouye_goods", arg0, arg1);
			}
		});
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 当输入框中的内容不为空时，显示“取消”，否则隐藏“取消”。
		if (s.toString().length() > 0) {
			quxiao.setVisibility(View.VISIBLE);
		} else {
			currentState = LOAD_GOODS;
			quxiao.setVisibility(View.INVISIBLE);
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
			imageloader.init(ImageLoaderConfiguration.createDefault(context));
		}
		String value = v.getText().toString().trim();
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.setLimit(Utils.REQUEST_COUNT);
		query.order(order);
		query.setSkip(list_search_result.size());
		query.addWhereContains("title", value);
		query.findObjects(context, new FindListener<Goods>() {

			@Override
			public void onSuccess(List<Goods> arg0) {
				if (list_search_result.size() == 0) {
					if (arg0.size() == 0) {
						showToast("没有您要搜索的商品");
						listView.onRefreshComplete();
						return;
					} else {
						list_search_result = arg0;
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
						listView.onRefreshComplete();
						return;
					} else {
						list_search_result.addAll(arg0);
					}
				}
				showLog("", 1, "查询成功");
				list = list_search_result;
				adapter.setList(list);
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				showErrorToast(arg0, arg1);
				showLog("shouye_goods", arg0, arg1);
			}
		});
	}
}
