package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.registInfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.MD5Util;
import com.joytouch.superlive.utils.SaveOneInfoUtils;
import com.joytouch.superlive.utils.TimeCountNum;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import okhttp3.FormBody;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_title;
    private ImageView iv_finish;
    private TextView count_time;
    private TimeCountNum timeCount;
    private Button but_load;
    private EditText et_phone;
    private EditText et_code;
    private EditText et_password;
    private registInfo registinfo;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();

    }

    private void initView() {
        et_password=(EditText)findViewById(R.id.et_password);
        et_code=(EditText)findViewById(R.id.et_code);
        et_phone=(EditText)findViewById(R.id.et_phone);
        but_load=(Button)findViewById(R.id.but_load);
        count_time=(TextView)findViewById(R.id.count_time);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title.setText("注册");
        iv_finish.setOnClickListener(this);
        count_time.setOnClickListener(this);
        but_load.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                Intent intent21=new Intent(RegisterActivity.this,LoadActivity.class);
                intent21.putExtra("isstar","1");
                startActivity(intent21);
                finish();
                break;
            case R.id.count_time:
                String phone_num=et_phone.getText().toString().trim();
                FormBody.Builder build=new FormBody.Builder();
                build
                        .add("action","1")
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
                                Log.e("注册验证码",json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")){
                                    //访问接口,成功后执行以下
                                    timeCount = new TimeCountNum(count_time, 60000, 1000, "重新发送");
                                    timeCount.start();
                                }else{
                                    Toast.makeText(RegisterActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;

            case R.id.but_load:
                if (et_phone.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"手机号不能为空",1000).show();
                    return;
                }
                if (et_password.getText().toString().equals("") || et_code.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this,"验证码或密码不能为空",1000).show();
                    return;
                }
                if (et_password.getText().toString().length()<6){
                    Toast.makeText(RegisterActivity.this, "密码最少6位", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    FormBody.Builder build_2=new FormBody.Builder();
                    build_2.add("code", et_code.getText().toString().trim())
                            .add("phone", Preference.phone)
                            .add("version", Preference.version) .
                             add("passwd", MD5Util.md5(et_password.getText().toString().trim()))
                            .add("username", et_phone.getText().toString().trim())
                            .build();
                    new HttpRequestUtils(this).httpPost(Preference.Reg, build_2,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    but_load.setEnabled(true);
                                    Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(String json) {
                                    Log.e("注册", json.toString());
                                    Gson gson = new Gson();
                                    registinfo = gson.fromJson(json, registInfo.class);
                                    if (registinfo.status.equals("_0000")) {
                                        new SaveOneInfoUtils(RegisterActivity.this, registinfo).save();
                                        // SaveUserInfo(registinfo);
                                        //注册成功后清楚签到本地保存的数据为0
                                        sp.edit()
                                                .putString("register_time","0")
                                                .commit();
                                        Intent intent = new Intent(RegisterActivity.this, PersonalDataActivity.class);
                                        intent.putExtra("wxqq", "0");
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        but_load.setEnabled(true);
                                        Toast.makeText(RegisterActivity.this, registinfo.message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }

                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent=new Intent(RegisterActivity.this,LoadActivity.class);
            intent.putExtra("isstar","0");
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 登录注册成功后保存用户信息
     * @param registinfo
     */
    private void SaveUserInfo(registInfo registinfo) {
        sp.edit().putString(Preference.token,
                registinfo.user_info.token)
                .putString(Preference.nickname,
                        registinfo.user_info.nick_name)
                .putString(Preference.headPhoto,
                        registinfo.user_info.image)
                .putString(Preference.name159,
                        registinfo.user_info.cp159_name)
                .putString(Preference.pwd159,
                        registinfo.user_info.cp159_passwd)
                .putString(Preference.myuser_id,
                        registinfo.user_info.userid)
                .commit();
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 手机号正则
        String regEx = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
