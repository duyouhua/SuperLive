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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.weekRankAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.GuessRank;
import com.joytouch.superlive.javabean.rank_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 周末排行榜
 * Created by Administrator on 2016/4/27.
 */
public class weekRankFragement extends Fragment implements PullToRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener {
    private View view;
    private PullToRefreshLayout refreshLayout;
    private ListView listView;
    private int time = 1;//刷新或开始传"1",加载传最后一条time
    private String last_id = "0";//记录最后一个条目的position,定位
    private boolean isUp=false;//是否是上啦加载
    private List<rank_item> groups = new ArrayList<rank_item>();;
    private SharedPreferences sp;
    private CommonShowView loadstate;
    private weekRankAdapter adapter;
    private TextView tv_total_num;
    private TextView tv_name;
    private TextView tv_total_gold;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weekrank, container, false);
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
                .add("page", time+"")
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(getActivity()).httpPost(Preference.week, build,
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
                        Log.e("周榜列表", json);
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        Gson gson = new Gson();
                        GuessRank Bean = gson.fromJson(json, GuessRank.class);
                        if (Bean.status.equals("_0000")) {
                            time+=1;
                            //拿到最后一个条目的位置,和最后一条的time
                            if (groups.size() > 0) {
                                last_id = String.valueOf(groups.size() - 1);
                            }
                            //获取所有的集合
                            groups.addAll(Bean.rank_list);
                            if (groups.size() > 0) {
                                //有数据的情况下,隐藏界面
                                loadstate.setVisibility(View.GONE);
                            } else {
                                //如果没有数据,显示暂无数据
                                loadstate.setnull();
                            }
                            if ((!Bean.myrank.rank.equals("")) && Integer.parseInt(Bean.myrank.rank)<1000){
                                tv_total_num.setText(Bean.myrank.rank);
                            }else{
                                tv_total_num.setText("999+");
                            }
                            tv_name.setText(Bean.myrank.nick_name);
                            tv_total_gold.setText(Bean.myrank.balance);

                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
//                            //上拉加载时,选择最后一个条目固定在底端
                            listView.setSelection(Integer.parseInt(last_id));
                        } else if (Bean.status.equals("_1000")) {
                            new LoginUtils(getActivity()).reLogin(getActivity());
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initView() {
        tv_total_gold=(TextView)view.findViewById(R.id.tv_total_gold);
        tv_total_num=(TextView)view.findViewById(R.id.tv_total_num);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        loadstate=(CommonShowView)view.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter=new weekRankAdapter(groups,getActivity());
        listView.setAdapter(adapter);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        refresh(true);
        isUp=false;
        refreshLayout.refreshFinish(0);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        refresh(false);
        isUp = true;
        refreshLayout.loadmoreFinish(0);
    }
}
