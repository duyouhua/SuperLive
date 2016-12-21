package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.guess_item;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by sks on 2016/4/13.
 */
public class my_lottGuessDetailsLeftAdapter extends AppBaseAdapter<guess_item> {

    private final String s;

    public my_lottGuessDetailsLeftAdapter(List<guess_item> list, Context context, String s) {
        super(list, context);
        this.s=s;
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.itemmy_guess_detail_left,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_gold = (TextView) convertView.findViewById(R.id.tv_gold);
            holder.iv_budao=(ImageView)convertView.findViewById(R.id.iv_budao);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final guess_item info=list.get(position);
        ImageLoader.getInstance().displayImage(Preference.photourl+"200x200/"+info.image, holder.iv_icon, ImageLoaderOption.optionsHeader);
        holder.tv_name.setText(info.nick_name);
        if (s.equals("0")){//左边赢
            holder.tv_gold.setText(info.money);
        }else if (s.equals("1")){//右边赢
            holder.tv_gold.setText("0");
        }else{
            holder.tv_gold.setText(info.money);
        }

        if (info.last.equals("1")){
            holder.iv_budao.setVisibility(View.VISIBLE);
        }else{
            holder.iv_budao.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        TextView tv_name;
        TextView tv_gold;
        ImageView iv_budao;
    }
}
