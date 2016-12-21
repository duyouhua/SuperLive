package com.joytouch.superlive.wxapi;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.WXhelpUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import java.io.IOException;

import okhttp3.FormBody;

/*
* 微信分享返回结果类0位成功
* */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        SuperLiveApplication.wxapi.handleIntent(getIntent(), this);
//        sp = this.getSharedPreferences(Preference.preference,
//                Context.MODE_PRIVATE);
    }


    //实现IWXAPIEventHandler接口，微信发送的请求将回调到onReq方法，发送到微信请求的响应结果将回调到onResp方法
    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
    }

    /*
        ERR_OK = 0(用户同意)
        ERR_AUTH_DENIED = -4（用户拒绝授权）
        ERR_USER_CANCEL = -2（用户取消）
     */

    @Override
    public void onResp(BaseResp arg0) {
        // TODO Auto-generated method stub
        if (WXhelpUtils.DEBUG) {
            Log.i(getClass().getName(), "resp");
            Log.e(getClass().getName(), "errCode=" + arg0.errCode + "");
        }
        if (arg0.errCode == 0) {

            try {
                SendAuth.Resp resp = (SendAuth.Resp) arg0;
                String code = resp.code;
                WXhelpUtils.wxMyCode = code;

            } catch (Exception e) {
                // TODO: handle exception

                FormBody.Builder build=new FormBody.Builder();
                build.add("token",  SPUtils.get(WXEntryActivity.this, Preference.token, "", Preference.preference)+"")
                        .add("phone", Preference.phone)
                        .add("version", Preference.version)
                        .build();
                new HttpRequestUtils(WXEntryActivity.this).httpPost(Preference.share, build,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                                Toast.makeText(WXEntryActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(String json) {
                                Log.e("任务分享", json);
                                Gson gson = new Gson();
                                BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                if (Bean.status.equals("_0000")) {
                                    if (Bean.is_share.equals("0")){
                                        final Dialog dialog1 =  new Dialog(WXEntryActivity.this, R.style.Dialog_bocop);
                                        dialog1.setContentView(R.layout.my_task_1);
                                        LinearLayout close = (LinearLayout) dialog1.findViewById(R.id.close);
                                        close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog1.dismiss();
                                            }
                                        });
                                        TextView getMoney = (TextView) dialog1.findViewById(R.id.getMoney);
                                        getMoney.setText("+"+Bean.getMoney());
                                        dialog1.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
                                        dialog1.setCancelable(false);
                                        dialog1.show();
                                    }
                                } else {
                                    Toast.makeText(WXEntryActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        }
        finish();
    }
}
