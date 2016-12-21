package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.ReviewOptionOrder;

import java.util.List;

/**
 * Created by sks on 2016/4/19.
 */
public class ReviewOrderAdpter extends AppBaseAdapter<ReviewOptionOrder> {
    public ReviewOrderAdpter(List<ReviewOptionOrder> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_popwindow_time,null);
            holder = new ViewHolder();
            holder.iv_mark = (ImageView) convertView.findViewById(R.id.iv_mark_time);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_time.setText(list.get(position).getSort_name());
        return convertView;
    }
    class ViewHolder{
        ImageView iv_mark;
        TextView tv_time;
    }
}
