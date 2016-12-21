package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.Square;
import com.joytouch.superlive.widget.CircleImageView;

import java.util.List;

/**
 * Created by sks on 2016/4/10.
 */
public class SquareAdapter extends AppBaseAdapter<Square> {

    public SquareAdapter(List<Square> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_square,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_anchor_name = (TextView) convertView.findViewById(R.id.tv_anchor_name);
            holder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_name;
        CircleImageView iv_icon;
        TextView tv_anchor_name;
        ImageView iv_level;
        TextView tv_num;
    }
}
