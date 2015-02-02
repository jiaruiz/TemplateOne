package com.hc.nmj.base;

import java.util.ArrayList;
import java.util.List;






import com.hc.nmj.R;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TreeViewAdapter extends BaseExpandableListAdapter {

	public static final int ItemHeight = 55;
	public static final int PaddingLeft = 38;
	private int myPaddingLeft = 0;

	static public class TreeNode {
		public Object parent;
		public List<Object> childs = new ArrayList<Object>();
	}

	List<TreeNode> treeNodes = new ArrayList<TreeNode>();
	Context parentContext;

	public TreeViewAdapter(Context context, int myPaddingLeft) {
		parentContext = context;
		this.myPaddingLeft = myPaddingLeft;

	}

	public List<TreeNode> getTreeNode() {
		return treeNodes;
	}

	public void updateTreeNode(List<TreeNode> nodes) {
		treeNodes = nodes;
	}

	public void removeAll() {
		treeNodes.clear();
	}
	
	static public TextView getTextView(Context context) {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ItemHeight);
		TextView textView = new TextView(context);
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		return textView;
	}
	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return treeNodes.get(groupPosition).childs.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView textView = getTextView(this.parentContext);
		textView.setText(getChild(groupPosition, childPosition).toString());
		textView.setPadding(myPaddingLeft + PaddingLeft, 0, 0, 0);
		return textView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return treeNodes.get(groupPosition).childs.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return treeNodes.get(groupPosition).parent;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return treeNodes.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.group, null);
		}
        TextView group = (TextView) view.findViewById(R.id.groupto);
        group.setText(getGroup(groupPosition).toString());
		
		ImageView imageview = (ImageView)view.findViewById(R.id.tubiao);
		if (isExpanded) {		
			imageview.setBackgroundResource(R.drawable.narrow_select);
		} else {
			imageview.setBackgroundResource(R.drawable.narrow);
		}

		return view;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
