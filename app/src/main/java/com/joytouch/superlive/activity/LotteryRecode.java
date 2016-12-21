package com.joytouch.superlive.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.LoginUtils;

/**
 * Created by Administrator on 2015/11/18.
 * 加载159h5界面的购彩记录
 */
public class LotteryRecode extends BaseActivity implements View.OnClickListener {
    private ImageView ib_back;
    private TextView tv_title;
    private WebView wb;
    private String  urls;
    private boolean isTop = false;
    final Activity activity = this;
    private String name159;
    private String pw159;
    private ImageView iv_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_recode);

        initView();
    }

    private void initView() {
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("彩金提现");

        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        ib_back=(ImageView)findViewById(R.id.iv_finish);
        ib_back.setOnClickListener(this);
        ib_back.setVisibility(View.VISIBLE);
        SharedPreferences sp = getSharedPreferences(Preference.preference, Context.MODE_PRIVATE);
        name159 = sp.getString(Preference.name159, "");
        pw159 = sp.getString(Preference.pwd159, "");
        urls = LoginUtils.url(name159, pw159, "http://cjzb.159cai.com/account/draw.html");//购彩记录的h5页面
        wb = (WebView) findViewById(R.id.lottery_recode_wb);
        //设置WebView属性，能够执行Javascript脚本
        wb.getSettings().setJavaScriptEnabled(true);
        wb.loadUrl(urls);
        //让网页在webview里面加载
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (wb.canGoBack()) {
                wb.goBack();//goBack()表示返回WebView的上一页面
                return true;
            } else {
//                System.exit(0);// 退出程序
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_finish:
                    finish();
                break;
        }
    }
    //让webview返回上一级的界面
    public void webBack() {
        if(wb.canGoBack()){
            wb.goBack();
        }
    }
}
