package com.joytouch.superlive.fragement;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.AttentionAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.atentionInfo;
import com.joytouch.superlive.javabean.atten_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * A simple {@link Fragment} subclass.
 * 关注
 */
public class AttentionFragment extends Fragment implements PullToRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener {
    private View view;
    private PullToRefreshLayout refresh;
    private ListView listView;
    private SharedPreferences sp;
    private int time = 1;//刷新或开始传"1",加载传最后一条time
    private String last_id = "0";//记录最后一个条目的position,定位
    private boolean isUp=false;//是否是上啦加载
    private List<atten_item> groups = new ArrayList<atten_item>();;
    private AttentionAdapter adapter;
    private CommonShowView loadstate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attention_and_fans, container, false);
        sp = getActivity().getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
        //获取网络数据
        refresh(true);

        return view;
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
        new HttpRequestUtils(getActivity()).httpPost(Preference.Conlist, build,
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
                        refresh.refreshFinish(PullToRefreshLayout.FAIL);
                        loadstate.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("关注列表", json);
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        Gson gson = new Gson();
                        atentionInfo Bean = gson.fromJson(json, atentionInfo.class);
                        if (Bean.status.equals("_0000")) {
                            time+=1;
                            //拿到最后一个条目的位置,和最后一条的time
                            if (groups.size() > 0) {
                                last_id = String.valueOf(groups.size() - 1);
                            }
                            //获取所有的集合
                            groups.addAll(Bean.list);
                            if (groups.size()>0){
                                //有数据的情况下,隐藏界面
                                loadstate.setVisibility(View.GONE);
                            }else{
                                //如果没有数据,显示暂无数据
                                loadstate.setnull();
                            }
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            //上拉加载时,选择最后一个条目固定在底端
                            listView.setSelection(Integer.parseInt(last_id));
                        } else if (Bean.status.equals("_1000")){
                            new LoginUtils(getActivity()).reLogin(getActivity());
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {
        loadstate=(CommonShowView)view.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        refresh = (PullToRefreshLayout) view.findViewById(R.id.refresh);
        listView = (ListView) view.findViewById(R.id.lv_content);
        refresh.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        adapter = new AttentionAdapter(groups, getActivity());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
