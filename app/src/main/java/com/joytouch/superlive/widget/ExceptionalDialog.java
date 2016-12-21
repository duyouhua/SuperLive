package com.joytouch.superlive.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.AnchorInfo;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/25.
 * 打赏
 */
public class ExceptionalDialog extends BaseDialog implements View.OnClickListener {
     private ImageView header;
     private TextView name;
     private ImageView level_bg;
     private LinearLayout container;
     private Button sure;
     private List<String> golds;
     private AnchorInfo infl;
     private RelativeLayout bg;
     private int[] goldLogo;
     private int select = -1;
     private boolean isReward;
     private boolean isLoading;
     private RelativeLayout container_rl;
     private String matchid;

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public ExceptionalDialog(Context context) {
        super(context);
    }

    public void setInfl(AnchorInfo infl) {
        this.infl = infl;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_exceptional);
        golds = new ArrayList<>();
        goldLogo = new int[]{R.drawable.reward0,R.drawable.reward1,R.drawable.reward2,
                R.drawable.reward3,R.drawable.reward4};
        getData();
        initView();
        name.setText(infl.getAnchorname());
        ImageLoader.getInstance().displayImage(Preference.photourl+"200x200/"+infl.getHeaderimg(), header, ImageLoaderOption.optionsHeader);
        ConfigUtils.level(level_bg,infl.getLevel());
    }

    private void initView() {
        header = (ImageView) findViewById(R.id.heard);
        name = (TextView) findViewById(R.id.name);
        level_bg = (ImageView) findViewById(R.id.level_bg);
        container = (LinearLayout) findViewById(R.id.container_ll);
        sure = (Button) findViewById(R.id.sure);
        bg = (RelativeLayout) findViewById(R.id.bg);
        container_rl = (RelativeLayout) findViewById(R.id.rl_container);


        sure.setOnClickListener(this);
        bg.setOnClickListener(this);
    }
    //获取打赏金额列表
    public void getData(){
        HttpRequestUtils requestUtils = new HttpRequestUtils((Activity)context);

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone",Preference.phone);
        builder.add("version", Preference.version);
        requestUtils.httpPost(Preference.exceptional_list, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.getString("status"))) {
                        JSONArray array = object.getJSONArray("reward");
                        //填充打赏金额的list
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                golds.add(array.optString(i));
                            }

                        }
                    }
                    //填充打赏金额的每个item
                    for (int i = 0; i < golds.size(); i++) {
                        View view = LayoutInflater.from(context).inflate(R.layout.dialog_golds, null);
                        final LinearLayout button = (LinearLayout) view.findViewById(R.id.item);
                        //设置标志区分是哪个金额
                        button.setTag(i);
                        button.setBackgroundColor(context.getResources().getColor(R.color.withe));
                        ImageView img = (ImageView) view.findViewById(R.id.rewarlogo);
                        img.setBackgroundResource(goldLogo[i]);

                        TextView tv = (TextView) view.findViewById(R.id.reward);
                        tv.setTextColor(context.getResources().getColor(R.color.textcolor_1));
                        tv.setText("x" + golds.get(i));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                        params.weight = 1;
                        view.setLayoutParams(params);
                        if (i == 0) {
                            button.setBackgroundResource(R.drawable.cicrl_withe_border_yellow);
                            tv.setTextColor(context.getResources().getColor(R.color.color_yellow));
                            select = 0;
                        }
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //当点击打赏金额时把其它金额取消选中效果
                                if (select == (int) view.getTag()) {
                                    return;
                                }
                                select = (int) view.getTag();
                                view.setBackgroundResource(R.drawable.cicrl_withe_border_yellow);

                                for (int j = 0; j < container.getChildCount(); j++) {

                                    if (j != (int) view.getTag()) {
                                        container.getChildAt(j).findViewById(R.id.item).setBackgroundColor(context.getResources().getColor(R.color.withe));
                                        ((TextView) container.getChildAt(j).findViewById(R.id.reward)).setTextColor(context.getResources().getColor(R.color.textcolor_1));
                                    } else {
                                        ((TextView) container.getChildAt(j).findViewById(R.id.reward)).setTextColor(context.getResources().getColor(R.color.color_yellow));
                                    }
                                }
                            }
                        });
                        container.addView(view);

                    }
                    isLoading = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bg:
                dismiss();
                break;
            case R.id.sure:
                if(!isReward&isLoading) {
                    isReward = true;
                    reward();

                }
                break;
        }

    }
    //提交打赏的金额
    public void reward(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone",Preference.phone);
        builder.add("version", Preference.version);
        builder.add("anchor_id", infl.anchorid);
        builder.add("room_id", Preference.room_id);
        builder.add("money", golds.get(select));
        //builder.add("token", "815cd26a-b394-4f1d-b7cc-ffe6caeb52d7");
       builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils((Activity)context);
        requestUtils.httpPost(Preference.reward, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                    isReward = false;
            }

            @Override
            public void onSuccess(String json) {
                isReward = false;
                try {
                    JSONObject object = new JSONObject(json);
                    if("_0000".equals(object.optString("status"))){
                        animation();
                    }else {
                        if("_1000".equals(object.optString("status"))){
                            new LoginUtils(context).reLogin(context);
                        }
                        Toast.makeText(context,object.optString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    //打赏动画
    private void animation() {

        View fromView = container.getChildAt(select).findViewById(R.id.rewarlogo);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                goldLogo[select]);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();


        final ImageView iv = new ImageView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        iv.setLayoutParams(params);
        iv.setImageBitmap(bitmap);
        container_rl.addView(iv);

        int[] loc_0 = new int[2];
        int[] loc_1 = new int[2];
        int[] loc_2 = new int[2];

        iv.getLocationOnScreen(loc_0);
        fromView.getLocationOnScreen(loc_1);
        header.getLocationOnScreen(loc_2);

        TranslateAnimation t1 = new TranslateAnimation(loc_1[0] - loc_0[0]
                + fromView.getWidth() / 2 - width / 2, loc_2[0] - loc_0[0]
                + header.getWidth() / 2 - width / 2, loc_1[1] - loc_0[1]
                + fromView.getHeight() / 2 - height / 2, loc_2[1] - loc_0[1]
                + header.getHeight() / 2 - height / 2);

        final AnimationSet set = new AnimationSet(false);
        set.addAnimation(t1);

        set.setDuration(1000);

        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
                iv.setVisibility(View.GONE);
                set();
            }
        });
        iv.startAnimation(set);
    }
    public void set() {
        //创建属性动画组合
        AnimatorSet set = new AnimatorSet();
        // 把透明度动画对象作用到按钮上
        ObjectAnimator aa = ObjectAnimator.ofFloat(header, "alpha", 1.0f, 0.6f, 0.3f, 0.0f, 0.2f,
                0.4f, 0.6f, 0.8f, 1.0f);
        aa.setDuration(1000);
//        aa.setRepeatCount(2);
        aa.setRepeatMode(ObjectAnimator.REVERSE);

        // 把旋转动画对象作用到按钮上
        ObjectAnimator ra = ObjectAnimator.ofFloat(header, "rotationY", 0, 30, 60,
                90, 120, 150, 180, 210, 240, 270, 300, 330, 360);
        ObjectAnimator ra1 = ObjectAnimator.ofFloat(header, "rotationX", 0, 30, 60,
                90, 120, 150, 180, 210, 240, 270, 300, 330, 360);
        ra.setDuration(1000);
        ra1.setDuration(1000);
//        ra.setRepeatCount(2);
        ra.setRepeatMode(ObjectAnimator.REVERSE);
        ra1.setRepeatMode(ObjectAnimator.REVERSE);
        // 把缩放动画对象作用到按钮上
        ObjectAnimator sa = ObjectAnimator.ofFloat(header, "scaleY",0.0f, 0.2f,
                0.4f, 0.6f, 0.8f, 1.0f);
        ObjectAnimator sa1 = ObjectAnimator.ofFloat(header, "scaleX",0.0f, 0.2f,
                0.4f, 0.6f, 0.8f, 1.0f);
        sa.setDuration(1000);
        sa1.setDuration(1000);
//        sa.setRepeatCount(2);
        sa.setRepeatMode(ObjectAnimator.REVERSE);
        sa1.setRepeatMode(ObjectAnimator.REVERSE);
        //按照顺序播放动画
        //set.playSequentially(ta,aa,ra,sa);

        //播放动画组合
        set.playTogether(aa, ra, ra1, sa, sa1);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                container_rl.removeAllViews();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
