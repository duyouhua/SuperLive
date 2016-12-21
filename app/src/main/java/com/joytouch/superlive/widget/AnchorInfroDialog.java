package com.joytouch.superlive.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.OtherUserMessageActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.FastBlur;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.SPUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/9.
 * 主播介绍dialog
 */
public class AnchorInfroDialog extends Dialog implements View.OnClickListener {
    private TextView attention;
    private ImageView level;
    private TextView session;
    private TextView odds;
    private String nickname;
    private String imag;
    private boolean isAddattention;
    private boolean isBanned;
    private TextView tv_jinyan;
    private boolean isJinyan;

    public AnchorInfroDialog(Context context) {
        super(context, R.style.Dialog_bocop);
        this.context = context;
    }
    private RoundedImageView bg;
    private CircleImageView head;
    private LinearLayout inform;
    private ImageView close;
    private TextView name;
    private TextView fans;
    private TextView  signature;
    private Context context;
    private ImageView iv;
    private Bitmap bitmaps;
    private LinearLayout container;
    private int type = 1;
    private String userid;
    private LinearLayout loading;
    private TextView tv_guanli;
    private TextView tv_chuti;
    private SharedPreferences sp;
    private String quanxian_by_zhubo;
    private String quanxian_by_yonghu;
    private String topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anchorinfo);
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        initView();
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void initView() {
        bg= (RoundedImageView) findViewById(R.id.anchor_bg);
        head = (CircleImageView) findViewById(R.id.anchor_headimg);
        inform = (LinearLayout) findViewById(R.id.inform);
        close = (ImageView) findViewById(R.id.close);
        iv = (ImageView) findViewById(R.id.iv);
        name = (TextView) findViewById(R.id.anchor_name);
        fans = (TextView) findViewById(R.id.anchor_fans);
        signature = (TextView) findViewById(R.id.signature);
        container = (LinearLayout) findViewById(R.id.anchor_container);
        level = (ImageView) findViewById(R.id.level);
        loading = (LinearLayout) findViewById(R.id.loading);

        //type 1 认证 2 个人 3其它 4 高级根据不同的类型来添加不同的布局
        switch (type){
            case 2:
                View certification = LayoutInflater.from(context).inflate(R.layout.certification_anchor,null);
                container.addView(certification);
                initAchorInfo(1,certification);
                break;
            case 1:
                View personal = LayoutInflater.from(context).inflate(R.layout.personal_anchor,null);
                container.addView(personal);
                initAchorInfo(1, personal);
                break;
            case 3:
                View other = LayoutInflater.from(context).inflate(R.layout.other_anchor,null);
                container.addView(other);
                tv_jinyan = (TextView) other.findViewById(R.id.tv_jinyan);
                tv_jinyan.setOnClickListener(this);
                initAchorInfo(3, other);
                break;
            case 4:
                inform.setVisibility(View.GONE);
                View view = LayoutInflater.from(context).inflate(R.layout.anchor_senior,null);
                container.addView(view);
                initAchorInfo(4,view);
                break;
            case 5:
                View privateAnchor = LayoutInflater.from(context).inflate(R.layout.dialog_private,null);
                container.addView(privateAnchor);
                tv_guanli = (TextView) this.findViewById(R.id.tv_guanli);
                tv_guanli.setOnClickListener(this);
                tv_chuti = (TextView) this.findViewById(R.id.tv_chuti);
                tv_chuti.setOnClickListener(this);
                initAchorInfo(5, privateAnchor);
                break;
        }
        close.setOnClickListener(this);
        inform.setOnClickListener(this);
        getData();
    }
    TextView addattention = null;
    TextView banned = null;
    public void  initAchorInfo(int type,View view) {
       attention = (TextView) view.findViewById(R.id.attention);
        fans = (TextView) view.findViewById(R.id.fans);

        session = (TextView) view.findViewById(R.id.session);
        odds = (TextView) view.findViewById(R.id.odds);


        TextView exceptional = null;

        TextView homepage = null;
        if (type != 4) {
            addattention = (TextView) view.findViewById(R.id.addattention);
            if (type ==1) {
                exceptional = (TextView) view.findViewById(R.id.exceptional);
            }
            if(type==5){
                banned = (TextView) view.findViewById(R.id.banned);
                banned.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(isBanned){
                           isBanned = false;

                       }else {
                           isBanned = true;
                       }
                        SureBannedDialog dialog = new SureBannedDialog(context);
                        dialog.setIsBanned(isBanned);
                        dialog.setUserid(userid);
                        dialog.setCancelable(false);
                        dialog.setName(name.getText().toString());
                        dialog.show();
                        dismiss();
                    }
                });
            }
            homepage = (TextView) view.findViewById(R.id.homepage);
        }

        if(type==1) {
            //打赏的点击事件
            exceptional.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reward();
                }
            });
        }
        if(type!=4) {
            //主页的点击事件
            homepage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OtherUserMessageActivity.class);
                    intent.putExtra("user_id",userid);
                    intent.putExtra("othernickname",nickname);
                    intent.putExtra("otherimg",imag);
                    context.startActivity(intent);
                }
            });
            //添加关注的点击事件
            addattention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isAddattention){
                        delguanzhu();
                    }else {
                        addguanzhu();
                    }

                }
            });
        }
    }
    //得到背景的图片进行模糊处理
    private void applyBlur() {
        head.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (bitmaps != null) {
                    float w = (float) (bg.getWidth() * 1.0 / bitmaps.getWidth());
                    float h = (float) (bg.getHeight() * 1.0 / bitmaps.getHeight());
                    Matrix matrix = new Matrix();
                    matrix.postScale(w, h); //长和宽放大缩小的比例
                    Bitmap bmp = Bitmap.createBitmap(bitmaps, 0, 0, bitmaps.getWidth(), bitmaps.getHeight(), matrix, true);
                    blur(bmp, bg);
                } else {
                    bitmaps = BitmapFactory.decodeResource(context.getResources(), R.drawable.morenbac);
                    float w = (float) (bg.getWidth() * 1.0 / bitmaps.getWidth());
                    float h = (float) (bg.getHeight() * 1.0 / bitmaps.getHeight());
                    Matrix matrix = new Matrix();
                    matrix.postScale(w, h); //长和宽放大缩小的比例
                    Bitmap bmp = Bitmap.createBitmap(bitmaps, 0, 0, bitmaps.getWidth(), bitmaps.getHeight(), matrix, true);


                    blur(bmp, bg);
                }
                return true;
            }
        });
    }
    //模糊处理方法
    private void blur(Bitmap bkg, RoundedImageView view) {
        float radius = 8;
        float scaleFactor = 8;

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor), (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);

        view.setImageDrawable(new BitmapDrawable(context.getResources(), overlay));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close:
                dismiss();
                break;
            case R.id.inform:
                break;
            case R.id.tv_guanli:
                if ("0".equals(quanxian_by_zhubo)){
                    qusetionPermission();
                }else {
                    removePermission();
                }
                break;
            case R.id.tv_chuti:
                //授予出题权限
                if ("1".equals(topic)){
                    Toast.makeText(context,"您已授予该用户出题权限",Toast.LENGTH_SHORT).show();
                    dismiss();
                }else {
                    final SureDialog dialog = new SureDialog(context);
                    dialog.setContents("是否赋予 "+name.getText().toString()+" 出题权限？");
                    dialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (dialog.isSure()) {
                                sendQuestionPermission();
                            }
                        }
                    });
                    dialog.show();

                }
                break;
            case R.id.tv_jinyan:
                //禁言
                SureBannedDialog dialog = new SureBannedDialog(context);
                dialog.setIsBanned(isJinyan);
                dialog.setUserid(userid);
                dialog.setCancelable(false);
                dialog.setName(name.getText().toString());
                dialog.show();
                dismiss();
                break;
        }
    }
    public void getData(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", Preference.room_id);
        builder.add("userid", userid);
        builder.add("token", (String) SPUtils.get(context,Preference.token,"",Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils((Activity) context,loading,false);
        String url = "";
//        if(type == 5) {
            url = Preference.privateperson;
//        }else {
//            url = Preference.person;
//        }
        requestUtils.httpPost(url, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("个人信息",json);
                try {
                    JSONObject object1 = new JSONObject(json);
                    JSONObject object = object1.optJSONObject("Person");
                    if("_0000".equals(object1.optString("status"))){
                        nickname = object.optString("name");
                        imag = object.optString("image");
                        name.setText(nickname);
                        signature.setText(object.optString("sign"));
                        ConfigUtils.level(level, object.optString("level"));
                        attention.setText(object.optString("con_count"));
                        fans.setText(object.optString("fan_count"));
                        session.setText(object.optString("part_total"));
                        odds.setText(object.optString("percent"));
                        quanxian_by_zhubo = object.optString("quanxian_by_zhubo");
                        quanxian_by_yonghu = object.optString("quanxian_by_yonghu");
                        topic = object.optString("topic");
                        if(type == 5){
                            if("0".equals(object.optString("stop_talk"))){
                                isBanned = false;
                                 banned.setText("禁言");
                            }else {
                                isBanned = true;
                                banned.setText("解禁");
                            }
                            Log.e("zhubo111",quanxian_by_yonghu);
                            if ("0".equals(quanxian_by_zhubo)){
                                tv_guanli.setText("授予管理");
                            }else {
                                tv_guanli.setText("取消管理");
                            }
                        }
                        if (type == 3){
                            if ("0".equals(object.optString("quanxian_by_yonghu"))){
                                tv_jinyan.setVisibility(View.GONE);
                            }else {
                                tv_jinyan.setVisibility(View.VISIBLE);
                                if ("1".equals(object.optString("stop_talk"))){
                                    tv_jinyan.setText("解禁");
                                    isJinyan = false;
                                }else {
                                    tv_jinyan.setText("禁言");
                                    isJinyan = true;
                                }
                            }
                        }

                        if("1".equals(object.optString("is_attention"))) {
                            if (type != 4) {
                                addattention.setText("已关注");
                                addattention.setBackgroundResource(R.drawable.retangle_gray);
                                isAddattention  = true;
                            }
                        }else {
                            if (type != 4) {
                                isAddattention  = false;
                                addattention.setText("+关注");
                                addattention.setBackgroundResource(R.drawable.retangle_main);
                            }
                        }
                        ImageLoader.getInstance().displayImage(Preference.photourl+"200x200/"+imag , head, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                bitmaps = bitmap;
                                applyBlur();

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 添加关注
     */
    private void addguanzhu() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(context,Preference.token,"",Preference.preference))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("userid",userid)
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.Addcon, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("添加关注", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            isAddattention = true;
                            if (type != 4) {
                                addattention.setText("已关注");
                                addattention.setBackgroundResource(R.drawable.retangle_gray);
                            }
                        } else {
                            Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    /**
     * 取消关注
     */
    private void delguanzhu() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", (String) SPUtils.get(context,Preference.token,"",Preference.preference))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .add("userid", userid)
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.Delcon, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("取消关注", json);
                        Gson gson = new Gson();
                        BaseBean Bean = gson.fromJson(json, BaseBean.class);
                        if (Bean.status.equals("_0000")) {
                            isAddattention  = false;
                            if (type != 4) {
                                addattention.setText("+关注");
                                addattention.setBackgroundResource(R.drawable.retangle_main);
                            }
                        } else {
                            Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //提交打赏的金额
    public void reward(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone",Preference.phone);
        builder.add("version", Preference.version);
        builder.add("anchor_id", Preference.zhubo_id);
        builder.add("room_id", Preference.room_id);
        builder.add("money", "50");
        //builder.add("token", "815cd26a-b394-4f1d-b7cc-ffe6caeb52d7");
        builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils((Activity)context);
        requestUtils.httpPost(Preference.reward, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.optString("status"))) {
                    } else {
                        Toast.makeText(context, object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    /**
     * 授予出题权限
     */
    public void qusetionPermission(){
        AdminPermissionDialog dialog = new AdminPermissionDialog((Activity)context,Preference.room_id,userid,nickname);
        dialog.show();
        dismiss();
    }
    /**
     * 解除管理权限
     */
    public void removePermission(){
        RemovePermissionDialog removePermissionDialog = new RemovePermissionDialog((Activity)context,Preference.room_id,userid,nickname);
        removePermissionDialog.show();
        dismiss();
    }
    /**
     * 授予出题权限
     */
    public void sendQuestionPermission(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone",Preference.phone)
                .add("version", Preference.version)
                .add("token",sp.getString(Preference.token,""))
                .add("room_id",Preference.room_id)
                .add("auth_user_id",userid);
        Log.e("121212",Preference.room_id+" "+userid);
        new HttpRequestUtils((Activity)context).httpPost(Preference.addquestionpermission, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                dismiss();
            }

            @Override
            public void onSuccess(String json) {
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.getString("status"))){
                        Toast.makeText(context,"授予出题权限成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context,object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.toString();
                }
                dismiss();
            }
        });
    }

}
