package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.ReviewOptionMatch;

import java.util.List;

/**
 * Created by lzx on 2016/5/9.
 */
public class MatchRightAdapter extends AppBaseAdapter<ReviewOptionMatch> {
    private int mark;
    public MatchRightAdapter(List<ReviewOptionMatch> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_popwindow_time,null);
            holder = new ViewHolder();
            holder.iv_mark = (ImageView) convertView.findViewById(R.id.iv_mark_time);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time_time);
            holder.rl_bg = (RelativeLayout) convertView.findViewById(R.id.rl_bg);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_time.setText(list.get(position).getDisplay_name());
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
        RelativeLayout rl_bg;
    }
}
