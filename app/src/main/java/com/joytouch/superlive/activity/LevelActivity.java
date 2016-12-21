package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.level_jifen;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.widget.DonutProgress;

import java.io.IOException;
import java.math.BigDecimal;

import okhttp3.FormBody;

/**
 * 等级
 */
public class LevelActivity extends BaseActivity implements View.OnClickListener{
    private DonutProgress donutProgress;
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_task;
    private SharedPreferences sp;
    private TextView tv_level_num;
    private TextView tv_current_experience;
    private TextView tv_left_experience;
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;
    private double jindu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        setContentView(R.layout.activity_level);
        initView();
        getdata();
    }
    int total;
    int progress;
    private void getdata() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(this).httpPost(Preference.live_jifen, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("等级积分", json);
                        Gson gson = new Gson();
                        level_jifen Bean = gson.fromJson(json, level_jifen.class);
                        if (Bean.status.equals("_0000")) {
                                //总共进度
                                total = Integer.parseInt(Bean.User.total);
//                            donutProgress.setMax(total);
                                //我的进度
                                progress = total - Integer.parseInt(Bean.User.need);
                                double cf = div((double) progress, (double) total, 3);
                                jindu = mul((double) 100, cf);
                            if (jindu<100 && jindu>=99){
                                jindu=98;
                            }
                            Log.e("等级",jindu+"");
                                tv_level_num.setText(Bean.User.level);
                                tv_current_experience.setText("累计经验值：" + Bean.User.score);
                                tv_left_experience.setText("距离升级还差：" + Bean.User.need);
                                handler.sendEmptyMessage(1);
                        } else {
                            Toast.makeText(LevelActivity.this, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    int i=0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (i<=jindu) {
                donutProgress.setProgress(i + 1);
                i=i+1;
            }else{
                handler.removeCallbacksAndMessages(null);
            }
            sendEmptyMessageDelayed(1, 30);
        };
    };
    private void initView() {
        tv_left_experience=(TextView)this.findViewById(R.id.tv_left_experience);
        tv_current_experience=(TextView)this.findViewById(R.id.tv_current_experience);
        tv_task=(TextView)this.findViewById(R.id.tv_task);
        tv_task.setOnClickListener(this);
        donutProgress = (DonutProgress) this.findViewById(R.id.progress_level);
//        donutProgress.setProgress(20);
        donutProgress.setInnerBottomText("我的等级");
        tv_level_num=(TextView)this.findViewById(R.id.tv_level_num);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("等级");
    }
    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b2.multiply(b1).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1,double v2,int scale){
        if(scale<=0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                finish();
                break;
            case R.id.tv_task:
                toActivity(TaskActivity.class);
                break;
        }
    }
}
