package com.kane.application.keysave.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.kane.application.keysave.base.Massage;
import com.kane.application.keysave.controller.BiometricController;
import com.kane.application.keysave.controller.ItemController;
import com.kane.application.keysave.utils.BiometricUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	Button submit;
	Button find;
	Button biometricSwitch;
	EditText name;
	EditText userName;
	EditText userPwd;
	BiometricUtil biometricUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		submit = findViewById(R.id.button_submit);
		find = findViewById(R.id.button_find);
		biometricSwitch = findViewById(R.id.biometric_switch);
		name = findViewById(R.id.item_name);
		userName = findViewById(R.id.item_user_name);
		userPwd = findViewById(R.id.item_user_pwd);
		submit.setOnClickListener(this);
		find.setOnClickListener(this);
		biometricSwitch.setOnClickListener(this);
		biometricUtil = new BiometricUtil(this);
		if(biometricUtil.test()){
			biometricSwitch.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.button_submit:
				onClickSubmit(v);
				break;
			case R.id.button_find:
				onClickFind(v);
				break;
			case R.id.biometric_switch:
				onClickBiometricSwitch(v);
				break;
		}
	}

	private void onClickBiometricSwitch(View v) {
		BiometricController controller = new BiometricController(this);
		boolean state = controller.findState();
		new AlertDialog.Builder(this)
				.setTitle("确认安全")
				.setMessage("开启系统认证将导致所有使用此设备的人均可打开此软件\n"
				 + "当前状态：" + (state ? "已开启" : "未开启"))
				.setPositiveButton(state ? "关闭系统认证" : "开启系统认证", (dialog, which) -> setBiometric(controller, !state))
				.setNegativeButton("取消", null)
				.show();
	}

	private void setBiometric(BiometricController controller, boolean b) {
		if(controller.saveState(b)){
			new AlertDialog.Builder(this)
					.setTitle("结果")
					.setMessage("已" + (b ? "开启" : "关闭") + "系统认证")
					.setPositiveButton("继续", null)
					.show();
		}
	}

	public void onClickSubmit(View v) {
		new AlertDialog.Builder(this)
				.setTitle("确认安全")
				.setMessage("下面将显示您的密码，请确认安全后继续...")
				.setPositiveButton("继续", (dialog, which) -> showPwd())
				.setNegativeButton("取消", null)
				.show();
	}

	void showPwd(){
		final String name = this.name.getText().toString();
		final String userName = this.userName.getText().toString();
		final String userPwd = this.userPwd.getText().toString();
		final ItemController itemController = new ItemController(this);
		new AlertDialog.Builder(this)
				.setTitle("提交")
				.setMessage("名称：" + name +
						"\n用户名：" + userName +
						"\n密码：" + userPwd +
						"\n是否正确？")
				.setPositiveButton("是", (dialog, which) -> showMsg(itemController.submit(name, userName, userPwd)))
				.setNegativeButton("否", null)
				.show();
	}

	private void showMsg(Massage massage){
		new AlertDialog.Builder(this)
				.setTitle(massage.isReturnValue() ? "成功" : "错误")
				.setMessage(massage.getMassage())
				.setPositiveButton("确定", null)
				.show();
	}

	public void onClickFind(View v){
		Intent intent = new Intent();
		intent.putExtra("jumpTo", "ListActivity");
		intent.setClass(MainActivity.this, LoginActivity.class);
		startActivity(intent);
	}
}
