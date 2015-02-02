package com.hc.nmj.weight;

import com.hc.nmj.R;
import com.hc.nmj.common.Constant;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SelectPicPopupWindow extends PopupWindow implements OnClickListener {

    private View mMenuView;
    private RadioGroup mColorGroups;
    private RadioGroup mSizeGroups;

    public SelectPicPopupWindow(Activity context, OnClickListener itemsOnClick, String name, String price) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.weight_goods_d_bootom, null);
        mColorGroups = (RadioGroup) mMenuView.findViewById(R.id.goods_d_color_ll);
        mSizeGroups = (RadioGroup) mMenuView.findViewById(R.id.goods_d_size_ll);
        mMenuView.findViewById(R.id.goods_d_cancel).setOnClickListener(this);
        mMenuView.findViewById(R.id.goods_d_confirm).setOnClickListener(this);

        RadioButton colorButton1 = new RadioButton(context);
        colorButton1.setText("红");
        colorButton1.setId(1);
        colorButton1.setOnClickListener(this);
//        colorButton1.setButtonDrawable(null);
//        colorButton1.setBackgroundResource(R.drawable.radio_bg);
        RadioButton colorButton2 = new RadioButton(context);
        colorButton2.setText("黄");
        colorButton2.setId(2);
        colorButton2.setOnClickListener(this);
//        colorButton2.setButtonDrawable(null);
//        colorButton2.setBackgroundResource(R.drawable.radio_bg);
        RadioButton colorButton3 = new RadioButton(context);
        colorButton3.setText("绿");
        colorButton3.setId(3);
        colorButton3.setOnClickListener(this);
//        colorButton3.setButtonDrawable(null);
//        colorButton3.setBackgroundResource(R.drawable.radio_bg);
        mColorGroups.addView(colorButton1);
        mColorGroups.addView(colorButton2);
        mColorGroups.addView(colorButton3);

        RadioButton sizeButton1 = new RadioButton(context);
        sizeButton1.setText("大码");
        sizeButton1.setId(4);
        sizeButton1.setOnClickListener(this);
//        sizeButton1.setButtonDrawable(null);
//        sizeButton1.setBackgroundResource(R.drawable.radio_bg);
        RadioButton sizeButton2 = new RadioButton(context);
        sizeButton2.setText("小码");
        sizeButton2.setId(5);
        sizeButton2.setOnClickListener(this);
//        sizeButton2.setButtonDrawable(null);
//        sizeButton2.setBackgroundResource(R.drawable.radio_bg);
        RadioButton sizeButton3 = new RadioButton(context);
        sizeButton3.setText("均码");
        sizeButton3.setId(6);
//        sizeButton3.setButtonDrawable(null);
//        sizeButton3.setBackgroundResource(R.drawable.radio_bg);
        sizeButton3.setOnClickListener(this);
        mSizeGroups.addView(sizeButton1);
        mSizeGroups.addView(sizeButton2);
        mSizeGroups.addView(sizeButton3);

        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        // this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        // mMenuView.setOnTouchListener(new OnTouchListener() {
        //
        // public boolean onTouch(View v, MotionEvent event) {
        //
        // int height = mMenuView.findViewById(R.id.pop_layout).getTop();
        // int y=(int) event.getY();
        // if(event.getAction()==MotionEvent.ACTION_UP){
        // if(y<height){
        // dismiss();
        // }
        // }
        // return true;
        // }
        // });

        mColorGroups.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;

                default:
                    break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case 1:

            break;
        case 2:

            break;
        case R.id.goods_d_confirm:
            Constant.buy = true;
            dismiss();
            break;
        case R.id.goods_d_cancel:
            Constant.buy = false;
            dismiss();
            break;

        default:
            break;
        }
    }

}
