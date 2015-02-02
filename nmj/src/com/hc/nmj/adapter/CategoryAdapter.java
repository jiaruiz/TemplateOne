package com.hc.nmj.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hc.nmj.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryAdapter extends BaseAdapter{
	
	private List<String> mList = new ArrayList<String>();
	private Context mContext;
	
	public CategoryAdapter(Context context, List<String> list) {
		mContext = context;
		mList.add("上衣");
		mList.add("裙子");
		mList.add("裤子");
		mList.add("鞋子");
		mList.add("包包");
		mList.add("配饰");
		mList.add("美妆");
		mList.add("团购");
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_category, null);
			holderView = new HolderView();
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.textView = (TextView) convertView.findViewById(R.id.name);
		holderView.textView.setText(mList.get(position));
		return convertView;
	}
	
	static class HolderView {
		TextView textView;
	}

}
