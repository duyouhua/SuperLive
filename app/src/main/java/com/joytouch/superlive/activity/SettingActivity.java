package com.joytouch.superlive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.nitifyinfo;
import com.joytouch.superlive.utils.DataCleanManager;
import com.joytouch.superlive.utils.HttpRequestUtils;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.FormBody;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private RelativeLayout rl_black,rl_push,rl_about_us,rl_feedback,rl_cache,rl_phone;
    private Button but_exit;
    private TextView tv_cache_num;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("设置");
        iv_finish.setOnClickListener(this);
        rl_black = (RelativeLayout) this.findViewById(R.id.rl_black);
        rl_black.setOnClickListener(this);
        rl_push = (RelativeLayout) this.findViewById(R.id.rl_push);
        rl_push.setOnClickListener(this);
        rl_about_us = (RelativeLayout) this.findViewById(R.id.rl_about_us);
        rl_about_us.setOnClickListener(this);
        rl_feedback = (RelativeLayout) this.findViewById(R.id.rl_feedback);
        rl_feedback.setOnClickListener(this);
        but_exit = (Button) this.findViewById(R.id.but_exit);
        but_exit.setOnClickListener(this);
        rl_cache = (RelativeLayout) this.findViewById(R.id.rl_cache);
        rl_cache.setOnClickListener(this);
        rl_phone = (RelativeLayout) this.findViewById(R.id.rl_phone);
        rl_phone.setOnClickListener(this);
        tv_cache_num = (TextView) this.findViewById(R.id.tv_cache_num);
        try {
            String cache = DataCleanManager.getTotalCacheSize(this);
            tv_cache_num.setText(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (sp.getString(Preference.token,"").equals("")){
            but_exit.setVisibility(View.GONE);
        }else{
            but_exit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rl_black:
                toActivity(BlackActivity.class);
//                sendAlluser();//所有用户
                break;
            case R.id.rl_push:
                break;
            case R.id.rl_about_us:
                toActivity(AboutUsActivity.class);
//                sendOnLine();//所有在线
                break;
            case R.id.rl_feedback:
                toActivity(FeedBackActivity.class);
//                sendOntoOne();//单用户
                break;
            case R.id.but_exit:
                final Dialog dialog4 = new Dialog(SettingActivity.this, R.style.Dialog_bocop);
                dialog4.setContentView(R.layout.exitdialog);
                TextView close = (TextView) dialog4.findViewById(R.id.cancle);
                TextView sucess = (TextView) dialog4.findViewById(R.id.sucess);
                dialog4.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                    }
                });
                sucess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sp.edit()
                                .putString(Preference.token, "")
                                .putString(Preference.nickname, "")
                                .putString(Preference.headPhoto, "")
                                .putString(Preference.name159, "")
                                .putString(Preference.pwd159, "")
                                .putString(Preference.myuser_id, "")
                                .putString(Preference.passwd, "")
                                .putString(Preference.username, "")
                                .putString(Preference.sign, "")
                                .putString(Preference.level, "")
                                .putString(Preference.balance, "")
                                .commit();
                        Intent intent_ = new Intent(SettingActivity.this, LoadActivity.class);
                        intent_.putExtra("isstar", "0");
                        startActivity(intent_);
                        // 极光添加别名
                        JPushInterface.setAlias(getApplicationContext(), "",
                                null);
                        JPushInterface.resumePush(getApplicationContext());
                        finish();
                        dialog4.dismiss();
                    }
                });
                dialog4.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
                dialog4.setCancelable(true);//点击返回键取消
                dialog4.show();

                break;
            case R.id.rl_cache://清理缓存
                DataCleanManager.clearAllCache(SettingActivity.this);
                try {
                    String cache = DataCleanManager.getTotalCacheSize(this);
                    tv_cache_num.setText(cache);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_phone:
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:4009601681"));
                startActivity(intent);
                break;
        }
    }

    private void sendOntoOne() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("img_url1", "http://p3.so.qhimg.com/t010b3ad64dada31208.jpg")
                .add("img_intent_url1", "www.baidu.com")
                .add("text_url1", "http://www.sanw.net/mwxs/2015-10-27/4029.html")
                .add("content-type","application/x-www-form-urlencoded")
                .add("cache-control","no-cache")
                .add("postman-token","fb1887a614fb45c2935d7ad63b2b5029")
                .build();
        new HttpRequestUtils(this).httpPost("http://103.235.227.207:8091/v1/notify_image_url", build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(SettingActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("单用户通知", json);
                        Gson gson = new Gson();
                        nitifyinfo Bean = gson.fromJson(json, nitifyinfo.class);
                        if (Bean.getCode().equals("success")) {
                            Toast.makeText(SettingActivity.this, "单用户通知", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingActivity.this, Bean.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sendOnLine() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("img_url1", "http://p3.so.qhimg.com/t013a2ce7c04c0ad558.jpg")
                .add("img_intent_url1", "www.baidu.com")
                .add("text_url1", "http://tieba.baidu.com/p/4578425009")
                .add("content-type","application/x-www-form-urlencoded")
                .add("cache-control","no-cache")
                .add("postman-token","fb1887a614fb45c2935d7ad63b2b5029")
                .build();
        new HttpRequestUtils(this).httpPost("http://103.235.227.207:8091/v1/notify_image_url_online_users", build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(SettingActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("在线用户通知", json);
                        Gson gson = new Gson();
                        nitifyinfo Bean = gson.fromJson(json, nitifyinfo.class);
                        if (Bean.getCode().equals("success")) {
                            Toast.makeText(SettingActivity.this, "在线用户通知", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingActivity.this, Bean.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void sendAlluser() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("img_url1", "http://p3.so.qhimg.com/sdr/720_1080_/t0193560b42c1429a4d.jpg")
                .add("img_intent_url1", "www.baidu.com")
                .add("text_url1", "http://baike.so.com/doc/3401064-3579942.html")
                .add("content-type","application/x-www-form-urlencoded")
                .add("cache-control","no-cache")
                .add("postman-token","fb1887a614fb45c2935d7ad63b2b5029")
                .build();
        new HttpRequestUtils(this).httpPost("http://103.235.227.207:8091/v1/notify_image_url_all_users", build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(SettingActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("所有用户通知", json+"-----");
                        Gson gson = new Gson();
                        nitifyinfo Bean = gson.fromJson(json, nitifyinfo.class);
                        if (Bean.getCode().equals("success")) {
                            Toast.makeText(SettingActivity.this, "所有用户通知", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SettingActivity.this, Bean.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}
