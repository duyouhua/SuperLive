package com.joytouch.superlive.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.widget.MyViewPager;

import java.util.ArrayList;

/**
 * Created by yj on 5/11 0011.
 */
public class IntroductionActivity extends Activity implements ViewPager.OnPageChangeListener {
    private MyViewPager viewPager;
    private int size;
    private ImageView[] tips;
    private ArrayList<View> viewList;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    ViewGroup group;
    SharedPreferences sp;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.introd);

        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean("isFirst", false).commit();
        init();
    }

    private void init() {
        group = (ViewGroup)findViewById(R.id.viewGroup);
        //页面的集合
        LayoutInflater lf = getLayoutInflater().from(this);
        view1 = lf.inflate(R.layout.layout1, null);
        view2 = lf.inflate(R.layout.layout2, null);
        view3 = lf.inflate(R.layout.layout3, null);
        view4 = lf.inflate(R.layout.layout4, null);
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        size=viewList.size();
        viewPager = (MyViewPager) findViewById(R.id.viewpager);


        //点的集合
        tips = new ImageView[size];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(15, 0, 0, 0);
            imageView.setLayoutParams(lp);
//            imageView.setLayoutParams(new ViewGroup.LayoutParams(20,20));
            tips[i] = imageView;
            if(i == 0){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            group.addView(imageView);
        }


        viewPager.setAdapter(new MyAdapter());
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    /**
     * 屏幕滚动过程中不断被调用
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        this.position=position;
    }

    /**
     * 如果翻动成功了（滑动的距离够长），手指抬起来就会立即执行这个方法
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        setImageBackground(position % size);
    }

    /**
     * 在手指操作屏幕的时候发生变化,有三个值：0（END）,1(PRESS) , 2(UP)
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        if (position==0 && state==1){
            view2.findViewById(R.id.rl_layout2).setVisibility(View.GONE);
            sownmohu(view1.findViewById(R.id.rl_layout1), 1);
        }else if (position==1 && state==1){
            view1.findViewById(R.id.rl_layout1).setVisibility(View.GONE);
            view3.findViewById(R.id.rl_layout3).setVisibility(View.GONE);
            sownmohu(view2.findViewById(R.id.rl_layout2),2);
        }else if (position==2 && state==1 ){
            view2.findViewById(R.id.rl_layout2).setVisibility(View.GONE);
            view4.findViewById(R.id.rl_layout4).setVisibility(View.GONE);
            sownmohu(view3.findViewById(R.id.rl_layout3),3);
        }else if (position==3 && state==1){
            view3.findViewById(R.id.rl_layout3).setVisibility(View.GONE);
            sownmohu(view4.findViewById(R.id.rl_layout4),4);
        }

        if (state==0){
            view1.findViewById(R.id.rl_layout1).setVisibility(View.VISIBLE);
            view2.findViewById(R.id.rl_layout2).setVisibility(View.VISIBLE);
            view3.findViewById(R.id.rl_layout3).setVisibility(View.VISIBLE);
            view4.findViewById(R.id.rl_layout4).setVisibility(View.VISIBLE);
            if (position==0 ){
                changeMohu(view1.findViewById(R.id.rl_layout1));
            }else if (position==1){
                changeMohu(view2.findViewById(R.id.rl_layout2));
            }else if (position==2 ){
                changeMohu(view3.findViewById(R.id.rl_layout3));
            }else if (position==3){
                changeMohu(view4.findViewById(R.id.rl_layout4));
            }
        }

    }

    public void changeMohu(View view){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.pull_ok);
        view.startAnimation(anim);
    }
    public void sownmohu(View view,int i){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.pull_down);
        view.startAnimation(anim);
        if (i==1){
            view1.findViewById(R.id.rl_layout1).setVisibility(View.GONE);
        }else if (i==2){
            view2.findViewById(R.id.rl_layout2).setVisibility(View.GONE);
        }else if (i==3){
            view3.findViewById(R.id.rl_layout3).setVisibility(View.GONE);
        }else if (i==4){
            view4.findViewById(R.id.rl_layout4).setVisibility(View.GONE);
        }
    }

    class MyAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return size;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position), 0);//添加页卡
            if (position==size-1){
                viewList.get(position).findViewById(R.id.tv_join).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!sp.getString(Preference.token,"").equals("")){
                            Intent intent=new Intent(IntroductionActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent=new Intent(IntroductionActivity.this,LoadActivity.class);
                            intent.putExtra("isstar","1");
                            sp.edit().putString("okmohu","1").commit();
                            startActivity(intent);
                        }
                        finish();
                    }
                });
            }

            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        ConfigUtils.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConfigUtils.removeActivtity(this);
    }
}
