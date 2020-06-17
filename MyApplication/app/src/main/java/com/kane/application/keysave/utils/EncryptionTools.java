package com.kane.application.keysave.utils;

import java.util.Random;

public class EncryptionTools {

	public static String onPwd(String content, String password){
		return encryption(content, password);
	}

	public static String unOnOwd(String content, String password){
		return decryption(content, password);
	}

	private static String encryption(String content, String password) {
		boolean pwdState = false;
		int pwdLen = 0;
		char[] pwdChars = null;
		if (!"".equals(password) && password != null){
			pwdState = true;
			pwdLen = password.length();
			pwdChars = password.toCharArray();
		}
		Random random = new Random();
		int[] key = {random.nextInt(31), random.nextInt(31),
				random.nextInt(31), random.nextInt(31)};
		int contentLen = content.length();
		char[] conChars = content.toCharArray();
		StringBuilder overContent = new StringBuilder("#key");
		for (int i :
				key) {
			overContent.append("/").append(i);
		}
		for (int i = 0; i < contentLen; i++) {
			int thisChar = conChars[i];
			if (i > 0) {
				if (pwdState)
					thisChar += pwdChars[i % pwdLen];
				switch (i % 4) {
					case 0:
						thisChar -= key[0];
						break;
					case 1:
						thisChar -= key[1];
						break;
					case 2:
						thisChar -= key[2];
						break;
					case 3:
						thisChar -= key[3];
						break;
				}
			}else {
				if (pwdState)
					thisChar += pwdChars[0];
				thisChar -= key[0];
			}
			overContent.append("/").append(Integer.toHexString(thisChar));
		}
		return overContent.toString().toUpperCase();
	}

	private static String decryption(String content, String password) {
		boolean pwdState = false;
		int pwdLen = 0;
		char[] pwdChars = null;
		if (!"".equals(password) && password != null){
			pwdState = true;
			pwdLen = password.length();
			pwdChars = password.toCharArray();
		}
		String[] s = content.split("/");
		int[] key = new int[4];
		for (int i = 0; i < 4; i++) {
			key[i] = Integer.parseInt(s[i + 1]);
		}
		int[] chars = new int[s.length - 5];
		StringBuilder overContent = new StringBuilder();
		for (int j = 0; j < chars.length; j++) {
			chars[j] = Integer.parseInt(s[j + 5], 16);
		}
		for (int i = 0; i < chars.length; i++) {
			int thisChar = chars[i];
			if (i > 0) {
				switch (i % 4) {
					case 0:
						thisChar += key[0];
						break;
					case 1:
						thisChar += key[1];
						break;
					case 2:
						thisChar += key[2];
						break;
					case 3:
						thisChar += key[3];
						break;
				}
				if (pwdState)
					thisChar -= pwdChars[i % pwdLen];
			}else {
				thisChar += key[0];
				if (pwdState)
					thisChar -= pwdChars[0];
			}
			overContent.append((char) thisChar);
		}
		return overContent.toString();
	}
}
