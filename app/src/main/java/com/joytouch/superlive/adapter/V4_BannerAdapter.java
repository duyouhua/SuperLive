package com.joytouch.superlive.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yj on 2015/12/8.
 */
public class V4_BannerAdapter extends PagerAdapter {
    private List<View> views;
    public V4_BannerAdapter(List<View> views){
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
        super.destroyItem(container, position, object);
        ViewPager pager = (ViewPager) container;
        View view = (View) object;
        pager.removeView(view);
    }
}

