package com.joytouch.superlive.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ChargeActivity;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;

/**
 * Created by yj on 2016/5/11.
 * 游戏dialog
 */
public class GameDialog extends BaseDialog {
    private WebView webView;
    private String url;
    private RelativeLayout bg;

    public GameDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_game);
        webView = (WebView) findViewById(R.id.game);
        bg = (RelativeLayout) findViewById(R.id.bg);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if ("m://back".equals(url)) {
                } else if ("m://iswrong".equals(url)) {
                    new LoginUtils(context).reLogin(context);
                } else if ("m://recharge".equals(url)) {
                    Intent intent = new Intent(context, ChargeActivity.class);
                    context.startActivity(intent);
                } else {
                    view.loadUrl(url);
                }

                return true;
            }

        });
        url =Preference.gameurl+ SPUtils.get(context, Preference.token,"",Preference.preference);
        LogUtils.e("------00000", (String) SPUtils.get(context, Preference.token,"",Preference.preference));
        webView.loadUrl(url);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    public  void refresh(){
        webView.loadUrl(url);
    }
}
