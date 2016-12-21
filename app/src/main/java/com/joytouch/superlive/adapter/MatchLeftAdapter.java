package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.ReviewOptionMatchBase;

import java.util.List;

/**
 * Created by lzx on 2016/4/19.
 */
public class MatchLeftAdapter extends AppBaseAdapter<ReviewOptionMatchBase> {
    public MatchLeftAdapter(List<ReviewOptionMatchBase> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_popwindow_match_left,null);
            holder = new ViewHolder();
            holder.rl_match = (RelativeLayout) convertView.findViewById(R.id.rl_match);
            holder.tv_match = (TextView) convertView.findViewById(R.id.tv_match);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
            holder.tv_match.setText(list.get(position).getDisplay_name());
        if (list.get(position).isSelected()){
            holder.tv_match.setTextColor(context.getResources().getColor(R.color.theme_color));
            holder.rl_match.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            holder.tv_match.setTextColor(context.getResources().getColor(R.color.textcolor_1));
            holder.rl_match.setBackgroundColor(context.getResources().getColor(R.color.review_search_bg));
        }
            return convertView;
    }
    class ViewHolder{
        RelativeLayout rl_match;
        TextView tv_match;
    }
}
