package com.joytouch.superlive.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.BallBankAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.GuessRank;
import com.joytouch.superlive.javabean.rank_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/7/18.
 */
public class BallBankFragment extends BaseFragment {
    private PullableListView plt;
    private PullToRefreshLayout prfl;
    private LinearLayout loadingview;
    private TextView myRank;
    private TextView myName;
    private TextView myGold;
    private BallBankAdapter adapter;
    private int time = 1;
    private List<rank_item>  balllist;
    private boolean isUp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ballrank  ,null);
        balllist = new ArrayList<>();
        init(view);
        return view;
    }

    private void init(View view) {
        plt = (PullableListView) view.findViewById(R.id.listView);
        prfl = (PullToRefreshLayout) view.findViewById(R.id.refresh);
        loadingview = (LinearLayout) view.findViewById(R.id.loading);
        myRank = (TextView) view.findViewById(R.id.tv_total_num);
        myName = (TextView) view.findViewById(R.id.tv_name);
        myGold = (TextView) view.findViewById(R.id.tv_total_gold);
        prfl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                time = 1;
                isUp = false;
//                balllist.clear();
                getdata();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isUp = true;
                time = time + 1;
                getdata();
            }
        });
        getdata();
    }

    public void getdata() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(getActivity(), Preference.token, "", Preference.preference))
                .add("page", time + "")
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(getActivity(),loadingview,isUp).httpPost(Preference.ballrank, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        prfl.refreshFinish(PullToRefreshLayout.FAIL);
                        if(time > 1){
                            time = time-1;
                        }

                    }

                    @Override
                    public void onSuccess(String json) {
                        if (!isUp){
                            balllist.clear();
                        }
                        int pos = balllist.size();
                        Log.e("球票列表", json);
                        prfl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            Gson gson = new Gson();
                            GuessRank Bean = gson.fromJson(json, GuessRank.class);

                        if (Bean.status.equals("_0000")) {
                            if ((!Bean.myrank.rank.equals("")) && Integer.parseInt(Bean.myrank.rank)<1000){
                                myRank.setText(Bean.myrank.rank);
                            }else{
                                myRank.setText("999+");
                            }
                            myName.setText(Bean.myrank.nick_name);
                            myGold.setText(Bean.myrank.balance);
                            int size = balllist.size();
                          if(isUp){
                            balllist.addAll(Bean.rank_list);
                          }else {
                              balllist.addAll(size,Bean.rank_list);
                          }
                            adapter = new BallBankAdapter(balllist,getActivity());
                            plt.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            if(isUp){
                                plt.setSelection(pos);
                            }
                        } else if (Bean.status.equals("_1000")) {
                            if(time > 1){
                                time = time-1;
                            }
                            new LoginUtils(getActivity()).reLogin(getActivity());
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        } else {
                            if(time > 1){
                                time = time-1;
                            }
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void parseJson(String json) {

    }
}
