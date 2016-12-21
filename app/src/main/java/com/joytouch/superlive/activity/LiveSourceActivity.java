package com.joytouch.superlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.LiveSourceAdapter;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.LiveSource;
import com.joytouch.superlive.javabean.LiveSourceBase;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.widget.PullToRefreshLayout;
import com.joytouch.superlive.widget.PullableListView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/14.
 * 直播源
 */
public class LiveSourceActivity extends BaseActivity implements View.OnClickListener,LiveSourceAdapter.OnRadioClickListener{
    private Button ok;
    private Button no;
    private PullToRefreshLayout prl;
    private PullableListView plv;
    private List<LiveSource> infoList;
    private LiveSourceAdapter adapter;
    private int ps;//用于记录选择的是哪个
    private int visibleCount;
    private boolean isLive = false;
    private LinearLayout bg;
    private DisplayMetrics dm;
    private LiveSource source;
    private int mark;//记录用户上次选中的源
    private LinearLayout loading;
    private LinearLayout empty;
    private int RESULT_CODE = 1;
    private String request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_livesource);
        Intent intent = getIntent();
        if (intent != null){
            request = intent.getStringExtra("request");
        }
        infoList = new ArrayList<LiveSource>();
        dm = new DisplayMetrics();
       getWindowManager().getDefaultDisplay().getMetrics(dm);
        //将dialog设置成全屏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = dm.widthPixels; //设置宽度
        lp.height = dm.heightPixels;
        getWindow().setAttributes(lp);
        initView();
    }
    private void initView() {
        loading = (LinearLayout) this.findViewById(R.id.loading);
        empty = (LinearLayout) this.findViewById(R.id.empty);
        ok = (Button) findViewById(R.id.sure);
        no = (Button) findViewById(R.id.cancel);
        prl = (PullToRefreshLayout) findViewById(R.id.livesource_prl);
        prl.setCanPullDown(false);
        prl.setCanPullUp(false);
        plv = (PullableListView) findViewById(R.id.livesource);
        bg = (LinearLayout) findViewById(R.id.bg);
        adapter = new LiveSourceAdapter(infoList,this);
        adapter.setRadioClickListener(this);
        plv.setAdapter(adapter);
        prl.setCanPullDown(false);
        ok.setOnClickListener(this);
        no.setOnClickListener(this);
        plv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleCount = visibleItemCount;
            }
        });
        if(isLive){
            bg.setBackgroundResource(R.drawable.live_dialog_bg);
        }else {
            //bg.setBackgroundColor(getResources().getColor(R.color.color_80black));
        }
        getSourceList();
    }
    //获取源列表
    private void getSourceList(){
        FormBody.Builder build=new FormBody.Builder();
        build.add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this,loading,false).httpPost(Preference.LiveSourceList, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
            }
            @Override
            public void onSuccess(String json) {
                LogUtils.e("liveResource",json);
                Gson gson = new Gson();
                Type type = new TypeToken<LiveSourceBase>(){}.getType();
                if (!ConfigUtils.isJsonFormat(json)){
                    showToast("数据访问失败");
                    return;
                }
                LiveSourceBase sourceBase = gson.fromJson(json,type);
                if (sourceBase.getStatus().equals("_0000")){
                    if (sourceBase.getSource().size()>0){
                        empty.setVisibility(View.GONE);
                        infoList.addAll(sourceBase.getSource());
                        adapter.notifyDataSetChanged();
                    }else {
                        //数据为空
                        empty.setVisibility(View.VISIBLE);
                    }
                }else {
                    showToast(sourceBase.getMessage());
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sure:
                if (source != null){
                    Intent detail = new Intent(LiveSourceActivity.this, LiveTitleActivity.class);
                    detail.putExtra("source", source);
                    if ("request".equals(request)){
                        Log.e("request",request);
                        setResult(RESULT_CODE,detail);
                        finish();
                    }else {
                        startActivity(detail);
                        finish();
                    }
                }else {
                    if ("request".equals(request)){
                        setResult(2);
                        finish();
                    }else {
                        showToast("请选择一个数据源");
                    }
                }
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    @Override
    public void onRaidoClick(CheckBox checkBox,int position) {
        if (position!= mark){
            infoList.get(mark).setIsSelect(false);
            mark = position;
            Log.e("mark",""+mark);
        }
        infoList.get(position).setIsSelect(true);
        adapter.notifyDataSetChanged();
        if (infoList.size()>0){
            source =infoList.get(position);
        }
    }
}
