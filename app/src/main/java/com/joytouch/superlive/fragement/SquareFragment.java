package com.joytouch.superlive.fragement;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.SquareAttentionAdapter;
import com.joytouch.superlive.adapter.SquareCommendAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.SquareAttention;
import com.joytouch.superlive.javabean.SquareAttentionBase;
import com.joytouch.superlive.javabean.SquareRecommend;
import com.joytouch.superlive.javabean.SquareRecommendBase;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.MyListView;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/7.
 */
public class SquareFragment extends Fragment implements PullToRefreshLayout.OnRefreshListener{
    private View view;
    private MyListView attention;//用户关注人的动态列表
    private MyListView recommend;//后台推荐的人的列表
    private LinearLayout ll_line;//中间分隔
    private TextView tv_recommend;
    private List<SquareAttention> attentions = new ArrayList<>();
    private List<SquareRecommend> recommends = new ArrayList<>();
    private SquareCommendAdapter squareCommendAdapter;
    private SquareAttentionAdapter squareAttentionAdapter;
    private PullToRefreshLayout refreshLayout;//刷新和加载更多控件
    private SquareAttentionBase squareAttentionBase;//关注基类
    private View header;//无关注用户时显示的Header；
    private LinearLayout loading;
    private boolean isFirstAttention = true;
    private boolean isFirstCommend = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_squar,null);
        initView();
        return view;
    }

    private void initView() {
        loading = (LinearLayout) view.findViewById(R.id.loading);
        header = LayoutInflater.from(getActivity()).inflate(R.layout.header_noattention,null);
        attention = (MyListView) view.findViewById(R.id.listView_attention);
        recommend = (MyListView) view.findViewById(R.id.listView_recommend);
        ll_line = (LinearLayout) view.findViewById(R.id.ll_line);
        tv_recommend = (TextView) view.findViewById(R.id.tv_recommend);
        squareAttentionAdapter = new SquareAttentionAdapter(attentions, getActivity());
        attention.setAdapter(squareAttentionAdapter);
        squareCommendAdapter = new SquareCommendAdapter(recommends, getActivity());
        recommend.setAdapter(squareCommendAdapter);
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setCanPullUp(false);
        getAttentionDate();
        getRecommentDate();
    }
    private void getRecommentDate() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("token", (String) SPUtils.get(getActivity(),"token","",Preference.preference))
                .build();
        Log.e("token:::",(String) SPUtils.get(getActivity(),"token","",Preference.preference));
        HttpRequestUtils.ResRultListener listener =  new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                Toast.makeText(getActivity(),"访问失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {
                Log.e("推荐列表", json);
                Gson gson = new Gson();
                Type type = new TypeToken<SquareRecommendBase>() {}.getType();
                SquareRecommendBase squareRecommendBase = gson.fromJson(json, type);
                if (squareRecommendBase.getStatus().equals("_0000")){
                    recommends.clear();
                    isFirstCommend = false;
                    if (null!=squareRecommendBase.getRecommend().getOnlineUser()&&squareRecommendBase.getRecommend().getOnlineUser().size() > 0) {
                        for (int i =0;i<squareRecommendBase.getRecommend().getOnlineUser().size();i++){
                            SquareRecommend recommend = squareRecommendBase.getRecommend().getOnlineUser().get(i);
                            recommend.setSign("在线玩家");
                            if (null!= recommend.getBehavitor_key()&&!"".equals(recommend.getBehavitor_key())){
                                recommends.add(recommend);
                            }
                        }
                    }
                    if (null!=squareRecommendBase.getRecommend().getHotAnchor()&&squareRecommendBase.getRecommend().getHotAnchor().size() > 0) {
                        for (int i =0;i<squareRecommendBase.getRecommend().getHotAnchor().size();i++){
                            SquareRecommend recommend = squareRecommendBase.getRecommend().getHotAnchor().get(i);
                            recommend.setSign("人气主播");
                             if (null!= recommend.getBehavitor_key()&&!"".equals(recommend.getBehavitor_key())){
                                recommends.add(recommend);
                            }
                        }
                    }
                    if (null!=squareRecommendBase.getRecommend().getRichPeople()&&squareRecommendBase.getRecommend().getRichPeople().size() > 0) {
                        for (int i =0;i<squareRecommendBase.getRecommend().getRichPeople().size();i++){
                            SquareRecommend recommend = squareRecommendBase.getRecommend().getRichPeople().get(i);
                            recommend.setSign("富豪榜前十位");
                            if (null!= recommend.getBehavitor_key()&&!"".equals(recommend.getBehavitor_key())){
                                recommends.add(recommend);
                            }
                        }
                    }
                    if (null!=squareRecommendBase.getRecommend().getSeniorPlayer()&&squareRecommendBase.getRecommend().getSeniorPlayer().size() > 0) {
                        for (int i =0;i<squareRecommendBase.getRecommend().getSeniorPlayer().size();i++){
                            SquareRecommend recommend = squareRecommendBase.getRecommend().getSeniorPlayer().get(i);
                            recommend.setSign("高级玩家");
                            if (null!= recommend.getBehavitor_key()&&!"".equals(recommend.getBehavitor_key())){
                                recommends.add(recommend);
                            }
                        }
                    }
                    if (null!=squareRecommendBase.getRecommend().getWeekRank()&&squareRecommendBase.getRecommend().getWeekRank().size()>0){
                        for (int i =0;i<squareRecommendBase.getRecommend().getWeekRank().size();i++){
                            SquareRecommend recommend = squareRecommendBase.getRecommend().getWeekRank().get(i);
                            recommend.setSign("周榜前十位");
                            if (null!= recommend.getBehavitor_key()&&!"".equals(recommend.getBehavitor_key())){
                                recommends.add(recommend);
                            }
                        }
                    }

                    squareCommendAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(),"访问失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (isFirstCommend){
            new HttpRequestUtils(getActivity(),loading,false).httpPost(Preference.recommend, build, listener);
        }else {
            new HttpRequestUtils(getActivity()).httpPost(Preference.recommend, build, listener);
        }

    }
    private void getAttentionDate(){
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("token", (String) SPUtils.get(getActivity(),Preference.token,"",Preference.preference))
                .build();
        HttpRequestUtils.ResRultListener attentionListener = new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void onSuccess(String json) {
                Log.e("我的关注",json);
//                String area_str = File_Util.readAssets(getContext(), "attention.json");
                squareAttentionBase= null;
                Gson gson = new Gson();
                Type type = new TypeToken<SquareAttentionBase>(){}.getType();
                squareAttentionBase = gson.fromJson(json,type);
                if (squareAttentionBase.getStatus().equals("_0000")){
                    isFirstAttention = false;
                    if (null!=refreshLayout){
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }else {
                        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh);
                        refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                    attentions.clear();
                    //squareAttentionBase.getList().get(k).getIs_line().equals("1")&&
                    for (int k=0;k<squareAttentionBase.getList().size(); k++) {
                        if (!"".equals(squareAttentionBase.getList().get(k).getBehavitor_key())){
                            attentions.add(squareAttentionBase.getList().get(k));
                        }
                    }
                    if (attentions.size()>0){
                        attention.setVisibility(View.VISIBLE);
//                        attentions.addAll(squareAttentionBase.getList());
                        squareAttentionAdapter.notifyDataSetChanged();
                        if (attentions.size()>=10){
//                            refreshLayout.setCanPullUp(true);
                            ll_line.setVisibility(View.GONE);
                            recommend.setVisibility(View.GONE);
                        }else {
//                            refreshLayout.setCanPullUp(false);
                            ll_line.setVisibility(View.VISIBLE);
                            recommend.removeHeaderView(header);
                            recommend.setVisibility(View.VISIBLE);
                        }
                    }else {
                        recommend.removeHeaderView(header);
                        recommend.addHeaderView(header);
                        ll_line.setVisibility(View.GONE);
                        attention.setVisibility(View.GONE);
                        recommend.setVisibility(View.VISIBLE);
                    }
                    getRecommentDate();
                }else {
                    Toast.makeText(getActivity(),squareAttentionBase.getMessage(), Toast.LENGTH_SHORT).show();
                    refreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                }
            }
        };
        if (isFirstAttention){
            new HttpRequestUtils(getActivity(),loading,false).httpPost(Preference.online, build, attentionListener);// Conlist
        }else {
            new HttpRequestUtils(getActivity()).httpPost(Preference.online, build, attentionListener);
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getAttentionDate();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("token", (String) SPUtils.get(getActivity(),Preference.token,"",Preference.preference))
                .build();
        new HttpRequestUtils(getActivity()).httpPost(Preference.online, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                refreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void onSuccess(String json) {
                Gson gson = new Gson();
                Type type = new TypeToken<SquareAttentionBase>(){}.getType();
                squareAttentionBase = gson.fromJson(json,type);
                if (squareAttentionBase.getStatus().equals("_0000")){
                    List<SquareAttention> aa=new ArrayList<SquareAttention>();
                    refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    if (squareAttentionBase.getList().size()>0){
                        for (int k=0;k<squareAttentionBase.getList().size(); k++) {
                            if (squareAttentionBase.getList().get(k).getIs_line().equals("1")){
                                aa.add(squareAttentionBase.getList().get(k));
                            }
                        }
                        attentions.addAll(aa);
                        squareAttentionAdapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(getActivity(),"数据访问失败",Toast.LENGTH_SHORT).show();
                    refreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                }
            }
        });
    }
}
