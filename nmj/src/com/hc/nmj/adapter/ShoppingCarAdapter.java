package com.hc.nmj.adapter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hc.nmj.R;
import com.hc.nmj.bean.Clothes;
import com.hc.nmj.common.Constant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ShoppingCarAdapter extends BaseAdapter {

    Context mContext;
    DisplayImageOptions options;
    ImageLoader imageLoader;
    Clothes clothes;
    boolean mBlnSelecteAll;

    public boolean ismBlnSelecte() {
        return mBlnSelecteAll;
    }

    public void setmBlnSelecte(boolean mBlnSelecte) {
        this.mBlnSelecteAll = mBlnSelecte;
    }

    public ShoppingCarAdapter(Context context) {
        mContext = context;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).build();
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Constant.shopCarList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Constant.shopCarList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shopping_car, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title = (TextView) convertView.findViewById(R.id.item_shopping_car_name);
        holder.checkBox = (CheckBox) convertView.findViewById(R.id.item_shopping_car_ck);
        holder.img = (ImageView) convertView.findViewById(R.id.item_shopping_car_img);
        clothes = Constant.shopCarList.get(position);

        holder.title.setText(clothes.getTitle());
        imageLoader.displayImage(clothes.getUrl(), holder.img, options);

        holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Constant.mSelectedMap.put(position, clothes);
                } else {
                    Constant.mSelectedMap.remove(position);
                }
                if (onSelectListener != null) {
                    onSelectListener.select();
                }
            }
        });
        if (Constant.mSelectedMap.containsKey(position) || mBlnSelecteAll) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView title;
        CheckBox checkBox;
        ImageView img;
        TextView price;
    }
    
    OnSelectListener onSelectListener;
    
    public interface OnSelectListener {
        void select();
    }
    
    public void setListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

}
