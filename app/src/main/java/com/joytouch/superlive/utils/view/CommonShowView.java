package com.joytouch.superlive.utils.view;


import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;

/**
 * Created by Administrator on 5/10 0010.
 */

public class CommonShowView extends FrameLayout {

    private  TextView tvnull;
    private  LinearLayout ll_ping;
    private ImageView img;
    private CommonShowView im;
    private Context context;
    private LinearLayout loding_ll;
    private LinearLayout loading_failed;
    private LinearLayout loading_null;
    private Animation operatingAnim;
    public CommonShowView(Context context) {
        super(context);
    }

    public CommonShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        operatingAnim = AnimationUtils.loadAnimation(context, R.anim.loading);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        //必须是this
        View view = View.inflate(context, R.layout.loading, this);
        loding_ll=(LinearLayout)view.findViewById(R.id.loding_ll);
        loading_null=(LinearLayout)view.findViewById(R.id.loading_null);
        loading_failed=(LinearLayout)view.findViewById(R.id.loading_failed);
        ll_ping=(LinearLayout)view.findViewById(R.id.ll_ping);
        img = (ImageView) view.findViewById(R.id.v4_logo);
        tvnull=(TextView)view.findViewById(R.id.tvnull);
    }

    /**
     setInterpolator表示设置旋转速率。LinearInterpolator为匀速效果，
     Accelerateinterpolator为加速效果、DecelerateInterpolator为减速效果
     */
    public void starAnimal(){
        //在操作开始之前调用
        if (operatingAnim != null) {
            img.startAnimation(operatingAnim);
        }
    }

    public void stopAnimal(){
        //在操作开始之前调用
        if (operatingAnim != null) {
            img.clearAnimation();
        }
    }

    /**
     * 加载中
     */
    public void setloading(){
        loding_ll.setVisibility(VISIBLE);
        loading_null.setVisibility(GONE);
        loading_failed.setVisibility(GONE);
    }

    public void setTitle(String message){
        tvnull.setText(message);
    }
    /**
     * 数据为空
     */
    public void setnull(){
        loding_ll.setVisibility(GONE);
        loading_null.setVisibility(VISIBLE);
        loading_failed.setVisibility(GONE);
        //需要事件向下穿透
    }

    /**
     * 加载失败
     */
    public void setfail(){
        loding_ll.setVisibility(GONE);
        loading_null.setVisibility(GONE);
        loading_failed.setVisibility(VISIBLE);
        //点击屏幕重新加载
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        if (operatingAnim != null && img != null && operatingAnim.hasStarted()) {
            img.clearAnimation();
            img.startAnimation(operatingAnim);
        }
    }

//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return super.onTouchEvent(ev)  ;
//    }
}
