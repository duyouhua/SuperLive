package com.joytouch.superlive.utils;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joytouch.superlive.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yj on 2016/4/22.
 * 使用方式:
     FormBody.Builder build=new FormBody.Builder();
     build.add("token", "AFAF2806-5AE1-3D90-4C18-0050AF2DE2D")
     .add("phone", "1")
     .add("content", "helougewei")
     .add("version", "4")
     .add("zb_id", "41622")
     .add("show_type", "chat")
     .build();
      new HttpRequestUtils(ChargeActivity.this).httpPost("http://www.qiuwin.com/I/v1.php/SuperLive4/ChatClient.json", build,
      new HttpRequestUtils.ResRultListener() {
        @Override
        public void onFailure(IOException e) {}
        @Override
        public void onSuccess(String json) {
        Gson gson = new Gson();
        BaseBean  bean = gson.fromJson(json, BaseBean.class);
        Log.e("测试",bean.message);}
        });
 * okhttp请求封装累
 */
public class HttpRequestUtils {
    private OkHttpClient httpClient;
    private Call call;
    private Activity activity;
    private long timeout = 10;
    private LinearLayout loadingfailed;
    private LinearLayout refreshll;
    private ImageView refresh;
    private View view;
    private boolean isloading = false;
    private String type;
    public HttpRequestUtils(Activity activity,View view,boolean isup) {
        //创建okhttp对象
        httpClient = new OkHttpClient();
        httpClient.newBuilder().connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout,TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS);
        this.view = view;
        this.activity = activity;
        if(!isup) {
            loadingfailed = (LinearLayout) view.findViewById(R.id.loading_failed);
            refreshll = (LinearLayout) view.findViewById(R.id.refreshll);
            refresh = (ImageView) view.findViewById(R.id.refresh_logo);
            isloading = true;
        }else{
            isloading = false;
        }


    }
    public HttpRequestUtils(Activity activity) {
        //创建okhttp对象
        httpClient = new OkHttpClient();
        httpClient.newBuilder().connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS);
        this.activity = activity;
        isloading = false;
    }
    //取消请求
    private void cancel(){
        if(call!= null){
            call.cancel();
        }
    }
    //get请求
    public void httpGet(String url, final ResRultListener resRultListener){
        //创建一个请求
        final Request request = new Request.Builder().url(url).build();
        //创建一个呼叫请求
        call = httpClient.newCall(request);
        //创建一个异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(resRultListener !=null){
                    resRultListener.onFailure(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resRultListener != null) {
                            resRultListener.onSuccess(json);
                        }
                    }
                });


            }
        });
    }
    //post的请求
    public void httpPost(final String url , final FormBody.Builder builder,final ResRultListener resRultListener){

        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url).post(body).build();
        String bodycontent = "";
        int length = builder.build().size();
        if(LogUtils.isOpen()) {
            for (int i = 0; i < length; i++) {
                bodycontent = bodycontent + builder.build().encodedName(i) + "=" + builder.build().encodedValue(i)+"&";
            }
        }
        LogUtils.e("builder", bodycontent);
        if(isloading) {
            loading();
        }
        //创建一个呼叫请求
        call = httpClient.newCall(request);
        //创建一个异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resRultListener !=null){

                            resRultListener.onFailure(e);
                            if(isloading) {
                                loadingFailed(url,builder,resRultListener);
                            }
//                            LogUtils.e("exception", e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                LogUtils.e("json",json);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resRultListener != null) {
                            if(isloading) {
                                loadinSuccess();
                            }
                            resRultListener.onSuccess(json);
                        }
                    }
                });


            }
        });

    }

    //post的请求上传单张图片
    public void httpPostImage(String url ,String filekey,String filePath,String imageName,final ResRultListener resRultListener){
        File file = new File(filePath);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart(filekey, imageName, RequestBody.create(MediaType.parse("image/png"), file));
        Request request = new Request.Builder()
                .url(url).post(builder.build()).build();
        //创建一个呼叫请求
        call = httpClient.newCall(request);
        //创建一个异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resRultListener != null) {
                            resRultListener.onFailure(e);

                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resRultListener != null) {

                            resRultListener.onSuccess(json);
                        }
                    }
                });


            }
        });

    }
//请求结果回调
public interface ResRultListener{
    void onFailure(IOException e);
   void onSuccess(String json);
}
    public  void loading(){
        if(this.view == null){
            LogUtils.e("sssssssssssss","///////////");
        }
        view.setVisibility(View.VISIBLE);
        loadingfailed.setVisibility(View.GONE);
        refreshll.setVisibility(View.VISIBLE);
        RotateAnimation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        an.setInterpolator(new LinearInterpolator());//不停顿
        an.setRepeatCount(-1);//重复次数
        an.setFillAfter(true);//停在最后
        an.setDuration(600);
        refresh.startAnimation(an);

    }
    public void loadingFailed(final String url , final FormBody.Builder builder,final ResRultListener resRultListener){
        loadingfailed.setVisibility(View.VISIBLE);
        refreshll.setVisibility(View.GONE);
        refresh.clearAnimation();
        loadingfailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e("loadingfailed", url);
                httpPost(url, builder, resRultListener);
            }
        });

    }
    public void loadinSuccess(){
        refresh.clearAnimation();
        view.setVisibility(View.GONE);
        loadingfailed.setVisibility(View.GONE);
    }

}
