package com.kane.application.keysave.base;

import java.io.Serializable;

public class Item implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String userName;
	private String userPwd;

	public Item() {}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Item item = (Item) o;

		if (!name.equals(item.name)) return false;
		if (!userName.equals(item.userName)) return false;
		return userPwd.equals(item.userPwd);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + userName.hashCode();
		result = 31 * result + userPwd.hashCode();
		return result;
	}

	public Item(String name, String userName, String userPwd) {
		this.name = name;
		this.userName = userName;
		this.userPwd = userPwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
}
