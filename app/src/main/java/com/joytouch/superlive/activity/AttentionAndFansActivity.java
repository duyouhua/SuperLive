package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.AttentionFansViewPagerAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.fragement.AttentionFragment;
import com.joytouch.superlive.fragement.FansFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理关注和粉丝两个fragment
 */
public class AttentionAndFansActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_finish;
    private RadioButton rb_attention,rb_fans;
    private List<Fragment> fragments;
    private ViewPager viewPager;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_and_fans);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
        String type = getIntent().getStringExtra("type");
        if (null!=type&&type.equals("fans")){
            viewPager.setCurrentItem(1);
        }else {
            viewPager.setCurrentItem(0);
        }
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        rb_attention = (RadioButton) this.findViewById(R.id.rb_attention);
        rb_fans = (RadioButton) this.findViewById(R.id.rb_fans);
        viewPager = (ViewPager) this.findViewById(R.id.viewPager);

        iv_finish.setOnClickListener(this);
        rb_fans.setOnClickListener(this);
        rb_attention.setOnClickListener(this);
        fragments = new ArrayList<>();

        rb_attention.setText("关注"+sp.getString("tv_attention_num",""));
        rb_fans.setText("粉丝"+sp.getString("tv_fans_num",""));

        fragments.add( new AttentionFragment());
        fragments.add(new FansFragment());

        viewPager.setAdapter(new AttentionFansViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        rb_attention.setChecked(true);
                        break;
                    case 1:
                        rb_fans.setChecked(true);
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
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rb_attention:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_fans:
                viewPager.setCurrentItem(1);
                break;
        }
    }

}
