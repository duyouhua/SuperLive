package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.messagelist;
import com.joytouch.superlive.javabean.messagelistAll;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HomeAppstateutils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.xutil.XUtil;
import com.joytouch.superlive.widget.TaskDialog;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by sks on 2016/4/6.
 * BaseActivity和ChatActivity不会同时执行
 */
public class BaseActivity extends FragmentActivity {
    public ImageView back;
    public TextView title;
    private SharedPreferences sp;
    private long startTime;
    public DbManager db_date;
    public List<messagelist> Alllist=new ArrayList<messagelist>();
    public List<messagelist> onelist=new ArrayList<messagelist>();
    private ArrayList<String> strs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        DbManager.DaoConfig daoConfig= XUtil.getMessageListDaoConfig();
        db_date = x.getDb(daoConfig);
    }



    public void title(){
        back = (ImageView) findViewById(R.id.iv_finish);
        title = (TextView) findViewById(R.id.tv_title);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        sp.edit().putString("isChatActivity", "0").commit();
        startTime = System.currentTimeMillis();
        if (sp.getInt("updateversion", 4)==1){//普通更新

        } else if (sp.getInt("updateversion",4)==2){//中级更新
            HomeAppstateutils.JiantingHome(this);
        }
        //签到
        long register_time = Long.parseLong(sp.getString("register_time", "0"));
        //如果签到当天后台给的当前0点时间+24小时>当前时间,说明已经签到了,否则执行签到
        if (register_time+86400000<=(startTime)) {
            Dailyattendance();
        }
        System.gc();
        ConfigUtils.addActivity(this);
//        ConfigUtils.chagevegive(BaseActivity.this);
    }

    private void Dailyattendance() {
        if (!sp.getString(Preference.myuser_id,"").equals("")){
            FormBody.Builder build=new FormBody.Builder();
            build.add("token", sp.getString(Preference.token,""))
                    .add("phone", Preference.phone)
                    .add("version", Preference.version)
                    .build();
            new HttpRequestUtils(this).httpPost(Preference.entry, build,
                    new HttpRequestUtils.ResRultListener() {
                        @Override
                        public void onFailure(IOException e) {
                            Toast.makeText(BaseActivity.this, "访问网络失败", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String json) {
                            Log.e("每日签到参数错误", json);
                            Gson gson = new Gson();
                            BaseBean Bean = gson.fromJson(json, BaseBean.class);
                            if (Bean.status.equals("_0000")) {
                                if (Bean.is_entry.equals("1")) {
                                    //已经签到
//                                    showOneTaskDialog(Bean.money);
                                } else if (Bean.is_entry.equals("0")) {
                                    //后台帮签到
                                    //弹框签到,签到成功,保存本地数据
                                    sp.edit().putString("register_time", Bean.time)
                                            .commit();
                                    showOneTaskDialog(Bean.getMoney());
                                }
                            } else if (Bean.status.equals("_1000")) {
                                new LoginUtils(BaseActivity.this).reLogin(BaseActivity.this);
                            } else {
                                Log.e("签到", Bean.message);
                                Toast.makeText(BaseActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    private void showOneTaskDialog(String money) {
        new TaskDialog(BaseActivity.this,money).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        ConfigUtils.removeActivtity(this);
    }

    protected void toActivity(Class<?> clas){
        Intent intent = new Intent(this,clas);
        startActivity(intent);
    }
    protected void toActivity(Class<?> clas,Bundle bundle) {
        Intent intent = new Intent(this,clas);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    protected void toActivityKill(Class<?> clas){
        Intent intent = new Intent(this,clas);
        startActivity(intent);
        finish();
    }
    protected void toActivityKill(Class<?> clas,Bundle bundle){
        Intent intent = new Intent(this,clas);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
    protected void showToast(String message){
        if (message!= null&&!message.equals("")){
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        }
    }
    //找到适配表中所有关于我的通知
    public List<messagelist> findaboutAlll() {
        Alllist.clear();
        try {
            List<messagelist> users = db_date.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("type", "!=", 3)
                    .findAll();

            if(users == null || users.size() == 0){
                return Alllist;
            } else{
                Log.e("user::::",users.toString());
                for (int k=users.size()-1;k>=0;k--){
                    Alllist.add(users.get(k));
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return Alllist;
    }
//    public List<messagelist> findaboutAlll(int lastid,int nextid) {
//        Alllist.clear();
//        try {
//            List<messagelist> users = db_date.selector(messagelist.class)
//                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
//                    .and("id", "<=", lastid)
//                    .and("id", ">", nextid)
//                    .and("type", "!=", 3)
//                    .findAll();
//
//            if(users == null || users.size() == 0){
//              return Alllist;
//            } else{
//                Log.e("user::::",users.toString());
//                for (int k=users.size()-1;k>=0;k--){
//                    Alllist.add(users.get(k));
//                }
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        return Alllist;
//    }

    /**
     * 将系统消息单独拿出来显示置顶
     * @return
     */
    public List<messagelist> findaonesystem() {
        onelist.clear();
        try {
            List<messagelist> users = db_date.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("type", "=", 3)
                    .findAll();

            if(users == null || users.size() == 0){
                return onelist;
            } else{
                onelist.add(users.get(users.size()-1));
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return onelist;
    }


    public int findsize() {
        int k = 0;
        try {
            List<messagelist> users = db_date.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .findAll();

            if(users == null || users.size() == 0){
            } else{
                k=users.size();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return k;
    }
    /**
     * 该方法是用来删除适配表
     */
    public void delete_table() {
        try {
            db_date.dropTable(messagelist.class);
            sp.edit().putInt("hongdian", 0).commit();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public void delete_table2() {
        try {
            db_date.dropTable(messagelistAll.class);
            sp.edit().putInt("hongdian", 0).commit();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
