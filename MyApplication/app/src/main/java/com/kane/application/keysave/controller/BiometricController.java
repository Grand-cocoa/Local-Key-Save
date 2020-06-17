package com.kane.application.keysave.controller;

import android.app.Activity;
import android.content.Context;

import java.io.*;

public class BiometricController {
	private final String fileName = "biometric.obj";

	private final File file;

	private final Activity activity;

	public BiometricController(Activity activity) {
		this.file = new File(activity.getFilesDir(), fileName);
		this.activity = activity;
	}

	public boolean saveState(boolean b){
		FileOutputStream outputStream = null;
		ObjectOutputStream os = null;
		try {
			outputStream = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(outputStream);
			os.writeBoolean(b);
			os.close();
			outputStream.close();
		} catch (IOException e) {
			if (os != null) {
				try {
					os.close();
					return false;
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
			if(outputStream != null) {
				try {
					outputStream.close();
					return false;
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		}
		return true;
	}

	public boolean findState(){
		if (file.exists()){
			FileInputStream inputStream = null;
			ObjectInputStream is = null;
			try {
				inputStream = activity.openFileInput(fileName);
				is = new ObjectInputStream(inputStream);
				boolean b = is.readBoolean();
				is.close();
				inputStream.close();
				return b;
			} catch (IOException e) {
				if (is != null) {
					try {
						is.close();
						return false;
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
				if(inputStream != null) {
					try {
						inputStream.close();
						return false;
					} catch (IOException ioException) {
						ioException.printStackTrace();
					}
				}
			}
		}
		return false;
	}
}
