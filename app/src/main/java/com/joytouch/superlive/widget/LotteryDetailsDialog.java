package com.joytouch.superlive.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.GuessDetail;

import java.util.List;

/**
 * Created by sks on 2016/4/28.
 * 直播页竞猜列表的竞猜详情
 */
public class LotteryDetailsDialog extends Dialog implements View.OnClickListener{
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
    private RelativeLayout ll_refresh;//刷新布局
    private ImageView iv_refresh;//刷新图片（动画）
    private TextView tv_touzhu_num;//我的投注（刷新布局中）
    private TextView tv_shouyi_num;//预计收益（刷新布局中）
    private RelativeLayout rl_refresh;//刷新操作（刷新布局中）
    private LinearLayout ll_xuanxiang;//选项布局(用于隐藏)
    private ImageView iv_win_l;//赢的图片左
    private ImageView iv_win_r;//赢的图片右
    private TextView tv_option_l;//左侧选项（选项布局中）
    private TextView tv_gold_left;//左侧金币数（选项布局中）
    private TextView tv_gold_right;//右侧金币数（选项布局中）
    private PullToRefreshLayout refreshLayout;//参加竞猜人的刷新
    private MyListView listView_l;//左侧列表
    private MyListView listView_r;//右侧列表

    private List<GuessDetail> detailsl;//左侧投注人
    private List<GuessDetail> detailsr;//右侧投注人

    public LotteryDetailsDialog(Context context) {
        super(context, R.style.Dialog_bocop);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_lottery_details);
        initView();
    }

    private void initView() {
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
        tv_gold_left = (TextView) this.findViewById(R.id.tv_gold_left);
        tv_gold_right = (TextView) this.findViewById(R.id.tv_gold_right);
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refreshLayout.setCanPullDown(false);
        listView_l = (MyListView) this.findViewById(R.id.listView_l);
        listView_r = (MyListView) this.findViewById(R.id.listView_r);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                dismiss();
                break;
        }
    }
}
