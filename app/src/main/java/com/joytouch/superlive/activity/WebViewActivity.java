package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.wxbackinfi;
import com.joytouch.superlive.javabean.wxpay_iqnfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.ShareBottomDialog;
import com.joytouch.superlive.widget.ShareDialog;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;
import okhttp3.FormBody;

/**
 * 竞猜帮助
 */
public class WebViewActivity extends BaseActivity implements OnClickListener{

	private WebView webView;
	private TextView tv_title;

	private String url="";
	private String title="";
	private SharedPreferences sp;
	private LinearLayout root_lin;
	private BounceTopEnter bas_in;
	private ShareBottomDialog dialog;
	private LinearLayout ll_wxmoney;
	private String money;
	private String modle;
	private IWXAPI msgApi;
	private PayReq req;
	private wxpay_iqnfo wxinfo;
	private CommonShowView loadstate;
	private String content;
	private boolean isbanner;
	private ImageButton share;
	private String shareurl;
	private ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_activity);
		sp = this.getSharedPreferences(Preference.preference,
				Context.MODE_PRIVATE);
		bas_in = new BounceTopEnter();
		shareurl =  getIntent().getStringExtra("url");
		if(TextUtils.isEmpty(shareurl)){
			shareurl = "";
		}
		url = getIntent().getStringExtra("url")+"?token="+sp.getString(Preference.token,"");
		title = getIntent().getStringExtra("title");
		content = getIntent().getStringExtra("content");
		isbanner = getIntent().getBooleanExtra("isbanner",false);
		if(TextUtils.isEmpty(content)){
			content = "";
		}
		root_lin=(LinearLayout)findViewById(R.id.root_lin);
//		loadstate=(CommonShowView)this.findViewById(R.id.loadstate_ing);
//		loadstate.setloading();
//		loadstate.starAnimal();
		bar = (ProgressBar) findViewById(R.id.pb);
		share = (ImageButton) findViewById(R.id.share);
		webView = (WebView) findViewById(R.id.webview);
		tv_title = (TextView) findViewById(R.id.tv_title);
		webView.getSettings().setUseWideViewPort(true);//设置WebView使用广泛的视窗  设定支持viewport
		webView.getSettings().setUserAgentString(null);  //设置WebView的用户代理字符串。如果字符串“ua”是null或空,它将使用系统默认的用户代理字符串
		webView.getSettings().setBuiltInZoomControls(true);// 设置是否可缩放
		webView.getSettings().setSupportZoom(true); //设置可以支持缩放  ,缩放开关
		webView.getSettings().setAppCacheEnabled(true);//告诉webview启用应用程序缓存api。
		webView.getSettings().setDomStorageEnabled(true);  //设置是否启用了DOM storage API。
		webView.getSettings().setLoadWithOverviewMode(true);  //设置WebView 可以加载更多格式页面
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setPluginState(WebSettings.PluginState.ON);
		webView.getSettings().setAllowFileAccess(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		//webView.addJavascriptInterface(new JsObject(), "zetq");
		tv_title.setText(title);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode,
										String description, String failingUrl) {
				webView.loadUrl("about:blank");
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler,
										   SslError error) {
				handler.proceed();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				//互联网用：webView.loadUrl("http://www.google.com");
				//本地文件用：webView.loadUrl("file:///android_asset/XX.html"); 本地文件存放在：assets 文件中
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				String[] strs = url.split(":");
				String[] moneys = strs[1].split("_");
				Log.e("split", strs[0]);
				if ("m://resign".equals(url)) {
					new LoginUtils(WebViewActivity.this).reLogin(WebViewActivity.this);
				} else if ("charge".equals(strs[0])) {
					dialog = new ShareBottomDialog(WebViewActivity.this, root_lin);
					dialog.showAnim(bas_in).show();
					ll_wxmoney = (LinearLayout) dialog.findViewById(R.id.ll_wxmoney);
					money = moneys[1];
					modle = moneys[2];
					ll_wxmoney.setOnClickListener(WebViewActivity.this);
				} else {
					view.loadUrl(url);
				}
				Log.e("ccccccccc", url);
				return true;
			}

		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				bar.setProgress(newProgress);
				if (newProgress == 100) {
					bar.setVisibility(View.GONE);
				}
//				else {
//					if (View.INVISIBLE == bar.getVisibility()) {
//						bar.setVisibility(View.VISIBLE);
//					}
//					bar.setProgress(newProgress);
//				}
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				// TODO Auto-generated method stub
				super.onReceivedTitle(view, title);

			}
		});

		webView.setDownloadListener(new MyWebViewDownLoadListener());
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		webView.loadUrl(url);
		share.setOnClickListener(this);
		if(isbanner){
			share.setVisibility(View.GONE);
		}else {
			share.setVisibility(View.GONE);
		}
	}

	Handler closeWebHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			finish();
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.ll_wxmoney:
				req = new PayReq();
				msgApi = WXAPIFactory.createWXAPI(WebViewActivity.this, Preference.wx_appid, false);
				msgApi.registerApp(Preference.wx_appid);
				FormBody.Builder build=new FormBody.Builder();
				build.add("token", sp.getString(Preference.token,""))
						.add("phone", Preference.phone)
						.add("version", Preference.version)
						.add("money", money)
						.add("editor", Preference.payType)
						.add("type","huodong")
						.add("mode",modle)
						.build();
				Log.e("weixin",money+"__"+modle);
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
			case R.id.share:
				ShareDialog dialog = new ShareDialog(WebViewActivity.this);
				dialog.setMatchname(title);
				dialog.setContetnt(content);
				dialog.setShareurl(shareurl);
				dialog.setIsBanner(isbanner);
				dialog.show();
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

	class JsObject {
		JsObject() {
		}

		@JavascriptInterface
		public void closeWeb() {
			closeWebHandler.sendEmptyMessage(1);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("帮助");
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
		webView.loadUrl(url);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("帮助");
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
//		webView.loadUrl("about:blank");
	}

	private class MyWebViewDownLoadListener implements DownloadListener{

		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
									long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}
}
