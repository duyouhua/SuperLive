package com.joytouch.superlive.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.BaseBean;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;

/**
 * Created by yj on 2016/4/26.
 * 分享工具类
 */
public class ShareUtils {
    final UMSocialService umShare = UMServiceFactory
            .getUMSocialService("com.umeng.share");
    private SharedPreferences sp;

    public void QzoneShare(final Activity context,String content,String title,String url,String imageUrl) {
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
                Preference.qq_appid, Preference.qq_appkey);
        qZoneSsoHandler.addToSocialSDK();

        QZoneShareContent qzone = new QZoneShareContent();

            // 设置分享文字
            qzone.setShareContent(content);
            // 设置点击消息的跳转URL
            qzone.setTargetUrl(url);
            // 设置分享内容的标题
            qzone.setTitle(title);
            // 设置分享图片

            if (!"".equals(imageUrl)) {
                qzone.setShareImage(new UMImage(context, imageUrl));
            } else {
                qzone.setShareImage(new UMImage(context, R.mipmap.logo));
            }

      qqShareReult(context);
        umShare.setShareMedia(qzone);
            umShare.postShare(context, SHARE_MEDIA.QZONE, null);


    }
    public void qqShare(final Activity context,String content,String title,String url,String imageUrl){
        qqShareReult(context);
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context,
                Preference.qq_appid, Preference.qq_appkey);
        qqSsoHandler.addToSocialSDK();


        QQShareContent qqShareContent = new QQShareContent();
        // 设置分享文字
        qqShareContent.setShareContent(content);
        // 设置分享title
        qqShareContent.setTitle(title);
        qqShareContent.setTargetUrl(url);

        // 设置分享图片
        if (!"".equals(imageUrl)) {
            qqShareContent.setShareImage(new UMImage(context, imageUrl));
        } else {
            qqShareContent.setShareImage(new UMImage(context, R.mipmap.logo));
        }
        // 设置点击分享内容的跳转链接
        umShare.setShareMedia(qqShareContent);
//        umShare.openShare(context, true);
        umShare.postShare(context, SHARE_MEDIA.QQ, null);
    }
    public void wxFriend(final Activity context,int flag,String content,String title,String url,String imageUrl){
        IWXAPI wxApi;
        // 实例化
        wxApi = WXAPIFactory.createWXAPI(context, Preference.wx_appid);
        wxApi.registerApp(Preference.wx_appid);
        //1
        WXWebpageObject webpage = new WXWebpageObject();
        // webpage.webpageUrl = "www.baidu.com";
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        // 分享内容.url替代
        msg.description = content;
        // 这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.logo);
        msg.setThumbImage(thumb);

        // // 用WXTextObject对象初始化一个WXMediaMessage对象
        // WXMediaMessage tmsg = new WXMediaMessage();
        // tmsg.mediaObject = textObj;
        // // 发送文本类型的消息时，title字段不起作用
        // tmsg.title = "Will be ignored";
        // tmsg.description = "description";

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis()); //
        // transaction字段用于唯一标识一个请求
        req.message = msg;

        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession
                : SendMessageToWX.Req.WXSceneTimeline;


        // 发送数据
        boolean b = wxApi.sendReq(req);
        if (b == false) {
            Toast.makeText(context, "请安装微信软件", 1000).show();
        }
    }

    /**
     * wechatShare(0);分享到微信好友 wechatShare(1);分享到微信朋友圈
     */

    public void wxShare(final Activity context,String content,String title,String url,String imageUrl) {

        UMWXHandler wxCircleHandler = new UMWXHandler(context,
                Preference.wx_appid, Preference.wx_appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        CircleShareContent circleShareContent = new CircleShareContent();
        circleShareContent.setTitle(title);
        circleShareContent.setShareContent(content);
        circleShareContent.setTargetUrl(url);
        umShare.setShareMedia(circleShareContent);
        umShare.openShare(context, false);
    }
    private void qqShareReult(final Activity context){

        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        umShare.getConfig().closeToast();
        umShare.registerListener(new SocializeListeners.SnsPostListener() {


            @Override
            public void onStart() {
//                Toast.makeText(context, "qq邀请..", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode,
                                   SocializeEntity entity) {
                if (eCode == 200) {
                    Toast.makeText(context, "qq邀请成功", Toast.LENGTH_SHORT).show();
                    //执行分享任务接口
                    FormBody.Builder build=new FormBody.Builder();
                    build.add("token", sp.getString(Preference.token,""))
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .build();
                    new HttpRequestUtils(context).httpPost(Preference.share, build,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess(String json) {
                                    Log.e("任务分享", json);
                                    Gson gson = new Gson();
                                    BaseBean Bean = gson.fromJson(json, BaseBean.class);
                                    if (Bean.status.equals("_0000")) {
                                        if (Bean.is_share.equals("0")){
                                                final Dialog dialog1 =  new Dialog(context, R.style.Dialog_bocop);
                                                dialog1.setContentView(R.layout.my_task_1);
                                            LinearLayout close = (LinearLayout) dialog1.findViewById(R.id.close);
                                                close.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog1.dismiss();
                                                    }
                                                });
                                                TextView getMoney = (TextView) dialog1.findViewById(R.id.getMoney);
                                                getMoney.setText("+"+Bean.getMoney());
                                                dialog1.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
                                                dialog1.setCancelable(false);
                                                dialog1.show();
                                        }
                                    } else {
                                        Toast.makeText(context, Bean.message, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }

        });


    }
    public void shareToQQzone(Activity context,String content,String title,String url,String imageUrl) {

        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
                QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);// 必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);// 选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);// 必填

        ArrayList<String> imageUrls = new ArrayList<String>();
        if (imageUrl.equals("")) {
            imageUrls.add("");
        } else {
            imageUrls.add(imageUrl);
        }

        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        Tencent mTencent = Tencent.createInstance(Preference.qq_appkey,
                context);
        mTencent.shareToQzone(context, params, new BaseUiListener(context));
    }
    private class BaseUiListener implements IUiListener {
        private Activity context;

        public BaseUiListener(Activity context) {
            this.context = context;
        }

        @Override
        public void onComplete(Object response) {
            // V2.0版本，参数类型由JSONObject 改成了Object,具体类型参考api文档
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {
            // 分享成功
            Log.e("分享成功报告","qq");
        }

        @Override
        public void onError(UiError e) {
            // 在这里处理错误信息


        }

        @Override
        public void onCancel() {
            // 分享被取消

        }
    }
}
