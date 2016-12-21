package com.joytouch.superlive.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.WXhelpUtils;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

public class LoadActivity extends Activity implements View.OnClickListener{
    private static final int LOAD_TO_LOADPHONE = 11;
    private TextView tv_load,tv_register;
    private ImageView iv_qq,iv_weixin;
    private SharedPreferences sp;
    private String isstar="0";//默认不是从startactivity来的
    private ImageView iv_mohu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        isstar=getIntent().getStringExtra("isstar");
        setContentView(R.layout.activity_load);
        initView();
//        ConfigUtils.chagevegive(this);
    }

    private void initView() {
        iv_mohu=(ImageView)this.findViewById(R.id.iv_mohu);
        tv_load = (TextView) this.findViewById(R.id.tv_load);
        tv_register = (TextView) this.findViewById(R.id.tv_register);
        iv_qq = (ImageView) this.findViewById(R.id.iv_qq);
        iv_weixin = (ImageView) this.findViewById(R.id.iv_weixin);
        tv_load.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        iv_qq.setOnClickListener(this);
        iv_weixin.setOnClickListener(this);
        if (sp.getString("okmohu","0").equals("1")){
            changeMohu(iv_mohu);
            sp.edit().putString("okmohu","0").commit();
        }

    }
    public void changeMohu(View view){
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.pull_ok);
        view.startAnimation(anim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_load:
                Intent intent=new Intent(LoadActivity.this,LoadPhoneActivity.class);
                startActivityForResult(intent, LOAD_TO_LOADPHONE);
                finish();
                break;
            case R.id.tv_register:
                Intent intent2=new Intent(LoadActivity.this,RegisterActivity.class);
                startActivityForResult(intent2, LOAD_TO_LOADPHONE);
                finish();
                break;
            case R.id.iv_qq:
                LoginUtils utils=new LoginUtils(LoadActivity.this);
                utils.doQQLogin();
                break;
            case R.id.iv_weixin:
                LoginUtils util=new LoginUtils(LoadActivity.this);
                util.wxLogin();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode == LOAD_TO_LOADPHONE && requestCode==LOAD_TO_LOADPHONE){
//            finish();
//        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPageEnd("登录");
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("登录");
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
        if (!WXhelpUtils.wxMyCode.equals("")) {
            new LoginUtils(this).new Wxtoken(this, Preference.wx_appid, Preference.wx_appSecret,
                    WXhelpUtils.wxMyCode).execute();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isstar.equals("0")){
                //退出登录FLAG_ACTIVITY_CLEAR_TOP,MainActivity设置android:launchMode="singleTop"
               ConfigUtils.exit();
                finish();
            }else if (isstar.equals("1")){
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConfigUtils.removeActivtity(this);
    }
}
