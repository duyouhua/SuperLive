package com.joytouch.superlive.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.LiveListLotteryInfo;
import com.joytouch.superlive.javabean.SendLotteryResult;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.widget.stickylistheaders.StickyListHeadersAdapter;

import java.util.ArrayList;

/**
 * Created by yj on 2016/5/4.
 * 直播列表竞猜
 */
public class LiveListLotteryAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private ArrayList<LiveListLotteryInfo> item;
    private Context context;
    public LayoutInflater inflater;
    public LiveListLotteryAdapter(Context context,ArrayList<LiveListLotteryInfo> item) {
        this.item = item;
        this.context = context;
        if(context!=null) {
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_livelist_group,null);
            holder = new GroupViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText(item.get(position).getTime().getTime());
        

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return item.get(position).getTime().getTimeLong();
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ChildViewHolder holder;
        if(view == null){
            view = inflater.inflate(R.layout.item_guess_mode,null);
            holder = new ChildViewHolder();
            holder.team1name = (TextView) view.findViewById(R.id.team1_name);
            holder.team2name = (TextView) view.findViewById(R.id.team2_name);
            holder.left = (RadioButton) view.findViewById(R.id.rl_victory);
            holder.center = (RadioButton) view.findViewById(R.id.rl_draw);
            holder.right = (RadioButton) view.findViewById(R.id.rl_fail);
            holder.leftgold = (TextView) view.findViewById(R.id.tv_victory_num);
            holder.centergold = (TextView) view.findViewById(R.id.tv_draw_num);
            holder.rightgold = (TextView) view.findViewById(R.id.tv_fail_num);
            holder.time = (TextView) view.findViewById(R.id.stoptime);
            holder.vs = (TextView) view.findViewById(R.id.tv_vs);
            holder.leftcontainer = (RelativeLayout) view.findViewById(R.id.leftcontainer);
            holder.centercontainer = (RelativeLayout) view.findViewById(R.id.center_container);
            holder.rightcontainer = (RelativeLayout) view.findViewById(R.id.right_container);
            holder.rl_center = (RelativeLayout) view.findViewById(R.id.rl_center);

            view.setTag(holder);
        }else {
            holder = (ChildViewHolder) view.getTag();
        }
        final LiveListLotteryInfo info = item.get(i);
        holder.team1name.setText(info.getLeft());
        holder.team2name.setText(info.getRight());
        holder.time.setText(info.getEndTime());
        holder.leftgold.setText(info.getLeftGold());
        holder.centergold.setText(info.getCenterGold());
        holder.rightgold.setText(info.getRightGold());

        holder.left.setEnabled(true);
        holder.center.setEnabled(true);
        holder.right.setEnabled(true);
        holder.left.setChecked(false);
        holder.center.setChecked(false);
        holder.right.setChecked(false);
        holder.left.setTextColor(context.getResources().getColor(R.color.color_yellow));
        holder.right.setTextColor(context.getResources().getColor(R.color.color_yellow));
        holder.center.setTextColor(context.getResources().getColor(R.color.color_yellow));
        //判断自己选择了哪边
        if(!TextUtils.isEmpty(info.getMyOption())){
            if(info.getMyOption().equals(info.getLeft())){
                holder.center.setEnabled(false);
                holder.right.setEnabled(false);
                holder.left.setChecked(true);
                holder.left.setTextColor(Color.WHITE);
                holder.center.setTextColor(Color.WHITE);
                holder.right.setTextColor(Color.WHITE);
            }
            if(info.getMyOption().equals(info.getCenter())){
                holder.left.setEnabled(false);
                holder.right.setEnabled(false);
                holder.center.setChecked(true);
                holder.left.setTextColor(Color.WHITE);
                holder.center.setTextColor(Color.WHITE);
                holder.right.setTextColor(Color.WHITE);
            }
            if(info.getMyOption().equals(info.getRight())){
                holder.left.setEnabled(false);
                holder.center.setEnabled(false);
                holder.right.setChecked(true);
                holder.left.setTextColor(Color.WHITE);
                holder.center.setTextColor(Color.WHITE);
                holder.right.setTextColor(Color.WHITE);
            }
        }
        if("2".equals(info.getMode())){
            holder.vs.setVisibility(View.VISIBLE);
            holder.center.setVisibility(View.GONE);
            holder.rl_center.setVisibility(View.GONE);
        }
        if("3".equals(info.getMode())){
            holder.vs.setVisibility(View.GONE);
            holder.center.setVisibility(View.VISIBLE);
            holder.rl_center.setVisibility(View.VISIBLE);
        }
        holder.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.left.setChecked(false);
                ConfigUtils.sendLotteryResult((Activity) context, info.getRoom_id(),info.getLotteryId(), info.getLeft(), info.getRoombet(), new ConfigUtils.ResultListener() {
                    @Override
                    public void result(SendLotteryResult result) {
                        holder.left.setChecked(true);
                        holder.center.setEnabled(false);
                        holder.right.setEnabled(false);
                        holder.left.setTextColor(Color.WHITE);
                        holder.right.setTextColor(Color.WHITE);
                        holder.center.setTextColor(Color.WHITE);
                        item.get(i).setMyOption(info.getLeft());
                        ConfigUtils.flash(context,holder.leftcontainer,info.getRoombet());
                        holder.leftgold.setText(result.getLeftgold());
                        holder.rightgold.setText(result.getRightgold());
                        holder.centergold.setText(result.getCentergold());
                    }

                    @Override
                    public void last(String islast) {

                    }
                });
            }
        });
        holder.center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.center.setChecked(false);
                ConfigUtils.sendLotteryResult((Activity) context,info.getRoom_id(), info.getLotteryId(), info.getCenter(), info.getRoombet(), new ConfigUtils.ResultListener() {
                    @Override
                    public void result(SendLotteryResult result) {
                        holder.left.setEnabled(false);
                        holder.right.setEnabled(false);
                        holder.center.setChecked(true);
                        holder.left.setTextColor(Color.WHITE);
                        holder.right.setTextColor(Color.WHITE);
                        holder.center.setTextColor(Color.WHITE);
                        item.get(i).setMyOption(info.getCenter());
                        ConfigUtils.flash(context, holder.centercontainer, info.getRoombet());
                        holder.leftgold.setText(result.getLeftgold());
                        holder.rightgold.setText(result.getRightgold());
                        holder.centergold.setText(result.getCentergold());
                    }

                    @Override
                    public void last(String islast) {

                    }
                });
            }
        });
        holder.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.right.setChecked(false);
                ConfigUtils.sendLotteryResult((Activity) context, info.getRoom_id(),info.getLotteryId(), info.getRight(), info.getRoombet(), new ConfigUtils.ResultListener() {
                    @Override
                    public void result(SendLotteryResult result) {
                        holder.left.setEnabled(false);
                        holder.center.setEnabled(false);
                        holder.right.setChecked(true);
                        item.get(i).setMyOption(info.getRight());
                        ConfigUtils.flash(context, holder.rightcontainer, info.getRoombet());
                        holder.leftgold.setText(result.getLeftgold());
                        holder.rightgold.setText(result.getRightgold());
                        holder.centergold.setText(result.getCentergold());
                        holder.left.setTextColor(Color.WHITE);
                        holder.right.setTextColor(Color.WHITE);
                        holder.center.setTextColor(Color.WHITE);
                    }

                    @Override
                    public void last(String islast) {

                    }
                });
            }
        });
        return view;
    }

    public class GroupViewHolder{
        TextView tv;
    }
    public class ChildViewHolder{
        TextView team1name;
        TextView team2name;
        RadioButton left;
        RadioButton center;
        RadioButton right;
        TextView leftgold;
        TextView centergold;
        TextView rightgold;
        TextView time;
        TextView vs;
        RelativeLayout leftcontainer;
        RelativeLayout centercontainer;
        RelativeLayout rightcontainer;
        RelativeLayout rl_center;
    }
}

