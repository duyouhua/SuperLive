package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.ReviewOptionTime;

import java.util.List;

/**
 * Created by sks on 2016/4/19.
 * 回顾选项时间列表adapter
 */
public class ReviewPopTimeAdapter extends AppBaseAdapter<ReviewOptionTime> {
    public ReviewPopTimeAdapter(List<ReviewOptionTime> list, Context context) {
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
        holder.tv_time.setText(list.get(position).getName());
        if (list.get(position).isSelected()){
            holder.tv_time.setTextColor(context.getResources().getColor(R.color.theme_color));
            holder.iv_mark.setVisibility(View.VISIBLE);
        }else {
            holder.tv_time.setTextColor(context.getResources().getColor(R.color.textcolor_1));
            holder.iv_mark.setVisibility(View.GONE);
        }

        return convertView;
    }
    class ViewHolder{
        ImageView iv_mark;
        TextView tv_time;
    }
}
