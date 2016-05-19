package com.xiaomai.shopping.utils;

import java.io.FileOutputStream;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.os.Environment;
import android.util.Xml;

import com.xiaomai.shopping.bean.Message;

public class BackMessage {

	public static void backMessage(List<Message> list) {
		try {
			XmlSerializer serializer = Xml.newSerializer();
			FileOutputStream os = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/message.xml");
			serializer.setOutput(os, "utf-8");// 设置参数
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "smss");

			for (Message message : list) {
				serializer.startTag(null, "sms");
				serializer.startTag(null, "uid");
				String uid = message.getUid();
				serializer.text(uid);
				serializer.endTag(null, "uid");

				serializer.startTag(null, "content");
				String content = message.getContent();
				serializer.text(content);
				serializer.endTag(null, "content");

				serializer.startTag(null, "type");
				String type = message.getType();
				serializer.text(type);
				serializer.endTag(null, "type");

				serializer.startTag(null, "time");
				String time = message.getTime();
				serializer.text(time);
				serializer.endTag(null, "time");
				serializer.endTag(null, "sms");

			}
			serializer.endTag(null, "smss");
			serializer.endDocument();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
