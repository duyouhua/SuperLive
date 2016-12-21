package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.LoteryListAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.MyGuess;
import com.joytouch.superlive.javabean.Mylotterylist;
import com.joytouch.superlive.javabean.mylottery_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 我的竞猜列表
 */
public class MyGuessListActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,ExpandableListView.OnChildClickListener{
    private ImageView iv_finish;
    private ImageView iv_right;
    private TextView tv_title;
    private PullToRefreshLayout refresh;
    private List<MyGuess> myGuesses;
    private PullableListView listView;
    private int time = 1;//刷新或开始传"1",加载传最后一条time
    private String last_id = "0";//记录最后一个条目的position,定位
    private boolean isUp=false;//是否是上啦加载
    private List<mylottery_item> groups = new ArrayList<mylottery_item>();;
    private SharedPreferences sp;
    private CommonShowView loadstate;
    private LoteryListAdapter adapter;
    private TextView tv_score;
    private TextView tv_guess_times;
    private TextView tv_percent;
    private double precent;
    private List<BaseBean> messagelist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myguesslist);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
        //获取网络数据
        refresh(true);
    }

    private void refresh(boolean isFirst) {
        //true代表刷新状态,false代表上拉加载
        if (isFirst) {
            time = 1;
            last_id = "0";
            isUp=false;
            //注意: 必须notifyDataSetChanged,不然报错
            groups.clear();
            adapter.notifyDataSetChanged();
            getdata();
        } else {
            isUp=true;
            getdata();
        }
    }
    double cf;
    private void getdata() {
        if(isUp){
//            time+=1;
        }else{
            time=1;
        }
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("pages", time+"")
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        Log.e("我的竞猜值", sp.getString(Preference.token,"")+"__"+time+"__");
        new HttpRequestUtils(this).httpPost(Preference.Myquelist, build,
                new HttpRequestUtils.ResRultListener() {

                    @Override
                    public void onFailure(IOException e) {
                        //访问网络失败后,隐藏动画.显示重新加载
                        loadstate.stopAnimal();
                        loadstate.setVisibility(View.VISIBLE);
                        loadstate.setfail();
                        loadstate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadstate.setloading();
                                loadstate.starAnimal();
                                refresh(true);
                                isUp = false;
                            }
                        });
                        refresh.refreshFinish(PullToRefreshLayout.FAIL);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("我的竞猜", json);
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        Gson gson = new Gson();
                        Mylotterylist Bean = gson.fromJson(json, Mylotterylist.class);
                        if (Bean.status.equals("_0000")) {
                            messagelist=Bean.str;
                            time+=1;
                            //拿到最后一个条目的位置,和最后一条的time
                            if (groups.size() > 0) {
                                last_id = String.valueOf(groups.size() - 1);
                            }
                            //获取所有的集合
                            groups.addAll(Bean.list);
                            if (groups.size() > 0) {
                                //有数据的情况下,隐藏界面
                                loadstate.setVisibility(View.GONE);
                            } else {
                                //如果没有数据,显示暂无数据
                                loadstate.setnull();
                            }
                            adapter=new LoteryListAdapter(groups,MyGuessListActivity.this,messagelist);
                            tv_score.setText(Bean.win_money);
                            tv_guess_times.setText("竞猜次数："+Bean.total);
                            Log.e("我的竞猜数据",Bean.win_count+"___"+Bean.total+"__"+Bean.count);
                            //当总数为0时不执行
                            if (!Bean.count.equals("0")){
                                cf = div((double)Integer.parseInt(Bean.win_count),(double) Integer.parseInt(Bean.count),2);
                                Log.e("哈哈哈",cf+"");
                                precent = mul((double) 100, cf);
                                tv_percent.setText("胜率："+precent+"%");
                            }else{
                                tv_percent.setText("0%");
                            }
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            //上拉加载时,选择最后一个条目固定在底端
                            listView.setSelection(Integer.parseInt(last_id));
                        } else if (Bean.status.equals("_1000")) {
                            new LoginUtils(MyGuessListActivity.this).reLogin(MyGuessListActivity.this);
                            Toast.makeText(MyGuessListActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyGuessListActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {
        tv_percent=(TextView)this.findViewById(R.id.tv_percent);
        tv_guess_times=(TextView)this.findViewById(R.id.tv_guess_times);
        tv_score=(TextView)this.findViewById(R.id.tv_score);
        loadstate=(CommonShowView)this.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        listView=(PullableListView)this.findViewById(R.id.listView);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("竞猜记录");
        iv_right = (ImageView) this.findViewById(R.id.iv_right);
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setBackgroundResource(R.drawable.money_white);
        iv_right.setOnClickListener(this);
        refresh = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        adapter=new LoteryListAdapter(groups,this,messagelist);
        listView.setAdapter(adapter);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.iv_right:
                toActivity(ChargeActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refresh(true);
        isUp=false;
        refresh.refreshFinish(0);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        refresh(false);
        isUp = true;
        refresh.loadmoreFinish(0);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        toActivity(GuessDetailsActivity.class);
        return false;
    }

    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b2.multiply(b1).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1,double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
