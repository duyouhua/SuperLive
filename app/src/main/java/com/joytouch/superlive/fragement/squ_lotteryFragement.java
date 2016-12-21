package com.joytouch.superlive.fragement;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LiveDetailActivity;
import com.joytouch.superlive.adapter.AppBaseAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.ChanBestBack;
import com.joytouch.superlive.javabean.Gc_item;
import com.joytouch.superlive.javabean.GcloInfo;
import com.joytouch.superlive.javabean.SendLotteryResult;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by Administrator on 6/22 0022.
 */
public class squ_lotteryFragement extends Fragment implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener {
    private View view;
    private CommonShowView loadstate;
    private PullToRefreshLayout refreshLayout;
    private PullableListView listView;
    private squlotAdapter adapter;
    private List<Gc_item> groups= new ArrayList<Gc_item>();
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lottery,null);
        initView();
        getDate();
//        getData();
        return view;
    }

    private void getDate() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(getActivity()).httpPost(Preference.Hotguess, build,
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
                                getDate();
                            }
                        });
                        refreshLayout .refreshFinish(PullToRefreshLayout.FAIL);
                        loadstate.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(String json) {
//                        String area_str = File_Util.readAssets(getContext(), "attention.json");
                        //加载成功后,隐藏动画
                        loadstate.stopAnimal();
                        groups.clear();
                        Log.e("广场推题",json);
                        Gson gson = new Gson();
                        GcloInfo Bean = gson.fromJson(json, GcloInfo.class);
                        if (Bean.status.equals("_0000")) {
                            if (Bean.list.size()>0){
                                for (int k=0;k<Bean.list.size();k++){
                                    Bean.list.get(k).sta="0";
                                }
                                groups.addAll(Bean.list);
                                //有数据的情况下,隐藏界面
                                loadstate.setVisibility(View.GONE);
                                adapter=new squlotAdapter(groups, getActivity());
                                listView.setAdapter(adapter);
                            }else{
                                loadstate.setVisibility(View.VISIBLE);
                                //如果没有数据,显示暂无数据
                                loadstate.setnull();
                            }
                        } else {
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void getData() {
        groups.clear();
        for (int i=0;i<100;i++){
            Gc_item info=new  Gc_item();
            info.cat_type="足球";
            info.guess_id="";
            info.guess_title="哈喽,你好";
            info.match_id="";
            info.match_name="中国vs日本";
            info.my_option="0";
            info.option_left="假的";
            info.option_right="真的";
            info.room_bet="330";
            info.room_id="";
            info.stop_time="2:10";
            info.total_left="1";
            info.total_right="2";
            info.sta="0";
            groups.add(info);
        }
        adapter=new squlotAdapter(groups, getActivity());
        listView.setAdapter(adapter);
        loadstate.stopAnimal();
        loadstate.setVisibility(View.GONE);

        if (groups.size()==0){
            loadstate.setVisibility(View.VISIBLE);
            loadstate.setnull();
        }
    }

    private void initView() {
        loadstate=(CommonShowView)view.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        loadstate.setTitle("暂时没有新题啦");
        refreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setCanPullUp(false);
        listView = (PullableListView) view.findViewById(R.id.lv_content);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        getDate();
//        getData();
//        refreshLayout.refreshFinish(0);
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        refreshLayout.loadmoreFinish(0);
    }

    public void change(int position) {
        Log.e("positionL:",position+"");
        groups.get(position).sta="1";
        adapter.notifyDataSetChanged();
    }

    public class squlotAdapter extends AppBaseAdapter<Gc_item> {
        private final SharedPreferences sp;
        private final List<Gc_item> groups;
        public squlotAdapter(List<Gc_item> groups, Context activity) {
            super(groups, activity);
            this.groups = groups;
            this.context = activity;
            sp = context.getSharedPreferences(Preference.preference,
                    Context.MODE_PRIVATE);
        }

        @Override
        public View createView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder ;
            if (convertView == null){
                convertView = inflater.inflate(R.layout.squ_item_lottery,null);
                holder = new ViewHolder();
                holder.tv_matchname = (TextView) convertView.findViewById(R.id.tv_matchname);
                holder.tv_touzhu = (TextView) convertView.findViewById(R.id.tv_touzhu);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.odds_ratio = (TextView) convertView.findViewById(R.id.odds_ratio);
                holder.state = (TextView) convertView.findViewById(R.id.state);
                holder.left_selector= (Button) convertView.findViewById(R.id.left_selector);
                holder.right_selector= (Button) convertView.findViewById(R.id.right_selector);
                holder.left_gold = (TextView) convertView.findViewById(R.id.left_gold);
                holder.right_gold = (TextView) convertView.findViewById(R.id.right_gold);
                holder.container_right=(RelativeLayout)convertView.findViewById(R.id.container_right);
                holder.container_left=(RelativeLayout)convertView.findViewById(R.id.container_left);
                holder.logo = (TextView) convertView.findViewById(R.id.type_logo);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            final  Gc_item item=list.get(position);

            if (item.sta.equals("1")){
                holder.left_selector.setEnabled(false);
                holder.right_selector.setEnabled(false);
            }
            else{
                if (item.my_option.equals(item.option_left)){
                    holder.left_selector.setEnabled(true);
                    holder.right_selector.setEnabled(false);
                }else if (item.my_option.equals(item.option_right)){
                    holder.left_selector.setEnabled(false);
                    holder.right_selector.setEnabled(true);
                }else{
                    holder.right_selector.setEnabled(true);
                    holder.left_selector.setEnabled(true);
                }
            }

            holder.tv_matchname.setText(item.match_name);
            holder.tv_touzhu.setText(item.room_bet+"每注");
            holder.title.setText(item.guess_title);
            holder.left_selector.setText(item.option_left);
            holder.right_selector.setText(item.option_right);
            holder.left_gold.setText(item.total_left);
            holder.right_gold.setText(item.total_right);
            holder.odds_ratio.setText(item.stop_time + "截止");


            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,LiveDetailActivity.class);
                    intent.putExtra("romid",item.room_id);
                    intent.putExtra("matchid",item.match_id);
                    intent.putExtra("qiutanid","");
                    context.startActivity(intent);
                }
            });


            holder.left_selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfigUtils.sendLotteryResult((Activity) context, item.room_id, item.guess_id, item.option_left, item.room_bet, new ConfigUtils.ResultListener() {
                        @Override
                        public void result(SendLotteryResult result) {
                            ConfigUtils.flash(context, holder.container_left, item.room_bet);
                            holder.right_selector.setEnabled(false);
                            holder.right_gold.setText(result.getRightgold());
                            holder.left_gold.setText(result.getLeftgold());
                        }

                        @Override
                        public void last(String islast) {
                            //竞猜截止
                            showDialog(position);
                        }
                    });
                }
            });
            holder.right_selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfigUtils.sendLotteryResult((Activity) context, item.room_id, item.guess_id, item.option_right,item.room_bet, new ConfigUtils.ResultListener() {
                        @Override
                        public void result(SendLotteryResult result) {
                            ConfigUtils.flash(context, holder.container_right, item.room_bet);
                            holder.left_selector.setEnabled(false);
                            holder.left_gold.setText(result.getLeftgold());
                            holder.right_gold.setText(result.getRightgold());
                        }

                        @Override
                        public void last(String islast) {
                            //竞猜截止
                            showDialog(position);
                        }
                    });
                }
            });

            if("篮球".equals(item.cat_type)){
                holder.logo.setBackgroundResource(R.drawable.basketball);
            }else
            if("足球".equals(item.cat_type)){
                holder.logo.setBackgroundResource(R.drawable.football);
            }else {
                holder.logo.setBackgroundResource(R.drawable.match);
            }
            return convertView;
        }

        private void showDialog(final int position) {
            final Dialog dialog = new Dialog(context, R.style.Dialog_bocop);
            dialog.setContentView(R.layout.layout_lottdone);
            dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
            dialog.show();
            dialog.findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ChanBestBack back = (ChanBestBack) context;
                    back.call(position);
                }
            });
        }


        class ViewHolder{
            TextView tv_matchname;//比赛名称
            TextView tv_touzhu;//题目投注
            TextView title;//题目内容
            TextView odds_ratio;//截止日期
            TextView state;//进入直播间
            Button left_selector;//左边答案
            Button right_selector;//右边答案
            TextView left_gold;//左边投注金额
            TextView right_gold;//右边投注金额
            RelativeLayout container_right,container_left;
            TextView logo;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
