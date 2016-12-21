package com.joytouch.superlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ResetNameActivity;
import com.joytouch.superlive.javabean.ShopItemInfo;

import java.util.List;

/**
 * Created by sks on 2016/4/19.
 */
public class MyPropAdapter extends AppBaseAdapter<ShopItemInfo> {
    public MyPropAdapter(List<ShopItemInfo> list, Context context) {
        super(list, context);
    }


    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_myprop,null);
            holder = new ViewHolder();
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_current_time = (TextView) convertView.findViewById(R.id.tv_current_time);
            holder.tv_use=(TextView)convertView.findViewById(R.id.tv_use);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShopItemInfo javabean = list.get(position);
        holder.tv_name.setText(javabean.name);
        holder.tv_time.setText("("+javabean.expire+")");
        holder.tv_current_time.setText(javabean.fail_day + "天后过期");


        if (javabean.type.equals("0")){//勋章
            if (javabean.name.equals("一级勋章")){
                holder.iv_icon.setBackgroundResource(R.drawable.xunzhang_1);
            }else if(javabean.name.equals("二级勋章")){
                holder.iv_icon.setBackgroundResource(R.drawable.xunzhang_2);
            }else if(javabean.name.equals("三级勋章")){
                holder.iv_icon.setBackgroundResource(R.drawable.xunzhang_3);
            }
        }else if (javabean.type.equals("1")){//昵称卡
            holder.iv_icon.setBackgroundResource(R.drawable.nichengka);
        }
//        ImageLoader.getInstance().displayImage(Preference.img_url + "200x200/" + javabean.image, holder.iv_icon, ImageUtils.options);

        //勋章
        if (javabean.type.equals("0")){
            holder.tv_use.setText("已佩戴");
            holder.tv_use.setEnabled(false);
        }else {//道具
            holder.tv_use.setText("使用");
            holder.tv_use.setEnabled(true);
        }

        holder.tv_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ResetNameActivity.class);
                intent.putExtra("nick_id",javabean.goods_id);
                intent.putExtra("origin", "1");
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_current_time;
        TextView tv_use;
    }
}
