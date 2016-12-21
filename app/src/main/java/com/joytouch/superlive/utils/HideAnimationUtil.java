package com.joytouch.superlive.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.joytouch.superlive.R;

/**
 * Created by sks on 2016/8/16.
 */
public class HideAnimationUtil {
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private View view5;
    private View view6;
    private View finish;
    private View info;
    private View gold;
    private Context context;
    private TranslateAnimation animation1;
    private TranslateAnimation animation2 ;
    private TranslateAnimation animation3;
    private TranslateAnimation animation4;
    private TranslateAnimation animation5 ;
    private TranslateAnimation animation6;
    private TranslateAnimation hide;
    private TranslateAnimation hide_left;
    private TranslateAnimation show_left;

    public HideAnimationUtil(View view1, View view2, View view3, View view4, View view5, View view6,View finish,View info,View gold, Context context) {
        this.view1 = view1;
        this.view2 = view2;
        this.view3 = view3;
        this.view4 = view4;
        this.view5 = view5;
        this.view6 = view6;
        this.context = context;
        this.finish = finish;
        this.info = info;
        this.gold = gold;
        initAnimation();
    }

    private void initAnimation() {
        animation1  = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.fullsceeenin);
        animation1.setInterpolator(new OvershootInterpolator(2f));
        animation2 = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.fullsceeenin);
        animation2.setInterpolator(new OvershootInterpolator(2f));
        animation3  = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.fullsceeenin);
        animation3.setInterpolator(new OvershootInterpolator(2f));
        animation4  = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.fullsceeenin);
        animation4.setInterpolator(new OvershootInterpolator(2f));
        animation5 = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.fullsceeenin);
        animation5.setInterpolator(new OvershootInterpolator(2f));
        animation6  = (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.fullsceeenin);
        animation6.setInterpolator(new OvershootInterpolator(2f));
        hide = (TranslateAnimation) AnimationUtils.loadAnimation(context,R.anim.hide);
        hide_left = (TranslateAnimation) AnimationUtils.loadAnimation(context,R.anim.hide_left);
        show_left = (TranslateAnimation) AnimationUtils.loadAnimation(context,R.anim.show_left);
    }

    public void showViewAnimation(){
        view1.clearAnimation();
        view1.startAnimation(animation1);
        view1.setVisibility(View.VISIBLE);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view2start();
                    }
                }, 50);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view1.clearAnimation();
                view1.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        finish.startAnimation(show_left);
        info.startAnimation(show_left);
        gold.startAnimation(show_left);
        show_left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                finish.setVisibility(View.VISIBLE);
                info.setVisibility(View.VISIBLE);
                gold.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void view2start(){
        view2.clearAnimation();
        view2.startAnimation(animation2);
        view2.setVisibility(View.VISIBLE);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view3start();
                    }
                },50);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view2.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void view3start(){
        view3.clearAnimation();
        view3.startAnimation(animation3);
        view3.setVisibility(View.VISIBLE);
        animation3.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view4start();
                    }
                },50);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view3.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void view4start(){
        view4.clearAnimation();
        view4.startAnimation(animation4);
        view4.setVisibility(View.VISIBLE);
        animation4.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view5start();
                    }
                },50);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view4.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void view5start(){
        view5.clearAnimation();
        view5.startAnimation(animation5);
        view5.setVisibility(View.VISIBLE);
        animation5.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view6.clearAnimation();
                        view6.startAnimation(animation6);
                        animation6.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                view6.clearAnimation();
                                view6.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                },50);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view5.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void hideAnimation(){
        view1.startAnimation(hide);
        view2.startAnimation(hide);
        view3.startAnimation(hide);
        view4.startAnimation(hide);
        view5.startAnimation(hide);
        view6.startAnimation(hide);
        hide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.e("animationend","gone");
                view1.setVisibility(View.GONE);
                view1.clearAnimation();
                view2.setVisibility(View.GONE);
                view2.clearAnimation();
                view3.setVisibility(View.GONE);
                view3.clearAnimation();
                view4.setVisibility(View.GONE);
                view4.clearAnimation();
                view5.setVisibility(View.GONE);
                view5.clearAnimation();
                view6.setVisibility(View.GONE);
                view6.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        finish.startAnimation(hide_left);
        info.startAnimation(hide_left);
        gold.startAnimation(hide_left);
        hide_left.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish.setVisibility(View.GONE);
                finish.clearAnimation();
                info.setVisibility(View.GONE);
                info.clearAnimation();
                gold.setVisibility(View.GONE);
                gold.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
