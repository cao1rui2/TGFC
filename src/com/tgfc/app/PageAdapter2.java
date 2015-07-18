package com.tgfc.app;

import java.util.List;

import com.tgfc.app.PageAdapter.ViewHolder;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PageAdapter2 extends ArrayAdapter<Page2> {
	
	private int resourceId;
	
	public PageAdapter2(Context context, int textViewResourceId, List<Page2> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Page2 page = getItem(position);
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
		Spannable orange = new SpannableString(page.getReplynum());
		orange.setSpan(new ForegroundColorSpan(Color.rgb(139, 0, 0)), 0, page.getReplynum().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		viewHolder.pageTitle2.setText(page.getTitle() + "   ");
		viewHolder.pageTitle2.append(orange);
		return view;
	}
	
	class ViewHolder {
		TextView pageTitle2;
	}

}
