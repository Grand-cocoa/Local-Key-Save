package com.kane.application.keysave.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.kane.application.keysave.base.Item;
import com.kane.application.keysave.myapplication.R;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item> {

	int resource;

	public ItemAdapter(@NonNull Context context, int resource, List<Item> list) {
		super(context, resource, list);
		this.resource = resource;
	}

	@NonNull
	@Override
	@SuppressLint("ViewHolder")
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		//return super.getView(position, convertView, parent);
		Item item = getItem(position);
		View view = null;
		if (item != null){
			view = LayoutInflater.from(getContext()).inflate(resource, parent, false);
			((TextView)(view.findViewById(R.id.list_item_name))).setText(item.getName());
			((TextView)(view.findViewById(R.id.list_item_user_name))).setText("用户名：" + item.getUserName());
			if (item.getUserName().length() > 16 || item.getUserPwd().length() > 16) {
				TextView text = view.findViewById(R.id.list_item_user_pwd_line2);
				text.setText("密码：" + item.getUserPwd());
				text.setVisibility(View.VISIBLE);
				view.findViewById(R.id.list_item_user_pwd).setVisibility(View.GONE);
			}else {
				((TextView) (view.findViewById(R.id.list_item_user_pwd))).setText("密码：" + item.getUserPwd());
			}
		}
		return view;
	}
}
