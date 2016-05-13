package com.xiaomai.shopping.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaomai.shopping.R;

public class RefreshListView extends ListView implements OnScrollListener {

	public interface OnRefreshListener {
		// 下拉刷新
		public void pullDownRefresh();

		// 卡拉加载
		public void pullUpLoadMore();
	}

	private OnRefreshListener listener;

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.listener = listener;
	}

	/**
	 * 刷新完成后的操作
	 */
	public void onRefreshFinish() {

		if (IS_REFRESH == currentOption) {
			currentOption = PULL_REFRESH;
			refresh_header_progressbar.setVisibility(View.INVISIBLE);
			refresh_header_imageview.setVisibility(View.VISIBLE);
			refresh_header_text.setText("下拉刷新");
			refresh_header_view.setPadding(0, -height, 0, 0);
		}
		// 下拉加载完成
		if (isLoad) {
			isLoad = !isLoad;
			viewFooter.setPadding(0, -footerHeight, 0, 0);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			onRefreshFinish();
		};
	};

	private LinearLayout refresh_header_root;
	private LinearLayout refresh_header_view;
	private ProgressBar refresh_header_progressbar;
	private ImageView refresh_header_imageview;
	private TextView refresh_header_text;
	private TextView refresh_header_time;
	private int height;
	private View customerView;
	private RotateAnimation animationup;
	private RotateAnimation animationdown;
	private int downY = -30;
	private int padding;
	private int mFirstVisibleItem = -1;

	private boolean isUp;// 是不是在向上拉

	// 下拉刷新
	private static final int PULL_REFRESH = 0;
	// 释放刷新
	private static final int RELEASE_REFRESH = 1;
	// 正在刷新
	private static final int IS_REFRESH = 2;
	// 当前状态默认是下拉刷新
	private int currentOption = PULL_REFRESH;

	private View viewFooter;

	private int footerHeight;

	private boolean isLoad;

	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeader();
		initFooter();
		this.setOnScrollListener(this);
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeader();
		initFooter();
		this.setOnScrollListener(this);
	}

	public RefreshListView(Context context) {
		super(context);
		initHeader();
		initFooter();
		this.setOnScrollListener(this);
	}

	private void initFooter() {
		viewFooter = View.inflate(getContext(), R.layout.refresh_footer, null);
		viewFooter.measure(0, 0);
		footerHeight = viewFooter.getMeasuredHeight();
		viewFooter.setPadding(0, -footerHeight, 0, 0);
		this.addFooterView(viewFooter);
	}

	private void initHeader() {
		View viewHeader = View.inflate(getContext(), R.layout.refresh_header,
				null);
		refresh_header_root = (LinearLayout) viewHeader
				.findViewById(R.id.refresh_header_root);
		// 刷新头
		refresh_header_view = (LinearLayout) viewHeader
				.findViewById(R.id.refresh_header_view);
		// 进度条
		refresh_header_progressbar = (ProgressBar) viewHeader
				.findViewById(R.id.refresh_header_progressBar);
		refresh_header_imageview = (ImageView) viewHeader
				.findViewById(R.id.refresh_header_imageview);
		refresh_header_text = (TextView) viewHeader
				.findViewById(R.id.refresh_header_text);
		refresh_header_time = (TextView) viewHeader
				.findViewById(R.id.refresh_header_time);

		refresh_header_time.setText("上次刷新时间：" + getDateTime());

		// 测量刷新头
		refresh_header_view.measure(0, 0);
		// 获取测量过后的高度
		height = refresh_header_view.getMeasuredHeight();
		// 隐藏刷新头
		refresh_header_view.setPadding(0, -height, 0, 0);
		// 添加头的操作
		this.addHeaderView(viewHeader);
		// 初始化动画
		initAnimation();
	}

	// 暴露一个方法用于添加其他view
	public void addCustomerView(View view) {
		customerView = view;
		// 添加到
		refresh_header_root.addView(customerView);
	}

	public void addCustomerLinearLayout(LinearLayout layout) {
		for (int i = 0; i < layout.getChildCount(); i++) {
			refresh_header_root.addView(layout.getChildAt(i));
		}

	}

	public void setCurrentOption() {
		switch (currentOption) {
		case RELEASE_REFRESH:
			refresh_header_text.setText("释放刷新");
			refresh_header_imageview.setVisibility(View.VISIBLE);
			refresh_header_progressbar.setVisibility(View.INVISIBLE);
			refresh_header_imageview.startAnimation(animationup);
			break;

		case PULL_REFRESH:
			refresh_header_text.setText("下拉刷新");
			refresh_header_imageview.setVisibility(View.VISIBLE);
			refresh_header_progressbar.setVisibility(View.INVISIBLE);
			refresh_header_imageview.startAnimation(animationdown);
			break;
		case IS_REFRESH:
			refresh_header_text.setText("正在刷新...");
			refresh_header_imageview.clearAnimation();
			refresh_header_imageview.setVisibility(View.INVISIBLE);
			refresh_header_progressbar.setVisibility(View.VISIBLE);
			refresh_header_time.setText("上次刷新时间：" + getDateTime());
			break;
		}
	}

	private CharSequence getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private void initAnimation() {
		animationup = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, // 旋转点x轴的坐标
				0.5f, Animation.RELATIVE_TO_SELF, // 旋转点y轴的坐标
				0.5f);
		animationup.setDuration(500);
		// 旋转后停到当前位置
		animationup.setFillAfter(true);

		animationdown = new RotateAnimation(-180, -360,
				Animation.RELATIVE_TO_SELF, // 旋转点x轴的坐标
				0.5f, Animation.RELATIVE_TO_SELF, // 旋转点y轴的坐标
				0.5f);
		animationdown.setDuration(500);
		// 旋转后停到当前位置
		animationdown.setFillAfter(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 记录手指点下y坐标
			downY = (int) ev.getRawY();

			break;
		case MotionEvent.ACTION_MOVE:
			if (downY == -30) {
				// 无限接近点下downY的值
				downY = (int) ev.getRawY();
			}
			int moveY = (int) ev.getRawY();
			if (moveY - downY < 10) {
				isUp = true;
			} else {
				isUp = false;
			}
			if (!(moveY - downY > 30)) {
				break;
			}
			padding = (moveY - downY - 30) / 2 - height;

			if (customerView != null) {
				int[] listViewLocation = new int[2];
				this.getLocationOnScreen(listViewLocation);
				int listViewLocationY = listViewLocation[1];

				int[] customerViewLocation = new int[2];
				customerView.getLocationOnScreen(customerViewLocation);
				int customerLocationY = customerViewLocation[1];

				if (listViewLocationY > customerLocationY) {
					break;
				}
			}

			// 如果 正在刷新 的时候不响应
			if (IS_REFRESH == currentOption) {
				break;
			}

			if (padding > -height && mFirstVisibleItem == 0) {
				// 在第一个条目向下拖拽
				if (padding > 0 && currentOption == PULL_REFRESH) {
					// 完整的拖拽出来
					currentOption = RELEASE_REFRESH;
					setCurrentOption();
				}
				if (padding < 0 && currentOption == RELEASE_REFRESH) {
					// 没有完整的拖拽出来
					currentOption = PULL_REFRESH;
					setCurrentOption();
				}
				refresh_header_view.setPadding(0, padding, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			downY = -30;
			if (currentOption == PULL_REFRESH) {
				refresh_header_view.setPadding(0, -height, 0, 0);
			}
			if (currentOption == RELEASE_REFRESH) {
				refresh_header_view.setPadding(0, 0, 0, 0);
				currentOption = IS_REFRESH;
				setCurrentOption();
				// 刷新业务逻辑调用的地方，
				// 回调（1.定义一个接口，
				// 2.在接口中定义一个未实现的方法）
				if (listener != null) {
					listener.pullDownRefresh();
				}
				// 刷新完成后进度条，刷新关隐藏
				handler.sendEmptyMessageDelayed(0, 1000);
			}
			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		/**
		 * OnScrollListener.SCROLL_STATE_FLING 飞速滚动
		 * OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 一直触摸着滚动
		 * OnScrollListener.SCROLL_STATE_IDLE 滚动状态发生改变
		 */
		if (scrollState == SCROLL_STATE_FLING
				|| (scrollState == SCROLL_STATE_TOUCH_SCROLL && isUp)) {
			// 监听滑动状态的方法
			if (getLastVisiblePosition() == getAdapter().getCount() - 1
					&& !isLoad) {
				// 最后一条可见
				// 加载操作
				isLoad = !isLoad;
				// 显示正在加载的脚
				viewFooter.setPadding(0, 0, 0, 0);
				// 加载的业务逻辑
				if (listener != null) {
					listener.pullUpLoadMore();
				}

				handler.sendEmptyMessageDelayed(0, 1000);
			}
		}

	}

	//
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// TODO Auto-generated method stub
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
//	}
}
