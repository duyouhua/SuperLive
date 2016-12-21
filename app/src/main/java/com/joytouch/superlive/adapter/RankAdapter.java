package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.GuessRank;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by sks on 2016/4/8.
 */
public class RankAdapter extends AppBaseAdapter<GuessRank> {
    private List<GuessRank> ranks;
    public RankAdapter(List<GuessRank> list, Context context) {
        super(list, context);
        this.ranks = list;
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
            holder.tv_gold_num = (TextView) convertView.findViewById(R.id.tv_gold_num);
            holder.tv_rank_num = (TextView) convertView.findViewById(R.id.tv_rank_num);
            holder.levelbg = (ImageView) convertView.findViewById(R.id.level);
            holder.topic = (ImageView) convertView.findViewById(R.id.topic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

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
        GuessRank rank = list.get(position);

        holder.tv_rank_num.setText(rank.getTop());
        holder.tv_name.setText(rank.getName());
        holder.tv_gold_num.setText(rank.getGold()) ;
        ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/" + rank.getImage_(), holder.iv_icon, ImageLoaderOption.optionsHeader);
        ConfigUtils.level(holder.levelbg,rank.getLevel_());
        if("1".equals(rank.getTopic())){
            holder.topic.setVisibility(View.VISIBLE);
        }else {
            holder.topic.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        ImageView iv_top_b;
        ImageView iv_top_a;
        TextView tv_name;
        ImageView iv_sex;
        TextView tv_gold_num;
        TextView tv_rank_num;
        ImageView iv_level;
        ImageView levelbg;
        ImageView topic;
    }
}
