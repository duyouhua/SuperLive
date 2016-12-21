package com.joytouch.superlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.RankAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.GuessRank;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;
import com.joytouch.superlive.widget.RuleDescriptionDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * 竞猜列表
 */
public class GuessRankActivity extends BaseActivity implements View.OnClickListener,PullToRefreshLayout.OnRefreshListener,AdapterView.OnItemClickListener{
    private ImageView iv_finish;
    private ImageView iv_rules;
    private PullToRefreshLayout refreshLayout;
    private PullableListView listView;
    private LinearLayout loading;
    private TextView mRank;
    private TextView mname;
    private TextView gold;
    List<GuessRank> ranks;
    private Handler handler= new Handler();
    private boolean isUp;
    private boolean isfirst;
    private int pager = 0;
    private RankAdapter adapter;
    private String roomid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_rank);
        ranks = new ArrayList<>();
        roomid = getIntent().getStringExtra("id");
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        iv_rules = (ImageView) this.findViewById(R.id.iv_rules);
        iv_rules.setOnClickListener(this);
        refreshLayout = (PullToRefreshLayout) this.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        listView = (PullableListView) this.findViewById(R.id.listView);
        listView.setAdapter(new RankAdapter(ranks, this));
        listView.setOnItemClickListener(this);
        loading = (LinearLayout) findViewById(R.id.loading);
        mRank = (TextView) findViewById(R.id.tv_total_num);
        mname = (TextView) findViewById(R.id.tv_name);
        gold = (TextView) findViewById(R.id.tv_total_gold);
        refreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                isUp = false;
                pager = 0;
                getmatData();;
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                pager++;
                isUp = true;
                getmatData();

            }
        });
        getmatData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.iv_rules:
                RuleDescriptionDialog dialog = new RuleDescriptionDialog(GuessRankActivity.this);
                dialog.show();
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        isUp = false;



    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        isUp = true;



    }

    public void getmatData(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("page",String.valueOf(pager));
        builder.add("token", (String) SPUtils.get(GuessRankActivity.this,Preference.token,"",Preference.preference));
        builder.add("room_id", roomid);
        HttpRequestUtils requestUtils;
        Log.e("pages:::",String.valueOf(pager));
        if(!isfirst) {
            requestUtils = new HttpRequestUtils(GuessRankActivity.this, loading,isUp);
        }else {
            loading.setVisibility(View.GONE);
            requestUtils = new HttpRequestUtils(GuessRankActivity.this);
        }
        requestUtils.httpPost(Preference.LiveRank, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("详情页排行榜",json);
                try {
                    if(isfirst) {
                        if (isUp) {
                            refreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        } else {
                            ranks.clear();
                            refreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        }
                    }
                    JSONObject object = new JSONObject(json);
                    parser(object);
                    adapter = new RankAdapter(ranks,GuessRankActivity.this);
                    listView.setAdapter(adapter);
                    isfirst=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void parser(JSONObject object){
        if("_0000".equals(object.optString("status"))){
            JSONObject user = object.optJSONObject("user");
            if (user.optString("top").equals("0")){
                mRank.setText("--");
            }else{
                mRank.setText(user.optString("top"));
            }
                mname.setText(user.optString("user_name"));
                gold.setText(user.optString("score"));
            JSONArray array = object.optJSONArray("LocalTyrant");
            if(array!=null){
                for (int i= 0;i<array.length();i++){
                    GuessRank rank = new GuessRank();
                    JSONObject object1 = array.optJSONObject(i);
                    rank.setGold(object1.optString("score"));
                    rank.setImage_(object1.optString("image"));
                    rank.setName(object1.optString("username"));
                    rank.setTop(object1.optString("top"));
                    rank.setLevel_(object1.optString("level"));
                    rank.setTopic(object1.optString("topic"));
                    rank.setName(object1.optString("username"));
                    rank.setUserId(object1.optString("userid"));
                    ranks.add(rank);
                }
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.e("ssssssssssssss","ssssssssssssssssssssssssssssssss======");
        Intent intent = new Intent(GuessRankActivity.this,OtherUserMessageActivity.class);
        intent.putExtra("user_id",ranks.get(position).getUserId());
        intent.putExtra("isShow",false);
        intent.putExtra("otherimg",ranks.get(position).getImage_());
        intent.putExtra("othernickname",ranks.get(position).getName());
        startActivity(intent);
    }
}
