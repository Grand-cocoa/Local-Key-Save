package com.kane.application.keysave.utils;

import android.content.pm.PackageManager;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import com.kane.application.keysave.base.Massage;

import java.util.concurrent.Executor;

/**
 * 系统生物验证工具类，配合内部ActionUtil接口使用
 */
public class BiometricUtil {
	private final Handler handler = new Handler();

	private final Executor executor = handler::post;

	private final AppCompatActivity mainActivity;

	public BiometricUtil(AppCompatActivity activity) {
		mainActivity = activity;
	}

	public boolean test(){
		PackageManager packageManager = mainActivity.getPackageManager();
		return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT);
	}

	//生物认证的setting
	public final void showBiometricPrompt(BiometricUtil.ActionUtil<Massage> actionUtil) {
		BiometricPrompt.PromptInfo promptInfo =
				new BiometricPrompt.PromptInfo.Builder()
						.setTitle("使用系统认证") //设置大标题
						.setSubtitle("使用您的生物认证登录") // 设置标题下的提示
						//.setNegativeButtonText("Cancel") //设置取消按钮
						.setDeviceCredentialAllowed(true) //设置使用其他密码
						.build();

		//需要提供的参数callback
		BiometricPrompt biometricPrompt = new BiometricPrompt(mainActivity,
				executor, new BiometricPrompt.AuthenticationCallback() {
			//各种异常的回调
			@Override
			public void onAuthenticationError(int errorCode,
											  @NonNull CharSequence errString) {
				super.onAuthenticationError(errorCode, errString);
				Toast.makeText(mainActivity.getApplicationContext(),
						"Authentication error: " + errString, Toast.LENGTH_SHORT)
						.show();
				actionUtil.errorAction();
			}

			//认证成功的回调
			@Override
			public void onAuthenticationSucceeded(
					@NonNull BiometricPrompt.AuthenticationResult result) {
				super.onAuthenticationSucceeded(result);
				Toast.makeText(mainActivity.getApplicationContext(), "OK",
						Toast.LENGTH_SHORT)
						.show();
				result.getCryptoObject();
				actionUtil.action();
				// User has verified the signature, cipher, or message
				// authentication code (MAC) associated with the crypto object,
				// so you can use it in your app's crypto-driven workflows.
			}

			//认证失败的回调
			@Override
			public void onAuthenticationFailed() {
				super.onAuthenticationFailed();
				Toast.makeText(mainActivity.getApplicationContext(), "Authentication failed",
						Toast.LENGTH_SHORT)
						.show();
				actionUtil.errorAction();
			}
		});

		// 显示认证对话框
		biometricPrompt.authenticate(promptInfo);
	}

	public interface ActionUtil<T> {
		default T action(){
			return null;
		}

		default T action(T t){
			return t;
		}

		default T errorAction(){
			return null;
		}

		default T errorAction(T t){
			return t;
		}
	}
}
