package com.joytouch.superlive.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.LoginUtils;

//import com.joytouch.superlive.v3.interfaces.WebBack;

/**
 * Created by Administrator on 2015/11/17.
 * 加载159界面的购彩列表
 */
public class JincaiActivity extends Activity {
    private static WebView webView;
    static String  urls;
    private static String name159;
    private static String pw159;
    private static String token;
    static SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jincai_activity);
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.jincai_wb);
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        //浏览器的设置对象，使用这个对象设置浏览器
        WebSettings settings = webView.getSettings();
        //设置浏览器是否缓存数据，true 缓存数据，fasle不缓存数据
        settings.setAppCacheEnabled(false);

        sp = getSharedPreferences(Preference.preference, Context.MODE_PRIVATE);

        name159 = sp.getString(Preference.name159, "");
        pw159 = sp.getString(Preference.pwd159,"");
        token = sp.getString(Preference.token, "");
        LogUtils.e("sssss", "name159=" + name159);
        LogUtils.e("sssss","token="+token);
        LogUtils.e("sssss","pw159="+pw159);
        urls = LoginUtils.url(name159, pw159, "");
        webView.loadUrl(urls);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //判断159是否发出请求登陆，如果发出跳到登陆界面
                if ("oc://func".equals(url)) {
                    //判断用户是否登陆过

                    if (TextUtils.isEmpty(name159) || TextUtils.isEmpty(token) || TextUtils.isEmpty(pw159)) {
                        Intent intent = new Intent(JincaiActivity.this, LoadActivity.class);
                        intent.putExtra("159", "true");
                        startActivity(intent);
                    }
                } else {
                    LogUtils.e("--------------------", "=====url=====" + url);
                    view.loadUrl(url);
                }
                return true;
            }
        });
        //监听网页是否加载好
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    public void webBack() {
        if(webView.canGoBack()){
            webView.goBack();
        }
    }
    //广播室在webviewactivity发起的
    WebBackBroadcastReceiver webBackBroadcastReceiver;
    @Override
    protected void onResume() {
        super.onResume();
        webBackBroadcastReceiver = new WebBackBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("back");
        registerReceiver(webBackBroadcastReceiver, intentFilter);
        ConfigUtils.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(webBackBroadcastReceiver!=null){
            unregisterReceiver(webBackBroadcastReceiver);
        }
        ConfigUtils.removeActivtity(this);
    }

    public static   class WebBackBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //webBack();
            name159 = sp.getString(Preference.name159, "");
            pw159 = sp.getString(Preference.pwd159,"");
            token = sp.getString(Preference.token,"");
            urls = LoginUtils.url(name159,pw159,"");
            webView.loadUrl(urls);
        }
    }

}
