package com.hc.nmj.ui;

import org.kymjs.kjframe.utils.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hc.nmj.R;
import com.hc.nmj.bean.Clothes;
import com.hc.nmj.common.Constant;
import com.hc.nmj.weight.SelectPicPopupWindow;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GoodsDetailAct extends Activity implements OnClickListener {

    TextView mPrice;
    TextView mName;
    ImageView mIMG;
    private DisplayImageOptions options;
    SelectPicPopupWindow menuWindow;
    Clothes mClothes;
    String url;
    String price;
    String title;
    ImageButton mZanIB;
    /**
     * 0 before 1 after
     */
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_detail);
        setOptions();
        init();
        initData();
    }

    private void init() {
        mPrice = (TextView) findViewById(R.id.goods_d_price);
        mName = (TextView) findViewById(R.id.goods_d_name);
        mIMG = (ImageView) findViewById(R.id.goods_d_img);
        findViewById(R.id.goods_d_buy_now).setOnClickListener(this);
        findViewById(R.id.goods_d_jion).setOnClickListener(this);
        mZanIB = (ImageButton) findViewById(R.id.goods_d_zan);
        // 显示窗口
        // 设置layout在PopupWindow中显示的位置
    }

    private void initData() {
        Intent i = getIntent();
        mClothes = (Clothes) i.getSerializableExtra("clothes");
        if (mClothes != null) {
            url = mClothes.getUrl();
            title = mClothes.getTitle();
            price = mClothes.getPrice();
        }

        if (url != null) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(url, mIMG, options);
            mPrice.setText(price);
//            mName.setText( "数量" + mClothes.getAmount());
        }
        menuWindow = new SelectPicPopupWindow(this, this, title, price);

        menuWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                if (Constant.buy) {
                    Constant.shopCarList.add(mClothes);
                    Toast.makeText(GoodsDetailAct.this, "添加成功",
                            Toast.LENGTH_SHORT).show();
                    HomeActivity.judgeActivity(3, "SHOPPINGCAR_ACTIVITY");
                    finish();
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.goods_d_buy_now:
        case R.id.goods_d_jion:
            menuWindow.showAtLocation(this.findViewById(R.id.goods_d_jion),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            break;
        case R.id.goods_d_zan:
            if (flag == 0) {
                flag = 1;
                mZanIB.setBackgroundResource(R.drawable.zan_after);
            } else if (flag == 1) {
                flag = 0;
                mZanIB.setBackgroundResource(R.drawable.zan_before);
            }
            break;
        default:
            break;
        }
    }

    public void setOptions() {

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

}
