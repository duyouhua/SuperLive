package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.PayItemAdapter;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.getPay;
import com.joytouch.superlive.javabean.getpay_one;
import com.joytouch.superlive.javabean.wxbackinfi;
import com.joytouch.superlive.javabean.wxpay_iqnfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/8/16.
 */
public class PayDialog extends BaseDialog {
    private PayReq req;
    private IWXAPI msgApi;
    private wxpay_iqnfo wxinfo;
    private boolean isCancel;
    private int i;

    public PayDialog(Context context) {
        super(context);
    }
    private SharedPreferences sp;
    private TextView money;
    private GridView gv;
    private Button sure;
    private  Button cancel;
    private getPay payBean;
    private List<getpay_one> charges;
    private String mymoney;
    private String selectormoney = "";

    public String getMymoney() {
        return mymoney;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);

        initView();
    }

    private void initView() {
        money = (TextView) findViewById(R.id.money);
        sure = (Button) findViewById(R.id.sure);
        gv = (GridView) findViewById(R.id.gridView);
        cancel = (Button) findViewById(R.id.cancle);

        req = new PayReq();
        msgApi = WXAPIFactory.createWXAPI(context, Preference.wx_appid, false);
        msgApi.registerApp(Preference.wx_appid);
        getData();
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(selectormoney)){
                    Toast.makeText(context,"请选择充值金额！",Toast.LENGTH_SHORT).show();
                    return;
                }
                final SureDialog dialog = new SureDialog(context);
                dialog.setContents("确认前往充值，直播会暂时中断");
                dialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (dialog.isSure()) {
                            pay(selectormoney);
                        }
                    }
                });
                dialog.show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCancel = true;
                dismiss();
                //getData();
            }
        });
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                RelativeLayout rl_charge = (RelativeLayout) view.findViewById(R.id.rl_charge);
                rl_charge.setBackgroundResource(R.drawable.charge_shape_fill_theme_color);
                TextView tv_gold_num = (TextView) view.findViewById(R.id.tv_gold_num);
                tv_gold_num.setTextColor(context.getResources().getColor(R.color.white));
                TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
                TextView mo_tu= (TextView) view.findViewById(R.id.mo_tu);
                mo_tu.setTextColor(context.getResources().getColor(R.color.white));
                selectormoney = charges.get(position).money;
                tv_money.setTextColor(context.getResources().getColor(R.color.white));
                if (i!=position){
                    gv.getChildAt(i).findViewById(R.id.rl_charge).setBackgroundResource(R.drawable.charge_shape_theme_color);
                    ((TextView) gv.getChildAt(i).findViewById(R.id.tv_gold_num)).setTextColor(context.getResources().getColor(R.color.theme_color));
                    ((TextView) gv.getChildAt(i).findViewById(R.id.tv_money)).setTextColor(context.getResources().getColor(R.color.textcolor_1));
                    ((TextView) gv.getChildAt(i).findViewById(R.id.mo_tu)).setTextColor(context.getResources().getColor(R.color.textcolor_1));
                }
                i = position;
            }
        });
    }
    public void pay(String okmoney){
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("money", okmoney)
                .add("editor", Preference.payType)
                .add("type","")
                .add("mode","")
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.WxPay, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("微信支付", json.toString());
                        try {
                            JSONObject object1= new JSONObject(json);
                            String message=object1.getString("message");
                            String status=object1.getString("status");
                            JSONObject listJSON = object1.getJSONObject("list");
                            String partnerId = listJSON.getString("partnerId");
                            String nonceStr=listJSON.getString("nonceStr");
                            String appId=listJSON.getString("appId");
                            String package1=listJSON.getString("package");
                            String prepayId=listJSON.getString("prepayId");
                            String sign=listJSON.getString("sign");
                            String timeStamp=listJSON.getString("timeStamp");
                            wxbackinfi in=new wxbackinfi(appId,partnerId,package1,prepayId,nonceStr,timeStamp,sign);
                            wxinfo = new wxpay_iqnfo(in,message,status);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (wxinfo!=null){
                            req.appId = wxinfo.getList().getAppid();
                            req.partnerId = wxinfo.getList().getPartnerId();
                            req.prepayId = wxinfo.getList().getPrepayId();
                            req.packageValue = "Sign=WXPay";
                            req.nonceStr = wxinfo.getList().getNonceStr();
                            req.timeStamp = wxinfo.getList().getTimeStamp();
                            req.sign = wxinfo.getList().getSign();
                            Log.e("微信支付",json);
                            sendPayReq();
                        }
                    }
                });
    }

                    //调微信支付
                    private void sendPayReq() {
                        //注册app
//        msgApi.registerApp(Preference.wx_appid);
                        //开启微信支付app
                        msgApi.sendReq(req);
                        dismiss();
                    }
    /**
     * 获取可以充值的数据
     */
    private void getData() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.getPayData, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("获取重置数据", json);
                        Gson gson = new Gson();
                        payBean = gson.fromJson(json, getPay.class);
                        money.setText(payBean.user_credit);
                        sp.edit().putString(Preference.balance,payBean.user_credit).commit();
                        if(isCancel){
                            dismiss();
                        }else {
                            charges = payBean.list;
                            gv.setAdapter(new PayItemAdapter(charges,context));
                        }

                    }
                });
    }
}
