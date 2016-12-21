package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.GuessDetail;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by sks on 2016/4/13.
 */
public class GuessDetailsLeftAdapter extends AppBaseAdapter<GuessDetail> {

    public GuessDetailsLeftAdapter(List<GuessDetail> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_guess_detail_left,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_gold = (TextView) convertView.findViewById(R.id.tv_gold);
            holder.last = (ImageView) convertView.findViewById(R.id.last);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GuessDetail detail = list.get(position);
        holder.tv_name.setText(detail.getName());
        holder.tv_gold.setText(detail.getMoney());
        ImageLoader.getInstance().displayImage(Preference.photourl+"200x200/"+detail.getImgurl(), holder.iv_icon, ImageLoaderOption.optionsHeader);
        if("1".equals(detail.getIsMe())){
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.main));
        }else {
            holder.tv_name.setTextColor(context.getResources().getColor(R.color.textcolor_2));
        }
        if("1".equals(detail.getIsLast())){
            holder.last.setVisibility(View.VISIBLE);
        }else{
            holder.last.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        TextView tv_name;
        TextView tv_gold;
        ImageView last;
    }
}
