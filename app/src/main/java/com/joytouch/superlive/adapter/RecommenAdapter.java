package com.joytouch.superlive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.RecommendattentionList;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.recom_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/4/26.
 */
public class RecommenAdapter extends AppBaseAdapter<recom_item> {
    private final ArrayList miaoshu;
    private final SharedPreferences sp;
    private List<recom_item> list;
    private Context context;
    private int k;
    public RecommenAdapter(List<recom_item> list, RecommendattentionList context, ArrayList miaoshu) {
        super(list, context);
        this.context=context;
        this.list=list;
        this.miaoshu=miaoshu;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        k=position;
       final ViewHolder holder ;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_recomment,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.iv_red_point = (ImageView) convertView.findViewById(R.id.iv_level);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.anchor_fans = (TextView) convertView.findViewById(R.id.anchor_fans);
            holder.tv_message= (TextView) convertView.findViewById(R.id.tv_message);
            holder.cb_state = (CheckBox) convertView.findViewById(R.id.cb_state);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
       final  recom_item info=  list.get(position);
        ImageLoader.getInstance().displayImage(Preference.img_url + "200x200/" + info.getImage(), holder.iv_icon, ImageLoaderOption.optionsHeader);
//        ImageLoader.getInstance().displayImage(Preference.img_url + "200x200/" + info.getImage(), holder.iv_icon, ImageUtils.reward);
        holder.tv_name.setText(info.getUsername());
        holder.anchor_fans.setText(info.getLevel());
        holder.tv_message.setText(miaoshu.get(k) + "");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cb_state.isChecked() == true) {
                    //关注
                    FormBody.Builder build = new FormBody.Builder();
                    build.add("token", sp.getString(Preference.token, ""))
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .add("userid", info.getUserid())
                            .build();
                    new HttpRequestUtils((Activity) context).httpPost(Preference.Addcon, build,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    holder.cb_state.setChecked(true);
                                }

                                @Override
                                public void onSuccess(String json) {
                                    Log.e("添加关注", json);
                                    Gson gson = new Gson();
                                    BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                    if (Bean.status.equals("_0000")) {
                                        holder.cb_state.setChecked(false);
                                        Toast.makeText(context, "已关注此人", Toast.LENGTH_SHORT).show();
                                    } else {
                                        holder.cb_state.setChecked(true);
                                        Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    //取消关注
                    FormBody.Builder build = new FormBody.Builder();
                    build.add("token", sp.getString(Preference.token, ""))
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .add("userid", info.getUserid())
                            .build();
                    new HttpRequestUtils((Activity) context).httpPost(Preference.Delcon, build,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    holder.cb_state.setChecked(false);
                                }

                                @Override
                                public void onSuccess(String json) {
                                    Log.e("取消关注", json);
                                    Gson gson = new Gson();
                                    BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                    if (Bean.status.equals("_0000")) {
                                        holder.cb_state.setChecked(true);
                                        Toast.makeText(context, "关注成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        holder.cb_state.setChecked(false);
                                        Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
        //绑定监听器
//        holder.cb_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//                if (arg1==true){
//                    holder.cb_state.setChecked(false);
//                }else {
//                    holder.cb_state.setChecked(true);
//                }
////                Toast.makeText(context,
////                        arg1 ? "选中了"+position : "取消了选中"+position, Toast.LENGTH_SHORT).show();
//            }
//        });
        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        ImageView iv_red_point;
        TextView tv_name;
        TextView anchor_fans;
        TextView tv_message;
        CheckBox cb_state;
    }

}
