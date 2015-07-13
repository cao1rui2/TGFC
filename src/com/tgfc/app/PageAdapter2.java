package com.tgfc.app;

import java.util.List;

import com.tgfc.app.PageAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PageAdapter2 extends ArrayAdapter<Page> {
	
	private int resourceId;
	
	public PageAdapter2(Context context, int textViewResourceId, List<Page> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Page page = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.pageTitle2 = (TextView) view.findViewById(R.id.page_title2);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.pageTitle2.setText(page.getTitle());
		return view;
	}
	
	class ViewHolder {
		TextView pageTitle2;
	}

}
