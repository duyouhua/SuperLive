package com.joytouch.superlive.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by fengdongfei on 2015/12/18.
 * 倒计时封装类
 */
public class TimeCountNum extends CountDownTimer {
    private TextView button;
    private String msg;

    public TimeCountNum(TextView button, long millisInFuture, long countDownInterval, String msg) {
        super(millisInFuture, countDownInterval);
        this.button = button;
        this.msg = msg;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setEnabled(false);
//        button.setBackgroundResource(R.drawable.default_small_gray_selector);
        button.setText(millisUntilFinished / 1000 + "S后重发");
        button.setTextColor(Color.parseColor("#999999"));
    }

    @Override
    public void onFinish() {
        button.setText(msg);
        button.setEnabled(true);
        button.setTextColor(Color.parseColor("#333333"));
//        button.setBackgroundResource(R.drawable.default_blue_selector);
    }

}