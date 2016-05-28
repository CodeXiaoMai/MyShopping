package com.xiaomai.shopping.module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiaomai.shopping.R;

public class MoreActivity extends Activity {

	private WebView webview;
	private ProgressDialog dialog;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		webview = (WebView) findViewById(R.id.webview);
		webview.requestFocus();
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new WebViewClient());
		webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webview.loadUrl("https://ai.m.taobao.com/index.html?pid=mm_32549094_7052631_23486504");
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// newProgress 1-100之间的整数
				if (newProgress == 100) {
					// 网页加载完毕,关闭ProgressDialog
					closeDialog();
				} else {
					// 网页正在加载
					openDialog(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

			private void closeDialog() {
				if (null != dialog && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
			}

			private void openDialog(int newProgress) {
				if (dialog == null) {
					dialog = new ProgressDialog(MoreActivity.this);
					dialog.setTitle("正在加载");
					dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					dialog.setProgress(newProgress);
					dialog.show();
				} else {
					dialog.setProgress(newProgress);
				}
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 点击返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webview.canGoBack()) {
				webview.goBack();// 返回上一页面
				return true;
			} else {
				// 退出程序
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
