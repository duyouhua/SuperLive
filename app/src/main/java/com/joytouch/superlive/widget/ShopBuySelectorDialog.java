package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ChargeActivity;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.registInfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/7/21.
 * 购买商品金币选择
 */
public class ShopBuySelectorDialog extends BaseDialog {
    private boolean isBall;
    private String goods_id;
    private boolean isStart = false ;
    private boolean isSuccess = false;

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public ShopBuySelectorDialog(Context context) {
        super(context);
    }
private TextView goldprice;
    private  TextView ballgoldprice;
    private  TextView ok;
    private  TextView cancle;
    private RadioButton gold;
    private RadioButton ball;
    private String goldmoney;
    private String ballmoney;
    private String oldmoney;
    private boolean isActivty = false;
    private TextView actityprice;
    private TextView activty;

    public void setOldmoney(String oldmoney) {
        this.oldmoney = oldmoney;
    }

    public void setIsActivty(boolean isActivty) {
        this.isActivty = isActivty;
    }

    public void setGoldmoney(String goldmoney) {
        this.goldmoney = goldmoney;
    }

    public void setBallmoney(String ballmoney) {
        this.ballmoney = ballmoney;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_buyselector);
        initView();
    }

    private void initView() {
        goldprice = (TextView) findViewById(R.id.gold_price);
        gold = (RadioButton) findViewById(R.id.gold_price_selector);
        ballgoldprice = (TextView) findViewById(R.id.ballgold_price);
        ball = (RadioButton) findViewById(R.id.ballgold_price_selector);
        ok = (TextView) findViewById(R.id.ok);

        cancle = (TextView) findViewById(R.id.cancle);
        actityprice = (TextView) findViewById(R.id.activity_price);
        activty = (TextView) findViewById(R.id.activity_logo);
        actityprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if(isActivty){
            actityprice.setVisibility(View.VISIBLE);
            activty.setVisibility(View.VISIBLE);
            actityprice.setText(oldmoney);
        }else {
            actityprice.setVisibility(View.GONE);
            activty.setVisibility(View.GONE);
        }
        goldprice.setText(goldmoney);
        ballgoldprice.setText(ballmoney);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStart) {
                    return;
                }
                isStart = true;
                LogUtils.e("sssssssssss",""+isBall);
                if (isBall) {
                    ballBuy();
                } else {
                    godBuy();
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBall = false;
                ball.setChecked(false);
            }
        });
        ball.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBall = true;
                gold.setChecked(false);
            }
        });
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                WindowManager windowManager = ((Activity) context).getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.gravity = Gravity.BOTTOM;
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                getWindow().setAttributes(lp);
            }
        });
    }
    public void godBuy(){
        FormBody.Builder build_2=new FormBody.Builder();
        build_2
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("goods_id", goods_id)
                .add("token", (String) SPUtils.get(context,Preference.token,"",Preference.preference))
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.insertgoods, build_2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        isStart = false;
                    }
                    @Override
                    public void onSuccess(String json) {
                        isStart = false;
                        Log.e("商品购买", json.toString());
                        Gson gson = new Gson();
                        registInfo registinfo = gson.fromJson(json, registInfo.class);
                        if (registinfo.status.equals("_0000")) {
                            isSuccess = true;
                            Toast.makeText( context, "购买成功！", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else if (registinfo.status.equals("_1000")) {
                            new LoginUtils( context).reLogin( context);
                            Toast.makeText( context, registinfo.message, Toast.LENGTH_SHORT).show();
                        } else if (registinfo.status.equals("_2001")) {
                            Intent intent = new Intent( context, ChargeActivity.class);
                            context.startActivity(intent);
                            Toast.makeText( context, "余额不足", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void ballBuy(){
        FormBody.Builder build_2=new FormBody.Builder();
        build_2
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("goods_id", goods_id)
                .add("token", (String) SPUtils.get(context,Preference.token,"",Preference.preference))
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.ballBuy, build_2,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        isStart = false;
                    }
                    @Override
                    public void onSuccess(String json) {
                        isStart = false;
                        Log.e("商品购买", json.toString());
                        Gson gson = new Gson();
                        registInfo registinfo = gson.fromJson(json, registInfo.class);
                        if (registinfo.status.equals("_0000")) {
                            Toast.makeText( context, "购买成功！", Toast.LENGTH_SHORT).show();
                            isSuccess = true;
                            dismiss();
                        } else if (registinfo.status.equals("_1000")) {
                            new LoginUtils( context).reLogin( context);
                            Toast.makeText( context, registinfo.message, Toast.LENGTH_SHORT).show();
                        } else if (registinfo.status.equals("_2001")) {
//                            Intent intent = new Intent( context, ChargeActivity.class);
//                            context.startActivity(intent);
                            Toast.makeText( context, "球票不足", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
