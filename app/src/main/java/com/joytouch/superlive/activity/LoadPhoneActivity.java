package com.joytouch.superlive.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.registInfo;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.MD5Util;
import com.joytouch.superlive.utils.SaveOneInfoUtils;
import com.joytouch.superlive.utils.getPhoneIdutils;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.IOException;

import okhttp3.FormBody;

public class LoadPhoneActivity extends Activity implements View.OnClickListener{
    private static final int LOAD_TO_LOADPHONE = 11;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_finish;
    private EditText et_phone;
    private EditText et_pass;
    private ImageView iv_delete;
    private CircleProgressBar progressBar;
    private RelativeLayout rl_load;
    private TextView tv_load;
    private ImageView iv_load_result;
    private TextView tv_load_failed;
    private Handler handler = new Handler();
    private TextView tv_lost_password;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_phone);
        initView();
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    private void initView() {
        tv_lost_password=(TextView)this.findViewById(R.id.tv_lost_password);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        et_phone = (EditText) this.findViewById(R.id.et_phone);
        et_pass = (EditText) this.findViewById(R.id.et_password);
        iv_delete = (ImageView) this.findViewById(R.id.iv_delete);
        rl_load = (RelativeLayout) this.findViewById(R.id.rl_load);
        iv_load_result = (ImageView) this.findViewById(R.id.iv_load_result);
        tv_load_failed = (TextView) this.findViewById(R.id.tv_load_failed);
        rl_load.setOnClickListener(this);
        tv_load = (TextView) this.findViewById(R.id.tv_load);
        iv_delete.setVisibility(View.GONE);
        et_phone.addTextChangedListener(watcher);
        //调用数字键盘
//        et_phone.setInputType(EditorInfo.TYPE_CLASS_PHONE);
//        et_pass.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        progressBar = (CircleProgressBar) this.findViewById(R.id.progress_bar);
        tv_title.setText("登录");
        tv_right.setText("注册");
        tv_right.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        tv_lost_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right:
                Intent intent=new Intent(LoadPhoneActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_finish:
                Intent intent21=new Intent(LoadPhoneActivity.this,LoadActivity.class);
                intent21.putExtra("isstar","0");
                startActivity(intent21);
                finish();
                break;
            case R.id.tv_lost_password:
                Intent intent2=new Intent(LoadPhoneActivity.this,LostPasswordActivity.class);
                startActivity(intent2);
//                finish();
                break;
            case R.id.rl_load:
                iv_load_result.setVisibility(View.GONE);
                iv_load_result.setBackgroundResource(R.drawable.load_succeed);
                rl_load.setBackgroundResource(R.drawable.load_success_bg);
                tv_load_failed.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if (et_pass.getText().toString().length()<6 && (!et_pass.getText().toString().equals("")) && (!et_phone.getText().toString().equals(""))){
                    Toast.makeText(LoadPhoneActivity.this, "密码过于简单,请重置", Toast.LENGTH_SHORT).show();
                    Intent intent3=new Intent(LoadPhoneActivity.this,ResetPasswordActivity.class);
                    startActivity(intent3);
                    return;
                }
                if (TextUtils.isEmpty(et_phone.getText().toString()) || et_pass.getText().toString().equals("")){
                    tv_load.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            iv_load_result.setVisibility(View.VISIBLE);
                            iv_load_result.setBackgroundResource(R.drawable.load_failed);
                            rl_load.setBackgroundResource(R.drawable.load_failed_bg);
                            if (TextUtils.isEmpty(et_phone.getText().toString())){
                                tv_load_failed.setText("请输入手机号");
                            }else{
                                tv_load_failed.setText("请输入密码");
                            }
                            tv_load_failed.setVisibility(View.VISIBLE);
                        }
                    }, 1000);
                    return;
                }
                    new getPhoneIdutils(LoadPhoneActivity.this).getid();
                    String phoneid= sp.getString("phone_id","");
                    //显示进度
                    tv_load.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    //网络获取
                    FormBody.Builder build=new FormBody.Builder();
                    build
                            .add("username", et_phone.getText().toString().trim())
                            .add("passwd", MD5Util.md5(et_pass.getText().toString().trim()))
                            .add("mime", phoneid)
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .build();

                    new HttpRequestUtils(this).httpPost(Preference.normal, build,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                }
                                @Override
                                public void onSuccess(String json) {
                                    Log.e("登錄",json.toString());
                                    Gson gson = new Gson();
                                    final registInfo info = gson.fromJson(json, registInfo.class);
                                    if (info.status.equals("_0000")) {

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //登录成功后保存本地数据
                                                new SaveOneInfoUtils(LoadPhoneActivity.this,info).save();
                                                progressBar.setVisibility(View.GONE);
                                                iv_load_result.setVisibility(View.VISIBLE);
                                                rl_load.setBackgroundResource(R.drawable.load_success_bg);
                                                iv_load_result.setEnabled(false);
                                                Intent intent=new Intent(LoadPhoneActivity.this,MainActivity.class);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }, 1000);
                                    } else if (info.status.equals("_1001")) {//老用户正常登录需要绑定手机号
                                            Intent intent=new Intent(LoadPhoneActivity.this,bindPhoneActivity.class);
                                            intent.putExtra("load","0");
                                            startActivity(intent);
                                            sp.edit().putString(Preference.username,
                                                    info.user_info.username)
                                                    .putString(Preference.passwd,
                                                            info.user_info.passwd)
                                                    .commit();
                                        finish();
                                    } else {
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                            Toast.makeText(LoadPhoneActivity.this, info.message, Toast.LENGTH_SHORT).show();
//                                            //失败
                                            progressBar.setVisibility(View.GONE);
                                            iv_load_result.setVisibility(View.VISIBLE);
                                            iv_load_result.setBackgroundResource(R.drawable.load_failed);
                                            rl_load.setBackgroundResource(R.drawable.load_failed_bg);
                                            tv_load_failed.setVisibility(View.VISIBLE);
                                            }
                                        }, 1000);
                                    }
                                }
                            });
                break;

            case R.id.iv_delete:
                et_phone.setText("");
                iv_delete.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent=new Intent(LoadPhoneActivity.this,LoadActivity.class);
            intent.putExtra("isstar","0");
            startActivity(intent);
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //监听Edittext中数据变化
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
                iv_delete.setVisibility(View.VISIBLE);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
        }
    };

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
