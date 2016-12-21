package com.joytouch.superlive.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
	private static final String tag = "MyViewPager";
	private float preX;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(tag, "VIEWPAGER MotionEvent.ACTION_DOWN ");
			break;
		case MotionEvent.ACTION_UP:
			Log.i(tag, "VIEWPAGER MotionEvent.ACTION_UP ");
			
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i(tag, "VIEWPAGER MotionEvent.ACTION_MOVE ");
			
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.i(tag, "VIEWPAGER MotionEvent.ACTION_CANCEL ");
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	//解决viewpager滑动不流畅问题
	@Override
	public boolean onInterceptTouchEvent(MotionEvent even) {
		if(even.getAction()==MotionEvent.ACTION_DOWN)
		{
			preX=(int) even.getX();
		}else
		{
			if(Math.abs((int)even.getX()-preX)>10)
			{
				return true;
			}else
			{
				preX=(int) even.getX();
			}
		}
		return super.onInterceptTouchEvent(even);
	}
}
