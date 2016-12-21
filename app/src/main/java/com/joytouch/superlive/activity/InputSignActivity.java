package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * 修改签名
 */
public class InputSignActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;
    private EditText et_sign;
    private TextView tv_num;
    private SharedPreferences sp;
    private int tttime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_input_sign);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("个性签名");
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        tv_right.setText("完成");
        tv_num = (TextView) this.findViewById(R.id.tv_num);
        tv_right.setOnClickListener(this);
        et_sign = (EditText) this.findViewById(R.id.et_sign);
        et_sign.addTextChangedListener(watcher);
        et_sign.setText(sp.getString(Preference.sign, ""));
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
//            iv_delete.setVisibility(View.VISIBLE);
            tv_num.setText(String.valueOf(30-s.length()));
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_right://完成
                String sign=et_sign.getText().toString();
                UpdateSign(sign);
                break;
        }
    }

    private void UpdateSign(final String sign) {
        FormBody.Builder build_2=new FormBody.Builder();
        build_2.add("sign", sign)
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("token", sp.getString(Preference.token,""))
                .build();

        new HttpRequestUtils(this).httpPost(Preference.EditSign, build_2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("修改签名", json.toString());
                        Gson gson = new Gson();
                        BaseBean bean = gson.fromJson(json, BaseBean.class);
                        if (bean.status.equals("_0000")){
                            sp.edit().putString(Preference.sign,sign).commit();
                            finish();
                        }else if (bean.status.equals("_1000")){
                            new LoginUtils(InputSignActivity.this).reLogin(InputSignActivity.this);
                        }else{
                            if (tttime<1){
                                tttime=tttime+1;
                                Toast.makeText(InputSignActivity.this, bean.message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigUtils.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConfigUtils.removeActivtity(this);
    }
}
