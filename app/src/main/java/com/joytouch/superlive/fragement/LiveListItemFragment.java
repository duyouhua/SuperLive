package com.joytouch.superlive.fragement;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LiveDetailActivity;
import com.joytouch.superlive.activity.WebViewActivity;
import com.joytouch.superlive.adapter.LiveListAdapter;
import com.joytouch.superlive.adapter.LiveListLotteryAdapter;
import com.joytouch.superlive.adapter.V4_BannerAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BannerInfo;
import com.joytouch.superlive.javabean.LiveListLotteryInfo;
import com.joytouch.superlive.javabean.LiveMatchInfoJavabean;
import com.joytouch.superlive.javabean.MatchTime;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.FastBlur;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.AutoScrollViewPager;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.stickylistheaders.StickyListHeadersListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/7.
 */
public class LiveListItemFragment extends Fragment {
    private View view;
    private ImageView bannerImg;
    private LinearLayout bannerTitle;
    private TextView bannerName;
    private String type;
    private StickyListHeadersListView plv;
    private PullToRefreshLayout prl;
    private DisplayMetrics dm;
    private  int screenWidth;
    private RelativeLayout rl;
    private boolean isLottory;
    private LiveListAdapter adapter;
    private ArrayList<LiveListLotteryInfo> lottery;
    ArrayList<LiveMatchInfoJavabean>  item;
    private LiveListLotteryAdapter lotteryAdapter;
    private String loadtime;
    private boolean isup;
    private AutoScrollViewPager autoScrollViewPager;
    private List<View> bannerViews;
    private List<BannerInfo> bannerifo;
    private View view1;
    private int size;
    private boolean isFrist = true;
    private LinearLayout loading;
    private boolean isOPenLottery;
    private int postion;
    private boolean isloading = true;

    public boolean isloading() {
        return isloading;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        loadtime = "";
        bannerViews = new ArrayList<>();
        bannerifo = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_livelistitem,null);
        initView();
        return view;

    }

    private void initView() {
        //pulltorefreshlistview设置设置listview的头部时，头部的布局要是abslistview

        loading = (LinearLayout) view.findViewById(R.id.loading);
       view1 = getActivity().getLayoutInflater().inflate(R.layout.banner_layout,null);
        autoScrollViewPager = (AutoScrollViewPager) view1.findViewById(R.id.banner_container);
        //设置banner的滚动模式
        autoScrollViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_NONE);
        //设置滚动间隔时间
        autoScrollViewPager.setInterval(3000);
        //当触摸时停止滚动
        autoScrollViewPager.setStopScrollWhenTouch(true);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,AbsListView.LayoutParams.WRAP_CONTENT);
        layoutParams.height = screenWidth*8/15;
        view1.setLayoutParams(layoutParams);

        plv = (StickyListHeadersListView) view.findViewById(R.id.pelv);
        prl = (PullToRefreshLayout) view.findViewById(R.id.prl);
        plv.addHeaderView(view1);
        View empty = LayoutInflater.from(getActivity()).inflate(R.layout.empty,null);
        empty.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        plv.setEmptyView(empty);
       item = new ArrayList<>();
       lottery = new ArrayList<>();
        prl.setCanPullDown(true);
        prl.setCanPullUp(true);
        plv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!isOPenLottery) {
                    Intent intent = new Intent(getActivity(), LiveDetailActivity.class);
                    intent.putExtra("romid", item.get(i - 1).getRoomid());
                    intent.putExtra("matchid", item.get(i - 1).getMatchId());
                    intent.putExtra("islive",true);
                    startActivity(intent);
                }
            }
        });
      getmatchlist();
        prl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                isup = false;
                if (!isOPenLottery) {
                    loadtime = "";
                    getmatchlist();
                } else {
                    getLotterylist();
                }
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isup = true;
                if (item.size() > 0) {
                    loadtime = item.get(item.size() - 1).getLoadTime();
                } else {
                    loadtime = "";
                }
                getmatchlist();
            }
        });
        plv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                postion = i;

            }
        });

    }
    public void refreshMatch(List<LiveMatchInfoJavabean> infos){
        if(item==null|| item.size()==0){
            return;
        }
        for(int i = 0;i<infos.size();i++){
            for (int j = 0;j<item.size();j++){
                if(infos.get(i).getMatchId().equals(item.get(j).getMatchId())){
                    if("5".equals(infos.get(i).getMatchStatus())) {
                        item.remove(j);
                    }else{
                        item.get(j).setScore(infos.get(i).getScore());
                        item.get(j).setMatchStatus(infos.get(i).getMatchStatus());
                        item.get(j).setStating(infos.get(i).getStating());
                    }

                }
            }
        }
        adapter.notifyDataSetChanged();
        LogUtils.e("livelistitme__--", "");
        plv.setListSelection(postion);
    }
//得到背景的图片进行模糊处理
    private void applyBlur(final Bitmap bitmap) {
        if(bitmap != null) {
           blur(bitmap, bannerTitle);
        }
    }
    //模糊处理方法
    private void blur(Bitmap bkg, View view) {
        float radius = 2;
        float scaleFactor = 8;
        int width = ConfigUtils.px2dip(getActivity(), screenWidth);
        Bitmap overlay = Bitmap.createBitmap((int)(view.getMeasuredWidth()/scaleFactor), (int)(view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
    }

    //切换竞猜
    public void openLottery(boolean isopen){
        isOPenLottery = isopen;
        isloading = false;
        if(!isopen) {
            adapter = new LiveListAdapter(getActivity(), item);
            plv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            plv.addHeaderView(view1);
            prl.setCanPullUp(true);
            isloading = true;
        }else {

            getLotterylist();
        }
    }
    //得到比赛列表
    public void getmatchlist(){

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("load_date", loadtime);
        builder.add("tag_id",type);
        HttpRequestUtils requestUtils;
        if(isFrist) {
            requestUtils = new HttpRequestUtils(getActivity(), loading,isup);
        }else {
            loading.setVisibility(View.GONE);
            requestUtils = new HttpRequestUtils(getActivity());
        }
        requestUtils.httpPost(Preference.livematchlist, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                if (isup) {
                    prl.loadmoreFinish(-1);
                    //上拉加载的处理
                } else {
                    if (!isFrist) {
                        prl.refreshFinish(-1);
                    }
                }
            }

            @Override
            public void onSuccess(String json) {
                Log.e("直播列表",json);
                if (isup) {
                    prl.loadmoreFinish(0);
                    //上拉加载的处理
                    size = item.size()-1;
                } else {
                    if (!isFrist) {
                        prl.refreshFinish(0);
                    }
                    //下拉刷新的处理
                    item.clear();
                    bannerifo.clear();
                    bannerViews.clear();
                    autoScrollViewPager.removeAllViews();
                }
                jsonParseMatchList(json);
                isFrist = false;
                //添加banner的view
                if (bannerifo.size() > 0 && !isup && getActivity() != null) {
                    for (int i = 0; i < bannerifo.size(); i++) {
                        final View banner = getActivity().getLayoutInflater().inflate(R.layout.fragment_livelist_banner, null);
                        rl = (RelativeLayout) banner.findViewById(R.id.banner);
                        bannerImg = (ImageView) banner.findViewById(R.id.banner_img);
                        bannerName = (TextView) banner.findViewById(R.id.banner_name);
                        bannerTitle = (LinearLayout) banner.findViewById(R.id.banner_title);
                        bannerName.setText(bannerifo.get(i).getTitle());

                        String url = Preference.photourl+"orig/"+bannerifo.get(i).getLogo();
                        Log.e("bannerurl",url);
                        ImageLoader.getInstance().displayImage(url,bannerImg, ImageLoaderOption.optionsBaner, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                if(bitmap!=null&&bannerTitle.getMeasuredHeight()>0&&bannerTitle.getMeasuredWidth()>0) {
                                    applyBlur(bitmap);
                                }
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
                        final int finalI = i;
                        bannerImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if("match".equals(bannerifo.get(finalI).getType())){
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("userId", (String) SPUtils.get(getActivity(), Preference.myuser_id, "", Preference.preference));
                                    hashMap.put("isMatch", "yes");
                                    MobclickAgent.onEvent(getActivity(), "BannerPoint", hashMap);
                                    Intent intent = new Intent(getActivity(),LiveDetailActivity.class);
                                    intent.putExtra("matchid",bannerifo.get(finalI).getMatchid());
                                    intent.putExtra("romid", bannerifo.get(finalI).getRommid());

                                    startActivity(intent);

                                }
                                if("web".equals(bannerifo.get(finalI).getType())||"download".equals(bannerifo.get(finalI).getType())){
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("userId",(String) SPUtils.get(getActivity(), Preference.myuser_id, "", Preference.preference));
                                    hashMap.put("isMatch", "no");
                                    MobclickAgent.onEvent(getActivity(), "BannerPoint", hashMap);
                                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                    intent.putExtra("url",bannerifo.get(finalI).getUrl());
                                    intent.putExtra("title", bannerifo.get(finalI).getTitle());
                                    if(TextUtils.isEmpty(bannerifo.get(finalI).getContent())){
                                        intent.putExtra("content", "");
                                    }else {
                                        intent.putExtra("content", bannerifo.get(finalI).getContent());
                                    }
                                    intent.putExtra("isbanner",true);
                                    startActivity(intent);
                                }

                            }
                        });

                        bannerViews.add(banner);
                    }

                }
                V4_BannerAdapter bannerAdapter = new V4_BannerAdapter(bannerViews);
                autoScrollViewPager.setAdapter(bannerAdapter);
                bannerAdapter.notifyDataSetChanged();
                autoScrollViewPager.startAutoScroll();
                if (item.size() == 0) {
                    plv.removeHeaderView(view1);
                    prl.setCanPullUp(false);
                } else {
                    plv.removeHeaderView(view1);
                    plv.addHeaderView(view1);
                    prl.setCanPullUp(true);
                }
                adapter = new LiveListAdapter(getActivity(), item);
                plv.setAdapter(adapter);
                if (isup) {
                    plv.setSelection(size);
                }
            }

        });
    }
    //比赛列表的json解析
    public void jsonParseMatchList(String json){
        try {
            JSONObject object = new JSONObject(json);
            if("_0000".equals(object.optString("status"))){
                JSONArray timeArray = object.optJSONArray("date_list");
                ArrayList<MatchTime> matchtime = new ArrayList<>();
                if(timeArray!=null){

                    for(int i = 0;i<timeArray.length();i++){
                        JSONObject ob = timeArray.optJSONObject(i);
                        MatchTime timeJavabean = new MatchTime();
                        timeJavabean.setTimeLong(i+item.size());
                        timeJavabean.setTime(ob.optString("key"));
                        timeJavabean.setToday(ob.optString("is_today"));
                        matchtime.add(timeJavabean);
                    }
                }
                JSONObject matchArray = object.optJSONObject("list");
                if(matchArray!=null){
                    LogUtils.e("ssssss",""+matchtime.size());
                    for(int j = 0;j<matchtime.size();j++){
                        JSONArray jsonObject = matchArray.optJSONArray(matchtime.get(j).getTime());
                        if(jsonObject!=null){
                            for(int k= 0;k<jsonObject.length();k++){
                                JSONObject object1 = jsonObject.optJSONObject(k);

                                JSONObject baseobject = object1.optJSONObject("base_info");
                                LiveMatchInfoJavabean bean = new LiveMatchInfoJavabean();
                                bean.setMatchId(baseobject.optString("match_id"));
                                bean.setMatchNametop(baseobject.optString("match_name1"));
                                bean.setMatchNameBottom(baseobject.optString("match_name2"));
                                bean.setMatchStatus(baseobject.optString("match_status"));
                                bean.setTime(baseobject.optString("time"));
                                bean.setQiutanId(baseobject.optString("qiutan_id"));
                                bean.setCat_type(baseobject.optString("cat_type"));
                                bean.setClassification(baseobject.optString("cat_league"));
                                bean.setMatchGroup(baseobject.optString("cat_group"));
                                bean.setOnLine(baseobject.optString("online_num"));
                                bean.setType(baseobject.optString("match_mode"));
                                bean.setCat_imag(baseobject.optString("cat_image"));
                                bean.setLeague_imag(baseobject.optString("league_image"));
                                bean.setLoadTime(baseobject.optString("load_date"));
                                bean.setStartTime(baseobject.optLong("start_time"));
                                bean.setRoomid(baseobject.optString("prefer_room_id"));
                                if("1".equals(baseobject.optString("reward"))){
                                    bean.setIsReward(true);
                                }else{
                                    bean.setIsReward(false);
                                }
                                if("1".equals(baseobject.optString("subsidies"))){
                                    bean.setSubsidies(true);
                                }else{
                                    bean.setSubsidies(false);
                                }
                                if("1".equals(baseobject.optString("is_redpacket"))){
                                    bean.setIsred(true);
                                }else{
                                    bean.setIsred(false);
                                }
                                if(!TextUtils.isEmpty(baseobject.optString("result"))) {
                                    String[] ss = baseobject.optString("result").split(",");
                                    bean.setScore(ss[0] + " - " + ss[1]);
                                }
                                SharedPreferences sp = getActivity().getSharedPreferences(Preference.prefernce_alarms, Context.MODE_PRIVATE);
                                bean.setMatchAlarm(sp.getBoolean("aa" + baseobject.optString("match_id"), false));
                                JSONArray team = object1.optJSONArray("team_info");
                                if(team!=null){
                                    for(int n =0;n<team.length();n++){
                                        JSONObject object2 = team.optJSONObject(n);
                                        if(n==0) {
                                            bean.setTeama1Name(object2.optString("competitor_name"));
                                            bean.setTeama1Logo(object2.optString("competitor_image"));
                                        }else {
                                            bean.setTeama2Name(object2.optString("competitor_name"));
                                            bean.setTeama2Logo(object2.optString("competitor_image"));
                                        }
                                    }

                                }
                                bean.setMatchTime(matchtime.get(j));
                                item.add(bean);

                            }

                        }
                    }

                }
                //banner数据
                JSONArray bannerlist = object.optJSONArray("banner_list");
                if(bannerlist !=null){
                        for(int m=0;m<bannerlist.length();m++){
                            JSONObject obj = bannerlist.getJSONObject(m);
                            BannerInfo banner = new BannerInfo();
                            banner.setLogo(obj.optString("image"));
                            banner.setMatchid(obj.optString("match_id"));
                            banner.setRommid(obj.optString("room_id"));
                            banner.setUrl(obj.optString("jump_url"));
                            banner.setType(obj.optString("jump_type"));
                            banner.setTitle(obj.optString("title"));
                            banner.setContent(obj.optString("share_desp"));
                            bannerifo.add(banner);
                        }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //得到比赛列表
    public void getLotterylist(){

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("token", (String) SPUtils.get(getActivity(),Preference.token,"",Preference.preference));
        builder.add("tag_id",type);
        HttpRequestUtils requestUtils = new HttpRequestUtils(getActivity());
        requestUtils.httpPost(Preference.LiveAListLottery, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                isloading =true;
            }

            @Override
            public void onSuccess(String json) {
                if(!isOPenLottery) {
                    adapter = new LiveListAdapter(getActivity(), item);
                    plv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    plv.addHeaderView(view1);
                    prl.setCanPullUp(true);
                    return;
                }
                if (isup) {
                    prl.loadmoreFinish(0);
                    //上拉加载的处理
                    size = lottery.size();
                } else {
                    if (!isFrist) {
                        prl.refreshFinish(0);
                    }
                    //下拉刷新的处理
                    lottery.clear();
                }
                jsonParseLotteryList(json);
                isFrist = false;
                lotteryAdapter = new LiveListLotteryAdapter(getActivity(),lottery);
                plv.setAdapter(lotteryAdapter);
                plv.removeHeaderView(view1);
                lotteryAdapter.notifyDataSetChanged();
                prl.setCanPullUp(false);
                isloading =true;
            }

        });
    }
    //比赛列表的json解析
    public void jsonParseLotteryList(String json){
        try {
            JSONObject object = new JSONObject(json);
            if("_0000".equals(object.optString("status"))){
                JSONArray timeArray = object.optJSONArray("date_list");
                ArrayList<MatchTime> matchtime = new ArrayList<>();
                if(timeArray!=null){

                    for(int i = 0;i<timeArray.length();i++){
                        JSONObject ob = timeArray.optJSONObject(i);
                        MatchTime timeJavabean = new MatchTime();
                        timeJavabean.setTimeLong(i+lottery.size());
                        timeJavabean.setTime(ob.optString("key"));
                        timeJavabean.setToday(ob.optString("is_today"));
                        matchtime.add(timeJavabean);
                    }
                }
                JSONObject matchArray = object.optJSONObject("list");
                if(matchArray!=null){
                    LogUtils.e("ssssss",""+matchtime.size());
                    for(int j = 0;j<matchtime.size();j++){
                        JSONArray jsonObject = matchArray.optJSONArray(matchtime.get(j).getTime());
                        if(jsonObject!=null){
                            for(int k= 0;k<jsonObject.length();k++){
                                JSONObject baseobject = jsonObject.optJSONObject(k);

                                LiveListLotteryInfo bean = new LiveListLotteryInfo();
                                bean.setMyOption(baseobject.optString("my_option"));
                                bean.setCenter(baseobject.optString("option_mid"));
                                bean.setCenterGold(baseobject.optString("total_mid"));
                                bean.setEndTime(baseobject.optString("stop_time"));
                                bean.setLeft(baseobject.optString("option_left"));
                                bean.setLeftGold(baseobject.optString("total_left"));
                                bean.setLotteryId(baseobject.optString("guess_id"));
                                bean.setMode(baseobject.optString("mode"));
                                bean.setRight(baseobject.optString("option_right"));
                                bean.setRightGold(baseobject.optString("total_right"));
                                bean.setRoom_id(baseobject.optString("room_id"));
                                bean.setRoombet(baseobject.optString("room_bet"));


                                bean.setTime(matchtime.get(j));
                                lottery.add(bean);

                            }

                        }
                    }

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
