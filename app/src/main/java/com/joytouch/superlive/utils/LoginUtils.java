package com.joytouch.superlive.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.joytouch.superlive.activity.LoadActivity;
import com.joytouch.superlive.activity.MainActivity;
import com.joytouch.superlive.activity.bindPhoneActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.app.SuperLiveApplication;
import com.joytouch.superlive.javabean.registInfo;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.tencent.weibo.sdk.android.component.sso.tools.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/4/25.
 */
public class LoginUtils {
    private  SharedPreferences sp;
    private  Context context;
    private Tencent mTencent;
    private String nickname;
    private String image_url;
    private UserInfo mInfo;
    private String unionids;
    private String wx_openid;
    public static String wxTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public LoginUtils(Context context) {
        this.context=context;
        if (context!=null){
            sp = context.getSharedPreferences(Preference.preference,
                    Context.MODE_PRIVATE);
        }
    }
//--------------------------------------------------------------------------------------------------------------------------

public void wxLogin() {
//        dialog = new V4_LoadingDaliog(context);
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
    SendAuth.Req req = new SendAuth.Req();
    req.scope = "snsapi_userinfo";
    req.state = "superGuess";
    SuperLiveApplication.wxapi.sendReq(req);
}

    /**
     * 重新登录
     * @param context
     */
    public void reLogin(Context context) {
        sp.edit()
                .putString(Preference.token,"")
                .putString(Preference.nickname,"")
                .putString(Preference.headPhoto,"")
                .putString(Preference.name159,"")
                .putString(Preference.pwd159,"")
                .putString(Preference.myuser_id, "")
                .putString(Preference.passwd, "")
                .putString(Preference.username,"")
                .putString(Preference.sign,"")
                .putString(Preference.level, "")
                .putString(Preference.balance, "")
                .commit();
        //退出登录时:  判断是否是登陆界面出去,  签到保存本地的时间去掉
        sp.edit().putString("settexit","1")
                .putString("register_time","0")
                .commit();

        Intent intent_ = new Intent(context, LoadActivity.class);
        intent_.putExtra("isstar","0");
        context.startActivity(intent_);
    }


    public class Wxtoken extends AsyncTask<Void, Void, String> {
        //        public ProgressDialogUtil progressDialogUtil;
        Context context;
        String appid = "";
        String code = "";
        String secret = "";
        StringBuffer sb;


        public Wxtoken(Context context, String appid, String secret, String code) {
            // TODO Auto-generated constructor stub
            this.appid = appid;
            this.secret = secret;
            this.code = code;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
//            progressDialogUtil = new ProgressDialogUtil(context);
//            progressDialogUtil.showProgressDialog("请稍候...");
            sb = new StringBuffer();
            sb.append(wxTokenUrl + "?");
            WXhelpUtils.wxMyCode = "";
        }

        @Override
        protected String doInBackground(Void... params) {

            sb.append("appid=").append(appid).append("&secret=").append(secret)
                    .append("&code=").append(code)
                    .append("&grant_type=authorization_code");

            HttpGet get = new HttpGet(sb.toString());
            try {
                HttpResponse response = new DefaultHttpClient().execute(get);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    return null;
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                Toast.makeText(context, "获取数据失败", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String unionid = parser(result);
                    if (!unionid.equals("")) {
                        unionids = unionid;
                        Log.e("微信登录",unionids+":::"+unionid);
                        getWXUserInfo(unionid);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private void getWXUserInfo(final String openid) {
            new WxUserInfoTask().execute();
        }

        public class WxUserInfoTask extends AsyncTask<Void, Void, String> {




            @Override
            protected void onPreExecute() {
            }

            @Override
            protected String doInBackground(Void... params) {

                String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                        + WXhelpUtils.wxToken + "&openid="
                        + WXhelpUtils.wxOpenId;

                return new WXhttputils().getRespString(url);

            }

            @Override
            protected void onPostExecute(String result) {
//            progressDialogUtil.dismissProgressDialog();
                Log.e("微信登录json222",result);
                try {
                    result = new String(result.getBytes("iso-8859-1"), "UTF-8");
                    JSONObject json = new JSONObject(result);
                    Log.e("微信登录json",result);
                    wx_openid=json.getString("openid");
                    nickname = json.getString("nickname");
                    image_url = json.getString("headimgurl");
                    //向后台传数据
                    FormBody.Builder build_wx=new FormBody.Builder();
                    new getPhoneIdutils(context).getid();
                    final String phoneid= sp.getString("phone_id","");
                    build_wx
                            .add("unionid", unionids)
                            .add("openid", wx_openid)
                            .add("bind_type", "wx")
                            .add("connect_name", nickname)
                            .add("connect_image", image_url)
                            .add("mime", phoneid)
                            .add("phone", Preference.phone)
                            .add("version", Preference.version)
                            .build();
                    LogUtils.e("=====微信======", "" + image_url + "/" + nickname + "/" + unionids + "/" + wx_openid);
//                    Toast.makeText(context, image_url+"/"+ nickname+"/"+phoneid, Toast.LENGTH_SHORT).show();
                    new HttpRequestUtils((Activity) context).httpPost(Preference.connect, build_wx,
                            new HttpRequestUtils.ResRultListener() {
                                @Override
                                public void onFailure(IOException e) {
                                    Log.e("wx登录", e.toString());
                                }
                                @Override
                                public void onSuccess(String json) {
                                    Log.e("wx登录", json.toString());
                                    Gson gson = new Gson();
                                    registInfo registinfo = gson.fromJson(json, registInfo.class);
                                    if (registinfo.status.equals("_0000")) {

                                        new SaveOneInfoUtils(context, registinfo).save();
                                        Intent intent=new Intent(context,MainActivity.class);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);
//                                        ((Activity) context).setResult(100);
                                        ((Activity) context).finish();
                                    } else if (registinfo.status.equals("_1001")){
                                        sp.edit().putString(Preference.username,
                                                registinfo.user_info.username)
                                                .putString(Preference.passwd,
                                                        registinfo.user_info.passwd)
                                                .commit();
                                        //5.0前,老的三方账号没有绑定账号的传1
                                        Intent intent=new Intent(context,bindPhoneActivity.class);
                                        intent.putExtra("load","1");
                                        context.startActivity(intent);
                                        //调用bindOld接口,旧账号绑定手机号并登录
                                        Toast.makeText(context, registinfo.message, Toast.LENGTH_SHORT).show();
                                    }else if (registinfo.status.equals("_1002")){
                                        //5.0后,新的三方账号没有绑定账号的传1
                                        //新的三方账号
                                        sp.edit()
                                                .putString("register_time","0")
                                                .commit();
                                        Intent intent=new Intent(context,bindPhoneActivity.class);
                                        intent.putExtra("load","2");
                                        intent.putExtra("openid",wx_openid);
                                        intent.putExtra("bind_type","wx");
                                        intent.putExtra("mime",phoneid);
                                        intent.putExtra("connect_name",nickname);
                                        intent.putExtra("connect_image",image_url);
                                        intent.putExtra("unionid",unionids);
                                        context.startActivity(intent);
                                        //调用bindConnect 接口,新的联合登录绑定账号 并登录
                                        Toast.makeText(context, registinfo.message, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, registinfo.message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Exception e) {

                }

            }

        }
        public String parser(String content) throws JSONException {
            String unionid = "";
            if (TextUtils.isEmpty(content) || content.equals("null")
                    || content.equals("[]")) {
                return "";
            }
            JSONObject json = new JSONObject(content);
            Log.e("微信content",content);
            if (!json.isNull("unionid")) {
                unionid = json.getString("unionid");
                WXhelpUtils.wxUnionId = json.getString("unionid");
            }
            if (!json.isNull("openid")) {
                WXhelpUtils.wxOpenId = json.getString("openid");
                wx_openid=json.getString("openid");
            }
            if (!json.isNull("access_token")) {
                WXhelpUtils.wxToken = json.getString("access_token");
            }

            return unionid;

        }
    }





    //--------------------------------------------------------------------------------------------------------------------------
    public void doQQLogin() {
        if (SuperLiveApplication.mQQAuth == null) {
            SuperLiveApplication.mQQAuth = QQAuth
                    .createInstance(Preference.qq_appid,
                            context.getApplicationContext());
        }
        mTencent = Tencent.createInstance(Preference.qq_appid, context);
        IUiListener listener = new BaseUiListener() {
            @Override
            protected void doComplete(JSONObject values) {
                try {
                    if (!values.isNull("openid")) {
                        String openid = values.getString("openid");
                        if (!"".equals(openid)) {
                            getQQUserInfo(openid);
                        } else {
                            Toast.makeText(context, "登录失败", 1000).show();
                            qqLogout(context);
                        }
                    } else {
                        qqLogout(context);
                        doQQLogin();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        mTencent.login((Activity) context, "all", listener);
    }

    public class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            doComplete((JSONObject) response);
            Log.e("QQ登录onComplete---", response.toString());
        }
        protected void  doComplete(JSONObject values) {
            Log.e("QQ登录---doComplete", values.toString());
        }
        @Override
        public void onError(UiError e) {
            Log.e("QQ登录---onError:", "code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
//            progressDialogUtil.dismissProgressDialog();
            Toast.makeText(context, "登录失败", 1000).show();
        }
        @Override
        public void onCancel() {
//            progressDialogUtil.dismissProgressDialog();
            Toast.makeText(context, "登录取消", 1000).show();

        }
    }
    private void getQQUserInfo(final String openid) {
        IUiListener listener = new BaseUiListener() {
            @Override
            protected void doComplete(JSONObject values) {
                try {
                    if (!values.isNull("nickname")) {
                        nickname = values.getString("nickname");
                    }
                    if (!values.isNull("figureurl_qq_2")) {
                        image_url = values.getString("figureurl_qq_2");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtils.e("===================getQQUserInfo", "" + nickname + "" + image_url + " /" + openid);
                //向后台传数据
                FormBody.Builder build_qq=new FormBody.Builder();
                new getPhoneIdutils(context).getid();
                final String phoneid= sp.getString("phone_id","");
                build_qq
                        .add("openid", openid)
                        .add("bind_type", "qq")
                        .add("connect_name", nickname)
                        .add("connect_image", image_url)
                        .add("mime", phoneid)
                        .add("phone", Preference.phone)
                        .add("version", Preference.version)
                        .build();
                Log.e("Q登录介绍",openid+":::"+nickname+":::"+image_url+":::"+phoneid);
                new HttpRequestUtils((Activity) context).httpPost(Preference.connect, build_qq,
                        new HttpRequestUtils.ResRultListener() {
                            @Override
                            public void onFailure(IOException e) {
                                Log.e("QQ登录",e.toString());
                            }
                            @Override
                            public void onSuccess(String json) {
                                Log.e("QQ登录",json.toString());
                                Gson gson = new Gson();
                                registInfo registinfo = gson.fromJson(json, registInfo.class);
                                if (registinfo.status.equals("_0000")){
                                    new SaveOneInfoUtils(context,registinfo).save();
                                    Intent intent=new Intent(context,MainActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
//                                    ((Activity) context).setResult(100);
                                    ((Activity) context).finish();
                                }else if (registinfo.status.equals("_1001")){
                                    sp.edit().putString(Preference.username,
                                            registinfo.user_info.username)
                                            .putString(Preference.passwd,
                                                    registinfo.user_info.passwd)
                                            .commit();
                                    //5.0前,老的三方账号没有绑定账号的传1
                                    Intent intent=new Intent(context,bindPhoneActivity.class);
                                    intent.putExtra("load","1");
                                    context.startActivity(intent);
                                    ((Activity) context).finish();
                                    //调用bindOld接口,旧账号绑定手机号并登录
                                    Toast.makeText(context, registinfo.message, Toast.LENGTH_SHORT).show();
                                }else if (registinfo.status.equals("_1002")){
                                    //新的三方账号
                                    sp.edit()
                                            .putString("register_time","0")
                                            .commit();
                                    //5.0后,新的三方账号没有绑定账号的传1
                                    Intent intent=new Intent(context,bindPhoneActivity.class);
                                    intent.putExtra("load","2");
                                    intent.putExtra("openid",openid);
                                    intent.putExtra("bind_type","qq");
                                    intent.putExtra("mime",phoneid);
                                    intent.putExtra("connect_name",nickname);
                                    intent.putExtra("connect_image",image_url);
                                    intent.putExtra("unionid",unionids);
                                    context.startActivity(intent);
                                    //调用bindConnect 接口,新的联合登录绑定账号 并登录
                                    Toast.makeText(context, registinfo.message, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, registinfo.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        };
        mTencent = Tencent.createInstance(Preference.qq_appid, context);
        mInfo = new UserInfo(context, mTencent.getQQToken());
        mInfo.getUserInfo(listener);
    }
    public static void qqLogout(Context context) {
        Tencent tencent = Tencent.createInstance(Preference.qq_appid,
                context);
        tencent.logout(context);
    }
    //--------------------------------------------------------------------------------------------------------------------------
     /*
    * 159  h5界面的url
    * */
    public static String url(String name, String pass, String url) {
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<message>\n" +
                "\t<userId>" + name + "</userId>\n" +
                "\t<mailAddr>" + "" + "</mailAddr>\n" +
                "\t<mobileNo>" + "" + "</mobileNo>\n" +
                "\t<pwd>" + pass + "</pwd>\n" +
                "<target_url>" + url + "</target_url>" +
                "</message>";
        String message = null;
        try {
            LogUtils.e("DES", "KEY= " + content);
            String keys = Des3Util.Encrypt(content, "123456789123456789123456", new byte[]{});
            LogUtils.e("DES", "KEY= " + keys);
            message = new String(Base64.encode(keys.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        message = "http://cjzb.159cai.com/phpu/cjzb.phpx?Messages=" + message;
        LogUtils.e("DES", "message= " + message);
        return message;
    }
}
