package com.joytouch.superlive.broadcast;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LiveDetailActivity;
import com.joytouch.superlive.activity.MainActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.LogUtils;

/**
 * Created by jy on 2015/12/7.
 */
public class V4_AlarmReceiver extends BroadcastReceiver {
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.e("==============", "receive" + intent.getStringExtra("roomid") +"   "+intent.getLongExtra("notionid", 0));
        int id  = (int) intent.getLongExtra("notionid", 0);
        String roomid = intent.getStringExtra("roomid");
        //当提醒后移除本地文件
        SharedPreferences preferences = context.getSharedPreferences(Preference.prefernce_alarms, Context.MODE_PRIVATE);
                    String matchname = preferences.getString("name"+intent.getAction(),"0");
                    preferences.edit().remove("aa"+intent.getAction()).remove("name"+intent.getAction()).commit();

            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.drawable.logo);
            mBuilder.setContentTitle(context.getResources().getString(R.string.app_name));
            mBuilder.setContentText("您关注的比赛"+matchname+"就要开始啦!");
            //向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_SOUND);
            //设置点击一次后消失（如果没有点击事件，则该方法无效。）
            mBuilder.setAutoCancel(true);
        String ss =intent.getAction();
        if(ss==null){
            ss = "0";
        }
            //点击通知之后需要跳转的页面
            Intent resultIntent = new Intent(context,LiveDetailActivity.class);
        resultIntent.putExtra("matchid",ss);
        resultIntent.putExtra("roomid",roomid);
            //使用TaskStackBuilder为“通知页面”设置返回关系
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            //为点击通知后打开的页面设定 返回 页面。（在manifest中指定）
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent pIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pIntent);
            // mId allows you to update the notification later on.
            LogUtils.e("==============",""+id);
            nm.notify(id, mBuilder.build());

    }
}
