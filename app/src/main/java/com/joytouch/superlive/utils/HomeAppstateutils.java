package com.joytouch.superlive.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.joytouch.superlive.activity.IntroductionActivity;
import com.joytouch.superlive.activity.MainActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.widget.updateVersion.UpDateVersion;

import java.util.List;

import static com.tencent.open.utils.Global.getContext;

/**
 * Created by Administrator on 5/11 0011.
 */
public class HomeAppstateutils {
    static boolean bcho;
    private static UpDateVersion updatever;

    private static  Context context;
    private static  SharedPreferences sp;
    private static Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            startMain();
        }
    };

    private static void startMain() {
        if (sp.getBoolean("isFirst", true) == true) {
            //第一次,进入引导页
            Intent intent = new Intent(context,
                    IntroductionActivity.class);
            context.startActivity(intent);
        } else {
            //非第一次,进入主页面
            Intent intent = new Intent(getContext(),
                    MainActivity.class);
            context.startActivity(intent);
        }
        ((Activity) context).finish();
    }

    public HomeAppstateutils(Context context) {
        this.context=context;
        sp = context.getSharedPreferences(Preference.preference, Context.MODE_PRIVATE);

    }
    /**
     * 注册广播监听是否后台运行
     * @param context
     */
    public static void JiantingHome(Context context){
        updatever = new UpDateVersion(context);
        updatever.setNewVersionListener(new UpDateVersion.NewVersionListener() {
            @Override
            public void onNewVersion() {
//                myHandler.removeCallbacksAndMessages(null);
            }
        });

        updatever.setUpdateCancelListener(new UpDateVersion.UpdateCancelListener() {
            @Override
            public void onUpdateCancel() {
//                startMain();
            }
        });

        updatever.update();

    }
    /**
     * 是否是在后台运行
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
				BACKGROUND=400 EMPTY=500 FOREGROUND=100
				GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
                Log.e("前后台", "PackageName=" + context.getPackageName() + "   " + "appProcess.importance="
                        + appProcess.importance + "   " + "getName()=" + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.e("处于前台", "处于前台" + context.getPackageName()+"/"+appProcess.processName);
                    bcho=false;
                } else {
                    Log.e("处于后台", "处于后台" + context.getPackageName()+"/"+appProcess.processName);
                    bcho=true;
                }
            }
        }
        return bcho;
    }


    /**
     * 在进程中去寻找当前APP的信息，判断是否在前台运行
     * @return
     */
    private static boolean isAppOnForeground()  {
        ActivityManager activityManager =(ActivityManager) getContext().getSystemService(
                Context.ACTIVITY_SERVICE);
        String packageName =getContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo>appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses){
            if (appProcess.processName.equals(packageName)&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    private static boolean isForeground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName)
                && currentPackageName.equals(context.getPackageName())) {
            Log.e("yy", "处于前台");
            return true;
        }
        Log.e("yy", "处于后台");
        return false;
    }
}
