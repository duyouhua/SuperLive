package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.funs_item;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by sks on 2016/8/16.
 */
public class InvitationFansAdapter extends AppBaseAdapter<funs_item> {
    public InvitationFansAdapter(List<funs_item> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_invitationfans,null);
            holder = new ViewHolder();
            holder.headimage = (CircleImageView) convertView.findViewById(R.id.headimage);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.state = (TextView) convertView.findViewById(R.id.state);
            holder.level_bg = (ImageView) convertView.findViewById(R.id.level_bg);
            holder.selector = (CheckBox) convertView.findViewById(R.id.selector);
            holder.selector.setEnabled(false);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(Preference.img_url + "40x40/" + list.get(position).image, holder.headimage, ImageLoaderOption.optionsHeader);
        holder.name.setText(list.get(position).nick_name);
        ConfigUtils.level(holder.level_bg, list.get(position).level);
        holder.state.setText(list.get(position).sign);
        if (list.get(position).isCheck()){
            holder.selector.setChecked(true);
        }else {
            holder.selector.setChecked(false);
        }
        return convertView;
    }
    public class ViewHolder{
        CircleImageView headimage;
        TextView name;
        TextView state;
        ImageView level_bg;
        CheckBox selector;
    }
}
