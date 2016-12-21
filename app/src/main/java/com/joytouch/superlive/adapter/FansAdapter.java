package com.joytouch.superlive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.OtherUserMessageActivity;
import com.joytouch.superlive.app.Preference;
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
 * Created by sks on 2016/4/7.
 */
public class FansAdapter extends AppBaseAdapter<funs_item> {
    private final SharedPreferences sp;
    private List<funs_item> list;
    private Context context;
    public FansAdapter(List<funs_item> list, Context context) {
        super(list, context);
        this.list = list;
        this.context = context;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_fans,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.cb_attention = (CheckBox) convertView.findViewById(R.id.cb_attention);
            holder.anchor_fans= (TextView) convertView.findViewById(R.id.anchor_fans);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final funs_item bean=list.get(position);
        ImageLoader.getInstance().displayImage(Preference.img_url + "200x200/" + bean.image, holder.iv_icon, ImageLoaderOption.optionsHeader);
        holder.tv_name.setText(bean.nick_name);
//        holder.anchor_fans.setText(bean.level);
        ConfigUtils.level(holder.anchor_fans,bean.level);
        holder.tv_sign.setText(bean.sign);
        if (bean.is_con.equals("1")){
            holder.cb_attention.setChecked(true);//已关注
            holder.cb_attention.setText("√ 已关注");
        }else{
            holder.cb_attention.setChecked(false);//未关注
            holder.cb_attention.setText("+关注");
        }
        holder.cb_attention.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    FormBody.Builder build = new FormBody.Builder();
                    build.add("token", sp.getString(Preference.token, ""))
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .add("userid", bean.userid)
                            .build();
                    new HttpRequestUtils((Activity) context).httpPost(Preference.Addcon, build,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    holder.cb_attention.setChecked(false);
                                }

                                @Override
                                public void onSuccess(String json) {
                                    Log.e("添加关注", json);
                                    Gson gson = new Gson();
                                    BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                    if (Bean.status.equals("_0000")) {
                                        holder.cb_attention.setText("√ 已关注");
                                    } else {
                                        holder.cb_attention.setChecked(false);
                                        Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    FormBody.Builder build = new FormBody.Builder();
                    build.add("token", sp.getString(Preference.token, ""))
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .add("userid", bean.userid)
                            .build();
                    new HttpRequestUtils((Activity) context).httpPost(Preference.Delcon, build,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    holder.cb_attention.setChecked(true);
                                }

                                @Override
                                public void onSuccess(String json) {
                                    Log.e("取消关注", json);
                                    Gson gson = new Gson();
                                    BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                    if (Bean.status.equals("_0000")) {
                                        holder.cb_attention.setText("+关注");
                                    } else {
                                        holder.cb_attention.setChecked(true);
                                        Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将关注我的人userid传过去
                Intent intent = new Intent(context, OtherUserMessageActivity.class);
                intent.putExtra("user_id",bean.userid);
                intent.putExtra("othernickname",bean.nick_name);
                intent.putExtra("otherimg",bean.image);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        TextView tv_name,anchor_fans;
        ImageView iv_level;
        TextView tv_sign;
        CheckBox cb_attention;
    }
}
