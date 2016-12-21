package com.joytouch.superlive.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.LotteryAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.LotteryInfo;
import com.joytouch.superlive.javabean.LotteryList;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.TimeUtil;
import com.joytouch.superlive.widget.LeaveOutRoomDialog;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableExpandableListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/11.
 * 竞猜列表
 */
public class LotteryFragment extends BaseFragment{
    private View view;
    private PullableExpandableListView pelv;
    private PullToRefreshLayout refresh;
    private List<LotteryList> data;
    private LinearLayout loading;
    private LotteryAdapter adapter;
    private WebView web;
    private boolean isdown;
    private boolean isOpend = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lottertlist,null);
        initView();

        return view;
    }

    private void initView() {
        web = (WebView) view.findViewById(R.id.web);
        //设置WebView属性，能够执行Javascript脚本
        web.getSettings().setJavaScriptEnabled(true);
        loading = (LinearLayout) view.findViewById(R.id.loading);
        pelv = (PullableExpandableListView) view.findViewById(R.id.pelv);

        refresh = (PullToRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setCanPullUp(false);
        View empty = LayoutInflater.from(getActivity()).inflate(R.layout.empty,null);
        empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        pelv.setEmptyView(empty);

        data = new ArrayList<>();
        //getLotteryList();
        loading.setVisibility(View.GONE);
        refresh.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getLotteryList("");
                isdown = true;
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });

        pelv.setGroupIndicator(null);
    }


    public void getLotteryList(final String type){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", Preference.room_id);
        builder.add("token", (String) SPUtils.get(getActivity(), Preference.token, "", Preference.preference));
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils(getActivity());
        httpRequestUtils.httpPost(Preference.LotteryLabel, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                if (isdown) {
                    isdown = false;
                    refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            @Override
            public void onSuccess(String json) {
                Log.e("竞猜列表", json);
                try {
                    data.clear();
                    if (isdown) {
                        isdown = false;
                        refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                    }
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.optString("status"))) {
                        LotteryList noopen = new LotteryList();
                        noopen.setTitle("进行中");
                        LotteryList open = new LotteryList();
                        open.setTitle("已截止");
                        JSONObject array = object.optJSONObject("list");
                        JSONArray conduct = array.optJSONArray("conduct");
                        if (conduct != null) {

                            List<LotteryInfo> lotteryno = new ArrayList<LotteryInfo>();
                            for (int i = 0; i < conduct.length(); i++) {
                                JSONObject object1 = conduct.getJSONObject(i);
                                lotteryno.add(jsoParse(object1));
                            }
                            noopen.setInfo(lotteryno);
                        }
                        JSONArray colse = array.optJSONArray("close");
                        if (colse != null) {

                            List<LotteryInfo> lotteryyes = new ArrayList<LotteryInfo>();
                            for (int i = 0; i < colse.length(); i++) {
                                JSONObject object1 = colse.getJSONObject(i);
                                lotteryyes.add(jsoParse(object1));
                            }
                            open.setInfo(lotteryyes);
                        }
                        data.add(noopen);
                        data.add(open);
                        if ("hasOpend".equals(type)){
                            hasOpend();
                        }
                        if ((open.getInfo() == null && noopen.getInfo() == null) || (open.getInfo().size() == 0 && noopen.getInfo().size() == 0)) {
                            LogUtils.e("ssssssswebv", "" + object.getString("web_url"));
                            web.setVisibility(View.VISIBLE);
                            refresh.setVisibility(View.GONE);
                            web.loadUrl(object.getString("web_url"));
                        } else {
                            web.setVisibility(View.GONE);
                            refresh.setVisibility(View.VISIBLE);
                            LogUtils.e("*******", "" + data.size());
                            adapter = new LotteryAdapter(data, getActivity());
                            pelv.setAdapter(adapter);
                            for (int i = 0; i < 2; i++) {
                                pelv.expandGroup(i);
                            }
                        }

                    } else {
                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public  LotteryInfo jsoParse(JSONObject object) {
        LotteryInfo info = new LotteryInfo();
        info.setTitle(object.optString("content"));
        info.setEndtime(TimeUtil.currentSplitTimeStringHm(object.optString("stop_time")));
        info.setStopTime(object.optLong("stop_time_s"));
        info.setMatchid(object.optString("guess_id"));
        info.setBettingGolde(object.optString("room_bet"));
        info.setLeftgold(object.optString("total_left"));
        info.setRightgold(object.optString("total_right"));
        info.setLeftselector(object.optString("option_left"));
        info.setRightselector(object.optString("option_right"));
        info.setWingold(object.optString("my_win_money"));
        info.setAdd_user_id(object.optString("Add_user_id"));

        if(!TextUtils.isEmpty(object.optString("my_option"))) {
            info.setIsSelf(true);
            if(object.optString("option_left").equals(object.optString("my_option"))) {
                info.setSelfeSelector(0);
            }else {
                info.setSelfeSelector(1);
            }
        }else {
            info.setIsSelf(false);
        }
        info.setIsOdds(false);
        switch (object.optInt("status")){
            case 2:
                //进行中
                info.setIsOpenLottery(false);
                info.setIsEnd(false);
                break;
            case 3:
                //已截止
                info.setIsOpenLottery(false);
                info.setIsEnd(true);
                break;
            case 4:
                //已开奖
                info.setIsOpenLottery(true);
                info.setIsEnd(true);
                info.setIsWin(false);
                break;
            case 5:
                info.setIsOpenLottery(false);
                break;
            case 6:
                //赢
                info.setIsOpenLottery(true);
                info.setIsEnd(true);
                info.setIsWin(true);
                break;
        }
        return info;
    }
    public void hasOpend(){
        if (null == data){
            getActivity().finish();
            return ;
        }
        if (data.size()>0&&null!=data.get(0).getInfo() &&data.get(0).getInfo().size()>0){
            for (int j = 0;j<data.get(0).getInfo().size();j++){
                if (data.get(0).getInfo().get(j).getAdd_user_id().equals(SPUtils.get(getActivity(),Preference.myuser_id,"",Preference.preference))){
                    LeaveOutRoomDialog outRoomDialog = new LeaveOutRoomDialog(getActivity());
                    outRoomDialog.show();
                    return ;
                }
            }
        }
        if (data.size()>1&&null!=data.get(1).getInfo() &&data.get(1).getInfo().size()>0){
            for (int i = 0;i<data.get(1).getInfo().size();i++){
                if (!data.get(1).getInfo().get(i).isOpenLottery()&&data.get(1).getInfo().get(i).getAdd_user_id().equals(SPUtils.get(getActivity(),Preference.myuser_id,"",Preference.preference))){
                    LeaveOutRoomDialog outRoomDialog = new LeaveOutRoomDialog(getActivity());
                    outRoomDialog.show();
                    return ;
                }
            }
            getActivity().finish();
            return ;
        }else {
            getActivity().finish();
            return ;
        }
    }
}
