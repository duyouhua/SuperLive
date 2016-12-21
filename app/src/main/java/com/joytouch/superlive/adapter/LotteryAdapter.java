package com.joytouch.superlive.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LotteryDetailsActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.LotteryInfo;
import com.joytouch.superlive.javabean.LotteryList;
import com.joytouch.superlive.javabean.SendLotteryResult;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.OpenLotteryDialog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by yj on 2016/4/27.
 * 聊竞猜标签
 */
public class LotteryAdapter extends BaseExpandableListAdapter {
    private List<LotteryList> list;
    private LayoutInflater inflater;
    private Activity context;
    public LotteryAdapter(List<LotteryList> list,Activity context) {
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getInfo().size();
    }

    @Override
    public Object getGroup(int i) {
        return list.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getInfo().get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolderGroup group;
        if(view == null){
            view = inflater.inflate(R.layout.adapter_group,null);
            group = new ViewHolderGroup();
            group.name = (TextView) view.findViewById(R.id.group_name);
            view.setTag(group);
        }else {
            group = (ViewHolderGroup) view.getTag();
        }
        group.name.setText(list.get(i).getTitle());
        group.name.setEnabled(false);
        group.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderChildren children;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_lotteryitem,null);
            children = new ViewHolderChildren();
            children.detail = (TextView) convertView.findViewById(R.id.detail);
            children.title = (TextView) convertView.findViewById(R.id.title);
            children.left = (Button) convertView.findViewById(R.id.left_selector);
            children.right = (Button) convertView.findViewById(R.id.right_selector);
            children.lefthead = (CircleImageView) convertView.findViewById(R.id.head_left);
            children.righthead = (CircleImageView) convertView.findViewById(R.id.head_right);
            children.odds = (TextView) convertView.findViewById(R.id.odds);
            children.odds_tadio = (TextView) convertView.findViewById(R.id.odds_ratio);
            children.state = (TextView) convertView.findViewById(R.id.state);
            children.detailSuccess = (TextView) convertView.findViewById(R.id.success_detail);
            children.normaltitle = (LinearLayout) convertView.findViewById(R.id.normalTitle);
            children.successrl = (RelativeLayout) convertView.findViewById(R.id.win);
            children.leftGold = (TextView) convertView.findViewById(R.id.left_gold);
            children.rightGold = (TextView) convertView.findViewById(R.id.right_gold);
            children.gold = (TextView) convertView.findViewById(R.id.success_gold);
            children.containerleft = (RelativeLayout) convertView.findViewById(R.id.container_left);
            children.containerright = (RelativeLayout) convertView.findViewById(R.id.container_right);
            convertView.setTag(children);
        }else {
            children = (ViewHolderChildren) convertView.getTag();
        }
        final LotteryInfo info = list.get(groupPosition).getInfo().get(childPosition);
        //动态改变button的背景色
        children.lefthead.setVisibility(View.GONE);
        children.righthead.setVisibility(View.GONE);
        children.left.setEnabled(true);
        children.right.setEnabled(true);
        children.leftGold.setText(info.getLeftgold());
        children.rightGold.setText(info.getRightgold());
        children.left.setText(info.getLeftselector());
        children.right.setText(info.getRightselector());
        children.gold.setText("+"+info.getWingold());
        children.title.setText(info.getTitle());
        children.odds_tadio.setText(info.getOdds());
       //判断是不是擂台题
        if(info.isOdds()){
            children.left.setBackgroundResource(R.drawable.retangle_blue_gray);
            children.right.setBackgroundResource(R.drawable.retangle_blue_gray);
            children.gold.setTextColor(context.getResources().getColor(R.color.color_blue));
            children.odds.setVisibility(View.VISIBLE);
            children.odds_tadio.setVisibility(View.VISIBLE);
            //处理擂主头像问题
            //判断擂台题是否有擂主
            if (info.isOddsSelector()) {
                //判断擂主在哪边
                if (info.getOddsSelector() == 0) {
                    children.lefthead.setVisibility(View.VISIBLE);
                    children.left.setEnabled(false);
                } else {
                    children.righthead.setVisibility(View.VISIBLE);
                    children.right.setEnabled(false);
                }
            }

        }else {

            children.left.setBackgroundResource(R.drawable.retangle_yellow_gray);
            children.right.setBackgroundResource(R.drawable.retangle_yellow_gray);
            children.gold.setTextColor(context.getResources().getColor(R.color.color_yellow));
            children.odds.setVisibility(View.GONE);
            children.odds_tadio.setVisibility(View.GONE);

        }
        if(info.isEnd()&&info.isWin()){
            children.successrl.setVisibility(View.VISIBLE);
            children.normaltitle.setVisibility(View.GONE);
            children.lefthead.setVisibility(View.GONE);
            children.righthead.setVisibility(View.GONE);
        }else{
            children.normaltitle.setVisibility(View.VISIBLE);
            children.successrl.setVisibility(View.GONE);
        }
        if(info.isEnd()) {
            children.left.setEnabled(false);
            children.right.setEnabled(false);
            //已经截止的比赛
            if (info.isOpenLottery()) {
                //判断是否赢
                if (info.isWin()) {
                    children.successrl.setVisibility(View.VISIBLE);
                    children.normaltitle.setVisibility(View.GONE);
                } else {
                    children.normaltitle.setVisibility(View.VISIBLE);
                    children.successrl.setVisibility(View.GONE);
                    children.state.setTextColor(context.getResources().getColor(R.color.editext_hint));
                    children.state.setText("已开奖");
                }
            } else {
                if (info.getAdd_user_id().equals(SPUtils.get(context,Preference.myuser_id,"",Preference.preference))){
                    children.state.setTextColor(context.getResources().getColor(R.color.color_yellow));
                    children.state.setText("我要开奖");
                    children.state.setEnabled(true);
                    children.state.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //开奖
                            final OpenLotteryDialog openLotteryDialog = new OpenLotteryDialog(context,info.getMatchid(),info.getLeftselector(),info.getRightselector(),info.getTitle());
                            openLotteryDialog.show();
                            openLotteryDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (openLotteryDialog.isOpend()){
                                        children.state.setTextColor(context.getResources().getColor(R.color.editext_hint));
                                        children.state.setText("已开奖");
                                        children.state.setEnabled(false);
                                    }else {
                                        children.state.setTextColor(context.getResources().getColor(R.color.color_yellow));
                                        children.state.setText("我要开奖");
                                        children.state.setEnabled(true);
                                    }
                                }
                            });
                        }
                    });
                }else {
                    children.state.setTextColor(context.getResources().getColor(R.color.main));
                    children.state.setText("未开奖");
                    children.state.setEnabled(false);
                }

            }
            if(!info.isWin()) {
                //判断自己是否参加
                if (info.isSelf()) {
                    //判断自己选择了哪边
                    if (info.getSelfeSelector() == 0) {
                        children.left.setEnabled(false);
                        children.left.setBackgroundResource(R.drawable.retangle_b1);
                    } else {
                        children.right.setEnabled(false);
                        children.right.setBackgroundResource(R.drawable.retangle_b1);
                    }

                }
            }
        }else{
            //children.state.setText(info.getEndtime());
            children.left.setEnabled(true);
            children.right.setEnabled(true);
            if(!info.isWin()) {
                //判断自己是否参加
                if (info.isSelf()) {
                    //判断自己选择了哪边
                    if (info.getSelfeSelector() == 0) {
                        children.right.setEnabled(false);
                    } else {
                        children.left.setEnabled(false);
                    }

                }
            }
        }

        children.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConfigUtils.sendLotteryResult((Activity) context, Preference.room_id, info.getMatchid(), info.getLeftselector(), info.getBettingGolde(), new ConfigUtils.ResultListener() {
                    @Override
                    public void result(SendLotteryResult result) {
                        ConfigUtils.flash(context,children.containerleft,info.getBettingGolde());
                        children.leftGold.setText(result.getLeftgold());
                        children.rightGold.setText(result.getRightgold());
                        children.right.setEnabled(false);
                        list.get(groupPosition).getInfo().get(childPosition).setLeftgold(result.getLeftgold());
                        list.get(groupPosition).getInfo().get(childPosition).setRightgold(result.getRightgold());
                        list.get(groupPosition).getInfo().get(childPosition).setIsSelf(true);
                        list.get(groupPosition).getInfo().get(childPosition).setSelfeSelector(0);

                    }

                    @Override
                    public void last(String islast) {

                    }
                });
            }
        });
        children.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConfigUtils.sendLotteryResult((Activity) context,Preference.room_id, info.getMatchid(), info.getRightselector(), info.getBettingGolde(), new ConfigUtils.ResultListener() {
                    @Override
                    public void result(SendLotteryResult result) {
                        ConfigUtils.flash(context, children.containerright, info.getBettingGolde());
                        children.leftGold.setText(result.getLeftgold());
                        children.rightGold.setText(result.getRightgold());
                        children.left.setEnabled(false);
                        list.get(groupPosition).getInfo().get(childPosition).setLeftgold(result.getLeftgold());
                        list.get(groupPosition).getInfo().get(childPosition).setRightgold(result.getRightgold());
                        list.get(groupPosition).getInfo().get(childPosition).setIsSelf(true);
                        list.get(groupPosition).getInfo().get(childPosition).setSelfeSelector(1);
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
                        msg.arg1 = groupPosition;
                        msg.arg2 = childPosition;
                        msg.obj = children;
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
        children.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.put(context, "lotterydilog", true, Preference.preference);
                Intent intent = new Intent(context, LotteryDetailsActivity.class);
                intent.putExtra("qid",info.getMatchid());
                intent.putExtra("roomid", Preference.room_id);
                intent.putExtra("anchorid", Preference.zhubo_id);
                context.startActivity(intent);
            }
        });
        children.detailSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.put(context, "lotterydilog", true, Preference.preference);
                Intent intent = new Intent(context, LotteryDetailsActivity.class);
                intent.putExtra("qid",info.getMatchid());
                intent.putExtra("roomid", Preference.room_id);
                intent.putExtra("anchorid", Preference.zhubo_id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    Handler handlers = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            long nowtime = msg.getData().getLong("timestr");
            final LotteryInfo info = list.get(msg.arg1).getInfo().get(msg.arg2);
            long endtime = info.getStopTime();
            LogUtils.e("&&&****", "postion = " + msg.arg1 + " endtime =" + endtime);
            long time = endtime- nowtime;
            LogUtils.e("&&&****","time = " +time);
            final ViewHolderChildren holder = (ViewHolderChildren) msg.obj;
            if(time>30000) {
                holder.state.setTextColor(context.getResources().getColor(R.color.editext_hint));
                holder.state.setText(info.getEndtime() + "截止");

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
                        holder.state.setTextColor(context.getResources().getColor(R.color.color_yellow));
                        holder.state.setText(miao + "s");

                    }

                    public void onFinish() {
                        list.get(msg.arg1).getInfo().get(msg.arg2).setIsEnd(true);
                        holder.left.setEnabled(false);
                        holder.right.setEnabled(false);
                        holder.state.setText("待开奖");
                        holder.state.setTextColor(context.getResources().getColor(R.color.main));

                    }
                };

                Count.start();
            }
        }
    };
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    private class ViewHolderChildren{
        TextView title;//竞猜题目
        Button left;//左边投注按钮
        Button right;//右边投注按钮
        CircleImageView lefthead;//左边抢擂台者的头像
        CircleImageView righthead;//右边抢擂台的头像
        TextView detail;//正常题的详情
        TextView odds;//擂台赔率字样
        TextView odds_tadio;//擂台的比率
        TextView state;//当前题的状态
        TextView detailSuccess;//赢的详情按钮
        LinearLayout normaltitle;//除赢以外的界面
        RelativeLayout successrl;//赢的界面
        TextView leftGold;//左边投注金额
        TextView rightGold;//右边投注金额
        TextView gold;//赢的金额
        RelativeLayout containerleft;
        RelativeLayout containerright;

    }
    private class ViewHolderGroup{
        TextView name;
    }
}
