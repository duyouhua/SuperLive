package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.ChargeAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.getPay;
import com.joytouch.superlive.javabean.getpay_one;
import com.joytouch.superlive.javabean.wxbackinfi;
import com.joytouch.superlive.javabean.wxpay_iqnfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.widget.ShareBottomDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;

public class ChargeActivity extends BaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private GridView gridView;
    private int i;//记录用户上次选择的金额
    private LinearLayout root_lin;
    private BounceTopEnter bas_in;
    private LinearLayout ll_wxmoney;
    private String money;//支付金额;
    private PayReq req;
    private IWXAPI msgApi;
    private TextView money_detail;
    private getPay payBean;
    private TextView tv_total_money;
    private List<getpay_one> charges;
    private String okmoney;
    private SharedPreferences sp;
    private TextView tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_charge);
        bas_in = new BounceTopEnter();
        x.view().inject(this);
        initView();
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
        new HttpRequestUtils(this).httpPost(Preference.getPayData, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("获取重置数据",json);
                        Gson gson = new Gson();
                        payBean = gson.fromJson(json, getPay.class);
                        tv_total_money.setText(payBean.user_credit);
                        sp.edit().putString(Preference.balance,payBean.user_credit).commit();
                        charges = payBean.list;
                        gridView.setAdapter(new ChargeAdapter(charges,ChargeActivity.this));
                    }
                });
    }

    private void initView() {
        tv_right=(TextView)findViewById(R.id.tv_right);
        tv_right.setText("彩金提现");
        if ((sp.getString(Preference.name159,"").equals("")) || sp.getString(Preference.pwd159,"").equals("")){
            tv_right.setVisibility(View.GONE);
        }else{
            tv_right.setVisibility(View.VISIBLE);
        }
        tv_total_money=(TextView)findViewById(R.id.tv_total_money);
        money_detail=(TextView)findViewById(R.id.money_detail);
        //天添加划线
        money_detail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        root_lin=(LinearLayout)this.findViewById(R.id.root_lin);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("充值");
        gridView = (GridView) this.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);
        money_detail.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        getData();
    }
    ShareBottomDialog dialog;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RelativeLayout rl_charge = (RelativeLayout) view.findViewById(R.id.rl_charge);
        rl_charge.setBackgroundResource(R.drawable.charge_shape_fill_theme_color);
        TextView tv_gold_num = (TextView) view.findViewById(R.id.tv_gold_num);
        tv_gold_num.setTextColor(getResources().getColor(R.color.white));
        TextView tv_money = (TextView) view.findViewById(R.id.tv_money);
        TextView mo_tu= (TextView) view.findViewById(R.id.mo_tu);
        mo_tu.setTextColor(getResources().getColor(R.color.white));
        okmoney = charges.get(position).money;
        tv_money.setTextColor(getResources().getColor(R.color.white));
        if (i!=position){
            gridView.getChildAt(i).findViewById(R.id.rl_charge).setBackgroundResource(R.drawable.charge_shape_theme_color);
            ((TextView) gridView.getChildAt(i).findViewById(R.id.tv_gold_num)).setTextColor(getResources().getColor(R.color.theme_color));
            ((TextView) gridView.getChildAt(i).findViewById(R.id.tv_money)).setTextColor(getResources().getColor(R.color.textcolor_1));
            ((TextView) gridView.getChildAt(i).findViewById(R.id.mo_tu)).setTextColor(getResources().getColor(R.color.textcolor_1));
        }
        i = position;
        dialog = new ShareBottomDialog(ChargeActivity.this, root_lin);
        dialog.showAnim(bas_in) .show();
        ll_wxmoney=(LinearLayout)dialog.findViewById(R.id.ll_wxmoney);
        money=tv_money.getText().toString().trim();
        ll_wxmoney.setOnClickListener(this);
    }
    wxpay_iqnfo wxinfo;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right:
                Intent intent=new Intent(ChargeActivity.this,LotteryRecode.class);
                startActivity(intent);
                break;

            case R.id.iv_finish:
                finish();
                break;
            case R.id.ll_wxmoney:
                req = new PayReq();
                msgApi = WXAPIFactory.createWXAPI(ChargeActivity.this,Preference.wx_appid, false);
                msgApi.registerApp(Preference.wx_appid);
                FormBody.Builder build=new FormBody.Builder();
                build.add("token", sp.getString(Preference.token,""))
                        .add("phone", Preference.phone)
                        .add("version", Preference.version)
                        .add("money", okmoney)
                        .add("editor", Preference.payType)
                        .add("type","")
                        .add("mode","")
                        .build();
                new HttpRequestUtils(this).httpPost(Preference.WxPay, build,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                            }
                            @Override
                            public void onSuccess(String json) {
                                Log.e("微信支付",json.toString());
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
                                dialog.dismiss();
                            }
                        });
                break;

            case R.id.money_detail:
                    toActivity(Jinbi_detailActivity.class);
                break;
        }
    }

    //调微信支付
    private void sendPayReq() {
        //注册app
//        msgApi.registerApp(Preference.wx_appid);
        //开启微信支付app
        msgApi.sendReq(req);
    }

}
