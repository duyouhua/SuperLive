package com.joytouch.superlive.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.GuessDetailsLeftAdapter;
import com.joytouch.superlive.adapter.GuessDetailsRightAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.GuessDetail;
import com.joytouch.superlive.javabean.LotteryInfo;
import com.joytouch.superlive.javabean.SendLotteryResult;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.TimeUtil;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.LotteryShareDialog;
import com.joytouch.superlive.widget.MyListView;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import okhttp3.FormBody;

/**
 * 直播界面竞猜列表的竞猜详情
 *
 */

public class LotteryDetailsActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_content;//竞猜内容
    private ImageView iv_close;//关闭
    private Button but_left;//左侧按钮
    private Button but_right;//右侧按钮
    private CircleImageView iv_icon_left;//左侧按钮上的头像
    private CircleImageView iv_icon_right;//右侧按钮上的头像
    private TextView tv_time;//时间或者状态
    private TextView tv_num_left;//左侧按钮下的金币数
    private TextView tv_num_right;//右侧按钮下的金币数
    private LinearLayout ll_dashang;//打赏主播布局（用于隐藏）
    private TextView tv_open_result;//开奖结果(打赏主播布局中)
    private TextView tv_betting_result;//投注结果(打赏主播布局中)
    private TextView tv_betting_num;//投注金币（打赏主播布局中）
    private TextView tv_gold_num;//赢得的金币
    private RelativeLayout rl_dashang;//打赏功能
    private TextView anchor_gold;//打赏金额
    private RelativeLayout ll_refresh;//刷新布局
    private ImageView iv_refresh;//刷新图片（动画）
    private TextView tv_touzhu_num;//我的投注（刷新布局中）
    private TextView tv_shouyi_num;//预计收益（刷新布局中）
    private RelativeLayout rl_refresh;//刷新操作（刷新布局中）
    private LinearLayout ll_xuanxiang;//选项布局(用于隐藏)
    private ImageView iv_win_l;//赢的图片左
    private ImageView iv_win_r;//赢的图片右
    private TextView tv_option_r;
    private TextView tv_option_l;//左侧选项（选项布局中）
    private TextView tv_gold_left;//左侧金币数（选项布局中）
    private TextView tv_gold_right;//右侧金币数（选项布局中）
    private PullToRefreshLayout refreshLayout;//参加竞猜人的刷新
    private MyListView listView_l;//左侧列表
    private MyListView listView_r;//右侧列表
    private RelativeLayout rl_share;//分享题目
    private LinearLayout oddsll;
    private TextView odds_tv;
    private TextView odds_time;
    private RelativeLayout starting;
    private String gold;
    private RelativeLayout containerleft;
    private RelativeLayout containerright;

    private List<GuessDetail> detailsl;//左侧投注人
    private List<GuessDetail> detailsr;//右侧投注人
    private LotteryInfo info;
    private GuessDetailsLeftAdapter leftadpter;
    private GuessDetailsRightAdapter righteadapter;
    private boolean isUp;
    private int pager = 1;
    private boolean isResh = false;
    private int status;
    private LinearLayout loading;
    private boolean isFresh;
    private String roomid;
    private String anchorid;
    private static String ACTION_LOTTERY = "lottery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_lottery_details);
        detailsl = new ArrayList<>();
        detailsr = new ArrayList<>();
        gold = getIntent().getStringExtra("qid");
        roomid = getIntent().getStringExtra("roomid");
        anchorid = getIntent().getStringExtra("anchorid");
        if(TextUtils.isEmpty(roomid)){
            roomid = "";
        }
        if(TextUtils.isEmpty(anchorid)){
            anchorid = "";
        }
        initView();
    }
    private void initView() {
        loading = (LinearLayout) findViewById(R.id.loading);
        starting = (RelativeLayout) findViewById(R.id.Starting);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        but_left = (Button) this.findViewById(R.id.but_left);
        but_left.setOnClickListener(this);
        but_right = (Button) this.findViewById(R.id.but_right);
        but_right.setOnClickListener(this);
        iv_icon_left = (CircleImageView) this.findViewById(R.id.iv_icon_left);
        iv_icon_right = (CircleImageView) this.findViewById(R.id.iv_icon_right);
        tv_time = (TextView) this.findViewById(R.id.tv_time);
        tv_num_left = (TextView) this.findViewById(R.id.tv_num_left);
        tv_num_right = (TextView) this.findViewById(R.id.tv_num_right);
        ll_dashang = (LinearLayout) this.findViewById(R.id.ll_dashang);
        tv_open_result = (TextView) this.findViewById(R.id.tv_open_result);
        tv_betting_result = (TextView) this.findViewById(R.id.tv_betting_result);
        tv_betting_num = (TextView) this.findViewById(R.id.tv_betting_num);
        tv_gold_num = (TextView) this.findViewById(R.id.tv_gold_num);
        rl_dashang = (RelativeLayout) this.findViewById(R.id.rl_dashang);
        ll_refresh = (RelativeLayout) this.findViewById(R.id.ll_refresh);
        iv_refresh  = (ImageView) this.findViewById(R.id.iv_refresh);
        tv_touzhu_num = (TextView) this.findViewById(R.id.tv_touzhu_num);
        tv_shouyi_num = (TextView) this.findViewById(R.id.tv_shouyi_num);
        rl_refresh = (RelativeLayout) this.findViewById(R.id.rl_refresh);
        ll_xuanxiang = (LinearLayout) this.findViewById(R.id.ll_xuanxiang);
        iv_win_l = (ImageView) this.findViewById(R.id.iv_win_l);
        iv_win_r = (ImageView) this.findViewById(R.id.iv_win_r);
        tv_option_l = (TextView) this.findViewById(R.id.tv_option_l);
        tv_option_r = (TextView) this.findViewById(R.id.tv_option_r);
        tv_gold_left = (TextView) this.findViewById(R.id.tv_gold_left);
        tv_gold_right = (TextView) this.findViewById(R.id.tv_gold_right);
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refreshLayout.setCanPullDown(false);
        listView_l = (MyListView) this.findViewById(R.id.listView_l);
        listView_r = (MyListView) this.findViewById(R.id.listView_r);
        rl_share = (RelativeLayout) this.findViewById(R.id.rl_share);
        rl_share.setOnClickListener(this);
        oddsll = (LinearLayout) findViewById(R.id.odds_center_ll);
        odds_time = (TextView) findViewById(R.id.odds_time);
        odds_tv = (TextView) findViewById(R.id.odds_tv);
        anchor_gold = (TextView) findViewById(R.id.tv_dashang);
        containerleft = (RelativeLayout) findViewById(R.id.container_left);
        containerright = (RelativeLayout) findViewById(R.id.container_right);
        rl_dashang.setOnClickListener(this);
        getLotteryDetail();
        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                if (!isResh) {
                    isResh = true;
                    isUp = true;
                    pager++;
                    upData();
                }

            }
        });
        rl_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager = 1;
                isUp = false;
                if(!isFresh) {
                    isFresh = true;
                    RotateAnimation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    an.setInterpolator(new LinearInterpolator());//不停顿
                    an.setRepeatCount(-1);//重复次数
                    an.setFillAfter(true);//停在最后
                    an.setDuration(600);
                    iv_refresh.startAnimation(an);
                    getLotteryDetail();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                finish();
                break;
            case R.id.rl_dashang:
                reward();
                break;
            case R.id.rl_share:
                LotteryShareDialog lsd = new LotteryShareDialog(LotteryDetailsActivity.this,Preference.match_id,Preference.room_id,
                        (String) SPUtils.get(LotteryDetailsActivity.this,Preference.myuser_id,"",Preference.preference),info.getMatchid(),info.getTitle());
                lsd.show();
                break;
        }
    }
    public void  logic(final LotteryInfo info){
        Preference.lotteryMoney = info.getBettingGolde();
        tv_content.setText(info.getTitle());
        tv_num_left.setText(info.getLeftgold());
        tv_num_right.setText(info.getRightgold());
        tv_touzhu_num.setText("我投注的："+info.getBetting());
        tv_shouyi_num.setText("预计收益："+info.getEarnings());
        but_right.setText(info.getRightselector());
        but_left.setText(info.getLeftselector());
        tv_betting_num.setText(info.getBetting());
        tv_betting_result.setText("");
        if(info.isSelf()){
            if(info.getSelfeSelector() == 0){
                but_right.setEnabled(false);
                tv_betting_result.setText(info.getLeftselector());
            }else{
                but_left.setEnabled(false);
                tv_betting_result.setText(info.getRightselector());
            }
        }
        //是否是擂台
        if(info.isOdds()){
            iv_icon_left.setVisibility(View.GONE);
            iv_icon_right.setVisibility(View.GONE);
            but_left.setBackgroundResource(R.drawable.retangle_blue_gray);
            but_right.setBackgroundResource(R.drawable.retangle_blue_gray);
            oddsll.setVisibility(View.VISIBLE);
            tv_time.setVisibility(View.GONE);
            odds_time.setText(info.getEndtime());
            odds_tv.setText(info.getOdds());

            //判断擂台提是否选择
            if(info.isOddsSelector()){
                //判断选着了哪边
                if(info.getOddsSelector()==0){
                    iv_icon_left.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(Preference.photourl+"200x200/"+info.getImagehead(),iv_icon_left, ImageLoaderOption.optionsHeader);
                    if(!info.isSelf()){
                        but_left.setEnabled(false);
                    }
                    //判断是否和自己选的一样
                    if(but_left.isEnabled()){
                        iv_icon_left.setBorderWidth(ConfigUtils.dip2px(LotteryDetailsActivity.this,1));
                        iv_icon_left.setBorderColor(getResources().getColor(R.color.color_blue));
                    }else{
                        iv_icon_left.setBorderWidth(0);
                    }
                }else{
                    iv_icon_right.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(Preference.photourl+"200x200/"+info.getImagehead(), iv_icon_left, ImageLoaderOption.optionsHeader);
                    if(!info.isSelf()){
                        but_right.setEnabled(false);
                    }
                    if(but_right.isEnabled()){
                        iv_icon_right.setBorderWidth(ConfigUtils.dip2px(LotteryDetailsActivity.this,1));
                        iv_icon_right.setBorderColor(getResources().getColor(R.color.color_blue));
                    }else{
                        iv_icon_right.setBorderWidth(0);
                    }
                }
            }


        }else{
            iv_icon_left.setVisibility(View.GONE);
            iv_icon_right.setVisibility(View.GONE);
            but_left.setBackgroundResource(R.drawable.retangle_yellow_gray);
            but_right.setBackgroundResource(R.drawable.retangle_yellow_gray);
            oddsll.setVisibility(View.GONE);
            tv_time.setVisibility(View.VISIBLE);

        }
        ll_dashang.setVisibility(View.GONE);
        ll_xuanxiang.setVisibility(View.GONE);
        if(info.isEnd()){

            if(info.isOpenLottery()) {
                ll_dashang.setVisibility(View.VISIBLE);
                ll_xuanxiang.setVisibility(View.VISIBLE);
                starting.setVisibility(View.GONE);
                ll_refresh.setVisibility(View.GONE);
                iv_win_l.setVisibility(View.GONE);
                iv_win_r.setVisibility(View.GONE);
                tv_option_r.setText(info.getRightselector());
                tv_option_l.setText(info.getLeftselector());
                tv_gold_left.setText(info.getLeftgold());
                tv_gold_right.setText(info.getRightgold());
                if("0".equals(info.getOpenSelector())){
                    tv_option_l.setTextColor(getResources().getColor(R.color.color_yellow));
                    tv_open_result.setText(info.getLeftselector());

                }else {
                    tv_option_r.setTextColor(getResources().getColor(R.color.color_yellow));
                    tv_open_result.setText(info.getRightselector());
                }
                if(info.isWin()){
                    tv_gold_num.setText("+"+info.getWingold());
                    tv_gold_num.setTextColor(getResources().getColor(R.color.color_yellow));
                    rl_dashang.setVisibility(View.VISIBLE);
                    anchor_gold.setText("打赏主播" + info.getAnchorGold());
                    gold = info.getAnchorGold();
                    if("0".equals(info.getOpenSelector())){
                        iv_win_l.setVisibility(View.VISIBLE);

                    }else {
                        iv_win_r.setVisibility(View.VISIBLE);
                    }

                }else{
                    rl_dashang.setVisibility(View.GONE);
                    tv_gold_num.setText("-" + info.getBetting());
                    tv_gold_num.setTextColor(getResources().getColor(R.color.main));
                    if (info.getBetting().equals("0")){
                        tv_gold_num.setVisibility(View.GONE);
                    }
                }

            }else{
                rl_refresh.setVisibility(View.GONE);
                but_right.setEnabled(false);
                but_left.setEnabled(false);
                if(info.isSelf()) {
                    if (info.getSelfeSelector() == 0) {
                        but_left.setBackgroundResource(R.drawable.retangle_b1);
                    } else {
                        but_right.setBackgroundResource(R.drawable.retangle_b1);
                    }
                }
                odds_time.setText("待开奖");
                tv_time.setText("待开奖");
                odds_time.setTextColor(getResources().getColor(R.color.main));
                tv_time.setTextColor(getResources().getColor(R.color.main));
            }
        }
        but_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConfigUtils.sendLotteryResult(LotteryDetailsActivity.this,Preference.room_id, gold, info.getLeftselector(), info.getBettingGolde(), new ConfigUtils.ResultListener() {
                    @Override
                    public void result(SendLotteryResult result) {
                        but_right.setEnabled(false);
                        ConfigUtils.flash(LotteryDetailsActivity.this, containerleft, info.getBettingGolde());
                        tv_num_left.setText(result.getLeftgold());
                        tv_num_right.setText(result.getRightgold());
                        tv_touzhu_num.setText("我投注的：" + result.getBetting());
                        tv_shouyi_num.setText("预计收益：" + result.getEarnings());
                    }

                    @Override
                    public void last(String islast) {

                    }
                });

            }
        });

        but_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConfigUtils.sendLotteryResult(LotteryDetailsActivity.this,Preference.room_id, gold, info.getRightselector(), info.getBettingGolde(), new ConfigUtils.ResultListener() {
                    @Override
                    public void result(SendLotteryResult result) {
                        but_left.setEnabled(false);
                        tv_num_left.setText(result.getLeftgold());
                        tv_num_right.setText(result.getRightgold());
                        tv_touzhu_num.setText("我投注的：" + result.getBetting());
                        tv_shouyi_num.setText("预计收益：" + result.getEarnings());
                        ConfigUtils.flash(LotteryDetailsActivity.this, containerright, info.getBettingGolde());
                    }

                    @Override
                    public void last(String islast) {

                    }
                });
            }
        });
        if(!info.isOpenLottery()) {
            //是否开启倒计时 去网络时间
            new Thread() {
                public void run() {
                    try {
                        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8")); // 时区设置
                        URL url = new URL(Preference.timeUrl);// 取得资源对象
                        URLConnection uc = url.openConnection();// 生成连接对象
                        uc.connect(); // 发出连接
                        long timestr = uc.getDate(); // 取得网站日期时间（时间戳）
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putLong("timestr", timestr);
                        msg.setData(bundle);
                        handlers.sendMessage(msg);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
    Handler handlers = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            long nowtime = msg.getData().getLong("timestr");
            long endtime = info.getStopTime();
            long time = endtime- nowtime;
            LogUtils.e("&&&****","time = " +time);
            if(time>30000) {
                tv_time.setTextColor(getResources().getColor(R.color.editext_hint));
                tv_time.setText(info.getEndtime() + "截止");

            }
            if(time<=30000&&time>0){

                CountDownTimer Count = null;
                Count = new CountDownTimer(time, 250) {
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
                        tv_time.setTextColor(getResources().getColor(R.color.color_yellow));
                        tv_time.setText(miao + "s");

                    }

                    public void onFinish() {
                        but_left.setEnabled(false);
                        but_right.setEnabled(false);
                        tv_time.setText("待开奖");
                        tv_time.setTextColor(getResources().getColor(R.color.main));

                    }
                };

                Count.start();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.put(this, "lotterydilog",false, Preference.preference);
    }

    //提交打赏的金额
    public void reward(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("anchor_id", anchorid);
        builder.add("room_id",roomid);
        builder.add("money", gold);
        builder.add("token", (String) SPUtils.get(LotteryDetailsActivity.this, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils(this);
        requestUtils.httpPost(Preference.reward, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.optString("status"))) {
                        Toast.makeText(LotteryDetailsActivity.this,"打赏成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LotteryDetailsActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    public void getLotteryDetail(){
        if(gold == null){
            gold = "";
        }
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", roomid);
        builder.add("que_id", gold);
        builder.add("token", (String) SPUtils.get(LotteryDetailsActivity.this, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils(this,loading,isUp);
        requestUtils.httpPost(Preference.LotteryDetail, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                    isFresh = false;
            }

            @Override
            public void onSuccess(String json) {
                Log.e("竞猜详情",json);
                if(isFresh){
                    isFresh = false;
                    iv_refresh.clearAnimation();
                }
                detailsl.clear();
                detailsr.clear();
                try {
                    JSONObject object = new JSONObject(json);
                    if("_0000".equals(object.optString("status"))){

                    jsoParse(object);
                        JSONArray left = object.optJSONArray("left");
                        if(left!=null){
                            for(int i = 0;i<left.length();i++){
                                JSONObject ob = left.optJSONObject(i);
                                GuessDetail detail = new GuessDetail();
                                detail.setImgurl(ob.optString("image"));
                                detail.setIsLast(ob.optString("last"));
                                detail.setIsMe(ob.optString("is_me"));
                                detail.setMoney(ob.optString("money"));
                                detail.setName(ob.optString("nick_name"));
                                detailsl.add(detail);
                            }
                        }
                        JSONArray right = object.optJSONArray("right");
                        if(right!=null){
                            for(int i = 0;i<right.length();i++){
                                JSONObject ob = right.optJSONObject(i);
                                GuessDetail detail = new GuessDetail();
                                detail.setImgurl(ob.optString("image"));
                                detail.setIsLast(ob.optString("last"));
                                detail.setIsMe(ob.optString("is_me"));
                                detail.setMoney(ob.optString("money"));
                                detail.setName(ob.optString("nick_name"));
                                detailsr.add(detail);
                            }
                        }
                        logic(info);
                        LogUtils.e("-----------", ""+detailsl.size()+"    "+detailsr.size());

                        leftadpter = new GuessDetailsLeftAdapter(detailsl,LotteryDetailsActivity.this);
                        listView_l.setAdapter(leftadpter);
                        leftadpter.notifyDataSetChanged();
                        righteadapter = new GuessDetailsRightAdapter(detailsr,LotteryDetailsActivity.this);
                        listView_r.setAdapter(righteadapter);
                        righteadapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public  LotteryInfo jsoParse(JSONObject obj) {
        JSONObject object = obj.optJSONObject("que_info");
        info = new LotteryInfo();
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
        info.setBetting(object.optString("my_option_money"));
        info.setEarnings(object.optString("predict_money"));
        info.setAnchorGold(object.optString("largess_money"));
        info.setIsOdds(false);
        if(object.optString("option_left").equals(object.optString("win_option"))){
            info.setOpenSelector("0");
        }else{
            info.setOpenSelector("1");
        }


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
        status = object.optInt("status");
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

    public void upData(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", roomid);
        builder.add("que_id", gold);
        builder.add("pages", String.valueOf(pager));
        builder.add("status", String.valueOf(status));
        builder.add("token", (String) SPUtils.get(LotteryDetailsActivity.this, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils(this);
        requestUtils.httpPost(Preference.LotteryPeople, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                isResh = false;
                    if(isUp){
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
            }

            @Override
            public void onSuccess(String json) {
                isResh = false;

                try {
                    JSONObject object = new JSONObject(json);
                    JSONArray left = object.optJSONArray("left");
                    int lsize = detailsl.size();
                    int rsize = detailsr.size();
                    if(!isUp){
                        detailsr.clear();
                        detailsl.clear();
                    }else {
                        refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if(left!=null){
                        for(int i = 0;i<left.length();i++){
                            JSONObject ob = left.optJSONObject(i);
                            GuessDetail detail = new GuessDetail();
                            detail.setImgurl(ob.optString("image"));
                            detail.setIsLast(ob.optString("last"));
                            detail.setIsMe(ob.optString("is_me"));
                            detail.setMoney(ob.optString("money"));
                            detail.setName(ob.optString("nick_name"));
                            detailsl.add(detail);
                        }
                    }
                    JSONArray right = object.optJSONArray("right");
                    if(right!=null){
                        for(int i = 0;i<right.length();i++){
                            JSONObject ob = right.optJSONObject(i);
                            GuessDetail detailr= new GuessDetail();
                            detailr.setImgurl(ob.optString("image"));
                            detailr.setIsLast(ob.optString("last"));
                            detailr.setIsMe(ob.optString("is_me"));
                            detailr.setMoney(ob.optString("money"));
                            detailr.setName(ob.optString("nick_name"));
                            detailsr.add(detailr);
                        }
                    }
                    leftadpter.notifyDataSetChanged();
                    righteadapter.notifyDataSetChanged();
                    listView_r.setSelection(rsize);
                    listView_l.setSelection(lsize);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
