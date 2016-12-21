package com.joytouch.superlive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.AuchorInfo;
import com.joytouch.superlive.javabean.CreateRoomBase;
import com.joytouch.superlive.javabean.LiveSource;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.SPUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/21.
 * 直播填写标题
 */
public class LiveTitleActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private Button start;
    private ImageView close;
    private String match_id;
    private String live_id;
    private String startTime;
    private String room_id;
    private AuchorInfo auchorInfo;
    private LiveSource liveSource;
    private RadioButton rb_qq,rb_wx,rb_pyq,rb_fans;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_livetitle);
        Intent intent = getIntent();
        if (intent!= null){
            liveSource = (LiveSource) intent.getSerializableExtra("source");
            Log.e("liveSource",liveSource.getLive_id());
            if (liveSource!= null){
                match_id = liveSource.getMatch_id();
                live_id = liveSource.getLive_id();
            }
        }
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.live_title);
//        if (liveSource.getMatch_name().length()>15){
//            title.setText(liveSource.getMatch_name().substring(0,14));
//        }else {
//            title.setText(liveSource.getMatch_name());
//        }
        if (liveSource.getMatch_name().trim().contains(" ")){
            title.setText(liveSource.getMatch_name().trim().split(" ")[1]);
        }else {
            title.setText(liveSource.getMatch_name());
        }
        start = (Button) findViewById(R.id.start);
        close = (ImageView) findViewById(R.id.close);
        start.setOnClickListener(this);
        close.setOnClickListener(this);
        rb_qq = (RadioButton) this.findViewById(R.id.rb_qq);
        rb_wx = (RadioButton) this.findViewById(R.id.rb_wx);
        rb_pyq = (RadioButton) this.findViewById(R.id.rb_pyq);
        rb_fans = (RadioButton) this.findViewById(R.id.rb_fans);
        rb_qq.setOnClickListener(this);
        rb_wx.setOnClickListener(this);
        rb_pyq.setOnClickListener(this);
        rb_fans.setOnClickListener(this);
    }
    public void createRoomBySource(final String tp){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("title",title.getText().toString())
                .add("token", (String)SPUtils.get(this,"token","",Preference.preference))
                .add("match_id", match_id)
                .add("live_id",live_id)
                .build();
        Log.e("share_url", (String)SPUtils.get(this,"token","",Preference.preference) + " " + match_id + " "+live_id);
        new HttpRequestUtils(this).httpPost(Preference.CreateRoomsBySource, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                LogUtils.e("createRoom",e.toString());
            }

            @Override
            public void onSuccess(String json) {
                LogUtils.e("createRoom", json);
                Gson gson = new Gson();
                Type type = new TypeToken<CreateRoomBase>(){}.getType();
                if (!ConfigUtils.isJsonFormat(json)){
                   return;
                }
                CreateRoomBase createRoomBase = gson.fromJson(json, type);
                if ("_0000".equals(createRoomBase.getStatus())){
                    //创建成功
                    startTime = createRoomBase.getStart();
                    room_id = createRoomBase.getRoom_id();
                    auchorInfo = createRoomBase.getAnchor_info();
                    String share_url = createRoomBase.getShare_url()+"?zb_id="+liveSource.getMatch_id()+"&room_id="+room_id+"&share_id="
                            +SPUtils.get(LiveTitleActivity.this,"myuser_id","",Preference.preference);
                    Intent intent = new Intent(LiveTitleActivity.this,OpenLivingDetailsActivity.class);
                    intent.putExtra("room_id", room_id);
                    intent.putExtra("start", startTime);
                    intent.putExtra("source", liveSource);
                    intent.putExtra("room_price",createRoomBase.getRoom_price());
                    intent.putExtra("auchor_info", auchorInfo);
                    intent.putExtra("shareurl", share_url);
                    intent.putExtra("gift_red",createRoomBase.getGift_of_red());
                    intent.putExtra("gift_blue",createRoomBase.getGift_of_blue());
                    intent.putExtra("room_score",createRoomBase.getRoom_score());
                    intent.putExtra("online",createRoomBase.getOnline());
                    if(createRoomBase!=null&&createRoomBase.getTeam_info().size()>0) {
                        if(createRoomBase.getTeam_info().get(0)!=null&&createRoomBase.getTeam_info().get(0).getCompetitor_name()!=null) {
                            intent.putExtra("rednames", createRoomBase.getTeam_info().get(0).getCompetitor_name());
                        }
                        if(createRoomBase.getTeam_info().get(1)!=null&&createRoomBase.getTeam_info().get(1).getCompetitor_name()!=null) {
                            intent.putExtra("bluenames", createRoomBase.getTeam_info().get(1).getCompetitor_name());
                        }
                    }
                    if (!title.getText().toString().equals("")){
                        intent.putExtra("title",title.getText().toString());
                    }else {
                        intent.putExtra("title","");
                    }
                    intent.putExtra("type",tp);
                    startActivity(intent);
                    finish();
                }else {
                    showToast(createRoomBase.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rb_qq:
                type = "qq";
                break;
            case R.id.rb_wx:
                type = "wx";
                break;
            case R.id.rb_pyq:
                type = "pyq";
                break;
            case R.id.start:
                createRoomBySource(type);
                break;
            case R.id.close:
                finish();
                break;
            case R.id.rb_fans:
                type ="fans";
                break;
        }
    }
}
