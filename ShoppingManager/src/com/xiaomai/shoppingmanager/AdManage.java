package com.xiaomai.shoppingmanager;

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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shoppingmanager.base.BaseActivity;
import com.xiaomai.shoppingmanager.bean.Ad;
import com.xiaomai.shoppingmanager.utils.RequestCode;

public class AdManage extends BaseActivity {

	// private ImageView iv1, iv2, iv3, iv4, iv5;
	// private View rl1, rl2, rl3, rl4, rl5;
	// private View quxiao1, quxiao2, quxiao3, quxiao4, quxiao5;
	private ListView listView;
	private Myadapter adapter;
	// 图片的路径名称
	private ArrayList<String> string_list = new ArrayList<>();
	// 默认选择的图片
	private ArrayList<String> mSelectPath = new ArrayList<String>();

	// 下载的图片
	private ListView listView_down;
	private DownAdapter downAdapter;
	private List<Ad> list_ad;
	private Button bt_commit;
	private ImageView iv_add;
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_manage);
		initView();
		imageloader = ImageLoader.getInstance();
		imageloader.init(ImageLoaderConfiguration.createDefault(this));
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub

		// iv1 = (ImageView) findViewById(R.id.iv1);
		// iv2 = (ImageView) findViewById(R.id.iv2);
		// iv3 = (ImageView) findViewById(R.id.iv3);
		// iv4 = (ImageView) findViewById(R.id.iv4);
		// iv5 = (ImageView) findViewById(R.id.iv5);

		// rl1 = findViewById(R.id.rl1);
		// rl2 = findViewById(R.id.rl2);
		// rl3 = findViewById(R.id.rl3);
		// rl4 = findViewById(R.id.rl4);
		// rl5 = findViewById(R.id.rl5);
		//
		// quxiao1 = findViewById(R.id.quxiao1);
		// quxiao2 = findViewById(R.id.quxiao2);
		// quxiao3 = findViewById(R.id.quxiao3);
		// quxiao4 = findViewById(R.id.quxiao4);
		// quxiao5 = findViewById(R.id.quxiao5);

		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("广告管理");

		iv_add = (ImageView) findViewById(R.id.add_image);
		bt_commit = (Button) findViewById(R.id.bt_commit);

		listView = (ListView) findViewById(R.id.listView);
		adapter = new Myadapter();
		listView.setAdapter(adapter);

		listView_down = (ListView) findViewById(R.id.listView_down);
		downAdapter = new DownAdapter();
		listView_down.setAdapter(downAdapter);
		setOnClick(back, bt_commit, iv_add);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.add_image:
			openImageSelector();
			break;
		// case R.id.quxiao1:
		// deletAd(0);
		// break;
		// case R.id.quxiao2:
		// deletAd(1);
		// break;
		// case R.id.quxiao3:
		// deletAd(2);
		// break;
		// case R.id.quxiao4:
		// deletAd(3);
		// break;
		// case R.id.quxiao5:
		// deletAd(4);
		// break;
		case R.id.bt_commit:
			shangchuantupian();
			break;
		default:
			break;
		}
	}

	private void shangchuantupian() {
		// TODO Auto-generated method stub
		final String[] list = string_list
				.toArray(new String[string_list.size()]);
		if (string_list.size() > 0) {
			final ProgressDialog progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("正在上传图片");
			progressDialog.setProgress(0);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			BmobFile.uploadBatch(context, list, new UploadBatchListener() {

				@Override
				public void onSuccess(List<BmobFile> arg0, List<String> arg1) {
					// TODO Auto-generated method stub
					showLog("图片张数：", 1, arg1.size() + "");
					// 保存
					if (arg1.size() == list.length) {
						progressDialog.dismiss();
						onImageSuccess(arg1);
					}
				}

				@Override
				public void onProgress(int arg0, int arg1, int arg2, int arg3) {
					// showToast(arg0 + "/" + arg2);
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
		}
	}

	private void onImageSuccess(List<String> arg1) {
		for (int i = 0; i < arg1.size(); i++) {
			Ad ad = new Ad(arg1.get(i), "", "");
			ad.save(context, new SaveListener() {

				@Override
				public void onSuccess() {
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					showLog("fabu", arg0, arg1);
					showErrorToast(arg0, arg1);
				}
			});
		}
		showToast("发布成功!");

	}

	private void deletAd(Ad ad, final int position) {
		// TODO Auto-generated method stub
		showLog("ad", 0, ad.getObjectId());
		ad.delete(this, new DeleteListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				list_ad.remove(position);
				downAdapter.setList(list_ad);
				downAdapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub
				showToast("操作失败");
				showErrorToast(arg0, arg1);
				showLog("删除", arg0, arg1);
			}
		});
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		BmobQuery<Ad> bmobQuery = new BmobQuery<Ad>();
		bmobQuery.findObjects(context, new FindListener<Ad>() {

			@Override
			public void onSuccess(List<Ad> arg0) {
				// TODO Auto-generated method stub
				list_ad = arg0;
//				download();
				downAdapter.setList(list_ad);
				downAdapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

//	private void download() {
//		// TODO Auto-generated method stub
//		imageloader = ImageLoader.getInstance();
//		imageloader.init(ImageLoaderConfiguration.createDefault(this));
//		count = list_ad.size();
//		switch (count) {
//		case 5:
//			iv_add.setVisibility(View.GONE);
//			rl5.setVisibility(View.VISIBLE);
//			imageloader.displayImage(list_ad.get(4).getImage_url(), iv5);
//		case 4:
//			rl4.setVisibility(View.VISIBLE);
//			imageloader.displayImage(list_ad.get(3).getImage_url(), iv4);
//		case 3:
//			rl3.setVisibility(View.VISIBLE);
//			imageloader.displayImage(list_ad.get(2).getImage_url(), iv3);
//		case 2:
//			rl2.setVisibility(View.VISIBLE);
//			imageloader.displayImage(list_ad.get(1).getImage_url(), iv2);
//		case 1:
//			imageloader.displayImage(list_ad.get(0).getImage_url(), iv1);
//			break;
//		}
//	}

	private void openImageSelector() {
		Intent intent = new Intent(context, MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
				5 - list_ad.size());
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

			adapter.setList(string_list);
			adapter.notifyDataSetChanged();
		}
	}

	private class DownAdapter extends BaseAdapter {

		private List<Ad> list;

		public void setList(List<Ad> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub

			View view = convertView;
			ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				view = View.inflate(context, R.layout.item_shangchuan_tupian,
						null);
				holder.image = (ImageView) view.findViewById(R.id.image);
				holder.shanchu = (ImageView) view.findViewById(R.id.quxiao);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			final Ad ad = list.get(position);
			imageloader.displayImage(list_ad.get(position).getImage_url(),holder.image);
			holder.shanchu.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					deletAd(ad, position);
				}
			});
			return view;
		}

		private class ViewHolder {
			ImageView image;
			ImageView shanchu;
		}
	}

	private class Myadapter extends BaseAdapter {

		private ArrayList<String> string_list;

		public void setList(ArrayList<String> string_list) {
			this.string_list = string_list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (string_list != null) {
				return string_list.size();
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
			// TODO Auto-generated method stub
			View view = convertView;
			ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				view = View.inflate(context, R.layout.item_shangchuan_tupian,
						null);
				holder.image = (ImageView) view.findViewById(R.id.image);
				holder.shanchu = (ImageView) view.findViewById(R.id.quxiao);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			File mFile = new File(string_list.get(position));
			// 若该文件存在
			if (mFile.exists()) {
				bitmap = BitmapFactory.decodeFile(string_list.get(position));
				holder.image.setImageBitmap(bitmap);
			}
			holder.shanchu.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					string_list.remove(position);
					mSelectPath.remove(position);
					adapter.setList(string_list);
					adapter.notifyDataSetChanged();
				}
			});
			return view;
		}

		private class ViewHolder {
			ImageView image;
			ImageView shanchu;
		}
	}

	// private void showImages() {
	// // TODO Auto-generated method stub
	// /**
	// * 因为至少有一张照片，所有选择的照片最多是4张，这样从最后一张开始显示
	// */
	// File mFile;
	// switch (string_list.size()) {
	// case 4:
	// mFile = new File(string_list.get(3));
	// // 若该文件存在
	// if (mFile.exists()) {
	// bitmap = BitmapFactory.decodeFile(string_list.get(3));
	// iv2.setVisibility(View.VISIBLE);
	// iv2.setImageBitmap(bitmap);
	// }
	// case 3:
	// mFile = new File(string_list.get(2));
	// // 若该文件存在
	// if (mFile.exists()) {
	// bitmap = BitmapFactory.decodeFile(string_list.get(2));
	// iv3.setVisibility(View.VISIBLE);
	// iv3.setImageBitmap(bitmap);
	// }
	// case 2:
	// mFile = new File(string_list.get(1));
	// // 若该文件存在
	// if (mFile.exists()) {
	// bitmap = BitmapFactory.decodeFile(string_list.get(1));
	// iv4.setVisibility(View.VISIBLE);
	// iv4.setImageBitmap(bitmap);
	// }
	// case 1:
	// mFile = new File(string_list.get(0));
	// // 若该文件存在
	// if (mFile.exists()) {
	// bitmap = BitmapFactory.decodeFile(string_list.get(0));
	// iv5.setVisibility(View.VISIBLE);
	// iv5.setImageBitmap(bitmap);
	// }
	// break;
	// }
	// }

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

}
