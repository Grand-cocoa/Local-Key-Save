package com.kane.application.keysave.controller;

import android.app.Activity;
import android.content.Context;
import com.kane.application.keysave.base.Item;
import com.kane.application.keysave.base.Massage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemController {

	private final String fileName = "key_item.obj";

	private final File file;

	private final Activity activity;

	public ItemController(Activity activity) {
		this.file = new File(activity.getFilesDir(), fileName);
		this.activity = activity;
	}

	//增加
	public Massage submit(String name, String userName, String userPwd){
		Item item = new Item(name, userName, userPwd);
		List<Item> itemList;
		try {
			itemList = findAll();
		} catch (IOException e) {
			e.printStackTrace();
			return new Massage(false, "出现IO错误");
		} catch (ClassNotFoundException e) {
			return new Massage(false, "出现未知错误");
		}
		if (itemList != null) {
			for (Item value :
					itemList) {
				if (item.equals(value)) {
					return new Massage(false, "已存在相同条目\n名称、用户名、密码都相同的条目。");
				}
			}
		}else {
			itemList = new ArrayList<>();
		}
		itemList.add(item);
		if (!saveObject(itemList)){
			return new Massage(false, "出现未知错误");
		}
		return new Massage(true, "保存成功");
	}

	public Massage submit(List<Item> list){
		List<Item> itemList;
		try {
			itemList = findAll();
		} catch (IOException e) {
			e.printStackTrace();
			return new Massage(false, "出现IO错误");
		} catch (ClassNotFoundException e) {
			return new Massage(false, "出现未知错误");
		}
		itemList.addAll(list);
		if (!saveObject(itemList)){
			return new Massage(false, "出现未知错误");
		}
		return new Massage(true, "保存成功");
	}

	//追加
	public Massage submit(List<Item> list, boolean state){
		List<Item> itemList;
		try {
			itemList = findAll();
		} catch (IOException e) {
			e.printStackTrace();
			return new Massage(false, "出现IO错误");
		} catch (ClassNotFoundException e) {
			return new Massage(false, "出现未知错误");
		}
		if (state){
			for (Item item :
					list) {
				itemList.remove(item);
			}
		}else {
			for (Item item :
					itemList) {
				list.remove(item);
			}
		}
		itemList.addAll(list);
		if (!saveObject(itemList)){
			return new Massage(false, "出现未知错误");
		}
		return new Massage(true, "保存成功");
	}

	//保存
	public boolean saveObject(Object o){
		FileOutputStream outputStream = null;
		ObjectOutputStream os = null;
		try {
			outputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(outputStream);
			os.writeObject(o);
			os.close();
			outputStream.close();
		} catch (Exception e) {
			if (os != null){
				try {
					os.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (outputStream != null){
				try {
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return true;
	}

	//查找
	@SuppressWarnings("unchecked")
	public List<Item> findAll() throws IOException, ClassNotFoundException {
		if (file.exists()){
			List<Item> list;
			FileInputStream inputStream = activity.openFileInput(fileName);
			ObjectInput objectInput = new ObjectInputStream(inputStream);
			Object o = objectInput.readObject();
			if (o instanceof List){
				list = (List<Item>) o;
			}else {
				return new ArrayList<>();
			}
			objectInput.close();
			inputStream.close();
			return list;
		}
		return new ArrayList<>();
	}

	//修改
	public Massage update(int index, String name, String userName, String pwd){
		List<Item> all;
		try {
			all = findAll();
		} catch (IOException e) {
			return new Massage(false, "IOException");
		} catch (ClassNotFoundException e) {
			return new Massage(false, "ClassNotFoundException");
		}
		if (name != null && !"".equals(name))
			all.get(index).setName(name);
		if (userName != null && !"".equals(userName))
			all.get(index).setUserName(userName);
		if (pwd != null && !"".equals(pwd))
			all.get(index).setUserPwd(pwd);
		boolean info = saveObject(all);
		return new Massage(info, info + "");
	}

	public Massage delete(int id){
		List<Item> all;
		try {
			all = findAll();
		} catch (IOException e) {
			return new Massage(false, "IOException");
		} catch (ClassNotFoundException e) {
			return new Massage(false, "ClassNotFoundException");
		}
		all.remove(id);
		boolean b = saveObject(all);
		return new Massage(b, b + "");
	}

	public int check(Item item){
		List<Item> itemList;
		try {
			itemList = findAll();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} catch (ClassNotFoundException e) {
			return -1;
		}
		if (itemList != null) {
			return itemList.indexOf(item);
		}
		return -1;
	}

	public Massage check(List<Item> list){
		List<Item> itemList;
		try {
			itemList = findAll();
		} catch (IOException e) {
			e.printStackTrace();
			return new Massage(false, "出现IO错误");
		} catch (ClassNotFoundException e) {
			return new Massage(false, "出现未知错误");
		}
		for (Item item :
				list) {
			if (itemList != null) {
				for (Item value :
						itemList) {
					if (item.equals(value)) {
						return new Massage(false, "已存在");
					}
				}
			}
		}
		return new Massage(true, "");
	}
}
