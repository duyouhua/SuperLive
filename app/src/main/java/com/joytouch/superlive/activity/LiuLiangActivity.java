package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.LoginUtils;

/**
 * 应用推荐
 */
public class LiuLiangActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_path;
    private WebView webView;
    private ProgressBar bar;
    private ImageView iv_finish;
    private TextView tv_title;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liuiang);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title.setText("兑换流量");
        iv_finish.setOnClickListener(this);

        webView=(WebView) findViewById(R.id.wv);
        webView.setVerticalScrollbarOverlay(true); // 指定的垂直滚动条有叠加样式
        WebSettings settings = webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true); //设置WebView的属性，此时可以去执行JavaScript脚本
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //自动打开窗口
        settings.setPluginState(PluginState.ON);//支持2.2以上所有版本
        settings.setLoadWithOverviewMode(true);//设置WebView 可以加载更多格式页面
//        settings.setDefaultTextEncodingName("UTF-8");//设置字符编码
//        webView.setScrollBarStyle(0);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        settings.setUseWideViewPort(true);//设置WebView使用广泛的视窗  设定支持viewport
        settings.setUserAgentString(null);  //设置WebView的用户代理字符串。如果字符串“ua”是null或空,它将使用系统默认的用户代理字符串
        settings.setBuiltInZoomControls(true);// 设置是否可缩放
        settings.setSupportZoom(true); //设置可以支持缩放  ,缩放开关
        settings.setAppCacheEnabled(true);//告诉webview启用应用程序缓存api。
        settings.setDomStorageEnabled(true);  //设置是否启用了DOM storage API。
        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR); //根据分辨率480宽度为基准缩放。
        settings.setBuiltInZoomControls(true);//设置出现缩放工具
        //设置加载进来的页面自适应手机屏幕,并且WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
        settings.setLoadWithOverviewMode(true);  //设置WebView 可以加载更多格式页面
        settings.setUseWideViewPort(true);    //无限缩放   设置此属性，可任意比例缩放。
        //WebView cookies清理
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        //清理cache 和历史记录的方法
        webView.clearCache(true);
        webView.clearHistory();


        /**
         * 设置网页加载动画
         */
        if (webView != null) {
            bar = (ProgressBar) findViewById(R.id.pb);
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    bar.setProgress(newProgress);
                    if (newProgress == 100) {
                        bar.setVisibility(View.GONE);
                    }
//                    else {
//                        if (View.INVISIBLE == bar.getVisibility()) {
//                            bar.setVisibility(View.VISIBLE);
//                        }
//                        bar.setProgress(newProgress);
//                    }
                    super.onProgressChanged(view, newProgress);
                }

            });
            //点击网页内容时可能出现的情况
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                	 互联网用：webView.loadUrl("http://www.google.com"); 
//                	 本地文件用：webView.loadUrl("file:///android_asset/XX.html"); 本地文件存放在：assets 文件中
                    // 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    if ("m://resign".equals(url)) {
                        new LoginUtils(LiuLiangActivity.this).reLogin(LiuLiangActivity.this);
                    } else if ("m://recharge".equals(url)) {
                        Intent intent = new Intent(LiuLiangActivity.this, ChargeActivity.class);
                        LiuLiangActivity.this.startActivity(intent);
                    } else {
                        view.loadUrl(url);
                    }
                    Log.e("ccccccccc",url);
                    return true;
                }
            });
            //加载网络地址传token
            String URL =Preference.liuliangurl+sp.getString(Preference.token,"");
//            String URL =Preference.liuliangurl;
            webView.loadUrl(URL);
        }
        //Android判断WebView是否已经滚动到页面底端: 
        //getScrollY()方法返回的是当前可见区域的顶端距整个页面顶端的距离,也就是当前内容滚动的距离.
        //getHeight()或者getBottom()方法都返回当前WebView 这个容器的高度
        //getContentHeight 返回的是整个html 的高度,但并不等同于当前整个页面的高度,因为WebView 有缩放功能, 所以当前整个页面的高度实际上应该是原始html 的高度再乘上缩放比例. 因此,更正后的结果,准确的判断方法应该是：
        if(webView.getContentHeight()*webView.getScale() == (webView.getHeight()+webView.getScrollY())){
            //Toast.makeText(this, "到底步", 1000).show();
        }
        //WebView默认没有开启文件下载的功能，如果要实现文件下载的功能，需要设置WebView的DownloadListener
        webView.setDownloadListener(new MyWebViewDownLoadListener());
    }
    //设置回退
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//goBack()表示返回WebView的上一页面  
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
        }
    }


    private class MyWebViewDownLoadListener implements DownloadListener{

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Log.i("tag", "url="+url);
            Log.i("tag", "userAgent="+userAgent);
            Log.i("tag", "contentDisposition="+contentDisposition);
            Log.i("tag", "mimetype="+mimetype);
            Log.i("tag", "contentLength="+contentLength);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

}





