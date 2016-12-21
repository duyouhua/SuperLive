package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.fulllotteryback;
import com.joytouch.superlive.javabean.SendLotteryResult;
import com.joytouch.superlive.javabean.lotteryDetailInfo;
import com.joytouch.superlive.utils.ConfigUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 8/15 0015.
 */
public class FullScreenLottreyPopupwindow extends BaseDialog implements View.OnClickListener {

    private final lotteryDetailInfo info;
    private final String type;
    //题目7秒消失
    private  Handler DissmissHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            HideView();
            dismiss();
        }
    };
    private String lotteryid="";
    private  View dronid;
    private  Context context;
    private LinearLayout right_check;
    private RelativeLayout full_bottom;
    private LinearLayout left_check;
    private LinearLayout ll_containtop;
    private TextView left_answer;
    private TextView left_total_money;
    private TextView right_answer;
    private TextView right_all_money;
    private TextView tv_counttime;
    private TextView room_money;
    private TextView lottery_title;
    private TextView my_bet_money;
    private TextView predict_money;
    private ImageView iv_light_left;
    private TextView tv_left_light;
    private TextView tv_right_light;
    private ImageView iv_light_right;
    private String full_lever="";
    private String roommoney;
    private String left_text;
    private String right_text;
    private long endTime;
    private CountDownTimer Count;
    private String sel;
    private Handler counts_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //倒计时开始网络时间
            long nowtime = (long) msg.obj;
            long time = endTime - nowtime;

            Log.e("time的值:", time + "___" + endTime + "__" + nowtime);
            if (time > 30000) {
//                countTime.setVisibility(View.INVISIBLE);
                time=30000;
            }
            if (time <= 30000 && time > 0) {
                ll_containtop.setVisibility(View.VISIBLE);
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
                        ll_containtop.setVisibility(View.INVISIBLE);
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
    private long firstTime=0;
    private long interval=1000;
    private int count=0;
    private TimerTask _task;
    private Timer delayTimer;
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
    private long xx_time=500;
    private long dismiss_tims=7000;
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
    private RelativeLayout ll_time_down;

    public FullScreenLottreyPopupwindow(String lotteryid, final Context context, View dronid, lotteryDetailInfo info, String type) {
        super(context);
        this.lotteryid=lotteryid;
        this.dronid=dronid;
        this.context=context;
        this.info=info;
        this.type=type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreenlot_popup_window);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                fulllotteryback achor = (fulllotteryback) context;
                achor.isDissmiss("0");
            }
        });

        initView();
        setData(info, type);
        DissmissHandler.sendEmptyMessageDelayed(View.GONE, dismiss_tims);
    }

    private void initView() {
        ll_time_down=(RelativeLayout)findViewById(R.id.ll_time_down);
        full_bottom=(RelativeLayout)findViewById(R.id.full_bottom);//底部所有区域container

        ll_containtop=(LinearLayout) findViewById(R.id.top);//包裹倒计时的contain
        tv_counttime=(TextView) findViewById(R.id.tv_counttime);//倒计时

        room_money=(TextView) findViewById(R.id.room_money);//房间投注金额
        lottery_title=(TextView) findViewById(R.id.lottery_title);//竞猜题目
        my_bet_money=(TextView) findViewById(R.id.my_bet_money);//我的投注金额
        predict_money=(TextView) findViewById(R.id.predict_money);//我的预计收益

        left_check=(LinearLayout) findViewById(R.id.left_check);//左边选择项
        left_answer=(TextView) findViewById(R.id.left_answer);//左边答案
        left_total_money=(TextView) findViewById(R.id.left_total_money);//左边投注总金额

        right_check=(LinearLayout) findViewById(R.id.right_check);//右边选择项
        right_answer=(TextView) findViewById(R.id.right_answer);//右边答案
        right_all_money=(TextView) findViewById(R.id.right_all_money);//右边投注总金额

        iv_light_left=(ImageView) findViewById(R.id.iv_light_left);//左边图片
        tv_left_light=(TextView) findViewById(R.id.tv_left_light);//左边每注金额展示

        iv_light_right=(ImageView) findViewById(R.id.iv_light_right);//右边图片
        tv_right_light=(TextView) findViewById(R.id.tv_right_light);//右边每注金额展示

        right_check.setOnClickListener(this);
        left_check.setOnClickListener(this);
        ll_time_down.setOnClickListener(this);

    }

    private void setData(lotteryDetailInfo lotinfo, String type) {
        left_answer.setText(lotinfo.getQue_info().getOption_left());
        left_total_money.setText(lotinfo.getQue_info().getTotal_left());
        right_answer.setText(lotinfo.getQue_info().getOption_right());
        right_all_money.setText(lotinfo.getQue_info().getTotal_right());
        room_money.setText("[每注" + lotinfo.getQue_info().getRoom_bet()+"]");
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
        full_bottom.setVisibility(View.VISIBLE);
        if (type.equals("4")) {//显示倒计时
            ll_containtop.setVisibility(View.VISIBLE);
            CountTime(lotinfo.getQue_info().getStop_time_s());
        } else {
            //隐藏倒计时
            ll_containtop.setVisibility(View.INVISIBLE);
        }


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

    public void Showloction(){
        fulllotteryback achor = (fulllotteryback)context;
        achor.isDissmiss("1");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_check:
                //如果参数为null的话，会将所有的Callbacks和Messages全部清除掉。
                //实现7秒消失,连点不消失
                DissmissHandler.removeCallbacksAndMessages(null);
                canyuingcai("2", right_text);
                break;
            case R.id.left_check:
                DissmissHandler.removeCallbacksAndMessages(null);
                canyuingcai("1", left_text);
                break;
            case R.id.ll_time_down:
                dismiss();
                break;
        }
    }

    private void canyuingcai(final String sel,String answer) {
        //参与成功后改变颜色
        this.sel=sel;
        Log.e("参与竞猜:",Preference.room_id+"__"+lotteryid+"__"+answer+"__"+roommoney);
        ConfigUtils.sendLotteryResult((Activity) context, Preference.room_id, lotteryid, answer, roommoney, new ConfigUtils.ResultListener() {
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
                DissmissHandler.sendEmptyMessageDelayed(View.GONE, dismiss_tims);
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
                DissmissHandler.sendEmptyMessageDelayed(View.GONE, dismiss_tims);
            }
        });
    }

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

    public void HideView(){
        full_bottom.setVisibility(View.GONE);
        ll_containtop.setVisibility(View.GONE);
        fulllotteryback achor = (fulllotteryback) context;
        achor.isDissmiss("0");
    }
}
