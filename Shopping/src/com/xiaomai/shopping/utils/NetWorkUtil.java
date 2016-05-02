package com.xiaomai.shopping.utils;

import com.xiaomai.shopping.R;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetWorkUtil {

	/**
	 * 
	 * @author:
	 * @tag: @param context
	 * @tag: @return
	 * @data: 2014年8月24日 下午2:25:00 TODO 判断网络
	 * @return:boolean
	 */
	public static boolean isNetwork(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			Toast.makeText(context,
					context.getResources().getString(R.string.network_error),
					Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	/**
	 * 
	 * @TODO ：判断WiFi网络
	 * @author ：
	 * @date : 2014年8月30日 上午9:51:35
	 * @params ：@param context
	 * @params ：@return
	 * @return ：boolean
	 */
	public static boolean isWifiNetWork(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info.getState() == NetworkInfo.State.CONNECTED) { // 判断是不是在连接。。。
			if (info.getType() == ConnectivityManager.TYPE_WIFI)
				return true;
		}

		return false;
	}

	/**
	 * 
	 * @TODO ：判断是否是移动网络
	 * @author ：
	 * @date : 2014年8月30日 上午9:52:59
	 * @params ：@param context
	 * @params ：@return
	 * @return ：boolean
	 */
	public static boolean isMobileNetWork(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info.getState() == NetworkInfo.State.CONNECTED) { // 判断是不是在连接。。。
			if (info.getType() == ConnectivityManager.TYPE_MOBILE)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param context
	 * @param b
	 *            是否提示
	 * @return
	 */
	public static boolean isNetwork(Context context, boolean b) {
		ConnectivityManager conn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			if (b) {
				Toast.makeText(
						context,
						context.getResources()
								.getString(R.string.network_error),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return false;
	}
}
