package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.wheelview.ArrayWheelAdapter;
import com.joytouch.superlive.widget.wheelview.OnWheelChangedListener;
import com.joytouch.superlive.widget.wheelview.WheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/6/22.
 */
public class SelfLotteryDialog extends BaseDialog implements View.OnClickListener{
    private int postion;
    private RelativeLayout rl_red;
    private RelativeLayout rl_lottery;
    private EditText title_red;
    private EditText selector_left_red;
    private EditText selector_right_red;
    private CheckBox checkbox_1;
    private CheckBox checkbox_2;
    private TextView commit_red;
    private TextView cancel_red;
    private LinearLayout ll_red;
    private TextWatcher textWatcher;

    public SelfLotteryDialog(Context context) {
        super(context);
    }
    public SelfLotteryDialog(Context context,String room_id,String match_id){
        super(context);
        this.room_id = room_id;
        this.match_id = match_id;
    }
    private LinearLayout ll_lottery;
    private EditText title;
    private  EditText left;
    private  EditText right;
    private TextView commit;
    private TextView cancel;
    private RelativeLayout timerl;
    private TextView time;
    private String roomid;
    private String userid;
    private TextView sure;
    private TextView close;
    private WheelView wheelView;
    private LinearLayout timeselector;
    private RelativeLayout bg;
    private boolean isopen;
    private TextView tv_gold;
    private String room_id,match_id,question_title,question_item,question_answer;
    String[] timess = {"5","10","15","30","60","90","120","180"};

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_selflottery);
        initView();
    }

    private void initView() {
        //竞猜红包
        ll_red = (LinearLayout) this.findViewById(R.id.ll_red);
        rl_red = (RelativeLayout) this.findViewById(R.id.rl_red);
        rl_red.setOnClickListener(this);
        rl_lottery = (RelativeLayout) this.findViewById(R.id.rl_lottery);
        rl_lottery.setOnClickListener(this);
        title_red = (EditText) this.findViewById(R.id.title_red);
        selector_left_red = (EditText) this.findViewById(R.id.selector_left_red);
        selector_right_red = (EditText) this.findViewById(R.id.selector_right_red);
        checkbox_1 = (CheckBox) this.findViewById(R.id.checkbox_1);
        checkbox_1.setOnClickListener(this);
        checkbox_2 = (CheckBox) this.findViewById(R.id.checkbox_2);
        checkbox_2.setOnClickListener(this);
        commit_red = (TextView) this.findViewById(R.id.commit_red);
        commit_red.setOnClickListener(this);
        cancel_red = (TextView) this.findViewById(R.id.cancel_red);
        cancel_red.setOnClickListener(this);

        //竞猜题目
        ll_lottery = (LinearLayout) this.findViewById(R.id.ll_lottery);
        bg = (RelativeLayout) findViewById(R.id.bg);
        title = (EditText) findViewById(R.id.title);
        timeselector = (LinearLayout) findViewById(R.id.time_selector);
        left = (EditText) findViewById(R.id.selector_left);
        right = (EditText) findViewById(R.id.selector_right);
        commit = (TextView) findViewById(R.id.commit);
        cancel = (TextView) findViewById(R.id.cancel);
        timerl = (RelativeLayout) findViewById(R.id.time_rl);
        time = (TextView) findViewById(R.id.time);
        sure = (TextView) findViewById(R.id.sure);
        close = (TextView) findViewById(R.id.close);
        wheelView = (WheelView) findViewById(R.id.timeselector);
        wheelView.setVisibleItems(7);// 显示5条
        wheelView.setCyclic(false);
        wheelView.setCurrentItem(0);
        selector_left_red.addTextChangedListener(new TextWatcher() {
            String tem = "";
            String digits = ",";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tem = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selector_left_red.setSelection(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.equals(tem)) {
                    return;
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < str.length(); i++) {
                    if (digits.indexOf(str.charAt(i)) < 0) {
                        sb.append(str.charAt(i));
                    }
                }
                tem = sb.toString();
                selector_left_red.setText(tem);
            }
        });
        selector_right_red.addTextChangedListener(new TextWatcher() {
            String tem = "";
            String digits = ",";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tem = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                selector_right_red.setSelection(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                if (str.equals(tem)){
                    return;
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 0;i<str.length();i++){
                    if (digits.indexOf(str.charAt(i))<0){
                        sb.append(str.charAt(i));
                    }
                }
                tem = sb.toString();
                selector_right_red.setText(tem);
            }
        });
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                WindowManager windowManager = ((Activity) context).getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                lp.height = (int) (display.getHeight());
                getWindow().setAttributes(lp);
            }
        });
        final String[] timelist = {"5分钟","10分钟","15分钟","30分钟","1小时","1.5小时","2小时","3小时"};

        wheelView.setAdapter(new ArrayWheelAdapter<String>(timelist));
        wheelView.addChangingListener(new OnWheelChangedListener() {
            // 监听当前停留在那个时间
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                postion = newValue;

            }
        });
        timerl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeselector.getVisibility() == View.VISIBLE) {
                    timeselector.setVisibility(View.GONE);
                } else {
                    timeselector.setVisibility(View.VISIBLE);
                }
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(title.getText().toString())){
                    Toast.makeText(context,"题目不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else
                if(TextUtils.isEmpty(left.getText().toString())||TextUtils.isEmpty(right.getText().toString())){
                    Toast.makeText(context,"选项不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else
                if(TextUtils.isEmpty(time.getText().toString())){
                    Toast.makeText(context,"截止時間不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else
                if(left.getText().toString().equals(right.getText().toString())){
                    Toast.makeText(context,"选项不能相同",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isopen) {
                    isopen = true;
                    getData();
                }
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeselector.setVisibility(View.GONE);
                time.setText(timelist[postion]);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.setText("");
                timeselector.setVisibility(View.GONE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        time.setText("5分钟");
        postion =0;
    }

    public void setTv_gold(TextView tv_gold) {
        this.tv_gold = tv_gold;
    }

    private void getData(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("token", (String) SPUtils.get(context,Preference.token,"",Preference.preference));
        builder.add("roomId", Preference.room_id);
        builder.add("title", title.getText().toString());
        builder.add("time_m", timess[postion]);
        builder.add("select1", left.getText().toString());
        builder.add("select2", right.getText().toString());
        HttpRequestUtils requestUtils = new HttpRequestUtils((Activity) context);
        requestUtils.httpPost(Preference.selfLotterySend, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                isopen = false;
                Toast.makeText(context,"网络连接失败，请重试！  ",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String json) {
                isopen =false;
                try {
                    JSONObject object = new JSONObject(json);
                    if("_0000".equals(object.optString("status"))){
                        Toast.makeText(context,"出题成功",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else {
                        Toast.makeText(context,object.optString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_red:
                rl_red.setBackgroundResource(R.drawable.retangle_soild_top_left_w);
                rl_lottery.setBackgroundResource(R.drawable.layer_gray_right);
                ll_red.setVisibility(View.VISIBLE);
                ll_lottery.setVisibility(View.GONE);
                break;
            case R.id.rl_lottery:
                rl_red.setBackgroundResource(R.drawable.layer_gray_left);
                rl_lottery.setBackgroundResource(R.drawable.retangle_soild_top_right_w);
                ll_red.setVisibility(View.GONE);
                ll_lottery.setVisibility(View.VISIBLE);
                break;
            case R.id.checkbox_1:
                break;
            case R.id.checkbox_2:
                break;
            case R.id.commit_red:
                //发送竞猜红包
                if ("".equals(title_red.getText().toString())){
                    Toast.makeText(context,"请输入竞猜题目。",Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(selector_left_red.getText().toString())||"".equals(selector_right_red.getText().toString())){
                    Toast.makeText(context,"请输入竞猜选项",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selector_left_red.getText().toString().equals(selector_right_red.getText().toString())){
                    Toast.makeText(context,"两个选项不能一样",Toast.LENGTH_SHORT).show();
                    return;
                }
                question_title = title_red.getText().toString();
                question_item = selector_left_red.getText().toString()+","+selector_right_red.getText().toString();
                if (checkbox_1.isChecked()){
                    question_answer = selector_left_red.getText().toString();
                }else if (checkbox_2.isChecked()){
                    question_answer = selector_right_red.getText().toString();
                }
                if (checkbox_1.isChecked()&&checkbox_2.isChecked()){
                    question_answer = selector_left_red.getText().toString()+","+ selector_right_red.getText().toString();
                }
                sendLotteryRed();
                break;
            case R.id.cancel_red:
                dismiss();
                break;
        }
    }
    /**
     * 发竞猜红包
     */
    public void sendLotteryRed(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference))
                .add("room_id",room_id)
                .add("match_id",match_id)
                .add("question_title",question_title)
                .add("question_item",question_item)
                .add("question_answer",question_answer);
        new HttpRequestUtils((Activity)context).httpPost(Preference.sendRed, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                dismiss();
            }

            @Override
            public void onSuccess(String json) {
                Log.e("jingcaihongbao", json);
                Gson gson = new Gson();
                Type type = new TypeToken<SendLotteryRedBase>() {}.getType();
                SendLotteryRedBase base = gson.fromJson(json,type);
                if (!"ok".equals(base.getResult().getRes_code())){
                    Toast.makeText(context,"金币不足",Toast.LENGTH_SHORT).show();
                }else {
                    if (null!=tv_gold){
                        tv_gold.setText(base.getResult().getBalance());
                    }
                }
                dismiss();
            }
        });
    }
}
