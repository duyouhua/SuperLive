package com.joytouch.superlive.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.widget.CircleImageView;

/**
 * Created by sks on 2016/8/15.
 */
public class InvitationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public CircleImageView headimage;
    public TextView name;
    public ImageView level_bg;
    public TextView state;
    public CheckBox selector;
    public InvitationViewHolder(View itemView) {
        super(itemView);
        headimage = (CircleImageView) itemView.findViewById(R.id.headimage);
        name = (TextView) itemView.findViewById(R.id.name);
        level_bg = (ImageView) itemView.findViewById(R.id.level_bg);
        state = (TextView) itemView.findViewById(R.id.state);
        selector = (CheckBox) itemView.findViewById(R.id.selector);
    }

    @Override
    public void onClick(View v) {

    }
}
