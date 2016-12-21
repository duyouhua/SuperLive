package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.TimeCountNum;
import com.joytouch.superlive.widget.TimeSelectDialog.DialogBase;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * 此dialog会随着键盘的弹出向上移动
 * Created by Administrator on 8/12 0012.
 */
public class BindNumberDialog extends DialogBase implements android.view.View.OnClickListener {

    private  int xml;
    private  Context context;
    private EditText et_phone;
    private TextView count_time;
    private EditText et_code;
    private LinearLayout id_positive;
    private LinearLayout id_negtive;
    private TimeCountNum timeCount;
    private SharedPreferences sp;

    public BindNumberDialog(Context context,int xml) {
        super(context);

        this.context=context;
        this.xml=xml;
        this.setCancel(true);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);//外部点击无效
        this.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        this.getWindow().setWindowAnimations(R.style.servicescheduledialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(xml);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        et_phone=(EditText)findViewById(R.id.et_phone);
        count_time=(TextView)findViewById(R.id.count_time);
        et_code=(EditText)findViewById(R.id.et_code);
        id_positive=(LinearLayout)findViewById(R.id.id_positive);
        id_negtive=(LinearLayout)findViewById(R.id.id_negtive);
        count_time.setOnClickListener(this);
        id_positive.setOnClickListener(this);
        id_negtive.setOnClickListener(this);
    }
    @Override
    protected void onBuilding() {

    }

    @Override
    protected boolean OnClickPositiveButton() {
        return false;
    }

    @Override
    protected void OnClickNegativeButton() {

    }

    @Override
    protected void onDismiss() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_positive:
                final String phone_num_=et_phone.getText().toString().trim();
                String code=et_code.getText().toString().trim();
                if (phone_num_.equals("") || code.equals("")){
                    return;
                }
                FormBody.Builder build_=new FormBody.Builder();
                build_.add("code", code)
                        .add("userid",sp.getString(Preference.myuser_id,""))
                        .add("phone", Preference.phone)
                        .add("mobile",phone_num_)
                        .add("version", Preference.version)
                        .build();
                new HttpRequestUtils((Activity) context).httpPost(Preference.bindmobile, build_,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onSuccess(String json) {
                                Log.e("开直播绑定手机号", json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")) {
                                    sp.edit().putString(Preference.mobile,phone_num_).commit();
                                    dismiss();
                                } else {
                                    dismiss();
                                    Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case R.id.id_negtive:
                dismiss();
                break;

            case R.id.count_time:
                String phone_num=et_phone.getText().toString().trim();
                if (phone_num.equals("")){
                    return;
                }
                FormBody.Builder build=new FormBody.Builder();
                build.add("phone_no", phone_num)
                        .add("action","0")
                        .add("phone", Preference.phone)
                        .add("version", Preference.version)
                        .build();
                new HttpRequestUtils((Activity) context).httpPost(Preference.Captcha, build,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onSuccess(String json) {
                                Log.e("绑定手机号验证码", json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")) {
                                    //访问接口,成功后执行以下
                                    timeCount = new TimeCountNum(count_time, 60000, 1000, "重新发送");
                                    timeCount.start();
                                } else {
                                    Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }
}
