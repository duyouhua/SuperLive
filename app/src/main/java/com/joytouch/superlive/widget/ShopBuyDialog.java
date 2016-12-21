package com.joytouch.superlive.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ChargeActivity;
import com.joytouch.superlive.activity.LiuLiangActivity;
import com.joytouch.superlive.activity.ResetNameActivity;
import com.joytouch.superlive.activity.addvicemanageActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.ChangstatusBack;
import com.joytouch.superlive.javabean.ShopItemInfo;
import com.joytouch.superlive.javabean.registInfo;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/1/25.
 * 购买的二次确定界面
 */
public class ShopBuyDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private LinearLayout ll_shopduihuan;
    private CheckBox cb_check;
    private String s="";//代表点击的是头像,0代表点击的是正航

    public ShopBuyDialog(Context context) {
        super(context, R.style.Dialog_bocop);
        this.context = context;
    }
    private boolean isSure = false;
    private SharedPreferences sp;
    private boolean isSuccess = false;
    private ShopItemInfo bean;
    private Button buy;
    private Button use;
    private TextView name;
    private TextView time;
    private ImageView close;
    private LinearLayout content;
    private ImageView logo;
    private RelativeLayout logoBG;
    private boolean isTools = false;
    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setIsTools(boolean isTools) {
        this.isTools = isTools;
    }

    public ShopItemInfo getBean() {
        return bean;
    }

    public void setBean(ShopItemInfo bean) {
        this.bean = bean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdetail);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
    }

    private void initView() {
        cb_check=(CheckBox)findViewById(R.id.cb_check);
        ll_shopduihuan=(LinearLayout)findViewById(R.id.ll_shopduihuan);
        content = (LinearLayout) findViewById(R.id.v4_shop_content);
        name = (TextView) findViewById(R.id.v4_goods_name);
        time = (TextView) findViewById(R.id.v4_shop_time);
        buy = (Button) findViewById(R.id.v4_shop_buy);
        use = (Button) findViewById(R.id.v4_shop_use);
        close = (ImageView) findViewById(R.id.v4_goods_detail_close);
        logo = (ImageView) findViewById(R.id.v4_goods_logo);
        logoBG = (RelativeLayout) findViewById(R.id.v4_logo_back);
        //是勋章并且没有佩戴
        if(bean.type.equals("0") && bean.status.equals("0")){
            buy.setVisibility(View.VISIBLE);
            buy.setText("购买");
            use.setVisibility(View.GONE);
            //是勋章并且已佩戴
        }else if (bean.type.equals("0") && bean.status.equals("1")){
            ll_shopduihuan.setVisibility(View.GONE);
            buy.setVisibility(View.VISIBLE);
            buy.setText("已佩戴");
            buy.setEnabled(false);
            use.setVisibility(View.GONE);
        }else if (bean.type.equals("1")){
            ll_shopduihuan.setVisibility(View.GONE);
        }
        //道具
        if (bean.type.equals("1") ){
            use.setVisibility(View.GONE);
            use.setText("购买");
            buy.setVisibility(View.VISIBLE);
        }
        name.setText(bean.name);
        GradientDrawable myGrad = (GradientDrawable)logoBG.getBackground();
        if (!bean.bgcolor.equals("")){
            myGrad.setColor(Color.parseColor(bean.bgcolor));
        }
                TextView tv = new TextView(context);
                tv.setTextColor(context.getResources().getColor(R.color.v4_gray_two));
                if("0".equals(bean.type)) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(params);
                    SpannableString sst = new SpannableString(bean.detail);
                    tv.setText(sst);
                    String str="道具使用期限 "+ bean.expire +" 天";
                    SpannableString ss = new SpannableString(str);
                    ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.v4_main)),6, 6+bean.expire.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    time.setText(ss);
                }else if ("1".equals(bean.type)){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(params);
                    tv.setText(bean.detail);
                    String str="道具有效期"+bean.expire+"天";
                    SpannableString ss = new SpannableString(str);
                    ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.v4_main)), 5, 5 + bean.expire.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    time.setText(ss);
                }else{
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    tv.setLayoutParams(params);
                    tv.setText(bean.detail);
                    String str="使用期限1个月";
                    SpannableString ss = new SpannableString(str);
                    ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.v4_main)), 4, 5,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    time.setText(ss);
                }
                tv.setTextSize(11);
                content.addView(tv);
        buy.setOnClickListener(this);
        use.setOnClickListener(this);
        close.setOnClickListener(this);
        if(bean.status.equals("1")){
            buy.setEnabled(false);
            buy.setTextColor(context.getResources().getColor(R.color.v4_gray_two));
            buy.setText("已佩戴");
        }else{
            buy.setEnabled(true);
            buy.setTextColor(context.getResources().getColor(R.color.v4_withe));
            buy.setText("购买");
        }
        
//        ImageLoader.getInstance().displayImage(Preference.img_url + "200x200/" + bean.image, logo, ImageUtils.reward);
        if (bean.type.equals("0")){//勋章
            if (bean.name.equals("一级勋章")){
                logo.setBackgroundResource(R.drawable.x1);
            }else if(bean.name.equals("二级勋章")){
                logo.setBackgroundResource(R.drawable.x2);
            }else if(bean.name.equals("三级勋章")){
                logo.setBackgroundResource(R.drawable.x3);
            }
        }else if (bean.type.equals("1")){//昵称卡
            logo.setBackgroundResource(R.drawable.n1);
        }else if (bean.type.equals("2")){//流量
            logo.setBackgroundResource(R.drawable.liu);
        }
        ll_shopduihuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_check.isChecked()){
                    cb_check.setChecked(false);
                }else{
                    cb_check.setChecked(true);
                }
            }
        });

        if (s.equals("1")){
            buy.setVisibility(View.GONE);
            use.setVisibility(View.GONE);
            ll_shopduihuan.setVisibility(View.GONE);
        }

        if (bean.type.equals("0") && bean.status.equals("0") && bean.name.equals("三级勋章") && s.equals("0")){
            ll_shopduihuan.setVisibility(View.VISIBLE);
        }else{
            ll_shopduihuan.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.v4_goods_detail_close:
                dismiss();
                break;
            case R.id.v4_shop_buy:
                if (cb_check.isChecked()!=true && bean.type.equals("0") && bean.name.equals("三级勋章")){
                    Intent intent=new Intent(context,addvicemanageActivity.class);
                    intent.putExtra("goods_id",bean.goods_id);
                    intent.putExtra("price",bean.price);
                    intent.putExtra("oldprice",bean.old_price);
                    intent.putExtra("ballprice",bean.price2);
                    intent.putExtra("isActivity",bean.is_active);
                    context.startActivity(intent);
                    dismiss();
                    break;
                }
                if ( bean.type.equals("2")){
                    Intent intent=new Intent(context,LiuLiangActivity.class);
                    context.startActivity(intent);
                    dismiss();
                    break;
                }
                if(!isSure){
                    isSure = true;
                    FormBody.Builder build_2=new FormBody.Builder();
                    build_2
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .add("goods_id", bean.goods_id)
                            .add("token",  sp.getString(Preference.token, ""))
                            .build();
                    new HttpRequestUtils((Activity) context).httpPost(Preference.insertgoods, build_2,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {

                                }
                                @Override
                                public void onSuccess(String json) {
                                    Log.e("商品购买",json.toString());
                                    Gson gson = new Gson();
                                    registInfo registinfo = gson.fromJson(json, registInfo.class);
                                    if (registinfo.status.equals("_0000")){
                                        isSuccess=true;
                                        Toast.makeText(context,"购买成功！", Toast.LENGTH_SHORT).show();
                                        ChangstatusBack achor = (ChangstatusBack) context;
                                        achor.anchor(2);
                                        dismiss();
                                    }else if (registinfo.status.equals("_1000")){
                                        isSuccess=false;
                                        new LoginUtils(context).reLogin(context);
                                        Toast.makeText(context, registinfo.message, Toast.LENGTH_SHORT).show();
                                    }else if (registinfo.status.equals("_2001")){
                                        isSuccess=false;
                                        Intent intent=new Intent(context, ChargeActivity.class);
                                        context.startActivity(intent);
                                        Toast.makeText(context, "余额不足", Toast.LENGTH_SHORT).show();
                                    }else{
                                        isSuccess=false;
                                    }
                                }
                            });
                }
                break;
            case R.id.v4_shop_use:
                Intent intent = new Intent(context, ResetNameActivity.class);
                intent.putExtra("nick_id",bean.goods_id);
                intent.putExtra("origin","1");
                context.startActivity(intent);
                dismiss();
                break;
        }
    }
    public boolean isSuccess() {
        return isSuccess;
    }

    public void setFrom(String s) {
        this.s=s;
    }
}
