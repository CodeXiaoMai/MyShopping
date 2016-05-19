package com.xiaomai.shopping.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

public class Request {

	public static String getDate(String urlSocre, Context context) {
		StringBuffer getString = new StringBuffer();
		try {

			urlSocre = new String(urlSocre.getBytes("utf-8"), "iso-8859-1");

			URL url = new URL(urlSocre);
			URLConnection urlcon = url.openConnection();
			urlcon.setConnectTimeout(5000);
			InputStream input = urlcon.getInputStream();
			int flag = -1;
			while ((flag = input.read()) != -1) {
				getString.append((char) flag);
			}

		} catch (IOException e) {
			e.printStackTrace();
			getString.append("timeOut");
		}
		String flag = new String();
		try {
			flag = new String(getString.toString().getBytes("iso-8859-1"),
					"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static String getDate(String urlSocre) {

		StringBuffer getString = new StringBuffer();
		try {

			urlSocre = new String(urlSocre.getBytes("utf-8"), "iso-8859-1");

			URL url = new URL(urlSocre);
			URLConnection urlcon = url.openConnection();
			urlcon.setConnectTimeout(5000);
			InputStream input = urlcon.getInputStream();
			int flag = -1;
			while ((flag = input.read()) != -1) {
				getString.append((char) flag);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String flag = new String();
		try {
			flag = new String(getString.toString().getBytes("iso-8859-1"),
					"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
}
