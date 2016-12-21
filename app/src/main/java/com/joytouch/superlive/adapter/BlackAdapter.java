package com.joytouch.superlive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.updateCallback;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.funs_item;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by sks on 2016/4/18.
 */
public class BlackAdapter extends AppBaseAdapter<funs_item> {
    private final SharedPreferences sp;

    public BlackAdapter(List<funs_item> list, Context context) {
        super(list, context);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_black,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
            holder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.tv_remove = (TextView) convertView.findViewById(R.id.tv_remove);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final funs_item bean=list.get(position);
        ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + bean.image, holder.iv_icon, ImageLoaderOption.optionsHeader);
        holder.tv_name.setText(bean.nick_name);
//        holder.tv_level.setText(bean.level);
        ConfigUtils.level(holder.tv_level,bean.level);
        holder.tv_sign.setText(bean.sign);
        final ViewHolder finalHolder = holder;
        holder.tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finalHolder.tv_remove.setVisibility(View.GONE);
                FormBody.Builder build2=new FormBody.Builder();
                build2.add("token", sp.getString(Preference.token,""))
                        .add("phone", Preference.phone)
                        .add("version", Preference.version)
                        .add("userid",bean.userid)
                        .build();
                new HttpRequestUtils((Activity) context).httpPost(Preference.Delblack, build2,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {

                            }

                            @Override
                            public void onSuccess(String json) {
                                Log.e("取消拉黑", json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")) {
                                    updateCallback anthorback = (updateCallback) context;
                                    anthorback.Callback(position);
                                } else {
                                    Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        TextView tv_name;
        TextView tv_level;
        ImageView iv_level;
        TextView tv_sign;
        TextView tv_remove;
    }
}
