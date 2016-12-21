package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.RedChaiUser;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by sks on 2016/7/24.
 */
public class RedAdapter extends AppBaseAdapter<RedChaiUser> {
    public RedAdapter(List<RedChaiUser> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        RedViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_redlist,null);
            holder = new RedViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_gold = (TextView) convertView.findViewById(R.id.tv_gold);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        }else {
            holder = (RedViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(Preference.img_url+"40x40/"+list.get(position).getReceiver_user_image(),holder.iv_icon, ImageLoaderOption.optionsHeader);
        holder.tv_name.setText(list.get(position).getReceiver_user_name());
        holder.tv_gold.setText(list.get(position).getMoney()+"金币");
        return convertView;
    }
    class RedViewHolder{
        CircleImageView iv_icon;
        TextView tv_name;
        TextView tv_gold;
    }
}
