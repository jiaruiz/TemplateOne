package com.hc.nmj.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hc.nmj.R;
import com.hc.nmj.base.GetNetData;
import com.hc.nmj.bean.Cityinfo;
import com.hc.nmj.bean.Clothes;
import com.hc.nmj.utils.CitycodeUtil;
import com.hc.nmj.utils.DBUtils;
import com.hc.nmj.utils.FileUtil;
import com.hc.nmj.utils.LocationApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DiscoverActivity extends FragmentActivity {

    // fragment管理器
    private FragmentManager mFragmentManager;

    // fragment事务管理
    private FragmentTransaction fragmentTransaction;

    /** 下拉框内容 */
    private SpinnerPopupWindow mspinnerWindow = null;

    private PullToRefreshListView mPullRefreshListView;

    private ListView lv;

    DiscoverAdapter adapter;

    private DisplayImageOptions options;
    
    TextView dis_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        init();
        getaddressinfo();
    }

    private void init() {
        setOptions();
        dis_location = (TextView) findViewById(R.id.dis_location);
        dis_location.setTag("dis");
        ((LocationApplication) getApplication()).mLocationResult = dis_location;
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_grid);
        lv = mPullRefreshListView.getRefreshableView();
        adapter = new DiscoverAdapter();
        lv.setAdapter(adapter);

        mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
            }
        });
        getGoods();
        ggvItemClick();
        initLocation();
    }
    
    
    
    private void getGoods() {
        ls = DBUtils.queryAll();
    }



    private LocationClient mLocationClient;
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";

    private void initLocation() {
        mLocationClient = ((LocationApplication) getApplication()).mLocationClient;
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);// 设置定位模式
        option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
        int span = 1000 * 60;
        option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }
    
    @Override
    protected void onPause() {
        mLocationClient.stop();
        super.onPause();
    }
    
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    class DiscoverAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return ls.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return ls.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(DiscoverActivity.this).inflate(R.layout.item_discover_adapter, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv = (ImageView) convertView.findViewById(R.id.item_dis_iv);
            holder.title = (TextView) convertView.findViewById(R.id.item_dis_title);
            holder.content = (TextView) convertView.findViewById(R.id.item_dis_content);
            holder.price = (TextView) convertView.findViewById(R.id.item_dis_price);

            Clothes clothes = ls.get(position);

            imageLoader.displayImage(clothes.getUrl(), holder.iv, options);
            holder.price.setText(clothes.getPrice());
            holder.title.setText("数量" + clothes.getAmount() + "");

            return convertView;
        }

    }

    public void setOptions() {

        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).build();
    }

    ImageLoader imageLoader = ImageLoader.getInstance();

    List<Clothes> ls = new ArrayList<Clothes>();

//    private ArrayList<Map<String, String>> getGoods(int cateId) {
//        
//        if (cateId == 1) {
//            Map<String, String> map1 = new HashMap<String, String>();
//            map1.put("img", "assets://images" + cateId + "/" + 1 + ".jpg");
//            map1.put("title", "title");
//            map1.put("price", "￥" + "118");
//            map1.put("sale", "￥" + "100");
//            map1.put("discount", "8" + "折");
//            
//            Map<String, String> map2 = new HashMap<String, String>();
//            map2.put("img", "assets://images" + cateId + "/" + 2 + ".jpg");
//            map2.put("title", "title");
//            map2.put("price", "￥" + "148");
//            map2.put("sale", "￥" + "110");
//            map2.put("discount", "7" + "折");
//            
//            Map<String, String> map3 = new HashMap<String, String>();
//            map3.put("img", "assets://images" + cateId + "/" + 3 + ".jpg");
//            map3.put("title", "title");
//            map3.put("price", "￥" + "208");
//            map3.put("sale", "￥" + "160");
//            map3.put("discount", "8" + "折");
//            
//            Map<String, String> map4 = new HashMap<String, String>();
//            map4.put("img", "assets://images" + cateId + "/" + 4 + ".jpg");
//            map4.put("title", "title");
//            map4.put("price", "￥" + "199");
//            map4.put("sale", "￥" + "130");
//            map4.put("discount", "8" + "折");
//            
//            ls.add(map1);
//            ls.add(map2);
//            ls.add(map3);
//            ls.add(map4);
//        } 
//        else if (cateId == 2) {
//            Map<String, String> map1 = new HashMap<String, String>();
//            map1.put("img", "assets://images" + cateId + "/" + 1 + ".jpg");
//            map1.put("title", "title");
//            map1.put("price", "￥" + "128");
//            map1.put("sale", "￥" + "100");
//            map1.put("discount", "8" + "折");
//            
//            Map<String, String> map2 = new HashMap<String, String>();
//            map2.put("img", "assets://images" + cateId + "/" + 2 + ".jpg");
//            map2.put("title", "title");
//            map2.put("price", "￥" + "138");
//            map2.put("sale", "￥" + "110");
//            map2.put("discount", "7" + "折");
//            
//            Map<String, String> map3 = new HashMap<String, String>();
//            map3.put("img", "assets://images" + cateId + "/" + 3 + ".jpg");
//            map3.put("title", "title");
//            map3.put("price", "￥" + "149");
//            map3.put("sale", "￥" + "123");
//            map3.put("discount", "8" + "折");
//            
//            Map<String, String> map4 = new HashMap<String, String>();
//            map4.put("img", "assets://images" + cateId + "/" + 4 + ".jpg");
//            map4.put("title", "title");
//            map4.put("price", "￥" + "129");
//            map4.put("sale", "￥" + "81");
//            map4.put("discount", "6" + "折");
//            
//            ls.add(map1);
//            ls.add(map2);
//            ls.add(map3);
//            ls.add(map4);
//        } 
//        
//        else if (cateId == 3) {
//            Map<String, String> map1 = new HashMap<String, String>();
//            map1.put("img", "assets://images" + cateId + "/" + 1 + ".jpg");
//            map1.put("title", "title");
//            map1.put("price", "￥" + "148");
//            map1.put("sale", "￥" + "100");
//            map1.put("discount", "8" + "折");
//            
//            Map<String, String> map2 = new HashMap<String, String>();
//            map2.put("img", "assets://images" + cateId + "/" + 2 + ".jpg");
//            map2.put("title", "title");
//            map2.put("price", "￥" + "148");
//            map2.put("sale", "￥" + "100");
//            map2.put("discount", "7" + "折");
//            
//            Map<String, String> map3 = new HashMap<String, String>();
//            map3.put("img", "assets://images" + cateId + "/" + 3 + ".jpg");
//            map3.put("title", "title");
//            map3.put("price", "￥" + "109");
//            map3.put("sale", "￥" + "78");
//            map3.put("discount", "8" + "折");
//            
//            Map<String, String> map4 = new HashMap<String, String>();
//            map4.put("img", "assets://images" + cateId + "/" + 4 + ".jpg");
//            map4.put("title", "title");
//            map4.put("price", "￥" + "179");
//            map4.put("sale", "￥" + "130");
//            map4.put("discount", "8" + "折");
//            
//            ls.add(map1);
//            ls.add(map2);
//            ls.add(map3);
//            ls.add(map4);
//        } 
//        
////        String url = "http://www.ankobeauty.com/anko/index.php?a=index&m=Index&g=API";
////        try {
////            String jsonstring = GetNetData.getResultForHttpGet(url);
////            JSONObject result = new JSONObject(jsonstring);
////            JSONArray goodsList = result.getJSONArray("goodslist");
////            int length = goodsList.length();
////            for (int i = 0; i < length; i++) {
////                JSONObject oj = goodsList.getJSONObject(i);
////                String cidStr = oj.getString("cid");// 取出属性
////                String[] arr = cidStr.split(",");
////                // 对比属性
////                for (int j = 0; j < arr.length; j++) {
////                    if (cateId == Integer.parseInt(arr[j])) {
////                        Map<String, String> map = new HashMap<String, String>();
////                        map.put("title", oj.getString("title").toString());
////                        map.put("price", "￥" + oj.getString("price").toString());
////                        map.put("sale", "￥" + oj.getString("sale").toString());
////                        map.put("img", "http://www.ankobeauty.com/anko" + oj.getString("img") + "_500x1000.jpg");
////                        map.put("discount", oj.getString("discount") + "折");
////                        map.put("url", oj.getString("url").toString());
////                        ls.add(map);
////                    }
////                }
////            }
////        } catch (ClientProtocolException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        } catch (IOException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        } catch (JSONException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
//        return ls;
//    }

    class ViewHolder {
        ImageView iv;
        TextView title;
        TextView content;
        TextView price;
    }

    // private void showCategroy(String index) {
    // mFragmentManager = getSupportFragmentManager();
    // fragmentTransaction = mFragmentManager.beginTransaction();
    // fragmentTransaction.replace(R.id.category_gridview,
    // FirstFragment.newInstance(index));
    // fragmentTransaction.commit();
    // }

    private CitycodeUtil citycodeUtil;
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.offer_spinner_type:
            mspinnerWindow = new SpinnerPopupWindow(v, new String[] { "衣服", "鞋子", "裤袜", "帽子" }, null);
            popShow();
            break;
        case R.id.offer_spinner_area:
            mspinnerWindow = new SpinnerPopupWindow(v, null, citycodeUtil.getCouny(couny_map,
                    "320100"));
            popShow();
            break;
        case R.id.offer_spinner_sort:
            mspinnerWindow = new SpinnerPopupWindow(v, new String[] { "销量", "价格", "日期",}, null);
            popShow();
            break;

        default:
            break;
        }
    }
    
    private ArrayList<String> couny_list = new ArrayList<String>();
    private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();
    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    
    private void getaddressinfo() {
        citycodeUtil = CitycodeUtil.getSingleton();
        // TODO Auto-generated method stub
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = FileUtil.readAssets(this, "area.json");
        province_list = parser.getJSONParserResult(area_str, "area0");
         citycodeUtil.setProvince_list_code(parser.province_list_code);
        city_map = parser.getJSONParserResultArray(area_str, "area1");
        // System.out.println("city_mapsize" +
        // parser.city_list_code.toString());
         citycodeUtil.setCity_list_code(parser.city_list_code);
        couny_map = parser.getJSONParserResultArray(area_str, "area2");
         citycodeUtil.setCouny_list_code(parser.city_list_code);
        // System.out.println("couny_mapsize" +
        // parser.city_list_code.toString());
    }
    
    public static class JSONParser {
        public ArrayList<String> province_list_code = new ArrayList<String>();
        public ArrayList<String> city_list_code = new ArrayList<String>();

        public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
            List<Cityinfo> list = new ArrayList<Cityinfo>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
                        .next();
                Cityinfo cityinfo = new Cityinfo();

                cityinfo.setCity_name(entry.getValue().getAsString());
                cityinfo.setId(entry.getKey());
                province_list_code.add(entry.getKey());
                list.add(cityinfo);
            }
            System.out.println(province_list_code.size());
            return list;
        }

        public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
                String JSONString, String key) {
            HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
            JsonObject result = new JsonParser().parse(JSONString)
                    .getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
                        .next();
                List<Cityinfo> list = new ArrayList<Cityinfo>();
                JsonArray array = entry.getValue().getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    Cityinfo cityinfo = new Cityinfo();
                    cityinfo.setCity_name(array.get(i).getAsJsonArray().get(0)
                            .getAsString());
                    cityinfo.setId(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    city_list_code.add(array.get(i).getAsJsonArray().get(1)
                            .getAsString());
                    list.add(cityinfo);
                }
                hashMap.put(entry.getKey(), list);
            }
            return hashMap;
        }
    }

    
    public ArrayList<String> getCouny(
            HashMap<String, List<Cityinfo>> cityHashMap, String citycode) {
        System.out.println("citycode" + citycode);
        List<Cityinfo> couny = null;
        if (couny_list.size() > 0) {
            couny_list.clear();
        }
        if (couny == null) {
            couny = new ArrayList<Cityinfo>();
        } else {
            couny.clear();
        }

        couny = cityHashMap.get(citycode);
        System.out.println("couny--->" + couny.toString());
        for (int i = 0; i < couny.size(); i++) {
            couny_list.add(couny.get(i).getCity_name());
        }
        return couny_list;

    }

    private void popShow() {
        if (mspinnerWindow.isShowing()) {
            // 消失
            mspinnerWindow.dismiss();
        }
        // 如果下拉列表没有显示——显示
        else if (!mspinnerWindow.isShowing()) {
            // 显示下拉列表
            mspinnerWindow.show();
        }
    }

//    private class SearchSpinnerItemClickListener implements OnClickListener {
//
//        /** 当前点击位置 */
//        private int position = 0;
//
//        String[] mStrChoices = null;
//
//        TextView tv;
//
//        public SearchSpinnerItemClickListener(int _position, String[] mStrChoices, View tv) {
//            this.mStrChoices = mStrChoices;
//            this.position = _position;
//            this.tv = (TextView) tv;
//        }
//
//        @Override
//        public void onClick(View v) {
//            // 记录当前选中的内容
//            mspinnerWindow.intPosition = position;
//            tv.setText(mStrChoices[position]);
//            ls.clear();
//            getGoods((int) (Math.random() * 2 + 1));
//            adapter.notifyDataSetChanged();
//            mspinnerWindow.dismiss();
//        }
//    }

    /**
     * 
     * PopupAccountWindow类
     * 
     * @ClassName:PopupAccountWindow
     * @Description: 下拉列表
     * @author: 6270000220
     * @date: 2012-11-8
     * 
     */
    private class SpinnerPopupWindow {

        /** 下拉列表弹出框 */
        private PopupWindow popwindow;
        /** 父控件 */
        private View parentView = null;
        /** 当前选中位置 */
        private int intPosition = 0;

        /** item title */
        private TextView txtTitle = null;
        /** item actor */
        private TextView txtActor = null;
        /** item director */
        private TextView txtDirector = null;
        /** item keywords */
        private TextView txtKeywords = null;
        String[] mStrChoices = null;
        
        java.util.List<String> list = null;;
        
        ListView lv;

        /**
         * 自定义popupWindow构造器
         * 
         * @param vew
         */
        public SpinnerPopupWindow(View vew, String[] mStrChoices, java.util.List<String> list) {
            this.parentView = vew;
            // 初始化
            this.mStrChoices = mStrChoices;
            this.list = list;
            initPopWindow();
        }

        /**
         * 
         * initPopWindow方法名
         * <p>
         * Description: 下拉列表初始化方法
         * <p>
         * 
         * @date 2012-11-8
         */
        private void initPopWindow() {

            // 初始化popuwindow
            LayoutInflater lay = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 填充view
            View view = lay.inflate(R.layout.search_spinner_popup_list, null);

            // mSearchSpinnerItemLayout = (LinearLayout)
            // view.findViewById(R.id.search_spinner_popu_item_layout);
            // 初始化popuWindow
            popwindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            // 监听返回按钮
            popwindow.setBackgroundDrawable(new BitmapDrawable(getResources()));

            // 设置焦点，可获取焦点，能点击
            popwindow.setOutsideTouchable(true);

            popwindow.setFocusable(true);

            // 触摸后dismiss
            popwindow.getContentView().setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popwindow.dismiss();

                    return true;
                }
            });
            popwindow.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {

                }
            });
            lv = (ListView) view.findViewById(R.id.dicovery_spinner_list);
            if (list != null) {
                lv.setAdapter(new SpinnerAdapter(list));
            } else {
                lv.setAdapter(new SpinnerAdapter(mStrChoices));
            }
            
            lv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    ls.clear();
//                    getGoods((int) (Math.random() * 2 + 1));
//                    adapter.notifyDataSetChanged();
                    mspinnerWindow.dismiss();
                }
            });
            // 控件初始化
//            txtTitle = (TextView) view.findViewById(R.id.search_spinner_popu_item_title);
//            txtActor = (TextView) view.findViewById(R.id.search_spinner_popu_item_actor);
//            txtDirector = (TextView) view.findViewById(R.id.search_spinner_popu_item_director);
//            txtKeywords = (TextView) view.findViewById(R.id.search_spinner_popu_item_keywords);
            // 控件内容初始化
//            txtTitle.setText(mStrChoices[0]);
//            txtActor.setText(mStrChoices[1]);
//            txtDirector.setText(mStrChoices[2]);
//            txtKeywords.setText(mStrChoices[3]);

            // item点击事件监听
//            txtTitle.setOnClickListener(new SearchSpinnerItemClickListener(0, mStrChoices, parentView));
//            txtActor.setOnClickListener(new SearchSpinnerItemClickListener(1, mStrChoices, parentView));
//            txtDirector.setOnClickListener(new SearchSpinnerItemClickListener(2, mStrChoices, parentView));
//            txtKeywords.setOnClickListener(new SearchSpinnerItemClickListener(3, mStrChoices, parentView));

        }

        /**
         * 
         * show()
         * <p>
         * Description: 显示popupWindow
         * <p>
         * 
         * @date 2012-11-21
         */
        public void show() {
            popwindow.showAsDropDown(parentView, 0, 50);
        }

        /**
         * 
         * isShowing()
         * <p>
         * Description: 判断是否显示
         * <p>
         * 
         * @date 2012-11-21
         * @return
         */
        public boolean isShowing() {
            return popwindow.isShowing();
        }

        /**
         * 
         * dismiss()
         * <p>
         * Description: 消失
         * <p>
         * 
         * @date 2012-11-21
         */
        public void dismiss() {
            popwindow.dismiss();
        }
    }
    
    class SpinnerAdapter extends BaseAdapter {
        
        java.util.List<String> list;
        
        String[] strs;
        
        public SpinnerAdapter(String[] strs) {
            this.strs = strs;
        }
        
        public SpinnerAdapter(java.util.List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            if (strs != null) {
                return strs.length;
            } else {
                return list.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = LayoutInflater.from(DiscoverActivity.this).inflate(R.layout.item_discovery_spinner, null);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            
            vh.title = (TextView) convertView.findViewById(R.id.item_dic_spinner_tv);
            if (strs != null) {
                vh.title.setText(strs[position]);
            } else {
                vh.title.setText(list.get(position));
            }
            
            return convertView;
        }
        
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
            adapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
    
    private void ggvItemClick() {
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DiscoverActivity.this, GoodsDetailAct.class);
                intent.putExtra("clothes", ls.get(position));
                startActivity(intent);
            }

        });
    }

}
