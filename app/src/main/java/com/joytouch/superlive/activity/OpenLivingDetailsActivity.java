package com.joytouch.superlive.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjzhibo.im.ImApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.AttentionFansViewPagerAdapter;
import com.joytouch.superlive.adapter.ViewPagerAdapter;
import com.joytouch.superlive.app.MediaPlayer;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.fragement.ChatFragment;
import com.joytouch.superlive.fragement.Goldwater;
import com.joytouch.superlive.fragement.LiveactionFragment;
import com.joytouch.superlive.fragement.LotteryFragment;
import com.joytouch.superlive.interfaces.FullScreenListener;
import com.joytouch.superlive.javabean.AnchorInfo;
import com.joytouch.superlive.javabean.AuchorInfo;
import com.joytouch.superlive.javabean.ChatInfo;
import com.joytouch.superlive.javabean.LiveMatchInfoJavabean;
import com.joytouch.superlive.javabean.LiveSource;
import com.joytouch.superlive.javabean.Question;
import com.joytouch.superlive.javabean.QuestionBase;
import com.joytouch.superlive.javabean.RoomBank;
import com.joytouch.superlive.javabean.SendQuestion;
import com.joytouch.superlive.javabean.redInfo;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.KeyBoardUtils;
import com.joytouch.superlive.utils.KeyboardUtil;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.ShareUtils;
import com.joytouch.superlive.widget.CoustomViewPager;
import com.joytouch.superlive.widget.GameDialog;
import com.joytouch.superlive.widget.LotteryRedDialog;
import com.joytouch.superlive.widget.MyLinearLayout;
import com.joytouch.superlive.widget.OuteLiveDialog;
import com.joytouch.superlive.widget.RedDialog;
import com.joytouch.superlive.widget.SendPresentAnimal;
import com.joytouch.superlive.widget.SetTopicDialog;
import com.joytouch.superlive.widget.ShareDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by lzx on 2016/4/11.
 */
public class OpenLivingDetailsActivity extends BaseActivity implements View.OnClickListener ,ImApi.OtherMessage, ImApi.ReceiveMessage,FullScreenListener{
    private ImageView iv_finish;//结束按钮
    private TextView match_name;//比赛名
    private ImageView iv_rotate;//旋转摄像头
    private RelativeLayout ll_title;//头部布局
    private RelativeLayout bottom;//底部布局
    private TextView rooms;//房间
    private TextView online;//在线人数
    private TextView tv_time;//观看人数
    private RelativeLayout playerview;
    private RadioGroup rg_tab;//头部tab
    private RadioButton rb_chat;
    private View view1;
    private View view2;
    private RadioButton rb_lottery;
    private RadioButton rb_action;
    private RadioButton rb_gold;
    private ChatFragment chatFragment;//聊天fragment
    private LotteryFragment lotteryFragment;//竞猜fragment
    private LiveactionFragment liveactionFragment;//赛况fragment
    private Goldwater goldwater;//金币流水fragment
    private CoustomViewPager pager;//管理fragment
    private LinearLayout ll_chat;//聊天布局
    private ImageView add;//加号
    private LinearLayout add_content;//发起竞猜，金币充值，玩游戏等布局
    private LinearLayout start_lottery;//发起竞猜
    private LinearLayout invitation;//邀请好友
    private LinearLayout pay;//金币充值
    private LinearLayout game;//玩游戏
    private LinearLayout ll_lottery;//出题布局
    private ViewPager vp_question;//题目
    private Button but_start;//确定
    private TextView tv_set_qustion;//我要出题
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titlename;
    private List<View> views = new ArrayList<>();
    private boolean isBottomOpen = false;
    private MyLinearLayout mll;//最外层，监听软键盘方法功能
    private RelativeLayout vedioView;//播放器父控件
    private MediaPlayer mediaPlayer;//播放器控制器
    private ImageView iv_play;//播放按钮
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideView();
        }
    };
    private ImageView iv_bg;//键盘弹出后的蒙版
    private EditText et_input;
    private boolean isKeyOpen = false;
    private String room_id;
    private String start;
    private String match_id;
    private String shareurl;
    private LiveSource liveSource;
    private AuchorInfo auchorInfo;
    private int screenWidth;
    private String title;
    private String room_price;
    private boolean isPlaying;
    private List<Question> questions = new ArrayList<>();//题目列表
    private static int REQUEST_CODE = 0;
    private TextView tv_noquestion;//无题目提示语
    private String tp;
    ShareUtils utils ;
    private boolean isShowAction = false;
    private ImageView iv_left;
    private ImageView iv_right;
    private String rednames;
    private String bluenames;
    private ImageView present;
    private boolean isDialogShow = false;
    private List<redInfo> infos = new ArrayList<>();
    private String gift_red;
    private String gift_blue;
    private boolean isShowMatch;
    private String room_score;
    private String onlinenum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_living_details);
        Intent intent = getIntent();
        if (intent!= null){
            room_id = intent.getStringExtra("room_id");
            start = intent.getStringExtra("start");
            shareurl = intent.getStringExtra("shareurl");
            liveSource = (LiveSource) intent.getSerializableExtra("source");
            auchorInfo = (AuchorInfo) intent.getSerializableExtra("auchor_info");
            room_price = intent.getStringExtra("room_price");
            rednames = intent.getStringExtra("rednames");
            bluenames = intent.getStringExtra("bluenames");
            title = intent.getStringExtra("title");
            room_score = intent.getStringExtra("room_score");
            SPUtils.put(OpenLivingDetailsActivity.this,Preference.matchname,title,Preference.preference);
            match_id = liveSource.getMatch_id();
            Preference.match_id = match_id;
            tp = intent.getStringExtra("type");
            gift_red = intent.getStringExtra("gift_red");
            gift_blue = intent.getStringExtra("gift_blue");
            onlinenum = intent.getStringExtra("online");
        }
        share();
        SuperLiveApplication.imApi.setReceiveMessageListener(this);
        SuperLiveApplication.imApi.setOtherMessageListener(this);
        initView();
        refreshScore();
        handler.sendEmptyMessageDelayed(0, 5000);
        initmatch();
    }

    private void share() {
         utils = new ShareUtils();
        if ("".equals(title)){
            if (liveSource.getMatch_name().trim().contains(" ")){
                title = liveSource.getMatch_name().trim().split(" ")[1];
            }else {
                title = liveSource.getMatch_name();
            }
        }
        if ("qq".equals(tp)){
            utils.qqShare(OpenLivingDetailsActivity.this,liveSource.getMatch_name()+ConfigUtils.getShareContent(),
                    title,shareurl,"");
        }else if ("wx".equals(tp)){
            utils.wxFriend(OpenLivingDetailsActivity.this,0,liveSource.getMatch_name()+ConfigUtils.getShareContent(),
                    title,shareurl,"");
        }else if ("pyq".equals(tp)){
            utils.wxFriend(OpenLivingDetailsActivity.this,1,liveSource.getMatch_name()+ConfigUtils.getShareContent(),
                    title,shareurl,"");
        }else if ("fans".equals(tp)){
            Intent it = new Intent(OpenLivingDetailsActivity.this,InvitationFriendActivity.class);
            it.putExtra("qiutanid","");
            it.putExtra("matchid",liveSource.getMatch_id());
            startActivity(it);
        }
    }

    private void initView() {
        present=(ImageView)findViewById(R.id.present);
        //判断是否是主播,设置不同的图片
        present.setBackgroundResource(R.drawable.present_logo);
        present.setOnClickListener(this);
        iv_finish = (ImageView) this.findViewById(R.id.back);
        iv_finish.setOnClickListener(this);
        match_name = (TextView) this.findViewById(R.id.match_name);
//        if ("".equals(title)){
            if (liveSource!= null){
//                match_name.setText(liveSource.getMatch_name());
                if (liveSource.getMatch_name().trim().contains(" ")){
                    match_name.setText(liveSource.getMatch_name().trim().split(" ")[1]);
                }else {
                    match_name.setText(liveSource.getMatch_name());
                }
            }
//        }else {
//            match_name.setText(title);
//        }

        iv_rotate = (ImageView) this.findViewById(R.id.iv_rotate);
        iv_rotate.setOnClickListener(this);
        ll_title = (RelativeLayout) this.findViewById(R.id.ll_title);
        bottom = (RelativeLayout) this.findViewById(R.id.bottom);
        playerview = (RelativeLayout) this.findViewById(R.id.playerview);
        playerview.setOnClickListener(this);
        rooms = (TextView) this.findViewById(R.id.rooms);
        rooms.setOnClickListener(this);
        online = (TextView) this.findViewById(R.id.online);
        tv_time = (TextView) this.findViewById(R.id.tv_time);
        rg_tab = (RadioGroup) this.findViewById(R.id.rg_tab);
        rb_chat = (RadioButton) this.findViewById(R.id.rb_chat);
        rb_chat.setOnClickListener(this);
        rb_chat.setChecked(true);
        view1 = this.findViewById(R.id.view1);
        view2 = this.findViewById(R.id.view2);
        rb_lottery = (RadioButton) this.findViewById(R.id.rb_lottery);
        rb_lottery.setOnClickListener(this);
        rb_action = (RadioButton) this.findViewById(R.id.rb_action);
        rb_action.setOnClickListener(this);
        rb_gold = (RadioButton) this.findViewById(R.id.rb_gold);
        pager = (CoustomViewPager) this.findViewById(R.id.fragmentCotainer);
        pager.setScrollble(false);
        pager.setOffscreenPageLimit(3);
        add = (ImageView) this.findViewById(R.id.add);
        add.setOnClickListener(this);
        ll_chat = (LinearLayout) this.findViewById(R.id.ll_chat);
        iv_bg = (ImageView) this.findViewById(R.id.iv_bg);
        iv_bg.setOnClickListener(this);
        et_input = (EditText) this.findViewById(R.id.et_input);
        add_content = (LinearLayout) this.findViewById(R.id.add_content);
        start_lottery = (LinearLayout) this.findViewById(R.id.start_lottery);
        start_lottery.setOnClickListener(this);
        invitation = (LinearLayout) this.findViewById(R.id.invitation);
        invitation.setOnClickListener(this);
        pay = (LinearLayout) this.findViewById(R.id.pay);
        pay.setOnClickListener(this);
        game = (LinearLayout) this.findViewById(R.id.game);
        game.setOnClickListener(this);
        ll_lottery = (LinearLayout) this.findViewById(R.id.ll_lottery);
        vp_question = (ViewPager) this.findViewById(R.id.vp_question);
        but_start = (Button) this.findViewById(R.id.but_start);
        but_start.setOnClickListener(this);
        tv_set_qustion = (TextView) this.findViewById(R.id.tv_set_qustion);
        tv_set_qustion.setOnClickListener(this);
        tv_noquestion = (TextView) this.findViewById(R.id.tv_noquestion);
        iv_left = (ImageView) this.findViewById(R.id.iv_left);
        iv_left.setOnClickListener(this);
        iv_right = (ImageView) this.findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);
        setRadioGroup();
        if (fragments.size()!=0){
            pager.setAdapter(new AttentionFansViewPagerAdapter(getSupportFragmentManager(), fragments));
        }
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position > 0) {
                    ll_chat.setVisibility(View.GONE);
                    iv_bg.setVisibility(View.GONE);
                    if (add_content.isShown()) {
                        add_content.setVisibility(View.GONE);
                    }
                    if (ll_lottery.isShown()) {
                        ll_lottery.setVisibility(View.GONE);
                        iv_bg.setVisibility(View.GONE);
                    }
                } else {
                    ll_chat.setVisibility(View.VISIBLE);
                    iv_bg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mll = (MyLinearLayout) this.findViewById(R.id.mll);
        mll.setOnResizeListener(new MyLinearLayout.OnResizeListener() {
            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                if (h < oldh && isBottomOpen) {
                    add_content.setVisibility(View.GONE);
                    isBottomOpen = false;
                }
            }
        });
        //监听输入框的touch方法
        et_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == event.ACTION_MOVE) {
                    return false;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("isKeyOpen1", String.valueOf(isKeyOpen));
                    if (ll_lottery.isShown()) {
                        ll_lottery.setVisibility(View.GONE);
                    }
                    if (isKeyOpen) {
                        KeyboardUtil.closeKeybord(et_input, OpenLivingDetailsActivity.this);
                        iv_bg.setVisibility(View.GONE);
                        isKeyOpen = false;
                        Log.e("isKeyOpen2", String.valueOf(isKeyOpen));
                    } else {
                        if (isBottomOpen) {
                            add_content.setVisibility(View.GONE);
                            isBottomOpen = false;
                        }
                        isKeyOpen = true;
                        Log.e("isKeyOpen3", String.valueOf(isKeyOpen));
                        KeyboardUtil.openKeybord(et_input, OpenLivingDetailsActivity.this);
                        iv_bg.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
        et_input.setOnClickListener(this);
        //监听输入法中的发送按钮 发送消息
        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //发送消息功能
                    sendMessage();
                }
                return false;
            }
        });
        //播放器
        iv_play = (ImageView) this.findViewById(R.id.iv_play);
        iv_play.setOnClickListener(this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        vedioView = (RelativeLayout) this.findViewById(R.id.vedioView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vedioView.getLayoutParams();
        layoutParams.height = screenWidth * 9 / 16;
        vedioView.setLayoutParams(layoutParams);
        mediaPlayer = new MediaPlayer(vedioView,this,true);
        setMediaPlayerSetting();
    }
    //设置播放器监听事件
    public void setMediaPlayerSetting(){
        mediaPlayer.setCompletionListener(new MediaPlayer.CompletionListener() {
            @Override
            public void completion() {
                isPlaying = false;
                iv_play.setBackgroundResource(R.drawable.start);
            }
        });
        mediaPlayer.setErroListener(new MediaPlayer.ErroListener() {
            @Override
            public void onErro() {
                iv_play.setBackgroundResource(R.drawable.stop);
                isPlaying = false;
            }

            @Override
            public void onStart() {
                iv_play.setBackgroundResource(R.drawable.start);
                isPlaying = true;
            }
        });
    }
    //刷新比分
    public void refreshScore(){
        //刷新比分
        SuperLiveApplication.imApi.setMatchListener(new ImApi.Match() {
            @Override
            public void match(boolean b, List<LiveMatchInfoJavabean> infos) {
                for (int i = 0; i < infos.size(); i++) {
                    Log.e("scores", match_id + "   " + infos.get(i).getMatchId());
                    if (match_id.equals(infos.get(i).getMatchId())) {
                        if (liveactionFragment != null) {
                            liveactionFragment.setScore(infos.get(i).getScore(), infos.get(i).getStating());
                        }
                        Log.e("scores", infos.get(i).getScore() + "   " + infos.get(i).getStating());
                        if (!TextUtils.isEmpty(infos.get(i).getScore())) {
                            rednum.setText(infos.get(i).getScore().split(" - ")[0]);
                            bluenum.setText(infos.get(i).getScore().split(" - ")[1]);
                        }
                        if (!TextUtils.isEmpty(infos.get(i).getStating())&&isShowMatch) {
                            matchstate.setVisibility(View.VISIBLE);
                            matchstate.setText(infos.get(i).getStating());
                        } else {
                            matchstate.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if(mediaPlayer!=null){
            Log.e("live_url",liveSource.getLive_url());
            mediaPlayer.player(liveSource.getLive_url(), liveSource.getUrl_type(),liveSource.getUrl_js(),"",liveSource.getUrl_reg());
            iv_play.setBackgroundResource(R.drawable.stop);
            isPlaying = true;
        }
    }
    boolean isanchor=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.present:
                if (Preference.zhubo_id.equals(SPUtils.get(OpenLivingDetailsActivity.this, Preference.myuser_id, "", Preference.preference))){
                    isanchor=true;
                    new SendPresentAnimal(OpenLivingDetailsActivity.this,R.id.chat,"0",isanchor,room_id,match_id).popupwindow_anchoe();
                }else{
                    isanchor=false;
                    //参数三: 0 主播  1 蓝色  2红色
                    //参数4:  是否是主播,true代表主播,false代表不是
                    add_content.setVisibility(View.GONE);
                    iv_bg.setVisibility(View.GONE);
                    KeyboardUtil.closeKeybord(et_input, OpenLivingDetailsActivity.this);
                    new SendPresentAnimal(OpenLivingDetailsActivity.this,R.id.chat,"0",isanchor,room_id,match_id).popupWindow_Present();
                }

                break;
            case R.id.back:
                showOutLiveDialog();
                break;
            case R.id.iv_rotate:
                break;
            case R.id.rooms:
                Intent intent = new Intent(OpenLivingDetailsActivity.this,LiveSourceActivity.class);
                intent.putExtra("request","request");
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.add:
                if (isKeyOpen){
                    KeyboardUtil.closeKeybord(et_input,OpenLivingDetailsActivity.this);
                    isKeyOpen = false;
                }
                closeAdd();
                if (ll_lottery.isShown()){
                    ll_lottery.setVisibility(View.GONE);
                }
                break;
            case R.id.start_lottery:
                closeAdd();
                getQuestion();
                ll_lottery.setVisibility(View.VISIBLE);
                iv_bg.setVisibility(View.VISIBLE);
                break;
            case R.id.invitation:
                closeAdd();
                ShareDialog dialog = new ShareDialog(OpenLivingDetailsActivity.this);
                dialog.setMatchname(liveSource.getMatch_name());//比赛
                dialog.setMatchid(match_id);//比赛id
                dialog.setShareurl(shareurl);//分享链接
                dialog.setQiutanid(liveSource.getQiutan_match_id());//球探id
                dialog.show();
                break;
            case R.id.pay:
                closeAdd();
                toActivity(ChargeActivity.class);
                break;
            case R.id.game:
                //游戏点击事件
                HashMap hashMap = new HashMap();
                hashMap.put("userId",(String) SPUtils.get(this, Preference.myuser_id, "", Preference.preference));
                hashMap.put("isPrivate","yes");
                MobclickAgent.onEvent(OpenLivingDetailsActivity.this,"gamesPoint",hashMap);
                GameDialog gameDialog = new GameDialog(OpenLivingDetailsActivity.this);
                gameDialog.show();
                closeAdd();
                break;
            case R.id.but_start:
                sendQuestion();
                ll_lottery.setVisibility(View.GONE);
                iv_bg.setVisibility(View.GONE);
                break;
            case R.id.tv_set_qustion:
                SetTopicDialog topicDialog = new SetTopicDialog(OpenLivingDetailsActivity.this);
                topicDialog.show();
                ll_lottery.setVisibility(View.GONE);
                break;
            case R.id.iv_left:
                vp_question.setCurrentItem(vp_question.getCurrentItem()-1);
                break;
            case R.id.iv_right:
                vp_question.setCurrentItem(vp_question.getCurrentItem()+1);
                break;
            case R.id.playerview:
                if (ll_title.isShown()){
                    hideView();
                }else {
                    ll_title.setVisibility(View.VISIBLE);
                    bottom.setVisibility(View.GONE);
                    if(isShowMatch) {
                        matchs.setVisibility(View.VISIBLE);
                    }else {
                        matchs.setVisibility(View.GONE);
                    }
                    iv_play.setVisibility(View.VISIBLE);
                    handler.removeMessages(0);
                    handler.sendEmptyMessageDelayed(0,5000);
                }
                break;
            case R.id.et_input:
                if(isKeyOpen) {
                    KeyBoardUtils.openKeybord(et_input, OpenLivingDetailsActivity.this);
                }else{
                    KeyBoardUtils.closeKeybord(et_input, OpenLivingDetailsActivity.this);
                }
                break;
            case R.id.iv_play:
                if (isPlaying){
                    mediaPlayer.onStop();
                    iv_play.setBackgroundResource(R.drawable.start);
                    isPlaying = false;
                }else {
                    mediaPlayer.player(liveSource.getLive_url(), liveSource.getUrl_type(),liveSource.getUrl_js(),"",liveSource.getUrl_reg());
                    iv_play.setBackgroundResource(R.drawable.stop);
                    isPlaying = true;
                }
                break;
            case R.id.iv_bg:
                if (isBottomOpen){
                    add_content.setVisibility(View.GONE);
                    isBottomOpen = false;
                }
                KeyboardUtil.closeKeybord(et_input, OpenLivingDetailsActivity.this);
                isKeyOpen = false;
                iv_bg.setVisibility(View.GONE);
                ll_lottery.setVisibility(View.GONE);
                break;
            case R.id.rb_chat:
                isShowAction = false;
                pager.setCurrentItem(0);
                iv_bg.setVisibility(View.GONE);
                break;
            case R.id.rb_lottery:
                isShowAction = false;
                pager.setCurrentItem(1);
                lotteryFragment.getLotteryList("");
                break;
            case R.id.rb_action:
                if (isShowAction){
                    liveactionFragment.refresh();
                    Log.e("refreshAction", "true");
                }else {
                    isShowAction = true;
                    Log.e("refreshAction", "false");
                    pager.setCurrentItem(2);
                }
                break;
            case R.id.rb_gold:
                pager.setCurrentItem(3);
                break;
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
        .add("room_id", room_id)
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
    //隐藏布局
    public void hideView(){
       ll_title.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);
        matchs.setVisibility(View.GONE);
        iv_play.setVisibility(View.GONE);
    }
    //关闭加号
    public void closeAdd(){
        if (!isBottomOpen){
            add_content.setVisibility(View.VISIBLE);
            iv_bg.setVisibility(View.VISIBLE);
            isBottomOpen = true;
        }else {
            add_content.setVisibility(View.GONE);
            iv_bg.setVisibility(View.GONE);
            isBottomOpen = false;
        }
    }
    //根据后台数据添加不同的RadioGroup
    public void setRadioGroup(){
        for (int i = 0;i<3;i++){
            switch (i){
                case 0:
                    chatFragment = new ChatFragment();
                    AnchorInfo info = new AnchorInfo();
                    info.setAnchorid(auchorInfo.getUserid());
                    info.setAnchorname(auchorInfo.getNick_name());
                    info.setLevel(auchorInfo.getLevel());
                    info.setHeaderimg(auchorInfo.getImage());
                    info.setRoomid(room_id);
                    info.setBetting(room_price);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("id", match_id);
                    bundle1.putString("isAnchor", "yes");
                    bundle1.putSerializable("info", info);
                    bundle1.putString("isPrivate", "1");
                    bundle1.putString("online",onlinenum);
                    chatFragment.setArguments(bundle1);
                    fragments.add(chatFragment);
                    break;
                case 1:
                    lotteryFragment = new LotteryFragment();
                    fragments.add(lotteryFragment);
                    break;
                case 2:
                    if (liveSource.getQiutan_match_id() != null&&!liveSource.getQiutan_match_id().equals("")){
                        view2.setVisibility(View.VISIBLE);
                        rb_action.setVisibility(View.VISIBLE);
                        liveactionFragment = new LiveactionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("matchid",match_id);
                        Log.e("match_idlottery",match_id);
                        liveactionFragment.setArguments(bundle);
                        fragments.add(liveactionFragment);
                    }else {
                        view2.setVisibility(View.GONE);
                        rb_action.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }
    //获取题目列表
    public void getQuestion(){
        questions.clear();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("match_id",match_id)
                .add("room_id",room_id)
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
    //主播发送消息
    private void sendMessage() {
        if (!et_input.getText().toString().equals("")){
            SuperLiveApplication.imApi.sendRoomMsg(room_id, et_input.getText().toString());
            Log.e("room_id", room_id);
            et_input.setText("");
            KeyboardUtil.closeKeybord(et_input, OpenLivingDetailsActivity.this);
            iv_bg.setVisibility(View.GONE);
            isKeyOpen = false;
        }else {
            showToast("请输入发送内容");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            showOutLiveDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void showOutLiveDialog(){
        OuteLiveDialog outeLiveDialog = new OuteLiveDialog(OpenLivingDetailsActivity.this,room_id,start,auchorInfo.getUserid(),"","","","");
        outeLiveDialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.destory();
        isPlaying = false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void online(String count,String roomid) {
        online.setText("在线人数" + count + "人");
        chatFragment.online(count,roomid);
    }

    @Override
    public void roomRankTop(String roomid,List<RoomBank> list) {
        chatFragment.roomTOp10(roomid,list);
    }

    @Override

    public void Messagelist(String from, String body, String type2) {

    }

    @Override
    public void chatMessage(String from, String body) {

    }

    @Override
    public void groupMessage(ChatInfo info) {
        info.setIsPrivate(true);
        chatFragment.message(info);
        //更新比分
        if ("11".equals(info.getType())){
            Log.e("更新比分", info.getBlue()+"__"+info.getRed());
            matchView(Integer.parseInt(info.getRed()), Integer.parseInt(info.getBlue()));
            red_num.setText(info.getRed());
            blue_num.setText(info.getBlue());
        }
    }

    @Override
    public void statusFormIm(int status) {
        chatFragment.joinRoom(status);
    }

    @Override
    public void redReceive(redInfo info) {
        infos.add(info);
        xunhuan();

    }
    public void xunhuan(){
        Log.e("xunhuan", "111111" + isDialogShow);
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
            RedDialog redDialog = new RedDialog(OpenLivingDetailsActivity.this,info);
            redDialog.setCancelable(false);
            redDialog.show();
            isDialogShow = true;
            redDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isDialogShow = false;
                    xunhuan();
                }
            });
        }else {
            LotteryRedDialog lotteryRedDialog = new LotteryRedDialog(OpenLivingDetailsActivity.this,info);
            lotteryRedDialog.show();
            isDialogShow = true;
            lotteryRedDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    isDialogShow = false;
                    xunhuan();
                }
            });
        }

    }


    @Override
    public void fullScren(boolean isFull) {
        new SendPresentAnimal(OpenLivingDetailsActivity.this,R.id.chat,"0",isanchor,room_id,match_id).setNoXuanzhuan();
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
        matchstate = (TextView) findViewById(R.id.match_state);
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
        matchs.setVisibility(View.GONE);
        matchstate.setVisibility(View.GONE);
        red_praise.setBackgroundResource(R.drawable.red_praise);
        blue_praise.setBackgroundResource(R.drawable.blue_praise);
        red_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OpenLivingDetailsActivity.this, "作为主播，不支持给球队送礼物", Toast.LENGTH_SHORT).show();
            }
        });
        blue_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(OpenLivingDetailsActivity.this, "作为主播，不支持给球队送礼物", Toast.LENGTH_SHORT).show();
            }
        });
/*        updatenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UpdateScoreDialog dialog = new UpdateScoreDialog(OpenLivingDetailsActivity.this);
                dialog.setRedname(redname.getText().toString());
                dialog.setBluename(bluename.getText().toString());
                dialog.setCancelable(false);

                dialog.show();
            }
        });*/
        if(!TextUtils.isEmpty(rednames)){
            redname.setText(rednames);
        }
        if(!TextUtils.isEmpty(bluenames)){
            bluename.setText(bluenames);
        }

        if(!TextUtils.isEmpty(rednames)&&!TextUtils.isEmpty(bluenames)){
            isShowMatch = true;
        }else{
            isShowMatch = false;
        }
        if (!TextUtils.isEmpty(room_score)) {
            rednum.setText(room_score.split(",")[0]);
            bluenum.setText(room_score.split(",")[1]);
        }else{
            rednum.setText("0");
            bluenum.setText("0 ");
        }
        Log.e("dayin",gift_red+"__"+gift_blue);
        matchView(Integer.parseInt(gift_red), Integer.parseInt(gift_blue));
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
        int layout = (screenWidth-ConfigUtils.dip2px(OpenLivingDetailsActivity.this,176))/2;
        LogUtils.e("ssssssssssss*", (screenWidth - ConfigUtils.dip2px(OpenLivingDetailsActivity.this, 176)) + "");
        LogUtils.e("ssssssssssss*", layout + "");
        if(isShowMatch){
            matchs.setVisibility(View.VISIBLE);
        }else{
            matchs.setVisibility(View.GONE);
        }

        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((int) (layout*redcon),ConfigUtils.dip2px(OpenLivingDetailsActivity.this,11));
        redcount.setLayoutParams(parms);
        redcount.setPadding(8, 0, 0, 0);
        redcount.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams((int) (layout*bluecon),ConfigUtils.dip2px(OpenLivingDetailsActivity.this,11));
        redcount.setPadding(0, 0, 8, 0);
        redcount.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        bluecont.setLayoutParams(parm);
    }
}
