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
import com.joytouch.superlive.adapter.MyPropAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.ShopItemInfo;
import com.joytouch.superlive.javabean.ShopJavabean;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 我的道具
 */
public class MyPropActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private PullToRefreshLayout refreshLayout;
    private ListView listView;
    private List<ShopItemInfo> props;
    private SharedPreferences sp;
    private CommonShowView loadstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_my_prop);
        getdata();
        initView();
    }

    private void getdata() {
        FormBody.Builder build_2=new FormBody.Builder();
        build_2.add("token", sp.getString(Preference.token, ""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();

        new HttpRequestUtils(this).httpPost(Preference.asset, build_2,
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
                            }
                        });
                        refreshLayout .refreshFinish(PullToRefreshLayout.FAIL);
                        loadstate.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("我的道具", json.toString());
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        Gson gson = new Gson();
                        ShopJavabean info = gson.fromJson(json, ShopJavabean.class);
                        if (info.status.equals("_0000")) {
                            props=new ArrayList<ShopItemInfo>();
                            props.clear();
                            props.addAll(info.Goods);
                            if (props.size()>0){
                                //有数据的情况下,隐藏界面
                                loadstate.setVisibility(View.GONE);
                            }else{
                                //如果没有数据,显示暂无数据
                                loadstate.setnull();
                            }
                            listView.setAdapter(new MyPropAdapter(props,MyPropActivity.this));
                        } else if (info.status.equals("_1000")){
                            new LoginUtils(MyPropActivity.this).reLogin(MyPropActivity.this);
                            Toast.makeText(MyPropActivity.this, info.message, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MyPropActivity.this, info.message, Toast.LENGTH_SHORT).show();
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
        tv_title.setText("我的道具");
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refreshLayout.setCanPullDown(false);
        refreshLayout.setCanPullUp(false);
        refreshLayout.setOnRefreshListener(this);
        listView = (ListView) this.findViewById(R.id.listView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

}
