package com.hc.nmj.weight;

import com.hc.nmj.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

public class SpinnerPopupWindow {

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

    private Context mContext;

    private LinearLayout mSearchSpinnerItemLayout = null;

    /** 搜索下拉内容 */
    private String[] mStrChoices = null;

    SpinnerPopupListener iSpinnerPopupListener;

    /**
     * 自定义popupWindow构造器
     * 
     * @param vew
     */
    public SpinnerPopupWindow(View vew, Context context, String[] mStrChoices) {
        this.parentView = vew;
        this.mContext = context;
        this.mStrChoices = mStrChoices;
        // 初始化
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
        LayoutInflater lay = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 填充view
        View view = lay.inflate(R.layout.search_spinner_popup_list, null);

        mSearchSpinnerItemLayout = (LinearLayout) view.findViewById(R.id.search_spinner_popu_item_layout);
        // 初始化popuWindow
        popwindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 监听返回按钮
        popwindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources()));

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
        // 控件初始化
        txtTitle = (TextView) view.findViewById(R.id.search_spinner_popu_item_title);
        txtActor = (TextView) view.findViewById(R.id.search_spinner_popu_item_actor);
        txtDirector = (TextView) view.findViewById(R.id.search_spinner_popu_item_director);
        txtKeywords = (TextView) view.findViewById(R.id.search_spinner_popu_item_keywords);
        // 控件内容初始化
        txtTitle.setText(mStrChoices[0]);
        txtActor.setText(mStrChoices[1]);
        txtDirector.setText(mStrChoices[2]);
        txtKeywords.setText(mStrChoices[3]);
        // item点击事件监听
        txtTitle.setOnClickListener(new SearchSpinnerItemClickListener(0));
        txtActor.setOnClickListener(new SearchSpinnerItemClickListener(1));
        txtDirector.setOnClickListener(new SearchSpinnerItemClickListener(2));
        txtKeywords.setOnClickListener(new SearchSpinnerItemClickListener(3));

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
        mSearchSpinnerItemLayout.setVisibility(View.VISIBLE);
        popwindow.showAsDropDown(parentView, 0, 0);
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

    public class SearchSpinnerItemClickListener implements OnClickListener {

        /** 当前点击位置 */
        private int position = 0;

        public SearchSpinnerItemClickListener(int _position) {
            this.position = _position;
        }

        @Override
        public void onClick(View v) {
            // 记录当前选中的内容
            ((TextView) v).setText(mStrChoices[position]);
            popwindow.dismiss();
            if (iSpinnerPopupListener != null) {
                iSpinnerPopupListener.select(position);
            }
        
        }

    }

    public interface SpinnerPopupListener {
        void select(int positon);
    }

}
