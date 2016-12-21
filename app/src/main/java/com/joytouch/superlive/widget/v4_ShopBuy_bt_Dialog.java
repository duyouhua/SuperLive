package com.joytouch.superlive.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ChargeActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.ShopItemInfo;
import com.joytouch.superlive.javabean.registInfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/1/25.
 * 购买的二次确定界面
 */
public class v4_ShopBuy_bt_Dialog extends Dialog implements View.OnClickListener{
    private Context context;
    private ShopItemInfo bean;

    public v4_ShopBuy_bt_Dialog(Context context) {
        super(context, R.style.Dialog_bocop);
        this.context = context;
    }
    private Button cancle;
    private Button sure;
    private boolean isSure = false;
    private SharedPreferences sp;
    private String id;
    private boolean isSuccess = false;
    private TextView content;
    private String sureContent;

    public void setBean(ShopItemInfo bean) {
        this.bean = bean;
    }

    public void setSureContent(String sureContent) {
        this.sureContent = sureContent;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopbuy);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        cancle = (Button) findViewById(R.id.v4_cancleBuy);
        sure = (Button) findViewById(R.id.v4_Buy);
        content = (TextView) findViewById(R.id.v4_sure_content);
        content.setText(sureContent);
        cancle.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.v4_cancleBuy:
                dismiss();
                break;
            case R.id.v4_Buy:
                if(!isSure){
                    isSure = true;
                    Buy_daoju();
                }
                break;
        }
    }

    private void Buy_daoju() {
        FormBody.Builder build_2=new FormBody.Builder();
        build_2
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("goods_id", id)
                .add("token",  sp.getString(Preference.token, ""))
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.insertgoods, build_2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("商品购买",json.toString());
                        Gson gson = new Gson();
                        registInfo registinfo = gson.fromJson(json, registInfo.class);
                        if (registinfo.status.equals("_0000")){
                            isSuccess=true;
                            Toast.makeText(context,"购买成功！", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else if (registinfo.status.equals("_1000")){
                            isSuccess=false;
                            new LoginUtils(context).reLogin(context);
                            Toast.makeText(context, registinfo.message, Toast.LENGTH_SHORT).show();
                        }else if (registinfo.status.equals("_2001")){
                            isSuccess=false;
                            Intent intent=new Intent(context, ChargeActivity.class);
                            context.startActivity(intent);
                            Toast.makeText(context, "余额不足", Toast.LENGTH_SHORT).show();
                        }else{
                            isSuccess=false;
                        }
                    }
                });
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
