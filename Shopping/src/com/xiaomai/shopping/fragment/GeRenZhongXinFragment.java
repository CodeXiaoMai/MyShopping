package com.xiaomai.shopping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.xiaomai.shopping.R;
import com.xiaomai.shopping.base.BaseFragment;
import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.listener.OnLoginOutListener;
import com.xiaomai.shopping.module.FaBuZhongXinActivity;
import com.xiaomai.shopping.module.LoginActivity;
import com.xiaomai.shopping.module.SettingActivity;
import com.xiaomai.shopping.module.ShangJiDingDanActivity;
import com.xiaomai.shopping.module.WoDeDingDanActivity;
import com.xiaomai.shopping.module.WoDeJiFenActivity;
import com.xiaomai.shopping.module.WoDeQiuGouActivity;
import com.xiaomai.shopping.module.WoDeShouCangActivity;
import com.xiaomai.shopping.utils.DES;
import com.xiaomai.shopping.utils.RequestCode;
import com.xiaomai.shopping.utils.ResultCode;
import com.xiaomai.shopping.utils.Utils;

/**
 * 个人中心
 * 
 * @author XiaoMai
 *
 */
public class GeRenZhongXinFragment extends BaseFragment implements
		OnLoginOutListener {

	// 登录
	private View login;
	// 头像
	private ImageView iv_head;
	// 用户名字
	private TextView tv_name;
	// 设置
	private View ll_setting;
	// 发布中心
	private View ll_fabu;
	// 我的收藏
	private View ll_wodeshoucang;
	// 我的订单
	private View ll_wodedingdan;
	// 我的求购
	private View ll_wodeqiugou;
	// 我的积分
	private View ll_wodejifen;
	// 商家订单
	private View ll_shangjia;
	private User user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_genrenzhongxin,
				container, false);
		showLog("gerenzhongxin", 1, "个人中心");
		initView(view);
		onLogin();
		return view;
	}

	private void initView(View view) {
		context = getContext();
		LoginActivity.listener = this;
		SettingActivity.listener = this;
		tv_name = (TextView) view.findViewById(R.id.gerenzhongxin_tv_name);
		iv_head = (ImageView) view.findViewById(R.id.geren_head);
		login = view.findViewById(R.id.ll_login_regist);
		ll_setting = view.findViewById(R.id.gerenzhongxin_ll_setting);
		ll_fabu = view.findViewById(R.id.gerenzhongxin_ll_fabuzhongxin);
		ll_wodeshoucang = view.findViewById(R.id.gerenzhongxin_ll_wodeshoucang);
		ll_wodedingdan = view.findViewById(R.id.gerenzhongxin_ll_wodedingdan);
		ll_wodeqiugou = view.findViewById(R.id.gerenzhongxin_ll_qiugou);
		ll_wodejifen = view.findViewById(R.id.gerenzhongxin_ll_wodejifen);
		ll_shangjia = view.findViewById(R.id.gerenzhongxin_ll_shangjiadingdan);
		setOnClick(login, ll_setting, ll_fabu, ll_wodeshoucang, ll_wodedingdan,
				ll_wodeqiugou, ll_wodejifen, ll_shangjia);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 登录、注册
		case R.id.ll_login_regist:
			startActivityForResult((new Intent(getContext(),
					LoginActivity.class)), RequestCode.REQUEST_CODE_LOGIN);
			break;
		// 设置
		case R.id.gerenzhongxin_ll_setting:
			startActivityForResult(new Intent(context, SettingActivity.class),
					RequestCode.REQUEST_CODE_SETTING);
			break;
		// 发布
		case R.id.gerenzhongxin_ll_fabuzhongxin:
			isLogin(FaBuZhongXinActivity.class);
			break;
		// 收藏
		case R.id.gerenzhongxin_ll_wodeshoucang:
			isLogin(WoDeShouCangActivity.class);
			break;
		// 我的订单
		case R.id.gerenzhongxin_ll_wodedingdan:
			isLogin(WoDeDingDanActivity.class);
			break;
		// 我的求购
		case R.id.gerenzhongxin_ll_qiugou:
			isLogin(WoDeQiuGouActivity.class);
			break;
		// 我的积分
		case R.id.gerenzhongxin_ll_wodejifen:
			isLogin(WoDeJiFenActivity.class);
			break;
		case R.id.gerenzhongxin_ll_shangjiadingdan:
			isLogin(ShangJiDingDanActivity.class);
			break;
		}
	}

	private <T> void isLogin(Class<T> clazz) {
		User user = BmobUser.getCurrentUser(context, User.class);
		if (user != null) {
			startActivity(new Intent(context, clazz));
		} else {
			showToast("请先登录");
		}
	}

	/*
	 * @Override public void onActivityResult(int requestCode, int resultCode,
	 * Intent data) { super.onActivityResult(requestCode, resultCode, data); if
	 * (requestCode == RequestCode.REQUEST_CODE_LOGIN && resultCode ==
	 * ResultCode.RESULT_CODE_LOGIN_SUCESS) { // 登录成功 User user = (User)
	 * data.getSerializableExtra("user"); showLog("登录成功", 0,
	 * user.getObjectId()); try { String userName =
	 * DES.decryptDES(user.getNicheng(), Utils.ENCRYPT_KEY);
	 * tv_name.setText(userName); login.setClickable(false); } catch (Exception
	 * e) { e.printStackTrace(); } } else if (requestCode ==
	 * RequestCode.REQUEST_CODE_SETTING && resultCode ==
	 * ResultCode.RESULT_CODE_SETTING_LOGOUT) { // 注销登录
	 * tv_name.setText("登录/注册"); login.setClickable(true); }
	 * 
	 * }
	 */

	@Override
	public void loadData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLogin() {
		// TODO Auto-generated method stub
		user = BmobUser.getCurrentUser(context, User.class);
		if (user != null) {
			if (user.getIsNiChengChanged()) {
				tv_name.setText(DES.decryptDES(user.getNicheng(),
						Utils.ENCRYPT_KEY));
			} else {
				tv_name.setText(user.getMobilePhoneNumber());
			}
			String uri = user.getImageUri();
			if (!TextUtils.isEmpty(uri)) {
				iv_head.setImageResource(R.drawable.tupian_jiazaizhong);
				loader = ImageLoader.getInstance();
				loader.init(ImageLoaderConfiguration.createDefault(context));
				loader.displayImage(uri, iv_head);
			}
			login.setClickable(false);
		}
	}

	@Override
	public void onLogOut() {
		// TODO Auto-generated method stub
		tv_name.setText("登录/注册");
		iv_head.setImageResource(R.drawable.ic_launcher);
		login.setClickable(true);
	}

}
