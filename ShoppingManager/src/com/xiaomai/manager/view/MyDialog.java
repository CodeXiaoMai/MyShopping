package com.xiaomai.manager.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class MyDialog {

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            提示信息
	 * @param positiveListener
	 *            确定按钮的事件
	 * @param negativeListener
	 *            取消按钮的事件
	 */
	public static void showDialog(Context context, String title,
			String message, DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener negativeListener) {
		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message)
				.setPositiveButton("确认", positiveListener)
				.setNegativeButton("取消", negativeListener).setCancelable(false);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}
}
