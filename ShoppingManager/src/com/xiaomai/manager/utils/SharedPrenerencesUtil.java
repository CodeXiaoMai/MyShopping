package com.xiaomai.manager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrenerencesUtil {

	public static final String SHAREDPREFERENCES_NAME = "config";

	/**
	 * 设置是否是第一次运行
	 * 
	 * @param context
	 * @param isFirst
	 */
	public static void setFirstTimeRun(Context context, Boolean isFirst) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isFirstTimeRun", isFirst);
		editor.commit();
	}

	/**
	 * 获得是否是第一次运行
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getIsFirstTimeRun(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean("isFirstTimeRun", true);
	}

	/**
	 * 存储userId
	 * 
	 * @param context
	 * @param userId
	 */
	public static void setUserId(Context context, String userId) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("userId", userId);
		editor.commit();
	}

	/**
	 * 获得userId
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		return sp.getString("userId", "");
	}

	/**
	 * 清除userId
	 * 
	 * @param context
	 */
	public static void clearUserId(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("userId", "");
		edit.commit();
	}

	/**
	 * 设置是否开启省流量模式
	 * 
	 * @param context
	 * @param flag
	 */
	public static void setShengLiuLiang(Context context, boolean flag) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isShengLiuLiang", flag);
		editor.commit();
	}

	/**
	 * 获取是否开启省流量模式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getShengLiuLiang(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean("isShengLiuLiang", false);
	}

	public static void setIsLogOut(Context context, boolean flag) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isLogOut", flag);
		editor.commit();
	}

	public static boolean getIsLogOut(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean("isLogOut", false);
	}
}
