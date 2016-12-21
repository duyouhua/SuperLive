package com.joytouch.superlive.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.MD5Util;
import com.joytouch.superlive.utils.TimeCountNum;

import java.io.IOException;

import okhttp3.FormBody;

public class LostPasswordActivity extends BaseActivity implements OnClickListener{
    private TextView tv_title;
    private ImageView iv_finish;
    private EditText et_password;
    private TextView count_time;
    private TimeCountNum timeCount;
    private EditText et_phone;
    private Button but_load;
    private EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        et_code=(EditText)findViewById(R.id.et_code);
        but_load=(Button)findViewById(R.id.but_load);
        but_load.setOnClickListener(this);
        et_phone=(EditText)findViewById(R.id.et_phone);
        count_time=(TextView)findViewById(R.id.count_time);
        count_time.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("忘记密码");
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        et_password = (EditText) this.findViewById(R.id.et_password);
        et_password.setHint("输入新密码");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.but_load:
                if (et_code.getText().toString().equals("")){
                    Toast.makeText(LostPasswordActivity.this, "请输入手机号获取验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (et_password.getText().toString().equals("")){
                    Toast.makeText(LostPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (et_password.getText().toString().length()<6){
                    Toast.makeText(LostPasswordActivity.this, "密码至少6位", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phone_num_=et_phone.getText().toString().trim();
                FormBody.Builder build1=new FormBody.Builder();
                build1
                        .add("code",et_code.getText().toString().trim())
                        .add("new_passwd", MD5Util.md5(et_password.getText().toString().trim()))
                        .add("phone_no", phone_num_)
                        .add("phone", Preference.phone)
                        .add("version", Preference.version)
                        .build();
                new HttpRequestUtils(this).httpPost(Preference.FindPasswd, build1,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                            }
                            @Override
                            public void onSuccess(String json) {
                                Log.e("找回密码", json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")){
                                    //访问接口,成功后执行以下
                                    finish();
                                }else{
                                    Toast.makeText(LostPasswordActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
            case R.id.count_time:
                String phone_num=et_phone.getText().toString().trim();
                FormBody.Builder build=new FormBody.Builder();
                build
                        .add("phone_no", phone_num)
                        .add("phone", Preference.phone)
                        .add("version", Preference.version)
                        .build();
                new HttpRequestUtils(this).httpPost(Preference.Captcha, build,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                            }
                            @Override
                            public void onSuccess(String json) {
                                Log.e("注册验证码", json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")){
                                    //访问接口,成功后执行以下
                                    timeCount = new TimeCountNum(count_time, 60000, 1000, "重新发送");
                                    timeCount.start();
                                }else{
                                    Toast.makeText(LostPasswordActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
        }
    }

}
