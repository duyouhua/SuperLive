package com.joytouch.superlive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LiveDetailActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.SquareRecommend;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by lzx on 2016/4/20.
 */
public class SquareCommendAdapter extends AppBaseAdapter<SquareRecommend> {
    private SharedPreferences sp;
    public SquareCommendAdapter(List<SquareRecommend> list, Context context) {
        super(list, context);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_square_recommend,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
            holder.tv_renzheng = (TextView) convertView.findViewById(R.id.tv_renzheng);
            holder.rl_statue = (RelativeLayout) convertView.findViewById(R.id.rl_statue);
            holder.att_rl = (RelativeLayout) convertView.findViewById(R.id.att_rl);
            holder.tv_att = (TextView) convertView.findViewById(R.id.tv_att);
            holder.iv_gueess = (ImageView) convertView.findViewById(R.id.iv_guessing);
            holder.tv_guess = (TextView) convertView.findViewById(R.id.tv_gueessing);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(Preference.photourl + "40x40/" + list.get(position).getImage(), holder.iv_icon, ImageLoaderOption.optionsHeader);
        holder.tv_name.setText(list.get(position).getUsername());
        ConfigUtils.level(holder.iv_level, list.get(position).getLevel());
        holder.tv_renzheng.setText(list.get(position).getSign());
        holder.rl_statue.setVisibility(View.GONE);
        if ("0".equals(list.get(position).getIs_con())){
            holder.tv_att.setText("+ 关注");
            holder.att_rl.setBackgroundResource(R.drawable.charge_shape_fill_theme_color);
            holder.tv_att.setTextColor(context.getResources().getColor(R.color.white));
            holder.att_rl.setVisibility(View.VISIBLE);
        }else {
            holder.tv_att.setText("已关注");
            holder.att_rl.setBackgroundResource(R.drawable.charge_shape_fill_239_color);
            holder.tv_att.setTextColor(context.getResources().getColor(R.color.textcolor_1));
            holder.att_rl.setVisibility(View.GONE);
            holder.rl_statue.setVisibility(View.VISIBLE);
            if ("live".equals(list.get(position).getBehavitor_key())){//正在直播
                holder.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_yellow);
                holder.iv_gueess.setBackgroundResource(R.drawable.playing);
                holder.tv_guess.setText("正在直播");
            }else if ("quiz".equals(list.get(position).getBehavitor_key())){
                holder.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_theme_color);
                holder.iv_gueess.setBackgroundResource(R.drawable.gueessing);
                holder.tv_guess.setText("正在竞猜");
            }else if ("look".equals(list.get(position).getBehavitor_key())){
                holder.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_square_looking);
                holder.iv_gueess.setBackgroundResource(R.drawable.looking);
                holder.tv_guess.setText("正在观看");
            }

        }
        final ViewHolder finalHolder = holder;
        holder.rl_statue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LiveDetailActivity.class);
                intent.putExtra("romid",list.get(position).getBehavitor_value().getRoom_id());
                intent.putExtra("matchid",list.get(position).getBehavitor_value().getMatch_id());
                if ("quiz".equals(list.get(position).getBehavitor_key())){
                    intent.putExtra("qid",list.get(position).getBehavitor_value().getQuiz_id());
                }
                context.startActivity(intent);
            }
        });
        final ViewHolder finalHolder1 = holder;
        holder.att_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(list.get(position).getIs_con())){
                    finalHolder.att_rl.setEnabled(false);
                    FormBody.Builder build = new FormBody.Builder();
                    build.add("token", sp.getString(Preference.token,"")).add("phone", Preference.phone).add("version", Preference.version).add("userid", list.get(position).getUserid())
                            .build();
                    new HttpRequestUtils((Activity) context).httpPost(Preference.Addcon, build,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    finalHolder.att_rl.setEnabled(true);
                                    Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(String json) {
                                    finalHolder.att_rl.setEnabled(true);
                                    LogUtils.e("添加关注", json);
                                    Gson gson = new Gson();
                                    BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                    if (Bean.status.equals("_0000")) {
                                        list.get(position).setIs_con("1");
                                        finalHolder.tv_att.setText("已关注");
                                        finalHolder.att_rl.setBackgroundResource(R.drawable.charge_shape_fill_239_color);
                                        finalHolder.tv_att.setTextColor(context.getResources().getColor(R.color.textcolor_1));
                                        finalHolder.att_rl.setVisibility(View.GONE);

                                        if ("live".equals(list.get(position).getBehavitor_key())){//正在直播
                                            finalHolder1.rl_statue.setVisibility(View.VISIBLE);
                                            finalHolder1.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_yellow);
                                            finalHolder1.iv_gueess.setBackgroundResource(R.drawable.playing);
                                            finalHolder1.tv_guess.setText("正在直播");
                                        }else if ("quiz".equals(list.get(position).getBehavitor_key())){
                                            finalHolder1.rl_statue.setVisibility(View.VISIBLE);
                                            finalHolder1.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_theme_color);
                                            finalHolder1.iv_gueess.setBackgroundResource(R.drawable.gueessing);
                                            finalHolder1.tv_guess.setText("正在竞猜");
                                        }else if ("look".equals(list.get(position).getBehavitor_key())){
                                            finalHolder1.rl_statue.setVisibility(View.VISIBLE);
                                            finalHolder1.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_square_looking);
                                            finalHolder1.iv_gueess.setBackgroundResource(R.drawable.looking);
                                            finalHolder1.tv_guess.setText("正在观看");
                                        }

                                    } else {
                                        if (Bean.status.equals("_1000")) {
                                            Toast.makeText(context, "登录状态失效，请重新登录", Toast.LENGTH_SHORT).show();
                                            new LoginUtils(context).reLogin(context);
                                        } else {
                                            Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }else if ("1".equals(list.get(position).getIs_con())){
                    FormBody.Builder build = new FormBody.Builder();
                    build.add("token", (String) SPUtils.get(context,"token","",Preference.preference))
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .add("userid", list.get(position).getUserid())
                            .build();
                    new HttpRequestUtils((Activity) context).httpPost(Preference.Delcon, build,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    Toast.makeText(context, "取消关注失败", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(String json) {
                                    Gson gson = new Gson();
                                    BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                    if (Bean.status.equals("_0000")) {
                                        list.get(position).setIs_con("0");
                                        finalHolder.tv_guess.setText("+ 关注");
                                        finalHolder.rl_statue.setBackgroundResource(R.drawable.charge_shape_fill_theme_color);
                                        finalHolder.tv_guess.setTextColor(context.getResources().getColor(R.color.white));
                                    } else {
                                        if (Bean.status.equals("_1000")){
                                            Toast.makeText(context, "登录状态失效，请重新登录", Toast.LENGTH_SHORT).show();
                                            new LoginUtils(context).reLogin(context);
                                        }else {
                                            Toast.makeText(context, "取消关注失败", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });

                }
            }
        });

        return convertView;
    }
    class ViewHolder{
        CircleImageView iv_icon;
        TextView tv_name,tv_att;
        ImageView iv_level;
        TextView tv_renzheng;
        RelativeLayout rl_statue,att_rl;
        ImageView iv_gueess;
        TextView tv_guess;
    }
}
