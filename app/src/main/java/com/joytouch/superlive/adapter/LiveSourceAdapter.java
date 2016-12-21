package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.LiveSource;

import java.util.List;

/**
 * Created by yj on 2016/4/14.
 * 视频源的adapter
 */
public class LiveSourceAdapter extends AppBaseAdapter<LiveSource>{
    public OnRadioClickListener radioClickListener;
    public LiveSourceAdapter(List<LiveSource> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_livesource,null);
            holder = new ViewHolder();
            holder.selector = (CheckBox) convertView.findViewById(R.id.source_selector);
            holder.matchname = (TextView) convertView.findViewById(R.id.machname);
            holder.level = (TextView) convertView.findViewById(R.id.level);
            holder.user = (TextView) convertView.findViewById(R.id.loaduser);
            holder.count = (TextView) convertView.findViewById(R.id.usercount);
            holder.rl_base = (RelativeLayout) convertView.findViewById(R.id.rl_base);
            convertView.setTag(R.id.holder,holder);
        }else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }
        holder.matchname.setText(list.get(position).getMatch_name());
        if (list.get(position).isSelect()){
            holder.selector.setChecked(true);
        }else {
            holder.selector.setChecked(false);
        }
        holder.rl_base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioClickListener.onRaidoClick(holder.selector,position);
            }
        });

        return convertView;
    }
    public class ViewHolder{
        CheckBox selector;
        TextView matchname;
        TextView level;
        TextView user;
        TextView count;
        RelativeLayout rl_base;
    }

    public void setRadioClickListener(OnRadioClickListener radioClickListener) {
        this.radioClickListener = radioClickListener;
    }

    public interface OnRadioClickListener{
        void onRaidoClick(CheckBox checkBox,int position);
    }
}
