package com.hc.nmj.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hc.nmj.R;
import com.hc.nmj.adapter.CategoryAdapter;
import com.hc.nmj.weight.SpinnerPopupWindow;

public class NewCategoryAct extends FragmentActivity {

    private ListView mListView;

    // fragment管理器
    private FragmentManager mFragmentManager;

    // fragment事务管理
    private FragmentTransaction fragmentTransaction;

    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_category);
        init();
    }

    private void init() {
//        adapter = new CategoryAdapter(this, null);
//        mListView = (ListView) findViewById(R.id.category_listview);
//        mListView.setAdapter(adapter);
//        mListView.setEnabled(false);

        showCategroy();
    }

    private void showCategroy() {
        mFragmentManager = getSupportFragmentManager();
        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.category_gridview, new FirstFragment());
        fragmentTransaction.commit();
    }

    private void showCategroy(int index) {
        mFragmentManager = getSupportFragmentManager();
        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.category_gridview, FirstFragment.newInstance(index + ""));
        fragmentTransaction.commit();
    }
    
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.category_one1:
        case R.id.category_one2:
        case R.id.category_one3:
        case R.id.category_one4:
        case R.id.category_one5:
        case R.id.category_one6:
        case R.id.category_one7:
        case R.id.category_one8:
            showCategroy((int) (Math.random() * 2 + 1));
            break;
        default:
            break;
        }
    }

}
