package com.joytouch.superlive.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjzhibo.im.ImApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.AnchorListAdapter;
import com.joytouch.superlive.adapter.AttentionFansViewPagerAdapter;
import com.joytouch.superlive.adapter.FullScreenAchorAdapter;
import com.joytouch.superlive.adapter.FullScreenLottery;
import com.joytouch.superlive.adapter.ViewPagerAdapter;
import com.joytouch.superlive.app.MediaPlayer;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.fragement.ChatFragment;
import com.joytouch.superlive.fragement.Goldwater;
import com.joytouch.superlive.fragement.LiveactionFragment;
import com.joytouch.superlive.fragement.LotteryFragment;
import com.joytouch.superlive.interfaces.FullScreenListener;
import com.joytouch.superlive.interfaces.PresentCallback;
import com.joytouch.superlive.interfaces.SelectorAchor;
import com.joytouch.superlive.javabean.AnchorInfo;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.ChatInfo;
import com.joytouch.superlive.javabean.LiveDetail;
import com.joytouch.superlive.javabean.LiveMatchInfoJavabean;
import com.joytouch.superlive.javabean.LotteryInfo;
import com.joytouch.superlive.javabean.Question;
import com.joytouch.superlive.javabean.QuestionBase;
import com.joytouch.superlive.javabean.RoomBank;
import com.joytouch.superlive.javabean.SendLotteryResult;
import com.joytouch.superlive.javabean.SendQuestion;
import com.joytouch.superlive.javabean.lotteryDetailInfo;
import com.joytouch.superlive.javabean.redInfo;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.EmojiReplace;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.KeyBoardUtils;
import com.joytouch.superlive.utils.KeyboardUtil;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.TimeUtil;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.CloseDialog;
import com.joytouch.superlive.widget.CoustomViewPager;
import com.joytouch.superlive.widget.GameDialog;
import com.joytouch.superlive.widget.LotteryRedDialog;
import com.joytouch.superlive.widget.MyLinearLayout;
import com.joytouch.superlive.widget.OuSignDialog;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;
import com.joytouch.superlive.widget.RedDialog;
import com.joytouch.superlive.widget.SelfLotteryDialog;
import com.joytouch.superlive.widget.SendPresentAnimal;
import com.joytouch.superlive.widget.ShareDialog;
import com.joytouch.superlive.widget.UpdateScoreDialog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/11.
 */
public class LiveDetailActivity extends BaseActivity implements View.OnClickListener,SelectorAchor,
        ImApi.ReceiveMessage, ImApi.OtherMessage,FullScreenListener ,PresentCallback {
    private ChatFragment fragment;
    private LotteryFragment lotteryFragment;
    private LiveactionFragment liveactionFragment;
    private Goldwater goldwater;
    private CoustomViewPager pager;
    private List<Fragment> fragments;
    private List<String> titlename;
    private LinearLayout titleContainer;
    private ImageView back;
    private RelativeLayout containers;
    private ImageView fullscreen;
    private TextView time;
    private TextView room;
    private LinearLayout ll_room;
    private TextView onlines;
    private ImageView camera;
    private View anchorView;
    private boolean isOpenAnchor;//判断是否打开主播列表
    private boolean isFullScreen = false;//是否全屏
    private RelativeLayout vedioview;//视屏
    private int screenHeight;
    private int screenWidth;
    private RelativeLayout bottom;//视屏底边栏
    private RadioButton barrage;//弹幕按钮
    private RelativeLayout fullscreenRoom;//全屏房间按钮
    private RadioButton lockscreen;//锁屏按钮
    private TextView fullscreenname;
    private RelativeLayout top;
    private boolean isLock;//是否锁定
    private long hideTime = 5000;
    private  ImageView player;
    private DrawerLayout fullscreenAnChor;
    private PullToRefreshLayout fullscreenll;
    private PullableListView fullsrcrenanchorList;
    private List<AnchorInfo> info;
    private RelativeLayout drawerrl;
    private RelativeLayout bottom_chat;
    private LinearLayout ll_lottery;
    private int LIVE_SOURCE = 10000;
    private ImageView add;
    private LinearLayout addll;
    private boolean isAdd = false;//加号是否打开
    private LinearLayout invitation;
    private LinearLayout pay;
    private LinearLayout game;
    private LinearLayout exceptional;
    private ImageView bg;
    private RelativeLayout lottery_rl;
    private TextView lottery_count;
    private  TextView matchname;
    private EditText chat;
    private LiveDetail detail;
    private LinearLayout playerview;
    private boolean isLiveList;
    //隐藏view
    private Handler hideHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideView(View.GONE);
        }
    };

    private LiveSourceActivity live;
    private FullScreenAchorAdapter achorAdapter;
    private boolean isFullLottery = false;
    private List<LotteryInfo> fullLottery;
    private FullScreenLottery lotteryAdapter;
    private boolean isSoftKey;
    private GameDialog gamedialog;
    private RelativeLayout fullbottom;
    private MyLinearLayout parent;
    private boolean issoft;
    private String roomid;
    private String matchid;
    private String qiutanid;
    private LinearLayout loading;
    private int defaults = -1;
    private int old = -1;
    private boolean isdown;
    private LinearLayout llanchor;
    private ImageView startPlayer;
    private boolean isPlayer;
    private MediaPlayer mediaplayer;
    private CircleImageView fullscreenAnchorHead;
    private TextView fullscreenAnchorname;
    private TextView fullScreenAnchoronline;
    private TextView fullscreenanchorbet;
    private boolean isError;
    private LinearLayout countTime;
    private TextView lottery_title;
    private TextView left_answer;
    private TextView left_total_money;
    private TextView right_answer;
    private TextView right_all_money;
    private TextView room_money;
    private TextView my_bet_money;
    private TextView predict_money;
    private CountDownTimer Count;
    private List<redInfo> infos = new ArrayList<>();
    private boolean isDialogShow = false;
    //倒计时
    private Handler counts_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //倒计时开始网络时间
            long nowtime = (long) msg.obj;
            long time = endTime - nowtime;

            Log.e("time的值:",time+"___"+endTime+"__"+nowtime);
            if (time > 30000) {
//                countTime.setVisibility(View.INVISIBLE);
                time=30000;
            }
            if (time <= 30000 && time > 0) {
                countTime.setVisibility(View.VISIBLE);
                Count = new CountDownTimer(time, 180) {
                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) ((millisUntilFinished / 1000));
                        String miao = String.valueOf(seconds);
                        String mm = String.valueOf(millisUntilFinished / 10 % 100);
                        if (miao.length() == 1) {
                            miao = "0" + miao;
                        }
                        if (mm.length() == 1) {
                            mm = "0" + mm;
                        }
                        tv_counttime.setText(miao + "″" + mm);
                    }

                    public void onFinish() {
                        countTime.setVisibility(View.INVISIBLE);
                        Count = null;
                        System.gc();
                        left_check.setBackgroundResource(R.drawable.right_check_zhihui);
                        right_check.setBackgroundResource(R.drawable.left_check_zhihui);
                            if (sel.equals("1")) {
                                right_check.setEnabled(false);
                                tv_left_light.setVisibility(View.GONE);
                                iv_light_right.setVisibility(View.GONE);
                                tv_right_light.setVisibility(View.GONE);
                            } else if (sel.equals("2")) {
                                left_check.setEnabled(false);
                                tv_right_light.setVisibility(View.GONE);
                                iv_light_left.setVisibility(View.GONE);
                                tv_left_light.setVisibility(View.GONE);
                            }
                    }
                };
                Count.start();
            }
        }
    };
    private Long endTime;
    private TextView tv_counttime;
    private LinearLayout left_check;
    private LinearLayout right_check;
    private String roommoney="";
    private String full_lever="";
    private long firstTime=0;
    private long interval = 1000;//点击时间间隔
    private int count = 0;
    private TextView tv_right_light;
    private TextView tv_left_light;
    private ImageView iv_light_left;
    private ImageView iv_light_right;
    private long xx_time = 500;
    private TimerTask _task;
    private Handler _task_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (count == 1) {
            } else if (count > 1) {
            }
            delayTimer.cancel();
            count = 0;
            super.handleMessage(msg);
        }
    };
    private Timer delayTimer;
    private Handler xx_left_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            iv_light_left.setVisibility(msg.what);
            tv_left_light.setVisibility(msg.what);
        }
    };
    private Handler xx_right_Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            iv_light_right.setVisibility(msg.what);
            tv_right_light.setVisibility(msg.what);
        }
    };
    private String lotteryid;
    private String left_text;
    private String right_text;
    private float mPosX;
    private float mPosY;
    private float mCurPosX;
    private float mCurPosY;
    private GestureDetector mGestureDetector;
    private  boolean isListviewScroll = false;
    private boolean flag_=true;
    private TextView dui_1;
    private TextView dui_2;
    private TextView add_time;
    private TextView iv_dui_1;
    private TextView iv_dui_2;
    private LinearLayout re_bifen_bu;
    private String sel="";
    OrientationEventListener orient = null; //用这个类来监听,
    private long lastTime = 0;
    private float lastX = 0;
    String showorient = null;
    private TextView tv_team2color;
    private TextView tv_team1color;

    private boolean isActionShow = false;
    private boolean isLottery;
    private boolean isPermisssion;
    private ShareDialog sharedialog;
    private int postion;
    private String qid;
    private ImageView present;
    private LinearLayout ll_above_detail;
    private boolean isShow;
    private boolean isUpdateScore = false;
    private boolean isShowMatch;
    private List<Question> questions = new ArrayList<>();//题目列表
    private TextView tv_noquestion;//无题目提示语
    private ViewPager vp_question;//题目
    private List<View> views = new ArrayList<>();
    private ImageView iv_left;
    private ImageView iv_right;
    private Button but_start;
    private LinearLayout empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_livedetail);
        Intent intent = getIntent();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        roomid = getIntent().getStringExtra("romid");
        matchid = getIntent().getStringExtra("matchid");
        if(TextUtils.isEmpty(roomid)){
            roomid = "";
        }
        if(TextUtils.isEmpty(matchid)){
            matchid = "";
        }
        isLiveList = getIntent().getBooleanExtra("islive", false);
        Preference.match_id = matchid;
        qiutanid= getIntent().getStringExtra("normal");
        qid = getIntent().getStringExtra("qid");
        fragments = new ArrayList<Fragment>();
        titlename = new ArrayList<String>();
        info = new ArrayList<AnchorInfo>();
        titlename.add("聊天");
        titlename.add("竞猜");


        fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", matchid);
        fragment.setArguments(bundle);
        lotteryFragment = new LotteryFragment();

        goldwater = new Goldwater();
        fragments.add(fragment);
        fragments.add(lotteryFragment);

        SuperLiveApplication.imApi.setReceiveMessageListener(this);
        SuperLiveApplication.imApi.setOtherMessageListener(this);

        orient = new OrientationEventListener(LiveDetailActivity.this) { //这里的context可以在activity里用this来代替
            //或者使用getApplicationContext()
            @Override
            public void onOrientationChanged(int orientation) {

                long time = System.currentTimeMillis();
                if (time - lastTime > 200) {

                    //横屏
                    if (((orientation >= 230) && (orientation <= 310))||orientation == 90) {
                        if (!"LANDSCAPE".equals(showorient)) {
                            showorient = "LANDSCAPE";
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            fullScreen();
                        }
                    }
                    //竖屏
                    if (((orientation >= 0) && (orientation <= 30)) || (orientation >= 330)) {
                        if (!"PORTRAIT".equals(showorient)) {
                            showorient = "PORTRAIT";
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            smallScreen();
                        }
                    }

                    lastTime = time;
                }
            }
        };

        initView();
        if(!(boolean)SPUtils.get(LiveDetailActivity.this,"isSmallScreenSliding",false,Preference.preference)){

            firstguide("isSmallScreenSliding");
        }

    }

    @Override
    protected void onStart() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onStart();
    }

    private void Sign(String matchid) {

        SharedPreferences   sp_ = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp_.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("match_id",matchid)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.ouzhoubeisign, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(LiveDetailActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("欧洲杯签到参数错误", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            //如果今天没有签到
                            if (!Bean.is_sign.equals("1")) {
                                final OuSignDialog dialog = new OuSignDialog(LiveDetailActivity.this, Integer.parseInt(Bean.sign_day));
                                dialog.setCancelable(false);
                                dialog.show();
                            }
                        } else {
                            Toast.makeText(LiveDetailActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void initView() {
        empty = (LinearLayout) findViewById(R.id.emptys);
        present=(ImageView)findViewById(R.id.present);
        //判断是否是主播,设置不同的图片
        present.setBackgroundResource(R.drawable.present_logo);

        re_bifen_bu=(LinearLayout)findViewById(R.id.re_bifen_bu);
        dui_1=(TextView)findViewById(R.id.dui_1);
        dui_2=(TextView)findViewById(R.id.dui_2);
        add_time=(TextView)findViewById(R.id.add_time);
        iv_dui_1=(TextView)findViewById(R.id.iv_dui_1);
        iv_dui_2=(TextView)findViewById(R.id.iv_dui_2);
        tv_team2color=(TextView)findViewById(R.id.tv_team2color);
        tv_team1color=(TextView)findViewById(R.id.tv_team1color);
        tv_right_light=(TextView)findViewById(R.id.tv_right_light);
        tv_left_light=(TextView)findViewById(R.id.tv_left_light);
        iv_light_left=(ImageView)findViewById(R.id.iv_light_left);
        iv_light_right=(ImageView)findViewById(R.id.iv_light_right);
        right_check=(LinearLayout)findViewById(R.id.right_check);
        left_check=(LinearLayout)findViewById(R.id.left_check);
        tv_counttime=(TextView)findViewById(R.id.tv_counttime);
        predict_money=(TextView)findViewById(R.id.predict_money);
        my_bet_money=(TextView)findViewById(R.id.my_bet_money);
        room_money=(TextView)findViewById(R.id.room_money);
        right_all_money=(TextView)findViewById(R.id.right_all_money);
        right_answer=(TextView)findViewById(R.id.right_answer);
        left_total_money=(TextView)findViewById(R.id.left_total_money);
        left_answer=(TextView)findViewById(R.id.left_answer);
        lottery_title=(TextView)findViewById(R.id.lottery_title);
        countTime=(LinearLayout)findViewById(R.id.top);
        playerview = (LinearLayout) findViewById(R.id.mediaPlayer);
        fullscreenAnchorHead = (CircleImageView) findViewById(R.id.fullscreen_head);
        fullscreenAnchorname = (TextView) findViewById(R.id.fullscreen_name);
        fullScreenAnchoronline = (TextView) findViewById(R.id.fullscreen_online);
        fullscreenanchorbet = (TextView) findViewById(R.id.room_bet);
        startPlayer = (ImageView) findViewById(R.id.playerStart);
        llanchor = (LinearLayout) findViewById(R.id.ll_achor);
        loading = (LinearLayout) findViewById(R.id.loading);
        parent = (MyLinearLayout) findViewById(R.id.parent);
        fullbottom = (RelativeLayout) findViewById(R.id.full_bottom);
        fullbottom.setVisibility(View.GONE);
        bottom_chat = (RelativeLayout) findViewById(R.id.bottom_chat);
        ll_lottery = (LinearLayout) this.findViewById(R.id.ll_lottery);
        drawerrl = (RelativeLayout) findViewById(R.id.drawerll);
        fullscreenAnChor = (DrawerLayout) findViewById(R.id.drawerlayout);
        fullscreenll = (PullToRefreshLayout) findViewById(R.id.fullscreen_anchor_ll);
        fullsrcrenanchorList = (PullableListView) findViewById(R.id.fullscreen_anchor_list);
        player = (ImageView) findViewById(R.id.player);
        top = (RelativeLayout) findViewById(R.id.normaluser);
        lockscreen = (RadioButton) findViewById(R.id.lockscreen);
        fullscreenRoom = (RelativeLayout) findViewById(R.id.fullscreen_room);
        fullscreenname = (TextView) findViewById(R.id.fullscreen_match_name);
        barrage = (RadioButton) findViewById(R.id.barrage);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        vedioview = (RelativeLayout) findViewById(R.id.vedioView);
        titleContainer = (LinearLayout) findViewById(R.id.label);
        back = (ImageView) findViewById(R.id.back);
        pager = (CoustomViewPager) findViewById(R.id.fragmentCotainer);
        containers = (RelativeLayout) findViewById(R.id.containers);
        fullscreen = (ImageView) findViewById(R.id.fullscreen);
        room = (TextView) findViewById(R.id.rooms);
        ll_room = (LinearLayout) findViewById(R.id.ll_room);
        onlines = (TextView) findViewById(R.id.online);
        time = (TextView) findViewById(R.id.time);
        camera = (ImageView) findViewById(R.id.end);
        add = (ImageView) findViewById(R.id.add);
        invitation = (LinearLayout) findViewById(R.id.invitation);
        game = (LinearLayout) findViewById(R.id.game);
        pay = (LinearLayout) findViewById(R.id.pay);
        exceptional = (LinearLayout) findViewById(R.id.exceptional);
        addll = (LinearLayout) findViewById(R.id.add_content);
        bg = (ImageView) findViewById(R.id.add_bg);
        lottery_rl = (RelativeLayout) findViewById(R.id.lottery_rl);
        lottery_count = (TextView) findViewById(R.id.lotterycount);
        matchname = (TextView) findViewById(R.id.match_name);
        chat = (EditText) findViewById(R.id.chat);
        tv_noquestion = (TextView) this.findViewById(R.id.tv_noquestion);
        vp_question = (ViewPager) this.findViewById(R.id.vp_question);
        iv_left = (ImageView) this.findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
        iv_right = (ImageView) this.findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);
        but_start = (Button) this.findViewById(R.id.but_start);
        but_start.setOnClickListener(this);

        pager.setScrollble(false);
        pager.setOffscreenPageLimit(2);
            room.setText("房间");
            fullscreen.setVisibility(View.VISIBLE);
            back.setBackgroundResource(R.drawable.finish);

        lottery_rl.setOnClickListener(this);
        invitation.setOnClickListener(this);
        game.setOnClickListener(this);
        pay.setOnClickListener(this);
        exceptional.setOnClickListener(this);
        fullscreenll.setCanPullDown(false);
        ll_room.setOnClickListener(this);
        back.setOnClickListener(this);
        fullscreen.setOnClickListener(this);
        lockscreen.setOnClickListener(this);
        barrage.setOnClickListener(this);
        player.setOnClickListener(this);
        add.setOnClickListener(this);
        addll.setVisibility(View.GONE);
        bg.setOnClickListener(this);
        chat.setOnClickListener(this);
        startPlayer.setOnClickListener(this);
        present.setOnClickListener(this);


        //滑动viewpager导航栏跟着联动
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //处理点击标题滑动到对应的pager页面
                for (int i = 0; i < titleContainer.getChildCount(); i++) {
                    int lable = (int) titleContainer.getChildAt(i).getTag();
                    if (lable == position) {
                        titleContainer.getChildAt(i).findViewById(R.id.line).setVisibility(View.VISIBLE);

                    } else {
                        titleContainer.getChildAt(i).findViewById(R.id.line).setVisibility(View.INVISIBLE);
                    }
                }
                if (position == 0) {
                    bottom_chat.setVisibility(View.VISIBLE);
                } else {
                    bottom_chat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vedioview.getLayoutParams();
        layoutParams.height = screenWidth * 9 / 16;
        vedioview.setLayoutParams(layoutParams);
        //五秒后隐藏
        hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);
        fullscreenAnChor.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        info = new ArrayList<AnchorInfo>();
        fullLottery = new ArrayList<>();

        fullscreenll.setCanPullUp(false);
        fullscreenll.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                isdown = true;
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
        fullscreenAnChor.setScrimColor(Color.TRANSPARENT);
        //drawlayout 关闭监听
        fullscreenAnChor.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        });

        chat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    if (old != -1) {
                        try {
                            LogUtils.e("test", "chat =  " + EmojiReplace.emojiConvert1(chat.getText().toString()));
                            SuperLiveApplication.imApi.sendRoomMsg(info.get(old).getRoomid(), EmojiReplace.emojiConvert1(chat.getText().toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    isSoftKey = false;
                    bg.setVisibility(View.GONE);
                    KeyBoardUtils.closeKeybord(chat, LiveDetailActivity.this);
                    chat.setText("");
                    fragment.setIsScroll(true);

                }

                return false;
            }
        });
        parent.setOnResizeListener(new MyLinearLayout.OnResizeListener() {
            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                if (h < oldh && isSoftKey && !isAdd) {
                    LogUtils.e("===============", "");
                    issoft = true;
                    return;
                }

                if (issoft) {
                    issoft = false;
                    isSoftKey = false;
                    bg.setVisibility(View.GONE);

                }
            }
        });
        chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == motionEvent.ACTION_MOVE) {
                    return false;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    LogUtils.e("===============", "touch" + isSoftKey);
                    if (isSoftKey) {
                        isSoftKey = false;
                        bg.setVisibility(View.GONE);
                        KeyBoardUtils.closeKeybord(chat, LiveDetailActivity.this);
                    } else {

                        isSoftKey = true;
                        if (isAdd) {
                            isAdd = false;
                            addll.setVisibility(View.GONE);
                        }
                        bg.setVisibility(View.VISIBLE);
                        KeyBoardUtils.openKeybord(chat, LiveDetailActivity.this);
                    }
                }

                return false;
            }
        });
        getMatchInfo();

        mediaplayer = new MediaPlayer(playerview,LiveDetailActivity.this,true);

        mediaplayer.setCompletionListener(new MediaPlayer.CompletionListener() {
            @Override
            public void completion() {
                stopPlayer();
            }
        });
        mediaplayer.setErroListener(new MediaPlayer.ErroListener() {
            @Override
            public void onErro() {
                startPlayer.setVisibility(View.GONE);
                isError = true;
            }

            @Override
            public void onStart() {
                startPlayer.setVisibility(View.VISIBLE);
                isError = false;
            }
        });
        mGestureDetector = new GestureDetector(LiveDetailActivity.this, new MyOnGestureListener());

        player.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);

                return false;
            }
        });
        initmatch();
    }
    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF))||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    boolean isanchor=false;
    @Override
    public void onClick(View view) {
        hideHandler.removeCallbacksAndMessages(null);
            switch (view.getId()){
//--------------------------------------------------------------------------------------------------------------------------------
                case R.id.present:
                    if (Preference.zhubo_id.equals(SPUtils.get(LiveDetailActivity.this, Preference.myuser_id, "", Preference.preference))){
                        isanchor=true;
                        new SendPresentAnimal(LiveDetailActivity.this,R.id.chat,"0",isanchor,info.get(0).getRoomid(),matchid).popupwindow_anchoe();

                    }else{
                        isanchor=false;
                        KeyBoardUtils.closeKeybord(chat, LiveDetailActivity.this);
                        new SendPresentAnimal(LiveDetailActivity.this,R.id.chat,"0",isanchor,info.get(0).getRoomid(),matchid).popupWindow_Present();
                    }
                        //参数三: 0 主播  1 蓝色  2红色
                    //参数4:  是否是主播,true代表主播,false代表不是
                    if(isAdd){
                        addClose();
                    }

                    break;
//--------------------------------------------------------------------------------------------------------------------------------
                case R.id.back://返回键的处理
                        if(isFullScreen){
                            smallScreen();
                        }else {
                            toFinish();
                        }
                    break;
                case R.id.ll_room:
                    //主播房间的点击事件处理
                        if(!isOpenAnchor&&anchorView == null) {
                            isOpenAnchor = true;
                            addAnchorListView();
                        }else {
                            isOpenAnchor = false;
                            containers.removeView(anchorView);
                            anchorView = null;
                            hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);
                        }

                    break;
                case R.id.end:
                    break;
                case R.id.fullscreen:
                    //全屏按钮
                    if(isFullScreen){
                        smallScreen();
                    }else {
                        fullScreen();

                    }
                    break;
                case R.id.lottery_rl:
                    //全屏竞猜列表
                    if(fullscreenAnChor.isDrawerOpen(drawerrl)){
                        fullscreenAnChor.closeDrawer(drawerrl);
                        hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);

                    }else{
                        fullscreenAnChor.openDrawer(drawerrl);
                        getLotteryList();
                    }
                    break;
                case R.id.lockscreen:
                    //点击锁的处理
                    if(isLock){
                        isLock = false;
                        hideView(View.VISIBLE);
                        hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);
                        //注册传感器
                        orient.enable();
                        lockscreen.setBackgroundResource(R.drawable.lockscreen);
                    }else{
                        isLock = true;
                        hideView(View.GONE);
                        hideHandler.removeCallbacksAndMessages(null);
                        orient.disable();
                        lockscreen.setBackgroundResource(R.drawable.lock);
                    }
                    break;
                case R.id.barrage:
                    LogUtils.e("-----------",""+mediaplayer.isDanmu());
                    if(mediaplayer.isDanmu()){
                        mediaplayer.setDanmu(false);
                        barrage.setChecked(false);
                    }else{
                        mediaplayer.setDanmu(true);
                        barrage.setChecked(true);
                    }
                    break;
                case R.id.player:
                    //屏幕
                    if(isLock){
                        break;
                    }
                    //点击屏幕隐藏和消失一些view
                    if(top.getVisibility() == View.VISIBLE){
                        hideView(View.GONE);
                    }else {
                        hideView(View.VISIBLE);
                        hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);
                    }
                    //点击屏幕,如果底步题目栏显示,则需要隐藏
                    if (fullbottom.getVisibility()==View.VISIBLE){
                        left_check.setEnabled(true);
                        right_check.setEnabled(true);
                        //精彩列表按钮隐藏
                        lottery_rl.setVisibility(View.VISIBLE);
                        fullbottom.setVisibility(View.GONE);
                        iv_light_left.setVisibility(View.GONE);
                        tv_left_light.setVisibility(View.GONE);
                        iv_light_right.setVisibility(View.GONE);
                        tv_right_light.setVisibility(View.GONE);
                    }
                    break;
                case R.id.add:
                    //加号按钮
                    if(isAdd){
                      addClose();
                    }else {
                       addOpen();
                    }
                    break;
                case R.id.invitation:
                    //邀请好友
                    addClose();
                    sharedialog = new ShareDialog(LiveDetailActivity.this);
                    sharedialog.setQiutanid(qiutanid);
                    sharedialog.setMatchid(matchid);
                    sharedialog.setMatchname(detail.getName());
                    sharedialog.setShareurl(detail.getShareUrl() +"?zb_id=" + matchid +
                            "&room_id=" + Preference.room_id+ "&share_id=" + SPUtils.get(LiveDetailActivity.this,Preference.myuser_id,"",Preference.preference));
                    sharedialog.setIsPrivate(detail.getIsPrivate());
                    sharedialog.show();
                    break;
                case R.id.pay:
                    //支付
                    addClose();
                    Intent intent = new Intent(LiveDetailActivity.this,ChargeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.exceptional:
                    //出题
                    isUpdateScore = false;
                    addClose();
                    permissions();
                    break;
                case R.id.game:
                    //玩游戏点击事件
                    addClose();
                    HashMap hashMap = new HashMap();
                    hashMap.put("userId",(String) SPUtils.get(this, Preference.myuser_id, "", Preference.preference));
                    Log.e("userids",(String) SPUtils.get(this, Preference.myuser_id, "", Preference.preference));
                    hashMap.put("isPrivate","no");
                    MobclickAgent.onEvent(LiveDetailActivity.this, "gamesPoint", hashMap);
                    gamedialog = new GameDialog(LiveDetailActivity.this);
                    gamedialog.show();
                    break;
                case R.id.add_bg:
                    addClose();
                    ll_lottery.setVisibility(View.GONE);
                    break;
                case R.id.chat:
                    if(isSoftKey) {
                        KeyBoardUtils.openKeybord(chat, LiveDetailActivity.this);

                    }else{
                        KeyBoardUtils.closeKeybord(chat, LiveDetailActivity.this);
                    }
                    break;
                case R.id.playerStart:
                    if(info==null||old==-1||info.get(old) == null){
                        break;
                    }
                    if(isPlayer){
                        isPlayer = false;
                        startPlayer.setVisibility(View.VISIBLE);
                        startPlayer.setBackgroundResource(R.drawable.start);
                        mediaplayer.onStop();
                    }else{
                        isPlayer = true;
                        isError = false;
                        mediaplayer.player(info.get(old).getLiveurl(),info.get(old).getUrltype(),info.get(old).getUrljs(),info.get(old).getAgent(),info.get(old).getReg());
                        startPlayer.setVisibility(View.GONE);
                        startPlayer.setBackgroundResource(R.drawable.stop);
                    }
                        break;

                case R.id.left_check:
                    canyuingcai("1",left_text);
                    break;

                case R.id.right_check:
                    canyuingcai("2",right_text);
                    break;
                case R.id.iv_left:
                    //左侧按钮
                    vp_question.setCurrentItem(vp_question.getCurrentItem()-1);
                    break;
                case R.id.iv_right:
                    vp_question.setCurrentItem(vp_question.getCurrentItem()+1);
                    break;
                case R.id.but_start:
                    sendQuestion();
                    ll_lottery.setVisibility(View.GONE);
                    bg.setVisibility(View.GONE);
                    break;
            }
    }
    View.OnClickListener onClicker = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

        }
    };





    SelfLotteryDialog dialog1;
    public  void permissions(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone",Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", Preference.room_id);
        builder.add("token", (String) SPUtils.get(LiveDetailActivity.this, Preference.token, "", Preference.preference));
        Log.e("3333333", (String) SPUtils.get(LiveDetailActivity.this, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils(LiveDetailActivity.this);
        requestUtils.httpPost(Preference.permissions, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                isPermisssion = false;
                exceptional.setEnabled(true);
            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.optString("status"))) {
                        if (!isUpdateScore){
                            if(!isLottery){
                                getQuestion();
                                ll_lottery.setVisibility(View.VISIBLE);
                                addll.setVisibility(View.GONE);
                                bg.setVisibility(View.VISIBLE);
                            }else {
                                dialog1 = new SelfLotteryDialog(LiveDetailActivity.this,info.get(0).getRoomid(),matchid);
                                dialog1.setUserid((String) SPUtils.get(LiveDetailActivity.this,Preference.myuser_id,"",Preference.preference));
                                dialog1.setRoomid(Preference.room_id);
                                dialog1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        isPermisssion = false;
                                    }
                                });
                                dialog1.show();
                            }
                        }else {
                            isPermisssion = false;
                            isUpdateScore = false;
                            UpdateScoreDialog dialog = new UpdateScoreDialog(LiveDetailActivity.this);
                            dialog.setRedname(redname.getText().toString());
                            dialog.setBluename(bluename.getText().toString());
                            dialog.setbifenlift(redcount.getText().toString());
                            dialog.setbifenright(bluecont.getText().toString());
                            dialog.setCancelable(false);
                            dialog.show();
                        }

                    } else {
                        isPermisssion = false;
                        if("_1000".equals(object.optString("status"))){
                            new LoginUtils(LiveDetailActivity.this).reLogin(LiveDetailActivity.this);
                            }
                        Toast.makeText(LiveDetailActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                exceptional.setEnabled(true);
            }
        });
    }
    private int verticalMinDistance = 50;
    private int minVelocity         = 100;

    @Override
    public void fullScren(boolean isFull) {
        if(isFull){
            orient.disable();
        }else{
            orient.enable();
        }
    }

    //回调修改比例
    @Override
    public void changestste(int red, int blue) {
//        matchView(red,blue);
    }

    private class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean flag=true;

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        //1.手势起点的移动事件 2.当前手势点的移动事件 3.每秒x轴方向移动 4.每秒y轴方向移动的
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1!=null && e2!=null){
                if(e1.getX()- e2.getX() > verticalMinDistance && Math.abs(velocityX) >
                        minVelocity && Math.abs(e1.getY()-e2.getY())<200 ) {
                    isListviewScroll =true;
                    if (old!=-1 && (isFullScreen)){
//                        anchor(old);
                       fragment.movetoleft();
                    }
                }else{
                    isListviewScroll = false;
                }
                if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity
                        && Math.abs(e1.getY()-e2.getY())<200){
                    isListviewScroll =true;
                    if (old!=-1 && (isFullScreen)){
//                        anchor(old);
                        fragment.movetoright();
                    }
                }else{
                    isListviewScroll = false;
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //左上正数,右下负数,(Math.abs(_mPosX-_mCurPosX) > 30)
            if ( Math.abs(distanceY)>40 && Math.abs(distanceX)<20){
//                Log.e("手势滑动","上下");
                flag=false;
            }
            else if (Math.abs(distanceX)>40 && Math.abs(distanceY)<20 ){
//                Log.e("手势滑动","左右");
                flag=true;
            }
            return flag;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return flag;
        }
    }
    /**
     * 参与竞猜
     * @param sel
     */
    private void canyuingcai(final String sel,String answer) {
        //参与成功后改变颜色
        this.sel=sel;
        Log.e("参与竞猜:",Preference.room_id+"__"+lotteryid+"__"+answer+"__"+roommoney);
        ConfigUtils.sendLotteryResult(LiveDetailActivity.this, Preference.room_id, lotteryid, answer, roommoney, new ConfigUtils.ResultListener() {
            @Override
            public void result(SendLotteryResult result) {
                //成功后,添加动画,改变背景
                if (sel.equals("1")) {
                    right_check.setEnabled(false);
                    ConfigUtils.fullscrLottery(left_check, full_lever, 0);
                } else if (sel.equals("2")) {
                    left_check.setEnabled(false);
                    ConfigUtils.fullscrLottery(right_check, full_lever, 1);
                }
                //动画,第一次是弹出金币,其余都是弹出次数
                long secondTime = System.currentTimeMillis();
                // 判断每次点击的事件间隔是否符合连击的有效范围,不符合时，有可能是连击的开始，否则就仅仅是单击
                if (secondTime - firstTime <= interval) {
                    ++count;
                    if (sel.equals("1")) {
                        iv_light_left.setVisibility(View.VISIBLE);
                        tv_left_light.setVisibility(View.VISIBLE);
                        tv_left_light.setText("x" + count);
                        iv_light_right.setVisibility(View.GONE);
                        tv_right_light.setVisibility(View.GONE);
                    } else {
                        iv_light_right.setVisibility(View.VISIBLE);
                        tv_right_light.setVisibility(View.VISIBLE);
                        tv_right_light.setText("x" + count);
                        iv_light_left.setVisibility(View.GONE);
                        tv_left_light.setVisibility(View.GONE);
                    }
                } else {
                    count = 1;
                    if (sel.equals("1")) {
                        iv_light_left.setVisibility(View.VISIBLE);
                        tv_left_light.setVisibility(View.VISIBLE);
                        tv_left_light.setText("+" + roommoney);
                        iv_light_right.setVisibility(View.GONE);
                        tv_right_light.setVisibility(View.GONE);
                    } else {
                        iv_light_right.setVisibility(View.VISIBLE);
                        tv_right_light.setVisibility(View.VISIBLE);
                        tv_right_light.setText("+" + roommoney);
                        iv_light_left.setVisibility(View.GONE);
                        tv_left_light.setVisibility(View.GONE);
                    }
                }
                delay();
                firstTime = secondTime;
                // 延迟，用于判断用户的点击操作是否结束
                if (sel.equals("1")) {
                    xx_left_Handler.sendEmptyMessageDelayed(View.GONE, xx_time);
                } else {
                    xx_right_Handler.sendEmptyMessageDelayed(View.GONE, xx_time);
                }
                //我投注的钱
                my_bet_money.setText(result.getBetting());
                //返回我的预计收益
                predict_money.setText(result.getEarnings());
                //左边总计
                left_total_money.setText(result.getLeftgold());
                //右边总计
                right_all_money.setText(result.getRightgold());
            }

            @Override
            public void last(String islast) {
                if (islast.equals("1")) {
                    if (sel.equals("1")) {
                        iv_light_left.setBackgroundResource(R.drawable.budao);
                        iv_light_left.setVisibility(View.VISIBLE);
                        tv_left_light.setVisibility(View.GONE);
                        iv_light_right.setVisibility(View.GONE);
                        tv_right_light.setVisibility(View.GONE);
                    } else if (sel.equals("2")) {
                        iv_light_right.setBackgroundResource(R.drawable.budao);
                        iv_light_right.setVisibility(View.VISIBLE);
                        tv_right_light.setVisibility(View.GONE);
                        iv_light_left.setVisibility(View.GONE);
                        tv_left_light.setVisibility(View.GONE);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iv_light_left.setVisibility(View.GONE);
                            tv_left_light.setVisibility(View.GONE);
                            iv_light_right.setVisibility(View.GONE);
                            tv_right_light.setVisibility(View.GONE);
                        }
                    }, 500);
                }
            }
        });
    }

    // 延迟时间是连击的时间间隔有效范围
    private void delay() {
        if (_task != null)
            _task.cancel();
        _task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                _task_handler.sendMessage(message);
            }
        };
        delayTimer = new Timer();
        delayTimer.schedule(_task, interval);
    }

    //添加主播列表
    public void addAnchorListView(){
        anchorView = LayoutInflater.from(LiveDetailActivity.this).inflate(R.layout.anchor_smalllist, null);
        PullToRefreshLayout prl = (PullToRefreshLayout) anchorView.findViewById(R.id.anchorlist_prl);
        PullableListView listView = (PullableListView) anchorView.findViewById(R.id.anchorlist);
        prl.setCanPullDown(false);
        prl.setCanPullUp(false);
        AnchorListAdapter adapter = new AnchorListAdapter(info,LiveDetailActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fragment.changeAchor(info.get(i), i);
                info.get(i).setIsselector(true);
                fullscreenAnchorname.setText(info.get(i).getAnchorname());
                fullscreenanchorbet.setText("[" + info.get(i).getBetting() + "]");
                fullScreenAnchoronline.setText(info.get(i).getOnline());
                ImageLoader.getInstance().displayImage(Preference.photourl + "200x200" + info.get(i).getHeaderimg(), fullscreenAnchorHead, ImageLoaderOption.optionsHeaderno);

                if (old != -1) {
                    info.get(old).setIsselector(false);
                }
                isOpenAnchor = false;
                containers.removeView(anchorView);
                anchorView = null;
                old = i;
            }
        });

        containers.addView(anchorView);
    }
    //切换小屏
    public void smallScreen(){
        //关闭竞猜
        fullscreenAnChor.closeDrawer(drawerrl);
        hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);
        isFullScreen = false;
        //显示通知栏
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置屏幕竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置视屏的宽高
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vedioview.getLayoutParams();
        layoutParams.height = screenWidth * 9 / 16;
        vedioview.setLayoutParams(layoutParams);
        //底边栏显示
        bottom.setVisibility(View.GONE);;
        //弹幕按钮消失
        barrage.setVisibility(View.GONE);
        //锁屏
        lockscreen.setVisibility(View.GONE);
        //房间
        fullscreenRoom.setVisibility(View.GONE);
        //聊天框
        bottom_chat.setVisibility(View.VISIBLE);
        //竞猜标签
        lottery_rl.setVisibility(View.GONE);
        //全屏比赛名
        fullscreenname.setVisibility(View.GONE);
        //半屏比赛名
        matchname.setVisibility(View.VISIBLE);
        //全屏底部布局
        fullbottom.setVisibility(View.GONE);
        fullscreen.setVisibility(View.VISIBLE);
        left_check.setEnabled(true);
        right_check.setEnabled(true);
        if(isShowMatch) {
            matchs.setVisibility(View.VISIBLE);
        }
        //关闭弹幕
        if(mediaplayer.isDanmu()) {
            mediaplayer.setDanmu(false);
            barrage.setChecked(false);
        }
        if (postion == 0) {
            bottom_chat.setVisibility(View.VISIBLE);
        } else {
            bottom_chat.setVisibility(View.GONE);
        }
    }
    //切换大屏
    public void fullScreen(){
        isFullScreen = true;
        //--------------------------------------------------------------------------------------------------------------------------------
        if(info.size()!=0){
            new SendPresentAnimal(LiveDetailActivity.this,R.id.chat,"0",isanchor,info.get(0).getRoomid(),matchid).setNoXuanzhuan();
        }
        hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //设置视屏布局的宽高
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vedioview.getLayoutParams();
        layoutParams.height = screenWidth;
        vedioview.setLayoutParams(layoutParams);
        //去掉通知栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底边栏
        bottom.setVisibility(View.GONE);
        matchs.setVisibility(View.GONE);

        //弹幕按钮显示
        barrage.setVisibility(View.VISIBLE);
        //锁屏
        lockscreen.setVisibility(View.VISIBLE);
        //房间
        fullscreenRoom.setVisibility(View.VISIBLE);
        //聊天框
        bottom_chat.setVisibility(View.GONE);
        //竞猜标签
        lottery_rl.setVisibility(View.VISIBLE);
        //全屏比赛名
        fullscreenname.setVisibility(View.VISIBLE);
        //半屏比赛名
        matchname.setVisibility(View.GONE);
        fullscreen.setVisibility(View.GONE);
        fullscreenanchor();
        if(mediaplayer.isDanmu()) {
            mediaplayer.setDanmu(false);
            barrage.setChecked(false);
        }
        if(!(boolean)SPUtils.get(LiveDetailActivity.this,"isFullScreenSliding",false,Preference.preference)){

            firstguide("isFullScreenSliding");
        }
        if(dialog1!=null){
            dialog1.dismiss();
        }
        if(sharedialog!=null){
            sharedialog.dismiss();
        }
        if(gamedialog!=null){
            gamedialog.dismiss();
        }
     // getLotteryList();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(orient!=null){
            orient.disable();
        }
        hideHandler.removeCallbacksAndMessages(null);

        if(old!=-1){
            SuperLiveApplication.imApi.leaveRoom(info.get(old).getRoomid());
        }
        if(mediaplayer!=null){
            mediaplayer.destory();
        }
        fragment.ondestory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("pppppppp", "onResume");
        if(gamedialog!=null){
            gamedialog.refresh();
        }

        if(mediaplayer!=null&&isPlayer&&!isError&&info!=null&&old!=-1&&!(boolean)SPUtils.get(LiveDetailActivity.this,"lotterydilog",false,Preference.preference)){
            mediaplayer.player(info.get(old).getLiveurl(),info.get(old).getUrltype(),info.get(old).getUrljs(),info.get(old).getAgent(),info.get(old).getReg());
            Log.e("播放地址",info.get(old).getLiveurl());
            startPlayer();
        }
        if(orient!=null) {
            orient.enable();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaplayer!=null&&isPlayer&&!isError&&!(boolean)SPUtils.get(LiveDetailActivity.this,"lotterydilog",false,Preference.preference)){
            mediaplayer.onStop();
            startPlayer.setVisibility(View.VISIBLE);
            startPlayer.setBackgroundResource(R.drawable.start);
        }
        KeyboardUtil.closeKeybord(chat, LiveDetailActivity.this);
        if(orient!=null) {
            orient.disable();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    //开始播放器
    public void startPlayer(){
        isPlayer = true;
        startPlayer.setVisibility(View.GONE);
        startPlayer.setBackgroundResource(R.drawable.stop);
    }
    public void  stopPlayer(){
        isPlayer = false;
        startPlayer.setVisibility(View.VISIBLE);
        startPlayer.setBackgroundResource(R.drawable.start);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            if(isFullScreen){
                smallScreen();
            }else if(isOpenAnchor){
                isOpenAnchor = false;
                containers.removeView(anchorView);
                anchorView = null;
                hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);


            }else {
                toFinish();
            }
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
    public  void hideView(int visible){
        top.setVisibility(visible);
        if(!isFullScreen) {
            bottom.setVisibility(View.GONE);
            if(isShowMatch) {
                matchs.setVisibility(visible);
            }
        }
        if(isPlayer&&!isError){
            startPlayer.setVisibility(visible);

        }
    }
    public void fullscreenanchor(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK&&requestCode == LIVE_SOURCE){
            hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);
        }
    }
    //打开加号
    public void addOpen(){
        isSoftKey = false;
        KeyboardUtil.closeKeybord(chat, LiveDetailActivity.this);
        isAdd = true;
        addll.setVisibility(View.VISIBLE);
        bg.setVisibility(View.VISIBLE);
        translateAnimationUp(bottom_chat);
    }
    //关闭加号
    public void addClose(){
        isAdd = false;
        addll.setVisibility(View.GONE);
        bg.setVisibility(View.GONE);
        isSoftKey = false;
        KeyboardUtil.closeKeybord(chat, LiveDetailActivity.this);

    }
    private void translateAnimationUp(final View view) {
        //从底部进入
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.v4_chat_in_from_bottom);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                bg.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }
    //得到比赛数据
    public void getMatchInfo(){
        if(matchid == null){
            matchid = "";
        }
        if(roomid == null){
            roomid ="";
        }

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("zb_id", matchid);
        builder.add("room_id", roomid);
        builder.add("token", (String) SPUtils.get(LiveDetailActivity.this, Preference.token, "", Preference.preference));
        Log.e("详情参数",matchid+"__"+roomid);
        HttpRequestUtils requestUtils = new HttpRequestUtils(LiveDetailActivity.this,loading,false);
        requestUtils.httpPost(Preference.LiveDetail, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("直播详情参数错误", json);
                try {
                    JSONObject object = new JSONObject(json);
                    boolean iserro =  parse(object);
                    if(iserro){
                        return;
                    }
                    LogUtils.e("ssssss======", "/////////");
                    if ("1".equals(detail.getMatch_mode()) || "2".equals(detail.getMatch_mode())) {
                        titlename.add("赛况");
                    }
                    dui_1.setText(detail.getTeam1());
                    dui_2.setText(detail.getTeam2());
                    if (!detail.getColor1().equals("") && !detail.getColor2().equals("")) {
                        tv_team1color.setBackgroundColor(Color.parseColor(detail.getColor1()));
                        tv_team2color.setBackgroundColor(Color.parseColor(detail.getColor2()));
                    }
                    //添加title选项
                    for (int j = 0; j < titlename.size(); j++) {
                        final View title = getLayoutInflater().inflate(R.layout.fragment_livelist_title, null);
                        TextView tv = (TextView) title.findViewById(R.id.tv);
                        tv.setTextColor(getResources().getColor(R.color.color_black));
                        tv.setTextSize(14);
                        View line = title.findViewById(R.id.line);
                        line.setBackgroundColor(getResources().getColor(R.color.main));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1;
                        title.setLayoutParams(params);
                        tv.setText(titlename.get(j));
                        title.setTag(j);
                        final int finalJ = j;
                        if (j == 0) {
                            line.setVisibility(View.VISIBLE);
                        } else {
                            line.setVisibility(View.INVISIBLE);
                        }
                        //设置每个title的点击事件
                        title.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //处理点击导航栏滑动到对应的pager页面
                                for (int i = 0; i < titleContainer.getChildCount(); i++) {
                                    int lable = (int) titleContainer.getChildAt(i).getTag();

                                    if (lable == finalJ) {
                                        titleContainer.getChildAt(i).findViewById(R.id.line).setVisibility(View.VISIBLE);
                                        postion = i;
                                        pager.setCurrentItem(i);
                                        if (i == 2) {
                                            if (isActionShow) {
                                                liveactionFragment.getDate();
                                                Log.e("refreshAction", "true");
                                            }
                                            isActionShow = true;
                                        } else {
                                            isActionShow = false;
                                        }
                                    } else {
                                        titleContainer.getChildAt(i).findViewById(R.id.line).setVisibility(View.INVISIBLE);
                                    }
                                }
                                if (finalJ == 1) {
                                    lotteryFragment.getLotteryList("");
                                }
                                if (finalJ == 2) {
                                    liveactionFragment.refresh();
                                }
                            }
                        });
                        titleContainer.addView(title);

                    }

                    if ("1".equals(detail.getMatch_mode()) || "2".equals(detail.getMatch_mode())) {
                        liveactionFragment = new LiveactionFragment();
                        Bundle live = new Bundle();
                        live.putString("matchid", matchid);
                        live.putString("result", detail.getResult());
                        liveactionFragment.setArguments(live);
                        fragments.add(liveactionFragment);
                    }
                    AttentionFansViewPagerAdapter adapter = new AttentionFansViewPagerAdapter(getSupportFragmentManager(), fragments);
                    pager.setAdapter(adapter);
                    if (info.size() > 0) {
                        if (defaults == -1) {
                            defaults = 0;
                        }
                        LogUtils.e("ssssssss", "" + info.get(defaults) + "__" + defaults);
                        info.get(defaults).setIsselector(true);
                        fragment.changeAchor(info.get(defaults), defaults);
                        if (isShowMatch) {
                            String[] ss = info.get(defaults).getMatch().split(",");
                            rednum.setText(ss[0]);
                            bluenum.setText(ss[1]);
                        }
                        fullscreenAnchorname.setText(info.get(defaults).getAnchorname());
                        fullscreenanchorbet.setText("[" + info.get(defaults).getBetting() + "]");
                        fullScreenAnchoronline.setText(info.get(defaults).getOnline());
                        ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + info.get(defaults).getHeaderimg(), fullscreenAnchorHead, ImageLoaderOption.optionsHeaderno);
                        fragment.anchorSize(info.size());
                        old = defaults;
                        isPlayer = true;
                        isError = false;
                        mediaplayer.player(info.get(old).getLiveurl(), info.get(old).getUrltype(), info.get(old).getUrljs(), info.get(old).getAgent(), info.get(old).getReg());
                        startPlayer.setVisibility(View.GONE);
                        startPlayer.setBackgroundResource(R.drawable.stop);
                        if ("2".equals(info.get(defaults).getRoomtype())) {
                            isLottery = true;
                        } else {
                            isLottery = false;
                        }
                    }


                    matchname.setText(detail.getName());
                    fullscreenname.setText(detail.getName());
                    onlines.setText(detail.getOnline());
                    if ("1".equals(detail.getIsPrivate())) {
                        llanchor.setVisibility(View.GONE);
                    } else {
                        llanchor.setVisibility(View.VISIBLE);
                    }

                    //判断是否是欧洲杯签到,执行签到
//                    if (detail.getCat_league().equals("欧洲杯") && detail.getCat_type().equals("足球")) {
//                        Sign(matchid);
//                    }
                    //刷新比分
                    SuperLiveApplication.imApi.setMatchListener(new ImApi.Match() {
                        @Override
                        public void match(boolean b, List<LiveMatchInfoJavabean> infos) {
                            for (int i = 0; i < infos.size(); i++) {
                                if (matchid.equals(infos.get(i).getMatchId())) {
                                    if (b) {
                                        if (!detail.getTeam1().equals("") && !detail.getTeam2().equals("")) {
//                                            re_bifen_bu.setVisibility(View.VISIBLE);
//                                            iv_dui_1.setText(infos.get(i).getScore().split(" - ")[0]);
//                                            iv_dui_2.setText(infos.get(i).getScore().split(" - ")[1]);
//                                            add_time.setText(infos.get(i).getStating());
                                            rednum.setText(infos.get(i).getScore().split(" - ")[0]);
                                            bluenum.setText(infos.get(i).getScore().split(" - ")[1]);
                                            if (!TextUtils.isEmpty(infos.get(i).getStating())) {
                                                matchstate.setVisibility(View.VISIBLE);
                                                matchstate.setText(infos.get(i).getStating());
                                            } else {
                                                matchstate.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                    if (liveactionFragment != null) {
                                        liveactionFragment.setScore(infos.get(i).getScore(), infos.get(i).getStating());
                                        Log.e("赛况", infos.get(i).getScore() + "__" + infos.get(i).getStating());
                                    }
                                }
                            }
                        }
                    });
                    orient.enable(); //开始监听
                    if (!TextUtils.isEmpty(qid)) {
                        Intent intent = new Intent(LiveDetailActivity.this, LotteryDetailsActivity.class);
                        intent.putExtra("qid", qid);
                        intent.putExtra("roomid", roomid);
                        intent.putExtra("anchorid", Preference.zhubo_id);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public boolean parse(JSONObject object) {
        if("_0000".equals(object.optString("status"))) {
            JSONObject object1 = object.optJSONObject("list");
            JSONObject match = object1.optJSONObject("match");
            detail = new LiveDetail();
            detail.setName(match.optString("match_name"));
            SPUtils.put(LiveDetailActivity.this, Preference.matchname, detail.getName(), Preference.preference);
            detail.setResult(match.optString("result"));
            detail.setTeam1(match.optString("team1"));
            detail.setTeam2(match.optString("team2"));
            detail.setColor1(match.optString("color1"));
            detail.setColor2(match.optString("color2"));
            detail.setOnline(match.optString("user_total"));
            detail.setShareUrl(match.optString("share_url"));
            detail.setIsPrivate(match.optString("is_private"));
            detail.setQiutanid(match.optString("qiutan_id"));
            detail.setCat_league(match.optString("cat_league"));
            detail.setLeague_name(match.optString("league_name"));
            detail.setCat_type(match.optString("cat_type"));
            detail.setMatch_statu(match.optString("match_status"));
            detail.setMatch_mode(match.optString("match_mode"));

            if(!TextUtils.isEmpty(match.optString("team1"))&&!TextUtils.isEmpty(match.optString("team2"))){
                isShow = true;
                isShowMatch = true;
                redname.setText(match.optString("team1"));
                bluename.setText(match.optString("team2"));
                red_num.setText(match.optString("gift_of_red"));
                blue_num.setText(match.optString("gift_of_blue"));
                if(!TextUtils.isEmpty(match.optString("gift_of_red"))&&!TextUtils.isEmpty(match.optString("gift_of_blue"))) {
                    int red = Integer.parseInt(match.optString("gift_of_red"));
                    int blue = Integer.parseInt(match.optString("gift_of_blue"));
                    matchView(red,blue);
                }

            }else {
                isShowMatch = false;
                matchs.setVisibility(View.GONE);
            }
            JSONArray rooms = object1.optJSONArray("room");
            if(rooms!=null){
                for(int i = 0;i<rooms.length();i++){
                    JSONObject object2 = rooms.optJSONObject(i);
                    AnchorInfo item = new AnchorInfo();
                    item.setRoomid(object2.optString("room_id"));
                    item.setAnchorid(object2.optString("anchor_id"));
                    item.setHeaderimg(object2.optString("image"));
                    item.setOnline(object2.optString("user_count"));
                    item.setAnchorname(object2.optString("anchor_name"));
                    item.setBetting(object2.optString("bet"));
                    item.setIsFirst(object2.optString("is_default"));
                    item.setLiveurl(object2.optString("live_url"));
                    item.setUrltype(object2.optString("url_type"));
                    item.setUrljs(object2.optString("url_js"));
                    item.setLevel(object2.optString("level"));
                    item.setReg(object2.optString("url_reg"));
                    item.setRoomtype(object2.optString("room_type"));
                    item.setReplace(object2.optString("url_replace"));
                    item.setMatch(object2.optString("scoreboard"));
                    item.setOnline(object2.optString("user_count"));
                    if("1".equals(qiutanid)){
                    if("1".equals(object2.optString("is_default"))){
                        defaults = i;
                    }
                    }else{
                        if(item.getRoomid().equals(SPUtils.get(LiveDetailActivity.this,matchid,"",Preference.matchroom))&&isLiveList){
                            defaults = i;
                        }else{
                            if(defaults == -1){
                                if("1".equals(object2.optString("is_default"))){
                                    defaults = i;
                                }
                            }
                        }
                    }
                    info.add(item);
                }
            }
            return false;
        }else{
            if("_3001".equals(object.optString("status"))){
                orient.disable();
                CloseDialog dialog = new CloseDialog(LiveDetailActivity.this);
                dialog.setCancelable(false);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });
                dialog.show();
            }
            return true;
        }
    }
    //得到全屏竞猜数据
    public void getLotteryList(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", Preference.room_id);
        builder.add("token", (String) SPUtils.get(LiveDetailActivity.this, Preference.token, "", Preference.preference));
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils(LiveDetailActivity.this);
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
                                lottery_count.setVisibility(View.VISIBLE);
                                lottery_count.setText("" + fullLottery.size());
                            } else {
                                lottery_count.setVisibility(View.GONE);
                            }
                            if(fullLottery.size()==0){
                                empty.setVisibility(View.VISIBLE);
                            }else {
                                LogUtils.e("()()()()()()()()","");
                                empty.setVisibility(View.GONE);
                            }
                            lotteryAdapter = new FullScreenLottery(fullLottery, LiveDetailActivity.this);
                            fullsrcrenanchorList.setAdapter(lotteryAdapter);
                            lotteryAdapter.notifyDataSetChanged();
                        } else {
                            lottery_count.setVisibility(View.GONE);
                        }


                    } else {
                        Toast.makeText(LiveDetailActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
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
    //切换主播的接口
    @Override
    public void anchor(int position) {

        fragment.changeAchor(info.get(position), position);
        fragment.anchorSize(info.size());
        info.get(position).setIsselector(true);
        isOpenAnchor = false;
        containers.removeView(anchorView);
        anchorView = null;
        hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);;
        if(old!=-1) {
            info.get(old).setIsselector(false);
        }
        if("2".equals(info.get(position).getRoomtype())){
            isLottery = true;
        }else{
            isLottery = false;
        }
        //填充全屏主播信息
        fullscreenAnchorname.setText(info.get(position).getAnchorname());
        fullscreenanchorbet.setText("[" + info.get(position).getBetting() + "]");
        fullScreenAnchoronline.setText(info.get(position).getOnline());
        ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + info.get(position).getHeaderimg(), fullscreenAnchorHead, ImageLoaderOption.optionsHeaderno);

        old = position;
        if(info!=null&&old!=-1&&info.get(old)!=null) {
            mediaplayer.player(info.get(old).getLiveurl(), info.get(old).getUrltype(), info.get(old).getUrljs(), info.get(old).getAgent(), info.get(old).getReg());
            startPlayer();
        }
        info.get(position).setIsselector(true);
    }
    //单聊数据
    @Override
    public void Messagelist(String from, String body, String type2) {

    }

    @Override
    public void chatMessage(String from, String body) {

    }
    //群聊数据接收
    @Override
    public void groupMessage(ChatInfo info) {
        fragment.message(info);
        if ("11".equals(info.getType())&&isShowMatch) {
            matchView(Integer.parseInt(info.getRed()), Integer.parseInt(info.getBlue()));
            blue_num.setText(info.getBlue());
            red_num.setText(info.getRed());
        }
            if (!TextUtils.isEmpty(info.getType())) {
                if ("1".equals(info.getType()) || "2".equals(info.getType())) {
                    LogUtils.e("sssssss", "sdddddddddd");
                    mediaplayer.addDanmu(info.getContent(), false);
                }
                if ("8".equals(info.getType())) {
                    mediaplayer.addDanmu(info.getContent(), true);
                }
            }
            //接收到新的竞猜题,需要将数据填充到底步,访问竞猜详情接口拿到其余数据
            if ((("9".equals(info.getType()) || "4".equals(info.getType())) && (fullbottom.getVisibility() == View.GONE) && isFullScreen)) {
                Log.e("全屏问题id", info.getLotteryid());
                //如果右边精彩列表弹框显示则隐藏,按钮也隐藏
                if (fullscreenAnChor.isDrawerOpen(drawerrl)) {
                    //竞猜列表隐藏
                    fullscreenAnChor.closeDrawer(drawerrl);
                    hideHandler.sendEmptyMessageDelayed(View.GONE, hideTime);
                }
                //精彩列表按钮隐藏
                lottery_rl.setVisibility(View.GONE);
                if ("4".equals(info.getType())) {
                    if (fullbottom.getVisibility() != View.VISIBLE) {
                        //获取竞猜题目详情数据
                        getLotteryMesage(info.getLotteryid(), "4");
                    }
                } else {
                    if (fullbottom.getVisibility() != View.VISIBLE) {
                        //获取竞猜题目详情数据
                        getLotteryMesage(info.getLotteryid(), "9");
                    }
                }
                left_check.setOnClickListener(this);
                right_check.setOnClickListener(this);
            }


    }
    @Override
    public void statusFormIm(int status) {
        fragment.joinRoom(status);
    }

    @Override
    public void redReceive(redInfo info) {
        if (!isFullScreen){
            infos.add(info);
            xunhuan();
        }
    }
    public void xunhuan(){
        Log.e("xunhuan","111111"+isDialogShow);
        for (int i = 0;i<infos.size();i++){
                if (!isDialogShow){
                    showRedDialog(infos.get(i));
                    infos.remove(i);
                }
        }
    }
    //
    public void showRedDialog(redInfo info){
        if (null == info.getQuestion_title()||"".equals(info.getQuestion_title())){
            RedDialog redDialog = new RedDialog(LiveDetailActivity.this,info);
            redDialog.setCancelable(false);
            orient.disable();
            redDialog.show();
            isDialogShow = true;
            redDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isDialogShow = false;
                    orient.enable();
                    xunhuan();
                }
            });
        }else {
            LotteryRedDialog lotteryRedDialog = new LotteryRedDialog(LiveDetailActivity.this,info);
            orient.disable();
            lotteryRedDialog.show();
            lotteryRedDialog.setCancelable(false);
            isDialogShow = true;
            lotteryRedDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isDialogShow = false;
                    orient.enable();
                    xunhuan();
                }
            });
        }

    }

    /**
     * 获取竞猜详情页数据
     * @param lotteryid
     */
    private void getLotteryMesage(String lotteryid, final String type) {
        this.lotteryid=lotteryid;
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", Preference.room_id);
        builder.add("que_id", lotteryid);
        builder.add("token", (String) SPUtils.get(LiveDetailActivity.this, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils(this);
        requestUtils.httpPost(Preference.LotteryDetail, builder, new HttpRequestUtils.ResRultListener() {

            @Override
            public void onFailure(IOException e) {
                Toast.makeText(LiveDetailActivity.this, "访问网络错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {
                Log.e("获取竞猜详情页数据参数错误", json);
                Gson json2 = new Gson();
                lotteryDetailInfo lotinfo = json2.fromJson(json, lotteryDetailInfo.class);
                if (lotinfo.getStatus().equals("_0000")) {
                    left_answer.setText(lotinfo.getQue_info().getOption_left());
                    left_total_money.setText(lotinfo.getQue_info().getTotal_left());
                    right_answer.setText(lotinfo.getQue_info().getOption_right());
                    right_all_money.setText(lotinfo.getQue_info().getTotal_right());
                    room_money.setText("[每注" + lotinfo.getQue_info().getRoom_bet());
                    my_bet_money.setText(lotinfo.getQue_info().getMy_option_money());
                    predict_money.setText(lotinfo.getQue_info().getPredict_money());
                    lottery_title.setText(lotinfo.getQue_info().getContent());
                    if (Integer.parseInt(lotinfo.getQue_info().getRoom_bet()) <= 200) {
                        full_lever = "1";
                        if (lotinfo.getQue_info().getMy_option().equals(lotinfo.getQue_info().getOption_left())) {//选择左边
                            left_check.setBackgroundResource(R.drawable.fulllottery_yellow_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_normal_right);
                        } else if (lotinfo.getQue_info().getMy_option().equals(lotinfo.getQue_info().getOption_right())) {//选择右边
                            left_check.setBackgroundResource(R.drawable.fulllottery_normal_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_yellow_right);
                        } else {
                            left_check.setBackgroundResource(R.drawable.fulllottery_normal_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_normal_right);
                        }

                    } else if (Integer.parseInt(lotinfo.getQue_info().getRoom_bet()) <= 400) {
                        full_lever = "2";
                        if (lotinfo.getQue_info().getMy_option().equals(lotinfo.getQue_info().getOption_left())) {//选择左边
                            left_check.setBackgroundResource(R.drawable.fulllottery_red_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_red_nopress_right);
                        } else if (lotinfo.getQue_info().getMy_option().equals(lotinfo.getQue_info().getOption_right())) {//选择右边
                            left_check.setBackgroundResource(R.drawable.fulllottery_red_nopress_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_red_right);
                        } else {
                            left_check.setBackgroundResource(R.drawable.fulllottery_red_nopress_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_red_nopress_right);
                        }
                    } else {
                        full_lever = "3";
                        if (lotinfo.getQue_info().getMy_option().equals(lotinfo.getQue_info().getOption_left())) {//选择左边
                            left_check.setBackgroundResource(R.drawable.fulllottery_lightyellow_press_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_lightyellow_right);
                        } else if (lotinfo.getQue_info().getMy_option().equals(lotinfo.getQue_info().getOption_right())) {//选择右边
                            left_check.setBackgroundResource(R.drawable.fulllottery_lightyellow_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_lightyellow_press_right);
                        } else {
                            left_check.setBackgroundResource(R.drawable.fulllottery_lightyellow_left);
                            right_check.setBackgroundResource(R.drawable.fulllottery_lightyellow_right);
                        }
                    }
                    roommoney = lotinfo.getQue_info().getRoom_bet();
                    left_text = lotinfo.getQue_info().getOption_left();
                    right_text = lotinfo.getQue_info().getOption_right();
                    //显示底步题目栏
                    fullbottom.setVisibility(View.VISIBLE);
                    if (type.equals("4")) {//显示倒计时
                        countTime.setVisibility(View.VISIBLE);
                        CountTime(lotinfo.getQue_info().getStop_time_s());
                    } else {
                        //隐藏倒计时
                        countTime.setVisibility(View.INVISIBLE);
                    }
                } else if (lotinfo.getStatus().equals("_1000")) {
                    new LoginUtils(LiveDetailActivity.this).reLogin(LiveDetailActivity.this);
                    Toast.makeText(LiveDetailActivity.this, lotinfo.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LiveDetailActivity.this, lotinfo.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CountTime(String start_time_s) {
        long l = Long.parseLong(start_time_s);
        this.endTime = l;
        new Thread() {
            public void run() {
                try {
                    TimeZone.setDefault(TimeZone.getTimeZone("GMT+8")); // 时区设置
                    URL url = new URL(Preference.timeUrl);// 取得资源对象
                    URLConnection uc = url.openConnection();// 生成连接对象
                    uc.connect(); // 发出连接
                    long timestr = uc.getDate(); // 取得网站日期时间（时间戳）
                    Message msg = new Message();
                    msg.obj = timestr;
                    counts_handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //在线人数
    @Override
    public void online(String count,String roomid) {
            onlines.setText("在线" + count + "人");
        fragment.online(count,roomid);
    }
    //聊天室里面投注排行数据
    @Override
    public void roomRankTop(String roomid,List<RoomBank> list) {
        LogUtils.e("+++++++++++++++", "*************");
        fragment.roomTOp10(roomid,list);
    }
    public void firstguide(String key){
//        GuideSlidingDialog dialog = new GuideSlidingDialog(LiveDetailActivity.this);
//        dialog.setKey(key);
//        dialog.show();
    }

    public void toFinish(){
        if (isLottery){
            lotteryFragment.getLotteryList("hasOpend");
        }else {
            finish();
        }


//        if (lotteryFragment.hasOpend()){
//            LeaveOutRoomDialog outRoomDialog = new LeaveOutRoomDialog(LiveDetailActivity.this);
//            outRoomDialog.show();
//        }else {
//            finish();
//        }
    }

    //比分牌
    private RelativeLayout matchs;
    private ImageView red_praise;
    private ImageView blue_praise;
    private TextView redname;
    private TextView bluename;
    private TextView rednum;
    private TextView bluenum;
    private TextView redcount;
    private TextView bluecont;
    private LinearLayout updatenum;
    private TextView red_num;
    private TextView blue_num;
    private TextView matchstate;
    public void initmatch(){
        matchs = (RelativeLayout) findViewById(R.id.match_num);
        red_praise = (ImageView) findViewById(R.id.red_praise);
        blue_praise = (ImageView) findViewById(R.id.blue_praise);
        redname = (TextView) findViewById(R.id.redname);
        bluename = (TextView) findViewById(R.id.bluename);
        rednum = (TextView) findViewById(R.id.red_num);
        bluenum = (TextView) findViewById(R.id.blue_num);
        redcount = (TextView) findViewById(R.id.red_count);
        bluecont = (TextView) findViewById(R.id.blue_count);
        updatenum = (LinearLayout) findViewById(R.id.update_num);
        red_num = (TextView) findViewById(R.id.red_tv_num);
        blue_num = (TextView) findViewById(R.id.blue_tv_num);
        matchstate = (TextView) findViewById(R.id.match_state);
        matchs.setVisibility(View.GONE);
        matchstate.setVisibility(View.GONE);
        //主播
            red_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isAdd){
                        addClose();
                    }
                    if (Preference.zhubo_id.equals(SPUtils.get(LiveDetailActivity.this, Preference.myuser_id, "", Preference.preference))){
                        isanchor=true;
//                        new SendPresentAnimal(LiveDetailActivity.this,R.id.chat,"1",isanchor,info.get(0).getRoomid(),matchid).popupwindow_anchoe();
                        Toast.makeText(LiveDetailActivity.this, "作为主播，不支持给球队送礼物", Toast.LENGTH_SHORT).show();
                    }else{
                        isanchor=false;
                        KeyBoardUtils.closeKeybord(chat, LiveDetailActivity.this);
                        new SendPresentAnimal(LiveDetailActivity.this,R.id.chat,"1",isanchor,info.get(0).getRoomid(),matchid).popupWindow_Present();
                    }

                }
            });
            blue_praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isAdd){
                        addClose();
                    }
                    if (Preference.zhubo_id.equals(SPUtils.get(LiveDetailActivity.this, Preference.myuser_id, "", Preference.preference))){
                        isanchor=true;
//                        new SendPresentAnimal(LiveDetailActivity.this,R.id.chat,"2",isanchor,info.get(0).getRoomid(),matchid).popupwindow_anchoe();
                        Toast.makeText(LiveDetailActivity.this, "作为主播，不支持给球队送礼物", Toast.LENGTH_SHORT).show();
                    }else{
                        isanchor=false;
                        KeyBoardUtils.closeKeybord(chat, LiveDetailActivity.this);
                        new SendPresentAnimal(LiveDetailActivity.this,R.id.chat,"2",isanchor,info.get(0).getRoomid(),matchid).popupWindow_Present();
                    }

                }
            });

        //跟新比分
        updatenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断是否是主播
                if (Preference.zhubo_id.equals(SPUtils.get(LiveDetailActivity.this, Preference.myuser_id, "", Preference.preference))) {
                    UpdateScoreDialog dialog = new UpdateScoreDialog(LiveDetailActivity.this);
                    dialog.setRedname(redname.getText().toString());
                    dialog.setBluename(bluename.getText().toString());
                    dialog.setCancelable(false);
                    dialog.show();
                } else {
                    isUpdateScore = true;
                    //判断是否具有出题权限
                    if (!isPermisssion) {
                        isPermisssion = true;
                        permissions();
                    }
                }
            }
        });
    }
    public  void matchView(int red,int blue){
        float redcon;
        float bluecon;
        if(red==0&&blue==0) {
            redcon = 0;
            bluecon = 0;
        }else {
            redcon = (float) (red * 1.0 / (red + blue));
            bluecon = (float) (blue * 1.0 / (red + blue));
        }
        int layout = (screenWidth-ConfigUtils.dip2px(LiveDetailActivity.this,176))/2;
        LogUtils.e("ssssssssssss*", (screenWidth - ConfigUtils.dip2px(LiveDetailActivity.this, 176)) + "");
        LogUtils.e("ssssssssssss*", layout + "");
        if(isShowMatch&&!isFullScreen) {
            matchs.setVisibility(View.VISIBLE);
        }else {
            matchs.setVisibility(View.GONE);
        }
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((int) (layout*redcon),ConfigUtils.dip2px(LiveDetailActivity.this,11));
        redcount.setLayoutParams(parms);
        redcount.setPadding(8, 0, 0, 0);
        redcount.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams((int) (layout*bluecon),ConfigUtils.dip2px(LiveDetailActivity.this,11));
        redcount.setPadding(0, 0, 8, 0);
        redcount.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        bluecont.setLayoutParams(parm);
    }

    //获取题目列表
    public void getQuestion(){
        questions.clear();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("match_id",matchid)
                .add("room_id",Preference.room_id)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.GetQuestion, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                showToast("数据访问失败");
            }

            @Override
            public void onSuccess(String json) {
                Log.e("question", json);
                Gson gson = new Gson();
                Type type = new TypeToken<QuestionBase>() {
                }.getType();
                QuestionBase base = gson.fromJson(json, type);
                if (base.getStatus().equals("_0000")) {
                    questions.clear();
                    questions.addAll(base.getList());
                    Log.e("qusize", questions.size() + "");
                    tv_noquestion.setVisibility(View.VISIBLE);
                    vp_question.setVisibility(View.GONE);
                    setViews(questions);
                } else {
                    showToast(base.getMessage());
                }
            }
        });
    }
    //添加系统题目
    public void setViews(List<Question> qs){
        views.clear();
        Log.e("qusize", views.size() + "");
        if (questions.size()>0){
            tv_noquestion.setVisibility(View.GONE);
            vp_question.setVisibility(View.VISIBLE);
            for (int i = 0;i<questions.size();i++){
                View view = LayoutInflater.from(this).inflate(R.layout.viewpager_topic,null);
                ((TextView)view.findViewById(R.id.tv_num)).setText((i + 1) + "/" + questions.size());
                ((TextView)view.findViewById(R.id.tv_content)).setText(questions.get(i).getTitle());
                ((TextView)view.findViewById(R.id.tv_time)).setText(questions.get(i).getStop_time()+"截止");
                views.add(view);
            }
            Log.e("qusize", views.size() + "");
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(views);
            vp_question.setAdapter(viewPagerAdapter);
            viewPagerAdapter.notifyDataSetChanged();
        }else {
            tv_noquestion.setVisibility(View.VISIBLE);
            vp_question.setVisibility(View.GONE);
        }

    }
    //主播出题
    public void sendQuestion(){
//        Log.e("vpsize",vp_question.getCurrentItem()+"");
        if (questions.size()==0){
            showToast("暂无题目");
            return;
        }
        Log.e("pageritem",""+questions.get(vp_question.getCurrentItem()));
        Log.e("pageritem",questions.size()+"");
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("userid", (String) SPUtils.get(this, Preference.myuser_id, "", Preference.preference))
                .add("room_id", roomid)
                .add("gid", questions.get(vp_question.getCurrentItem()).getGid())
                .build();
        Log.e("userid", (String) SPUtils.get(this, Preference.myuser_id, "", Preference.preference));
        new HttpRequestUtils(this).httpPost(Preference.UseGuess, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("sendQuestion", json);
                if (!ConfigUtils.isJsonFormat(json)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<SendQuestion>() {
                }.getType();
                SendQuestion question = gson.fromJson(json, type);
                showToast(question.getMessage());
            }
        });

    }

}
