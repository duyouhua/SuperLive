package com.joytouch.superlive.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_pic;
        TextView tv_dis;
        private ItemClickListener clickListener;
        public ViewHolder(View itemView,ItemClickListener clickListener) {
            super(itemView);
            iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
            tv_dis = (TextView) itemView.findViewById(R.id.tv_dis);
            iv_pic.setOnClickListener(this);
            this.clickListener = clickListener;
        }

    @Override
    public void onClick(View v) {
        clickListener.itemClick(iv_pic,getPosition());
    }

    public interface ItemClickListener{
            void itemClick(View view,int position);
        }
    }