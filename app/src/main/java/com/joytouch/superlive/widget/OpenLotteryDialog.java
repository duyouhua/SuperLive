package com.joytouch.superlive.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.UpdateMoney;
import com.joytouch.superlive.javabean.OpenLottery;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.FormBody;

/**
 * Created by lzx on 2016/6/21.
 * 竞猜开奖dialog
 */
public class OpenLotteryDialog extends BaseDialog implements View.OnClickListener{
    private TextView tv_lottery;
    private TextView tv_typecontent1;
    private TextView tv_typecontent2;
    private String type1;
    private String type2;
    private String title;
    private CheckBox cb_1;
    private CheckBox cb_2;
    private Button but_ok;
    private Button but_cancel;
    private Activity activity;
    private String winItem;
    private String guessId;
    private boolean opend = false;
    private String money;
    private boolean isOpenLiving;

    public void setIsOpenLiving(boolean isOpenLiving) {
        this.isOpenLiving = isOpenLiving;
    }

    public String getMoney() {
        return money;
    }


    public OpenLotteryDialog(Activity activity,String guessId,String type1,String type2,String title) {
        super(activity);
        this.activity = activity;
        this.guessId = guessId;
        this.type1 = type1;
        this.type2 = type2;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_openlottery);
        initView();
    }

    private void initView() {
        tv_lottery = (TextView) this.findViewById(R.id.tv_lottery);
        tv_lottery.setText(title);
        tv_typecontent1 = (TextView) this.findViewById(R.id.tv_typecontent1);
        tv_typecontent1.setText(type1);
        tv_typecontent2 = (TextView) this.findViewById(R.id.tv_typecontent2);
        tv_typecontent2.setText(type2);
        cb_1 = (CheckBox) this.findViewById(R.id.cb_1);
        cb_1.setOnClickListener(this);
        cb_2 = (CheckBox) this.findViewById(R.id.cb_2);
        cb_2.setOnClickListener(this);
        but_ok = (Button) this.findViewById(R.id.but_ok);
        but_cancel = (Button) this.findViewById(R.id.but_cancel);
        but_ok.setOnClickListener(this);
        but_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_ok:
                openLottery();
                break;
            case R.id.but_cancel:
                dismiss();
                break;
            case R.id.cb_1:
                cb_2.setChecked(false);
                winItem = tv_typecontent1.getText().toString();
                break;
            case R.id.cb_2:
                winItem = tv_typecontent2.getText().toString();
                cb_1.setChecked(false);
                break;
        }
    }
    private void openLottery(){
        if (null == winItem){
            Toast.makeText(activity,"请选择开奖选项",Toast.LENGTH_SHORT).show();
            return;
        }
        FormBody.Builder build=new FormBody.Builder();
        build.add("version", Preference.version)
                .add("token", (String) SPUtils.get(context, "token", "", Preference.preference))
                .add("guessId",guessId)
                .add("winItem",winItem)
                .build();
        HttpRequestUtils.ResRultListener listener = new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                OpenLottery openLottery ;
                Type type = new TypeToken<OpenLottery>(){}.getType();
                Gson gson = new Gson();
                openLottery = gson.fromJson(json,type);
                if (!"_0000".equals(openLottery.getStatus())){
                    Toast.makeText(activity,openLottery.getMessage(),Toast.LENGTH_SHORT).show();
                }else {
                    opend = true;
                    money = openLottery.getBalance();
                    if(isOpenLiving){
                        UpdateMoney updateMoney = (UpdateMoney) context;
                        updateMoney.update(money);
                    }
                    SPUtils.put(context,Preference.balance,money,Preference.preference);
                    dismiss();
                }
            }
        };
        new HttpRequestUtils(activity).httpPost(Preference.openlottery, build, listener);
    }

    public boolean isOpend() {
        return opend;
    }
}
