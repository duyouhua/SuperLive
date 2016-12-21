package com.joytouch.superlive.widget.MyScrollview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 7/16 0016.
 */
public class ChildViewPager extends ViewPager {


    public ChildViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
// TODO Auto-generated constructor stub

    }
    public static ViewPager mPager;//此处我直接在调用的时候静态赋值过来 的
    private int abc = 1;
    private float mLastMotionX;
    String TAG="@";

    private float firstDownX;
    private float firstDownY;
    private boolean flag=false;



    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        final float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPager.requestDisallowInterceptTouchEvent(true);
                abc=1;
                mLastMotionX=x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (abc == 1) {
                    if (x - mLastMotionX > 5 && getCurrentItem() == 0) {
                        abc = 0;
                        mPager.requestDisallowInterceptTouchEvent(false);
                    }


                    if (x - mLastMotionX < -5 && getCurrentItem()  == getAdapter().getCount()-1) {
                        abc = 0;
                        mPager.requestDisallowInterceptTouchEvent(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mPager.requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        return super.onTouchEvent(event);
    }
}