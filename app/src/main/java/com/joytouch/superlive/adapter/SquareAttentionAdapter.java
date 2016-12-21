package com.joytouch.superlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LiveDetailActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.SquareAttention;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by lzx on 2016/4/20.
 */
public class SquareAttentionAdapter extends AppBaseAdapter<SquareAttention> {
    public SquareAttentionAdapter(List<SquareAttention> list, Context context) {
        super(list, context);
    }


    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_square_attention,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
            holder.tv_renzheng = (TextView) convertView.findViewById(R.id.tv_renzheng);
            holder.rl_statue = (RelativeLayout) convertView.findViewById(R.id.rl_statue);
            holder.iv_gueess = (ImageView) convertView.findViewById(R.id.iv_guessing);
            holder.tv_guess = (TextView) convertView.findViewById(R.id.tv_gueessing);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(Preference.photourl+"40x40/"+list.get(position).getImage(), holder.iv_icon, ImageLoaderOption.optionsHeader);
        holder.tv_name.setText(list.get(position).getNick_name());
        holder.tv_renzheng.setText(list.get(position).getSign());
        ConfigUtils.level(holder.iv_level, list.get(position).getLevel());
        if ("quiz".equals(list.get(position).getBehavitor_key())){
            holder.rl_statue.setVisibility(View.VISIBLE);
            holder.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_theme_color);
            holder.iv_gueess.setBackgroundResource(R.drawable.gueessing);
            holder.tv_guess.setText("正在竞猜");
        }else if ("live".equals(list.get(position).getBehavitor_key())){
            holder.rl_statue.setVisibility(View.VISIBLE);
            holder.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_yellow);
            holder.iv_gueess.setBackgroundResource(R.drawable.playing);
            holder.tv_guess.setText("正在直播");
        }else if ("look".equals(list.get(position).getBehavitor_key())){
            holder.rl_statue.setVisibility(View.VISIBLE);
            holder.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_square_looking);
            holder.iv_gueess.setBackgroundResource(R.drawable.looking);
            holder.tv_guess.setText("正在观看");
//            holder.rl_statue.setVisibility(View.GONE);
        }
        holder.rl_statue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LiveDetailActivity.class);
                intent.putExtra("romid",list.get(position).getBehavitor_value().getRoom_id());
                intent.putExtra("matchid", list.get(position).getBehavitor_value().getMatch_id());
                if ("quiz".equals(list.get(position).getBehavitor_key())){
                    intent.putExtra("qid",list.get(position).getBehavitor_value().getQuiz_id());
                }
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        TextView tv_name;
        ImageView iv_level;
        TextView tv_renzheng;
        RelativeLayout rl_statue;
        ImageView iv_gueess;
        TextView tv_guess;
    }
}
