package com.xiaomai.shopping.module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.listener.UpdateListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.bean.Message;
import com.xiaomai.shopping.bean.Order;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.GetDate;
import com.xiaomai.shopping.view.MyDialog;

public class PayActivity extends BaseActivity {

	private ImageView image;
	private TextView tv_price;
	private TextView tv_title;

	private View jian;
	private View add;
	private EditText et_count;

	private View ll_weixin;
	private ImageView iv_weixin;
	private View ll_zhifubao;
	private ImageView iv_zhifubao;
	private Button bt_pay;
	private boolean isZhiFuBao;

	private String name;
	private Double money;
	private User user;
	private Goods goods;

	private int count = 1;
	private String orderId;

	private CheckBox checkBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		initView();
		fillData();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		goods = (Goods) intent.getSerializableExtra("goods");
		tv_price.setText("￥" + goods.getPrice());
		tv_title.setText(goods.getTitle());
		name = goods.getTitle();
		money = goods.getPrice();
		user = (User) intent.getSerializableExtra("user");
	}

	private void initView() {
		// TODO Auto-generated method stub
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("支付订单");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		image = (ImageView) findViewById(R.id.image);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_title = (TextView) findViewById(R.id.tv_name);

		checkBox = (CheckBox) findViewById(R.id.checkbox);

		jian = findViewById(R.id.jian);
		add = findViewById(R.id.add);
		et_count = (EditText) findViewById(R.id.et_count);
		et_count.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (v.getId() == R.id.et_count) {
					if (hasFocus) {
						et_count.setCursorVisible(true);
					} else {
						et_count.setCursorVisible(false);
						et_count.clearFocus();
					}
				}
			}
		});
		ll_weixin = findViewById(R.id.ll_weixin);
		iv_weixin = (ImageView) findViewById(R.id.iv_weixin);
		ll_zhifubao = findViewById(R.id.ll_zhifubao);
		iv_zhifubao = (ImageView) findViewById(R.id.iv_zhifubao);
		bt_pay = (Button) findViewById(R.id.bt_pay);
		setOnClick(back, jian, add, ll_weixin, ll_zhifubao, bt_pay);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.jian:
			if (count > 1) {
				count--;
				et_count.setText(count + "");
				money = count * goods.getPrice();
				tv_price.setText("￥" + money);
			}
			break;
		case R.id.add:
			if (count < goods.getCount()) {
				count++;
				et_count.setText(count + "");
				money = count * goods.getPrice();
				tv_price.setText("￥" + money);
			} else {
				showToast("商品只有这么多了...");
			}
			break;
		case R.id.ll_zhifubao:
			isZhiFuBao = true;
			iv_zhifubao.setImageResource(R.drawable.select);
			iv_weixin.setImageResource(R.drawable.no_select);
			break;
		case R.id.ll_weixin:
			isZhiFuBao = false;
			iv_zhifubao.setImageResource(R.drawable.no_select);
			iv_weixin.setImageResource(R.drawable.select);
			break;
		case R.id.bt_pay:
			if (checkBox.isChecked()) {
				pay();
			} else {
				showToast("请先认真阅读，并同意条款");
			}
			break;
		}
	}

	/**
	 * 调用支付宝支付
	 */
	private void pay() {
		// TODO Auto-generated method stub
		showDialog("正在获取订单");
		BP.pay(this, name, "", money, isZhiFuBao, new PListener() {
			private Order order;

			// 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
			@Override
			public void unknow() {
				// TODO Auto-generated method stub
				showToast("支付结果未知,请稍后手动查询");
				hideDialog();
			}

			// 支付成功,如果金额较大请手动查询确认
			@Override
			public void succeed() {
				// TODO Auto-generated method stub
				BP.query(PayActivity.this, PayActivity.this.orderId,
						new QListener() {

							@Override
							public void succeed(String arg0) {
								// TODO Auto-generated method stub
								if (arg0.equals("NOTPAY")) {
									showToast("支付失败，请到我的订单查看");
								} else if (arg0.equals("SUCCESS")) {
									order.setStatus(Order.ORDER_STATUS_ZHIFUCHENGGONG);
									order.update(context, new UpdateListener() {

										@Override
										public void onSuccess() {
											// TODO Auto-generated method stub
											showToast("支付成功!您可以到我的订单页面查看订单详情!");
											Message message = new Message(
													goods.getUserId(),
													"支付消息",
													"用户:"
															+ user.getMobilePhoneNumber()
															+ "想买您的["
															+ goods.getTitle()
															+ "],并已经成功支付，请到商家订单查看详情",
													GetDate.currentTime()
															.replace(" ", "\n"));
											message.save(context);
											finish();
										}

										@Override
										public void onFailure(int arg0,
												String arg1) {
											// TODO Auto-generated method stub

										}
									});

								}
							}

							@Override
							public void fail(int arg0, String arg1) {
								// TODO Auto-generated method stub
								showErrorToast(arg0, arg1);
								showLog("查询失败", arg0, arg1);
							}
						});
				hideDialog();
			}

			// 无论成功与否,返回订单号
			@Override
			public void orderId(String orderId) {
				// TODO Auto-generated method stub
				// 此处应该保存订单号,比如保存进数据库等,以便以后查询
				// order.setText(orderId);
				PayActivity.this.orderId = orderId;
				showDialog("获取订单成功!请等待跳转到支付页面~");
				goods.setCount(goods.getCount() - count);
				goods.update(context);
				order = new Order(orderId, user.getObjectId(), goods
						.getUserId(), goods.getObjectId(), name, goods
						.getImages().get(0), count, money,
						Order.ORDER_STATUS_WEIZHIFU);
				order.save(context);
				Message message = new Message(goods.getUserId(), "订单消息", "用户:"
						+ user.getMobilePhoneNumber() + "想买您的["
						+ goods.getTitle() + "],并已经成功下单，请到商家订单查看详情", GetDate
						.currentTime().replace(" ", "\n"));
				message.save(context);
			}

			@Override
			public void fail(int code, String reason) {
				// TODO Auto-generated method stub
				// 当code为-2,意味着用户中断了操作
				// code为-3意味着没有安装BmobPlugin插件
				switch (code) {
				case -3:
					new AlertDialog.Builder(PayActivity.this)
							.setMessage(
									"订单获取成功！\n你尚未安装支付插件,无法进行微信支付,请选择安装插件(无流量消耗),还是用支付宝支付,您还可以退出，进行线下付款")
							.setPositiveButton("安装",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											installBmobPayPlugin("bp_wx.db");
										}
									})
							.setNegativeButton("支付宝支付",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											isZhiFuBao = true;
											pay();
										}
									}).create().show();
					break;
				case 10777:
					showToast("您的操作过于频繁");
					finish();
					break;
				case -1:
				case 7777:
					MyDialog.showDialog(context, "", "微信客户端未安装,请到我的订单列表中查看",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									startActivity(new Intent(context,
											WoDeDingDanActivity.class));
									finish();
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							});
					break;
				case 8888:
					MyDialog.showDialog(context, "",
							"您的微信客户端版本过低，不支持微信支付,请到我的订单列表中查看",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									startActivity(new Intent(context,
											WoDeDingDanActivity.class));
									finish();
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							});
					break;
				// 支付中断
				case -2:
				case 6001:
					MyDialog.showDialog(context, "", "支付中断,您可以到我的订单页面查看订单详情!",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									startActivity(new Intent(context,
											WoDeDingDanActivity.class));
									finish();
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							});
					break;
				default:
					MyDialog.showDialog(context, "", "支付中断,您可以到我的订单页面查看订单详情!",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									startActivity(new Intent(context,
											WoDeDingDanActivity.class));
									finish();
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							});
					break;
				}
				hideDialog();
			}
		});
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

	void installBmobPayPlugin(String fileName) {
		try {
			InputStream is = getAssets().open(fileName);
			File file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + fileName + ".apk");
			if (file.exists())
				file.delete();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = is.read(temp)) > 0) {
				fos.write(temp, 0, i);
			}
			fos.close();
			is.close();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse("file://" + file),
					"application/vnd.android.package-archive");
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
