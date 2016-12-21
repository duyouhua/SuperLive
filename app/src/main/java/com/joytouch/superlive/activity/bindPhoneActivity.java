package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.joytouch.superlive.utils.SaveOneInfoUtils;
import com.joytouch.superlive.utils.TimeCountNum;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Administrator on 4/28 0028.
 */
public class bindPhoneActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_title;
    private ImageView iv_finish;
    private TextView count_time;
    private EditText et_phone;
    private TimeCountNum timeCount;
    private Button but_load;
    private EditText et_code;
    private SharedPreferences sp;
    private String load="0";
    private String openid="";
    private String bind_type="";
    private String mime="";
    private String connect_name="";
    private String connect_image="";
    private String unionid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingphone);
        initView();
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        //load表示从Loadphoneactivity过来
        load=  getIntent().getStringExtra("load");
        openid=getIntent().getStringExtra("openid");
        bind_type=getIntent().getStringExtra("bind_type");
        mime=getIntent().getStringExtra("mime");
        connect_name=getIntent().getStringExtra("connect_name");
        connect_image=getIntent().getStringExtra("connect_image");
        unionid=getIntent().getStringExtra("unionid");
    }

    private void initView() {
        et_phone=(EditText)findViewById(R.id.et_phone);
        count_time=(TextView)findViewById(R.id.count_time);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        but_load=(Button)findViewById(R.id.but_load);
        et_code=(EditText)findViewById(R.id.et_code);
        tv_title.setText("绑定手机号");
        iv_finish.setOnClickListener(this);
        but_load.setOnClickListener(this);
        count_time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.count_time:
                String phone_num=et_phone.getText().toString().trim();
                FormBody.Builder build=new FormBody.Builder();
                build.add("phone_no", phone_num)
                        .add("phone", Preference.phone)
                        .add("version", Preference.version)
                        .build();
                new HttpRequestUtils(this).httpPost(Preference.Captcha, build,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                                Toast.makeText(bindPhoneActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(String json) {
                                Log.e("绑定手机号验证码",json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")) {
                                    //访问接口,成功后执行以下
                                    timeCount = new TimeCountNum(count_time, 60000, 1000, "重新发送");
                                    timeCount.start();
                                } else {
                                    Toast.makeText(bindPhoneActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
            case R.id.but_load:
                    if (et_phone.getText().toString().equals("")){
                        Toast.makeText(bindPhoneActivity.this,"手机号不能为空",1000).show();
                        return;
                    }
                    if (et_code.getText().toString().equals("")){
                        Toast.makeText(bindPhoneActivity.this,"验证码不能为空",1000).show();
                        return;
                    }
                     if (!et_phone.getText().toString().equals("") && !et_code.getText().toString().equals("") ){
                        //从loadphoneactivity执行此步骤,老用户且未绑定手机号的,正常登录绑定手机号
                        //调用bindOld接口,旧账号绑定手机号并登录
                        //0,1分别表示旧的未绑定手机号正常登陆和旧的未绑定手机号的三方账号
                         but_load.setEnabled(false);
                         if (load.equals("0") || load.equals("1")){
                            FormBody.Builder build_2=new FormBody.Builder();
                            build_2.add("code", et_code.getText().toString().trim())
                                    .add("phone", Preference.phone)
                                    .add("phone_no", et_phone.getText().toString().trim())
                                    .add("version", Preference.version) .
                                    add("passwd", sp.getString("passwd",""))
                                    .add("username",  sp.getString("username", ""))
                                    .build();

                            Log.e("綁定",et_code.getText().toString().trim()+"/"+et_phone.getText().toString().trim()+"/"+sp.getString("passwd","")+"/"+sp.getString("username", ""));
                            new HttpRequestUtils(this).httpPost(Preference.bindOld, build_2,
                                    new HttpRequestUtils.ResRultListener() {
                                        @Override
                                        public void onFailure(IOException e) {
                                            but_load.setEnabled(true);
                                        }
                                        @Override
                                        public void onSuccess(String json) {
                                            Log.e("綁定bindold",json.toString());
                                            Gson gson = new Gson();
                                            registInfo  registinfo = gson.fromJson(json, registInfo.class);
                                            if (registinfo.status.equals("_0000")){
                                                new SaveOneInfoUtils(bindPhoneActivity.this,registinfo).save();
                                                // SaveUserInfo(registinfo);
                                                toActivity(MainActivity.class);
                                                finish();
                                            }else{
                                                but_load.setEnabled(true);
                                                Toast.makeText(bindPhoneActivity.this, registinfo.message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }else if (load.equals("2")){//调用bindConnect 接口,新的联合登录绑定账号 并登录
                            //unionid在qq不傳,wx傳
                            FormBody.Builder build_3=new FormBody.Builder();
                            if (bind_type.equals("wx")){
                                build_3.add("unionid", unionid);
                            }
                            build_3
                                    .add("code", et_code.getText().toString().trim())
                                    .add("phone_no", et_phone.getText().toString().trim())
                                    .add("openid",openid)
                                    .add("bind_type",bind_type)
                                    .add("mime",mime)
                                    .add("connect_image", connect_image)
                                    .add("connect_name",  connect_name)
                                    .add("phone", Preference.phone)
                                    .add("version", Preference.version)
                                    .build();
                            new HttpRequestUtils(this).httpPost(Preference.bindConnect, build_3,
                                    new HttpRequestUtils.ResRultListener() {
                                        @Override
                                        public void onFailure(IOException e) {
                                            but_load.setEnabled(true);
                                        }
                                        @Override
                                        public void onSuccess(String json) {
                                            Log.e("綁定bindConnect",json.toString());
                                            Gson gson = new Gson();
                                            registInfo registinfo = gson.fromJson(json, registInfo.class);
                                            if (registinfo.status.equals("_0000")) {
                                                new SaveOneInfoUtils(bindPhoneActivity.this, registinfo).save();
                                                // SaveUserInfo(registinfo);
                                                Intent intent=new Intent(bindPhoneActivity.this,PersonalDataActivity.class);
                                                intent.putExtra("wxqq", "1");
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                but_load.setEnabled(true);
                                                Toast.makeText(bindPhoneActivity.this, registinfo.message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                    finish();


                break;
        }
    }
}
