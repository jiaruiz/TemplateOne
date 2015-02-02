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
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hc.nmj.R;
import com.hc.nmj.base.GetNetData;
import com.hc.nmj.base.RemoteImageHelper;

public class ResultActivity extends Activity {
	
	
	private TextView tvcate;
	private ImageView ivback;
	RemoteImageHelper lazyImageHelper = new RemoteImageHelper();
	
	ListView lv;
	
	WindowManager windowManager;
    int sw,sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);
		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		//屏幕宽度
		sw = windowManager.getDefaultDisplay().getWidth();
		//屏幕高度
		sh = windowManager.getDefaultDisplay().getHeight();
		
		tvcate = (TextView) findViewById(R.id.catename);
		ivback = (ImageView) findViewById(R.id.back);
		Intent intent= getIntent();
		String catename= intent.getStringExtra("catename");
		String cid = intent.getStringExtra("cid");
		tvcate.setText(catename);
		ivback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ResultActivity.this.finish();				
			}
        	       	
        });
		
		lv = (ListView)findViewById(R.id.lve);
		
		String[] from = { "title","price","sale","discount","img","url","info"};
        int[] to = { R.id.title, R.id.price, R.id.sale, R.id.discount, R.id.img,R.id.url,R.id.info};	
        SimpleAdapter adapter = new MyAdapter(this, getGoods(cid),R.layout.record_row, from, to);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {  
            	ListView listView = (ListView)parent;
            	HashMap<String, Object> map = (HashMap<String, Object>)listView.getItemAtPosition(position); 
            	String url = map.get("url").toString();
				Intent it = new Intent();
				Intent intent = new Intent(ResultActivity.this,TaobaoActivity.class);
				intent.putExtra("url", url);	
				startActivity(intent);	
            
            }  
        });
	}
	
	private List<Map<String, String>> getGoods(String cid){
		   List<Map<String, String>> list = new ArrayList<Map<String,  String>>();	   
			String url = "http://www.ankobeauty.com/anko/index.php?a=result&m=Index&g=API";
			
			if(cid.trim().equals("")||cid.trim()==null){
				cid = "0";
			}
			
			url = url + "&cid="+ cid;
			Log.v("url",url);
			try {
				String jsonstring = GetNetData.getResultForHttpGet(url);
				
				JSONObject result = new JSONObject(jsonstring);
				JSONArray goodsList = result.getJSONArray("goodslist");	
				int length = goodsList.length();
			    for(int i = 0; i < length; i++)
				{
			    	 Map<String, String> map = new HashMap<String, String>();
			    	 JSONObject oj = goodsList.getJSONObject(i); 
			    	 map.put("title",ToDBC(oj.getString("title").toString()));
			    	 map.put("price","￥"+oj.getString("price").toString());
			    	 map.put("sale", "￥"+oj.getString("sale").toString());
			    	 map.put("img", "http://www.ankobeauty.com/anko"+oj.getString("img"));
			    	 map.put("discount", oj.getString("discount")+"折");
			    	 map.put("url", oj.getString("url").toString());
			    	 map.put("info", "推荐理由："+oj.getString("info"));
			    	 list.add(map);
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

		   return list;
		
	}

	public class MyAdapter extends SimpleAdapter {

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			Map<String, Object> record = (Map<String, Object>) getItem(position);

			TextView price = (TextView) view.findViewById(R.id.price);
			price.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

			ImageView imageView = (ImageView) view.findViewById(R.id.img);
			lazyImageHelper.loadImage(imageView, record.get("img").toString());
			LayoutParams params = imageView.getLayoutParams();
			int imgWidth = (int) Math.ceil(sw * 0.45);
			int imgHeight = imgWidth;
			params.width = imgWidth;
			params.height = imgHeight;
			imageView.setLayoutParams(params);

			return view;
		}

	}

    public static String ToDBC(String input) {  
        char[] c = input.toCharArray();  
        for (int i = 0; i< c.length; i++) {  
            if (c[i] == 12288) {  
              c[i] = (char) 32;  
              continue;  
            }if (c[i]> 65280&& c[i]< 65375)  
               c[i] = (char) (c[i] - 65248);  
            }  
        return new String(c);  
     }  
}
