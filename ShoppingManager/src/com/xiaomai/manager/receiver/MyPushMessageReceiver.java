package com.xiaomai.manager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {

	/**
	 * 这是收到推送的回调接口
	 * 
	 * @author XiaoMai
	 *
	 */
	public interface onReceiveMessageListener {
		public void onReceiveMessage(Intent intent);
	}

	public static onReceiveMessageListener listener;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			Log.d("bmob", "收到消息:" + intent.getStringExtra("msg"));
			if (listener != null) {
				listener.onReceiveMessage(intent);
			}
		}
	}

}
