package com.hc.nmj.ui;

import com.hc.nmj.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeActivity extends TabActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();

    private static RadioGroup mTabButtonGroup;
    private static TabHost mTabHost;

    public static final String TAB_MAIN = "MAIN_ACTIVITY";
    public static final String TAB_SEARCH = "SEARCH_ACTIVITY";
    public static final String TAB_CATEGORY = "CATEGORY_ACTIVITY";
    public static final String TAB_PERSONAL = "PERSONAL_ACTIVITY";
    public static final String TAB_SHOPPINGCAR = "SHOPPINGCAR_ACTIVITY";

    private ImageView splash = null;
    private Animation mAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        findViewById();
        initView();
//        initTranslateAnimation();
    }

    private void findViewById() {
        mTabButtonGroup = (RadioGroup) findViewById(R.id.home_radio_button_group);
        splash = (ImageView) findViewById(R.id.iv_splash_img);
    }

    private void initView() {

        mTabHost = getTabHost();

        Intent i_main = new Intent(this, IndexActivity.class);
        Intent i_search = new Intent(this, DiscoverActivity.class);
        Intent i_category = new Intent(this, NewCategoryAct.class);
        Intent i_personal = new Intent(this, PersonalActivity.class);
        Intent i_shoppingcar = new Intent(this, ShoppingCarActivity.class);

        mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN)
                .setContent(i_main));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_SEARCH)
                .setIndicator(TAB_SEARCH).setContent(i_search));
        mTabHost.addTab(mTabHost.newTabSpec(TAB_CATEGORY)
                .setIndicator(TAB_CATEGORY).setContent(i_category));

        mTabHost.addTab(mTabHost.newTabSpec(TAB_PERSONAL)
                .setIndicator(TAB_PERSONAL).setContent(i_personal));

        mTabHost.addTab(mTabHost.newTabSpec(TAB_SHOPPINGCAR)
                .setIndicator(TAB_SHOPPINGCAR).setContent(i_shoppingcar));

        mTabHost.setCurrentTabByTag(TAB_MAIN);

        mTabButtonGroup
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                        case R.id.home_tab_main:
                            mTabHost.setCurrentTabByTag(TAB_MAIN);
                            break;

                        case R.id.home_tab_search:
                            mTabHost.setCurrentTabByTag(TAB_SEARCH);
                            break;

                        case R.id.home_tab_category:
                            mTabHost.setCurrentTabByTag(TAB_CATEGORY);
                            break;
                        case R.id.home_tab_personal:
                            mTabHost.setCurrentTabByTag(TAB_PERSONAL);
                            break;
                        case R.id.home_tab_shopping_car:
                            mTabHost.setCurrentTabByTag(TAB_SHOPPINGCAR);
                            break;

                        default:
                            break;
                        }
                    }
                });
    }
    
    public static void judgeActivity(int index, String str) {
        ((RadioButton) (mTabButtonGroup.getChildAt(index))).setChecked(true);
        mTabHost.setCurrentTabByTag(str);
    }

}
