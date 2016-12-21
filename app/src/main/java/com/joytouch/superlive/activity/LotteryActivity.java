package com.joytouch.superlive.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.ConfigUtils;

/**
 * Created by Administrator on 2015/11/17.
 */
public class LotteryActivity extends ActivityGroup implements View.OnClickListener {
    private ImageButton btn_help, btn_ranking;
    private FrameLayout containerActivity;
    private RadioGroup mRadioFGroup;
    private ImageButton web_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_activity);
        initView();
    }

    private void initView() {
        btn_help = (ImageButton) findViewById(R.id.btn_help);
        btn_ranking = (ImageButton) findViewById(R.id.btn_ranking);
        mRadioFGroup = (RadioGroup) findViewById(R.id.lottery_rg);
        containerActivity = (FrameLayout) findViewById(R.id.container_activity);
        web_back = (ImageButton) findViewById(R.id.web_back);
        Intent jincai = new Intent(this,JincaiActivity.class);
        containerActivity.addView(LotteryActivity.this.getLocalActivityManager().startActivity("jincai",jincai).getDecorView());
        btn_help.setVisibility(View.GONE);
        btn_ranking.setVisibility(View.GONE);
        web_back.setVisibility(View.GONE);

        btn_help.setOnClickListener(this);
        btn_ranking.setOnClickListener(this);
        web_back.setOnClickListener(this);
        mRadioFGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                containerActivity.removeAllViews();
                switch (i){
                    case R.id.lottery_touzhu:
                        btn_help.setVisibility(View.GONE);
                        btn_ranking.setVisibility(View.GONE);
                        web_back.setVisibility(View.GONE);
                        Intent jincai = new Intent(LotteryActivity.this,JincaiActivity.class);
                        jincai.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        containerActivity.addView(LotteryActivity.this.getLocalActivityManager().startActivity("jincai",jincai).getDecorView());
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_help:

                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Preference.jifen_help_url);
                startActivity(intent);
                break;
            case R.id.btn_ranking:
                //startActivity(new Intent(this, JiFenJingCaiRankingActivity.class));
                break;
            case R.id.web_back:
                Intent intent1 = new Intent();
                intent1.setAction("back");
                sendBroadcast(intent1);
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConfigUtils.removeActivtity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigUtils.addActivity(this);
    }
}
