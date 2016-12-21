package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.SystemAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.messagelistAll;
import com.joytouch.superlive.utils.view.CommonShowView;
import com.joytouch.superlive.utils.xutil.XUtil;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统通知详情页
 * Created by Administrator on 5/18 0018.
 */
public class SyatemInfoActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private String time;
    private String img_url;
    private String content;
    private String text;
    private ImageView img;
    private SharedPreferences sp;
    private DbManager db_all;
    private String my_userid;
    private String other_userid;
    private List<messagelistAll> Syslist=new ArrayList<>();
    private CommonShowView loadstate;
    private PullToRefreshLayout pullToRefreshLayout;
    private ListView lv_message;
    private SystemAdapter adapter;
    private int sel;
    private TextView tv_title;
    private ImageView iv_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_system_info);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        //创建一个表存储需要在列表中填充数据的表   MessageTable
        //创建一个数据库存储所有的数据  MessageTableAll
        DbManager.DaoConfig daoConfig_ = XUtil.getMessageList();
        db_all = x.getDb(daoConfig_);
        time=getIntent().getStringExtra("time");
        img_url=getIntent().getStringExtra("img_url");
        content=getIntent().getStringExtra("content");
        text=getIntent().getStringExtra("text");
        my_userid=getIntent().getStringExtra("my_userid");
        other_userid=getIntent().getStringExtra("other_userid");

        initView();
        //加载成功后,隐藏动画
        loadstate.stopAnimal();
        if (findSystenListl().size()>0){
            //有数据的情况下,隐藏界面
            loadstate.setVisibility(View.GONE);
        }else{
            //如果没有数据,显示暂无数据
            loadstate.setnull();
        }

        adapter=new SystemAdapter(Syslist, this,time);
        lv_message.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        loadstate=(CommonShowView)this.findViewById(R.id.loadstate_ing);
        loadstate.setloading();
        loadstate.starAnimal();
        pullToRefreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        pullToRefreshLayout.setCanPullUp(false);
        pullToRefreshLayout.setCanPullDown(false);
        lv_message = (ListView) this.findViewById(R.id.lv_content);
        pullToRefreshLayout.setOnRefreshListener(this);
        lv_message.setOnItemClickListener(this);
        tv_title.setText("通知");
        iv_finish.setOnClickListener(this);
    }

    public List<messagelistAll> findSystenListl() {
        Syslist.clear();
        try {
            List<messagelistAll> users = db_all.selector(messagelistAll.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", other_userid)
                    .and("type", "=",3)
                    .findAll();
            if(users == null || users.size() == 0){
                Toast.makeText(SyatemInfoActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                return Syslist;//请先调用dbAdd()方法
            } else{
                for (int k=users.size()-1;k>=0;k--){
                    Syslist.add(users.get(k));
                }
//                Syslist.addAll(users);
                Log.e("总表中:",Syslist.size()+"");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return Syslist;
    }
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
