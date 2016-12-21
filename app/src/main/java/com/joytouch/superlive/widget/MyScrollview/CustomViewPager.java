package com.joytouch.superlive.widget.MyScrollview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class CustomViewPager extends ViewPager {
	private boolean result = false;
	private static final String TAG = "CustomViewPager";
 
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
 
    public CustomViewPager(Context context) {
        super(context);
    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        // TODO Auto-generated method stub
//        int expandSpec = MeasureSpec.makeMeasureSpec(
//                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//
//        super.onMeasure(widthMeasureSpec, expandSpec);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//    if(result)
//    return super.onInterceptTouchEvent(arg0);
//    else
//    return false;
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent arg0) {
//    if(result)
//    return super.onTouchEvent(arg0);
//    else
//    return false;
//    }
//
}
