package com.xiaomai.shopping.module;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseFragmentActivity;
import com.xiaomai.shopping.bean.Collection;
import com.xiaomai.shopping.bean.Comment;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.bean.Score;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.module.fragment.ImageFragment;
import com.xiaomai.shopping.utils.DES;
import com.xiaomai.shopping.utils.RequestCode;
import com.xiaomai.shopping.utils.ResultCode;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.MyListView;

public class ShangPinXiangQingActivity extends BaseFragmentActivity implements
		OnPageChangeListener, TextWatcher {

	private Context context;
	// 图片轮播
	private ViewPager viewPager;
	private List<ImageFragment> list_fragment;
	private MyFragmentAdapter fragment_adapter;
	// 页数指示
	private TextView tv_selector;
	private View back;
	private TextView title;
	private View share;

	// 宝贝名字
	private TextView tv_name;
	// 宝贝价格
	private TextView tv_price;
	// 剩余数量
	private TextView tv_count;
	// 已经卖出
	private TextView tv_maichu;
	// 收藏人数
	private TextView tv_shoucang;
	// 宝贝描述
	private TextView tv_desc;
	// 图片地址
	private List<String> images;

	// 评论
	private MyListView listView;
	private List<Comment> list_comment;
	private MyAdapter adapter;

	// 按钮集合
	private View ll_buttons;
	// 更多
	private ImageView iv_more;
	// 收藏
	private View bt_shoucang;
	private View iv_shoucang;
	private View tv_yishoucang;
	// 联系卖家
	private View bt_lianximaijia;
	// 想要
	private View bt_buy;
	// 评论
	private EditText et_comment;
	// 评论的字数
	private TextView tv_comment_length;
	// 发送
	private View iv_send;

	private Goods goods;

	private AlertDialog alertDialog;

	private PullToRefreshScrollView scrollView;
	private Collection collection;

	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shangpinxiangqing);
		initView();
		fillData();
	}

	private void fillData() {
		goods = (Goods) getIntent().getSerializableExtra("goods");
		fillGoods();
		fragment_adapter = new MyFragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(fragment_adapter);
		viewPager.addOnPageChangeListener(this);
		loadComment();
		checkCollection();
		loadCollectionCount();
	}

	private void fillGoods() {
		tv_name.setText(goods.getTitle());
		tv_price.setText(goods.getPrice());
		Integer remainCount = goods.getRemainCount();
		tv_count.setText("数量：" + remainCount);
		tv_maichu.setText("成交：" + (goods.getCount() - remainCount) + "笔");
		tv_desc.setText(goods.getContent());
		images = goods.getImages();
		// showToast(images.size() + "");
		tv_selector.setText("1/" + images.size());
	}

	// 查询收藏人数
	private void loadCollectionCount() {
		BmobQuery<Collection> bmobQuery = new BmobQuery<Collection>();
		bmobQuery.addWhereEqualTo("goodsId", goods.getObjectId());
		bmobQuery.count(context, Collection.class, new CountListener() {

			@Override
			public void onSuccess(int arg0) {
				tv_shoucang.setText("收藏：" + arg0 + "人");
				goods.setWant(arg0);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				showErrorToast(arg0, arg1);
				showLog("收藏人数", arg0, arg1);
			}
		});
	}

	/**
	 * 加载评论
	 */
	private void loadComment() {
		BmobQuery<Comment> bmobQuery = new BmobQuery<Comment>();
		bmobQuery.order("updatedAt");
		bmobQuery.addWhereEqualTo("goodsId", goods.getObjectId());
		bmobQuery.setLimit(Utils.REQUEST_COUNT);
		bmobQuery.setSkip(list_comment.size());
		bmobQuery.findObjects(context, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> arg0) {

				if (list_comment.size() == 0) {
					if (arg0.size() == 0) {
						showToast("暂无评论，快抢沙发！");
					} else {
						list_comment = arg0;
						adapter.notifyDataSetChanged();
						Utils.setHeight(adapter, listView, 1);
					}
				} else {
					if (arg0.size() == 0) {
						showToast("没有更多数据");
					} else {
						list_comment.addAll(arg0);
						adapter.notifyDataSetChanged();
						Utils.setHeight(adapter, listView, 1);
					}
				}
				scrollView.onRefreshComplete();
			}

			@Override
			public void onError(int arg0, String arg1) {
				showErrorToast(arg0, arg1);
				showLog("加载评论", arg0, arg1);
			}
		});
	}

	private class MyFragmentAdapter extends FragmentPagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			list_fragment = new ArrayList<ImageFragment>();
			for (int i = 0; i < images.size(); i++) {
				ImageFragment fragment = new ImageFragment();
				fragment.init(context, i, images);
				list_fragment.add(fragment);
			}
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list_fragment.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_fragment.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
		}

	}

	private void initView() {
		context = this;
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// 收藏
		bt_shoucang = findViewById(R.id.want);

		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("宝贝详情");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		viewPager = (ViewPager) findViewById(R.id.viewPager);

		tv_selector = (TextView) findViewById(R.id.tv_selector);
		tv_name = (TextView) findViewById(R.id.goods_tv_name);
		tv_price = (TextView) findViewById(R.id.goods_tv_price);
		tv_count = (TextView) findViewById(R.id.goods_tv_remain_count);
		tv_maichu = (TextView) findViewById(R.id.goods_tv_sale);
		tv_shoucang = (TextView) findViewById(R.id.goods_tv_shoucang);
		tv_desc = (TextView) findViewById(R.id.goods_tv_desc);

		iv_more = (ImageView) findViewById(R.id.more);
		ll_buttons = findViewById(R.id.ll_buttons);
		bt_shoucang = findViewById(R.id.want);
		iv_shoucang = findViewById(R.id.iv_want);
		tv_yishoucang = findViewById(R.id.tv_yishoucang);
		bt_lianximaijia = findViewById(R.id.chat);
		bt_buy = findViewById(R.id.buy);

		et_comment = (EditText) findViewById(R.id.et_pinglun);
		tv_comment_length = (TextView) findViewById(R.id.tv_comment_length);
		et_comment.addTextChangedListener(this);
		iv_send = findViewById(R.id.iv_fasong);
		listView = (MyListView) findViewById(R.id.listView);
		list_comment = new ArrayList<Comment>();
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		// Utils.setHeight(adapter, listView, 1);
		scrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		scrollView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		scrollView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"加载中...");
		scrollView.getLoadingLayoutProxy(false, true).setReleaseLabel("释放加载");

		scrollView.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				list_comment = new ArrayList<Comment>();
				loadGoods();
				loadComment();
				checkCollection();
				loadCollectionCount();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				loadComment();
			}
		});
		setOnClick(back, iv_send, iv_more, bt_shoucang, bt_lianximaijia, bt_buy);
	}

	private void loadGoods() {
		BmobQuery<Goods> bmobQuery = new BmobQuery<Goods>();
		bmobQuery.getObject(context, goods.getObjectId(),
				new GetListener<Goods>() {

					@Override
					public void onSuccess(Goods arg0) {
						goods = arg0;
						fillGoods();
						fragment_adapter.notifyDataSetChanged();
					}

					@Override
					public void onFailure(int arg0, String arg1) {

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.iv_fasong:
			getCommentContent();
			break;
		case R.id.more:
			int visible = ll_buttons.getVisibility();
			if (visible == View.INVISIBLE || visible == View.GONE) {
				ll_buttons.setVisibility(View.VISIBLE);
				iv_more.setImageResource(R.drawable.cancel);
			} else {
				ll_buttons.setVisibility(View.INVISIBLE);
				iv_more.setImageResource(R.drawable.more);
			}
			break;
		case R.id.want:
			if (iv_shoucang.getVisibility() == View.VISIBLE) {
				addCollection();
			} else {
				deleteCollection();
			}
			break;
		case R.id.buy:
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + goods.getPhone()));
			if (intent.resolveActivity(context.getPackageManager()) != null) {
				startActivity(intent);
			}
			break;
		case R.id.chat:
			String phoneNumber = goods.getPhone();
			intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
					+ phoneNumber));
			intent.putExtra("sms_body",
					"你好，我在北苑跳蚤市场看到你发布的\"" + goods.getTitle() + "\",我们可以聊聊吗？");
			startActivity(intent);
			break;
		}
	}

	/**
	 * 删除收藏
	 */
	private void deleteCollection() {
		if (collection == null) {
			showToast("Collection:objectid为空");
		} else {
			showLog("collectionId", 1, collection.getObjectId());
			String objectId = collection.getObjectId();
			collection = new Collection();
			collection.setObjectId(objectId);
			collection.delete(context, collection.getObjectId(),
					new DeleteListener() {

						@Override
						public void onSuccess() {
							iv_shoucang.setVisibility(View.VISIBLE);
							tv_yishoucang.setVisibility(View.INVISIBLE);
							goods.setWant(goods.getWant() - 1);
							tv_shoucang.setText("收藏：" + goods.getWant() + "人");
							showToast("取消收藏！");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							showErrorToast(arg0, arg1);
							showLog("取消收藏", arg0, arg1);
						}
					});

		}
	}

	/**
	 * 收藏
	 */
	private void addCollection() {
		final User user = getCurrentUser();
		if (user != null) {
			collection = new Collection(user.getObjectId(),
					goods.getObjectId(), goods.getTitle(), goods.getPrice(),
					goods.getImages().get(0));
			collection.save(context, new SaveListener() {

				@Override
				public void onSuccess() {
					showToast("收藏成功！");
					tv_yishoucang.setVisibility(View.VISIBLE);
					iv_shoucang.setVisibility(View.INVISIBLE);
					goods.setWant(goods.getWant() + 1);
					tv_shoucang.setText("收藏：" + goods.getWant() + "人");
					addScore(user, Utils.SCORE_ADD_COLLECTION, "收藏商品");
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					showErrorToast(arg0, arg1);
					showLog("收藏：", arg0, arg1);
				}
			});
		}
	}

	/**
	 * 发送评论
	 */
	private void getCommentContent() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et_comment.getWindowToken(), 0);

		final String content = et_comment.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			showToast("评论内容不能为空！");
			return;
		}
		final User currentUser = getCurrentUser();
		if (currentUser != null) {
			Builder builder = new AlertDialog.Builder(context);
			View view = View.inflate(context, R.layout.dialog_send_comment,
					null);
			TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
			final CheckBox checkBox = (CheckBox) view
					.findViewById(R.id.checkBox);
			tv_content.setText(content);

			final String userId = currentUser.getObjectId();
			Button button = (Button) view.findViewById(R.id.button);

			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					boolean isNiMing = checkBox.isChecked();

					String userName;
					String userHead;

					if (isNiMing) {
						userName = "匿名用户";
						userName = DES.encryptDES(userName);
						userHead = "";
					} else {
						userName = currentUser.getUsername();
						userHead = currentUser.getImageUri();
						if (TextUtils.isEmpty(userHead)) {
							userHead = "";
						}
					}
					Comment comment = new Comment(goods.getObjectId(), content,
							userId, userHead, userName);
					comment.save(context, new SaveListener() {

						@Override
						public void onSuccess() {
							showToast("发布评论成功！");
							if (alertDialog != null && alertDialog.isShowing()) {
								alertDialog.dismiss();
							}
							et_comment.setText("");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							showLog("发送评论", arg0, arg1);
							showErrorToast(arg0, arg1);
						}
					});
					showToast("正在发送");
				}
			});
			builder.setView(view);
			alertDialog = builder.create();
			alertDialog.show();
		}

	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list_comment.size();
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
			// TODO Auto-generated method stub
			ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = View.inflate(context, R.layout.item_shangpinpinglun,
						null);
				holder = new ViewHolder();
				holder.iv_head = (ImageView) view.findViewById(R.id.iv_head);
				holder.tv_userName = (TextView) view
						.findViewById(R.id.tv_userName);
				holder.tv_content = (TextView) view
						.findViewById(R.id.tv_content);
				holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			Comment comment = list_comment.get(position);
			String userName = comment.getUserName();
			try {
				userName = DES.decryptDES(userName, Utils.ENCRYPT_KEY);
				holder.tv_userName.setText(userName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String userHead = comment.getUserHead();
			if (!TextUtils.isEmpty(userHead)) {
				imageLoader.displayImage(userHead, holder.iv_head);
			}
			holder.tv_content.setText(comment.getContent());
			holder.tv_date.setText(comment.getCreatedAt());
			return view;
		}

		private class ViewHolder {
			private ImageView iv_head;
			private TextView tv_userName;
			private TextView tv_content;
			private TextView tv_date;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		tv_selector.setText((arg0 + 1) + "/" + images.size());
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		int length = s.toString().length();
		if (length < 80) {
			tv_comment_length.setVisibility(View.GONE);
		} else {
			tv_comment_length.setVisibility(View.VISIBLE);
			tv_comment_length.setText(length + "/100");
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == RequestCode.REQUEST_CODE_LOGIN
				&& arg1 == ResultCode.RESULT_CODE_LOGIN_SUCESS) {
			checkCollection();
		}
	}

	private void checkCollection() {
		User user = BmobUser.getCurrentUser(context, User.class);
		if (user != null) {
			showLog("checkCollection", 1, "开始");
			BmobQuery<Collection> bmobQuery = new BmobQuery<Collection>();
			String bql = "select * from Collection where userId = ? and goodsId = ?";
			showLog("user", 0, user.getObjectId());
			showLog("goods", 1, goods.getObjectId());
			bmobQuery.doSQLQuery(context, bql,
					new SQLQueryListener<Collection>() {

						@Override
						public void done(BmobQueryResult<Collection> arg0,
								BmobException arg1) {
							if (arg1 == null) {
								List<Collection> list = arg0.getResults();
								showLog("checked", list.size(), "");
								if (list != null && list.size() > 0) {
									collection = list.get(0);
									iv_shoucang.setVisibility(View.GONE);
									tv_yishoucang.setVisibility(View.VISIBLE);
								}
							} else {
								showLog("exception", 0, arg1.toString());

							}

						}
					}, user.getObjectId(), goods.getObjectId());
		}
	}
}
