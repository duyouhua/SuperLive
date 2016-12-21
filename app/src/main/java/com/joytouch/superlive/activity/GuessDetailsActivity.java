package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.my_lottGuessDetailsLeftAdapter;
import com.joytouch.superlive.adapter.mylottGuessDetailsRightAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.GuessDetail;
import com.joytouch.superlive.javabean.guess_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.Lotterydetail_ShareBottomDialog;
import com.joytouch.superlive.widget.MyListView;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

public class GuessDetailsActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener,View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private ImageView iv_right;
    private MyListView listView_l;
    private MyListView listView_r;
    private PullToRefreshLayout refreshLayout;
    private PullableScrollView scrollView;
    private BaseAnimatorSet bas_in;
    private BaseAnimatorSet bas_out;
    private Lotterydetail_ShareBottomDialog dialog;
    private LinearLayout root_detail;
    private String que_id;
    private String room_id;
    private SharedPreferences sp;
    private my_lottGuessDetailsLeftAdapter leftadapter;
    private mylottGuessDetailsRightAdapter rightadapter;
    private List<guess_item> left_details=new ArrayList<>();
    private List<guess_item> right_details=new ArrayList<>();
    private TextView tv_result_prompt;
    private TextView tv_score;
    private TextView tv_gold;
    private TextView tv_name;
    private String match_name;
    private TextView tv_time;
    private TextView tv_content;
    private TextView tv_your_result;
    private TextView tv_result;
    private TextView tv_gild;
    private TextView tv_option_l;
    private TextView tv_gold_l;
    private TextView tv_option_r;
    private TextView tv_gold_r;
    private String last_id_left = "0";//记录左边列表最后一个条目的position,定位
    private String last_id_right = "0";//记录右边列表最后一个条目的position,定位
    private String status;
    private int time=1;//刷新或开始传"1",加载传最后一条time
    private RelativeLayout rl_ping;
    private TextView tv_option_pin;
    private TextView ping_mon;
    private String meaage;
    private TextView tv_option_ping;
    private CommonShowView loadstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_details);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        que_id=getIntent().getStringExtra("que_id");
        room_id=getIntent().getStringExtra("room_id");
        match_name=getIntent().getStringExtra("name");
        meaage=getIntent().getStringExtra("meaage");
        iniView();
        getData();
        bas_in = new BounceTopEnter();
        bas_out = new SlideBottomExit();
    }

    private void getData() {
        time = 1;
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("room_id",room_id)
                .add("que_id",que_id)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.Quecetails, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        //访问网络失败后,隐藏动画.显示重新加载
                        loadstate.stopAnimal();
                        loadstate.setfail();
                        loadstate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadstate.setloading();
                                loadstate.starAnimal();
                                getData();

                            }
                        });
                        Toast.makeText(GuessDetailsActivity.this, "网络出错", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String json) {
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        refreshLayout.setVisibility(View.VISIBLE);
                        Log.e("竞猜详情页", json);
                        Gson gson = new Gson();
                        GuessDetail bean = gson.fromJson(json, GuessDetail.class);
                        if (bean.status.equals("_0000")) {
                            time+=1;
                            //填充下面的列表
                            left_details.addAll(bean.left);
                            right_details.addAll(bean.right);
                            //拿到最后一个条目的位置,和最后一条的time
                            if (left_details.size() > 0) {
                                last_id_left = String.valueOf(left_details.size() - 1);
                            }
                            if (right_details.size() > 0) {
                                last_id_right = String.valueOf(right_details.size() - 1);
                            }

                            if (bean.que_info.win_option.equals(bean.que_info.option_left)){
                                leftadapter=new my_lottGuessDetailsLeftAdapter(left_details,GuessDetailsActivity.this,"0");
                                rightadapter=new mylottGuessDetailsRightAdapter(right_details,GuessDetailsActivity.this,"0");
                                listView_l.setAdapter(leftadapter);
                                listView_r.setAdapter(rightadapter);

                            }else if (bean.que_info.win_option.equals(bean.que_info.option_right)){
                                leftadapter=new my_lottGuessDetailsLeftAdapter(left_details,GuessDetailsActivity.this,"1");
                                rightadapter=new mylottGuessDetailsRightAdapter(right_details,GuessDetailsActivity.this,"1");
                                listView_l.setAdapter(leftadapter);
                                listView_r.setAdapter(rightadapter);
                            }else{
                                leftadapter=new my_lottGuessDetailsLeftAdapter(left_details,GuessDetailsActivity.this,"2");
                                rightadapter=new mylottGuessDetailsRightAdapter(right_details,GuessDetailsActivity.this,"2");
                                listView_l.setAdapter(leftadapter);
                                listView_r.setAdapter(rightadapter);
                            }

                            //上拉加载时,选择最后一个条目固定在底端
                            listView_l.setSelection(Integer.parseInt(last_id_left));
                            listView_r.setSelection(Integer.parseInt(last_id_right));

                            status = bean.que_info.status;
                            tv_result_prompt.setText(meaage);
                            //根据不同的状态top不同的显示
                            if (status.equals("6")){//赢
                                tv_score.setText(bean.que_info.my_win_money);
                                tv_gold.setText("金币");
                                if (bean.que_info.show_type.equals("1")){
                                    tv_your_result.setText(bean.que_info.my_option +"[胜]");
                                    tv_result.setText(bean.que_info.my_option+"[胜]");
                                  }else{
                                    tv_your_result.setText(bean.que_info.my_option);
                                    tv_result.setText(bean.que_info.my_option);
                                }
                            }else if (status.equals("3")){//待开奖
                                tv_score.setText("待开奖");
                                tv_gold.setText("");
                                tv_result.setText("--");
                            }else if (status.equals("4")){//未中奖
                                tv_score.setText("继续努力吧");
                                tv_gold.setText("");
                                if (bean.que_info.my_option.equals(bean.que_info.option_left)){
                                    tv_result.setText(bean.que_info.option_right);
                                }else if (bean.que_info.my_option.equals(bean.que_info.option_right)){
                                    tv_result.setText(bean.que_info.option_left);
                                }else{
                                    tv_result.setText("--");
                                }

                            }else if (status.equals("2")){//进行中
                                tv_score.setText("该题正在竞猜");
                                tv_gold.setText("");
                                tv_result.setText("--");
                            }else  if (status.equals("5")){//已取消
                                tv_score.setText("该问题取消了");
                                tv_gold.setText("");
                                tv_result.setText("取消");
                            }else{
                                tv_result_prompt.setText("");
                                tv_score.setText("");
                                tv_gold.setText("");
                            }
                            tv_name.setText(match_name);
                            tv_time.setText(bean.que_info.stop_time.substring(6,bean.que_info.stop_time.length()));
                            tv_content.setText(bean.que_info.content);
                            tv_your_result.setText(bean.que_info.my_option);
                            tv_gild.setText(bean.que_info.my_option_money);
                            tv_gold_l.setText(bean.que_info.total_left);
                            tv_gold_r.setText(bean.que_info.total_right);
                            tv_option_l.setText(bean.que_info.option_left);
                            tv_option_r.setText(bean.que_info.option_right);

                            //胜平负
                            if (bean.que_info.show_type.equals("1") && (status.equals("6") || status.equals("4"))){
                                rl_ping.setVisibility(View.VISIBLE);
                                String item_list=bean.que_info.item_list;
                                String sl[] = item_list.split(",");
                                ArrayList<String> strs = new ArrayList<>();
                                for (int i=0;i<sl.length;i++){
                                    strs.add(sl[i]);
                                    if (sl[i].equals(tv_result.getText().toString())){
                                        if (i==0){
                                            tv_option_l.setTextColor(Color.parseColor("#FF7A00"));
                                        }else if (i==1){
                                            tv_option_ping.setTextColor(Color.parseColor("#FF7A00"));
                                        }else if (i==2){
                                            tv_option_r.setTextColor(Color.parseColor("#FF7A00"));
                                        }
                                    }
                                }
                                if (strs.size()==3){
                                    tv_option_l.setText("胜: " + strs.get(0));
                                    tv_option_pin.setText(strs.get(1));
                                    tv_option_r.setText("胜: "+strs.get(2));
                                    tv_gold_l.setText(bean.que_info.total_money.get(0));
                                    ping_mon.setText(bean.que_info.total_money.get(1));
                                    tv_gold_r.setText(bean.que_info.total_money.get(2));
                                }else if (strs.size()==2){
                                    tv_option_l.setText("胜: "+strs.get(0));
                                    tv_option_l.setTextColor(Color.parseColor("#FF7A00"));
                                    tv_option_r.setText("胜: "+strs.get(1));
                                    tv_gold_l.setText(bean.que_info.total_money.get(0));
                                    tv_gold_r.setText(bean.que_info.total_money.get(1));
                                    rl_ping.setVisibility(View.GONE);
                                }
                            }
                          //非胜平负:
                          if (!bean.que_info.show_type.equals("1")){
                                if (tv_result.getText().equals(tv_option_l.getText().toString())){
                                    tv_option_l.setTextColor(Color.parseColor("#FF7A00"));
                                }else if (tv_result.getText().equals(tv_option_r.getText().toString())){
                                    tv_option_r.setTextColor(Color.parseColor("#FF7A00"));
                                }
                            }} else if (bean.status.equals("_1000")){
                                new LoginUtils(GuessDetailsActivity.this).reLogin(GuessDetailsActivity.this);
                                Toast.makeText(GuessDetailsActivity.this, bean.message, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(GuessDetailsActivity.this, bean.message, Toast.LENGTH_SHORT).show();
                            }

                    }
                });
    }

    private void iniView() {
        loadstate=(CommonShowView)this.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        ping_mon=(TextView)findViewById(R.id.ping_mon);
        tv_option_pin=(TextView)findViewById(R.id.tv_option_ping);
        rl_ping=(RelativeLayout)findViewById(R.id.rl_ping);
        tv_gold_r=(TextView)findViewById(R.id.tv_gold_r);
        tv_option_r=(TextView)findViewById(R.id.tv_option_r);
        tv_gold_l=(TextView)findViewById(R.id.tv_gold_l);
        tv_option_l=(TextView)findViewById(R.id.tv_option_l);
        tv_option_ping=(TextView)findViewById(R.id.tv_option_ping);
        tv_gild=(TextView)findViewById(R.id.tv_gild);
        tv_result=(TextView)findViewById(R.id.tv_result);
        tv_your_result=(TextView)findViewById(R.id.tv_your_result);
        tv_content=(TextView)findViewById(R.id.tv_content);
        tv_time=(TextView)findViewById(R.id.tv_time);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_gold=(TextView)findViewById(R.id.tv_gold);
        tv_score=(TextView)findViewById(R.id.tv_score);
        tv_result_prompt=(TextView)findViewById(R.id.tv_result_prompt);
        root_detail=(LinearLayout)findViewById(R.id.root_detail);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("竞猜详情");
        iv_right = (ImageView) this.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setBackgroundResource(R.drawable.share);
        iv_right.setOnClickListener(this);
        iv_right.setVisibility(View.GONE);//这个分享暂时不做
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        scrollView = (PullableScrollView) this.findViewById(R.id.scrollView);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setCanPullDown(false);
        listView_l = (MyListView) this.findViewById(R.id.listView_l);
        listView_r = (MyListView) this.findViewById(R.id.listView_r);
        scrollView.smoothScrollBy(0,0);
    }



    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("pages",time+"")
                .add("status",status)
                .add("room_id", room_id)
                .add("que_id", que_id)
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.Partuserlist, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("上啦加载底步listview",json);
                        Gson gson = new Gson();
                        GuessDetail Bean = gson.fromJson(json, GuessDetail.class);
                        if (Bean.status.equals("_0000")) {
                            time+=1;
                            //填充下面的列表
                            left_details.addAll(Bean.left);
                            right_details.addAll(Bean.right);
                            //拿到最后一个条目的位置,和最后一条的time
                            if (left_details.size() > 0) {
                                last_id_left = String.valueOf(left_details.size() - 1);
                            }
                            if (right_details.size() > 0) {
                                last_id_right = String.valueOf(right_details.size() - 1);
                            }
                            listView_l.setAdapter(leftadapter);
                            listView_r.setAdapter(rightadapter);
                            //上拉加载时,选择最后一个条目固定在底端
                            listView_l.setSelection(Integer.parseInt(last_id_left));
                            listView_r.setSelection(Integer.parseInt(last_id_right));
                        } else if (Bean.status.equals("_1000")){
                            new LoginUtils(GuessDetailsActivity.this).reLogin(GuessDetailsActivity.this);
                            Toast.makeText(GuessDetailsActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(GuessDetailsActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.iv_right:
                dialog = new Lotterydetail_ShareBottomDialog(GuessDetailsActivity.this, root_detail);
                dialog.showAnim(bas_in) .show();
                break;
        }
    }
    public void setBasIn(BaseAnimatorSet bas_in) {
        this.bas_in = bas_in;
    }

    public void setBasOut(BaseAnimatorSet bas_out) {
        this.bas_out = bas_out;
    }
}
