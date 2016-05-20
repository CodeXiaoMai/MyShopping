package com.xiaomai.shopping.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import multi_image_selector.MultiImageSelectorActivity;
import multi_image_selector.utils.Bimp;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.Goods;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.listener.LunBoListener;
import com.xiaomai.shopping.utils.RequestCode;
import com.xiaomai.shopping.utils.StateCode;
import com.xiaomai.shopping.utils.Utils;
import com.xiaomai.shopping.view.MyGridView;

/**
 * 发布商品
 * 
 * @author XiaoMai
 *
 */
public class FaBuShangPinActivity extends BaseActivity implements LunBoListener {

	private View back;
	private TextView title;
	private View share;

	private Context context;
	// 标题
	private EditText et_title;
	// 描述
	private EditText et_content;
	// 价格
	private EditText et_price;
	// 数量
	private EditText et_count;
	// 交易地点
	private EditText et_address;
	// 手机号
	private EditText et_phone;
	// QQ号
	private EditText et_qq;
	// 发布
	private Button bt_fabu;
	// 图片
	private MyGridView gridView;
	private GridAdapter adapter;
	private Bitmap bitmap;
	// 图片的路径名称
	private ArrayList<String> string_list = new ArrayList<>();
	// 默认选择的图片
	private ArrayList<String> mSelectPath = new ArrayList<String>();
	private String userId;
	private String state;
	private int want;
	private String content;
	private Double price;
	private Integer count;
	private String address;
	private String phone;
	private String qq;
	private String type;
	private String title2;
	private User user;

	private Spinner spinner;
	private String[] names = { "点击选择分类 ", "交通工具", "电子产品", "体育器材", "学习用品",
			"衣帽鞋子", "其他" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fabu);
		initView();
	}

	private void initView() {
		context = this;
		user = getCurrentUser();
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("发布商品");
		share = findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);
		et_title = (EditText) findViewById(R.id.fabu_et_title);
		et_content = (EditText) findViewById(R.id.fabu_et_content);
		et_price = (EditText) findViewById(R.id.fabu_et_price);
		et_count = (EditText) findViewById(R.id.fabu_et_count);
		et_address = (EditText) findViewById(R.id.fabu_et_address);
		et_phone = (EditText) findViewById(R.id.fabu_et_phone);
		if (user != null) {
			et_phone.setText(user.getMobilePhoneNumber());
		}
		et_qq = (EditText) findViewById(R.id.fabu_et_qq);
		bt_fabu = (Button) findViewById(R.id.fabu_bt_fabu);
		gridView = (MyGridView) findViewById(R.id.fabu_gridView);
		adapter = new GridAdapter(string_list, this);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == adapter.getCount() - 1) {
					openImageSelector();
				}
			}
		});
		et_title.requestFocus();

		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_item,
				R.id.textView, names));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				type = names[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		setOnClick(back, bt_fabu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.fabu_bt_fabu:
			fabu();
			break;
		default:
			break;
		}
	}

	private void openImageSelector() {
		Intent intent = new Intent(context, MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 5);
		// 选择模式
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				MultiImageSelectorActivity.MODE_MULTI);
		// 默认选择
		if (mSelectPath != null && mSelectPath.size() > 0) {
			intent.putExtra(
					MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
					mSelectPath);
		}
		startActivityForResult(intent, RequestCode.REQUEST_IMAGE);
	}

	private void fabu() {
		title2 = et_title.getText().toString().trim();
		if (TextUtils.isEmpty(title2)) {
			showToast("标题不能为空！");
			return;
		}
		content = et_content.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			showToast("给你的宝贝添加几句描述吧！");
			return;
		}
		String strprice = et_price.getText().toString().trim();
		if (TextUtils.isEmpty(strprice)) {
			showToast("价格不能为空！");
			return;
		}
		price = Double.parseDouble(strprice);
		String str_count = et_count.getText().toString().trim();
		if (TextUtils.isEmpty(str_count)) {
			showToast("数量不能为空！");
			return;
		}
		if (TextUtils.isEmpty(type) || type.equals(names[0])) {
			showToast("请选择分类");
			return;
		}
		count = Integer.parseInt(str_count);
		address = et_address.getText().toString().trim();
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("留下您的联系方式吧！");
			return;
		}
		qq = et_qq.getText().toString().trim();
		if (TextUtils.isEmpty(qq)) {
			showToast("请务必留下您的支付宝账号，用于向您付款");
			return;
		}
		if (user != null) {
			userId = user.getObjectId();
			state = StateCode.GOODS_SHENHE;
			want = 0;
			// 加密
			// userId = DES.encryptDES(userId);
			// title = DES.encryptDES(title);
			// content = DES.encryptDES(content);
			// price = DES.encryptDES(price);
			// count = DES.encryptDES(count);
			// address = DES.encryptDES(address);
			// phone = DES.encryptDES(phone);
			// qq = DES.encryptDES(qq);
			// state = DES.encryptDES(state);
			final String[] list = string_list.toArray(new String[string_list
					.size()]);
			if (list.length > 0) {
				final ProgressDialog progressDialog = new ProgressDialog(
						context);
				progressDialog.setTitle("正在上传图片");
				progressDialog.setProgress(0);
				progressDialog
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
				BmobFile.uploadBatch(context, list, new UploadBatchListener() {

					@Override
					public void onSuccess(List<BmobFile> arg0, List<String> arg1) {
						// TODO Auto-generated method stub
						progressDialog.dismiss();
						showLog("图片张数：", 1, arg1.size() + "");
						// 保存
						if (arg1.size() == list.length) {
							onImageSuccess(arg1);
						}
					}

					@Override
					public void onProgress(int arg0, int arg1, int arg2,
							int arg3) {
						// showToast(arg0 + "/" + arg2);
						progressDialog.dismiss();
						progressDialog.setTitle("正在上传第" + arg0 + "张图片");
						progressDialog.setProgress(arg3);
						Log.d("progress", "arg0:" + arg0 + ",arg1:" + arg1
								+ ",arg2:" + arg2 + ",arg3:" + arg3);
					}

					@Override
					public void onError(int arg0, String arg1) {
						showErrorToast(arg0, arg1);

					}
				});
			} else {
				showToast("上传张图片吧！");
			}

		}

	}

	private void onImageSuccess(List<String> arg1) {
		showDialog("正在发布");
		Goods goods = new Goods(userId, title2, content, price, count, count,
				address, phone, qq, state, want, 0, type, arg1);
		goods.save(context, new SaveListener() {

			@Override
			public void onSuccess() {
				hideDialog();
				showToast("发布成功!");
				addScore(user, Utils.SCORE_ADD_GOODS, "发布商品");
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				hideDialog();
				showLog("fabu", arg0, arg1);
				showErrorToast(arg0, arg1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == RequestCode.REQUEST_IMAGE
					&& resultCode == RESULT_OK) {
				mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				writeSdCard();
			}
		}
	}

	private void writeSdCard() {
		string_list.clear();
		if (!mSelectPath.isEmpty()) {
			for (int i = 0; i < mSelectPath.size(); i++) {
				Bitmap mBitmap = null;
				try {
					String str = mSelectPath.get(i);
					mBitmap = Bimp.revitionImageSize(str);
				} catch (IOException e1) {
					e1.printStackTrace();
					String message = e1.getMessage();
					Log.d("ddd", message);
				}
				String name = (new Date().getTime()) + ".png";
				FileOutputStream b = null;
				File file = new File("/sdcard/myImage/");
				file.mkdirs();// 创建文件夹
				String fileName = "/sdcard/myImage/" + name;
				try {
					b = new FileOutputStream(fileName);
					mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
					string_list.add(fileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (mBitmap != null && mBitmap.isRecycled() == false) {
					mBitmap.recycle();
				}
			}
			adapter.add(string_list);
			adapter.notifyDataSetChanged();
		}
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;
		private ArrayList<String> string_list;
		private LunBoListener listener;

		public boolean isShape() {
			return shape;
		}

		public void add(ArrayList<String> mSelectPath) {
			this.string_list = mSelectPath;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(ArrayList<String> mSelectPath, LunBoListener listener) {
			// TODO Auto-generated constructor stub
			inflater = LayoutInflater.from(context);

			string_list = mSelectPath;

			this.listener = listener;

		}

		public int getCount() {
			if (string_list.size() == 5) {
				return 5;
			}
			return (string_list.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {

			selectedPosition = position;

		}

		public int getSelectedPosition() {

			return selectedPosition;

		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);

				holder = new ViewHolder();

				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);

				holder.shanchu = (ImageView) convertView
						.findViewById(R.id.shanchu);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();

			}

			if (position == string_list.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.add_juxing));
				holder.shanchu.setVisibility(View.GONE);
				if (position == 5) {
					holder.image.setVisibility(View.GONE);
				}

			} else {
				holder.shanchu.setVisibility(View.VISIBLE);
				File mFile = new File(string_list.get(position));
				holder.shanchu.setImageResource(R.drawable.shanchuzhaopian);
				// 若该文件存在
				if (mFile.exists()) {

					bitmap = BitmapFactory
							.decodeFile(string_list.get(position));

					holder.image.setImageBitmap(bitmap);

				}

			}

			holder.shanchu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					listener.callBack(position);

				}
			});

			return convertView;
		}

		public class ViewHolder {

			public ImageView image;

			public ImageView shanchu;

		}

	}

	@Override
	public void callBack(int position) {
		string_list.remove(position);
		mSelectPath.remove(position);
		adapter.add(string_list);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bitmap != null) {
			if (!bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
		deleteAllFiles(new File("/sdcard/myImage/"));
	}

	private void deleteAllFiles(File root) {
		File files[] = root.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isDirectory()) { // 判断是否为文件夹
					deleteAllFiles(f);
					try {
						f.delete();
					} catch (Exception e) {
					}
				} else {
					if (f.exists()) { // 判断是否存在
						deleteAllFiles(f);
						try {
							f.delete();
						} catch (Exception e) {
						}
					}
				}
			}
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}
}
