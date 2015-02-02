package com.hc.nmj.adapter;

import java.util.List;

import com.hc.nmj.R;
import com.hc.nmj.bean.Clothes;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class IndexGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<Clothes> list;
    ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public IndexGridAdapter(Context context, List<Clothes> list) {
        mContext = context;
        this.list = list;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher).cacheInMemory().cacheOnDisc().displayer(new RoundedBitmapDisplayer(20)).build();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IndexHolderView holderView;
        if (convertView == null) {
            holderView = new IndexHolderView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods, null);
            convertView.setTag(holderView);
        } else {
            holderView = (IndexHolderView) convertView.getTag();
        }
        holderView.iv = (ImageView) convertView.findViewById(R.id.ggv_img);
        holderView.price = (TextView) convertView.findViewById(R.id.ggv_sale);
        holderView.amount = (TextView) convertView.findViewById(R.id.ggv_title);

        Clothes clothes = list.get(position);
//        Log.e("jiarui", "" + clothes.getAmount());

        imageLoader.displayImage(clothes.getUrl(), holderView.iv, options);
        holderView.price.setText(clothes.getPrice());
        holderView.amount.setText("数量" + clothes.getAmount() + "");

        return convertView;
    }

    static class IndexHolderView {
        ImageView iv;
        TextView price;
        TextView amount;
    }

}
