package com.joytouch.superlive.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.ReviewPlayJijin;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lzx on 2016/4/11.
 */
public class ReviewPlayjijinAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<ReviewPlayJijin> jijins;
    private ViewHolder.ItemClickListener listener;
    public ReviewPlayjijinAdapter(List<ReviewPlayJijin> jijins) {
        this.jijins = jijins;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_player_jijin, null);
        ViewHolder vh = new ViewHolder(v,listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(Preference.photourl+ "orig/"+jijins.get(position).getImage(),holder.iv_pic, ImageLoaderOption.reviewLogo);
        holder.tv_dis.setText(jijins.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return jijins.size();
    }
    public void setListener(ViewHolder.ItemClickListener listener){
        this.listener = listener;
    }
}
