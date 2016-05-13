package com.xiaomai.shopping.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES {
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	// public static String key = "laia@416";

	// 加密
	public static String encryptDES(String encryptString) {
		String encryptKey = "20120401";
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher;
		byte[] encryptedData = null;
		try {
			cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			try {
				cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
				try {
					encryptedData = cipher.doFinal(encryptString
							.getBytes("utf-8"));
				} catch (IllegalBlockSizeException | BadPaddingException
						| UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Base64.encode(encryptedData);
	}

	// 解密
	public static String decryptDES(String decryptString, String decryptKey) {
		byte[] byteMi = Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");

		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);
			return new String(decryptedData, "utf-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	static public void main(String argv[]) throws Exception {
		String plaintext = "13788888888";
		String ciphertext = DES.encryptDES(plaintext);
		System.out.println("明文：" + plaintext);
		System.out.println("密钥：" + "20120401");
		System.out.println("密文：" + ciphertext);
		System.out.println("解密后："
				+ DES.decryptDES("Dx3EsJBIvicPGgOCjfBrew==", "20120401"));
	}
}
