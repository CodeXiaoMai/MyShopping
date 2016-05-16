package com.xiaomai.shopping.module;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import multi_image_selector.MultiImageSelectorActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseActivity;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.utils.DES;
import com.xiaomai.shopping.utils.RequestCode;
import com.xiaomai.shopping.utils.Utils;

/**
 * 个人资料页面
 * 
 * @author XiaoMai
 *
 */
public class GeRenZiLiaoActivity extends BaseActivity {

	private static final int PHOTO_REQUEST_CUT = 0;
	private View back;
	private TextView title;
	private View share;

	private ImageView iv_head;
	private EditText et_name;
	private EditText et_phone;
	private EditText et_grade;
	private EditText et_num;
	private EditText et_real_name;

	private ImageView iv_nan, iv_nv;
	private TextView tv_nan, tv_nv;
	private Button bt_save;
	private String imageUri;
	private String name = "";
	private String sex = "男";
	private String phone = "";
	private String grade = "";
	private String num = "";
	private String realName = "";

	private User user;
	private ArrayList<String> mSelectPath = new ArrayList<String>();
	private Bitmap bitmap;
	private String fileName = "";
	private boolean headChanged;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_geren_ziliao);
		initView();
		checkNetWorkState();
	}

	private void initView() {
		back = findViewById(R.id.title_back);
		title = (TextView) findViewById(R.id.title_title);
		title.setText("个人资料");
		share = (View) findViewById(R.id.title_share);
		share.setVisibility(View.INVISIBLE);

		iv_head = (ImageView) findViewById(R.id.iv_head);
		et_name = (EditText) findViewById(R.id.et_name);
		iv_nan = (ImageView) findViewById(R.id.iv_man);
		iv_nv = (ImageView) findViewById(R.id.iv_nv);
		tv_nan = (TextView) findViewById(R.id.tv_sex_man);
		tv_nv = (TextView) findViewById(R.id.tv_woman);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_grade = (EditText) findViewById(R.id.et_grade);
		et_num = (EditText) findViewById(R.id.et_num);
		et_real_name = (EditText) findViewById(R.id.et_real_name);
		bt_save = (Button) findViewById(R.id.bt_save);

		setOnClick(back, iv_head, iv_nan, iv_nv, tv_nan, tv_nv, bt_save);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.iv_man:
		case R.id.tv_sex_man:
			sex = "男";
			setSex(sex);
			break;
		case R.id.iv_nv:
		case R.id.tv_woman:
			sex = "女";
			setSex(sex);
			break;
		case R.id.bt_save:
			checkMessage();
			break;
		case R.id.iv_head:
			openImageSelector();
			break;
		}
	}

	private void openImageSelector() {
		Intent intent = new Intent(context, MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
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

	private void checkMessage() {
		name = et_name.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			showToast("昵称不能为空！");
			return;
		}
		phone = et_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			showToast("手机号不能为空！");
			return;
		}
		grade = et_grade.getText().toString().trim();
		if (TextUtils.isEmpty(grade)) {
			showToast("班级不能为空！");
			return;
		}
		num = et_num.getText().toString().trim();
		if (TextUtils.isEmpty(num)) {
			showToast("学号不能为空");
			return;
		}
		realName = et_real_name.getText().toString().trim();

		updateUserHead();
	}

	private void updateUserHead() {
		if (headChanged) {
			String[] filePaths = { fileName };
			showToast("正在上传头像...");
			BmobFile.uploadBatch(context, filePaths, new UploadBatchListener() {

				@Override
				public void onSuccess(List<BmobFile> arg0, List<String> arg1) {
					// TODO Auto-generated method stub
					if (arg1 != null) {
						imageUri = arg1.get(0);
						updateUserInfo();
					} else {
						showToast("arg1:null");
					}
				}

				@Override
				public void onProgress(int arg0, int arg1, int arg2, int arg3) {
				}

				@Override
				public void onError(int arg0, String arg1) {
					showErrorToast(arg0, arg1);
					showLog("上传图片", arg0, arg1);
				}
			});
		} else {
			updateUserInfo();
		}
	}

	private void updateUserInfo() {
		showDialog("正在上传数据");
		name = DES.encryptDES(name);
		user.setNicheng(name);
		realName = DES.encryptDES(realName);
		user.setRealName(realName);
		sex = DES.encryptDES(sex);
		user.setSex(sex);
		user.setMobilePhoneNumber(phone);
		grade = DES.encryptDES(grade);
		user.setGrade(grade);
		num = DES.encryptDES(num);
		user.setNum(num);
		user.setIsNiChengChanged(true);
		user.setImageUri(imageUri);
		user.update(context, new UpdateListener() {

			@Override
			public void onSuccess() {
				hideDialog();
				showToast("修改成功！");
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				hideDialog();
				showErrorToast(arg0, arg1);
				showLog("修改个人资料", arg0, arg1);
			}
		});
	}

	@Override
	public void loadData() {
		showDialog("数据加载中");
		user = getCurrentUser();
		if (user != null) {
			imageUri = user.getImageUri();
			if (!TextUtils.isEmpty(imageUri)) {
				loader = ImageLoader.getInstance();
				loader.displayImage(imageUri, iv_head);
			}
			name = user.getNicheng();
			if (name != null) {
				name = DES.decryptDES(name, Utils.ENCRYPT_KEY);
				et_name.setText(name);
			}
			if (user.getIsNiChengChanged()) {
				et_name.setEnabled(false);
			}
			et_phone.setText(user.getMobilePhoneNumber());
			sex = user.getSex();
			if (!TextUtils.isEmpty(sex) && !sex.equals("未知")) {
				sex = DES.decryptDES(sex, Utils.ENCRYPT_KEY);
				setSex(sex);
			}
			grade = user.getGrade();
			if (grade != null) {
				grade = DES.decryptDES(grade, Utils.ENCRYPT_KEY);
				et_grade.setText(grade);
			}
			num = user.getNum();
			if (num != null) {
				num = DES.decryptDES(num, Utils.ENCRYPT_KEY);
				et_num.setText(num);
			}
		}
		hideDialog();
	}

	private void setSex(String sex) {
		if (sex.equals("男")) {
			iv_nan.setImageResource(R.drawable.radiobutton_xuanzhong);
			iv_nv.setImageResource(R.drawable.radiobutton_weixuanzhong);
		} else if (sex.equals("女")) {
			iv_nv.setImageResource(R.drawable.radiobutton_xuanzhong);
			iv_nan.setImageResource(R.drawable.radiobutton_weixuanzhong);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RequestCode.REQUEST_IMAGE && resultCode == RESULT_OK) {
			if (data != null) {
				if (requestCode == RequestCode.REQUEST_IMAGE
						&& resultCode == RESULT_OK) {
					mSelectPath = data
							.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
					File file = new File(mSelectPath.get(0));
					Uri uri = Uri.fromFile(file);
					crop(uri);
				}
			}
		} else if (requestCode == PHOTO_REQUEST_CUT) {
			try {
				bitmap = data.getParcelableExtra("data");
				String name = DateFormat.format("yyyyMMdd_hhmmss",
						Calendar.getInstance(Locale.CHINA))
						+ ".png";
				FileOutputStream b = null;
				File file = new File("/sdcard/myImage/");
				file.mkdirs();// 创建文件夹
				fileName = "/sdcard/myImage/" + name;
				try {
					b = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
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

				iv_head.setImageBitmap(bitmap);
				headChanged = true;
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * 剪切图片
	 * 
	 * @function:
	 * @author:Jerry
	 * @date:2013-12-30
	 * @param uri
	 */
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
