package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.getpay_one;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public class PayItemAdapter extends AppBaseAdapter<getpay_one> {

    public PayItemAdapter(List<getpay_one> list, Context context) {
        super(list, context);
    }
    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.adapter_payitem,null);
            holder = new ViewHolder();
            holder.rl_charge = (RelativeLayout) convertView.findViewById(R.id.rl_charge);
            holder.tv_gold_num = (TextView) convertView.findViewById(R.id.tv_gold_num);
            holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_gold_num.setText(list.get(position).blance);
        holder.tv_money.setText("¥ "+list.get(position).money);
        return convertView;
    }
    class ViewHolder{
        RelativeLayout rl_charge;
        TextView tv_gold_num;
        TextView tv_money;
    }
}

