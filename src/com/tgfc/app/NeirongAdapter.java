package com.tgfc.app;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NeirongAdapter extends ArrayAdapter<Neirong> {
	
	private int resourceId;
	
	public NeirongAdapter(Context context, int textViewResourceId, List<Neirong> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Neirong neirong = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.neirongName = (TextView) view.findViewById(R.id.neirong_name);
			viewHolder.neirongContent = (TextView) view.findViewById(R.id.neirong_content);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.neirongName.setText(neirong.getName());
		viewHolder.neirongContent.setText(neirong.getContent());
		return view;
	}
	
	class ViewHolder {
		TextView neirongName;
		TextView neirongContent;
	}
	

}
