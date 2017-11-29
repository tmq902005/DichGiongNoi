package com.education.voicetranslator.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.education.voicetranslator.R;

public class QspinnerAdapter extends ArrayAdapter<ItemSpinner> {
	int groupid;
	Activity context;
	ArrayList<ItemSpinner> list;
	LayoutInflater inflater;

	public QspinnerAdapter(Context context2, int groupid, int id,
			ArrayList<ItemSpinner> list) {
		super(context2, id, list);
		this.list = list;
		inflater = (LayoutInflater) context2
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.groupid = groupid;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View itemView = inflater.inflate(groupid, parent, false);
		ImageView imageView = itemView.findViewById(R.id.qicon);
		imageView.setImageResource(list.get(position).getImageId());
		TextView textView = itemView
				.findViewById(R.id.qnamelanguage);
		textView.setText(list.get(position).getText());
		return itemView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);

	}
}
