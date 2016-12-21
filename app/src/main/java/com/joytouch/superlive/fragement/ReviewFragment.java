package com.joytouch.superlive.fragement;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ReviewPlayActivity;
import com.joytouch.superlive.adapter.MatchLeftAdapter;
import com.joytouch.superlive.adapter.MatchRightAdapter;
import com.joytouch.superlive.adapter.ReviewAdapter;
import com.joytouch.superlive.adapter.ReviewPopTimeAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.ReviewBase;
import com.joytouch.superlive.javabean.ReviewList;
import com.joytouch.superlive.javabean.ReviewOptionBase;
import com.joytouch.superlive.javabean.ReviewOptionMatch;
import com.joytouch.superlive.javabean.ReviewOptionMatchBase;
import com.joytouch.superlive.javabean.ReviewOptionOrder;
import com.joytouch.superlive.javabean.ReviewOptionTime;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/7.
 */
public class ReviewFragment extends Fragment implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{
    private View view;
    private ImageView iv_finish;//返回
    private TextView tv_title;//标题
    private PullToRefreshLayout refresh;
    private RelativeLayout rl_time,rl_liansai,rl_order;//选项布局
    private ImageView iv_time,iv_liansai,iv_order;//选项旁边的箭头
    private ImageView iv_time_up,iv_match_up,iv_order_up;//选项下边的箭头
    private LinearLayout ll_up;//选项下边的箭头布局
    private ListView listView;//回顾list
    private List<ReviewList> reviewLists = new ArrayList<>();//回顾列表数
    // 据
    private ReviewAdapter reviewAdapter;//回顾列表adapter
    private PopupWindow timeWindow;//时间
    private PopupWindow matchWindow;//联赛
    private PopupWindow orderWindow;//排序
    private List<ReviewOptionTime> times = new ArrayList<>();//时间搜索条件数据
    private List<ReviewOptionMatchBase> matches = new ArrayList<>();//赛事搜索条件数据
    private List<ReviewOptionMatch> optionMatches = new ArrayList<>();//赛事搜索条件精确数据
    private List<ReviewOptionOrder> orders = new ArrayList<>();//排序数据
    private LinearLayout rl_tab;
    private  ReviewOptionBase optionBase;//赛事
    private ListView listView_time;//时间搜索条件listview
    private ListView listView_match_base;//赛事搜索条件listview
    private ListView listView_match;//赛事搜索条件精确赛事
    private ListView listView_order;//排序listview
    private Display display;
    private ReviewBase reviewBase;//回顾
    int mark;//记录时间条件点击的位置
    int matchl;//记录赛事点击位置
    int matchr;//记录精确赛事点击位置
    int order;//记录排序点击位置
    private String date = "";//记录用户选择的时间条件
    private String cat1 = "";//记录用户选择的赛事类型
    private String cat2 = "";//记录用户选择的赛事
    private String sort = "";//记录用户的排序方式
    private int page = 2;//记录分页
    private ReviewPopTimeAdapter timeAdapter;
    private MatchRightAdapter rightAdapter;//赛事Adapter
    private MatchLeftAdapter leftAdapter;
    private LinearLayout loading;
    private boolean isFirstReview = true;
    private LinearLayout ll_empty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_review, null);
        WindowManager windowManager = getActivity().getWindowManager();
        display =  windowManager.getDefaultDisplay();
        initView();
        return view;
    }

    private void initView() {
        loading = (LinearLayout) view.findViewById(R.id.loading);
        ll_empty = (LinearLayout) view.findViewById(R.id.empty);
        iv_finish = (ImageView) view.findViewById(R.id.iv_finish);
        iv_finish.setVisibility(View.GONE);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText("回顾");
        rl_time = (RelativeLayout) view.findViewById(R.id.rl_time);
        rl_liansai = (RelativeLayout) view.findViewById(R.id.rl_liansai);
        rl_order = (RelativeLayout) view.findViewById(R.id.rl_order);
        iv_time = (ImageView) view.findViewById(R.id.iv_time);
        iv_liansai = (ImageView) view.findViewById(R.id.iv_liansai);
        iv_order = (ImageView) view.findViewById(R.id.iv_order);
        rl_time.setOnClickListener(this);
        rl_liansai.setOnClickListener(this);
        rl_order.setOnClickListener(this);
        rl_tab = (LinearLayout) view.findViewById(R.id.rl_tab);
        ll_up = (LinearLayout) view.findViewById(R.id.rl_sanjiao);
        iv_time_up = (ImageView) view.findViewById(R.id.iv_time_up);
        iv_match_up = (ImageView) view.findViewById(R.id.iv_match_up);
        iv_order_up = (ImageView) view.findViewById(R.id.iv_order_up);
        refresh = (PullToRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(this);
        listView = (ListView) view.findViewById(R.id.listView);
        reviewAdapter = new ReviewAdapter(reviewLists, getActivity());
        listView.setAdapter(reviewAdapter);
        listView.setOnItemClickListener(this);
        timeAdapter = new ReviewPopTimeAdapter(times,getActivity());
        rightAdapter = new MatchRightAdapter(optionMatches,getActivity());
        leftAdapter = new MatchLeftAdapter(matches, getActivity());
        getOptionDate();//获取搜索条件
        getReviewList(date, cat1, cat2, sort);//进入界面拉取数据
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_time:
                //按时间搜索
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_time,null);
                ImageView iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
                final ListView listView = (ListView) view.findViewById(R.id.listView);
                listView.setAdapter(timeAdapter);
                if (timeWindow == null){
                    timeWindow = new PopupWindow(view,display.getWidth(),display.getHeight(),true);
                    timeWindow.setOutsideTouchable(true);
                    timeWindow.setBackgroundDrawable(new BitmapDrawable());
                }
                timeWindow.setFocusable(true);
                int xPos = -timeWindow.getWidth() / 2
                        + ll_up.getWidth() / 2;
                timeWindow.showAsDropDown(ll_up, xPos, 0);
                timeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        iv_time.setBackgroundResource(R.drawable.pull_arrow);
                        iv_time_up.setVisibility(View.GONE);
                    }
                });
                iv_time_up.setVisibility(View.VISIBLE);
                iv_time.setBackgroundResource(R.drawable.up_arrow);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.findViewById(R.id.iv_mark_time).setVisibility(View.VISIBLE);
                        ((TextView) view.findViewById(R.id.tv_time_time)).setTextColor(getResources().getColor(R.color.theme_color));
                        times.get(position).setIsSelected(true);
                        if (mark != position) {
                            listView.getChildAt(mark).findViewById(R.id.iv_mark_time).setVisibility(View.GONE);
                            ((TextView) listView.getChildAt(mark).findViewById(R.id.tv_time_time)).setTextColor(getResources().getColor(R.color.textcolor_1));
                            times.get(mark).setIsSelected(false);
                        }
                        mark = position;
                        timeWindow.dismiss();
                        date = times.get(position).getDays();
                        matches.clear();
                        if (optionBase.getCat_conf() != null) {
                            matches.addAll(optionBase.getCat_conf());
                            matches.get(matchl).setIsSelected(false);
                            leftAdapter.notifyDataSetChanged();
                        }
                        if (optionMatches.size() > (matchr + 1)) {
                            optionMatches.get(matchr).setIsSelected(false);
                        }
                        optionMatches.clear();
                        rightAdapter.notifyDataSetChanged();
                        cat1 = "";
                        cat2 = "";
                        getReviewList(date, cat1, cat2, sort);
                    }
                });
                iv_bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timeWindow.dismiss();
                    }
                });
                break;
            case R.id.rl_liansai:
                //联赛按钮点击事件
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_match,null);
                ImageView iv_b = (ImageView) view1.findViewById(R.id.iv_bg);
                final ListView left = (ListView) view1.findViewById(R.id.listView_left);
                final ListView right = (ListView) view1.findViewById(R.id.listView_right);
                left.setAdapter(leftAdapter);
                right.setAdapter(rightAdapter);
                if (matchWindow == null){
                    matchWindow = new PopupWindow(view1,display.getWidth(),display.getHeight(),true);
                    matchWindow.setOutsideTouchable(true);
                    matchWindow.setBackgroundDrawable(new BitmapDrawable());
                }
                matchWindow.setFocusable(true);
                int xPos1 = -matchWindow.getWidth() / 2
                        + ll_up.getWidth() / 2;
                matchWindow.showAsDropDown(ll_up, xPos1, 0);
                matchWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        iv_liansai.setBackgroundResource(R.drawable.pull_arrow);
                        iv_match_up.setVisibility(View.GONE);
                    }
                });
                iv_match_up.setVisibility(View.VISIBLE);
                iv_liansai.setBackgroundResource(R.drawable.up_arrow);
                //联赛左侧listview点击事件
                left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.findViewById(R.id.rl_match).setBackgroundColor(getResources().getColor(R.color.white));
                        ((TextView)view.findViewById(R.id.tv_match)).setTextColor(getResources().getColor(R.color.textcolor_1));
                        matches.get(position).setIsSelected(true);
                        if (matchl!=position){
                            left.getChildAt(matchl) .findViewById(R.id.rl_match).setBackgroundColor(getResources().getColor(R.color.review_search_bg));
                            ((TextView)left.getChildAt(matchl).findViewById(R.id.tv_match)).setTextColor(getResources().getColor(R.color.textcolor_1));
                            matches.get(matchl).setIsSelected(false);
                        }
                        matchl = position;
                        cat1 = matches.get(position).getCat_id();
                        if (null != matches.get(position).getSon()){

                            if (optionMatches.size()>0){
                                optionMatches.clear();
                            }
                            for (int i = 0;i<matches.get(position).getSon().size();i++){
                                matches.get(position).getSon().get(i).setIsSelected(false);
                            }
                            matchr = 0;
                            optionMatches.addAll(matches.get(position).getSon());
                            rightAdapter.notifyDataSetChanged();
                            right.setAdapter(rightAdapter);
                        }else {
                            //点击全部操作
                            optionMatches.clear();
                            rightAdapter.notifyDataSetChanged();
                            if (optionBase.getDate_conf()!= null){
                                times.clear();
                                times.addAll(optionBase.getDate_conf());
                                times.get(mark).setIsSelected(false);
                                timeAdapter.notifyDataSetChanged();
                            }
                            right.setAdapter(rightAdapter);
                            matchWindow.dismiss();
                            date = "";
                            cat1 = "";
                            cat2 = "";
                            Log.e("全部","11111111");
                            getReviewList(date, cat1, cat2, sort);
                        }

                    }
                });
                //联赛右侧listview点击事件
                right.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.findViewById(R.id.iv_mark_time).setVisibility(View.VISIBLE);
                        ((TextView)view.findViewById(R.id.tv_time_time)).setTextColor(getResources().getColor(R.color.theme_color));
                        optionMatches.get(position).setIsSelected(true);
                        if (matchr !=position){
                            right.getChildAt(matchr).findViewById(R.id.iv_mark_time).setVisibility(View.GONE);
                            ((TextView)right.getChildAt(matchr).findViewById(R.id.tv_time_time)).setTextColor(getResources().getColor(R.color.textcolor_1));
                            optionMatches.get(matchr).setIsSelected(false);
                        }
                        cat2 = optionMatches.get(position).getCat_id();
                        date = "";
                        if (optionBase.getDate_conf()!= null){
                            times.clear();
                            times.addAll(optionBase.getDate_conf());
                            times.get(mark).setIsSelected(false);
                            timeAdapter.notifyDataSetChanged();
                        }
                        getReviewList(date, cat1, cat2, sort);
                        matchr = position;
                        matchWindow.dismiss();
                    }
                });
                iv_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        matchWindow.dismiss();
                    }
                });
                break;
            case R.id.rl_order:
                //排序 本期不做已隐藏
//                View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_time,null);
//                final ListView orderListView = (ListView) view2.findViewById(R.id.listView);
//                orderListView.setAdapter(new ReviewOrderAdpter(orders,getActivity()));
//                if (orderWindow == null){
//                    orderWindow = new PopupWindow(view2,display.getWidth(),display.getHeight(),true);
//                    orderWindow.setOutsideTouchable(true);
//                    orderWindow.setBackgroundDrawable(new BitmapDrawable());
//                }
//                orderWindow.setFocusable(true);
//                int xPos2 = -orderWindow.getWidth() / 2
//                        + ll_up.getWidth() / 2;
//                orderWindow.showAsDropDown(ll_up, xPos2, 4);
//                orderWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                    @Override
//                    public void onDismiss() {
//                        iv_order_up.setVisibility(View.GONE);
//                        iv_order.setBackgroundResource(R.drawable.pull_arrow);
//                    }
//                });
//                iv_order_up.setVisibility(View.VISIBLE);
//                iv_order.setBackgroundResource(R.drawable.up_arrow);
//                orderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        view.findViewById(R.id.iv_mark_time).setVisibility(View.VISIBLE);
//                        ((TextView)view.findViewById(R.id.tv_time_time)).setTextColor(getResources().getColor(R.color.theme_color));
//                        if (order!=position){
//                            orderListView.getChildAt(order).findViewById(R.id.iv_mark_time).setVisibility(View.GONE);
//                            ((TextView)orderListView.getChildAt(order).findViewById(R.id.tv_time_time)).setTextColor(getResources().getColor(R.color.textcolor_1));
//                        }
//                        order = position;
//                        orderWindow.dismiss();
//                    }
//                });
                break;
        }
    }
    //获取搜索条件
    public void getOptionDate(){
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", "1")
                .add("version", "1")
                .build();
        new HttpRequestUtils(getActivity()).httpPost(Preference.review_option, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("option",json);
                Gson gson = new Gson();
                Type type = new TypeToken<ReviewOptionBase>() {}.getType();
                if (!ConfigUtils.isJsonFormat(json)){
                    Toast.makeText(getActivity(),"数据访问失败",Toast.LENGTH_SHORT).show();
                }
                optionBase = gson.fromJson(json, type);
                if (optionBase.getStatus().equals("_0000")){
                    if (optionBase.getDate_conf()!= null){
                        times.addAll(optionBase.getDate_conf());
                    }
                    if (optionBase.getCat_conf()!= null){
                        matches.addAll(optionBase.getCat_conf());
                    }
                }else {
                    Toast.makeText(getActivity(),"数据访问失败",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    //获取回顾列表
    private void getReviewList(String date,String cat1,String cat2,String sort) {
        Log.e("options", "date:" + date + "cat1:" + cat1 + "cat2:" + cat2 + "sort:" + sort);
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", "1")
                .add("version", "1")
                .add("date",date)
                .add("cat1",cat1)
                .add("cat2",cat2)
                .add("sort",sort)
                .add("page","1")
                .add("size","20")
                .build();
        HttpRequestUtils.ResRultListener listener =  new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                refresh.refreshFinish(PullToRefreshLayout.FAIL);
                ll_empty.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(String json) {
                Log.e("review_list", json);
                refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                Gson gson = new Gson();
                Type type = new TypeToken<ReviewBase>(){}.getType();
                if (!ConfigUtils.isJsonFormat(json)){
                    Toast.makeText(getActivity(),"数据访问失败",Toast.LENGTH_SHORT).show();
                }
                ReviewBase base = gson.fromJson(json, type);
                if (base.getStatus().equals("_0000")){
                    page = 2;
                    isFirstReview = false;
                    reviewLists.clear();
                    if (base.getList().size()>0){
                        ll_empty.setVisibility(View.GONE);
                        reviewLists.addAll(base.getList());
                        reviewAdapter.notifyDataSetChanged();
                    }else {
                        //数据为空
//                        reviewLists.clear();
                        ll_empty.setVisibility(View.VISIBLE);
                        reviewAdapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(getActivity(),"访问失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (isFirstReview){
            new HttpRequestUtils(getActivity(),loading,false).httpPost(Preference.review_list, build,listener);
        }else {
            new HttpRequestUtils(getActivity()).httpPost(Preference.review_list, build,listener);
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getReviewList(date,cat1,cat2,sort);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", "1")
                .add("version", "1")
                .add("date", date)
                .add("cat1",cat1)
                .add("cat2",cat2)
                .add("sort",sort)
                .add("page",String.valueOf(page))
                .add("size","20")
                .build();
        new HttpRequestUtils(getActivity()).httpPost(Preference.review_list, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
            }

            @Override
            public void onSuccess(String json) {
                Log.e("onSuccess"+String.valueOf(page),json);
                refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                Gson gson = new Gson();
                Type type = new TypeToken<ReviewBase>(){}.getType();
                ReviewBase base = gson.fromJson(json, type);
                if (base.getList().size()>0){
                    page++;
                    reviewLists.addAll(base.getList());
                    reviewAdapter.notifyDataSetChanged();
                }
            }
        });
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), ReviewPlayActivity.class);
        intent.putExtra("match_id",reviewLists.get(position).getBase_info().getMatch_id());
        startActivity(intent);
    }
}
