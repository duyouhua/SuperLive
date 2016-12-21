package com.joytouch.superlive.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    public ViewPagerAdapter(List<View>views) {
        this.views = views;
    }



    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(View view, int position) {

        ((ViewPager) view).addView(views.get(position), 0);

        return views.get(position);
    }
    @Override
    public boolean isViewFromObject(View view, Object arg1) {
        return (view == arg1);
    }
    @Override
    public void destroyItem(View view, int position, Object arg2) {
        ((ViewPager) view).removeView(views.get(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager pager = (ViewPager) container;
        View view = (View) object;
        pager.removeView(view);
    }
    }