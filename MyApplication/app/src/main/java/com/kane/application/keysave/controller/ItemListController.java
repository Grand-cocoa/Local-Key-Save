package com.kane.application.keysave.controller;

import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.kane.application.keysave.base.Item;
import com.kane.application.keysave.utils.EncryptionTools;

import java.io.IOException;
import java.util.List;

public class ItemListController {
	public static String export(Activity activity, String pwd) throws IOException, ClassNotFoundException {
		ItemController itemController = new ItemController(activity);
		List<Item> all = itemController.findAll();
		String o = JSON.toJSON(all).toString();
		return EncryptionTools.onPwd(o, pwd);
	}

	public static List<Item> the_import(String content, String pwd){
		String s = EncryptionTools.unOnOwd(content, pwd);
		System.out.println(s);
		return JSON.parseArray(s, Item.class);
	}
}
