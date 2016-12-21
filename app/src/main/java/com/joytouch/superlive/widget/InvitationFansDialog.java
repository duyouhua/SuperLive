package com.joytouch.superlive.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.InvitationFansAdapter;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.Fans;
import com.joytouch.superlive.javabean.funs_item;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by sks on 2016/8/15.
 */
public class InvitationFansDialog extends BaseDialog implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{
    private List<funs_item> lists = new ArrayList<>();
    private ArrayList<String> addPeople = new ArrayList<>();
    private PullableGridView gridView;
    private InvitationFansAdapter fansAdapter;
    private Button but_confirm;
    private Button but_cancel;
    private TextView tv_all;
    private PullToRefreshLayout refresh;
    private LinearLayout loading;
    private boolean isUp=false;
    private int pager = 1;
    private boolean isFist = true;
    private Activity context;
    private String matchid;
    private String userids = "";
    private SharedPreferences sp;
    public InvitationFansDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invitationfans);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
        getdata();
    }

    private void initView() {
        refresh = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refresh.setCanPullDown(false);
        refresh.setOnRefreshListener(this);
        gridView = (PullableGridView) this.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(this);
        fansAdapter = new InvitationFansAdapter(lists,context);
        gridView.setAdapter(fansAdapter);
        loading = (LinearLayout) this.findViewById(R.id.loading);
        but_confirm = (Button) this.findViewById(R.id.but_confirm);
        but_cancel = (Button) this.findViewById(R.id.but_cancel);
        tv_all = (TextView) this.findViewById(R.id.tv_all);
        but_confirm.setOnClickListener(this);
        but_cancel.setOnClickListener(this);
        tv_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_confirm:
                //确定
                invitationFans();
                break;
            case R.id.but_cancel:
                dismiss();
                break;
            case R.id.tv_all:
                addPeople.clear();
                for (int i = 0;i<lists.size();i++){
                    lists.get(i).setIsCheck(true);
                    fansAdapter.notifyDataSetChanged();
                    addPeople.add(lists.get(i).userid);
                }
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        //刷新
        getdata();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        //加载更多
        loadMore();
        isUp=true;
    }

    private void getdata() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference))
                .add("pages", pager+"")
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
            HttpRequestUtils requestUtils;
            requestUtils = new HttpRequestUtils(context, loading,isUp);
            requestUtils.httpPost(Preference.Fanlist, build,
                    new HttpRequestUtils.ResRultListener() {
                        @Override
                        public void onFailure(IOException e) {
                            //访问网络失败后,隐藏动画.显示重新加载
                            refresh.refreshFinish(PullToRefreshLayout.FAIL);
                        }

                        @Override
                        public void onSuccess(String json) {
                            Log.e("粉丝列表", json);
                            refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
                            Gson gson = new Gson();
                            if (!ConfigUtils.isJsonFormat(json)) {
                                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Fans fans = gson.fromJson(json, Fans.class);
                            if (fans.status.equals("_0000")) {
                                pager++;
                                lists.clear();
                                lists.addAll(fans.list);
                                fansAdapter.notifyDataSetChanged();
                            }
                        }
                    });
    }
    private void loadMore(){
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference))
                .add("pages", pager+"")
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        HttpRequestUtils requestUtils;
        requestUtils = new HttpRequestUtils(context);
        requestUtils.httpPost(Preference.Fanlist, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        //访问网络失败后,隐藏动画.显示重新加载
                        refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("粉丝列表", json);
                        refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        Gson gson = new Gson();
                        if (!ConfigUtils.isJsonFormat(json)){
                            Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Fans fans = gson.fromJson(json, Fans.class);
                        if (fans.status.equals("_0000")){
                            pager++;
                            if (fans.list.size()>0){
                                lists.addAll(fans.list);
                                fansAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (lists.get(position).isCheck()){
            ((CheckBox)view.findViewById(R.id.selector)).setChecked(false);
            lists.get(position).setIsCheck(false);
            addPeople.remove(lists.get(position).userid);
        }else {
            ((CheckBox)view.findViewById(R.id.selector)).setChecked(true);
            lists.get(position).setIsCheck(true);
            addPeople.add(lists.get(position).userid);
        }

    }
    public void invitationFans(){
        for (int i = 0; i < addPeople.size(); i++) {
            userids = userids + addPeople.get(i);
            if (i != (addPeople.size() - 1)) {
                userids = userids + ",";
            }
        }
        LogUtils.e("---------", "" + userids);
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(context,Preference.token, "",Preference.preference))
                .add("room_id", Preference.room_id)
                .add("match_id", matchid)
                .add("userids", userids)
                .add("qiutan_id", "")
                .add("version", Preference.version)
                .add("phone", Preference.phone)
                .build();
        new HttpRequestUtils(context).httpPost(Preference.InvitationFans, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        try {
                            JSONObject object = new JSONObject(json);
                            if ("_0000".equals(object.optString("status"))) {
                                Toast.makeText(context, "邀请好友成功！", Toast.LENGTH_SHORT).show();
                                showdialog();
                                dismiss();
                            } else {
                                if ("_1000".equals(object.optString("status"))) {
                                    new LoginUtils(context).reLogin(context);
                                }
                                Toast.makeText(context, object.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    private void showdialog() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token, ""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(context).httpPost(Preference.share, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("任务分享", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            if (Bean.is_share.equals("0")){
                                final Dialog dialog1 =  new Dialog(context, R.style.Dialog_bocop);
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
                            Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }
}
