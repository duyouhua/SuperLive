package com.joytouch.superlive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.InvitationFrriendAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.SelectorFans;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.Fans;
import com.joytouch.superlive.javabean.funs_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/27.
 * 邀请关注人和粉丝
 */
public class InvitationFriendActivity extends BaseActivity implements SelectorFans{

    private PullableListView plv;
    private List<funs_item> list;
    private String qiutanid;
    private TextView send;
    private ImageView back;
    private PullToRefreshLayout refreshprl;
    private boolean isUp;
    private int pager = 1;
    private boolean isFist = true;
    private InvitationFrriendAdapter adapter;
    private ArrayList<String> addPeople;
    private String userId = "";
    private String matchid;
    private LinearLayout loading;
    private String isPrivate;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitationfriend);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        qiutanid = getIntent().getStringExtra("qiutanid");
        matchid = getIntent().getStringExtra("matchid");
        isPrivate = getIntent().getStringExtra("isprivate");
        list = new ArrayList<>();
        addPeople = new ArrayList<>();
        initView();
        getdata();
    }

    private void initView() {
        title();
        loading = (LinearLayout) findViewById(R.id.loading);
        plv = (PullableListView) findViewById(R.id.plv);
        send = (TextView) findViewById(R.id.tv_right);
        back = (ImageView) findViewById(R.id.iv_finish);
        refreshprl = (PullToRefreshLayout) findViewById(R.id.refreshprl);
        send.setText("确定");
        title.setText("邀请关注的人");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        plv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        refreshprl.setCanPullDown(true);
        refreshprl.setCanPullUp(true);
        refreshprl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                isUp = false;
                pager = 1;
                getdata();
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                isUp = true;
                pager = pager + 1;
                getdata();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < addPeople.size(); i++) {
                    userId = userId + addPeople.get(i);
                    if (i != (addPeople.size() - 1)) {
                        userId = userId + ",";
                    }
                }
                LogUtils.e("---------", "" + userId);
                if (!TextUtils.isEmpty(userId)) {
                    sendInvitation();
                }
            }
        });
    }
    private void getdata() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(InvitationFriendActivity.this,Preference.token, "",Preference.preference))
                .add("pages", pager+"")
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        HttpRequestUtils requestUtils;
        if(!isFist) {
            requestUtils = new HttpRequestUtils(InvitationFriendActivity.this, loading,isUp);
        }else {
            loading.setVisibility(View.GONE);
            requestUtils = new HttpRequestUtils(InvitationFriendActivity.this);
        }
        requestUtils.httpPost(Preference.Fanlist, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        //访问网络失败后,隐藏动画.显示重新加载
                        if (!isFist) {
                            if (isUp) {
                                refreshprl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            } else {
                                refreshprl.refreshFinish(PullToRefreshLayout.SUCCEED);
                            }
                        }
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("粉丝列表", json);
                        int size = 0;
                        if (!isFist) {
                            if (isUp) {
                                refreshprl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                size = list.size() - 1;
                            } else {
                                refreshprl.refreshFinish(PullToRefreshLayout.SUCCEED);
                                list.clear();
                                addPeople.clear();
                            }
                        }
                        isFist = false;
                        //加载成功后,隐藏动画
                        Gson gson = new Gson();
                        Fans Bean = gson.fromJson(json, Fans.class);
                        if (Bean.status.equals("_0000")) {
                            list.addAll(Bean.list);
                            int ps;
                            if (isUp) {
                                ps = size + 1;
                            } else {
                                ps = 0;

                            }
                            for (int i = ps; i < list.size(); i++) {
                                list.get(i).setIsCheck(true);
                                addPeople.add(list.get(i).userid);
                            }
                            LogUtils.e("sssssssssss", " " + addPeople.size());
                            adapter = new InvitationFrriendAdapter(list, InvitationFriendActivity.this);
                            plv.setAdapter(adapter);
                            plv.setSelection(size);

                        } else if (Bean.status.equals("_1000")) {
                            new LoginUtils(InvitationFriendActivity.this).reLogin(InvitationFriendActivity.this);
                            Toast.makeText(InvitationFriendActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(InvitationFriendActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void add(int postion) {
        addPeople.add(list.get(postion).userid);
        LogUtils.e("fans+++++++", addPeople.toString());
    }

    @Override
    public void cancel(int postion) {
        addPeople.remove(list.get(postion).userid);
        LogUtils.e("fans+++++++delete", addPeople.toString());
    }
    public  void sendInvitation(){
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(InvitationFriendActivity.this,Preference.token, "",Preference.preference))
                .add("room_id", Preference.room_id)
                .add("match_id", matchid)
                .add("userids", userId)
                .add("qiutan_id", "")
                .add("version", Preference.version)
                .add("phone", Preference.phone)
                .build();
        Log.e("粉丝邀请",Preference.room_id+"__"+matchid+"___"+userId);
        new HttpRequestUtils(InvitationFriendActivity.this).httpPost(Preference.InvitationFans, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        userId = "";
                    }

                    @Override
                    public void onSuccess(String json) {
                        userId = "";
                        try {
                            JSONObject object = new JSONObject(json);
                            if ("_0000".equals(object.optString("status"))) {
                                Toast.makeText(InvitationFriendActivity.this, "邀请好友成功！", Toast.LENGTH_SHORT).show();
                                showdialog();
                                finish();
                            } else {
                                if ("_1000".equals(object.optString("status"))) {
                                    new LoginUtils(InvitationFriendActivity.this).reLogin(InvitationFriendActivity.this);
                                }
                                Toast.makeText(InvitationFriendActivity.this, object.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showdialog() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(InvitationFriendActivity.this).httpPost(Preference.share, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(InvitationFriendActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("任务分享", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            if (Bean.is_share.equals("0")){
                                final Dialog dialog1 =  new Dialog(InvitationFriendActivity.this, R.style.Dialog_bocop);
                                dialog1.setContentView(R.layout.my_task_1);
                                LinearLayout close = (LinearLayout) dialog1.findViewById(R.id.close);
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog1.dismiss();
                                    }
                                });
                                TextView getMoney = (TextView) dialog1.findViewById(R.id.getMoney);
                                getMoney.setText("+"+Bean.getMoney());
                                dialog1.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
                                dialog1.setCancelable(false);
                                dialog1.show();
                            }
                        } else {
                            Toast.makeText(InvitationFriendActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
