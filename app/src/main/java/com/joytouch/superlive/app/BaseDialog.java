package com.joytouch.superlive.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.joytouch.superlive.R;

/**
 * Created by Administrator on 2016/4/25.
 */
public class BaseDialog extends Dialog {
    public Context context;
    public DisplayMetrics dm;
    public   int screenWidth;
    public int screenHight;
    public BaseDialog(Context context) {
        super(context, R.style.Dialog_bocop);
        setOwnerActivity((Activity) context);
        this.context =context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = new DisplayMetrics();
        // 注册事件
        context.registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        context.registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        context.registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHight = dm.heightPixels;
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                WindowManager windowManager = ((Activity) context).getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                //lp.height = (int) (display.getHeight());
                getWindow().setAttributes(lp);
            }
        });
    }

    private boolean islockScreen=false;
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction()) ) {//当按下电源键，屏幕亮起的时候

            }
            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction()) ) {//当按下电源键，屏幕变黑的时候
                islockScreen = true;
                dismiss();
            }
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()) ) {//当解除锁屏的时候
                islockScreen = false;
            }
        }
    };
}
