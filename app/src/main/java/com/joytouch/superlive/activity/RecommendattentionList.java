package com.joytouch.superlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.RecommenAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.Message;
import com.joytouch.superlive.javabean.recom_item;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.widget.PullToRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;

/**
 * 新的用户注册后的推荐关注列表
 * Created by Administrator on 2016/4/26.
 */
public class RecommendattentionList extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener{
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_right;
    private PullToRefreshLayout pullToRefreshLayout;
    private ListView lv_message;
    private ArrayList<recom_item> messages = new ArrayList<>();
    private ArrayList miaoshu = new ArrayList();
    private RelativeLayout tv_submit;
    private RecommenAdapter Recommenapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recomment_attention);
        getDate();
        initUI();
    }

    private void getDate() {
        FormBody.Builder build=new FormBody.Builder();
        build
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.recommend, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }
                    @Override
                    public void onSuccess(String json) {
                        Log.e("推荐列表", json);
                        Gson gson = new Gson();
                        Message bean = gson.fromJson(json, Message.class);
                        if (bean.status.equals("_0000")) {
                            for (int i=0;i<bean.Recommend.HotAnchor.size();i++){
                                miaoshu.add("热门主播前十位");
                            }
                            for (int i=0;i<bean.Recommend.RichPeople.size();i++){
                                miaoshu.add("富豪榜前十位");
                            }
                            for (int i=0;i<bean.Recommend.SeniorPlayer.size();i++){
                                miaoshu.add("高级玩家前十位");
                            }
                            for (int i=0;i<bean.Recommend.WeekRank.size();i++){
                                miaoshu.add("周榜前十位");
                            }
                            messages.addAll(0, bean.Recommend.WeekRank);
                            messages.addAll(0, bean.Recommend.SeniorPlayer);
                            messages.addAll(0, bean.Recommend.RichPeople);
                            messages.addAll(0, bean.Recommend.HotAnchor);
                            Recommenapter=  new RecommenAdapter(messages, RecommendattentionList.this,miaoshu);
                            lv_message.setAdapter(Recommenapter);
                        } else {
                            Toast.makeText(RecommendattentionList.this, bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initUI() {
        tv_submit = (RelativeLayout)findViewById(R.id.tv_submit);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_right=(TextView)this.findViewById(R.id.tv_right);
        tv_title.setText("关注");
        tv_right.setText("跳过");
        pullToRefreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        lv_message = (ListView) this.findViewById(R.id.lv_content);
        //设置是否有刷新和加载
        pullToRefreshLayout.setOnRefreshListener(this);
        pullToRefreshLayout.setCanPullDown(false);
        pullToRefreshLayout.setCanPullUp(false);
        iv_finish.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
    }

    /**
     * 监听返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_right:
                Intent intent=new Intent(RecommendattentionList.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_submit:
                Intent intent2=new Intent(RecommendattentionList.this,MainActivity.class);
                startActivity(intent2);
                finish();
                break;

        }
    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }
}
