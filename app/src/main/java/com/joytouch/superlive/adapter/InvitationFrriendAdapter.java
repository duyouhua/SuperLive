package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.SelectorFans;
import com.joytouch.superlive.javabean.funs_item;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yj on 2016/4/27.
 * 邀请关注的好友界面的子条目
 */
public class InvitationFrriendAdapter extends AppBaseAdapter<funs_item> {

    public InvitationFrriendAdapter(List<funs_item> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_invitionfriend,null);
            holder = new ViewHolder();
            holder.headimg = (CircleImageView) convertView.findViewById(R.id.headimage);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.levelbg = (ImageView) convertView.findViewById(R.id.level_bg);
            holder.selector = (CheckBox) convertView.findViewById(R.id.selector);
            holder.sign = (TextView) convertView.findViewById(R.id.state);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        funs_item item = list.get(position);
        holder.name.setText(item.nick_name);
        holder.sign.setText(item.sign);
        ConfigUtils.level(holder.levelbg, item.level);
        holder.selector.setChecked(item.isCheck());
        ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + item.image, holder.headimg, ImageLoaderOption.optionsHeader);
        holder.selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectorFans fans = (SelectorFans) context;
                if (holder.selector.isChecked()) {

                    fans.add(position);
                    list.get(position).setIsCheck(true);
                } else {
                    fans.cancel(position);
                    list.get(position).setIsCheck(false);
                }
            }
        });
        return convertView;
    }
    private class ViewHolder{
        CircleImageView headimg;
        TextView name;
        ImageView levelbg;
        CheckBox selector;
        TextView sign;
    }
}
