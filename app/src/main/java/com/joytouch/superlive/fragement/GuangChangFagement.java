package com.joytouch.superlive.fragement;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.joytouch.superlive.R;
import java.util.ArrayList;

/**
 * Created by Administrator on 6/22 0022.
 */
public class GuangChangFagement  extends android.app.Fragment implements View.OnClickListener  {
    private View view
            ;
    private RadioButton rb_lottery;
    private RadioButton rb_squ;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_guangchang,null);
        fragments = new ArrayList<Fragment>();
        initView();
        return view;
    }
    squ_lotteryFragement f1;
    private void initView() {
        rb_lottery=(RadioButton)view.findViewById(R.id.rb_lottery);
        rb_squ=(RadioButton)view.findViewById(R.id.rb_squ);
        rb_lottery.setOnClickListener(this);
        rb_squ.setOnClickListener(this);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        f1 = new squ_lotteryFragement();
        SquareFragment f2=new SquareFragment();
        fragments.add(f1);
        fragments.add(f2);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        rb_lottery.setChecked(true);
                        break;
                    case 1:
                        rb_squ.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_lottery:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_squ:
                viewPager.setCurrentItem(1);
                break;

        }
    }


    public void change(int position) {
        f1.change(position);
        Log.e("哈哈1",position+"");
    }
}
