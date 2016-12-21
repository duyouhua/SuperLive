package com.joytouch.superlive.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.endZhiboActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.CloseRoom;
import com.joytouch.superlive.javabean.LotteryInfo;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/13.
 * 退出直播
 */
public class OuteLiveDialog extends Dialog implements View.OnClickListener {
    private  String teamlogo2;
    private  String teamlogo1;
    private  String teamname2;
    private  String teamname1;
    private Activity context;
    private TextView exit;
    private TextView cancel;
    private boolean isEXIT = false;
    private String room_id;
    private String start;
    private String anchor_id;
    private TextView tv_content;
    private int status=0;

    public OuteLiveDialog(Activity context,String room_id,String start,String anchor_id,String teamname1,String teamname2,String teamlogo1,String teamlogo2) {
        super(context, R.style.Dialog_bocop);
        this.context = context;
        this.room_id = room_id;
        this.start = start;
        this.anchor_id = anchor_id;
        this.teamname1=teamname1;
        this.teamname2=teamname2;
        this.teamlogo1=teamlogo1;
        this.teamlogo2=teamlogo2;
    }

    public boolean isEXIT() {
        return isEXIT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_outlive);
        initView();
        getLotteryList();
    }

    private void initView() {
        tv_content=(TextView)findViewById(R.id.tv_content);
        exit = (TextView) findViewById(R.id.exit);
        cancel = (TextView) findViewById(R.id.cancel);
        exit.setOnClickListener(this);
        cancel.setOnClickListener(this);
        tv_content.setText("");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit:
                closeRoom();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    private void getLotteryList() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", Preference.room_id);
        builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference));
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils(context);
        httpRequestUtils.httpPost(Preference.LotteryLabel, builder, new HttpRequestUtils.ResRultListener(){

            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                try {
                    Log.e("结束直播",json);
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.optString("status"))) {
                        JSONObject array = object.optJSONObject("list");
                        JSONArray conduct = array.optJSONArray("conduct");
                        JSONArray colse = array.optJSONArray("close");
                        if (conduct != null && colse != null) {
                            List<LotteryInfo> lotteryyes = new ArrayList<LotteryInfo>();
                            for (int i = 0; i < colse.length(); i++) {
                                JSONObject object1 = colse.getJSONObject(i);
                                lotteryyes.add(jsoParse(object1));
                            }

                            List<LotteryInfo> lotteryno = new ArrayList<LotteryInfo>();
                            for (int i = 0; i < conduct.length(); i++) {
                                JSONObject object1 = conduct.getJSONObject(i);
                                lotteryno.add(jsoParse(object1));
                            }
                            //如果lottery有数据,循环判断是否有未开奖
                            if (lotteryyes != null && lotteryyes.size() > 0) {
                                status = 1;
                                tv_content.setText("您还有题目没有开奖,确认退出吗?");
                            } else {
                                if (lotteryno != null && lotteryno.size() > 0) {
                                    for (int k = 0; k < lotteryno.size(); k++) {
                                        if (lotteryno.get(k).getStatus().equals("3")) {
                                            status = 1;
                                            tv_content.setText("您还有题目没有开奖,确认退出吗?");
                                            return;
                                        }
                                    }
                                }else{
                                    status = 0;
                                    tv_content.setText("确定是否结束直播?");
                                }
                            }
                        }else{
                            status = 0;
                            tv_content.setText("确定是否结束直播?");
                        }
                        } else {
                            Toast.makeText(context, object.optString("message"), Toast.LENGTH_SHORT).show();
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
        info.setWin_option(object.optString("win_option"));
        info.setStatus(object.optString("status"));

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

    private void closeRoom() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("room_id",room_id)
                .add("token", (String) SPUtils.get(context,"token","",Preference.preference))
                .add("start",start)
                .add("anchor_id",anchor_id)
                .build();
        new HttpRequestUtils(context).httpPost(Preference.CloseRoom, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("closroom", json);
                if (!ConfigUtils.isJsonFormat(json)) {
                    return;
                }
                Gson gson = new Gson();
                Type type = new TypeToken<CloseRoom>() {

                }.getType();
                final CloseRoom closeRoom = gson.fromJson(json, type);
                if ("_0000".equals(closeRoom.getStatus())) {
                    if (closeRoom.getTask_room().equals("0")) {//代表未完成,不弹框
                        Intent intent=new Intent(context,endZhiboActivity.class);
                        intent.putExtra("team1",teamname1);
                        intent.putExtra("team2",teamname2);
                        intent.putExtra("logo1",teamlogo1);
                        intent.putExtra("logo2",teamlogo2);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("closeRoom", closeRoom);
                        intent.putExtras(mBundle);
                        context.startActivity(intent);
                        context.finish();
                    } else if (closeRoom.getTask_room().equals("1")) {//完成任务,弹框
                        final Dialog dialog1 = new Dialog(context, R.style.Dialog_bocop);
                        dialog1.setContentView(R.layout.my_task_4);
                        LinearLayout rl_dialog = (LinearLayout) dialog1.findViewById(R.id.rl_dialog);
                        rl_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                context.finish();

                            }
                        });
                        dialog1.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Intent intent=new Intent(context,endZhiboActivity.class);
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("closeRoom", closeRoom);
                                intent.putExtras(mBundle);
                                context.startActivity(intent);
                            }
                        });
                        TextView getMoney = (TextView) dialog1.findViewById(R.id.getMoney);
                        getMoney.setText("+" + closeRoom.getReward_balance());
                        dialog1.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
                        dialog1.setCancelable(false);
                        dialog1.show();

                    }
                }else {
                    context.finish();
                    dismiss();
                }
            }
        });

    }
}
