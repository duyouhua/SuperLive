package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.SelectorAchor;
import com.joytouch.superlive.javabean.AnchorInfo;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yj on 2016/4/12.
 * 主播列表
 */
public class AnchorListAdapter extends AppBaseAdapter<AnchorInfo>{
    public AnchorListAdapter(List<AnchorInfo> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.anchor_listitem,null);
            holder = new ViewHolder();
            holder.header = (CircleImageView) convertView.findViewById(R.id.anchor_head);
            holder.name = (TextView) convertView.findViewById(R.id.anchor_name);
            holder.online = (TextView) convertView.findViewById(R.id.online);
            holder.betting = (TextView) convertView.findViewById(R.id.betting);
            holder.selector = (RadioButton) convertView.findViewById(R.id.selector);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        AnchorInfo info = list.get(position);
        ImageLoader.getInstance().displayImage(Preference.photourl+"100x100/"+info.getHeaderimg(),holder.header, ImageLoaderOption.optionsHeaderno);
        holder.name.setText(info.getAnchorname());
        holder.online.setText(info.getOnline() + "人");
        holder.betting.setText(info.getBetting() + "每注");
        holder.selector.setChecked(info.isselector());
        holder.selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectorAchor achor = (SelectorAchor) context;
                achor.anchor(position);
            }
        });

        return convertView;
    }
    public class ViewHolder{
            CircleImageView header;
            TextView name;
            TextView online;
            TextView betting;
            RadioButton selector;
    }
}
