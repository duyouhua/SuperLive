package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.BlackAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.updateCallback;
import com.joytouch.superlive.javabean.Fans;
import com.joytouch.superlive.javabean.funs_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 黑名单列表 去掉拉黑等操作
 */
public class BlackActivity extends BaseActivity implements View.OnClickListener ,PullToRefreshLayout.OnRefreshListener,updateCallback {
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;//点击编辑选项列表中出现移除黑名单的按钮
    private PullToRefreshLayout refreshLayout;
    private ListView listView;
    private List<funs_item> groups= new ArrayList<funs_item>();
    private boolean isOver = false;
    private BlackAdapter adapter;
    private SharedPreferences sp;
    private int time = 1;//刷新或开始传"",加载传最后一条time
    private String last_id = "0";//记录最后一个条目的position,定位
    private boolean isUp=false;//是否是上啦加载
    private CommonShowView loadstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black);
        sp =this.getSharedPreferences(Preference.preference,
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
        new HttpRequestUtils(this).httpPost(Preference.Blacklist, build,
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
                                isUp = false;
                            }
                        });
                        refreshLayout .refreshFinish(PullToRefreshLayout.FAIL);
                        loadstate.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("黑名单列表", json);
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        Gson gson = new Gson();
                        Fans Bean = gson.fromJson(json, Fans.class);
                        if (Bean.status.equals("_0000")) {
                            time+=1;
                            //拿到最后一个条目的位置,和最后一条的time
                            if (groups.size() > 0) {
                                last_id = String.valueOf(groups.size() - 1);
                            }
                           //获取所有的集合
                            groups.addAll(Bean.list);
                            if (groups.size()>0){
                                tv_right.setEnabled(true);
                                //有数据的情况下,隐藏界面
                                loadstate.setVisibility(View.GONE);
                            }else{
                                tv_right.setEnabled(false);
                                //如果没有数据,显示暂无数据
                                loadstate.setnull();
                            }
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            //上拉加载时,选择最后一个条目固定在底端
                            listView.setSelection(Integer.parseInt(last_id));
                        } else if (Bean.status.equals("_1000")){
                            new LoginUtils(BlackActivity.this).reLogin(BlackActivity.this);
                            Toast.makeText(BlackActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(BlackActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {
        loadstate=(CommonShowView)this.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("黑名单");
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("编辑");
        tv_right.setOnClickListener(this);
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        listView = (ListView) this.findViewById(R.id.listView);
        adapter=new BlackAdapter(groups, this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_right:
                if (isOver){//完成操作
                    showRemove(false);
                    tv_right.setText("编辑");
                    isOver = false;
                }else {//编辑操作
                    showRemove(true);
                    tv_right.setText("完成");
                    isOver = true;
                }

                break;
        }
    }

    private void showRemove(boolean show) {
        if (show){
            for (int i=0;i<groups.size();i++){
                listView.getChildAt(i).findViewById(R.id.tv_remove).setVisibility(View.VISIBLE);
            }
        } else{
            for (int i=0;i<groups.size();i++){
                listView.getChildAt(i).findViewById(R.id.tv_remove).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refresh(true);
        isUp=false;
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        refresh(false);
        isUp=true;
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void Callback(int position) {
        groups.remove(position);
        adapter.notifyDataSetChanged();
    }
}
