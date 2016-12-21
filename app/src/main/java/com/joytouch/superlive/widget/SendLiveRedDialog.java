package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ChargeActivity;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.SendRedBase;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.FormBody;

/**
 * Created by sks on 2016/8/16.
 * 直播界面发红包弹窗
 */
public class SendLiveRedDialog extends BaseDialog implements View.OnClickListener{
    private ImageView iv_finish;
    private TextView tv_send;
    private Activity context;
    private String matchid;
    private String roomid;
    private TextView tv_gold;
    public SendLiveRedDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sendlivered);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_send= (TextView) this.findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                dismiss();
                break;
            case R.id.tv_send:
                sendRed(roomid,matchid);
                break;
        }
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public void setTv_gold(TextView tv_gold) {
        this.tv_gold = tv_gold;
    }

    public  void sendRed(String room_id,String match_id){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference))
                .add("room_id",room_id)
                .add("match_id", match_id);
        new HttpRequestUtils(context).httpPost(Preference.sendRed, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
            }

            @Override
            public void onSuccess(String json) {
                Log.e("hongbao","1111"+json);
                if (!ConfigUtils.isJsonFormat(json)){
                    Toast.makeText(context,"网络错误",Toast.LENGTH_SHORT).show();
                    return;
                }
                    Gson gson = new Gson();
                    Type type = new TypeToken<SendRedBase>(){}.getType();
                    SendRedBase base = gson.fromJson(json,type);
                    if ("ok".equals(base.getResult().getRes_code())){
                        Log.e("balance",base.getResult().getBalance());
                        tv_gold.setText(base.getResult().getBalance());
                        dismiss();
                    }else if ("money_is_not_enough".equals(base.getResult().getRes_code())){
                        //金币不足
                        final SureDialog sureDialog = new SureDialog(context);
                        sureDialog.setContents("金币不足，是否跳转充值页面？");
                        sureDialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (sureDialog.isSure()){
                                    Intent intent = new Intent(context, ChargeActivity.class);
                                    context.startActivity(intent);
                                }
                            }
                        });
                        sureDialog.show();
                        dismiss();
                    }else {
                        Toast.makeText(context,base.getResult().getRes_code(),Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                //友盟发红包计数事件
                MobclickAgent.onEvent(context, "redpackage");
            }
        });
    }
}
