package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.ReviewCommentAdapter;
import com.joytouch.superlive.adapter.ReviewPlayjijinAdapter;
import com.joytouch.superlive.adapter.ReviewTimeAdapter;
import com.joytouch.superlive.adapter.ViewHolder;
import com.joytouch.superlive.app.MediaPlayer;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.CommentsBase;
import com.joytouch.superlive.javabean.ReviewComment;
import com.joytouch.superlive.javabean.ReviewDetailBase;
import com.joytouch.superlive.javabean.ReviewPlayJijin;
import com.joytouch.superlive.javabean.ReviewTime;
import com.joytouch.superlive.javabean.SendCommend;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.KeyboardUtil;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.MyListView;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.ReviewShareDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pili.pldroid.player.AVOptions;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 回顾的二级界面
 * 播放回放
 */
public class ReviewPlayActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,ViewHolder.ItemClickListener{
    private RelativeLayout rl_no;//无视频的时候显示比分容器
    private ImageView iv_finish_no;//无视频时候的返回按钮
    private ImageView iv_share_no;//无视频情况的分享按钮
    private CircleImageView iv_home_team_no;//无视频主队头像
    private CircleImageView iv_visiting_team_no;//无视频客队头像
    private TextView tv_home_name_no;//无视频主队队名
    private TextView tv_visiting_name_no;//无视频客队队名
    private TextView tv_score_no;//无视频 比分
    private TextView tv_time_no;//无视频 时间
    private MediaPlayer mediaPlayer;//播放器控制器
    private ImageView player;//点击隐藏进度条等
    AVOptions options = new AVOptions();//播放器设置
    private RecyclerView recyclerView;//横向的视频
    private ReviewPlayjijinAdapter playjijinAdapter;//集锦Adapter
    private List<ReviewPlayJijin> jijins = new ArrayList<>();
    private List<ReviewComment> comments = new ArrayList<>();
    private List<ReviewTime> times;
    private MyListView myListView;//评论列表
    private ReviewCommentAdapter commentAdapter;
    private WebView webView;
    private EditText et_send;//评论内容
    private ImageView iv_finish;
    private ImageView iv_share;//分享
    private MyListView timeListView;//时间轴
    private PullToRefreshLayout refresh;//刷新控件
    private RelativeLayout rl_play;//有视频的时候显示的播放器布局容器
    private long mDuration;//播放时长？
    private TextView tv_name_full;//全屏模式下的视频名
    private ImageView iv_play_small;//小屏情况下的暂停按钮
    private ImageView iv_lock;//锁按钮
    private TextView tv_current_time;//当前时间
    private TextView tv_total_time;//总时间
    private SeekBar seekBar;//进度条
    private ImageView iv_fillscreen;//最大化
    private ImageView iv_play;//暂停按钮
    private RelativeLayout rl_top;//标题和返回的布局
    private LinearLayout ll_bottom;//进度条布局
    private boolean isfillScreen = false;
    private int screenHeight;
    private int screenWidth;
    private int nomalHeight;
    private int nomalWidth;
    private LinearLayout ll_score;//有视频的比分布局
    private TextView tv_title;//标题
    private TextView tv_time;//比赛时间
    private TextView tv_home_name;//主队名称
    private CircleImageView iv_home_team;//主队logo
    private TextView tv_visiting_name;//客队名称
    private CircleImageView iv_visiting_team;//客队logo
    private TextView tv_score;//比分
    private String match_id;//赛事ID
    private String match_name;//比赛名称
    private String user_id;
    private String username;//
    private int page = 1;
    private boolean isLock;//是否锁住传感器
    private boolean isPlaying;//记录视屏是否在播放
    private static final int SHOW_PROGRESS = 2;
    private ReviewDetailBase detailBase;//视频列表类
    private int currentType = 0;//记录当前播放的视频源是那一个
    private TextView tv_shipin;//视频二字
    private TextView tv_name_no;//无视频的名字
    //隐藏view
    private Handler hideHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            hideView();
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_play);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        user_id = (String) SPUtils.get(this, Preference.myuser_id, "", Preference.preference);
        username = (String) SPUtils.get(this,Preference.username,"",Preference.preference);
        Intent intent = getIntent();
        if (intent != null){
            match_id = intent.getStringExtra("match_id");
        }
        initView();
        initSensor();
    }

    @Override
    protected void onStart() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onStart();
    }

    private void initView() {
        rl_no = (RelativeLayout) this.findViewById(R.id.rl_no);
        iv_finish_no = (ImageView) this.findViewById(R.id.iv_finish_no);
        iv_finish_no.setOnClickListener(this);
        iv_share_no = (ImageView) this.findViewById(R.id.iv_share_no);
        iv_share_no.setOnClickListener(this);
        iv_home_team_no = (CircleImageView) this.findViewById(R.id.iv_home_team_no);
        iv_visiting_team_no = (CircleImageView) this.findViewById(R.id.iv_visiting_team_no);
        tv_home_name_no = (TextView) this.findViewById(R.id.tv_home_name_no);
        tv_visiting_name_no = (TextView) this.findViewById(R.id.tv_visiting_name_no);
        tv_score_no = (TextView) this.findViewById(R.id.tv_score_no);
        tv_time_no = (TextView) this.findViewById(R.id.tv_time_no);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        iv_share = (ImageView) this.findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        tv_shipin = (TextView) this.findViewById(R.id.tv_shipin);
        tv_name_no = (TextView) this.findViewById(R.id.tv_name_no);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        ll_score = (LinearLayout) this.findViewById(R.id.ll_score);
        tv_time = (TextView) this.findViewById(R.id.tv_time);
        tv_score = (TextView) this.findViewById(R.id.tv_score);
        tv_home_name = (TextView) this.findViewById(R.id.tv_home_name);
        iv_home_team = (CircleImageView) this.findViewById(R.id.iv_home_team);
        tv_visiting_name = (TextView) this.findViewById(R.id.tv_visiting_name);
        iv_visiting_team = (CircleImageView) this.findViewById(R.id.iv_visiting_team);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        webView = (WebView) this.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        recyclerView.setLayoutManager(llm);
        playjijinAdapter = new ReviewPlayjijinAdapter(jijins);
        playjijinAdapter.setListener(this);
        recyclerView.setAdapter(playjijinAdapter);
        myListView = (MyListView) this.findViewById(R.id.listView);
        et_send = (EditText) this.findViewById(R.id.et_input);
        et_send.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //软键盘右下角的发送键，点击发评论
                if (et_send.getText().toString().equals("")) {
                    return true;
                }
                if (username.equals("")) {
                    showToast("登录失效，请重新登录");
                    return true;
                }
                sendComment(et_send.getText().toString());
                return true;
            }
        });
        refresh = (PullToRefreshLayout) this.findViewById(R.id.refreshlayout);
        refresh.setCanPullDown(false);
        refresh.setOnRefreshListener(this);
        commentAdapter =new ReviewCommentAdapter(comments, this);
        myListView.setAdapter(commentAdapter);
        timeListView = (MyListView) this.findViewById(R.id.timelistview);
        timeListView.setAdapter(new ReviewTimeAdapter(times, this));
        tv_name_full = (TextView) this.findViewById(R.id.tv_name_full);
        iv_play_small = (ImageView) this.findViewById(R.id.iv_play_small);
        iv_play_small.setOnClickListener(this);
        iv_lock = (ImageView) this.findViewById(R.id.iv_lock);
        iv_lock.setOnClickListener(this);
        tv_current_time = (TextView) this.findViewById(R.id.tv_current_time);
        tv_total_time = (TextView) this.findViewById(R.id.tv_total_time);
        seekBar = (SeekBar) this.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(onseekbarChangeListener);
        iv_fillscreen = (ImageView) this.findViewById(R.id.iv_fillscreen);
        iv_fillscreen.setOnClickListener(this);
        iv_play = (ImageView) this.findViewById(R.id.iv_play);
        iv_play.setOnClickListener(this);
        rl_top = (RelativeLayout) this.findViewById(R.id.rl_top);
        ll_bottom = (LinearLayout) this.findViewById(R.id.ll_bottom);
        rl_play = (RelativeLayout) this.findViewById(R.id.rl_play);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_play.getLayoutParams();
        layoutParams.height = screenWidth * 9 / 16;
        rl_play.setLayoutParams(layoutParams);
        mediaPlayer = new MediaPlayer(rl_play,this,false);
        setMediaPlayer();
        player = (ImageView) this.findViewById(R.id.player);
        player.setOnClickListener(this);
        hideHandler.sendEmptyMessageDelayed(View.GONE, 5000);
        getDate();//视频列表和比分等
        getComments(match_id);//评论列表
    }
    //对播放器设置监听器
    private void setMediaPlayer(){
        mediaPlayer.setCompletionListener(new MediaPlayer.CompletionListener() {
            @Override
            public void completion() {
                isPlaying = false;
                iv_play.setBackgroundResource(R.drawable.review_stop);
                iv_play_small.setBackgroundResource(R.drawable.stop);
                playNext();
            }
        });
        mediaPlayer.setErroListener(new MediaPlayer.ErroListener() {
            @Override
            public void onErro() {

            }

            @Override
            public void onStart() {

            }
        });
        mediaPlayer.setPlayerTimeListener(new MediaPlayer.PlayerTime() {
            @Override
            public void playerTime(String time) {
                tv_total_time.setText(time);
            }

            @Override
            public void progressTime(String time, long currentPosition, long totalPosition) {
                tv_current_time.setText(time);
                if (currentPosition<totalPosition){
                    int current = (int)((new Long(currentPosition).floatValue())/(new Long(totalPosition).floatValue())*1000);
                    seekBar.setProgress(current);
                }

            }

        });
    }
    //自动播放下一个视频方法
    private void playNext() {
        if (detailBase != null&&mediaPlayer!= null){
            currentType++;
            if (currentType<detailBase.getList().getMatch_review().size()){
                mediaPlayer.onStop();
                isPlaying = false;
                mediaPlayer.player(jijins.get(currentType).getLive_url(), jijins.get(currentType).getUrl_type(), jijins.get(currentType).getUrl_js(), "","");
                Log.e("playnext", " " + currentType);
                isPlaying = true;
                iv_play.setBackgroundResource(R.drawable.review_stop);
                iv_play_small.setBackgroundResource(R.drawable.stop);
            }
        }

    }
    //获取集锦列表数据
    private void getDate() {
        Log.e("match_ids", match_id);
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", "1")
                .add("version", "1")
                .add("match_id", match_id)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.ReviewMv, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
            }

            @Override
            public void onSuccess(String json) {
                Log.e("回顾详情", json);
                LogUtils.e("reviewPlay", json);
                Gson gson = new Gson();
                Type type = new TypeToken<ReviewDetailBase>() {
                }.getType();
                detailBase = gson.fromJson(json, type);
                if (detailBase.getStatus().equals("_0000")) {
                    if (null != detailBase.getList().getMatch_review() && detailBase.getList().getMatch_review().size() > 0) {
                        setDate(detailBase);
                    } else {
                        setNoVideoDate(detailBase);
                    }
                } else {
                    showToast("请求失败");
                }
            }
        });
    }
    //将获得的数据显示到界面(有视频)
    public void setDate(ReviewDetailBase detailBase){
        rl_play.setVisibility(View.VISIBLE);
        ll_score.setVisibility(View.VISIBLE);
        rl_no.setVisibility(View.GONE);
        tv_shipin.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        tv_name_no.setVisibility(View.GONE);
        if (null!= detailBase.getList().getMatch_team()){
            if (detailBase.getList().getMatch_team().size()>1){
                tv_home_name.setText(detailBase.getList().getMatch_team().get(0).getTeam());
                tv_visiting_name.setText(detailBase.getList().getMatch_team().get(1).getTeam());
                ImageLoader.getInstance().displayImage(Preference.photourl + "40x40/" + detailBase.getList().getMatch_team().get(0).getLogo(), iv_home_team, ImageLoaderOption.optionsteamLogo);
                ImageLoader.getInstance().displayImage(Preference.photourl+"40x40/"+detailBase.getList().getMatch_team().get(1).getLogo(),iv_visiting_team, ImageLoaderOption.optionsteamLogo);
            }
        }
        if (null!= detailBase.getList().getMatch()){
            match_name = detailBase.getList().getMatch().getMatch_name();
            //比分
            if (null!= detailBase.getList().getMatch().getResult()){
                String[] arry = detailBase.getList().getMatch().getResult().split(",");
                if (arry.length>1){
                    tv_score.setText(arry[0]+" — "+arry[1]);
                }else {
                    tv_score.setText("");
                }
            }else {
                tv_score.setText("");
            }

            tv_name_full.setText(detailBase.getList().getMatch().getMatch_name());
            tv_title.setText(detailBase.getList().getMatch().getMatch_name());
            tv_time.setText(detailBase.getList().getMatch().getStarttime());
        }

        jijins.addAll(detailBase.getList().getMatch_review());
        playjijinAdapter.notifyDataSetChanged();
        webView.loadUrl(detailBase.getList().getH5_url());
        if (mediaPlayer != null&&jijins.size()!= 0){
            Log.e("live_url", jijins.get(0).getLive_url());
            mediaPlayer.player(jijins.get(0).getLive_url(), jijins.get(0).getUrl_type(), jijins.get(0).getUrl_js(), "","");
            isPlaying = true;
            iv_play.setBackgroundResource(R.drawable.review_stop);
            iv_play_small.setBackgroundResource(R.drawable.stop);
        }
    }
    //将后台拉取的数据显示到界面（无视屏情况下）
    public void setNoVideoDate(ReviewDetailBase detailBase){
        rl_play.setVisibility(View.GONE);
        ll_score.setVisibility(View.GONE);
        rl_no.setVisibility(View.VISIBLE);
        tv_shipin.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tv_name_no.setVisibility(View.VISIBLE);
        if (null!=detailBase.getList().getMatch()){
            tv_time_no.setText(detailBase.getList().getMatch().getStarttime());
            match_name = detailBase.getList().getMatch().getMatch_name();
            if (null!=detailBase.getList().getMatch().getResult()){
                String[] arry = detailBase.getList().getMatch().getResult().split(",");
                if (arry.length>1){
                    tv_score_no.setText(arry[0]+" — "+arry[1]);
                }else {
                    tv_score_no.setText("");
                }
            }
        }
        if (null!=detailBase.getList().getMatch()){
            tv_name_no.setText(detailBase.getList().getMatch().getMatch_name());
        }
        if (null!= detailBase.getList().getMatch_team()&&detailBase.getList().getMatch_team().size()>1){
            tv_home_name_no.setText(detailBase.getList().getMatch_team().get(0).getTeam());
            tv_visiting_name_no.setText(detailBase.getList().getMatch_team().get(1).getTeam());
            ImageLoader.getInstance().displayImage(Preference.photourl + "40x40/" + detailBase.getList().getMatch_team().get(0).getLogo(), iv_home_team_no, ImageLoaderOption.optionsteamLogo);
            ImageLoader.getInstance().displayImage(Preference.photourl+"40x40/"+detailBase.getList().getMatch_team().get(1).getLogo(),iv_visiting_team_no, ImageLoaderOption.optionsteamLogo);
        }
        webView.loadUrl(detailBase.getList().getH5_url());
    }
    //获取评论列表
    public void getComments(String match_id){
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", Preference.phone)
                .add("version",Preference.version)
                .add("match_id",match_id)
                .add("page", "0")
                .build();
        new HttpRequestUtils(this).httpPost(Preference.review_conmments, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                refresh.refreshFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void onSuccess(String json) {
                Log.e("comments", json);
                refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                page = 1;
                Gson gson = new Gson();
                Type type = new TypeToken<CommentsBase>() {
                }.getType();
                CommentsBase commentsBase = gson.fromJson(json, type);
                if (commentsBase.getComment().size() > 0) {
                    comments.clear();
                    comments.addAll(commentsBase.getComment());
                    commentAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    //发送评论功能
    public void sendComment(String content){
        Log.e("sendComment", match_id + "  " + username + "  " + match_name);
        Log.e("sendComment", content);
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", Preference.phone)
                .add("version",Preference.version)
                .add("token", (String) SPUtils.get(ReviewPlayActivity.this,"token","",Preference.preference))
                .add("match_id", match_id)
                .add("content", content)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.review_sendcomment, build,new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("sendComment",json);
                Type type = new TypeToken<SendCommend>(){}.getType();
                Gson gson = new Gson();
                SendCommend sendCommend =  gson.fromJson(json,type);
                if ("_0000".equals(sendCommend.getStatus())){
                    et_send.setText("");
                    KeyboardUtil.closeKeybord(et_send, ReviewPlayActivity.this);
                    getComments(match_id);
                }else {
                    showToast(sendCommend.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish_no:
                if (isfillScreen){
                    smallScreen();
                }else {
                    mediaPlayer.onStop();
                    finish();
                }
                break;
            case R.id.iv_finish:
                if (isfillScreen){
                    smallScreen();
                }else {
                    mediaPlayer.onStop();
                    finish();
                }
                break;
            case R.id.iv_share:
                ReviewShareDialog shareDialog = new ReviewShareDialog(ReviewPlayActivity.this);
                shareDialog.show();
                break;
            case R.id.iv_share_no:
                ReviewShareDialog shareDialog1 = new ReviewShareDialog(ReviewPlayActivity.this);
                shareDialog1.show();
                break;
            case R.id.iv_fillscreen:
                fillScreen();
                break;
            case R.id.iv_play:
                if (isPlaying){
                    mediaPlayer.onStop();
                    isPlaying = false;
                    iv_play.setBackgroundResource(R.drawable.playing);
                    iv_play_small.setBackgroundResource(R.drawable.start);
                    displayView();
                }else {
                    mediaPlayer.onResume();
                    isPlaying = true;
                    iv_play.setBackgroundResource(R.drawable.review_stop);
                    iv_play_small.setBackgroundResource(R.drawable.stop);
                    hideHandler.removeMessages(View.GONE);
                    hideHandler.sendEmptyMessageDelayed(View.GONE, 5000);
                }
                break;
            case R.id.player:
                if (rl_top.isShown()){
                    hideView();
                }else {
                    displayView();
                    hideHandler.removeMessages(View.GONE);
                    hideHandler.sendEmptyMessageDelayed(View.GONE, 5000);
                }
                break;
            case R.id.iv_play_small:
                if (isPlaying){
                    mediaPlayer.onStop();
                    isPlaying = false;
                    iv_play_small.setBackgroundResource(R.drawable.start);
                    iv_play.setBackgroundResource(R.drawable.playing);
                    displayView();
                }else {
                    mediaPlayer.onResume();
                    isPlaying = true;
                    iv_play_small.setBackgroundResource(R.drawable.stop);
                    iv_play.setBackgroundResource(R.drawable.review_stop);
                    hideHandler.removeMessages(View.GONE);
                    hideHandler.sendEmptyMessageDelayed(View.GONE, 5000);
                }
                break;
            case R.id.iv_lock:
                if (isLock){
                    sensorManager.unregisterListener(listener);
                    isLock = false;
                }else {
                    sensorManager.registerListener(listener,sensor,
                            SensorManager.SENSOR_DELAY_FASTEST);
                    isLock = true;
                }
                break;
        }
    }
    //返回正常屏幕播放
    private void smallScreen(){
        isfillScreen = false;
        iv_lock.setVisibility(View.GONE);
        tv_name_full.setVisibility(View.GONE);
        tv_title.setVisibility(View.VISIBLE);
        iv_play.setVisibility(View.GONE);
        iv_fillscreen.setVisibility(View.VISIBLE);
        iv_play_small.setVisibility(View.VISIBLE);
        //显示通知栏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置屏幕竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置视屏的宽高
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_play.getLayoutParams();
        layoutParams.width = screenWidth;
        layoutParams.height = nomalHeight;
        rl_play.setLayoutParams(layoutParams);
    }
    //最大化播放
    private void fillScreen() {
        isfillScreen = true;
        iv_lock.setVisibility(View.GONE);
        tv_name_full.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.GONE);
        iv_fillscreen.setVisibility(View.GONE);
        iv_play.setVisibility(View.VISIBLE);
        iv_play_small.setVisibility(View.GONE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_play.getLayoutParams();
        nomalWidth = layoutParams.width;
        nomalHeight = layoutParams.height;
        layoutParams.height = screenWidth;
        layoutParams.width = screenHeight;
        rl_play.setLayoutParams(layoutParams);
        //去掉通知栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager.registerListener(listener, sensor,
                SensorManager.SENSOR_DELAY_FASTEST);
        isLock = false;
        hideHandler.sendEmptyMessageDelayed(View.GONE,5000);
    }
    /**
     * 初始化传感器，横竖屏判定
     * 用于横屏手机切换方向
     */
    // 方向传感器
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastTime = 0;
    private float lastX = 0;
    private SensorEventListener listener;
    private void initSensor() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        listener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                // TODO Auto-generated method stub

                long time = System.currentTimeMillis();
                if (isfillScreen && time - lastTime > 1000) {
                    float x = event.values[0];
                    if (lastX >= 0 && x < -2) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        lastX = x;
                    } else if (lastX <= 0 && x > 2) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        lastX = x;
                    }
                    lastTime = time;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // TODO Auto-generated method stub
            }
        };

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", "1")
                .add("version", "1")
                .add("match_id",match_id)
                .add("page",String.valueOf(page))
                .build();
        new HttpRequestUtils(this).httpPost(Preference.review_conmments, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void onSuccess(String json) {
                refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                Gson gson = new Gson();
                Type type = new TypeToken<CommentsBase>() {}.getType();
                CommentsBase commentsBase = gson.fromJson(json, type);
                if (commentsBase.getComment().size() > 0) {
                    page++;
                    comments.addAll(commentsBase.getComment());
                    commentAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null){
            mediaPlayer.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer!= null){
            mediaPlayer.onStop();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null){
            mediaPlayer.destory();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (isfillScreen){
                smallScreen();
            }else {
                mediaPlayer.onStop();
                isPlaying = false;
                finish();
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
    private SeekBar.OnSeekBarChangeListener onseekbarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                mediaPlayer.seelTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
    //隐藏布局
    public void hideView(){
        rl_top.setVisibility(View.GONE);
        ll_bottom.setVisibility(View.GONE);
        iv_play_small.setVisibility(View.GONE);
    }
    public void displayView(){
        rl_top.setVisibility(View.VISIBLE);
        ll_bottom.setVisibility(View.VISIBLE);
        if (!isfillScreen){
            iv_play_small.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void itemClick(View view, int position) {
        Log.e("itemClick", position + "");
        currentType = position;
        mediaPlayer.onStop();
        isPlaying = false;
        mediaPlayer.player(jijins.get(position).getLive_url(), jijins.get(position).getUrl_type(), jijins.get(position).getUrl_js(), "","");
//        mediaPlayer.player("http://www.lesports.com/video/topic/s/14437_25598072.html", "auto", "_x={d:200,a:[location.href],e:function(b){return b?\"\"!=b.src?b.src:(b=document.getElementsByTagName(\"source\")[0])?b.getAttribute(\"src\"):\"\":\"\"},b:function(b){injectedObject.playVideo(b)},c:function(b,d){var a=document.getElementsByTagName(\"video\")[0];if(a){var c=_x.e(a);if(\"\"!=c&&(c!=_x.a[_x.a.length-1]&&(_x.a.push(c),a.play()),1!=a.paused&&-1<a.readyState)){a.pause();var e=document.createEvent(\"CustomEvent\");e.initCustomEvent(\"ended\");a.dispatchEvent(e)}if(_x.a.length>b||0>d)return _x.b(c)}setTimeout(function(){_x.c(b,d-(1<_x.a.length?_x.d:50))},_x.d)}};_x.c(2,1e4);", "","");
        isPlaying = true;
    }
}
