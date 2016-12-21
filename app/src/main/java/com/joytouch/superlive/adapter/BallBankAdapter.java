package com.joytouch.superlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.OtherUserMessageActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.rank_item;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yj on 2016/7/18.
 */
public class BallBankAdapter extends AppBaseAdapter<rank_item> {
    public BallBankAdapter(List<rank_item> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_rank,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.iv_top_b = (ImageView) convertView.findViewById(R.id.iv_top_b);
            holder.iv_top_a = (ImageView) convertView.findViewById(R.id.iv_top_a);
            holder.iv_sex = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.anchor_fans=(TextView)convertView.findViewById(R.id.anchor_fans);
            holder.tv_gold_num = (TextView) convertView.findViewById(R.id.tv_gold_num);
            holder.tv_rank_num = (TextView) convertView.findViewById(R.id.tv_rank_num);
            holder.iv_level=(ImageView)convertView.findViewById(R.id.level);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final rank_item info=list.get(position);
        ImageLoader.getInstance().displayImage(Preference.img_url + "40x40/" + info.image, holder.iv_icon, ImageLoaderOption.optionsHeader);
        if (position<3){
            holder.iv_top_a.setVisibility(View.VISIBLE);
            holder.iv_icon.setVisibility(View.VISIBLE);
            holder.iv_top_a.setVisibility(View.VISIBLE);
            holder.iv_top_b.setVisibility(View.VISIBLE);
            holder.tv_gold_num.setTextColor(context.getResources().getColor(R.color.rank_item_gold_num_color));
            if (position == 0){
                holder.iv_top_b.setBackgroundResource(R.drawable.rank_top1_b);
                holder.iv_top_a.setBackgroundResource(R.drawable.rank_top1_a);
            }
            if (position == 1){
                holder.iv_top_b.setBackgroundResource(R.drawable.rank_top2_b);
                holder.iv_top_a.setBackgroundResource(R.drawable.rank_top2_a);
            }
            if (position == 2){
                holder.iv_top_b.setBackgroundResource(R.drawable.rank_top3_b);
                holder.iv_top_a.setBackgroundResource(R.drawable.rank_top3_a);
            }
        }else {
            holder.iv_top_a.setVisibility(View.GONE);
            holder.iv_icon.setVisibility(View.GONE);
            holder.iv_top_a.setVisibility(View.GONE);
            holder.iv_top_b.setVisibility(View.GONE);
            holder.tv_gold_num.setTextColor(context.getResources().getColor(R.color.textcolor_1));
        }

        holder.tv_rank_num.setText(info.rank);
        holder.tv_name.setText(info.nick_name);
//        holder.anchor_fans.setText(info.level);
        ConfigUtils.level(holder.iv_level, info.level);
        holder.tv_gold_num.setText(info.balance);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OtherUserMessageActivity.class);
                intent.putExtra("user_id", info.userid);
                intent.putExtra("othernickname", info.nick_name);
                intent.putExtra("otherimg", info.image);
                Log.e("跳转", info.userid + "//" + info.nick_name + "//" + info.image);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        ImageView iv_top_b;
        ImageView iv_top_a;
        TextView tv_name;
        ImageView iv_sex;
        TextView tv_gold_num,anchor_fans;
        TextView tv_rank_num;
        ImageView iv_level;
    }

}
