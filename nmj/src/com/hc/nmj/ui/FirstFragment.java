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

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.hc.nmj.R;
import com.hc.nmj.adapter.IndexGridAdapter;
import com.hc.nmj.base.GetNetData;
import com.hc.nmj.base.RemoteImageHelper;
import com.hc.nmj.bean.Clothes;
import com.hc.nmj.utils.DBUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class FirstFragment extends Fragment {

    ImageLoader imageLoader = ImageLoader.getInstance();

    private Context context;
    private View view;

    GridView ggv;
    IndexGridAdapter gvadapter;

//    RemoteImageHelper lazyImageHelper = new RemoteImageHelper();

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    WindowManager windowManager;
    int sw, sh;

    String ccid;
    String defaultCid = "1";
    private PullToRefreshGridView mPullRefreshGridView;
    private DisplayImageOptions options;

    public static FirstFragment newInstance(String s) {
        FirstFragment newFragment = new FirstFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cid", s);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    public void setOptions() {

        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        Bundle args = getArguments();
        ccid = args != null ? args.getString("cid") : defaultCid;
        setOptions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_fashion, container, false);

        windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        // 屏幕宽度
        sw = windowManager.getDefaultDisplay().getWidth();
        // 屏幕高度
        sh = windowManager.getDefaultDisplay().getHeight();

        mPullRefreshGridView = (PullToRefreshGridView) view.findViewById(R.id.pull_refresh_grid);
        ggv = mPullRefreshGridView.getRefreshableView();

        mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                Log.e("jiarui", "pullup");
                new GetDataTask().execute();
            }

        });

        String[] from = { "img", "sale", "discount", "price", "title"};

        int[] to = { R.id.ggv_img, R.id.ggv_sale, R.id.ggv_discount, R.id.ggv_price, R.id.ggv_title};

        int cccid = Integer.parseInt(ccid);
//        gvadapter = new gvAdapter(context, getGoods(cccid), R.layout.item_goods, from, to);

        clothersLists = DBUtils.queryAll();
        gvadapter = new IndexGridAdapter(getActivity(), clothersLists);
        ggv.setAdapter(gvadapter);
        ggvItemClick();
        return view;
    }

    private void ggvItemClick() {
        ggv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Clothes clothes = (Clothes) ggv.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), GoodsDetailAct.class);
                intent.putExtra("clothes", clothes);
                startActivity(intent);
            }

        });
    }
    
    List<Clothes> clothersLists = new ArrayList<Clothes>();

    private class gvAdapter extends SimpleAdapter {
        ImageLoader imageLoader = ImageLoader.getInstance();

        public gvAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            // TODO Auto-generated constructor stub
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            Map<String, Object> record = (Map<String, Object>) getItem(position);
            TextView price = (TextView) view.findViewById(R.id.ggv_price);
            price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            ImageView imageView = (ImageView) view.findViewById(R.id.ggv_img);
            imageLoader.displayImage(record.get("img").toString(), imageView, options);
            // lazyImageHelper.loadImage(imageView,
            // record.get("img").toString());
            LayoutParams params = imageView.getLayoutParams();
            int imgWidth = (int) Math.ceil(sw * 0.45);
            int imgHeight = imgWidth;
            params.width = imgWidth;
            params.height = imgHeight;
            imageView.setLayoutParams(params);
            return view;
        }
    }

    private ArrayList<Map<String, String>> getGoods(int cateId) {
        
        if (cateId == 1) {
            ArrayList<Map<String, String>> ls = new ArrayList<Map<String, String>>();
            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("img", "assets://images" + cateId + "/" + 1 + ".jpg");
            map1.put("title", "数量 80");
            map1.put("price", "￥" + "118");
            map1.put("sale", "￥" + "100");
            map1.put("discount", "8" + "折");
            
            Map<String, String> map2 = new HashMap<String, String>();
            map2.put("img", "assets://images" + cateId + "/" + 2 + ".jpg");
            map2.put("title", "数量 10");
            map2.put("price", "￥" + "148");
            map2.put("sale", "￥" + "110");
            map2.put("discount", "7" + "折");
            
            Map<String, String> map3 = new HashMap<String, String>();
            map3.put("img", "assets://images" + cateId + "/" + 3 + ".jpg");
            map3.put("title", "数量 40");
            map3.put("price", "￥" + "208");
            map3.put("sale", "￥" + "160");
            map3.put("discount", "8" + "折");
            
            Map<String, String> map4 = new HashMap<String, String>();
            map4.put("img", "assets://images" + cateId + "/" + 4 + ".jpg");
            map4.put("title", "数量 160");
            map4.put("price", "￥" + "199");
            map4.put("sale", "￥" + "130");
            map4.put("discount", "8" + "折");
            
            ls.add(map1);
            ls.add(map2);
            ls.add(map3);
            ls.add(map4);
            return ls;
        } 
        else if (cateId == 2) {
            ArrayList<Map<String, String>> ls = new ArrayList<Map<String, String>>();
            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("img", "assets://images" + cateId + "/" + 1 + ".jpg");
            map1.put("title", "数量 100");
            map1.put("price", "￥" + "128");
            map1.put("sale", "￥" + "100");
            map1.put("discount", "8" + "折");
            
            Map<String, String> map2 = new HashMap<String, String>();
            map2.put("img", "assets://images" + cateId + "/" + 2 + ".jpg");
            map2.put("title", "数量 10");
            map2.put("price", "￥" + "138");
            map2.put("sale", "￥" + "110");
            map2.put("discount", "7" + "折");
            
            Map<String, String> map3 = new HashMap<String, String>();
            map3.put("img", "assets://images" + cateId + "/" + 3 + ".jpg");
            map3.put("title", "数量 6");
            map3.put("price", "￥" + "149");
            map3.put("sale", "￥" + "123");
            map3.put("discount", "8" + "折");
            
            Map<String, String> map4 = new HashMap<String, String>();
            map4.put("img", "assets://images" + cateId + "/" + 4 + ".jpg");
            map4.put("title", "数量 99");
            map4.put("price", "￥" + "129");
            map4.put("sale", "￥" + "81");
            map4.put("discount", "6" + "折");
            
            ls.add(map1);
            ls.add(map2);
            ls.add(map3);
            ls.add(map4);
            return ls;
        } 
        
        else if (cateId == 3) {
            ArrayList<Map<String, String>> ls = new ArrayList<Map<String, String>>();
            Map<String, String> map1 = new HashMap<String, String>();
            map1.put("img", "assets://images" + cateId + "/" + 1 + ".jpg");
            map1.put("title", "数量 109");
            map1.put("price", "￥" + "148");
            map1.put("sale", "￥" + "100");
            map1.put("discount", "8" + "折");
            
            Map<String, String> map2 = new HashMap<String, String>();
            map2.put("img", "assets://images" + cateId + "/" + 2 + ".jpg");
            map2.put("title", "数量 170");
            map2.put("price", "￥" + "148");
            map2.put("sale", "￥" + "100");
            map2.put("discount", "7" + "折");
            
            Map<String, String> map3 = new HashMap<String, String>();
            map3.put("img", "assets://images" + cateId + "/" + 3 + ".jpg");
            map3.put("title", "数量 160");
            map3.put("price", "￥" + "109");
            map3.put("sale", "￥" + "78");
            map3.put("discount", "8" + "折");
            
            Map<String, String> map4 = new HashMap<String, String>();
            map4.put("img", "assets://images" + cateId + "/" + 4 + ".jpg");
            map4.put("title", "数量 10");
            map4.put("price", "￥" + "179");
            map4.put("sale", "￥" + "130");
            map4.put("discount", "8" + "折");
            
            ls.add(map1);
            ls.add(map2);
            ls.add(map3);
            ls.add(map4);
            return ls;
        } 
        
//        String url = "http://www.ankobeauty.com/anko/index.php?a=index&m=Index&g=API";
//        try {
//            String jsonstring = GetNetData.getResultForHttpGet(url);
//            JSONObject result = new JSONObject(jsonstring);
//            JSONArray goodsList = result.getJSONArray("goodslist");
//            int length = goodsList.length();
//            for (int i = 0; i < length; i++) {
//                JSONObject oj = goodsList.getJSONObject(i);
//                String cidStr = oj.getString("cid");// 取出属性
//                String[] arr = cidStr.split(",");
//                // 对比属性
//                for (int j = 0; j < arr.length; j++) {
//                    if (cateId == Integer.parseInt(arr[j])) {
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("title", oj.getString("title").toString());
//                        map.put("price", "￥" + oj.getString("price").toString());
//                        map.put("sale", "￥" + oj.getString("sale").toString());
//                        map.put("img", "http://www.ankobeauty.com/anko" + oj.getString("img") + "_500x1000.jpg");
//                        map.put("discount", oj.getString("discount") + "折");
//                        map.put("url", oj.getString("url").toString());
//                        ls.add(map);
//                    }
//                }
//            }
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        return null;
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // mListItems.addFirst("Added after refresh...");
            // mListItems.addAll(Arrays.asList(result));
            gvadapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshGridView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

}
