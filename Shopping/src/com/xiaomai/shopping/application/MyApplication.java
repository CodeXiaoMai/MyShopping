package com.xiaomai.shopping.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import uk.co.senab.photoview.log.Logger;
import android.app.Application;
import android.widget.Toast;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.exception.BmobException;

import com.xiaomai.shopping.bean.User;
import com.xiaomai.shopping.messagehandler.MyMessageHandler;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		// 只有主进程运行的时候才需要初始化
		if (getApplicationInfo().packageName.equals(getMyProcessName())) {
			// NewIM初始化
			BmobIM.init(this);
			// 注册消息接收器
			BmobIM.registerDefaultMessageHandler(new MyMessageHandler());

		}
	}

	/**
	 * 获取当前运行的进程名
	 * 
	 * @return
	 */
	public static String getMyProcessName() {
		try {
			File file = new File("/proc/" + android.os.Process.myPid() + "/"
					+ "cmdline");
			BufferedReader mBufferedReader = new BufferedReader(new FileReader(
					file));
			String processName = mBufferedReader.readLine().trim();
			mBufferedReader.close();
			return processName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
}