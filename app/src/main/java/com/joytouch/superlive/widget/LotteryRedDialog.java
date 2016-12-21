package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.RedAdapter;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.RedChaiBase;
import com.joytouch.superlive.javabean.RedChaiUser;
import com.joytouch.superlive.javabean.redInfo;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by sks on 2016/7/21.
 */
public class LotteryRedDialog extends BaseDialog implements View.OnClickListener {
    private ImageView iv_finish;
    private CircleImageView iv_icon;
    private TextView tv_name;
    //显示竞猜题目（未开奖）
    private RelativeLayout rl_content;
    private TextView tv_subject;
    private Button but_left;
    private Button but_right;
    //开奖（赢得金币）
    private RelativeLayout rl_win;
    private TextView tv_gold_win;
    private TextView tv_red_num;
    private ListView listView;
    private RedAdapter redAdapter;
    private List<RedChaiUser> list = new ArrayList<>();
    private ImageView iv_pin;
    //开奖（未中奖）
    private RelativeLayout rl_wrong;
    private TextView tv_result;

    private SharedPreferences sp;

    private redInfo info;
    private RedChaiBase chaiBase;

    private TextView tv_result_gold;

    public LotteryRedDialog(Context context,redInfo info) {
        super(context);
        this.info = info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_lotteryred);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        iv_icon = (CircleImageView) this.findViewById(R.id.iv_icon);
        tv_name = (TextView) this.findViewById(R.id.tv_name);

        rl_content = (RelativeLayout) this.findViewById(R.id.rl_content);
        tv_subject = (TextView) this.findViewById(R.id.tv_subject);
        but_left = (Button) this.findViewById(R.id.but_left);
        but_left.setOnClickListener(this);
        but_right = (Button) this.findViewById(R.id.but_right);
        but_right.setOnClickListener(this);

        rl_win = (RelativeLayout) this.findViewById(R.id.rl_win);
        tv_gold_win = (TextView) this.findViewById(R.id.tv_gold_win);
        tv_red_num = (TextView) this.findViewById(R.id.tv_red_num);
        listView = (ListView) this.findViewById(R.id.listView);
        iv_pin = (ImageView) this.findViewById(R.id.iv_pin);
        redAdapter = new RedAdapter(list,context);
        listView.setAdapter(redAdapter);

        rl_wrong= (RelativeLayout) this.findViewById(R.id.rl_wrong);
        tv_result = (TextView) this.findViewById(R.id.tv_result);

        ImageLoader.getInstance().displayImage(Preference.img_url + "40x40/" + info.getCjzb_user_image(), iv_icon, ImageLoaderOption.optionsHeader);
        if (info.getCjzb_user_name().length()>8){
            tv_name.setText(info.getCjzb_user_name().substring(0,8) + " 的竞猜红包");
        }else {
            tv_name.setText(info.getCjzb_user_name() + " 的竞猜红包");
        }

        tv_subject.setText(info.getQuestion_title());

        String[] arry = info.getQuestion_item().split(",");
        but_left.setText(arry[0]);
        but_right.setText(arry[1]);
    }

    public void setTv_result_gold(TextView tv_result_gold) {
        this.tv_result_gold = tv_result_gold;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                dismiss();
                break;
            case R.id.but_left:
                chaiLotteryRed(but_left.getText().toString());
                break;
            case R.id.but_right:
                chaiLotteryRed(but_right.getText().toString());
                break;
        }
    }
    public void chaiLotteryRed(String answer){
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("redpacket_id", info.getId())
                .add("question_answer",answer)
                .build();
        new HttpRequestUtils((Activity)context).httpPost(Preference.grabred, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                if (ConfigUtils.isJsonFormat(json)){
                    Gson gson = new Gson();
                    Type type = new TypeToken<RedChaiBase>() {}.getType();
                    chaiBase = gson.fromJson(json, type);
                    if ("ok".equals(chaiBase.getResult().getRes_code())){
                        //抢到红包
                        if (chaiBase.getResult().getRecpacketer_user().getMoney()>0){
                            rl_content.setVisibility(View.GONE);
                            rl_win.setVisibility(View.VISIBLE);
                            rl_wrong.setVisibility(View.GONE);
                            tv_gold_win.setText("恭喜您获得" + chaiBase.getResult().getRecpacketer_user().getMoney() + "金币");
                            int num = Integer.valueOf(chaiBase.getResult().getRedpacketer().getCount())-Integer.valueOf(chaiBase.getResult().getRedpacketer().getCount_left());
                            int money = chaiBase.getResult().getRedpacketer().getMoney() -chaiBase.getResult().getRedpacketer().getMoney_left();
                            tv_red_num.setText("已抢走"+num+"个红包，共"+money+"金币");
                            if (null!=tv_result_gold){
                                tv_result_gold.setText(Integer.valueOf(tv_result_gold.getText().toString())+money+"");
                            }
                            list.clear();
                            list.addAll(chaiBase.getResult().getRedpacketer_user_list());
                            redAdapter.notifyDataSetChanged();
                            iv_pin.setVisibility(View.VISIBLE);
                        }else {
                            rl_content.setVisibility(View.GONE);
                            rl_win.setVisibility(View.GONE);
                            rl_wrong.setVisibility(View.VISIBLE);
                            tv_result.setText(chaiBase.getResult().getRes_code());
                        }
                    }else {
                        rl_content.setVisibility(View.GONE);
                        rl_win.setVisibility(View.GONE);
                        rl_wrong.setVisibility(View.VISIBLE);
                        tv_result.setText(chaiBase.getResult().getRes_code());
                    }
                }else {
                    Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
