package com.hc.nmj.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hc.nmj.R;
import com.hc.nmj.base.MyFragmentPagerAdapter;

public class SearchActivity extends FragmentActivity implements OnClickListener{
	
	EditText mSearchET;
    LinearLayout mHistorySearchLL;
    LinearLayout mHotSearchLL; 
    TextView mHistroyTV;
    LinearLayout mSearchBeforLL;
    LinearLayout mSearchAfterRL;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private RadioButton tvTabWeina, tvTabOulaiya, tvTabKashi;
    
 // fragment管理器
    private FragmentManager mFragmentManager;
    
    // fragment事务管理
    private FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		initView();
        initData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

    private void initView()
    {
        mHistroyTV = (TextView)findViewById(R.id.history_search_tv);
        mHistorySearchLL = (LinearLayout)findViewById(R.id.history_search_ll);
        mHotSearchLL = (LinearLayout)findViewById(R.id.hot_search_ll);
        mSearchBeforLL = (LinearLayout) findViewById(R.id.search_before);
        mSearchAfterRL = (LinearLayout) findViewById(R.id.search_after);
    }
    
    private void initData()
    {
        mHotSearchLL.removeAllViews();
        for (int i = 0; i < 5; i++)
        {
            Button b = new Button(this);
            b.setText(i + "");
            b.setId(i);
            b.setOnClickListener((OnClickListener)this);
            b.setTag("0");
            mHotSearchLL.addView(b);
        }
        
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case 0:
                Log.e("jiarui", "0");
                if ("0".equalsIgnoreCase((String)v.getTag())) {
//                    v.setBackgroundResource(R.drawable.locate_down);   
                    v.setTag("1");
                } else if ("1".equalsIgnoreCase((String)v.getTag())) {
//                    v.setBackgroundResource(R.drawable.locate_ok);
                    v.setTag("0"); 
                }
               mSearchBeforLL.setVisibility(View.GONE);
               mSearchAfterRL.setVisibility(View.VISIBLE);
               InitViewPager();
                break;
            case 1:
                Log.e("jiarui", "1");
                break;
            case R.id.back:
            	finish();
            	break;
            
            default:
                break;
        }
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        Fragment weinaFragment = FirstFragment.newInstance("16");
        Fragment oulaiyaFragment = FirstFragment.newInstance("17");
        Fragment kashiFragment=FirstFragment.newInstance("18");
//        Fragment shihuakouFragment=FirstFragment.newInstance("19");
//        Fragment meiqishiFragment=FirstFragment.newInstance("20");

        fragmentsList.add(weinaFragment);
        fragmentsList.add(oulaiyaFragment);
        fragmentsList.add(kashiFragment);
//        fragmentsList.add(shihuakouFragment);
//        fragmentsList.add(meiqishiFragment);
        
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        mPager.setCurrentItem(0);
//        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
      
    }

}
