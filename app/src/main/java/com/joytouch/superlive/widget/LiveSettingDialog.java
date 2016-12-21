package com.joytouch.superlive.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;

/**
 * Created by yj on 2016/4/19.
 * 直播设置
 */
public class LiveSettingDialog extends Dialog implements View.OnClickListener {
    private  Context context;
    private DisplayMetrics dm;
    private int screenWidth;
    private int screenHight;

    public LiveSettingDialog(Context context) {
        super(context, R.style.Dialog_bocop);
        setOwnerActivity((Activity) context);
        this.context =context;
    }
private RelativeLayout microphone;
    private  RelativeLayout camera;
    private ImageView cameraLogo;
    private TextView cameraName;
    private ImageView microphoneLogo;
    private TextView microphoneName;
    private boolean isMicrophone;
    private boolean isCamera;
    private ImageView bg;
    private ImageView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_live_setting);
        //获取屏幕的宽和高
        dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHight = dm.heightPixels;
        initView();
    }

    private void initView() {
        microphone = (RelativeLayout) findViewById(R.id.microphone);
        camera = (RelativeLayout) findViewById(R.id.camera);
        cameraLogo = (ImageView) findViewById(R.id.camera_logo);
        cameraName = (TextView) findViewById(R.id.camera_name);
        microphoneLogo = (ImageView) findViewById(R.id.microphone_logo);
        microphoneName = (TextView) findViewById(R.id.microphone_name);
        bg = (ImageView) findViewById(R.id.bg );
        close = (ImageView) findViewById(R.id.close);

        microphone.setOnClickListener(this);
        camera.setOnClickListener(this);
        close.setOnClickListener(this);

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                //将dialog设置成全屏
                WindowManager windowManager = ((Activity) context).getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                getWindow().setAttributes(lp);
            }
        });
        //处理dialog关闭时清除一些在使用的东西
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                bg.destroyDrawingCache();
                System.gc();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.microphone:
                if(isMicrophone){
                    isMicrophone = false;
                    microphone.setBackgroundResource(R.drawable.rentangle_60);
                    microphoneLogo.setBackgroundResource(R.drawable.microphone);
                    microphoneName.setTextColor(context.getResources().getColor(R.color.main));
                }else {
                    isMicrophone = true;
                    microphone.setBackgroundResource(R.drawable.retangle_main_15);
                    microphoneLogo.setBackgroundResource(R.drawable.hook);
                    microphoneName.setTextColor(context.getResources().getColor(R.color.white));
                }
                break;
            case R.id.camera:
                if(isCamera){
                    isCamera = false;
                    camera.setBackgroundResource(R.drawable.rentangle_60);
                    cameraLogo.setBackgroundResource(R.drawable.photograph);
                    cameraName.setTextColor(context.getResources().getColor(R.color.main));
                }else {
                    isCamera = true;
                    camera.setBackgroundResource(R.drawable.retangle_main_15);
                    cameraLogo.setBackgroundResource(R.drawable.hook);
                    cameraName.setTextColor(context.getResources().getColor(R.color.white));
                }
                break;
            case R.id.close:
                dismiss();
                break;
        }

    }




}
