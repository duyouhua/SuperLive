package com.joytouch.superlive.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.LotteryInfo;
import com.joytouch.superlive.javabean.SendLotteryResult;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.OpenLotteryDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by yj on 2016/5/10.
 * 全屏竞猜列表
 */
public class FullScreenLottery extends AppBaseAdapter<LotteryInfo> {
    public FullScreenLottery(List<LotteryInfo> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_fulllotterylist,null);
            holder = new ViewHolder();
            holder.normalLottery = (RelativeLayout) convertView.findViewById(R.id.normalLottery);
            holder.last = (ImageView) convertView.findViewById(R.id.last);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.left = (Button) convertView.findViewById(R.id.left);
            holder.leftgold = (TextView) convertView.findViewById(R.id.left_gold);
            holder.right = (Button) convertView.findViewById(R.id.right);
            holder.rightgold = (TextView) convertView.findViewById(R.id.right_gold);
            holder.stoptime = (TextView) convertView.findViewById(R.id.stoptime);
            holder.clock = (TextView) convertView.findViewById(R.id.clock);
            holder.leftCotainer = (RelativeLayout) convertView.findViewById(R.id.container_left);
            holder.rightCotainer = (RelativeLayout) convertView.findViewById(R.id.container_right);
            holder.oddsLottery = (RelativeLayout) convertView.findViewById(R.id.oddslottery);
            holder.oddsTitle = (TextView) convertView.findViewById(R.id.odds_title);
            holder.oddsLeft = (Button) convertView.findViewById(R.id.odds_left);
            holder.oddsLeftGold = (TextView) convertView.findViewById(R.id.odds_leftgold);
            holder.oddsRight = (Button) convertView.findViewById(R.id.odds_right);
            holder.oddsRightGold = (TextView) convertView.findViewById(R.id.odds_rightgold);
            holder.oddslefthead = (CircleImageView) convertView.findViewById(R.id.odds_lefthead);
            holder.oddsrighthead = (CircleImageView) convertView.findViewById(R.id.odds_righthead);
            holder.oddsLeftContainer = (RelativeLayout) convertView.findViewById(R.id.odds_container_left);
            holder.oddsRightContainer = (RelativeLayout) convertView.findViewById(R.id.odds_container_right);
            holder.oddsll = (LinearLayout) convertView.findViewById(R.id.oddsll);
            holder.odds = (TextView) convertView.findViewById(R.id.odds);
            holder.oddsStop = (TextView) convertView.findViewById(R.id.odds_stoptime);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LotteryInfo info = list.get(position);
        holder.last.setVisibility(View.GONE);
        holder.title.setText(info.getTitle());
        holder.left.setText(info.getLeftselector());
        holder.leftgold.setText(info.getLeftgold());
        holder.right.setText(info.getRightselector());
        holder.rightgold.setText(info.getRightgold());
        //holder.stoptime.setText(info.getEndtime());
        holder.stoptime.setTextColor(context.getResources().getColor(R.color.white));
        //判断是否是擂台题
        if(info.isOdds()){
            holder.oddsTitle.setText(info.getTitle());
            holder.oddsLeft.setText(info.getLeftselector());
            holder.oddsLeftGold.setText(info.getLeftgold());
            holder.oddsRight.setText(info.getRightselector());
            holder.oddsRightGold.setText(info.getRightgold());
            holder.odds.setText(info.getOdds());
            holder.oddsStop.setText(info.getEndtime()+"截止");
            holder.normalLottery.setVisibility(View.GONE);
            holder.oddsLottery.setVisibility(View.VISIBLE);
            holder.oddslefthead.setVisibility(View.GONE);
            holder.oddsrighthead.setVisibility(View.GONE);
            holder.oddsLeft.setEnabled(true);
            holder.oddsRight.setEnabled(true);
            //判断自己是否选择
            if(info.isSelf()){
                if(info.getSelfeSelector() == 0){
                    holder.oddsRight.setEnabled(false);
                }else{
                    holder.oddsLeft.setEnabled(false);
                }
            }
            //判断擂台题是否已经选择
            if(info.isOddsSelector()){
                //判断哪边被选择
                if(info.getOddsSelector()==0){
                    holder.oddslefthead.setVisibility(View.VISIBLE);
                    //判断自己是否是擂主
                    if(info.getOddsSelector()==info.getSelfeSelector()){
                        holder.right.setEnabled(false);
                        holder.oddslefthead.setBorderColor(context.getResources().getColor(R.color.color_blue));
                        holder.oddslefthead.setBorderWidth(ConfigUtils.dip2px(context, 1));
                    }else{
                        holder.left.setEnabled(false);
                        holder.oddslefthead.setBorderWidth(ConfigUtils.dip2px(context, 0));
                    }
                }else {
                    holder.oddsrighthead.setVisibility(View.VISIBLE);
                    if(info.getOddsSelector()==info.getSelfeSelector()){
                        holder.left.setEnabled(false);
                        holder.oddsrighthead.setBorderColor(context.getResources().getColor(R.color.color_blue));
                        holder.oddsrighthead.setBorderWidth(ConfigUtils.dip2px(context, 1));
                    }else{
                        holder.right.setEnabled(false);
                        holder.oddsrighthead.setBorderWidth(ConfigUtils.dip2px(context, 0));
                    }
                }
            }
            //判断是否是结束
            if(info.isEnd()){
                holder.oddsLeft.setEnabled(false);
                holder.oddsRight.setEnabled(false);
                holder.oddslefthead.setBorderWidth(ConfigUtils.dip2px(context, 0));
                holder.oddsrighthead.setBorderWidth(ConfigUtils.dip2px(context, 0));
                //判断是否是自己
                if(info.isSelf()){
                    if(info.getSelfeSelector() == 0){
                        holder.oddsLeft.setBackgroundResource(R.drawable.retangle_b1);
                    }else {
                        holder.oddsRight.setBackgroundResource(R.drawable.retangle_b1);
                    }
                }

            }

            holder.oddsLeftGold.setText(info.getLeftgold());
            holder.oddsRightGold.setText(info.getRightgold());
            holder.odds.setText(info.getOdds());
            holder.oddsLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    holder.oddsRight.setEnabled(false);
                    list.get(position).setIsSelf(true);
                    list.get(position).setSelfeSelector(0);
                    if(!list.get(position).isOdds()){
                        list.get(position).setIsOdds(true);
                        list.get(position).setOddsSelector(0);
                        list.get(position).setImagehead((String) SPUtils.get(context, Preference.headPhoto, "", Preference.preference));
                        holder.oddslefthead.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage((String) SPUtils.get(context, Preference.headPhoto, "", Preference.preference)
                        ,holder.oddslefthead, ImageLoaderOption.optionsHeader);
                    }
                    ConfigUtils.flash(context, holder.leftCotainer,list.get(position).getBettingGolde());
                }
            });
            holder.oddsRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.oddsLeft.setEnabled(false);
                    list.get(position).setIsSelf(true);
                    list.get(position).setSelfeSelector(1);
                    if(!list.get(position).isOdds()){
                        list.get(position).setIsOdds(true);
                        list.get(position).setOddsSelector(1);
                        list.get(position).setImagehead((String) SPUtils.get(context, Preference.headPhoto, "", Preference.preference));
                        holder.oddsrighthead.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage((String) SPUtils.get(context, Preference.headPhoto, "", Preference.preference)
                                ,holder.oddsrighthead, ImageLoaderOption.optionsHeader);
                    }
                    ConfigUtils.flash(context, holder.rightCotainer,list.get(position).getBettingGolde());
                }
            });
        }else {
            holder.normalLottery.setVisibility(View.VISIBLE);
            holder.oddsLottery.setVisibility(View.GONE);
            holder.left.setEnabled(true);
            holder.right.setEnabled(true);
            holder.stoptime.setEnabled(false);
            //判断是否是结束
            if(info.isEnd()){
                holder.left.setEnabled(false);
                holder.right.setEnabled(false);
                //判断是否是自己
                if(info.isSelf()){
                    if(info.getSelfeSelector() == 0){
                        holder.left.setBackgroundResource(R.drawable.retangle_b1);
                    }else {
                        holder.right.setBackgroundResource(R.drawable.retangle_b1);
                    }
                }
                if(info.isOpenLottery()){
                    holder.clock.setVisibility(View.GONE);
                    holder.stoptime.setVisibility(View.VISIBLE);
                    holder.stoptime.setText("已开奖");
                    holder.stoptime.setEnabled(false);
                    holder.stoptime.setTextColor(context.getResources().getColor(R.color.main));
                }else{
                    holder.clock.setVisibility(View.GONE);
                    holder.stoptime.setVisibility(View.VISIBLE);
                    holder.stoptime.setText("我要开奖");
                    holder.stoptime.setEnabled(true);
                    holder.stoptime.setTextColor(context.getResources().getColor(R.color.main));
                    holder.stoptime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OpenLotteryDialog dialog = new OpenLotteryDialog((Activity) context,info.getMatchid(),info.getLeftselector(),info.getRightselector(),info.getTitle());
                            dialog.setIsOpenLiving(info.isSelfOpenLiving());
                            dialog.show();
                        }
                    });
                }
            }else{
                if(info.isSelf()){
                    if(info.getSelfeSelector() == 0){
                        holder.right.setEnabled(false);
                    } else {
                        holder.left.setEnabled(false);
                    }
                }
            }

            holder.leftgold.setText(info.getLeftgold());
            holder.rightgold.setText(info.getRightgold());
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
                        msg.arg1 = position;
                        msg.obj = holder;
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
            holder.left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ConfigUtils.sendLotteryResult((Activity) context,Preference.room_id, info.getMatchid(), info.getLeftselector(), info.getBettingGolde(), new ConfigUtils.ResultListener() {
                        @Override
                        public void result(SendLotteryResult result) {
                            ConfigUtils.flash(context, holder.leftCotainer,info.getBettingGolde());
                            holder.right.setEnabled(false);
                            list.get(position).setIsSelf(true);
                            list.get(position).setSelfeSelector(0);
                            holder.leftgold.setText(result.getLeftgold());
                            holder.rightgold.setText(result.getRightgold());
                        }

                        @Override
                        public void last(String islast) {

                        }
                    });

                }
            });
            holder.right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConfigUtils.sendLotteryResult((Activity) context,Preference.room_id, info.getMatchid(), info.getRightselector(), info.getBettingGolde(), new ConfigUtils.ResultListener() {
                        @Override
                        public void result(SendLotteryResult result) {
                            holder.left.setEnabled(false);
                            list.get(position).setIsSelf(true);
                            list.get(position).setSelfeSelector(1);
                            holder.leftgold.setText(result.getLeftgold());
                            holder.rightgold.setText(result.getRightgold());
                            ConfigUtils.flash(context, holder.rightCotainer,info.getBettingGolde());
                        }

                        @Override
                        public void last(String islast) {

                        }
                    });

                }
            });
        }

        return convertView;
    }
    Handler handlers = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            long nowtime = msg.getData().getLong("timestr");
            final LotteryInfo info = list.get(msg.arg1);
            long endtime = info.getStopTime();
            LogUtils.e("&&&****", "postion = " + msg.arg1 + " endtime =" + endtime);
            long time = endtime  - nowtime;
            LogUtils.e("&&&****","time = " +time);
            final ViewHolder holder = (ViewHolder) msg.obj;
            holder.last.setVisibility(View.GONE);
            if(time>30000) {
                    holder.clock.setVisibility(View.GONE);
                    holder.stoptime.setVisibility(View.VISIBLE);
                    holder.stoptime.setText(list.get(msg.arg1).getEndtime() + "截止");

            }
            if(time<=30000&&time>0){

                    holder.clock.setVisibility(View.VISIBLE);
                    holder.stoptime.setVisibility(View.GONE);
                    holder.last.setVisibility(View.VISIBLE);
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
                            holder.clock.setText(miao + "s" );


                    }

                    public void onFinish() {
                        list.get(msg.arg1).setIsEnd(true);
                            holder.left.setEnabled(false);
                            holder.right.setEnabled(false);
                            holder.clock.setVisibility(View.GONE);
                            holder.stoptime.setVisibility(View.VISIBLE);
                            holder.stoptime.setText("待开奖");
                            holder.stoptime.setTextColor(context.getResources().getColor(R.color.main));
                            holder.last.setVisibility(View.GONE);
                            holder.last.setVisibility(View.GONE);

                    }
                };

                Count.start();
            }
        }
    };
    private class ViewHolder{
        RelativeLayout normalLottery;
        ImageView last;
        TextView title;
        Button left;
        Button right;
        TextView leftgold;
        TextView rightgold;
        TextView stoptime;
        TextView clock;
        RelativeLayout leftCotainer;
        RelativeLayout rightCotainer;
        RelativeLayout oddsLottery;
        TextView oddsTitle;
        Button oddsLeft;
        Button oddsRight;
        TextView oddsLeftGold;
        TextView oddsRightGold;
        CircleImageView oddslefthead;
        CircleImageView oddsrighthead;
        RelativeLayout oddsLeftContainer;
        RelativeLayout oddsRightContainer;
        LinearLayout oddsll;
        TextView odds;
        TextView oddsStop;



    }
}
