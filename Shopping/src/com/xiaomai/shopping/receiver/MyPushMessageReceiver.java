package com.xiaomai.shopping.receiver;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.push.PushConstants;

import com.xiaomai.shopping.bean.Message;

public class MyPushMessageReceiver extends BroadcastReceiver {

	/**
	 * 这是收到推送的回调接口
	 * 
	 * @author XiaoMai
	 *
	 */
	public interface onReceiveMessageListener {
		public void onReceiveMessage(String json);

		public void onReceiveMessage(List<Message> list);
	}

	public static onReceiveMessageListener listener;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			Log.d("bmob", "收到消息:" + intent.getStringExtra("msg"));
			if (listener != null) {
				listener.onReceiveMessage(intent.getStringExtra("msg"));
			}
		}
	}

	// 收到消息:{"alert":"Message [type=系统消息, content=您发布的求购[hh]未能通过审核, time=2016-05-19 09:19:13]"}

	// 收到消息:{"alert":"{\"content\":\"您发布的求购\\\"fjjf\\\"未能通过审核\",\"time\":\"2016-05-17\\n23:20:25\",\"type\":\"系统消息\"}"}
	// msg:{"alert":"{\"content\":\"您发布的求购\\\"fjjf\\\"未能通过审核\",\"time\":\"2016-05-17\\n23:20:25\",\"type\":\"系统消息\"}"}

}
