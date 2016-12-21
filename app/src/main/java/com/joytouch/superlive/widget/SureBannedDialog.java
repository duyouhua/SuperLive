package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/6/24.
 */
public class SureBannedDialog extends BaseDialog {
    private boolean isFinish;

    public SureBannedDialog(Context context) {
        super(context);
    }
    private boolean isBanned;
    private String userid;
    private String name;
    private TextView content;
    private TextView sure;
    private TextView cancle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setIsBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_surebanned);
        content = (TextView) findViewById(R.id.content);
        sure = (TextView) findViewById(R.id.sure);
        cancle = (TextView) findViewById(R.id.cancel);
        if(isBanned){
            content.setText("是否禁言  "+name+"？");
        }else {
            content.setText("是否解禁  "+name+"？");
        }
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFinish){
                    return;
                }
                isFinish = true;
                if(isBanned){
                    banned();
                }else {
                    remove();
                }
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
    public void banned(){
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("lock_user_id", userid)
                .add("room_id", Preference.room_id)
                .add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference))
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.banned, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        isFinish = false;
                    }

                    @Override
                    public void onSuccess(String json) {
                        try {
                            JSONObject object = new JSONObject(json);
                            if ("_0000".equals(object.optString("status"))) {
                                Toast.makeText(context, name+"已被禁言！", Toast.LENGTH_SHORT).show();
                                dismiss();
                            } else {
                                isFinish = false;
                                Toast.makeText(context, object.optString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    //解除禁言
    public  void remove(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone",Preference.phone);
        builder.add("version", Preference.version);
        builder.add("remove_user_id", userid);
        builder.add("room_id", Preference.room_id);
        builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils((Activity)context);
        requestUtils.httpPost(Preference.removebanned, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                isFinish = false;
            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.optString("status"))) {
                        Toast.makeText(context, name+"禁言已被解除！", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        isFinish = false;
                       /* if("_1000".equals(object.optString("status"))){
                            new LoginUtils(context).reLogin(context);
                        }*/
                        Toast.makeText(context, object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
}
