package com.joytouch.superlive.widget;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LiveSourceActivity;
import com.joytouch.superlive.activity.cammerActivity;

/**
 * Created by yj on 2016/4/19.
 * 选择直播方式
 */
public class LiveSelectorDialog extends Dialog implements View.OnClickListener {
    private  Context context;
    private LinearLayout close;
    private LinearLayout soure;
    private LinearLayout camera;
    private ImageView bg;
    private DisplayMetrics dm;
    private  int screenWidth;
    private int screenHight;
    private LinearLayout group;
    private Bitmap bmp1;
    private Bitmap bmp2;
    private boolean iscamera;
    private boolean isSource;
    int count = 0;
    public LiveSelectorDialog(Context context) {
        super(context, R.style.add_dialog);
        setOwnerActivity((Activity) context);
        this.context =context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_live_selector);
        dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHight = dm.heightPixels;
        initView();
    }

    private void initView() {
        close = (LinearLayout) findViewById(R.id.close);
        soure = (LinearLayout) findViewById(R.id.source_ll);
        camera = (LinearLayout) findViewById(R.id.camera_ll);
        group = (LinearLayout) findViewById(R.id.goup);
        bg = (ImageView) findViewById(R.id.bg);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                WindowManager windowManager = ((Activity) context).getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                getWindow().setAttributes(lp);
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                bg.destroyDrawingCache();
                handler.removeCallbacksAndMessages(null);
                openhanle.removeCallbacksAndMessages(null);
                handler = null;
                openhanle = null;

            }
        });
        close.setOnClickListener(this);
        soure.setOnClickListener(this);
        camera.setOnClickListener(this);
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_alpha);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                bg.clearAnimation();
//
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        bg.setAnimation(animation);
        soure.setVisibility(View.VISIBLE);
        camera.setVisibility(View.VISIBLE);
//        openAnimation();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
//            closeAnimation();
            dismiss();
        }
            return false; //默认返回 false
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close:
//                closeAnimation();
                dismiss();
                break;
            case R.id.camera_ll:
                if(!iscamera){
                    iscamera = true;
//                    LiveSettingDialog dialog = new LiveSettingDialog(context);
//                    dialog.setCancelable(true);
//                    dialog.show();
                    dismiss();
                    Intent intent=new Intent(context,cammerActivity.class);
                    context.startActivity(intent);
                }
                break;
            case R.id.source_ll:
                if(!isSource){
                    isSource = true;
                    Intent itent = new Intent(context,LiveSourceActivity.class);
                    itent.putExtra("islive",true);
                    context.startActivity(itent);
                    dismiss();
                }
                break;
        }
    }
    //用于处理关闭时的下拉动画
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int i = msg.what;
            final View view = group.getChildAt(i);
            view.animate().translationY(screenHight).setDuration(400).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if(i == (group.getChildCount()-1)) {
                        dismiss();
                    }
                    view.clearAnimation();
                    view.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }
    };
    public void  closeAnimation(){
        int childcount = group.getChildCount();
        for(int i = 0;i<childcount;i++){
            if(i!=0){
                handler.sendEmptyMessageDelayed(i,100);
            }else {
                handler.sendEmptyMessage(i);
            }

        }
    }
    //用于处理打开时的下拉动画
    Handler openhanle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int i = msg.what;
            final View view = group.getChildAt(i);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_translationy);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            view.startAnimation(animation);
        }
    };
    public void  openAnimation(){
        int childcount = group.getChildCount();
        for(int i = 0;i<childcount;i++){
            if(i!=0){
                openhanle.sendEmptyMessageDelayed(i,100);
            }else {
                openhanle.sendEmptyMessage(i);
            }

        }
    }
}
