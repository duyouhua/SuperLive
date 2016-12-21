package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.AppBaseAdapter;
import com.joytouch.superlive.adapter.BallGoldAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BallGoldJavabean;
import com.joytouch.superlive.javabean.Money_detailJavaBean;
import com.joytouch.superlive.javabean.detain_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 金币明细界面
 * Created by Administrator on 5/5 0005.
 */

public class Jinbi_detailActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener{
    private List<detain_item> groups = new ArrayList<detain_item>();;//存储数据集合
    private List<BallGoldJavabean> balllist;
    private int time = 0;//刷新或开始传"",加载传最后一条time
    private Money_detailJavaBean Info;
    private String last_id = "0";//记录最后一个条目的position,定位
    private ImageView iv_finish;
    private RadioButton golddetail;
    private RadioButton ballDetail;
    private PullToRefreshLayout refresh;
    private PullableListView lv_content;
    private SharedPreferences sp;
    private DetailAdapter adapter;
    private boolean isUp=false;//是否是上啦加载
    private CommonShowView loadstate;
    private boolean isBallGlod = false;
    private BallGoldAdapter balladapter;
    private String lasttime = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.jinbimiangxi);
        balllist = new ArrayList<>();
        //初始化界面
        initUI();
        //获取网络数据
        refresh(true);
    }

    private void initUI() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        golddetail = (RadioButton) findViewById(R.id.weekdetail);
        ballDetail = (RadioButton) findViewById(R.id.balldetail);
        refresh = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        lv_content= (PullableListView) this.findViewById(R.id.lv_content);
        adapter = new DetailAdapter(groups, Jinbi_detailActivity.this);
        loadstate=(CommonShowView)this.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        golddetail.setOnClickListener(this);
        ballDetail.setOnClickListener(this);
    }

    private void refresh(boolean isFirst) {
        //true代表刷新状态,false代表上拉加载
        if (isFirst) {
            time = 0;
            last_id = "0";
            isUp=false;
            //注意: 必须notifyDataSetChanged,不然报错
            groups.clear();
            balllist.clear();
            adapter.notifyDataSetChanged();
            if(!isBallGlod) {
                getadata();
            }else {
                lasttime = "-1";
                getBallGolddata();
            }
        } else {
            if(!isBallGlod) {
                getadata();
            }else {
                lasttime = balllist.get(balllist.size()-1).getTime();
                getBallGolddata();
            }
        }
    }

    private void getadata() {
        if(isUp){
            time+=1;
        }else{
            time=0;
        }
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("page", time+"")
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.golddetail, build,
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
                                refresh(true);
                                isUp=false;
                            }
                        });
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("金币明细", json);
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        Gson gson = new Gson();
                        Money_detailJavaBean Bean = gson.fromJson(json, Money_detailJavaBean.class);
                        if (Bean.status.equals("_0000")) {
                            //拿到最后一个条目的位置,和最后一条的time
                            if (groups.size() > 0) {
                                last_id = String.valueOf(groups.size() - 1);
                            }
                            //获取所有的集合
                            groups.addAll(Bean.GoldDetail);
                            if (groups.size()>0){
                                //有数据的情况下,隐藏界面
                                loadstate.setVisibility(View.GONE);
                            }else{
                                //如果没有数据,显示暂无数据
                                loadstate.setnull();
                                loadstate.setVisibility(View.VISIBLE);
                            }
                            lv_content.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            //上拉加载时,选择最后一个条目固定在底端
                            lv_content.setSelection(Integer.parseInt(last_id));
                        } else if (Bean.status.equals("_1000")){
                            new LoginUtils(Jinbi_detailActivity.this).reLogin(Jinbi_detailActivity.this);
                            Toast.makeText(Jinbi_detailActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Jinbi_detailActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getBallGolddata() {
        if(isUp){
            time+=1;
        }else{
            time=0;
        }
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""));
        if(!isBallGlod) {
            build.add("page", time + "");
        }else {
            build.add("last_time", lasttime + "");
        }
                build.add("phone", Preference.phone);
                build.add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.balldetail, build,
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
                                refresh(true);
                                isUp=false;
                            }
                        });
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("球票明细", json);
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        try {
                            JSONObject object = new JSONObject(json);
                            if("_0000".equals(object.optString("status"))){
                                JSONArray array = object.optJSONArray("list");
                                int pos = balllist.size();
                                if(array!=null){
                                    for(int i = 0;i<array.length();i++){
                                        JSONObject object1 = array.optJSONObject(i);
                                        BallGoldJavabean javabean = new BallGoldJavabean();
                                        javabean.setIsAdd(object1.optString("type_id"));
                                        javabean.setMoney(object1.optString("money"));
                                        javabean.setName(object1.optString("title"));
                                        javabean.setTime(object1.optString("time"));
                                        javabean.setDate(object1.optString("date"));
                                        JSONObject object2 = object1.optJSONObject("left");
                                        JSONObject object3 = object1.optJSONObject("right");
                                        if(object2!=null){
                                            javabean.setRedname(object2.optString("team_name"));
                                            javabean.setRedmoney(object2.optString("ticket_num"));
                                        }
                                        if(object3!=null){
                                            javabean.setBluename(object3.optString("team_name"));
                                            javabean.setBluemoney(object3.optString("ticket_num"));
                                        }
                                            javabean.setPayway(object1.optString("type_str"));

                                        balllist.add(javabean);
                                    }
                                    if (balllist.size()>0){
                                        //有数据的情况下,隐藏界面
                                        loadstate.setVisibility(View.GONE);
                                    }else{
                                        loadstate.setVisibility(View.VISIBLE);
                                        //如果没有数据,显示暂无数据
                                        loadstate.setnull();
                                    }
                                    balladapter = new BallGoldAdapter(balllist,Jinbi_detailActivity.this);
                                    lv_content.setAdapter(balladapter);
                                    balladapter.notifyDataSetChanged();
                                    if(isUp) {
                                        lv_content.setSelection(pos - 1);
                                    }

                                }
                            }else if (object.optString("status").equals("_1000")){
                                new LoginUtils(Jinbi_detailActivity.this).reLogin(Jinbi_detailActivity.this);
                                Toast.makeText(Jinbi_detailActivity.this,object.optString("message"), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(Jinbi_detailActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_finish:
                finish();
                break;
            case R.id.weekdetail:
                isBallGlod =false;
                groups.clear();
                getadata();
                    break;
            case R.id.balldetail:
                isBallGlod = true;
                balllist.clear();
                getBallGolddata();
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
        isUp=true;
        refresh.loadmoreFinish(0);
    }
    public class DetailAdapter extends AppBaseAdapter<detain_item> {
        private Context context;

        public DetailAdapter(List<detain_item> list, Context context) {
            super(list, context);
            this.context = context;
        }

        @Override
        public View createView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.jinbi_item, null);
                holder = new ViewHolder();
                holder.moneytv = (TextView) convertView.findViewById(R.id.money);
                holder.logIV = (ImageView) convertView.findViewById(R.id.v4_logeadd);
                holder.sorcetv = (TextView) convertView.findViewById(R.id.v4_source);
                holder.timetv = (TextView) convertView.findViewById(R.id.time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            detain_item item = list.get(position);
            if (item.type==0) {
                holder.logIV.setBackgroundResource(R.drawable.canyu);
                holder.moneytv.setTextColor(context.getResources().getColor(R.color.v4_main));
            } else  if (item.type==1){
                holder.logIV.setBackgroundResource(R.drawable.zhongjiang);
                holder.moneytv.setTextColor(context.getResources().getColor(R.color.v4_alrm_yellow));
            }else{
                holder.logIV.setBackgroundResource(R.drawable.xiaodao);
                holder.moneytv.setTextColor(context.getResources().getColor(R.color.v4_alrm_yellow));
            }
            holder.sorcetv.setText(item.type_id);
            holder.timetv.setText(item.time.substring(0, 11));
            holder.moneytv.setText(item.money);
            return convertView;
        }


        public class ViewHolder {
            TextView moneytv;
            ImageView logIV;
            TextView sorcetv;
            TextView timetv;
        }

    }
}


