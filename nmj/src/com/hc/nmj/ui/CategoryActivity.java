package com.hc.nmj.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.hc.nmj.R;
import com.hc.nmj.base.GetNetData;
import com.hc.nmj.base.TreeViewAdapter;

public class CategoryActivity extends Activity {

	ExpandableListView expandableListView;
	TreeViewAdapter adapter;
	List<TreeViewAdapter.TreeNode> treeNode;
	
	public String[] groups;
	public String[][] childs;
	//list 有两个字段 id和name
	public List<Map<String,String>> CateList = new ArrayList<Map<String, String>>();;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		expandableListView = (ExpandableListView) findViewById(R.id.expandablelistview);
		expandableListView.setGroupIndicator(null);
		adapter = new TreeViewAdapter(this, 48);
		getData();
        InitData();	
		adapter.updateTreeNode(treeNode);
		expandableListView.setAdapter(adapter);
		//默认第一项打开
		expandableListView.expandGroup(0);
		Listener();
	}
	
	private void Listener(){
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition,long id) {
				// TODO Auto-generated method stub
//				String str = "parent_id = " + groupPosition
//						+ " child_id = " + childPosition;
				
				//类别名称
				String catename = childs[groupPosition][childPosition];
				//类别Id
				String cid = getId(catename);
				
//				Toast.makeText(CategoryActivity.this,
//						cid, Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(CategoryActivity.this, ResultActivity.class);
				intent.putExtra("cid", cid);
				intent.putExtra("catename",catename);
				startActivity(intent);	
				return false;
			}
		});
	}
	
	private void InitData(){
		treeNode = adapter.getTreeNode();
		//父级循环 上面已经定义
		for (int i = 0; i < groups.length; i++) {
			TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
			//赋值父级 从数据资源中心拿
			node.parent = groups[i];
			Log.v("groups",groups[i]);
			//赋值子级
			for (int j = 0; j < childs[i].length; j++) {
				node.childs.add(childs[i][j]);
			}
			treeNode.add(node);
		}
		
	}
	
	//类别数组 第一级和第二级
	private  void getData() {

		String url = "http://www.ankobeauty.com/anko/index.php?a=cate&m=Index&g=API";
		try {
			String jsonstring = GetNetData.getResultForHttpGet(url);
			JSONObject result = new JSONObject(jsonstring);
			JSONArray groupslist = result.getJSONArray("parent");
			JSONObject sub = result.getJSONObject("sub");
			int length = groupslist.length();
			groups = new String[length];
			//定义第一维长度
			childs = new String[length][];
			for (int i = 0; i < length; i++) {
				JSONObject oj = groupslist.getJSONObject(i);
				groups[i] = oj.getString("name");
				JSONArray jsid = sub.getJSONArray(oj.getString("id"));
				int idlen = jsid.length();
				//定义第二维长度
				childs[i]=new String[idlen];
				for (int j = 0; j < idlen; j++) {
					JSONObject idoj = jsid.getJSONObject(j);
					childs[i][j] = idoj.getString("name").toString();
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", idoj.getString("id"));
					map.put("name", idoj.getString("name"));
					CateList.add(map);
				}
			}

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	//根据name获取id
	private String getId(String name){
		
		String id;
		String nullId ="无数据";
		if(CateList!=null){
			for(int i=0; i<CateList.size();i++){
				if(CateList.get(i).get("name")==name){
					id = CateList.get(i).get("id");
					return id;
				}
			}			
		}		
		return nullId;	
	}


}