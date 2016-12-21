package com.joytouch.superlive.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.joytouch.superlive.R;


/**
 * Created by yj on 2015/12/29.
 * 加载页面
 */
public class LoadingDaliog extends Dialog{
    public LoadingDaliog(Context context) {
        super(context, R.style.load_bocop);
    }
    private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loadinglayout);
        logo = (ImageView) findViewById(R.id.v4_loading);
        RotateAnimation animation =new RotateAnimation(0f,360f, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(500);
        animation.setRepeatCount(-1);
        logo.startAnimation(animation);
    }
}
