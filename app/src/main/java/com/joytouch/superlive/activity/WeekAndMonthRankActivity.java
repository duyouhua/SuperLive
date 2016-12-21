package com.joytouch.superlive.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.AttentionFansViewPagerAdapter;
import com.joytouch.superlive.fragement.BallBankFragment;
import com.joytouch.superlive.fragement.powerfulRankfragement;
import com.joytouch.superlive.fragement.weekRankFragement;
import com.joytouch.superlive.widget.RuleDescriptionDialog;
import java.util.ArrayList;

/**
 * 排行榜
 * Created by Administrator on 2016/4/27.
 */
public class WeekAndMonthRankActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_finish;
    private ImageView iv_rules;
    private RadioButton rb_weekrank;
    private RadioButton rb_powerful_rank;
    private RadioButton ball_bank;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private RadioGroup group;
    //OK代表从其他处调到这个界面是否显示富豪榜,0代表从"我的"排行榜进入,1代表其它地方进入
    private String ok="0";
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekmonthrank);
        ok=getIntent().getStringExtra("ok");
        initView();
    }

    private void initView() {
        view=this.findViewById(R.id.view);
        group  = (RadioGroup) findViewById(R.id.group);
        ball_bank = (RadioButton) findViewById(R.id.ball_bank);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_rules=(ImageView)this.findViewById(R.id.iv_rules);
        iv_finish.setOnClickListener(this);
        iv_rules.setOnClickListener(this);
        rb_weekrank=(RadioButton)this.findViewById(R.id.rb_weekrank);
        rb_powerful_rank=(RadioButton)this.findViewById(R.id.rb_powerful_rank);
        rb_weekrank.setOnClickListener(this);
        rb_powerful_rank.setOnClickListener(this);
        ball_bank.setOnClickListener(this);
        viewPager = (ViewPager) this.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        fragments = new ArrayList<>();
        if (ok.equals("0")){
            fragments.add(new BallBankFragment());
            fragments.add( new weekRankFragement());
            fragments.add(new powerfulRankfragement());

        }else{
            fragments.add(new weekRankFragement());
            view.setVisibility(View.GONE);
            rb_weekrank.setText("本场投注排行榜");
            rb_weekrank.setBackgroundColor(Color.parseColor("#00b38a"));
            rb_powerful_rank.setVisibility(View.GONE);
        }


        viewPager.setAdapter(new AttentionFansViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (position != i) {
                        ((RadioButton) group.getChildAt(i)).setChecked(false);
                    } else {
                        if (!((RadioButton) group.getChildAt(i)).isChecked()) {
                            ((RadioButton) group.getChildAt(i)).setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rb_weekrank:
                viewPager.setCurrentItem(1);
                break;
            case R.id.rb_powerful_rank:
                viewPager.setCurrentItem(2);
                break;
            case R.id.ball_bank:
                viewPager.setCurrentItem(0);
                break;
            case R.id.iv_rules:
                RuleDescriptionDialog dialog = new RuleDescriptionDialog(this);
                dialog.show();
                break;
        }
    }
}
