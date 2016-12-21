package com.joytouch.superlive.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.javabean.registInfo;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 保存本地一些用户数据
 * Created by Administrator on 4/28 0028.
 */
public  class SaveOneInfoUtils {

    private registInfo registinfo;
    private Context context;
    private SharedPreferences sp;
    private int count;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String useri = (String) msg.obj;
            if (count < 5) {
                count++;
                registerJPush(useri);
            }
        }
    };
    public SaveOneInfoUtils(Context context, registInfo registinfo) {
        this.context=context;
        this.registinfo=registinfo;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    public void  save(){
        sp.edit().putString(Preference.token,
                registinfo.user_info.token)
                .putString(Preference.nickname,
                        registinfo.user_info.nick_name)
                .putString(Preference.headPhoto,
                        registinfo.user_info.image)
                .putString(Preference.name159,
                        registinfo.user_info.cp159_name)
                .putString(Preference.pwd159,
                        registinfo.user_info.cp159_passwd)
                .putString(Preference.myuser_id,
                        registinfo.user_info.userid)
                .putString(Preference.passwd,
                        registinfo.user_info.passwd)
                .putString(Preference.username,
                        registinfo.user_info.username)
                .putString(Preference.sign,
                        registinfo.user_info.sign)
                .putString(Preference.level,
                        registinfo.user_info.level)
                .putString(Preference.balance,
                        registinfo.user_info.balance)
                .putString(Preference.ballgold,
                        registinfo.user_info.balance2)
                .putString(Preference.mobile,registinfo.user_info.mobile)
                .commit();
        sp.edit().putString("settexit", "0").commit();
        Log.e("bindceshi", registinfo.user_info.userid + "----");
        SuperLiveApplication.imApi.bindUser(registinfo.user_info.userid, registinfo.user_info.token);
        registerJPush("cjzb" + registinfo.user_info.userid);
    }

    public void registerJPush(final String userid) {
        // 极光添加别名
        JPushInterface.setAlias(context.getApplicationContext(), userid, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                LogUtils.e("-----------------jsp", "cjzb" + i);
                if (i != 0) {
                    Message message = new Message();
                    message.obj = userid;
                    handler.sendMessageDelayed(message, 60000);
                }
            }
        });
        LogUtils.e("-----------------jsp", userid);
        JPushInterface.resumePush(context.getApplicationContext());
    }


}
