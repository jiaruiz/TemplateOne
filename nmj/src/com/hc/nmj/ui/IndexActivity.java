package com.hc.nmj.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.zip.Inflater;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.handmark.pulltorefresh.library.HeaderGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshHeaderGridView;
import com.hc.nmj.R;
import com.hc.nmj.adapter.IndexGridAdapter;
import com.hc.nmj.base.GetNetData;
import com.hc.nmj.base.MyFragmentPagerAdapter;
import com.hc.nmj.base.MyGallery;
import com.hc.nmj.bean.Clothes;
import com.hc.nmj.common.Constant;
import com.hc.nmj.utils.DBUtils;
import com.hc.nmj.utils.LocationApplication;
import com.hc.nmj.weight.GridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class IndexActivity extends FragmentActivity {

    // 用于下面点点指示
    private LinearLayout ll_focus_indicator_container = null;
    // gallery的id
    private MyGallery gallery = null;
    List<Map<String, String>> list = new ArrayList<Map<String, String>>(); // 图片缓存
    List<Map<String, String>> ls = new ArrayList<Map<String, String>>(); // 图片缓存
    public boolean timeFlag = true;
    // 屏幕宽度
    WindowManager windowManager;
    int sw, sh;

    MyGllaryAdapter myAdapter;
    /**
     * 存储上一个选择项的Index
     */
    private int preSelImgIndex = 0;

    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private RadioButton tvTabWeina, tvTabOulaiya, tvTabKashi;
    // tvTabShihuakou,tvTabMeiqishi;

    private int currIndex = 0;
    private int position_one;
    private int position_two;
    // private int position_three;
    // private int position_four;
    private DisplayImageOptions options;

    private RelativeLayout index_gallery;
    
    HeaderGridView ggv;
//    GridView ggv;
    IndexGridAdapter gvadapter;
    
    private PullToRefreshHeaderGridView mPullRefreshGridView;
    LinearLayout view;
    List<Clothes> clothersLists = new ArrayList<Clothes>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.activity_index, null);
        setContentView(view);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initEvents();
        setOptions();
        initLocation();
        refreshView();
    }


    private void refreshView() {
        if (Constant.pay) {
            clothersLists = DBUtils.queryAll();
            gvadapter = new IndexGridAdapter(this, clothersLists);
            ggv.setAdapter(gvadapter);
            Constant.pay = false;
        }
        index_gallery.setVisibility(View.VISIBLE);
    }

    private void initEvents() {

        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int selIndex, long arg3) {
                // 修改上一次选中项的背景
                selIndex = selIndex % 2;

                ImageView preSelImg = (ImageView) ll_focus_indicator_container.findViewById(preSelImgIndex);
                preSelImg.setImageDrawable(IndexActivity.this.getResources().getDrawable(R.drawable.ic_focus));
                // 修改当前选中项的背景
                ImageView curSelImg = (ImageView) ll_focus_indicator_container.findViewById(selIndex);
                curSelImg.setImageDrawable(IndexActivity.this.getResources().getDrawable(R.drawable.ic_focus_select));
                preSelImgIndex = selIndex;
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//        new LoadNetData().execute();
    }

    public void setOptions() {
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).build();
    }


    private void InitTextView() {

        LocationResult = (TextView) view.findViewById(R.id.location);
        ((LocationApplication) getApplication()).mLocationResult = LocationResult;
    }

    @Override
    protected void onDestroy() {
        index_gallery.setVisibility(View.GONE);
        super.onDestroy();
    }

    private void initView() {

        // resources = getResources();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 屏幕宽度
        sw = windowManager.getDefaultDisplay().getWidth();
        // 屏幕高度
        sh = windowManager.getDefaultDisplay().getHeight();

        // getIntent().getParcelableArrayListExtra("data") == null) {
        // } else {
        // myAdapter = new MyAdapter(this, (List<? extends Map<String, ?>>)
        // getIntent().getParcelableArrayListExtra("data"), R.layout.item, from,
        // to);
        // }
        ll_focus_indicator_container = (LinearLayout) view.findViewById(R.id.ll_focus_indicator_container);
        InitFocusIndicatorContainer();

        
        mPullRefreshGridView = (PullToRefreshHeaderGridView) view.findViewById(R.id.pull_refresh_grid);
        ggv = mPullRefreshGridView.getRefreshableView();
//        ggv = (GridView) view.findViewById(R.id.ggv);
//        ggv.setNumColumns(2);
//        mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<HeaderGridView>() {
//
//            @Override
//            public void onPullDownToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
//            }
//
//            @Override
//            public void onPullUpToRefresh(PullToRefreshBase<HeaderGridView> refreshView) {
//                Log.e("jiarui", "pullup");
//                new GetDataTask().execute();
//            }
//
//        });

        index_gallery = (RelativeLayout) view.findViewById(R.id.index_gallery);
        index_gallery.setVisibility(View.VISIBLE);
        
        String[] from = { "img", "name" };
        int[] to = { R.id.gallery_image, R.id.gallery_text };
        myAdapter = new MyGllaryAdapter(this, getCate(), R.layout.item, from, to);


        gallery = (MyGallery) view.findViewById(R.id.banner_gallery);
        gallery.setAdapter(myAdapter);
        gallery.setFocusable(true);

        InitTextView();
        
        view.removeView(index_gallery);
        
        ggv.addHeaderView(index_gallery);
//        index_gallery.setLayoutParams(new FrameLayout.LayoutParams(1000, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
        
        String[] from2 = { "img", "sale", "discount", "price", "title"};

        int[] to2 = { R.id.ggv_img, R.id.ggv_sale, R.id.ggv_discount, R.id.ggv_price, R.id.ggv_title};

        clothersLists = DBUtils.queryAll();
        gvadapter = new IndexGridAdapter(this, clothersLists);
        ggv.setAdapter(gvadapter);
        ggvItemClick();
    }

    private void InitFocusIndicatorContainer() {

        for (int i = 0; i < 2; i++) {
            ImageView localImageView = new ImageView(this);
            localImageView.setId(i);
            ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
            localImageView.setScaleType(localScaleType);
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(15, 15);
            localImageView.setLayoutParams(localLayoutParams);
            localImageView.setPadding(5, 5, 5, 5);
            localImageView.setImageResource(R.drawable.ic_focus);
            localImageView.setBackgroundColor(getResources().getColor(R.color.trans));
            this.ll_focus_indicator_container.addView(localImageView);
        }
    }
    
    private void ggvItemClick() {
        ggv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Clothes clothes = (Clothes) ggv.getItemAtPosition(position);
                Intent intent = new Intent(IndexActivity.this, GoodsDetailAct.class);
                intent.putExtra("clothes", clothes);
                startActivity(intent);
            }

        });
    }

    public List<Map<String, String>> getCate() {

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("img", "assets://images" + 1 + "/" + 1 + ".jpg");
        map1.put("title", "title");
        map1.put("price", "￥" + "118");
        map1.put("sale", "￥" + "100");
        map1.put("discount", "8" + "折");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("img", "assets://images" + 1 + "/" + 2 + ".jpg");
        map2.put("title", "title");
        map2.put("price", "￥" + "148");
        map2.put("sale", "￥" + "110");
        map2.put("discount", "7" + "折");

        Map<String, String> map3 = new HashMap<String, String>();
        map3.put("img", "assets://images" + 1 + "/" + 3 + ".jpg");
        map3.put("title", "title");
        map3.put("price", "￥" + "208");
        map3.put("sale", "￥" + "160");
        map3.put("discount", "8" + "折");

        Map<String, String> map4 = new HashMap<String, String>();
        map4.put("img", "assets://images" + 1 + "/" + 4 + ".jpg");
        map4.put("title", "title");
        map4.put("price", "￥" + "199");
        map4.put("sale", "￥" + "130");
        map4.put("discount", "8" + "折");

        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        return list;

    }

    private class MyGllaryAdapter extends SimpleAdapter {
        ImageLoader imageLoader = ImageLoader.getInstance();

        public MyGllaryAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = super.getView(position, convertView, parent);
            Map<String, Object> record = (Map<String, Object>) getItem(position);
            // final String checkUrl = record.get("url").toString();
            ImageView imageView = (ImageView) view.findViewById(R.id.gallery_image);
            imageLoader.displayImage(record.get("img").toString(), imageView, options);
            // lazyImageHelper.loadImage(imageView,
            // record.get("img").toString());
            LayoutParams params = imageView.getLayoutParams();
            int imgWidth = 1000;
            int imgHeight = (int) Math.ceil((imgWidth * 240) / 640);
            params.width = imgWidth;
            params.height = imgHeight;
            imageView.setLayoutParams(params);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO Auto-generated method stub
                    // Intent it = new Intent();
                    Intent intent = new Intent(IndexActivity.this, GoodsDetailAct.class);
                    // intent.putExtra("url", checkUrl);

                    // intent.putExtra("img", imageView.getdr)
                    startActivity(intent);
                }

            });
            return view;
        }

    }

    private LocationClient mLocationClient;
    private LocationMode tempMode = LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private TextView LocationResult;

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

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        index_gallery.setVisibility(View.GONE);
        super.onStop();
    }

    class LoadNetData extends AsyncTask<Void, Void, List<Map<String, String>>> {

        @Override
        protected List<Map<String, String>> doInBackground(Void... params) {
            return getCate();
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> result) {
            myAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
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
            gvadapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshGridView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }
    
    private List<Map<String,String>> getGoods() {

        String url = "http://www.ankobeauty.com/anko/index.php/Index/Index/aad";
        try {
            String jsonstring = GetNetData.getResultForHttpGet(url);
            // String jsonstring = loadjson.getJson(url);
            JSONObject result = new JSONObject(jsonstring);
            JSONArray cateList = result.getJSONArray("adlist");
            int length = cateList.length();
            for (int i = 0; i < length; i++) {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject oj = cateList.getJSONObject(i);
                map.put("img", "http://www.ankobeauty.com" + oj.getString("img"));
                map.put("name", oj.getString("name"));
                map.put("url", oj.getString("url"));
                ls.add(map);
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
        return ls;

    }

}
