package com.joytouch.superlive.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.interfaces.changeStateCallback;
import com.joytouch.superlive.javabean.talkOneTone;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.TimeUtil;
import com.joytouch.superlive.widget.CircleImageView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by sks on 2016/4/12.
 */
public class ChatAdapter extends AppBaseAdapter<talkOneTone> {
    private final String otherimg;
    private final String myimg;

    private ViewHolder holder;

    public ChatAdapter(List<talkOneTone> list, Context context, String otherimg, String myimg) {
        super(list, context);
        this.otherimg=otherimg;
        this.myimg=myimg;
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_chat,null);
            holder = new ViewHolder();
            holder.rl_time = (RelativeLayout) convertView.findViewById(R.id.rl_time);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.iv_my_icon = (CircleImageView) convertView.findViewById(R.id.iv_my_icon);
            holder.iv_friend_icon = (CircleImageView) convertView.findViewById(R.id.iv_friend_icon);
            holder.tv_friend_message = (TextView) convertView.findViewById(R.id.tv_friend_message);
            holder.tv_my_message = (TextView) convertView.findViewById(R.id.tv_my_message);
            holder.iv_send_error = (ImageView) convertView.findViewById(R.id.iv_send_error);
            holder.progress_bar = (CircleProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        talkOneTone info=list.get(position);
        String myimageurl=Preference.img_url + "200x200/" + myimg;
        String otherimageurl=Preference.img_url + "200x200/" + otherimg;
        Log.e("touxiang",Preference.img_url + "200x200/" + otherimg);
        ImageLoader.getInstance().displayImage(myimageurl,holder.iv_my_icon, ImageLoaderOption.optionsHeader);
        ImageLoader.getInstance().displayImage(otherimageurl,holder.iv_friend_icon, ImageLoaderOption.optionsHeader);
//        ImageLoader.getInstance().displayImage(myimageurl, holder.iv_my_icon, ImageUtils.reward);
//        ImageLoader.getInstance().displayImage(otherimageurl, holder.iv_friend_icon, ImageUtils.reward);
        if (info.getLeft_right().equals("1")){//我发出

            holder.iv_friend_icon.setVisibility(View.GONE);
            holder.tv_friend_message.setVisibility(View.GONE);

            holder.iv_my_icon.setVisibility(View.VISIBLE);
            holder.tv_my_message.setVisibility(View.VISIBLE);

            holder.tv_my_message.setText(info.getContent());

            if (info.getTime()!=0){
                holder.rl_time.setVisibility(View.VISIBLE);
                holder.tv_time.setText(TimeUtil.currentLocalTimeString(info.getTime()));
            }else{
                holder.rl_time.setVisibility(View.GONE);
            }

        }else {

            holder.iv_friend_icon.setVisibility(View.VISIBLE);
            holder.tv_friend_message.setVisibility(View.VISIBLE);

            holder.iv_my_icon.setVisibility(View.GONE);
            holder.tv_my_message.setVisibility(View.GONE);

            holder.tv_friend_message.setText(info.getContent());

            if (info.getTime()!=0){
                holder.rl_time.setVisibility(View.VISIBLE);
                holder.tv_time.setText(TimeUtil.currentLocalTimeString(info.getTime()));
            }else{
                holder.rl_time.setVisibility(View.GONE);
            }
        }

        //1代表发送成功
        if(info.getStatus().equals("3")){
            holder.iv_send_error.setVisibility(View.GONE);
            holder.progress_bar.setVisibility(View.VISIBLE);
        }else {
            if (info.getStatus().equals("1") ){
                holder.iv_send_error.setVisibility(View.GONE);
                holder.progress_bar.setVisibility(View.GONE);
            }else{
                holder.iv_send_error.setVisibility(View.VISIBLE);
                holder.progress_bar.setVisibility(View.GONE);
            }
        }

        if (info.getLeft_right().equals("0")){
            holder.iv_send_error.setVisibility(View.GONE);
            holder.progress_bar.setVisibility(View.GONE);
        }

        holder.iv_send_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStateCallback back = (changeStateCallback) context;
                if (SuperLiveApplication.imApi.getStatus()==1){//重发成功
                    back.changestste("1", position);
                }else{//重发失败
                    back.changestste("0", position);
                }
            }
        });

        return convertView;
    }


    class ViewHolder{
        RelativeLayout rl_time;
        TextView tv_time;
        CircleImageView iv_friend_icon;
        TextView tv_friend_message;
        TextView tv_my_message;
        CircleImageView iv_my_icon;
        ImageView iv_send_error;
        CircleProgressBar progress_bar;
    }
}
