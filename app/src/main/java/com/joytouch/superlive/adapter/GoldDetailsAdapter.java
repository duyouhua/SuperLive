package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.GoldDetails;

import java.util.List;

/**
 * Created by sks on 2016/4/26.
 */
public class GoldDetailsAdapter extends AppBaseAdapter<GoldDetails> {
    public GoldDetailsAdapter(List<GoldDetails> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_gold_details,null);
            holder = new ViewHolder();
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_gold = (TextView) convertView.findViewById(R.id.tv_gold);
            holder.tv_my_result = (TextView) convertView.findViewById(R.id.tv_my_result);
            holder.tv_result = (TextView) convertView.findViewById(R.id.tv_result);
            holder.tv_result_status = (TextView) convertView.findViewById(R.id.tv_result_status);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    class ViewHolder{
        TextView tv_content;
        TextView tv_gold;
        TextView tv_my_result;
        TextView tv_result;
        TextView tv_result_status;
    }
}
