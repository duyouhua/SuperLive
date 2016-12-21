package com.joytouch.superlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.GuessDetailsActivity;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.mylottery_item;

import java.util.List;

/**
 * Created by sks on 2016/4/8.
 */
public class LoteryListAdapter extends AppBaseAdapter<mylottery_item> {
    private  List<BaseBean> messagelist;
    private List<mylottery_item> ranks;
    public LoteryListAdapter(List<mylottery_item> list, Context context, List<BaseBean> messagelist) {
        super(list, context);
        this.ranks = list;
        this.messagelist=messagelist;
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_myguess,null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_content=(TextView)convertView.findViewById(R.id.tv_content);
            holder.tv_gold = (TextView) convertView.findViewById(R.id.tv_gold);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_result= (TextView) convertView.findViewById(R.id.tv_result);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final  mylottery_item info=list.get(position);
        holder.tv_name.setText(info.match_name);
        holder.tv_content.setText(info.que_content);
        holder.tv_gold.setText(info.my_option_money);
        if (info.start_time.length()>7){
            holder.tv_time.setText(info.start_time.substring(6, info.start_time.length()));
        }
        if (info.status.equals("2")){//进行中
            holder.tv_result.setTextColor(Color.parseColor("#00B38A"));
            holder.tv_result.setText("进行中");
        }else if (info.status.equals("3")){//待开奖
            holder.tv_result.setTextColor(Color.parseColor("#00B38A"));
            holder.tv_result.setText("待开奖");
        }else if (info.status.equals("4")){//已开奖
            holder.tv_result.setTextColor(Color.parseColor("#333333"));
            holder.tv_result.setText("未中奖");
        }else if (info.status.equals("5")){//已取消
            holder.tv_result.setTextColor(Color.parseColor("#999999"));
            holder.tv_result.setText("已取消");
        }else if (info.status.equals("6")){//赢
            holder.tv_result.setTextColor(Color.parseColor("#FF7A00"));
            holder.tv_result.setText("+"+info.my_win_money);
        }else{
            holder.tv_result.setTextColor(Color.parseColor("#333333"));
            holder.tv_result.setText("");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, GuessDetailsActivity.class);
                if (info.status.equals("2")){
                    intent.putExtra("meaage",messagelist.get(0).message);
                }else if (info.status.equals("3")){
                    intent.putExtra("meaage",messagelist.get(1).message);
                }else if (info.status.equals("4")){
                    intent.putExtra("meaage",messagelist.get(2).message);
                }else if (info.status.equals("5")){
                    intent.putExtra("meaage",messagelist.get(3).message);
                }else if (info.status.equals("6")){
                    intent.putExtra("meaage",messagelist.get(4).message);
                }
                intent.putExtra("room_id",info.room_id);
                intent.putExtra("que_id",info.que_id);
                intent.putExtra("name",info.match_name);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView tv_name;
        TextView tv_content;
        TextView tv_gold;
        TextView tv_time;
        TextView tv_result;
    }
}
