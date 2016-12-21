package com.joytouch.superlive.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yj on 2015/12/9.
 * 自定义viewpager 禁止其滑动
 */
public class CoustomViewPager extends ViewPager {
    private boolean scrollble=true;
    public CoustomViewPager(Context context) {
        super(context);
    }

    public CoustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        if (scrollble) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            return false;
        }

    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}
