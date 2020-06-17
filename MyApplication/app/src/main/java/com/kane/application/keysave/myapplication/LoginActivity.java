package com.kane.application.keysave.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.kane.application.keysave.base.Massage;
import com.kane.application.keysave.controller.BiometricController;
import com.kane.application.keysave.utils.BiometricUtil;

import java.io.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

	EditText pwd;
	Button login;
	Button biometricLogin;
	String fileName = "pwd.obj";
	File file;
	String thisPwd;
	Intent intent;
	BiometricUtil biometricUtil;
	boolean biometric = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		pwd = findViewById(R.id.password);
		login = findViewById(R.id.login);
		login.setOnClickListener(this);
		biometricLogin = findViewById(R.id.biometric_login);
		biometricLogin.setOnClickListener(this);
		biometricUtil = new BiometricUtil(this);
		biometric = biometricUtil.test();
		intent = getIntent();
		if (intent == null) {
			intent = new Intent();
		}
		if (findPwd()){
			login.setText("登录");
			if(biometric && new BiometricController(this).findState()){
				biometricLogin.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login:
				if (pwd.getText().toString().length() > 5) {
					if (findPwd()){
						if (chick()){
							jumpActivity();
						}else {
							new AlertDialog.Builder(this)
									.setTitle("Error...")
									.setMessage("密码错误")
									.setPositiveButton("确定",null)
									.show();
						}
					}else {
						new AlertDialog.Builder(this)
								.setTitle("确认")
								.setMessage("确定设置此密码吗？")
								.setPositiveButton("确定", (dialog, which) -> setPwd())
								.setNegativeButton("取消",null)
								.show();
					}
				} else {
					new AlertDialog.Builder(this)
							.setTitle("Error...")
							.setMessage("密码必须大于5个字符")
							.setPositiveButton("确定",null)
							.show();
				}
				break;
			case R.id.biometric_login:
				biometricUtil.showBiometricPrompt(new BiometricUtil.ActionUtil<Massage>() {
					@Override
					public Massage action() {
						jumpActivity();
						return null;
					}

					@Override
					public Massage errorAction() {
						new AlertDialog.Builder(LoginActivity.this)
								.setTitle("Error...")
								.setMessage("认证失败")
								.setPositiveButton("确定",null)
								.show();
						return null;
					}
				});
				break;
		}
	}

	//检查密码是否正确
	private boolean chick() {
		//若当前没有获得正确密码则获取
		if (thisPwd == null) {
			FileInputStream is = null;
			ObjectInputStream ois = null;
			try {
				is = this.openFileInput(fileName);
				ois = new ObjectInputStream(is);
				Object o = ois.readObject();
				if (o instanceof String) {
					thisPwd = (String) o;
				}
				ois.close();
				is.close();
			} catch (Exception e) {
				if (ois != null) {
					try {
						ois.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		return pwd.getText().toString().equals(thisPwd);
	}

	//检查密码文件是否存在
	private boolean findPwd() {
		file = new File(this.getFilesDir(), fileName);
		return file.exists();
	}

	//设置密码
	private void setPwd(){
		FileOutputStream os = null;
		ObjectOutputStream oos = null;
		try {
			os = this.openFileOutput(fileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(os);
			oos.writeObject(pwd.getText().toString());
			oos.close();
			os.close();
		}catch (Exception e) {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("密码设置成功\n点击确定进入")
				.setPositiveButton("确定", (dialog, which) -> jumpActivity())
				.show();
	}

	//跳转页面
	private void jumpActivity() {
		String jump = intent.getStringExtra("jumpTo");
		if (jump != null) {
			switch (jump) {
				case "MainActivity":
					intent = new Intent(LoginActivity.this, MainActivity.class);
					break;
				case "ListActivity":
					intent = new Intent(LoginActivity.this, ListActivity.class);
					break;
				default:
					new AlertDialog.Builder(LoginActivity.this)
							.setTitle("Error...")
							.setMessage("回调错误，点击确定回到主页面")
							.setPositiveButton("确定",(dialog, which) -> intent = new Intent(LoginActivity.this, MainActivity.class))
							.show();
			}
		} else {
			intent = new Intent(LoginActivity.this, MainActivity.class);
		}
		startActivity(intent);
		LoginActivity.this.finish();
	}

}
