package com.joytouch.superlive.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.NetworkUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;
import master.flame.danmaku.ui.widget.DanmakuView;
import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by yj on 2016/4/25.
 * 视屏播放器
 */
public class  MediaPlayer implements PLMediaPlayer.OnCompletionListener, PLMediaPlayer.OnErrorListener, PLMediaPlayer.OnInfoListener, PLMediaPlayer.OnPreparedListener
                            , PLMediaPlayer.OnBufferingUpdateListener {
    private final boolean islive;
    private final DanmakuContext mcontext;
    private final BaseDanmakuParser mParser;
    private View palyer;
    private PLVideoView mVideoView;
    private ImageView logo;
    private TextView progress;
    private LinearLayout progressll;
    private ImageView bg;//视屏的遮挡背景
    private long mLastPosition;
    private LinearLayout wifi;//无WiFi的布局
    private Button wifiContinue;//无WiFi继续按钮
    private TextView wifitex;//WiFi的内容
    private boolean isFull;//是否全屏
    private TextView refresh;//播放出错显示的刷新按钮
    private CompletionListener completionListener;//视屏播放完成的监听
    private ErroListener erroListener;//视屏播放出错的监听
    private Context context;
    private String playerSource ;//得到视屏源的源
    private String sourceType;//得到视屏源的类型
    private String sourceJs = "a=[location.href];function f(b){return b?\"\"!=b.src?b.src:(b=document.getElementsByTagName(\"source\")[0])?b.getAttribute(\"src\"):\"\":\"\"}function g(b){for(var e=document.childNodes.length-1;0<=e;)document.removeChild(document.childNodes[e--]);injectedObject.playVideo(b)}function h(b,e){var c=document.getElementsByTagName(\"video\")[0],d=f(c);if(d==a[a.length-1]&&c.paused||location.href!=a[0])g(d);else{if(\"\"!=d&&(d!=a[a.length-1]&&(a.push(d),c.play()),1!=c.paused&&-1<c.readyState)){c.pause();try{c.dispatchEvent(new CustomEvent(\"ended\"))}catch(k){}}a.length>b||0>e?(g(d)):setTimeout(function(){h(b,e-(1<a.length?200:50))},200)}}h(3,1E4);";//得到视屏源的js
    private WebView webView;//用于auto类型去视屏源
    private boolean isAuto;//是否是auto类型的视屏
    private String mediaSource = "";//播放的视屏源
    private DanmakuView mDanmakuView;
    private boolean danmu;
    private String anget;
    private long totalTime;
    private String reg;
    private ImageView playerbg;

    public boolean isDanmu() {
        return danmu;
    }

    public void setDanmu(boolean danmu) {
        this.danmu = danmu;
        LogUtils.e("------------",""+mDanmakuView);
        if(danmu){
            mDanmakuView.show();
        }else {
            mDanmakuView.hide();

        }
    }
    public boolean isPlaying(){
        return mVideoView.isPlaying();
    }
    public void setPlayerTimeListener(PlayerTime playerTimeListener) {
        this.playerTimeListener = playerTimeListener;
    }

    private PlayerTime playerTimeListener;
    private Handler progressHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(playerTimeListener!=null){
                playerTimeListener.progressTime(generateTime(mVideoView.getCurrentPosition()),mVideoView.getCurrentPosition(),mVideoView.getDuration());
                if(mVideoView.getCurrentPosition() <= mVideoView.getDuration()) {
                    sendEmptyMessageDelayed(0, 1000-(mVideoView.getCurrentPosition()%1000));
                }
            }
        }
    };
    //将视屏时间转化string
    private static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }

    public void setErroListener(ErroListener erroListener) {
        this.erroListener = erroListener;
    }

    public void setCompletionListener(CompletionListener completionListener) {
        this.completionListener = completionListener;
    }

    public MediaPlayer(final View palyer,Context context, final boolean islive) {
        this.palyer = palyer;
        this.context = context;
        this.islive = islive;
        playerbg = (ImageView) palyer.findViewById(R.id.player_bg);
        mVideoView = (PLVideoView) palyer.findViewById(R.id.mvideoview);
        bg  = (ImageView) palyer.findViewById(R.id.player);
        logo = (ImageView) palyer.findViewById(R.id.logo);
        progress = (TextView) palyer.findViewById(R.id.excption_content);
        progressll = (LinearLayout) palyer.findViewById(R.id.logo_ll);
        wifi = (LinearLayout) palyer.findViewById(R.id.wifi);
        wifiContinue = (Button) palyer.findViewById(R.id.wifi_player);
        wifitex = (TextView) palyer.findViewById(R.id.wifitext);
        refresh = (TextView) palyer.findViewById(R.id.refresh);
        webView = (WebView) palyer.findViewById(R.id.webview);
        mDanmakuView = (DanmakuView) palyer.findViewById(R.id.danmu);
        refresh.setVisibility(View.GONE);
        progressll.setVisibility(View.GONE);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnPreparedListener(this);

        wifiContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifi.setVisibility(View.GONE);
                parseSource();
            }
        });
        progressll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (refresh.getVisibility() == View.VISIBLE) {
                    if (erroListener != null) {
                        progressll.setVisibility(View.GONE);
                        parseSource();
                        erroListener.onStart();
                    }
                }
            }
        });
        mVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        initWebView();
        //设置弹幕的初始化属性

        mcontext = DanmakuContext.create();
        // 设置弹幕的最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 9); // 滚动弹幕最大显示3行
// 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_LR, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_BOTTOM, true);

        mcontext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3) //设置描边样式
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f) //是否启用合并重复弹幕
                .setScaleTextSize(1.2f) //设置弹幕滚动速度系数,只对滚动弹幕有效
                .setMaximumLines(maxLinesPair) //设置最大显示行数
                .preventOverlapping(overlappingEnablePair); //设置防弹幕重叠，null为允许重叠

        mDanmakuView.showFPS(false);
        mDanmakuView.enableDanmakuDrawingCache(true);
        mParser = createParser(context.getResources().openRawResource(R.raw.comments));
        mDanmakuView.prepare(mParser, mcontext);
        mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
            @Override
            public void updateTimer(DanmakuTimer timer) {
            }

            @Override
            public void drawingFinished() {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {
//                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
            }

            @Override
            public void prepared() {
                mDanmakuView.start();
            }
        });
        mDanmakuView.hide();
    }
    //创建弹幕解析格式
    private BaseDanmakuParser createParser(InputStream stream) {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        try {
            loader.load(stream);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;

    }
    //添加弹幕
public  void  addDanmu(String content,boolean b){
    LogUtils.e("--------------", "" + isDanmu());
    if(!isDanmu()){
        return;
    }
    LogUtils.e("--------------","添加大幕");
    BaseDanmaku danmaku = mcontext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
    if (danmaku == null || mDanmakuView == null) {
        return;
    }
    int[] color = new int[]{R.color.main, R.color.white, R.color.color_yellow, R.color.color_blue, R.color.bg_red};
    Random random = new Random();
    danmaku.text = content;
    danmaku.padding = 5;
    danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
    danmaku.isLive = islive;
    danmaku.time = mDanmakuView.getCurrentTime() + 1200;
    danmaku.textSize = ConfigUtils.dip2px(context, 14);
    danmaku.textColor = b?context.getResources().getColor(R.color.color_yellow):context.getResources().getColor(R.color.white);
    danmaku.textShadowColor = Color.WHITE;
    mDanmakuView.addDanmaku(danmaku);

}

    /**
     * 初始化WebView，用于auto方式解析
     */
    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.addJavascriptInterface(new JsObject(), "injectedObject");
        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
        webView.setVisibility(View.INVISIBLE);
    }

    public void onResume() {
        if (mVideoView != null && mLastPosition != 0&&mVideoView.isPlaying()) {
                mVideoView.seekTo(mLastPosition);
                mVideoView.start();

        }
        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()&&islive&&mVideoView.isPlaying()) {
            mDanmakuView.resume();
        }
    }

    public void onStop() {
        if (mVideoView != null) {
            mLastPosition = mVideoView.getCurrentPosition();
            LogUtils.e("stop",mLastPosition+"");
            mVideoView.pause();
        }
        if (mDanmakuView != null && mDanmakuView.isPrepared()&&islive) {
            mDanmakuView.pause();
        }
    }
    public void onDestor(){
        mDanmakuView.stop();
    }
    public  void destory(){
        mVideoView.stopPlayback();
        if (mDanmakuView != null) {
            // dont forget release!
            mDanmakuView.release();
            mDanmakuView = null;
        }
    }
    public void player(String source,String type,String js,String agnet,String reg){
        if(!TextUtils.isEmpty(js)){
            sourceJs = js;
        }
        playerSource = source;
        if(TextUtils.isEmpty(type)){
            sourceType = "stream";
        }else {
            sourceType = type;
        }

        this.reg = reg;
        anget = agnet;
        if(!NetworkUtils.isWifiConnected(context)){
            wifi.setVisibility(View.VISIBLE);
            progressll.setVisibility(View.GONE);
        }else {
            parseSource();
        }
    }
    public void playerMedia(String source){
        if (null != source && !"".endsWith(source)) {
            webView.loadUrl("about:blank");
            Uri uriPath = Uri.parse(source);
            if (null != uriPath) {
                String scheme = uriPath.getScheme();
                if (null != scheme) {
                    mediaSource = uriPath.toString();
                } else {
                    mediaSource = uriPath.getPath();
                }
            }
        }else{
            mediaSource = "";
        }
        LogUtils.e("source1", mediaSource);
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0); // 设置解码方式1是硬解码，0是软解码
        options.setInteger(AVOptions.KEY_BUFFER_TIME, 1000);
        options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000); // 设置超时时间
        options.setString(AVOptions.KEY_FFLAGS, AVOptions.VALUE_FFLAGS_NOBUFFER); // "nobuffer"
//        if(isRtmp(mediaSource)) {
//            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);//是否为在线直播
//        }else {
//            options.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
//        }
        if(islive) {
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);//是否为在线直播
        }else{
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);//是否为在线直播
        }
        mVideoView.setAVOptions(options);
        Log.e("mediaSource",mediaSource);
        mVideoView.setVideoPath(mediaSource);
        mVideoView.start();
        bg.setBackgroundResource(R.color.transparent_background);
        playerbg.setVisibility(View.GONE);
        progressll.setVisibility(View.GONE);
    }
    private boolean isRtmp(String source){
        if (source.startsWith("rtmp://")){
            return true;
        }
        return false;
    }
    //开启动画
    private void startAnimation(){
        RotateAnimation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        an.setInterpolator(new LinearInterpolator());//不停顿
        an.setRepeatCount(-1);//重复次数
        an.setFillAfter(true);//停在最后
        an.setDuration(600);
        logo.startAnimation(an);
    }
    //关闭动画
    private void endAnimation(){
        logo.clearAnimation();
    }


    @Override
    public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int i) {
        LogUtils.e("mediaplayer***************", "onBufferingUpdate");
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int errorCode) {

        LogUtils.e("mediaplayer***************", "onError");
            endAnimation();
            progressll.setVisibility(View.VISIBLE);
            progress.setText("视频暂不能播放，尝试");
            refresh.setVisibility(View.VISIBLE);
        bg.setBackgroundResource(R.color.transparent_background);
        playerbg.setVisibility(View.VISIBLE);
            if(erroListener!=null){
                erroListener.onErro();
            }
        return true;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
        LogUtils.e("mediaplayer***************", "onInfo");
        refresh.setVisibility(View.GONE);
        switch (what) {
            case IMediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲
                progressll.setVisibility(View.VISIBLE);
                startAnimation();
                progress.setText("视频加载中....");
                break;
            case IMediaPlayer.MEDIA_INFO_BUFFERING_END://停止缓冲
                progressll.setVisibility(View.GONE);
                endAnimation();
                break;
        }
        return true;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer) {
        //当播放器准备就绪后，会回调该接口\
        //判断非直播下获取视屏时长和进度
        LogUtils.e("mediaplayer***************", "onPrepared");
        if(!islive) {
            if (playerTimeListener != null) {
                playerTimeListener.playerTime(generateTime(mVideoView.getDuration()));
            }
            progressHandle.sendEmptyMessageDelayed(0, 1000);
            totalTime = mVideoView.getDuration();
        }
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        LogUtils.e("mediaplayer***************", "onCompletion");
             /*
        * 播放结束后，会回调该接口，有以下注意事项：

        如果是播放文件，则是播放到文件结束后产生回调
        如果是在线直播，则会在获取数据超时后产生回调，回调前会先播放完已缓冲的数据
        如果播放过程中产生onError，并且没有处理的话，最后也会回调本接口
        如果播放前设置了 setLooping(true)，则播放结束后会自动重新开始，不会回调本接口
        * */
        if(completionListener!=null){
            completionListener.completion();
        }
        bg.setBackgroundResource(R.color.transparent_background);
        playerbg.setVisibility(View.VISIBLE);
    }

    public interface ErroListener{
        void onErro();
        void onStart();
    }
    public interface  CompletionListener{
        void completion();
    }
    /**
     * auto方式解析后，靠Handler 播放视频
     */
    Handler playHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            playerMedia((String) msg.obj);

        }
    };

    /**
     * WebView网页 通过js调用客户端方法播放视频 ，参数由js传入
     */
    class JsObject {
        JsObject() {
        }

        @JavascriptInterface
        public void playVideo(String paramString) {

            if (isAuto && null != paramString && !"".equals(paramString)) {

                Message message = new Message();
                message.obj = paramString;
                playHandler.sendMessage(message);
            }

        }

    }

    class CustomWebViewClient extends WebViewClient {
        CustomWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView paramWebView,
                                                String paramString) {

            if (!paramString.equalsIgnoreCase("about:blank")) {
                webView.loadUrl(paramString);
            }
            return true;
        }
    }

    class CustomWebChromeClient extends WebChromeClient {
        private boolean isNewPage = true;

        CustomWebChromeClient() {
        }

        CustomWebChromeClient(String js) {

        }

        @Override
        public boolean onJsAlert(WebView paramWebView, String paramString1,
                                 String paramString2, JsResult paramJsResult) {
            paramJsResult.cancel();
            return true;
        }

        @Override
        public void onProgressChanged(WebView paramWebView, int paramInt) {
            if (isNewPage && paramInt >= 100
                    && !"about:blank".equals(webView.getUrl())) {
                isNewPage = false;
                webView.loadUrl("javascript:" + sourceJs);
            }

            if (paramInt == 100) {
                isNewPage = true;
            }

            super.onProgressChanged(paramWebView, paramInt);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage cm) {
            return true;
        }
    }
    public interface PlayerTime{
        void  playerTime(String time);
        void  progressTime(String time,long currentPosition,long totalPosition);
    }
    public void seelTo(int progress){
        if (totalTime != 0){
            mVideoView.seekTo((long)(((float)progress/1000)*totalTime));
        }

    }

    /**
     * 绘制背景(自定义弹幕样式)
     */
    private static class BackgroundCacheStuffer extends SpannedCacheStuffer {
        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
        final Paint paint = new Paint();

        @Override
        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
            danmaku.padding = 10;  // 在背景绘制模式下增加padding
            super.measure(danmaku, paint, fromWorkerThread);
        }

        @Override
        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
            paint.setColor(0x8125309b);
            canvas.drawRect(left + 2, top + 2, left + danmaku.paintWidth - 2, top + danmaku.paintHeight - 2, paint);
        }

        @Override
        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
            // 禁用描边绘制
        }
    }
    public void parseSource(){
        // 解析source
        if ("auto".equals(sourceType)) {
            isAuto = true;

            String userAgent = anget;
            if (!"".equals(anget)) {
                userAgent = anget;
            }
            webView.getSettings().setUserAgentString(userAgent);

            String js = sourceJs;
            if (!"".equals(sourceJs)) {
                js = sourceJs;
            }
            LogUtils.e("js", js.toString());
            webView.setWebChromeClient(new CustomWebChromeClient(js));
            webView.addJavascriptInterface(new JsObject(), "injectedObject");
            webView.loadUrl(playerSource);
        } else {
            isAuto = false;
        }
        if ("stream".equals(sourceType)) {
            playerMedia(playerSource);
        }
        if ("link".equals(sourceType)) {
            Uri uri = Uri.parse(playerSource);
            Log.e("link的值url",playerSource);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            context.startActivity(intent);
        }
        if ("match".equals(sourceType)) {
            matchtSoucre();
        }
    }
    public void matchtSoucre(){
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils((Activity) context);
        httpRequestUtils.httpGet(playerSource, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Pattern p = Pattern.compile(reg);
                Matcher matcher = p.matcher(json);
                if (matcher.find()) {
                    playerMedia(matcher.group(1));
                }
            }
        });
    }
}
