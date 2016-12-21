package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjzhibo.im.ImApi;
import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.FullScreenLottery;
import com.joytouch.superlive.adapter.LiveChatAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.interfaces.UpdateMoney;
import com.joytouch.superlive.interfaces.fulllotteryback;
import com.joytouch.superlive.javabean.CammerSaishiBean;
import com.joytouch.superlive.javabean.ChatInfo;
import com.joytouch.superlive.javabean.GiftVo;
import com.joytouch.superlive.javabean.LiveMatchInfoJavabean;
import com.joytouch.superlive.javabean.LotteryInfo;
import com.joytouch.superlive.javabean.RoomBank;
import com.joytouch.superlive.javabean.lotteryDetailInfo;
import com.joytouch.superlive.javabean.redInfo;
import com.joytouch.superlive.utils.Config;
import com.joytouch.superlive.utils.Fream.FBO;
import com.joytouch.superlive.utils.HideAnimationUtil;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.KeyboardUtil;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.TimeUtil;
import com.joytouch.superlive.widget.AnchorInfroDialog;
import com.joytouch.superlive.widget.BindNumberDialog;
import com.joytouch.superlive.widget.CameraPreviewFrameView;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.FullScreenGiftShowManager;
import com.joytouch.superlive.widget.FullScreenLottreyPopupwindow;
import com.joytouch.superlive.widget.LiveChatDialog;
import com.joytouch.superlive.widget.LiveShareDialog;
import com.joytouch.superlive.widget.LotteryRedDialog;
import com.joytouch.superlive.widget.OuteLiveDialog;
import com.joytouch.superlive.widget.PayDialog;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;
import com.joytouch.superlive.widget.RedDialog;
import com.joytouch.superlive.widget.SelfLotteryDialog;
import com.joytouch.superlive.widget.SendLiveRedDialog;
import com.joytouch.superlive.widget.UpdateScoreDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pili.pldroid.streaming.CameraStreamingManager;
import com.pili.pldroid.streaming.CameraStreamingSetting;
import com.pili.pldroid.streaming.MicrophoneStreamingSetting;
import com.pili.pldroid.streaming.StreamingProfile;
import com.pili.pldroid.streaming.SurfaceTextureCallback;
import com.pili.pldroid.streaming.WatermarkSetting;
import com.pili.pldroid.streaming.widget.AspectFrameLayout;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

public class LiveStreamingActivity extends BaseActivity implements
        CameraPreviewFrameView.Listener,fulllotteryback,
        View.OnClickListener,
        CameraStreamingManager.StreamingStateListener,SurfaceTextureCallback,
        ImApi.ReceiveMessage, ImApi.OtherMessage, ImApi.Match ,
        UpdateMoney{
    private static final int MSG_FB = 1;
    private static final int GET_ANIMAL = 3;
    private boolean mIsNeedFB = true;

    private AspectFrameLayout cameraPreview_afl;
    private CameraPreviewFrameView cameraPreview_surfaceView;

    protected CameraStreamingManager mCameraStreamingManager;
    protected CameraStreamingSetting mCameraStreamingSetting;
    protected StreamingProfile mProfile;
    protected JSONObject mJSONObject;
    private Button but_light;
    private Button but_camera;
    private ImageView iv_startStreaming;
    private ImageView iv_meiyan;
    private RelativeLayout rl_camere;
    private ImageView iv_bg;
    private RelativeLayout rl_start;
    private RelativeLayout rl_order;
    private ImageView iv_touch;
    private RelativeLayout rl_info;
    private ImageView iv_finish;
    private RelativeLayout rl_gold;
    private boolean isMessageShow = true;
    private RelativeLayout rl_chat;
    private RelativeLayout rl_cai;
    private RelativeLayout rl_gift;
    private RelativeLayout rl_close;
    private EditText et_input;
    private ListView listView;
    private CircleImageView anchorimg;
    private TextView anchorname;
    private TextView onLine;
    private TextView teamleft;
    private TextView teamright;
    private TextView countleft;
    private TextView countright;
    private TextView matchstatu;
    private TextView title;
    private TextView mymoney;

    private LiveChatAdapter adapter;
    private List<ChatInfo> list;
    private List<RoomBank> rankDatlist;

    private boolean isReady = false;
    private boolean mIsTorchOn = false;
    private Switcher mSwitcher = new Switcher();
    private int mCurrentCamFacingIndex = Config.DEFAULT_CAMERA_FACING_ID.ordinal();

    private String matchid;
    private String streamingAddress;
    private FBO mFBO = new FBO();
    private String chatMessage = "";
    private FullScreenLottreyPopupwindow LottreyPopupwindow;
    private List<redInfo> infos = new ArrayList<>();
    private boolean isDialogShow = false;
    private boolean isScreenLock = false;

    private PopupWindow popupWindow;

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_FB:
                    Log.e("msg_fb",mIsNeedFB+"");
                    mIsNeedFB = !mIsNeedFB;
                    Log.e("msg_fb",mIsNeedFB+"");
                    mCameraStreamingManager.setVideoFilterType(mIsNeedFB ?
                            CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY
                            : CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_NONE);
                    break;
            }
        }
    };
    private SharedPreferences sp;
    private String roomId;
    private boolean isScroll;
    private String start="";
    private CammerSaishiBean streaminfo;

    private HorizontalScrollView topll;
    private LinearLayout topcontainer;
    private RelativeLayout updateScore;
    private DrawerLayout fullscreenAnChor;
    private RelativeLayout drawerrl;
    private PullToRefreshLayout fullscreenll;
    private PullableListView fullsrcrenanchorList;
    private boolean isdown;
    private FullScreenLottery lotteryAdapter;
    private List<LotteryInfo> fullLottery;
    private TextView lottery_count;
    private String popupStatus="0";
    private RelativeLayout rl_screenlock;
    private RelativeLayout lotterysize;
    private LinearLayout gift_con;
    private FullScreenGiftShowManager giftManger;
    private LinearLayout ll_buttom;
    private LiveChatDialog chatDialog;
    private RelativeLayout matchstatus;
    private boolean isfirst=true;
    private boolean isFrist = true;//用于判断是否是第一次进来不是 则重新进入房间
    private LinearLayout emprty;
    private boolean isMirror=false;
    private MicrophoneStreamingSetting mMicrophoneStreamingSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_live_streaming);
        LogUtils.e("88888888888","oncreate");
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        streaminfo= (CammerSaishiBean) bundle.getSerializable("stream_info");
        Log.e("streamindo", streaminfo.room_id+"__"+streaminfo.match_name);

        fullLottery=new ArrayList<>();
        rankDatlist = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new LiveChatAdapter(list,LiveStreamingActivity.this);
        initView();
        initUI();
//注册聊天监听
        SuperLiveApplication.imApi.setReceiveMessageListener(this);
        //注册排行榜和在线人数监听
        SuperLiveApplication.imApi.setOtherMessageListener(this);
        //加入房间
        if(isFrist) {
            SuperLiveApplication.imApi.joinRoom(streaminfo.room_id, (String) SPUtils.get(this, Preference.nickname, "", Preference.preference));
            isFrist = false;
        }
        //注册修改比分接口
        SuperLiveApplication.imApi.setMatchListener(this);
        giftManger = new FullScreenGiftShowManager(this, gift_con);
//        giftManger.showGift();//开始显示礼物


    }

    private void initView() {
        cameraPreview_afl = (AspectFrameLayout) this.findViewById(R.id.cameraPreview_afl);
        cameraPreview_afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        cameraPreview_surfaceView = (CameraPreviewFrameView) this.findViewById(R.id.cameraPreview_surfaceView);
        cameraPreview_surfaceView.setListener(this);
        streamingAddress = streaminfo.getStream_info();
        try {
            mJSONObject = new JSONObject(streamingAddress);
            StreamingProfile.Stream stream = new StreamingProfile.Stream(mJSONObject);
            mProfile = new StreamingProfile();
            //推流相关的参数配置
            mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_HIGH2)//视频质量相关参数:  设置视频的 fps 为 30，码率为 512 kbps
                    .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)//音频质量相关参数:  设置音频的采样率为 44100 HZ，码率为 96 kbps。
                    //Encoding size level 对应的分辨率
                    .setEncodingSizeLevel(StreamingProfile.VIDEO_ENCODING_HEIGHT_720)//Encoding size 的设定,Preview size 代表的是 Camera 本地预览的 size，
                                                                                        // encoding size 代表的是编码时候的 size，即播放端不做处理时候看到视频的 size
                    .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)//质量优先，实际的码率可能高于设置的码率
                    .setDnsManager(getMyDnsManager())
                    .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))//推流信息的反馈,设定其反馈的频率，每隔 3 秒回调 StreamStatus 信息。
                    .setStream(stream);  // You can invoke this before startStreaming, but not in initialization phase.
            mProfile.setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.LAND);//推流端的参数设置是横屏还是竖屏

            mCameraStreamingSetting = new CameraStreamingSetting();
            mCameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                    .setContinuousFocusModeEnabled(true)//自动对焦功能开启
                    .setRecordingHint(false)//提升数据源的帧率,部分机型开启 Recording Hint 之后，会出现画面卡帧等风险，所以请慎用该 API。如果需要实现高 fps 推流，可以考虑开启并加入白名单机制
                    .setCameraFacingId(Config.DEFAULT_CAMERA_FACING_ID)
                    .setBuiltInFaceBeautyEnabled(true)
                    .setResetTouchFocusDelayInMs(3000)
//                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)//设置对焦模式
                    //Camera 预览 size
                    .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.SMALL)
                    .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                    .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                    .setFrontCameraMirror(isMirror)//对于有对前置摄像头进行 Mirror 操作的用户,该操作目前仅针对播放端有效。可以避免前置摄像头拍摄字体镜像反转问题
                    .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);

            mCameraStreamingManager = new CameraStreamingManager(this, cameraPreview_afl, cameraPreview_surfaceView, CameraStreamingManager.EncodingType.SW_VIDEO_WITH_SW_AUDIO_CODEC);  // soft codec
            //水印相关的配置
            WatermarkSetting watermarksetting = new WatermarkSetting(this, R.drawable.chaojizhibo, WatermarkSetting.WATERMARK_LOCATION.SOUTH_WEST,WatermarkSetting.WATERMARK_SIZE.SMALL, 100);//100 为 alpha 值
            mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
            mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);
//            mCameraStreamingManager.prepare(mCameraStreamingSetting ,mMicrophoneStreamingSetting,watermarksetting,mProfile);//添加水印
            mCameraStreamingManager.prepare(mCameraStreamingSetting ,mProfile);
//            new EncodingOrientationSwitcher().run();

            mCameraStreamingManager.setStreamingStateListener(this);
            mCameraStreamingManager.setSurfaceTextureCallback(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initUI() {
        gift_con=(LinearLayout)this.findViewById(R.id.gift_con);
        emprty = (LinearLayout) findViewById(R.id.empty);
        ll_buttom=(LinearLayout)this.findViewById(R.id.ll_buttom);
        iv_startStreaming = (ImageView) this.findViewById(R.id.iv_startStreaming);
        iv_startStreaming.setOnClickListener(this);
        rl_camere = (RelativeLayout) this.findViewById(R.id.rl_camere);
        rl_camere.setOnClickListener(this);
        iv_bg = (ImageView) this.findViewById(R.id.iv_bg);
        iv_bg.setOnClickListener(this);
        rl_start = (RelativeLayout) this.findViewById(R.id.rl_start);
        rl_order = (RelativeLayout) this.findViewById(R.id.rl_order);
        rl_order.setOnClickListener(this);
        iv_touch = (ImageView) this.findViewById(R.id.iv_touch);
        iv_touch.setOnClickListener(this);
        rl_info = (RelativeLayout) this.findViewById(R.id.rl_info);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        rl_gold = (RelativeLayout) this.findViewById(R.id.rl_gold);
        rl_gold.setOnClickListener(this);
        rl_chat = (RelativeLayout) this.findViewById(R.id.rl_chat);
        rl_chat.setOnClickListener(this);
        rl_cai = (RelativeLayout) this.findViewById(R.id.rl_cai);
        rl_cai.setOnClickListener(this);
        rl_gift = (RelativeLayout) this.findViewById(R.id.rl_gift);
        rl_gift.setOnClickListener(this);
        rl_close = (RelativeLayout) this.findViewById(R.id.rl_close);
        rl_close.setOnClickListener(this);
        et_input = (EditText) this.findViewById(R.id.et_input);
        listView = (ListView) this.findViewById(R.id.listView);
        anchorimg = (CircleImageView) findViewById(R.id.iv_icon);
        anchorname = (TextView) findViewById(R.id.tv_name);
        onLine = (TextView) findViewById(R.id.tv_num);
        teamleft = (TextView) findViewById(R.id.tv_name_left);
        teamright = (TextView) findViewById(R.id.tv_name_right);
        countleft = (TextView) findViewById(R.id.tv_score_left);
        countright = (TextView) findViewById(R.id.tv_score_right);
        matchstatu = (TextView) findViewById(R.id.tv_time);
        topll = (HorizontalScrollView) findViewById(R.id.top_ll);
        topcontainer = (LinearLayout) findViewById(R.id.top_container);
        updateScore = (RelativeLayout) findViewById(R.id.update_score);
        fullscreenAnChor = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerrl = (RelativeLayout) findViewById(R.id.drawerll);
        fullscreenll = (PullToRefreshLayout) findViewById(R.id.fullscreen_anchor_ll);
        fullsrcrenanchorList = (PullableListView) findViewById(R.id.fullscreen_anchor_list);
        lottery_count = (TextView) findViewById(R.id.tv_lottery_num);
        lotterysize = (RelativeLayout) findViewById(R.id.lottery_num_bg);
        title = (TextView) findViewById(R.id.tv_title);
        mymoney = (TextView) findViewById(R.id.tv_gold_num);
        matchstatus = (RelativeLayout) findViewById(R.id.match_staus);
        matchstatus.setVisibility(View.GONE);
        lotterysize.setVisibility(View.GONE);
        title.setText(streaminfo.match_name);
        mymoney.setText((String) SPUtils.get(LiveStreamingActivity.this, Preference.balance, "0", Preference.preference));
        if(streaminfo!=null) {
            anchorname.setText(streaminfo.author_info.nick_name);
            ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/" + streaminfo.author_info.image, anchorimg, ImageLoaderOption.optionsHeader);
            onLine.setText("在线" + streaminfo.online + "人");
            String[] socres = streaminfo.room_score.split(",");
            if(socres.length==2){
                countleft.setText(socres[0]);
                countright.setText(socres[1]);
            }
            matchstatu.setText("");
            teamleft.setText(streaminfo.team_info.get(0).competitor_name);
            teamright.setText(streaminfo.team_info.get(1).competitor_name);
        }
        Preference.room_id = streaminfo.room_id;
        Log.e("roomid", streaminfo.room_id);
        roomId = streaminfo.room_id;
        anchorimg.setOnClickListener(this);
        updateScore.setOnClickListener(this);
        fullscreenAnChor.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        fullscreenAnChor.setScrimColor(Color.TRANSPARENT);

        fullscreenll.setCanPullDown(false);
        fullscreenll.setCanPullUp(false);
        fullscreenll.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                isdown = true;
                getLotteryList();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
        rl_screenlock = (RelativeLayout) this.findViewById(R.id.rl_screenlock);

        rl_screenlock.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isScreenLock = false;
                        Preference.isClock = false;
                        iv_startStreaming.setEnabled(true);
                        showAndHideView();
                        rl_screenlock.setVisibility(View.GONE);
                    }
                }, 1000);
                return true;
            }
        });
        listView.setAdapter(adapter);
        Preference.zhubo_id = streaminfo.author_info.userid;
//        fullscreenAnChor.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                new HideAnimationUtil(rl_chat,rl_cai,rl_gift,rl_camere,rl_order,rl_close,iv_finish,rl_info,rl_gold,LiveStreamingActivity.this).hideAnimation();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                new HideAnimationUtil(rl_chat,rl_cai,rl_gift,rl_camere,rl_order,rl_close,iv_finish,rl_info,rl_gold,LiveStreamingActivity.this).showViewAnimation();
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//
//            }
//        });
    }



    //用户详情
    public void userDetail(int i,String userid){
        AnchorInfroDialog dialog = new AnchorInfroDialog(this);
        dialog.setType(i);
        dialog.setUserid(userid);
        dialog.show();

    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onZoomValueChanged(float factor) {
        return false;
    }

    private static DnsManager getMyDnsManager() {
        IResolver r0 = new DnspodFree();
        IResolver r1 = AndroidDnsServer.defaultResolver();
        IResolver r2 = null;
        try {
            r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_touch:
                showAndHideView();
                //如果不是null,关闭竞猜题底边栏
                if (LottreyPopupwindow!=null){
                    LottreyPopupwindow.HideView();
                }
                break;
            case R.id.iv_finish:
                finish();
                break;
            case R.id.iv_startStreaming:
                //开始直播
                if (isReady){
                    iv_bg.setVisibility(View.GONE);
                    rl_start.setVisibility(View.GONE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (mCameraStreamingManager != null) {
                                mCameraStreamingManager.startStreaming();
                                Log.e("start", "startStreaming");
                            }
                        }
                    }).start();
                }else {
                    showToast("准备中，请稍后···");
                }
                start=(System.currentTimeMillis()/1000)+"";
                break;
            case R.id.rl_camere:
                showCamera(v);
                break;
            case R.id.rl_flip:
                //翻转摄像头
                new Handler().postDelayed(mSwitcher, 200);
                if (popupWindow !=null){
                    popupWindow.dismiss();
                }
                break;
            case R.id.rl_meiyan:
                //美颜
                if (!mHandler.hasMessages(MSG_FB)) {
                    mHandler.sendEmptyMessage(MSG_FB);
                }
                if (popupWindow !=null){
                    popupWindow.dismiss();
                }
                break;
            case R.id.rl_light:
                //闪光灯
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsTorchOn) {
                            mIsTorchOn = true;
                            mCameraStreamingManager.turnLightOn();
                        } else {
                            mIsTorchOn = false;
                            mCameraStreamingManager.turnLightOff();
                        }
                    }
                }).start();
                if (popupWindow !=null){
                    popupWindow.dismiss();
                }
                break;
            case R.id.rl_order:
                showOrder(v);
                break;
            case R.id.rl_lock:
                //锁屏
                if (null!= popupWindow) popupWindow.dismiss();
                showAndHideView();
                isScreenLock = true;
                iv_startStreaming.setEnabled(false);
                Preference.isClock = true;
                rl_screenlock.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_lottery:
                //出题
                SelfLotteryDialog lotteryDialog = new SelfLotteryDialog(this,streaminfo.room_id,streaminfo.match_id);
                lotteryDialog.setTv_gold(mymoney);
                lotteryDialog.show();
                if (popupWindow !=null){
                    popupWindow.dismiss();
                }
                break;
            case R.id.rl_share:
                //分享
                LiveShareDialog shareDialog = new LiveShareDialog(this);
                shareDialog.setMatchid(streaminfo.getMatch_id(),streaminfo.getShare_url(),streaminfo.getMatch_name());
                shareDialog.show();
                if (popupWindow !=null){
                    popupWindow.dismiss();
                }
                break;
            case R.id.rl_gold:
                //金币
                PayDialog payDialog = new PayDialog(LiveStreamingActivity.this);
                payDialog.setCancelable(false);
                payDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        mymoney.setText((String) SPUtils.get(LiveStreamingActivity.this,Preference.balance,"0",Preference.preference));
                    }
                });
                payDialog.show();
                break;
            case R.id.rl_chat:
                //聊天按钮
                chatDialog = new LiveChatDialog(this, new LiveChatDialog.SendClickListener() {
                    @Override
                    public void onSend(EditText editor,String message) {
                        //主播发送的内容
                        SuperLiveApplication.imApi.sendRoomMsg(streaminfo.room_id, message);
                        KeyboardUtil.closeKeybord(editor, LiveStreamingActivity.this);
                        editor.setText("");
                    }
                });
                //1.需要将布局最外面套一个escrollview,还需要改变AndroidManifest的配置
                //2. 设置dialog的位置在底部(默认在中间),window.setGravity(Gravity.BOTTOM);设置在底部
                // 3. 防止软键盘弹出布局变形,不会向上移动,不影响activity原有布局
                // android:windowSoftInputMode="adjustResize|adjustNothing"
                //  android:configChanges="keyboardHidden|orientation|screenSize"
                Window window=chatDialog.getWindow();
                window.setGravity(Gravity.BOTTOM);
                chatDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (!"".equals(chatDialog.getEt_input().getText().toString())){
                            Log.e("chatDialog",chatDialog.getEt_input().getText().toString());
                            chatMessage = chatDialog.getEt_input().getText().toString();
                        }else {
                            chatMessage = "";
                        }
                    }
                });
                chatDialog.show();
                chatDialog.setMemoryMessage(chatMessage);
                break;
            case R.id.rl_cai:
                //竞猜
                //全屏竞猜列表
                if(fullscreenAnChor.isDrawerOpen(drawerrl)){
                    fullscreenAnChor.closeDrawer(drawerrl);
                }else{
                    fullscreenAnChor.openDrawer(drawerrl);
                    getLotteryList();
                }
                break;
            case R.id.rl_gift:
                //红包
                SendLiveRedDialog liveRedDialog = new SendLiveRedDialog(this);
                liveRedDialog.setMatchid(streaminfo.getMatch_id());
                liveRedDialog.setRoomid(streaminfo.getRoom_id());
                liveRedDialog.setTv_gold(mymoney);
                liveRedDialog.show();
                break;
            case R.id.rl_close:
                //关闭
                showOutLiveDialog();
                break;
            case R.id.iv_icon://点击主播头像
                AnchorInfroDialog dialog = new AnchorInfroDialog(this);
                dialog.setType(4);
                dialog.setUserid(streaminfo.author_info.userid);
                dialog.show();
                break;
            case R.id.update_score://修改比分
                if (isScreenLock){
                    return;
                }
                UpdateScoreDialog dialogs = new UpdateScoreDialog(LiveStreamingActivity.this);
                dialogs.setRedname(streaminfo.team_info.get(0).competitor_name);
                dialogs.setBluename(streaminfo.team_info.get(1).competitor_name);
                dialogs.setbifenlift(countleft.getText().toString().trim());
                dialogs.setbifenright(countright.getText().toString().trim());
                dialogs.setCancelable(false);
                dialogs.show();
                break;
            case R.id.iv_bg:
                showAndHideView();
                //如果不是null,关闭竞猜题底边栏
                if (LottreyPopupwindow!=null){
                    LottreyPopupwindow.HideView();
                }
                break;
        }
    }
    private void showCamera(View v){
        View view = LayoutInflater.from(this).inflate(R.layout.popup_camera,null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = view.getMeasuredWidth();
        int popupHeight = view.getMeasuredHeight();
        int[] location = new int[2];
        // 允许点击外部消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 获得位置
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
        view.findViewById(R.id.rl_flip).setOnClickListener(this);
        view.findViewById(R.id.rl_meiyan).setOnClickListener(this);
        view.findViewById(R.id.rl_light).setOnClickListener(this);
    }
    private void showOrder(View v){
        View view = LayoutInflater.from(this).inflate(R.layout.pop_order,null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = view.getMeasuredWidth();
        int popupHeight = view.getMeasuredHeight();
        int[] location = new int[2];
        // 允许点击外部消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 获得位置
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
        view.findViewById(R.id.rl_lock).setOnClickListener(this);
        view.findViewById(R.id.rl_lottery).setOnClickListener(this);
        view.findViewById(R.id.rl_share).setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ceshi", isfirst + "");
       //isfirst:  防止黑屏后出来dialog重新绘画,重新组一边转换横屏
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && isfirst==true){
            isfirst=false;
            new Handler().postDelayed(new EncodingOrientationSwitcher(), 500);
        }
        if (sp.getString(Preference.mobile,"").equals("")){
            BindNumberDialog dialog= new BindNumberDialog(LiveStreamingActivity.this,R.layout.bindnum_dialog);
            Window window=dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            dialog.show();
        }
        try {
            mCameraStreamingManager.resume();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
        if(list!= null&&list.size()>0){
            SuperLiveApplication.imApi.joinRoom(streaminfo.room_id, "##" + (String) SPUtils.get(LiveStreamingActivity.this, Preference.nickname, "", Preference.preference));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SuperLiveApplication.imApi.leaveRoom(streaminfo.room_id);
        
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mCameraStreamingManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraStreamingManager.destroy();
        SuperLiveApplication.imApi.leaveRoom(streaminfo.room_id);
        Preference.isClock = false;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getRepeatCount() == 0) {
////            showOutLiveDialog();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    private void showOutLiveDialog() {
        if (streaminfo.getTeam_info().size()==2){
            String team1=streaminfo.getTeam_info().get(0).competitor_name;
            String team2=streaminfo.getTeam_info().get(1).competitor_name;
            String img_1=streaminfo.getTeam_info().get(0).competitor_image;
            String img_2=streaminfo.getTeam_info().get(1).competitor_image;
            OuteLiveDialog outeLiveDialog = new OuteLiveDialog(LiveStreamingActivity.this,streaminfo.room_id,start,streaminfo.author_info.userid
            ,team1,team2,img_1,img_2);

            outeLiveDialog.show();
        }

    }



    @Override
    public void onStateChanged(int i, Object o) {
        switch (i) {
            case CameraStreamingManager.STATE.PREPARING:
                Log.e("CameraStreamingManager","PREPARING");
                break;
            case CameraStreamingManager.STATE.READY:
                Log.e("CameraStreamingManager","READY");
                // start streaming when READY
                isReady = true;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mCameraStreamingManager != null) {
//                            mCameraStreamingManager.startStreaming();
//                            Log.e("start","i =");
//                        }
//                    }
//                }).start();
                break;
            case CameraStreamingManager.STATE.CONNECTING:
                Log.e("CameraStreamingManager","CONNECTING");
                break;
            case CameraStreamingManager.STATE.STREAMING:
                Log.e("CameraStreamingManager","STREAMING");
                // The av packet had been sent.
                break;
            case CameraStreamingManager.STATE.SHUTDOWN:
                Log.e("CameraStreamingManager","SHUTDOWN");
                // The streaming had been finished.
                break;
            case CameraStreamingManager.STATE.IOERROR:
                // Network connect error.网络断开
                Log.e("CameraStreamingManager","Network connect error");
                break;
            case CameraStreamingManager.STATE.SENDING_BUFFER_EMPTY:
                Log.e("CameraStreamingManager","SENDING_BUFFER_EMPTY");
                break;
            case CameraStreamingManager.STATE.SENDING_BUFFER_FULL:
                Log.e("CameraStreamingManager","SENDING_BUFFER_FULL");
                break;
            case CameraStreamingManager.STATE.AUDIO_RECORDING_FAIL:
                Log.e("CameraStreamingManager","AUDIO_RECORDING_FAIL");
                // Failed to record audio.
                break;
            case CameraStreamingManager.STATE.OPEN_CAMERA_FAIL:
                Log.e("CameraStreamingManager","OPEN_CAMERA_FAIL");
                // Failed to open camera.
                break;
            case CameraStreamingManager.STATE.DISCONNECTED:
                Log.e("CameraStreamingManager","DISCONNECTED");
                // The socket is broken while streaming 推流中网络断开

                break;
        }
    }

    @Override
    public boolean onStateHandled(int i, Object o) {
        return false;
    }

    @Override
    public void Messagelist(String from, String body, String type2) {

    }

    @Override
    public void chatMessage(String from, String body) {

    }

    @Override
    public void groupMessage(ChatInfo info) {
        LogUtils.e("test", roomId + "  ====  " + info.getRoomid());
        if(!roomId.equals(info.getRoomid())){
            return;
        }
        if(list == null) {
            return;
        }
        info.setIsLive(true);
        if(!"9".equals(info.getType())) {
            list.add(info);
            adapter.notifyDataSetChanged();
            listView.setSelection(list.size() - 1);

        }
        //礼物接收
        if (info.getType().equals("11") && !info.getGift_id().equals("4")) {
            GiftVo vo = new GiftVo();
            vo.setUserId(info.getNickname());
            vo.setNum(Integer.parseInt(info.getCount()));
            vo.setImage(info.getImage());
            vo.setGift_id(info.getGift_id());
            vo.setMsg(info.getContent());
            vo.setIdent(info.getIdent());
            giftManger.addGift(vo);
            giftManger.showGift();//开始显示礼物
        }

        //全屏竞猜逻辑处理(如果新的竞猜和补刀来时,判断popup是否消失)"4".equals(info.getType())补刀不需要
        if (("9".equals(info.getType()) && (popupStatus.equals("0")))){
            if (TextUtils.isEmpty(info.getTimestamp())){
                Log.e("buglist","---"+info.getLotteryid());
                getLotteryMesage(info.getLotteryid(), "9");
            }
//            if ("4".equals(info.getType())) {
//                    //获取竞猜题目详情数据
//                    getLotteryMesage(info.getLotteryid(), "4");
//            } else {
//                    //获取竞猜题目详情数据
//                    getLotteryMesage(info.getLotteryid(), "9");
//            }
        }
    }
//聊天状态监听
    private void getLotteryMesage(final String lotteryid,final String type) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", streaminfo.room_id);
        builder.add("que_id", lotteryid);
        builder.add("token", (String) SPUtils.get(LiveStreamingActivity.this, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils(this);
        requestUtils.httpPost(Preference.LotteryDetail, builder, new HttpRequestUtils.ResRultListener() {

            @Override
            public void onFailure(IOException e) {
                Toast.makeText(LiveStreamingActivity.this, "访问网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {
                if (chatDialog != null && chatDialog.isShowing()) {
                    chatDialog.dismiss();
                }
                Log.e("全屏竞猜获取竞猜详情页数据", json);
                Gson json2 = new Gson();
                lotteryDetailInfo lotinfo = json2.fromJson(json, lotteryDetailInfo.class);
                if (lotinfo.getStatus().equals("_0000") && (infos.size()==0)) {
                    Log.e("infosize",infos.size()+"");
                    FullScreenLottreyPopupwindow LottreyPopupwindow = new FullScreenLottreyPopupwindow(lotteryid, LiveStreamingActivity.this, rl_camere, lotinfo, type);
                    LottreyPopupwindow.Showloction();
                    LottreyPopupwindow.show();
                } else if (lotinfo.getStatus().equals("_1000")) {
                    new LoginUtils(LiveStreamingActivity.this).reLogin(LiveStreamingActivity.this);
                    Toast.makeText(LiveStreamingActivity.this, lotinfo.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LiveStreamingActivity.this, lotinfo.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void statusFormIm(int status) {
        LogUtils.e("=======", "   " + status);
        if(status==1){
            LogUtils.e("=======","   "+list.size());
            if(list.size()>0){
                SuperLiveApplication.imApi.joinRoom(streaminfo.room_id, "##"+(String) SPUtils.get(LiveStreamingActivity.this, Preference.nickname, "", Preference.preference));
            }else {
                SuperLiveApplication.imApi.joinRoom(streaminfo.room_id, (String) SPUtils.get(LiveStreamingActivity.this, Preference.nickname, "", Preference.preference));
            }

        }
    }

    @Override
    public void redReceive(redInfo info) {
        infos.add(info);
        xunhuan();
    }
    public void xunhuan(){
        for (int i = 0;i<infos.size();i++){
            if (!isDialogShow){
                showRedDialog(infos.get(i), i);
//                infos.remove(i);
            }
        }
    }
    //
    public void showRedDialog(redInfo info, final int i){
        if (null == info.getQuestion_title()||"".equals(info.getQuestion_title())){
            RedDialog redDialog = new RedDialog(LiveStreamingActivity.this,info);
            redDialog.setResultGold(mymoney);
            redDialog.setCancelable(false);
            redDialog.show();
            isDialogShow = true;
            redDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isDialogShow = false;
                    infos.remove(i);
                    xunhuan();
                }
            });
        }else {
            LotteryRedDialog lotteryRedDialog = new LotteryRedDialog(LiveStreamingActivity.this,info);
            lotteryRedDialog.setTv_result_gold(mymoney);
            lotteryRedDialog.show();
            lotteryRedDialog.setCancelable(false);
            isDialogShow = true;
            lotteryRedDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isDialogShow = false;
                    infos.remove(i);
                    xunhuan();
                }
            });
        }

    }

    @Override
    public void online(String count, String roomid) {
    if (roomid.equals(roomId)&&onLine!=null){
            onLine.setText("在线" + count + "人");
        }
    }

    @Override
    public void roomRankTop(String roomid,List<RoomBank> list) {
        if(!roomid.equals(streaminfo.room_id)){
            return;
        }

        if(topcontainer!=null&&list!=null&&list.size()>0){
            rankDatlist.clear();
            rankDatlist.addAll(list);
            topcontainer.removeAllViews();
            for (int i = 0;i<list.size();i++){
                View view = LiveStreamingActivity.this.getLayoutInflater().inflate(R.layout.top_item,null);
                CircleImageView head = (CircleImageView) view.findViewById(R.id.rank_3);
                ImageView toplogo = (ImageView) view.findViewById(R.id.top_3);
                toplogo.setVisibility(View.GONE);
                if(i == 0){
                    toplogo.setVisibility(View.VISIBLE);
                    toplogo.setBackgroundResource(R.drawable.top_1);
                }

                if(i == 1){
                    toplogo.setVisibility(View.VISIBLE);
                    toplogo.setBackgroundResource(R.drawable.top_2);
                }

                if(i == 2){
                    toplogo.setVisibility(View.VISIBLE);
                    toplogo.setBackgroundResource(R.drawable.top_3);
                }
                ImageLoader.getInstance().displayImage(Preference.photourl+"40x40/"+list.get(i).getImage(),head,ImageLoaderOption.optionsHeader);
                final int finalI = i;
                head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(rankDatlist.get(finalI)!=null&&!TextUtils.isEmpty(rankDatlist.get(finalI).getUseid())){
                            String userid = (String) SPUtils.get(getApplicationContext(), Preference.myuser_id, "", Preference.preference);
                            if (userid.equals(rankDatlist.get(finalI).getUseid())) {
                                userDetail(4, rankDatlist.get(finalI).getUseid());
                            } else {
                                userDetail(5, rankDatlist.get(finalI).getUseid());

                            }
                        }
                    }
                });
                topcontainer.addView(view);

            }
        }
    }
//比分监听
    @Override
    public void onSurfaceCreated() {
        mFBO.initialize(this);
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        mFBO.updateSurfaceSize(width, height);
    }

    @Override
    public void onSurfaceDestroyed() {
        mFBO.release();
    }

    @Override
    public int onDrawFrame(int texId, int texWidth, int texHeight, float[] transformMatrix) {
        int newTexId = mFBO.drawFrame(texId, texWidth, texHeight);
        return newTexId;
    }

    @Override
    public void match(boolean b, List<LiveMatchInfoJavabean> infos) {
        for (int i = 0; i < infos.size(); i++) {
            if (streaminfo.match_id.equals(infos.get(i).getMatchId())) {
                if (b) {
                        countleft.setText(infos.get(i).getScore().split(" - ")[0]);
                        countright.setText(infos.get(i).getScore().split(" - ")[1]);
                        if (!TextUtils.isEmpty(infos.get(i).getStating())) {
                            matchstatu.setVisibility(View.VISIBLE);
                            matchstatus.setVisibility(View.VISIBLE);
                            matchstatu.setText(infos.get(i).getStating());
                        } else {
                            matchstatu.setVisibility(View.GONE);
                            matchstatus.setVisibility(View.GONE);
                        }
                }
            }
        }
    }

    @Override
    public void isDissmiss(String status) {
        popupStatus=status;
        if (status.equals("0")){
            if (isScreenLock != true){
                ll_buttom.setVisibility(View.VISIBLE);
            }
        }else{
            if (chatDialog!=null){
                chatDialog.dismiss();
            }
//            ll_buttom.setVisibility(View.INVISIBLE);
            //关闭竞猜列表页
            if(fullscreenAnChor.isDrawerOpen(drawerrl)){
                fullscreenAnChor.closeDrawer(drawerrl);
            }
        }
    }

    @Override
    public void update(String money) {
        mymoney.setText(money);
    }

    //转换摄像头
    private class Switcher implements Runnable {
        @Override
        public void run() {
            mCurrentCamFacingIndex = (mCurrentCamFacingIndex + 1) % CameraStreamingSetting.getNumberOfCameras();

            CameraStreamingSetting.CAMERA_FACING_ID facingId;
            if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
            } else if (mCurrentCamFacingIndex == CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT.ordinal()) {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
            } else {
                facingId = CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
            }
            mCameraStreamingManager.switchCamera(facingId);
        }
    }
    private void showAndHideView(){
        if (!isScreenLock){
            if (!isMessageShow){
                new HideAnimationUtil(rl_chat,rl_cai,rl_gift,rl_camere,rl_order,rl_close,iv_finish,rl_info,rl_gold,this).showViewAnimation();
                topll.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);
                isMessageShow = true;
            }else {
                new HideAnimationUtil(rl_chat,rl_cai,rl_gift,rl_camere,rl_order,rl_close,iv_finish,rl_info,rl_gold,this).hideAnimation();
                topll.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                isMessageShow = false;
            }
        }

    }
    private class EncodingOrientationSwitcher implements Runnable {

        @Override
        public void run() {
            mProfile.setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.LAND);
            mCameraStreamingManager.setStreamingProfile(mProfile);

            mCameraStreamingManager.notifyActivityOrientationChanged();
        }
    }
    //得到全屏竞猜数据
    public void getLotteryList(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", Preference.room_id);
        builder.add("token", (String) SPUtils.get(LiveStreamingActivity.this, Preference.token, "", Preference.preference));
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils(LiveStreamingActivity.this);
        httpRequestUtils.httpPost(Preference.LotteryLabel, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                if (isdown) {
                    isdown = false;
                    fullscreenll.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            @Override
            public void onSuccess(String json) {
                Log.e("得到全屏竞猜数据参数错误", json);
                try {
                    if (isdown) {
                        isdown = false;
                        fullscreenll.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                    fullLottery.clear();
                    JSONObject object = new JSONObject(json);
                    LogUtils.e("*******", "yyyyyy");
                    if ("_0000".equals(object.optString("status"))) {
                        LogUtils.e("*******", "bbb");
                        JSONObject array = object.optJSONObject("list");
                        JSONArray conduct = array.optJSONArray("conduct");
                        if (conduct != null) {

                            List<LotteryInfo> lotteryno = new ArrayList<LotteryInfo>();
                            for (int i = 0; i < conduct.length(); i++) {
                                JSONObject object1 = conduct.getJSONObject(i);
                                lotteryno.add(jsoParse(object1));
                            }
                            fullLottery.addAll(lotteryno);
                            LogUtils.e("()()()()()()()()", " " + fullLottery + "      " + fullLottery.size());
                            if (fullLottery != null && fullLottery.size() > 0) {
                                lotterysize.setVisibility(View.VISIBLE);
                                lottery_count.setText("" + fullLottery.size());
                            } else {
                                lotterysize.setVisibility(View.GONE);
                            }


                        } else {
                            lotterysize.setVisibility(View.GONE);
                        }

                        JSONArray colse = array.optJSONArray("close");
                        if (colse != null) {

                            List<LotteryInfo> lotteryyes = new ArrayList<LotteryInfo>();
                            for (int i = 0; i < colse.length(); i++) {
                                JSONObject object1 = colse.getJSONObject(i);
                                lotteryyes.add(jsoParse(object1));
                            }
                            fullLottery.addAll(lotteryyes);
                        }
                        if(fullLottery.size()==0){
                           emprty.setVisibility(View.VISIBLE);
                        }else {
                           emprty.setVisibility(View.GONE);
                        }
                        lotteryAdapter = new FullScreenLottery(fullLottery, LiveStreamingActivity.this);
                        fullsrcrenanchorList.setAdapter(lotteryAdapter);
                        lotteryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(LiveStreamingActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public  LotteryInfo jsoParse(JSONObject object) {
        LotteryInfo info = new LotteryInfo();
        info.setTitle(object.optString("content"));
        info.setEndtime(TimeUtil.currentSplitTimeStringHm(object.optString("stop_time")));
        info.setStopTime(object.optLong("stop_time_s"));
        info.setMatchid(object.optString("guess_id"));
        info.setBettingGolde(object.optString("room_bet"));
        info.setLeftgold(object.optString("total_left"));
        info.setRightgold(object.optString("total_right"));
        info.setLeftselector(object.optString("option_left"));
        info.setRightselector(object.optString("option_right"));
        info.setWingold(object.optString("my_win_money"));
        info.setIsSelfOpenLiving(true);

        if(!TextUtils.isEmpty(object.optString("my_option"))) {
            info.setIsSelf(true);
            if(object.optString("option_left").equals(object.optString("my_option"))) {
                info.setSelfeSelector(0);
            }else {
                info.setSelfeSelector(1);
            }
        }else {
            info.setIsSelf(false);
        }
        info.setIsOdds(false);
        switch (object.optInt("status")){
            case 2:
                //进行中
                info.setIsOpenLottery(false);
                info.setIsEnd(false);
                break;
            case 3:
                //已截止
                info.setIsOpenLottery(false);
                info.setIsEnd(true);
                break;
            case 4:
                //已开奖
                info.setIsOpenLottery(true);
                info.setIsEnd(true);
                info.setIsWin(false);
                break;
            case 5:
                info.setIsOpenLottery(false);
                break;
            case 6:
                //赢
                info.setIsOpenLottery(true);
                info.setIsEnd(true);
                info.setIsWin(true);
                break;
        }
        return info;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        switch (level){
            case TRIM_MEMORY_UI_HIDDEN:
                SuperLiveApplication.imApi.leaveRoom(streaminfo.room_id);
                break;
        }

    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        return;

    }

}
