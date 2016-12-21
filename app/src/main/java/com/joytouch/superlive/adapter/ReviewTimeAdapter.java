package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.ReviewTime;

import java.util.List;

/**
 * Created by sks on 2016/4/18.
 */
public class ReviewTimeAdapter extends AppBaseAdapter<ReviewTime> {

    public ReviewTimeAdapter(List<ReviewTime> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_review_time,null);
            holder = new ViewHolder();
            holder.iv_type_1 = (ImageView) convertView.findViewById(R.id.iv_type_1);
            holder.iv_type_2 = (ImageView) convertView.findViewById(R.id.iv_type_2);
            holder.iv_type_3 = (ImageView) convertView.findViewById(R.id.iv_type_3);
            holder.tv_name_1 = (TextView) convertView.findViewById(R.id.tv_name_1);
            holder.tv_name_2 = (TextView) convertView.findViewById(R.id.tv_name_2);
            holder.tv_name_3 = (TextView) convertView.findViewById(R.id.tv_name_3);
            holder.tv_time_1 = (TextView) convertView.findViewById(R.id.tv_time_1);
            holder.tv_time_2 = (TextView) convertView.findViewById(R.id.tv_time_2);
            holder.tv_time_3 = (TextView) convertView.findViewById(R.id.tv_time_3);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    class ViewHolder{
        ImageView iv_type_1;
        TextView tv_time_1;
        TextView tv_name_1;
        ImageView iv_type_2;
        TextView tv_time_2;
        TextView tv_name_2;
        ImageView iv_type_3;
        TextView tv_time_3;
        TextView tv_name_3;
    }
}
