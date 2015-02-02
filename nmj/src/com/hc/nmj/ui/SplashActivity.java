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
import org.kymjs.kjframe.utils.PreferenceHelper;

import com.hc.nmj.R;
import com.hc.nmj.base.GetNetData;
import com.hc.nmj.common.Constant;
import com.hc.nmj.utils.DBUtils;
import com.ta.utdid2.android.utils.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.os.Parcelable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends Activity {

    private ImageView splash = null;
    private ImageView splashTxt = null;
    private Animation mAnimation;
    private ArrayList<String> data4Network;
    List<Map<String, String>> list = new ArrayList<Map<String, String>>(); // 图片缓存
    int mindicatorLen = 0;

    SharedPreferences mPreferences;
    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // splash = (ImageView) findViewById(R.id.iv_splash);
        splashTxt = (ImageView) findViewById(R.id.iv_splash_text);

        initdata();

        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }).start();
        activityJump();
        // getCate();
    }

    private void initdata() {
        if (!StringUtils.isEmpty(PreferenceHelper.readString(this, "user", "username"))) {
            Constant.login = true;
        } else {
            DBUtils.createTestData();
        }
    }

    private void initTranslateAnimation() {
        mAnimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_scale);
        mAnimation.setFillAfter(true);
        splashTxt.startAnimation(mAnimation);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            initTranslateAnimation();
        };
    };

    private void activityJump() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, HomeActivity.class);
                SplashActivity.this.startActivity(intent);
                finish();
            }
        }, 2000);
    }

    public void getCate() {

        String url = "http://www.ankobeauty.com/anko/index.php/Index/Index/aad";
        try {
            String jsonstring = GetNetData.getResultForHttpGet(url);
            // String jsonstring = loadjson.getJson(url);
            JSONObject result = new JSONObject(jsonstring);
            JSONArray cateList = result.getJSONArray("adlist");
            mindicatorLen = cateList.length();

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

}
