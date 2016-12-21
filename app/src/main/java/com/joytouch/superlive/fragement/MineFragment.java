package com.joytouch.superlive.fragement;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.AttentionAndFansActivity;
import com.joytouch.superlive.activity.ChargeActivity;
import com.joytouch.superlive.activity.LotteryActivity;
import com.joytouch.superlive.activity.MainActivity;
import com.joytouch.superlive.activity.MessageListActivity;
import com.joytouch.superlive.activity.MyGuessListActivity;
import com.joytouch.superlive.activity.SettingActivity;
import com.joytouch.superlive.activity.ShopActivity;
import com.joytouch.superlive.activity.StoreActivity;
import com.joytouch.superlive.activity.TaskActivity;
import com.joytouch.superlive.activity.UserInfoActivity;
import com.joytouch.superlive.activity.WeekAndMonthRankActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.redjavabean;
import com.joytouch.superlive.javabean.registInfo;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.FastBlur;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.LoginUtils;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.PullZoomView.PullToZoomScrollViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/7.
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RelativeLayout rl_attention;//关注布局
    private RelativeLayout rl_fans;//粉丝布局
    private ImageView iv_message;//消息
    private CircleImageView iv_icon;//用户头像 显示用户信息
    private RelativeLayout rl_guess,rl_rank,rl_charge,rl_duty,rl_lottery,rl_setting;
    private RelativeLayout rl_shop;
    private SharedPreferences sp;
    private TextView tv_attention_num;//关注
    private TextView tv_fans_num;//粉丝
    private TextView tv_sign;//签名
    private TextView anchor_fans;//等级
    private TextView tv_name;//昵称
    private TextView my_money;//金额
    private LinearLayout ll_level;
    private String level="";
    private View task_hongdian;
    private String asset="";
    private ImageView red_point;
    private ImageView medal1;
    private ImageView medal2;
    private ImageView medal3;
    private RelativeLayout rl_store;

    private TextView ballgold;
    private PullToZoomScrollViewEx scrollView;
    private ImageView iv_zoom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine_scroll,null);
        loadViewForCode();
        getdata();
        initView();
        /**
         * 通知(不包括离线消息)
         */
        MainActivity.setReceiveMessageListener(new MainActivity.shuaxinadapter() {
            @Override
            public void adapterMessage() {
                Log.e("mime是否执行listener","setReceiveMessageListener");
                red_point.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scroll_view=(PullToZoomScrollViewEx)view.findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.person_center_halftop, null, false);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.profile_zoom_view, null, false);
        iv_zoom=(ImageView)zoomView.findViewById(R.id.iv_zoom);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.person_center_halfdown, null, false);
        scroll_view.setHeaderView(headView);
        scroll_view.setZoomView(zoomView);
        scroll_view.setZoomEnabled(true);
        scroll_view.setScrollContentView(contentView);
    }

    //获取个人信息
    private void getdata() {
        FormBody.Builder build=new FormBody.Builder();
        build
                .add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(getActivity()).httpPost(Preference.Myinfo, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("个人中心", json);
                        Gson gson = new Gson();
                        registInfo Bean = gson.fromJson(json, registInfo.class);
                        if (Bean.status.equals("_0000")) {
                            Gson info = new Gson();
                            registInfo registinfo = info.fromJson(json, registInfo.class);
//                            new SaveOneInfoUtils(getActivity(), registinfo).save();
                            sp.edit().putString(Preference.level,registinfo.user_info.level).commit();
                            //填充我的数据
                            fullInfo(registinfo);
                        } else if (Bean.status.equals("_1000")) {
                            new LoginUtils(getActivity()).reLogin(getActivity());
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    String imgid;
    String imgurl;
    private void fullInfo(registInfo registinfo) {
        if (registinfo.medal_info.medal1.equals("0")){
            medal1.setVisibility(View.GONE);
        }else{
            medal1.setVisibility(View.VISIBLE);
        }
        if (registinfo.medal_info.medal2.equals("0")){
            medal2.setVisibility(View.GONE);
        }else{
            medal2.setVisibility(View.VISIBLE);
        }
        if (registinfo.medal_info.medal3.equals("0")){
            medal3.setVisibility(View.GONE);
        }else{
            medal3.setVisibility(View.VISIBLE);
        }
        tv_sign.setText(registinfo.user_info.sign);
        tv_fans_num.setText(registinfo.user_info.roster_to_num);
        tv_attention_num.setText(registinfo.user_info.roster_from_num);
        sp.edit().putString("tv_fans_num",registinfo.user_info.roster_to_num)
                 .putString("tv_attention_num", registinfo.user_info.roster_from_num)
                .putString(Preference.headPhoto,registinfo.user_info.image)
                .putString(Preference.level,registinfo.user_info.level)
                .putString(Preference.nickname,registinfo.user_info.nick_name)
        .commit();
        if (registinfo.user_info.level.equals("")){
            ll_level.setVisibility(View.GONE);
        }else{
            level=registinfo.user_info.level;
        }
        ConfigUtils.level(anchor_fans, registinfo.user_info.level);
        tv_name.setText(registinfo.user_info.nick_name);
        my_money.setText(registinfo.user_info.balance);
        ballgold.setText(registinfo.user_info.balance2);
        //设置默认头像
        imgid = registinfo.user_info.image;
        if (imgid.equals("")){
            imgid =Preference.img_url+"200x200/"+imgid;
        }
        imgurl = Preference.img_url+"200x200/"+imgid;
        ImageLoader.getInstance().displayImage(imgurl,
                iv_icon, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                        applyBlur(bitmap);
                        iv_zoom.setImageBitmap(ConfigUtils.fastblur(getActivity(), bitmap, 20));
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("红点通知",sp.getInt("hongdian", 0)+"");
        if (sp.getInt("hongdian", 0)>0){
            red_point.setVisibility(View.VISIBLE);
        }else{
            red_point.setVisibility(View.GONE);
        }
        my_money.setText(sp.getString(Preference.balance, ""));
        tv_sign.setText(sp.getString(Preference.sign, ""));
        tv_name.setText(sp.getString(Preference.nickname,""));
        if (sp.getString(Preference.level,"").equals("")){
            ll_level.setVisibility(View.GONE);
        }else{
            ll_level.setVisibility(View.VISIBLE);
//            anchor_fans.setText(sp.getString(Preference.level, ""));
        }
        imgid = sp.getString(Preference.headPhoto,"");
        if (imgid.equals("")){
//            imgid =Preference.baseheadphoto;
        }
//        ImageLoader.getInstance().displayImage(otherimageurl,holder.iv_friend_icon, ImageLoaderOption.optionsHeader);
        imgurl = Preference.img_url+"200x200/"+imgid;
        ImageLoader.getInstance().displayImage(imgurl,
                iv_icon, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                        applyBlur(bitmap);
                        iv_zoom.setImageBitmap(ConfigUtils.fastblur(getActivity(), bitmap, 20));
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
        getdata();
        //红点
        getisHongdian();
    }

    private void getisHongdian() {
        FormBody.Builder build=new FormBody.Builder();
        build.add("token", sp.getString(Preference.token,""))
                .add("phone", Preference.phone)
                .add("version", Preference.version)
                .build();
        new HttpRequestUtils(getActivity()).httpPost(Preference.redpoint, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {

                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("红点", json);
                        Gson gson = new Gson();
                        redjavabean Bean = gson.fromJson(json, redjavabean.class);
                        if (Bean.status.equals("_0000")) {
                            if (Bean.RedPoint.mission.equals("1")) {
                                task_hongdian.setVisibility(View.VISIBLE);

                            } else {
                                task_hongdian.setVisibility(View.GONE);
                            }
                            asset = Bean.RedPoint.asset;
                        } else {
//                            Toast.makeText(getActivity(), Bean.message, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void initView() {
//        scrollView = (PullToZoomScrollViewEx)view .findViewById(R.id.scroll_view);
//        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
//        int mScreenHeight = localDisplayMetrics.heightPixels;
//        int mScreenWidth = localDisplayMetrics.widthPixels;
//        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
//        scrollView.setHeaderLayoutParams(localObject);

        ballgold = (TextView) view.findViewById(R.id.ballgold);
        rl_store=(RelativeLayout)view.findViewById(R.id.rl_store);
        medal1=(ImageView)view.findViewById(R.id.medal1);
        medal2=(ImageView)view.findViewById(R.id.medal2);
        medal3=(ImageView)view.findViewById(R.id.medal3);
        red_point=(ImageView)view.findViewById(R.id.red_point);
        task_hongdian=view.findViewById(R.id.task_hongdian);
        ll_level=(LinearLayout)view.findViewById(R.id.ll_level);
        my_money=(TextView)view.findViewById(R.id.my_money);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        anchor_fans=(TextView)view.findViewById(R.id.anchor_fans);
        tv_sign=(TextView)view.findViewById(R.id.tv_sign);
        tv_fans_num=(TextView)view.findViewById(R.id.tv_fans_num);
        tv_attention_num=(TextView)view.findViewById(R.id.tv_attention_num);
        rl_attention = (RelativeLayout) view.findViewById(R.id.rl_attention);
        rl_fans = (RelativeLayout) view.findViewById(R.id.rl_fans);
        iv_message = (ImageView) view.findViewById(R.id.iv_message);
        iv_icon = (CircleImageView) view.findViewById(R.id.iv_icon);
        rl_guess = (RelativeLayout) view.findViewById(R.id.rl_guess);
        rl_rank = (RelativeLayout) view.findViewById(R.id.rl_rank);
        rl_charge = (RelativeLayout) view.findViewById(R.id.rl_charge);
        rl_duty = (RelativeLayout) view.findViewById(R.id.rl_duty);
        rl_lottery = (RelativeLayout) view.findViewById(R.id.rl_lottery);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        rl_shop=(RelativeLayout)view.findViewById(R.id.rl_shop);
        rl_shop.setOnClickListener(this);
        rl_guess.setOnClickListener(this);
        rl_rank.setOnClickListener(this);
        rl_charge.setOnClickListener(this);
        rl_duty.setOnClickListener(this);
        rl_lottery.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        iv_icon.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        rl_attention.setOnClickListener(this);
        rl_fans.setOnClickListener(this);
        rl_store.setOnClickListener(this);
//        ImageUtils.imageLoader.displayImage("http://s.cjzhibo.net/Uploads/SuperBid/201603/be85481d345381ba743d35b67fb8d94c.png", iv_icon,
//                ImageUtils.v3_Options);
        if (sp.getInt("hongdian", 0)>0){
            red_point.setVisibility(View.VISIBLE);
        }else{
            red_point.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_store:
                Intent intent_ = new Intent(getActivity(), StoreActivity.class);
                startActivity(intent_);
                break;

            case R.id.rl_attention:
                Intent intent = new Intent(getActivity(), AttentionAndFansActivity.class);
                intent.putExtra("type","attention");
                startActivity(intent);
                break;
            case R.id.rl_fans:
                Intent intent1 = new Intent(getActivity(), AttentionAndFansActivity.class);
                intent1.putExtra("type","fans");
                startActivity(intent1);
                break;
            case R.id.iv_message:
//                Intent intent2 = new Intent(getActivity(), WebViewActivity.class);
                Intent intent2 = new Intent(getActivity(), MessageListActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_rank:
                Intent intent3 = new Intent(getActivity(), WeekAndMonthRankActivity.class);
                intent3.putExtra("ok","0");
                startActivity(intent3);
                break;
            case R.id.rl_setting:
                Intent intent4 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent4);
                break;
            case R.id.iv_icon:
                Intent intent5 = new Intent(getActivity(),UserInfoActivity.class);
                startActivity(intent5);
                break;
            case R.id.rl_charge:
                Intent intent6 = new Intent(getActivity(), ChargeActivity.class);
                startActivity(intent6);
                break;
            case R.id.rl_guess:
                Intent intent7 = new Intent(getActivity(), MyGuessListActivity.class);
                startActivity(intent7);
                break;
            case R.id.rl_duty:
                Intent intent8 = new Intent(getActivity(), TaskActivity.class);
                startActivity(intent8);
                break;
            case R.id.rl_lottery:
                Intent intent9 = new Intent(getActivity(), LotteryActivity.class);
                startActivity(intent9);
                break;

            case R.id.rl_shop:
                Intent intent10 = new Intent(getActivity(), ShopActivity.class);
                intent10.putExtra("asset",asset);
                startActivity(intent10);
                break;
        }
    }
    private void applyBlur(final Bitmap bm) {
        if (bm == null){
            return;
        }
        iv_icon.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                iv_icon.getViewTreeObserver().removeOnPreDrawListener(this);
                iv_icon.buildDrawingCache();
                float w = (float) (iv_zoom.getWidth() * 1.0 / bm.getWidth());
                float h = (float) (iv_zoom.getHeight() * 1.0 / bm.getHeight());
                Matrix matrix = new Matrix();
                matrix.postScale(w, h); //长和宽放大缩小的比例
                Bitmap bmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                blur(bmp, iv_zoom);
                return true;
            }
        });
    }
    //模糊处理方法
    private void blur(Bitmap bkg, View view) {
        float radius = 8;
        float scaleFactor = 8;
        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor), (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getActivity().getResources(), overlay));
    }

}
