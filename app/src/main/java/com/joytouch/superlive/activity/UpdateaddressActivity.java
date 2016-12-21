package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.city.CityPicker;
import com.joytouch.superlive.utils.city.ShopPicker;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Administrator on 5/25 0025.
 */
public class UpdateaddressActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;
    private String name;
    private String tel;
    private String address;
    private EditText name_ok;
    private EditText tel_tv;
    private EditText detail_address;
    private String address_2;
    private TextView shengshi;
    private Button rl_bac;
    private TextView tv_bac;
    private LinearLayout select_shengshi;
    private TextView quxiao;
    private TextView tv_sucess;
    private ShopPicker cityPicker;
    String privenceid="";
    String quid="";
    String cityid="";
    private SharedPreferences sp;
    private String ident;
    private PopupWindow popupWindow;
    private TextView hello_kity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        //引用资源
        this.getClass().getClassLoader().getResourceAsStream("assets/" + "area");
        setContentView(R.layout.upaddress);
        name=getIntent().getStringExtra("name");
        tel=getIntent().getStringExtra("tel");
        address=getIntent().getStringExtra("address_1");
        address_2=getIntent().getStringExtra("address_2");
        ident=getIntent().getIntExtra("hy",3)+"";//0代表天机,1代表修改
        privenceid=getIntent().getStringExtra("province_id");
        cityid=getIntent().getStringExtra("city_id");
        quid=getIntent().getStringExtra("qu_id");
        initUI();
    }

    private void initUI() {
        rl_bac=(Button)findViewById(R.id.rl_bac);
        hello_kity=(TextView)findViewById(R.id.hello_kity);
        select_shengshi=(LinearLayout)findViewById(R.id.select_shengshi);
        select_shengshi.setOnClickListener(this);
        shengshi=(TextView)findViewById(R.id.shengshi);
        detail_address=(EditText)findViewById(R.id.detail_address);
        tel_tv=(EditText)findViewById(R.id.tel_tv);
        name_ok=(EditText)findViewById(R.id.name_ok);
        tv_right=(TextView)this.findViewById(R.id.tv_right);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title.setText("修改地址");
        tv_right.setText("保存");
        tv_right.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        shengshi.setText(address);
        detail_address.setText(address_2);
        name_ok.setText(name);
        tel_tv.setText(tel);
        rl_bac.setVisibility(View.GONE);
        rl_bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_bac.setVisibility(View.GONE);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                finish();
                break;

            case R.id.tv_right:
                FormBody.Builder build=new FormBody.Builder();
                Log.e("修改地址", build + "___" + ident + "___" + privenceid + "___" + cityid+"___"+quid);
                        build.add("phone", Preference.phone)
                                .add("token", sp.getString(Preference.token, ""))
                                .add("version", Preference.version)
                                .add("username", name_ok.getText().toString())
                                .add("province_id", privenceid)
                                .add("city_id", cityid)
                                .add("qu_id", quid)
                                .add("code", "")
                                .add("ident", ident)
                                .add("tel", tel_tv.getText().toString())
                                .add("address", detail_address.getText().toString())
                                .build();
                new HttpRequestUtils(this).httpPost(Preference.insert, build,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                                Toast.makeText(UpdateaddressActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(String json) {
                                Log.e("插入地址",json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")) {
                                    finish();
                                } else {
                                    Toast.makeText(UpdateaddressActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;

            case R.id.select_shengshi:
                showPpup();

                break;
            case R.id.quxiao:
                popupWindow.dismiss();
                rl_bac.setVisibility(View.GONE);
                break;

            case R.id.tv_sucess:
                rl_bac.setVisibility(View.GONE);
                popupWindow.dismiss();
                break;
        }
    }

    private void showPpup() {
        final View popupWindow_view = getLayoutInflater().inflate(
                R.layout.select_address, null);
        // 创建PopupWindow实例,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT分别是宽度和高度
        popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true); // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popupAnimation);
        // 这里是位置显示方式,在屏幕的底部，0,0分别表示X,Y的偏移量
        popupWindow.showAtLocation(hello_kity, Gravity.BOTTOM, 0, 0);
        quxiao=(TextView) popupWindow_view.findViewById(R.id.quxiao);
        tv_sucess=(TextView)popupWindow_view.findViewById(R.id.tv_sucess);
        quxiao.setOnClickListener(this);
        tv_sucess.setOnClickListener(this);
        cityPicker = (ShopPicker) popupWindow_view.findViewById(R.id.citypicker);
//        rl_bac.setVisibility(View.VISIBLE);
        cityPicker.setOnSelectingListener(new CityPicker.OnSelectingListener() {
            @Override
            public void selected(boolean selected) {
                privenceid = cityPicker.getPrivence_string();
                cityid = cityPicker.getCity_string();
                quid = cityPicker.getQu_string();
                shengshi.setText(cityPicker.getaddress());
            }
        });

    }
}
