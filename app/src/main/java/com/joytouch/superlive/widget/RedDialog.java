package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
 * Created by sks on 2016/7/18.
 */
public class RedDialog extends BaseDialog implements View.OnClickListener{
    private CircleImageView iv_icon;
    private ImageView iv_finish;
    private TextView tv_name;
    private TextView tv_tishi;
    private TextView tv_nored;
    private ImageView iv_chai;
    private RelativeLayout rl_back;
    private RelativeLayout rl_unopen;
    private RelativeLayout rl_win;
    private TextView tv_name_red;
    private TextView tv_gold_win;
    private TextView tv_red_num;
    private ListView listView;
    private List<RedChaiUser> list = new ArrayList<>();
    private RedAdapter adapter;
    private TextView resultGold;

    private SharedPreferences sp;
    private Activity context;
    private redInfo info;
    private RedChaiBase chaiBase;
    public RedDialog(Activity context) {
        super(context);
        this.context = context;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }
    public RedDialog(Activity context,redInfo info) {
        super(context);
        this.context = context;
        this.info = info;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_red);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        iv_icon = (CircleImageView) this.findViewById(R.id.iv_icon);
        tv_name = (TextView) this.findViewById(R.id.tv_name);
        tv_tishi = (TextView) this.findViewById(R.id.tv_tishi);
        tv_nored = (TextView) this.findViewById(R.id.tv_nored);
        iv_chai = (ImageView) this.findViewById(R.id.iv_chai);
        iv_chai.setOnClickListener(this);
        iv_chai.setClickable(true);
        rl_back = (RelativeLayout) this.findViewById(R.id.rl_back);
        rl_unopen = (RelativeLayout) this.findViewById(R.id.rl_unopen);
        rl_win = (RelativeLayout) this.findViewById(R.id.rl_win);
        tv_name_red = (TextView) this.findViewById(R.id.tv_name_red);
        tv_gold_win = (TextView) this.findViewById(R.id.tv_gold_win);
        tv_red_num = (TextView) this.findViewById(R.id.tv_red_num);
        listView = (ListView) this.findViewById(R.id.listView);
        adapter = new RedAdapter(list,context);
        listView.setAdapter(adapter);
        Log.e("imag_red", Preference.img_url + info.getCjzb_user_image());
        ImageLoader.getInstance().displayImage(Preference.img_url + "40x40/" + info.getCjzb_user_image(), iv_icon, ImageLoaderOption.optionsHeader);
        tv_name.setText(info.getCjzb_user_name());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_chai:
                iv_chai.setClickable(false);
                grabRed();
                break;
            case R.id.iv_finish:
                dismiss();
                break;
        }
    }

    private void grabRed() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("redpacket_id", info.getId())
                .build();
        new HttpRequestUtils(context).httpPost(Preference.grabred, build, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("hongbao111111",json);
                if (ConfigUtils.isJsonFormat(json)){
                    Gson gson = new Gson();
                    Type type = new TypeToken<RedChaiBase>() {}.getType();
                    chaiBase = gson.fromJson(json, type);
//                    try {
                        Log.e("res_code",chaiBase.getResult().getRes_code());
                        if (null!=chaiBase.getResult().getRes_code()&&"ok".equals(chaiBase.getResult().getRes_code())){
                            //抢到红包
                            winning();
                        }else {
                            nowin();
                        }
//                    }catch (Exception e){
//                        nowin();
//                    }

                }else {
                    Toast.makeText(context,"请求失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setResultGold(TextView resultGold) {
        this.resultGold = resultGold;
    }

    //中奖
    public void winning(){
        rl_back.setBackgroundResource(R.drawable.red_win);
        rl_win.setVisibility(View.VISIBLE);
        rl_unopen.setVisibility(View.GONE);
        iv_chai.setVisibility(View.GONE);
        tv_nored.setVisibility(View.GONE);
        if (info.getCjzb_user_name().length()>8){
            tv_name_red.setText(info.getCjzb_user_name().substring(0,7)+ "的红包");
        }else {
            tv_name_red.setText(info.getCjzb_user_name() + "的红包");
        }
        tv_gold_win.setText(chaiBase.getResult().getRecpacketer_user().getMoney()+"");
        int num = Integer.valueOf(chaiBase.getResult().getRedpacketer().getCount())-Integer.valueOf(chaiBase.getResult().getRedpacketer().getCount_left());
        int money = chaiBase.getResult().getRedpacketer().getMoney() -chaiBase.getResult().getRedpacketer().getMoney_left();
        tv_red_num.setText("已抢走" + num + "个红包，共" + money + "金币");
        if (null!=resultGold){
            resultGold.setText(Integer.valueOf(resultGold.getText().toString())+money+"");
        }
        list.clear();
        list.addAll(chaiBase.getResult().getRedpacketer_user_list());
        adapter.notifyDataSetChanged();
    }
    public void nowin(){
        rl_back.setBackgroundResource(R.drawable.red_background);
        rl_win.setVisibility(View.GONE);
        rl_unopen.setVisibility(View.VISIBLE);
        iv_chai.setVisibility(View.GONE);
        tv_tishi.setVisibility(View.GONE);
        tv_nored.setVisibility(View.VISIBLE);
    }
}
