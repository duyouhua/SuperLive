package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.BallGoldJavabean;

import java.util.List;

/**
 * Created by yj on 2016/7/20.
 */
public class BallGoldAdapter extends AppBaseAdapter<BallGoldJavabean> {
    public BallGoldAdapter(List<BallGoldJavabean> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_ballglod,null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
            holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        BallGoldJavabean javabean = list.get(position);
        holder.name.setText(javabean.getName());

        if(!"-1".equals(javabean.getIsAdd())){
            holder.money.setTextColor(context.getResources().getColor(R.color.color_yellow));
            holder.money.setText("收获  " + javabean.getMoney());
            if("1".equals(javabean.getIsAdd())){
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv1.setVisibility(View.VISIBLE);
                holder.tv1.setText(javabean.getRedname()+" +"+javabean.getRedmoney()+"球票");
                holder.tv2.setText(javabean.getBluename()+" +"+javabean.getBluemoney()+"球票");
            }else {
                holder.tv1.setVisibility(View.GONE);
                holder.tv2.setVisibility(View.GONE);
            }
        }else {
            holder.tv2.setVisibility(View.GONE);
            holder.tv1.setVisibility(View.VISIBLE);
            holder.money.setTextColor(context.getResources().getColor(R.color.textcolor_2));
            holder.money.setText("支付  "+javabean.getMoney());
            holder.tv1.setText(javabean.getPayway());
        }
        holder.time.setText(javabean.getDate());

        return convertView;
    }
    class ViewHolder{
     TextView name;
        TextView money;
        TextView time;
        TextView tv1;
        TextView tv2;
    }
}
