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
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.matchUtils;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * 修改昵称
 */
public class ResetNameActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;
    private ImageView iv_delete;
    private EditText et_name;
    private ImageView iv_error;
    private TextView tv_error;
    private SharedPreferences sp;
    //传0是个人直接进入修改界面,1表示从购买昵称卡其它地方计入
    private String origin="0";//默认是0,从个人信息过来的
    private String nick_id="";//从我的道具或者购买弹框后的来源
    private int tttime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_reset_name);
        nick_id=getIntent().getStringExtra("nick_id");
        origin=getIntent().getStringExtra("origin");
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("修改昵称");
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        tv_right.setText("完成");
        tv_right.setOnClickListener(this);
        iv_delete = (ImageView) this.findViewById(R.id.iv_delete);
        iv_delete.setVisibility(View.GONE);
        iv_delete.setOnClickListener(this);
        et_name = (EditText) this.findViewById(R.id.et_name);
        iv_error = (ImageView) this.findViewById(R.id.iv_error);
        tv_error = (TextView) this.findViewById(R.id.tv_error);
        et_name.addTextChangedListener(watcher);
        et_name.setText(sp.getString(Preference.nickname,""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_right:
                //完成操作
                String name = et_name.getText().toString();
                if (name.equals("")||name == null){
                    iv_error.setVisibility(View.VISIBLE);
                    tv_error.setVisibility(View.VISIBLE);
                    return;
                }else {
                    iv_error.setVisibility(View.GONE);
                    tv_error.setVisibility(View.GONE);
                }
                int length = matchUtils.getBytes(name);
                if (length>24){
                    iv_error.setVisibility(View.VISIBLE);
                    tv_error.setVisibility(View.VISIBLE);
                    tv_error.setText("昵称最多24个字符(或8个汉字)");
                    return;
                }else{
                    if (matchUtils.matchone(name) == true) {
                        Toast.makeText(ResetNameActivity.this, "昵称中包涵敏感字符，修改失败", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        UpdateNickname(name);
                    }
                }

                break;
            case R.id.iv_delete:
                et_name.setText("");
                break;
        }
    }

    private void UpdateNickname(final String name) {
        FormBody.Builder build_2=new FormBody.Builder();
        if (origin.equals("0")){
            build_2.add("nick_name", name)
                    .add("nick_card", origin)
                    .add("phone", Preference.phone)
                    .add("version", Preference.version)
                    .add("token", sp.getString(Preference.token,""))
                    .build();
            Log.e("修改1",origin+"/");
        }else{
            build_2.add("nick_name", name)
                    .add("nick_card",nick_id)
                    .add("phone", Preference.phone)
                    .add("version", Preference.version)
                    .add("token", sp.getString(Preference.token,""))
                    .build();
            Log.e("修改2", nick_id + "/");
        }

        new HttpRequestUtils(this).httpPost(Preference.EditNick, build_2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("修改昵称",json.toString());
                        Gson gson = new Gson();
                        BaseBean bean = gson.fromJson(json, BaseBean.class);
                        if (bean.status.equals("_0000")){
                            sp.edit().putString(Preference.nickname,name).commit();
                            finish();
                        }else if (bean.status.equals("_1000")){
                            new LoginUtils(ResetNameActivity.this).reLogin(ResetNameActivity.this);
                        }else{
                            if (tttime<1){
                                tttime=tttime+1;
                                Toast.makeText(ResetNameActivity.this, bean.message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            if (count == 0){
                iv_delete.setVisibility(View.GONE);
            }else {
                iv_delete.setVisibility(View.VISIBLE);
            }
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
        }
    };
}
