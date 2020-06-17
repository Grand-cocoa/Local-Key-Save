package com.kane.application.keysave.base;

public class Massage {

	private boolean returnValue;
	private String massage;

	public boolean isReturnValue() {
		return returnValue;
	}

	public void setReturnValue(boolean returnValue) {
		this.returnValue = returnValue;
	}

	public String getMassage() {
		return massage;
	}

	public void setMassage(String massage) {
		this.massage = massage;
	}

	public Massage(boolean returnValue, String massage) {
		this.returnValue = returnValue;
		this.massage = massage;
	}
}
