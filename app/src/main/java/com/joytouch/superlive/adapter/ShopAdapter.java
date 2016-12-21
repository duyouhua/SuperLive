package com.joytouch.superlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LiuLiangActivity;
import com.joytouch.superlive.javabean.ShopItemInfo;
import com.joytouch.superlive.widget.ShopBuyDialog;

import java.util.List;

/**
 * Created by sks on 2016/4/14.
 */
public class ShopAdapter extends AppBaseAdapter<ShopItemInfo> {
    public ShopAdapter(List<ShopItemInfo> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_goods,null);
            holder = new ViewHolder();
            holder.ll_expre=(LinearLayout)convertView.findViewById(R.id.ll_expre);
            holder.goodsName = (TextView) convertView.findViewById(R.id.v4_goods_name);
            holder.goodsLogo = (ImageView) convertView.findViewById(R.id.v4_goods_logo);
            holder.goodsPrice = (TextView) convertView.findViewById(R.id.v4_goods_price);
            holder.goodsBuy = (Button) convertView.findViewById(R.id.v4_shop_buy);
            holder.goodsTime = (TextView) convertView.findViewById(R.id.v4_goods_time);
            holder.logoBack = (RelativeLayout) convertView.findViewById(R.id.v4_logo_back);
            holder.ll_ball_qiu= (LinearLayout) convertView.findViewById(R.id.ll_ball_qiu);
            holder.v4_goods_qiu_price= (TextView) convertView.findViewById(R.id.v4_goods_qiu_price);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ll_ball_qiu.setVisibility(View.GONE);
        holder.goodsTime.setVisibility(View.VISIBLE);
        holder.ll_expre.setVisibility(View.VISIBLE);
        final ShopItemInfo javabean = list.get(position);
        holder.goodsName.setText(javabean.name);
        holder.goodsPrice.setText(javabean.price);
        holder.goodsTime.setText("(" + javabean.expire + "天)");
        holder.v4_goods_qiu_price.setText(javabean.old_price);
        GradientDrawable myGrad = (GradientDrawable)holder.logoBack.getBackground();
        if (!javabean.bgcolor.equals("")){
            myGrad.setColor(Color.parseColor(javabean.bgcolor));
        }
        if (javabean.type.equals("0")){//勋章
          if (javabean.name.equals("一级勋章")){
              holder.goodsLogo.setBackgroundResource(R.drawable.xunzhang_1);
          }else if(javabean.name.equals("二级勋章")){
              holder.goodsLogo.setBackgroundResource(R.drawable.xunzhang_2);
          }else if(javabean.name.equals("三级勋章")){
              holder.ll_ball_qiu.setVisibility(View.VISIBLE);
              holder.goodsLogo.setBackgroundResource(R.drawable.xunzhang_3);
          }
        }else if (javabean.type.equals("1")){//昵称卡
            holder.goodsLogo.setBackgroundResource(R.drawable.nichengka);
        }else if (javabean.type.equals("2")){//流量
            holder.goodsLogo.setBackgroundResource(R.drawable.liuliangbao);
            holder.goodsTime.setVisibility(View.GONE);
            holder.ll_expre.setVisibility(View.GONE);
        }
//        ImageLoader.getInstance().displayImage(Preference.img_url + "200x200/" + javabean.image, holder.goodsLogo, ImageUtils.reward);

        if (javabean.type.equals("0")){//勋章
            if(javabean.status.equals("1")){
                holder.goodsBuy.setBackgroundResource(R.drawable.retangle_222);
                holder.goodsBuy.setEnabled(false);
                holder.goodsBuy.setText("√ 已佩戴");
                holder.goodsBuy.setTextColor(context.getResources().getColor(R.color.textcolor_1));
            }else{
                holder.goodsBuy.setBackgroundResource(R.drawable.shop_buy_bac);
                holder.goodsBuy.setEnabled(true);
                holder.goodsBuy.setText("购买");
                holder.goodsBuy.setTextColor(context.getResources().getColor(R.color.color_yellow));
            }
        }else{
            //昵称卡
            holder.goodsBuy.setBackgroundResource(R.drawable.shop_buy_bac);
            holder.goodsBuy.setEnabled(true);
            holder.goodsBuy.setText("购买");
            holder.goodsBuy.setTextColor(context.getResources().getColor(R.color.color_yellow));
        }
        holder.goodsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(javabean.type.equals("2")){
//                    return;
//                }
                final ShopBuyDialog dialog = new ShopBuyDialog(context);
                dialog.setCancelable(false);
                dialog.setBean(javabean);
                dialog.setIsTools(true);
                dialog.setFrom("1");
                dialog.show();
            }
        });
        holder.goodsBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(javabean.type.equals("2")){
                    Intent intent=new Intent(context,LiuLiangActivity.class);
                    context.startActivity(intent);
                    return;
                }
                final ShopBuyDialog dialog = new ShopBuyDialog(context);
                dialog.setCancelable(false);
                dialog.setBean(javabean);
                dialog.setIsTools(true);
                dialog.setFrom("0");
                dialog.show();
            }
        });
   /*     convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShopBuyDialog dialog = new ShopBuyDialog(context);
                dialog.setCancelable(false);
                dialog.setBean(javabean);
                dialog.setIsTools(true);
                dialog.setFrom("0");
                dialog.show();
            }
        });*/
        return convertView;
    }
    public class ViewHolder{
        TextView goodsName,v4_goods_qiu_price;
        ImageView goodsLogo;
        TextView goodsPrice;
        Button goodsBuy;
        TextView goodsTime;
        RelativeLayout logoBack;
        LinearLayout ll_expre,ll_ball_qiu;
    }
}
