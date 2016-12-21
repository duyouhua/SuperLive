package com.joytouch.superlive.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.TaskInfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.TimeUtil;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * 我的任务界面
 * Created by Administrator on 2016/4/27.
 */
public class TaskActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_finish;
    private TextView tv_title;
    private SharedPreferences sp;
    private TextView reward_name_1;
    private TextView reward_name_2;
    private TextView reward_name_3;
    private TextView reward_name_4;
    private TextView reward_name_5;
    private TextView reward_name_6;
    private ImageView iv_reward_1;
    private ImageView iv_reward_2;
    private ImageView iv_reward_3;
    private ImageView iv_reward_4;
    private ImageView iv_reward_5;
    private ImageView iv_reward_6;
    private TextView reward_denglu;
    private TextView reward_jincai;
    private TextView reward_share;
    private TextView rew_game;
    private TextView num_room;
    private TextView num_yaoqing;
    private ImageView iv_1;
    private ImageView iv_2;
    private ImageView iv_3;
    private ImageView iv_4;
    private ImageView iv_5;
    private ImageView iv_6;
    private Dialog dialog1;
    private long LotterystartTime;
    private long GameDialogstartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);

        initView();
        getdata();
    }

    private void getdata() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token",sp.getString(Preference.token,"") )
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.quest, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("任务列表", json);
                        Gson gson = new Gson();
                        final TaskInfo bean = gson.fromJson(json, TaskInfo.class);
                        if (bean.status.equals("_0000")) {
                            reward_name_1.setText(bean.Mission.get(0).name);
                            reward_name_2.setText(bean.Mission.get(1).name);
                            reward_name_3.setText(bean.Mission.get(2).name);
                            reward_name_4.setText(bean.Mission.get(3).name);
                            reward_name_5.setText(bean.Mission.get(4).name);
                            reward_name_6.setText(bean.Mission.get(5).name);
                            reward_denglu.setText("+" + bean.Mission.get(0).money);
                            reward_jincai.setText("+" + bean.Mission.get(1).money);
                            reward_share.setText("+" + bean.Mission.get(2).money);
                            rew_game.setText("+" + bean.Mission.get(3).money);
                            num_room.setText("+" + bean.Mission.get(4).money);
                            num_yaoqing.setText("+" + bean.Mission.get(5).money);

                            if (bean.Mission.get(0).status.equals("1")) {
                                iv_1.setVisibility(View.VISIBLE);
                                reward_denglu.setVisibility(View.GONE);
                                iv_reward_1.setVisibility(View.GONE);
                            } else {
                                iv_1.setVisibility(View.GONE);
                                reward_denglu.setVisibility(View.VISIBLE);
                                iv_reward_1.setVisibility(View.VISIBLE);
                            }

                            if (bean.Mission.get(1).status.equals("1")) {
                                iv_2.setVisibility(View.VISIBLE);
                                reward_jincai.setVisibility(View.GONE);
                                iv_reward_2.setVisibility(View.GONE);
                            } else {
                                iv_2.setVisibility(View.GONE);
                                reward_jincai.setVisibility(View.VISIBLE);
                                iv_reward_2.setVisibility(View.VISIBLE);
                            }

                            if (bean.Mission.get(2).status.equals("1")) {
                                iv_3.setVisibility(View.VISIBLE);
                                reward_share.setVisibility(View.GONE);
                                iv_reward_3.setVisibility(View.GONE);
                            } else {
                                iv_3.setVisibility(View.GONE);
                                reward_share.setVisibility(View.VISIBLE);
                                iv_reward_3.setVisibility(View.VISIBLE);
                            }

                            if (bean.Mission.get(3).status.equals("1")) {
                                iv_4.setVisibility(View.VISIBLE);
                                rew_game.setVisibility(View.GONE);
                                iv_reward_4.setVisibility(View.GONE);
                            } else {
                                iv_4.setVisibility(View.GONE);
                                rew_game.setVisibility(View.VISIBLE);
                                iv_reward_4.setVisibility(View.VISIBLE);
                            }

                            if (bean.Mission.get(4).status.equals("1")) {
                                iv_5.setVisibility(View.VISIBLE);
                                num_room.setVisibility(View.GONE);
                                iv_reward_5.setVisibility(View.GONE);
                            } else {
                                iv_5.setVisibility(View.GONE);
                                num_room.setVisibility(View.VISIBLE);
                                iv_reward_5.setVisibility(View.VISIBLE);
                            }

                            if (bean.Mission.get(5).status.equals("1")) {
                                iv_6.setVisibility(View.VISIBLE);
                                num_yaoqing.setVisibility(View.GONE);
                                iv_reward_6.setVisibility(View.GONE);
                            } else {
                                iv_6.setVisibility(View.GONE);
                                num_yaoqing.setVisibility(View.VISIBLE);
                                iv_reward_6.setVisibility(View.VISIBLE);
                            }
                            //说明任务4完成
                            if (bean.Mission.get(3).status.equals("1")) {
                                LotterystartTime = System.currentTimeMillis();
                                long register_time = sp.getLong("lotterytoday", 0);
                                Log.e("弹框",(int)register_time+"__"+((int)register_time+(int)86400000)+"__"+(int)LotterystartTime+"   "+register_time+"  "+LotterystartTime);
                                if (((int)register_time+(int)86400000 +(int)86400000 )<=(int)LotterystartTime) {
                                    jincaiDialog(bean.Mission.get(3).money,bean);
                                }
                                Log.e("jingcai",bean.Mission.get(3).money);
                                //保存当前时间0点
                                sp.edit().putLong("lotterytoday", TimeUtil.getTodayZero()).commit();
                            }
                            //说明任务3完成
                            if (bean.Mission.get(2).status.equals("1")) {
                                    GameDialogstartTime = System.currentTimeMillis();
                                    long register_time = sp.getLong("gametoday", 0);
                                    if (((int)register_time+((int)86400000+(int)86400000))<=(int)GameDialogstartTime) {
                                        gemeDialog(bean.Mission.get(2).money);
                                    }
                                Log.e("jingcaigame",bean.Mission.get(2).money);
                                    //保存当前时间
                                    sp.edit().putLong("gametoday", TimeUtil.getTodayZero()).commit();
                            }
                        } else {
                            Toast.makeText(TaskActivity.this, bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * 开直播
     * @param money
     */
    public void ZhiboDialog(String money){
        final Dialog dialog3 = new Dialog(TaskActivity.this, R.style.Dialog_bocop);
        dialog3.setContentView(R.layout.my_task_4);
        LinearLayout rl_dialog = (LinearLayout) dialog3.findViewById(R.id.rl_dialog);
        rl_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog3.dismiss();
            }
        });

        TextView getMoney = (TextView) dialog3.findViewById(R.id.getMoney);
        getMoney.setText("+"+money);
        dialog3.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
        dialog3.setCancelable(false);
        dialog3.show();
    }

    /**
     * 成功邀请好友
     * @param money
     */
    public void sucessInviteDialog(String money){
        final Dialog dialog4 = new Dialog(TaskActivity.this, R.style.Dialog_bocop);
        dialog4.setContentView(R.layout.my_task_5);
        ImageView close = (ImageView) dialog4.findViewById(R.id.close);
        TextView getMoney = (TextView) dialog4.findViewById(R.id.getMoney);
        LinearLayout rl_root = (LinearLayout) dialog4.findViewById(R.id.rl_root);
        rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog4.dismiss();
            }
        });
        getMoney.setText(money);
        dialog4.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
        dialog4.setCancelable(false);
        dialog4.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog4.dismiss();
                Log.e("dismiss", "说明任务6完成");
            }
        });
    }

    /**
     * 游戏弹框
     * @param money
     */
    public void gemeDialog(String money){
        final Dialog dialog2 = new Dialog(TaskActivity.this, R.style.Dialog_bocop);
        dialog2.setContentView(R.layout.my_task_3);
        TextView  getMoney=(TextView)dialog2.findViewById(R.id.getMoney);
        LinearLayout root= (LinearLayout) dialog2.findViewById(R.id.root);
        getMoney.setText("+"+money);
        dialog2.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
        dialog2.setCancelable(false);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }
    /**
     * 竞猜弹框
     * @param money
     * @param bean
     */
    public void  jincaiDialog(String money, final TaskInfo bean){
        final Dialog dialog1 =  new Dialog(TaskActivity.this, R.style.Dialog_bocop);
        dialog1.setContentView(R.layout.my_task_2);
        LinearLayout rt_root = (LinearLayout) dialog1.findViewById(R.id.rt_root);
        rt_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        TextView getMoney = (TextView) dialog1.findViewById(R.id.getMoney);
        getMoney.setText(money);
        dialog1.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
        dialog1.setCancelable(false);
        dialog1.show();

    }
    private void initView() {
        reward_name_1=(TextView)this.findViewById(R.id.reward_name_1);
        reward_name_2=(TextView)this.findViewById(R.id.reward_name_2);
        reward_name_3=(TextView)this.findViewById(R.id.reward_name_3);
        reward_name_4=(TextView)this.findViewById(R.id.reward_name_4);
        reward_name_5=(TextView)this.findViewById(R.id.reward_name_5);
        reward_name_6=(TextView)this.findViewById(R.id.reward_name_6);
        iv_reward_1=(ImageView)this.findViewById(R.id.iv_reward_1);
        iv_reward_2=(ImageView)this.findViewById(R.id.iv_reward_2);
        iv_reward_3=(ImageView)this.findViewById(R.id.iv_reward_3);
        iv_reward_4=(ImageView)this.findViewById(R.id.iv_reward_4);
        iv_reward_5=(ImageView)this.findViewById(R.id.iv_reward_5);
        iv_reward_6=(ImageView)this.findViewById(R.id.iv_reward_6);
        iv_1=(ImageView)this.findViewById(R.id.iv_1);
        iv_2=(ImageView)this.findViewById(R.id.iv_2);
        iv_3=(ImageView)this.findViewById(R.id.iv_3);
        iv_4=(ImageView)this.findViewById(R.id.iv_4);
        iv_5=(ImageView)this.findViewById(R.id.iv_5);
        iv_6=(ImageView)this.findViewById(R.id.iv_6);
        reward_denglu=(TextView)this.findViewById(R.id.reward_denglu);
        reward_jincai=(TextView)this.findViewById(R.id.reward_jincai) ;
        reward_share=(TextView)this.findViewById(R.id.reward_share);
        rew_game=(TextView)this.findViewById(R.id.rew_game);
        num_room=(TextView)this.findViewById(R.id.num_room);
        num_yaoqing=(TextView)this.findViewById(R.id.num_yaoqing);

        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("我的任务");
        iv_finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
        }
    }
}
