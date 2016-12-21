package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.io.IOException;

import okhttp3.FormBody;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;
    private EditText et_content;
    private EditText et_email;
    private ImageView iv_camera;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("意见反馈");
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("提交");
        et_content = (EditText) this.findViewById(R.id.et_content);
        et_email = (EditText) this.findViewById(R.id.et_email);
        iv_camera = (ImageView) this.findViewById(R.id.iv_camare);
        iv_camera.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_right:
                String content = et_content.getText().toString();
                String et_emailtext=et_email.getText().toString();
                if (TextUtils.isEmpty(content) || TextUtils.isEmpty(et_emailtext) ){
                    Toast.makeText(FeedBackActivity.this, "内容或邮箱不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                FormBody.Builder build=new FormBody.Builder();
                build.add("token", sp.getString(Preference.token,""))
                        .add("phone", Preference.phone)
                        .add("title", et_emailtext)
                        .add("content",content)
                        .add("version", Preference.version)
                        .build();
                new HttpRequestUtils(this).httpPost(Preference.feedback, build,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                                Toast.makeText(FeedBackActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(String json) {
                                Log.e("一键反馈", json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")) {
                                    finish();
                                    Toast.makeText(FeedBackActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FeedBackActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                break;
            case R.id.iv_camare:
                break;
        }
    }
}
