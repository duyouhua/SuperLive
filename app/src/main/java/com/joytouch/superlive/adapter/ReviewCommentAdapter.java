package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.ReviewComment;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.RelativeDateFormat;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Date;
import java.util.List;

/**
 * Created by sks on 2016/4/11.
 */
public class ReviewCommentAdapter extends AppBaseAdapter<ReviewComment>{
    public ReviewCommentAdapter(List<ReviewComment> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_review_comment,null);
            holder = new ViewHolder();
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/" + list.get(position).getImage(), holder.iv_icon, ImageLoaderOption.optionsHeaderno);
        holder.tv_name.setText(list.get(position).getUser_name());
        holder.tv_comment.setText(list.get(position).getContent());
        Date date = new Date(Long.valueOf(list.get(position).getTime())*1000L);
        RelativeDateFormat dateFormat = new RelativeDateFormat();
        holder.tv_time.setText(dateFormat.format(date));
        return convertView;
    }
    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_comment;
    }
}
