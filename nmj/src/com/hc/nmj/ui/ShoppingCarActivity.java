package com.hc.nmj.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.nmj.R;
import com.hc.nmj.adapter.ShoppingCarAdapter;
import com.hc.nmj.adapter.ShoppingCarAdapter.OnSelectListener;
import com.hc.nmj.alipay.PayDemoActivity;
import com.hc.nmj.base.GetNetData;
import com.hc.nmj.bean.Clothes;
import com.hc.nmj.common.Constant;
import com.hc.nmj.utils.DBUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ShoppingCarActivity extends Activity implements OnSelectListener {

    TextView shopping_car_total;

    boolean mBlnSelectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shopping_car);
        // testdata();
        final ShoppingCarAdapter adapter = new ShoppingCarAdapter(this);
        adapter.setListener(this);
        ListView lv = (ListView) findViewById(R.id.shopping_car_listview);
        shopping_car_total = (TextView) findViewById(R.id.shopping_car_total);
        lv.setAdapter(adapter);

        ((CheckBox) findViewById(R.id.shopcar_all_check)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBlnSelectAll = true;
                } else {
                    mBlnSelectAll = false;
                }
                adapter.setmBlnSelecte(isChecked);
            }
        });
        ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int len = Constant.mSelectedMap.size();
        float total = 0;
        for (int i = 0; i < len; i++) {
            total += Float.parseFloat(Constant.mSelectedMap.get(i).getPrice().replace("￥", ""));
        }
        shopping_car_total.setText(total + "");
    }

    private void testdata() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(getCate().get(0).get("img").toString(), (ImageView) findViewById(R.id.item_shopping_car_img), options);
    }

    public List<Map<String, String>> getCate() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>(); // 图片缓存

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

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.shopping_car_pay:
            if (false) {
                DBUtils.update(); Constant.pay = true;
                return;
            }
            if (!mBlnSelectAll )
                return;
            if (!Constant.login) {
                Toast.makeText(ShoppingCarActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                return;
            } 
            Intent i = new Intent(ShoppingCarActivity.this, PayDemoActivity.class);
            int len = Constant.shopCarList.size();
            StringBuffer buffer = new StringBuffer();
            float price = 0;
            for (int j = 0; j < len; j++) {
                if (j > 0) {
                    buffer.append("\n");
                }
                buffer.append(Constant.shopCarList.get(j).getTitle());
                price += Float.parseFloat(Constant.shopCarList.get(j).getPrice().replace("￥", ""));
            }
            i.putExtra("name", buffer.toString());
            i.putExtra("price", price);
            startActivity(i);
            break;

        default:
            break;
        }
    }

    @Override
    public void select() {
        float total = 0;
        for (Integer key : Constant.mSelectedMap.keySet()) {
            total += Float.parseFloat(Constant.mSelectedMap.get(key).getPrice().replace("￥", ""));
            mBlnSelectAll = true;
        }
        shopping_car_total.setText(total + "");
    }

}
