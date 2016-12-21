package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.MD5Util;

import java.io.IOException;

import okhttp3.FormBody;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;
    private EditText et_current;
    private EditText et_new;
    private EditText et_new2;
    private ImageView iv_error;
    private TextView tv_error;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_reset_password);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("修改密码");
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        tv_right.setText("完成");
        tv_right.setOnClickListener(this);
        et_current = (EditText) this.findViewById(R.id.et_pass1);
        et_new = (EditText) this.findViewById(R.id.et_pass2);
        et_new2 = (EditText) this.findViewById(R.id.et_pass3);
        iv_error = (ImageView) this.findViewById(R.id.iv_error);
        tv_error = (TextView) this.findViewById(R.id.tv_error);
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.iv_finish:
            finish();
            break;
        case R.id.tv_right:
            String pass = et_current.getText().toString();
            String passNew = et_new.getText().toString();
            String passNew2 = et_new2.getText().toString();
            if (et_current.getText().toString().equals("")){
                iv_error.setVisibility(View.VISIBLE);
                tv_error.setVisibility(View.VISIBLE);
                tv_error.setText("请输入密码");
                return;
            }else {
                iv_error.setVisibility(View.GONE);
                tv_error.setVisibility(View.GONE);
            }
            if (!passNew.equals(passNew2)){
                iv_error.setVisibility(View.VISIBLE);
                tv_error.setVisibility(View.VISIBLE);
                tv_error.setText("新密码输入不一致");
                return;
            }
            if (et_new.getText().toString().trim().equals("")){
                iv_error.setVisibility(View.VISIBLE);
                tv_error.setVisibility(View.VISIBLE);
                tv_error.setText("请输入新密码");
                return;
            }

            if (et_new.getText().toString().trim().equals(et_current.getText().toString())){
                Toast.makeText(ResetPasswordActivity.this, "新旧密码不能一致", Toast.LENGTH_SHORT).show();
                return;
            }
            if (et_new.getText().toString().trim().length()<6){
                iv_error.setVisibility(View.VISIBLE);
                tv_error.setVisibility(View.VISIBLE);
                tv_error.setText("新密码至少6位");
                return;
            }else{
                UpdatePasswd(pass,passNew2);
            }
            break;
        }
    }

    private void UpdatePasswd(String pass, String passNew2) {
        FormBody.Builder build_2=new FormBody.Builder();
        build_2.add("pwd_old", MD5Util.md5(pass))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("token", sp.getString(Preference.token,""))
                .add("pwd_new", MD5Util.md5(passNew2))
                .build();

        new HttpRequestUtils(this).httpPost(Preference.EditPwd, build_2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("修改密码", json.toString());
                        Gson gson = new Gson();
                        BaseBean bean = gson.fromJson(json, BaseBean.class);
                        if (bean.status.equals("_0000")){
                            Toast.makeText(ResetPasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else if (bean.status.equals("_1000")){
                            new LoginUtils(ResetPasswordActivity.this).reLogin(ResetPasswordActivity.this);
                        }else{
                            iv_error.setVisibility(View.VISIBLE);
                            tv_error.setVisibility(View.VISIBLE);
                            tv_error.setText("当前密码错误");
                            Toast.makeText(ResetPasswordActivity.this, bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
