package com.joytouch.superlive.wxapi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.joytouch.superlive.app.SuperLiveApplication;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
/*
* 微信支付返回结果类 0支付成功
* */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		SuperLiveApplication.wxapi.handleIntent(getIntent(), this);
        
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);

	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.e("微信支付返回",resp.errCode+"/"+resp.getType());
		if (resp.errCode == 0) {
			finish();
		}else{
		}
		finish();
	}
}