package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.MessageAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.messagelist;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于回复我的消息
 * 消息列表
 */
public class MessageListActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener {
    private ImageView iv_finish;
    private TextView tv_title;
    private PullToRefreshLayout pullToRefreshLayout;
    private ListView lv_message;
    private List<messagelist> Allist;
    private List<messagelist> onelist=new ArrayList<>();
    private MessageAdapter adapter;
    private TextView tv_right;
    private SharedPreferences sp;
    private long ok_time;
    private String ok_content;
    private String ok_otherid;
    private int nextid;//默认查询id小于2的记录
    private int lastid;//从id>1的记录开始查询
    private int over=30;//默认上啦加载一条
    private CommonShowView loadstate;
    int lo;//适配表数据库的记录总数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        Allist=new ArrayList<>();
        initView();
        //加载成功后,隐藏动画
        loadstate.stopAnimal();
        //为了我使系统消息不在界面显示时,来了消息也显示置顶,单独拿处来
        Allist.addAll(findaboutAlll());
        onelist.addAll(findaonesystem());
        Allist.addAll(0, onelist);
        if(Allist.size()==0){
            loadstate.setVisibility(View.VISIBLE);
            loadstate.setnull();
        }else{
            loadstate.setVisibility(View.GONE);
            adapter=new MessageAdapter(Allist, this);
            lv_message.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
//        onelist.clear();
//        onelist.addAll(findaonesystem());
//        lo = findsize();
//        Log.e("lo,,,,",lo+"");
//        //如果第一次超过20条
//        if (lo-over>=0){
//            nextid=lo - over;
//            lastid=lo;
//            Allist.addAll(findaboutAlll(lastid, nextid));
//            Allist.addAll(0,onelist);
//            loadstate.setVisibility(View.GONE);
//            Log.e("走了","11111");
//        }else{
//            if (lo==0){
//                nextid=0;
//                //如果没有数据,显示暂无数据
//                loadstate.setnull();
//            }else{
//                lastid=lo;
//                Log.e("走了2","11111---"+lastid);
//                nextid=0;
//                //有数据的情况下,隐藏界面
//                loadstate.setVisibility(View.GONE);
//                Allist.addAll(findaboutAlll(lastid, nextid));
//                Allist.addAll(0,onelist);
//                Log.e("走了啊3",Allist.size()+"__"+findaboutAlll(lastid, nextid).size());
//            }
//        }

        /**
         * 使用自定义监听动态改变数据(不包括离线消息)
         */
        MainActivity.setReceiveMessageListener(new MainActivity.shuaxinadapter() {
            @Override
            public void adapterMessage() {
                Allist.clear();
                onelist.clear();
                onelist.addAll(findaonesystem());
                int sisi = findsize();
                lastid = sisi;
//                Allist.addAll(findaboutAlll(lastid, nextid));
                Allist.addAll(findaboutAlll());
                Allist.addAll(0, onelist);
                adapter = new MessageAdapter(Allist, MessageListActivity.this);
                lv_message.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loadstate.setVisibility(View.GONE);
            }
        });

    }


    private void initView() {
        loadstate=(CommonShowView)this.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        tv_right=(TextView)this.findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
//        tv_right.setText("清除表");
//        tv_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                delete_table();
//                delete_table2();
//            }
//        });
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("通知");
        pullToRefreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        lv_message = (ListView) this.findViewById(R.id.lv_content);
        pullToRefreshLayout.setOnRefreshListener(this);
        lv_message.setOnItemClickListener(this);
        pullToRefreshLayout.setCanPullDown(false);
        pullToRefreshLayout.setCanPullUp(false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//        Allist.clear();
//        onelist.clear();
//        onelist.addAll(findaonesystem());
//        int sisi = findsize();
//        if (sisi-over>=0){
//            nextid=sisi - over;
//            lastid=sisi;
//            Allist.addAll(findaboutAlll(lo, nextid));
//            Allist.addAll(0,onelist);
//            loadstate.setVisibility(View.GONE);
//        }else{
//            if (sisi==0){
//                nextid=0;
//                //如果没有数据,显示暂无数据
//                loadstate.setnull();
//            }else{
//                lastid=sisi;
//                nextid=0;
//                //有数据的情况下,隐藏界面
//                loadstate.setVisibility(View.GONE);
//                Allist.addAll(findaboutAlll(sisi, nextid));
//                Allist.addAll(0,onelist);
//            }
//
//        }
//        adapter=new MessageAdapter(Allist, this);
//        lv_message.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }
    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//        if (Allist.size()>0){
//            lastid=Allist.get(Allist.size()-1).getId()-1;
//            nextid=lastid-over;
//            Allist.addAll(findaboutAlll(lastid, nextid));
//            adapter=new MessageAdapter(Allist, this);
//            lv_message.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//        }
//        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sp.edit().putString("isChatActivity", "0").commit();
    }
    private int findNormalID(String otheruserid) {
        int id = 0;
        try {
            List<messagelist> users = db_date.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", "2")
                    .findAll();
            if(users == null || users.size() == 0){

            } else{
                id=users.get(0).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    private void updateContent(String body,int id,Long time) {
        try {
            List<messagelist> stus = db_date.findAll(messagelist.class);
            messagelist stu = stus.get(id-1);
            stu.setContent(body);
            stu.setTime(time);
            stu.setStatus("1");
            db_date.update(stu);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
