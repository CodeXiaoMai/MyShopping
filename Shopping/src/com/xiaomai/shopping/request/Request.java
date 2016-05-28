package com.xiaomai.shopping.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class Request {

	public static String uri = "http://10.153.103.180:8080/Shopping/RegisterServlet.action?";

	public static String getDate(String urlSocre) {
		StringBuffer getString = new StringBuffer();
		try {

			// urlSocre = new String(urlSocre.getBytes("utf-8"), "iso-8859-1");

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
}
