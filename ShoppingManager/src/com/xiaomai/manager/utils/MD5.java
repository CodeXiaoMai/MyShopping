package com.xiaomai.manager.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

	/**
	 * MD5加密
	 * 
	 * @param password 明文
	 * @return 密文
	 */
	public static String ecode(String password) {
		StringBuffer buffer;
		try {
			// 1.信息摘要器
			MessageDigest digest = MessageDigest.getInstance("md5");
			// 2.把明文变成byte数组
			byte[] bytes = digest.digest(password.getBytes());
			buffer = new StringBuffer();
			// 3.把每一个byte与8个二进制位数与运算
			for (byte b : bytes) {
				int number = b & 0xff;
				// 4.把int类型转换成十六进制
				String numberString = Integer.toHexString(number);
				// 5.如果numberString的长度为1，就在前面补0
				if (numberString.length() == 1) {
					buffer.append("0");
				}
				buffer.append(numberString);
			}
			return buffer.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
