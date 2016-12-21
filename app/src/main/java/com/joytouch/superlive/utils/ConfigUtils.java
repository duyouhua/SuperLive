package com.joytouch.superlive.utils;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ChargeActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.joytouch.superlive.javabean.SendLotteryResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/12.
 * 公用的方法
 */
public class ConfigUtils {
    private static List<Activity> list = new ArrayList<>();
    private static final String TAG = "Blur";
    public static final String[] strings = {"，今天我们不扯淡，我们就在超级直播看球。","，超级直播大战正在进行时！速来文明观球！","，超级直播快到不行，爽快的比赛你怎能错过？"};

    public static void addActivity(Activity activity){
        list.add(activity);
    }
    public static void  removeActivtity(Activity a){
        list.remove(a);
    }
    public static  void exit(){
        for (int i = 0;i< list.size();i++){
            ((Activity)list.get(i)).finish();
        }
    }

    /**
     * 随机生成漂亮的颜色
     */
    public static int generateBeautifulColor() {
        Random random = new Random();
        //为了让生成的颜色不至于太黑或者太白，所以对3个颜色的值进行限定
        int red = random.nextInt(150) + 50;//50-200
        int green = random.nextInt(150) + 50;//50-200
        int blue = random.nextInt(150) + 50;//50-200
        return Color.rgb(red, green, blue);//使用r,g,b混合生成一种新的颜色
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    //添加投注动画
    public static void flash(Context context,final RelativeLayout container,String money) {
        final TextView iv = new TextView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        iv.setLayoutParams(params);
        //iv.setImageBitmap(bitmap);
        iv.setText("+"+ money);
        iv.setTextColor(context.getResources().getColor(R.color.color_yellow));
        iv.setBackgroundColor(context.getResources().getColor(R.color.transparent_background));
        iv.setTextSize(15);
        container.addView(iv);
        //创建属性动画组合
        AnimatorSet set = new AnimatorSet();
        // 把平移动画对象作用到按钮上
        ObjectAnimator ta = ObjectAnimator.ofFloat(iv, "translationX", 0,
                0, 0, 0, 0, 0, 0);
        ObjectAnimator ta1 = ObjectAnimator.ofFloat(iv, "translationY", 0,
                -20, -30, -50, -70, -90, -120,-140,-170,-190);
        ta.setDuration(800);
//        ta.setRepeatCount(1);
        ta.setRepeatMode(ObjectAnimator.REVERSE);
        ta1.setDuration(800);
//        ta1.setRepeatCount(1);
        ta1.setRepeatMode(ObjectAnimator.REVERSE);
        // 把透明度动画对象作用到按钮上
        ObjectAnimator aa = ObjectAnimator.ofFloat(iv, "alpha", 1f, 0.8f,
                0.6f, 0.4f, 0.2f, 0f);
        aa.setDuration(800);
//        aa.setRepeatCount(1);
        aa.setRepeatMode(ObjectAnimator.REVERSE);
        //播放动画组合
        set.playTogether(ta, aa, ta1);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                container.removeView(iv);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
    public static void sendLotteryResult(final Activity context,String roomid,String queid, final String option,String money, final ResultListener listener){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("room_id", roomid);
        builder.add("option", option);
        builder.add("que_id", queid);
        builder.add("money", money);
        builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference));
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils(context);
        httpRequestUtils.httpPost(Preference.JoinLottery, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {

            }

            @Override
            public void onSuccess(String json) {
                Log.e("参与竞猜",json);
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.optString("status"))) {
                        JSONArray array = object.getJSONArray("total_money");
                        SendLotteryResult result = new SendLotteryResult();

                        if (array != null) {
                            if (array.length() > 2) {
                                result.setLeftgold(array.optString(0));
                                result.setRightgold(array.optString(2));
                                result.setCentergold(array.optString(1));
                            } else {
                                result.setLeftgold(array.optString(0));
                                result.setRightgold(array.optString(1));
                            }
                        }
                        result.setBetting(object.optString("my_option_money"));
                        result.setEarnings(object.optString("predict_money"));

                        if (listener != null) {
                            listener.result(result);
                        }
                    } else {

                        if ("_2001".equals(object.optString("status"))&&!isForeground(context,"com.joytouch.superlive.activity.ChargeActivity")) {
                            LogUtils.e("*&^%$$#@@",""+isForeground(context,"com.joytouch.superlive.activity.ChargeActivity"));
                            Intent intent = new Intent(context, ChargeActivity.class);
                            context.startActivity(intent);
                        }
                        if ("_1000".equals(object.optString("status"))) {
                            new LoginUtils(context).reLogin(context);
                        }
                        if ("_0003".equals(object.optString("status"))) {
                            Gson gson = new Gson();
                            BaseBean bean = gson.fromJson(json, BaseBean.class);
                            if (listener != null) {
                                listener.last(bean.last);
                            }
                        }
                        Toast.makeText(context, object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    public interface ResultListener{
        void result(SendLotteryResult result);
        void last(String islast);
    }
    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className
     *            某个界面名称
     */
    private static boolean isForeground(Context context, String className) {
        boolean result = false;
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(2).get(0).topActivity;
        if (cn != null) {
            if (className.equals(cn.getClassName())) {
                result = true;
            }
        }
        return result;
    }
    public static void level(View view,String level){
        if(level == null||level.equals("")){
            return;
        }

        int[] logo = new int[]{R.drawable.level1,R.drawable.level2,R.drawable.level3
        ,R.drawable.level4,R.drawable.level5,R.drawable.level6
        ,R.drawable.level7,R.drawable.level8,R.drawable.level9,
                R.drawable.level10,R.drawable.level11,R.drawable.level12,R.drawable.level13
                ,R.drawable.level14,R.drawable.level15,R.drawable.level16
                ,R.drawable.level17,R.drawable.level18,R.drawable.level19,
                R.drawable.level20,R.drawable.level21,R.drawable.level22,R.drawable.level23
                ,R.drawable.level24,R.drawable.level25,R.drawable.level26
                ,R.drawable.level27,R.drawable.level28,R.drawable.level29,
                R.drawable.level30,R.drawable.level31,R.drawable.level32,R.drawable.level33
                ,R.drawable.level34,R.drawable.level35,R.drawable.level36
                ,R.drawable.level37,R.drawable.level38,R.drawable.level39,
                R.drawable.level40,R.drawable.level41,R.drawable.level42,R.drawable.level43
                ,R.drawable.level44,R.drawable.level45,R.drawable.level46
                ,R.drawable.level47,R.drawable.level48,R.drawable.level49,
                R.drawable.level50,R.drawable.level51,R.drawable.level52,R.drawable.level53
                ,R.drawable.level54,R.drawable.level55,R.drawable.level56
                ,R.drawable.level57,R.drawable.level58,R.drawable.level59,
                R.drawable.level60,R.drawable.level61,R.drawable.level62,R.drawable.level63
                ,R.drawable.level64,R.drawable.level65,R.drawable.level66
                ,R.drawable.level67,R.drawable.level68,R.drawable.level69,
                R.drawable.level70,R.drawable.level71,R.drawable.level72,R.drawable.level73
                ,R.drawable.level74,R.drawable.level75,R.drawable.level76
                ,R.drawable.level77,R.drawable.level78,R.drawable.level79,
                R.drawable.level80,R.drawable.level81,R.drawable.level82,R.drawable.level83
                ,R.drawable.level84,R.drawable.level85,R.drawable.level86
                ,R.drawable.level87,R.drawable.level88,R.drawable.level89,
                R.drawable.level90,R.drawable.level91,R.drawable.level92,R.drawable.level93
                ,R.drawable.level94,R.drawable.level95,R.drawable.level96
                ,R.drawable.level97,R.drawable.level98,R.drawable.level99};


        if (Integer.parseInt(level)>99){
            view.setBackgroundResource(logo[98]);
        }else{
            view.setBackgroundResource(logo[Integer.parseInt(level)-1]);
        }
    }

    /**
     * @param view 控件
     * @param level 等级
     * @param order 0代表选择左边,1代表选择右边,2代表没有选择
     */
    public static void fullscrLottery(View view,String level,int order){
        int[] bac = new int[]{R.drawable.fulllottery_normal_left,R.drawable.fulllottery_yellow_left,R.drawable.fulllottery_normal_right
                ,R.drawable.fulllottery_yellow_right,R.drawable.fulllottery_red_nopress_left,R.drawable.fulllottery_red_left
                ,R.drawable.fulllottery_red_nopress_right,R.drawable.fulllottery_red_right,R.drawable.fulllottery_lightyellow_left,
                R.drawable.fulllottery_lightyellow_press_left, R.drawable.fulllottery_lightyellow_right, R.drawable.fulllottery_lightyellow_press_right};
        if(level == null||level.equals("")){
            return;
        }
        //如果等级是1
        if (level.equals("1")){
            if (order==0){//如果选择了左边
                view.setBackgroundResource(bac[1]);
            }else if (order==1){//如果选择了右边
                view.setBackgroundResource(bac[3]);
            }

        }else if (level.equals("2")){
            if (order==0){//如果选择了左边
                view.setBackgroundResource(bac[5]);
            }else if (order==1){//如果选择了右边
                view.setBackgroundResource(bac[7]);
            }
        }else if (level.equals("3")){
            if (order==0){//如果选择了左边
                view.setBackgroundResource(bac[9]);
            }else if (order==1){//如果选择了右边
                view.setBackgroundResource(bac[11]);
            }
        }
    }
    /**
     * 验证json合法性
     *
     * @param jsonContent
     * @return
     */
    public static boolean isJsonFormat(String jsonContent) {
        try {
            new JsonParser().parse(jsonContent);
            return true;
        } catch (JsonParseException e) {
            Log.e("bad json: ",jsonContent);
            return false;
        }
    }
    /**
     * 分享文案
     * 随机返回
     */
    public static String getShareContent(){
        return strings[(int)(Math.random()*3)];
    }


    /**
     * 改变标题栏颜色
     * @param activity
     */
//    public static void chagevegive(Activity activity){
//        /**
//         代码中加个判断
//         if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
//         //大于4.4.2
//         }else{
//         //小于等于4.4.2
//         }
//         Build.VERSION.SDK_INT（本机API）
//         Build.VERSION_CODES.KITKAT(4.4API(19))
//         */
//        if (Build.VERSION.SDK_INT <20 && Build.VERSION.SDK_INT>=19 ) {
//            //TODO:如果当前版本小于HONEYCOMB版本，即api19版本
//            changeNegive_4(activity);
//        }else{
//            changeNegive_5(activity);
//        }
//    }

//    private static  void changeNegive_4(Activity activity) {
//        Window window = activity.getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
//        ViewGroup mContentParent = (ViewGroup) mContentView.getParent();
//
//        View statusBarView = mContentParent.getChildAt(0);
//        if (statusBarView != null && statusBarView.getLayoutParams() != null && statusBarView.getLayoutParams().height == getStatusBarHeight(activity)) {
//            //避免重复调用时多次添加 View
//            statusBarView.setBackgroundColor(activity.getResources().getColor(R.color.main));
//            return;
//        }
//
////创建一个假的 View, 并添加到 ContentParent
//        statusBarView = new View(activity);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                getStatusBarHeight(activity));
//        statusBarView.setBackgroundColor(activity.getResources().getColor(R.color.main));
//        mContentParent.addView(statusBarView, 0, lp);
//
////ChildView 不需要预留系统空间
//        View mChildView = mContentView.getChildAt(0);
//        if (mChildView != null) {
//            ViewCompat.setFitsSystemWindows(mChildView, false);
//        }
//    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;

    }

//    /**
//     * 改变手机顶部颜色  5.0上
//     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private static  void changeNegive_5(Activity activity) {
//        Window window =activity.getWindow();
////取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////设置状态栏颜色
//        window.setStatusBarColor(activity.getResources().getColor(R.color.main));
//        ViewGroup mContentView = (ViewGroup)activity.findViewById(Window.ID_ANDROID_CONTENT);
//        View mChildView = mContentView.getChildAt(0);
//        if (mChildView != null) {
//            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
//            ViewCompat.setFitsSystemWindows(mChildView, true);
//        }
//    }


    @SuppressLint("NewApi")
    public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs,
                    sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs,
                    input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
                    Element.U8_4(rs));
            script.setRadius(radius /* e.g. 3.f */);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

}
