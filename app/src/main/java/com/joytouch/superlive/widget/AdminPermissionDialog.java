package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.HttpRequestUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by sks on 2016/7/20.
 */
public class AdminPermissionDialog extends BaseDialog implements View.OnClickListener {
    private TextView tv_content;
    private Button but_confirm;
    private Button but_cancel;
    private String room_id;
    private String admin_user_id;
    private String name;
    private SharedPreferences sp;
    private Activity activity;
    public AdminPermissionDialog(Activity context,String room_id,String admin_user_id,String name) {
        super(context);
        this.activity = context;
        this.room_id = room_id;
        this.admin_user_id = admin_user_id;
        this.name = name;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_adminpermission);
        initView();
    }

    private void initView() {
        tv_content = (TextView) this.findViewById(R.id.tv_content);
        tv_content.setText("是否授予 "+name+" 管理禁言权限？");
        but_cancel = (Button) this.findViewById(R.id.but_cancel);
        but_confirm = (Button) this.findViewById(R.id.but_confirm);
        but_cancel.setOnClickListener(this);
        but_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_confirm:
                permissionAdmin();
                break;
            case R.id.but_cancel:
                dismiss();
                break;
        }
    }

    private void permissionAdmin() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("room_id",room_id)
                .add("admin_user_id",admin_user_id)
                .build();
        new HttpRequestUtils(activity).httpPost(Preference.addadmin, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                dismiss();
            }

            @Override
            public void onSuccess(String json) {
                Log.e("授予禁言权限",json);
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.getString("status"))){
                        dismiss();
                    }else {
                        Toast.makeText(context,object.getString("message"),Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }catch (Exception e){
                    Log.e("e",e.toString());
                    dismiss();
                }

            }
        });
    }
}
