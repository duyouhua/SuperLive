package com.joytouch.superlive.fragement;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cjzhibo.im.ImApi;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.javabean.LiveMatchInfoJavabean;
import com.joytouch.superlive.javabean.TitleInfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.FirstLotteryDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/7.
 */
public class LiveListFragment extends android.app.Fragment implements View.OnClickListener {
    private View view;
    private ViewPager vp;
    private LinearLayout titleContainer;
    private ArrayList<TitleInfo> titlename ;
    private ArrayList<Fragment> fragments;
    private Button lottery;
    private LinearLayout lottery_ll;
    private boolean isOpenLottery;
    private LinearLayout loading;
    private int selectPostion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragement_livelist, null);
        titlename = new ArrayList<>();
        fragments = new ArrayList<Fragment>();

        initView();

        return view;
    }

    private void initView() {
        vp = (ViewPager) view.findViewById(R.id.vp);
        loading = (LinearLayout) view.findViewById(R.id.loading);
        titleContainer = (LinearLayout) view.findViewById(R.id.livelist_title);
        lottery = (Button) view.findViewById(R.id.circle);
        lottery_ll = (LinearLayout) view.findViewById(R.id.lottery);
        gettitle();
        //监听viewpager滑动 当滑动到下个界面时改变标题的选择
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < titleContainer.getChildCount(); i++) {
                    int lable = (int) titleContainer.getChildAt(i).getTag();
                    if (lable == position) {
                        selectPostion = i;
                        titleContainer.getChildAt(i).findViewById(R.id.line).setVisibility(View.VISIBLE);
                    } else {
                        titleContainer.getChildAt(i).findViewById(R.id.line).setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        lottery.setOnClickListener(this);
        lottery_ll.setOnClickListener(this);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private  void viewData(){
        if(getActivity()==null){
            return;
        }
        //添加title选项
        for(int j = 0;j<titlename.size();j++){
            final View title = getActivity().getLayoutInflater().inflate(R.layout.fragment_livelist_title,null);
            TextView tv = (TextView) title.findViewById(R.id.tv);
            View line = title.findViewById(R.id.line);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            title.setLayoutParams(params);
            tv.setText(titlename.get(j).getName());
            title.setTag(j);
            final int finalJ = j;
            if(j==0){
                line.setVisibility(View.VISIBLE);
            }else {
                line.setVisibility(View.INVISIBLE);
            }
            //设置每个title的点击事件
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //处理点击标题滑动到对应的pager页面
                    for (int i = 0; i < titleContainer.getChildCount(); i++) {
                        int lable = (int) titleContainer.getChildAt(i).getTag();
                        if (lable == finalJ) {
                            selectPostion = i;
                            titleContainer.getChildAt(i).findViewById(R.id.line).setVisibility(View.VISIBLE);
                            vp.setCurrentItem(i);
                        } else {
                            titleContainer.getChildAt(i).findViewById(R.id.line).setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
            titleContainer.addView(title);
        }
        for(int i= 0;i<titlename.size();i++){
            LiveListItemFragment fragment = new LiveListItemFragment();
            Bundle bundle= new Bundle();
                bundle.putString("type",titlename.get(i).getId());
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        FragmentManager childFragmentManager = getChildFragmentManager();
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(childFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
        vp.setOffscreenPageLimit(titlename.size());
        vp.setAdapter(adapter);
        vp.setCurrentItem(0);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lottery:
            case R.id.circle:
                if(titlename.size()==0||!((LiveListItemFragment)fragments.get(selectPostion)).isloading()){
                    break;
                }
                //设置开关
                if(isOpenLottery){
                    isOpenLottery = false;
                    lottery_ll.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
                    lottery.setTextColor(getActivity().getResources().getColor(R.color.white));
                    lottery.setBackgroundResource(R.drawable.cicrl_main);

                }else {
                    isOpenLottery = true;
                    lottery.setTextColor(getActivity().getResources().getColor(R.color.main));
                    lottery_ll.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                    lottery.setBackgroundResource(R.drawable.cicrl_withe);
                    if(!(boolean) SPUtils.get(getActivity(), "isFistLiveLottery", false, Preference.preference)){
                        FirstLotteryDialog dialog = new FirstLotteryDialog(getActivity());
                        dialog.show();
                    }
                }
                for(int i=0;i<fragments.size();i++){
                    ((LiveListItemFragment)fragments.get(i)).openLottery(isOpenLottery);
                }
                break;
        }
    }
//请求头部数据
    public void gettitle(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        HttpRequestUtils requestUtils = new HttpRequestUtils(getActivity(),loading,false);
        requestUtils.httpPost(Preference.livetitle, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    if("_0000".equals(object.optString("status"))){
                        JSONArray array = object.optJSONArray("tags");
                        if(array!=null){
                            for(int i = 0;i<array.length();i++){
                                JSONObject object1 = array.getJSONObject(i);
                                TitleInfo title= new TitleInfo();
                                title.setId(object1.optString("tag_id"));
                                title.setName(object1.optString("tag_name"));
                                if(i == 2&&Preference.isNBA) {
                                    titlename.add(0,title);
                                }else{
                                    titlename.add(title);
                                }
                            }
                            viewData();
                        }
                        SuperLiveApplication.imApi.setMatchListener(new ImApi.Match() {
                            @Override
                            public void match(boolean b,List<LiveMatchInfoJavabean> infos) {
                                LogUtils.e("ssssss","s11111111111");
                                for(int i = 0;i<titlename.size();i++){
                                    ((LiveListItemFragment)fragments.get(i)).refreshMatch(infos);
                                }

                            }

                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
