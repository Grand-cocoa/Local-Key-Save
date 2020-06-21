package com.kane.application.keysave.myapplication;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.kane.application.keysave.base.Item;
import com.kane.application.keysave.base.Massage;
import com.kane.application.keysave.controller.ItemAdapter;
import com.kane.application.keysave.controller.ItemController;
import com.kane.application.keysave.controller.ItemListController;

import java.io.IOException;
import java.util.List;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

	Button export;
	Button the_import;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		String login = intent.getStringExtra("login");
		if (!"true".equals(login)){
			intent = new Intent(ListActivity.this, LoginActivity.class);
			startActivity(intent);
			ListActivity.this.finish();
		}else {
			setContentView(R.layout.activity_list);
			ListView listView = creatListView();
			listView.setOnItemClickListener(this);
			export = findViewById(R.id.export);
			the_import = findViewById(R.id.the_import);
			export.setOnClickListener(this);
			the_import.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.export:
				export();
				break;
			case R.id.the_import:
				the_import();
				break;
		}
	}

	//导入
	private void the_import() {
		View view = getLayoutInflater().inflate(R.layout.activity_the_import, null);
		new AlertDialog.Builder(this)
				.setTitle("导入")
				.setView(view)
				.setNegativeButton("覆盖", (dialog, which) -> importAction(view, true))
				.setNeutralButton("追加", (dialog, which) -> importAction(view, false))
				.setPositiveButton("取消", null)
				.create()
				.show();
	}

	private void importAction(View view, boolean state) {
		String main = ((TextView)view.findViewById(R.id.import_edit_main)).getText().toString();
		String key = ((TextView)view.findViewById(R.id.import_edit_key)).getText().toString();
		List<Item> list = ItemListController.the_import(main, key);
		ItemController controller = new ItemController(this);
		if (state){
			controller.saveObject(list);
			creatListView();
		}else {
			if (controller.check(list).isReturnValue()){
				controller.submit(list);
				creatListView();
			}else {
				new AlertDialog.Builder(this)
						.setTitle("导入")
						.setMessage("存在相同条目")
						.setNegativeButton("取消", null)
						.setNegativeButton("保留新的", (dialog, which)
								-> {
									if (!controller.submit(list, false).isReturnValue())
										Toast.makeText(getApplicationContext(), "出现错误", Toast.LENGTH_SHORT).show();
									else {
										Toast.makeText(getApplicationContext(), "完成", Toast.LENGTH_SHORT).show();
										creatListView();
									}
								})
						.setPositiveButton("保留旧的", (dialog, which)
								-> {
							if (!controller.submit(list, false).isReturnValue())
								Toast.makeText(getApplicationContext(), "出现错误", Toast.LENGTH_SHORT).show();
							else {
								Toast.makeText(getApplicationContext(), "完成", Toast.LENGTH_SHORT).show();
								creatListView();
							}
						})
						.create()
						.show();
			}
		}
	}

	//导出
	private void export() {
		View view = getLayoutInflater().inflate(R.layout.activity_export, null);
		new AlertDialog.Builder(this)
				.setTitle("导出")
				.setView(view)
				.setPositiveButton("确认导出", (dialog, which) -> exportAction(view))
				.setNegativeButton("取消", null)
				.create()
				.show();
	}

	private void exportAction(View view) {
		String textView = ((TextView)view.findViewById(R.id.key)).getText().toString();
		String export = null;
		try {
			export = ItemListController.export(this, textView);
		} catch (Exception e) {
			new AlertDialog.Builder(this)
					.setTitle("导出")
					.setMessage("导出失败")
					.setNegativeButton("取消", null)
					.create()
					.show();
		}
		//获取剪贴板管理器：
		ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		// 创建普通字符型ClipData
		ClipData mClipData = ClipData.newPlainText("Label", export);
		// 将ClipData内容放到系统剪贴板里。
		assert cm != null;
		cm.setPrimaryClip(mClipData);
		Toast.makeText(getApplicationContext(), "已复制到剪切板", Toast.LENGTH_SHORT).show();
	}

	//刷新列表
	private ListView creatListView() {
		ItemController submit = new ItemController(this);
		List<Item> all = null;
		try {
			all = submit.findAll();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ItemAdapter adapter = new ItemAdapter(this, R.layout.activity_user_item, all);

		ListView listView = findViewById(R.id.pwd_list);
		listView.setAdapter(adapter);
		return listView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String name = ((TextView)view.findViewById(R.id.list_item_name)).getText().toString();
		String userName = ((TextView)view.findViewById(R.id.list_item_user_name)).getText().toString().substring(4);
		String userPwd = ((TextView)view.findViewById(R.id.list_item_user_pwd))
				.getText().toString().equals("undefined") ?
				((TextView)view.findViewById(R.id.list_item_user_pwd_line2)).getText().toString().substring(3) :
				((TextView)view.findViewById(R.id.list_item_user_pwd)).getText().toString().substring(3);
		new AlertDialog.Builder(this)
				.setTitle(name)
				.setMessage("用户名：" + userName + "\n密码：" + userPwd + "\n\n点击修改进行修改或复制")
				.setNegativeButton("删除", (dialog, which) -> showDeleteAlert(id, name))
				.setNeutralButton("修改", (dialog, which) -> showEditAlert(id, name, userName, userPwd))
				.setPositiveButton("关闭", null)
				.show();
	}

	//删除窗口
	private void showDeleteAlert(long id, String name) {
		new AlertDialog.Builder(this)
				.setTitle("删除")
				.setMessage("你确定要删除  " + name + "  吗？")
				.setPositiveButton("确定", (dialog, which) -> deleteItem((int)id))
				.setNegativeButton("取消", null)
				.create()
				.show();
	}

	private void deleteItem(int index){
		Massage delete = new ItemController(this).delete(index);
		if (delete.isReturnValue()) {
			Toast.makeText(getApplicationContext(), "完成", Toast.LENGTH_SHORT).show();
			creatListView();
		}else
			new AlertDialog.Builder(this)
					.setTitle("删除")
					.setMessage("操作失败")
					.setNegativeButton("取消", null)
					.create()
					.show();
	}

	//修改窗口
	private void showEditAlert(long id, String name, String userName, String userPwd) {
		View view = getLayoutInflater().inflate(R.layout.activity_item_edit, null);
		((TextView)view.findViewById(R.id.item_edit_index)).setText(id + "");
		((TextView)view.findViewById(R.id.item_edit_name)).setText(name);
		((TextView)view.findViewById(R.id.item_edit_user_name)).setText(userName);
		((TextView)view.findViewById(R.id.item_edit_user_pwd)).setText(userPwd);
		new AlertDialog.Builder(this)
				.setTitle("修改")
				.setView(view)
				.setPositiveButton("修改", ((dialog, which) -> editItem(view)))
				.setNegativeButton("取消", null)
				.create()
				.show();
	}

	//执行修改
	private void editItem(View view) {
		int index = Integer.parseInt(((TextView) view.findViewById(R.id.item_edit_index)).getText().toString());
		String name = ((TextView) view.findViewById(R.id.item_edit_name)).getText().toString();
		String userName = ((TextView) view.findViewById(R.id.item_edit_user_name)).getText().toString();
		String userPwd = ((TextView) view.findViewById(R.id.item_edit_user_pwd)).getText().toString();
		ItemController controller = new ItemController(this);
		Massage update = controller.update(index, name, userName, userPwd);
		if (update.isReturnValue()){
			Toast.makeText(getApplicationContext(), "完成", Toast.LENGTH_SHORT).show();
			creatListView();
		}else
			new AlertDialog.Builder(this)
					.setTitle("修改")
					.setMessage("操作失败")
					.setNegativeButton("取消", null)
					.create()
					.show();
	}
}
