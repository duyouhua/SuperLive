package com.joytouch.superlive.widget.updateVersion;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.SoftwareUpdate;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.MD5Util;
import com.joytouch.superlive.utils.PackageUtils;
import com.joytouch.superlive.utils.city.File_Util;
import com.joytouch.superlive.utils.view.DownLoadApkTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import okhttp3.FormBody;

/**
 * Created by Administrator on 5/11 0011.
 */
public class UpDateVersion {
    private final SharedPreferences sp;
    /**
     flag
     普通升级--[1]--杀进程重启后有提示（暂不升级，立刻升级），点击返回键，不退出app，不做页面跳转；
     中级升级--[2]--杀进程重启，挂起后再启动提示（暂不升级，立刻升级），点击返回键，不退出app，不做页面跳转；
     强制升级--[3]--任何方式进入app，提示升级（立刻升级）；点击返回键，直接退出；
     */
    private   Context context;
    private String flag="0";//默认普通升级
    private NewVersionListener newVersionListener_;
    private UpdateCancelListener updateCancelListener_;
    private boolean isUpdate=false;

    public UpDateVersion(Context context) {
        this.context=context;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }



    public void update(){
        String channel_b = PackageUtils.getSource(context);
        String codeVer_b = String.valueOf(PackageUtils.getVersionCode(context));
        String softname_b = Preference.softname;
        String ver_b = PackageUtils.getVersionName(context);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("channel", channel_b);
        map.put("codeVer", codeVer_b);
        map.put("softname", softname_b);
        map.put("ver", ver_b);
        map.put("version", Preference.version);
        String sign = getMD5(map);

        FormBody.Builder build=new FormBody.Builder();
        build
                .add("channel", channel_b)
                .add("codeVer", codeVer_b)
                .add("softname", softname_b)
                .add("ver", ver_b)
                .add("version", Preference.version)
                .add("sign", sign)
                .build();
        new HttpRequestUtils((Activity) context).httpPost(Preference.update_url, build,
                new HttpRequestUtils.ResRultListener() {
                    @Override
                    public void onFailure(IOException e) {
                        //获取网络数据失败会跳转到主页面
//                        Intent intent=new Intent(context, MainActivity.class);
//                        context.startActivity(intent);
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("版本更新", json);
                        String json_ = File_Util.readAssets(context, "banben.json");
                        Gson gson = new Gson();
                        SoftwareUpdate Bean = gson.fromJson(json, SoftwareUpdate.class);

                            //本版本
                            int version_code = PackageUtils.getVersionCode(context);
                            int newVersion = 0;

                            String ver = Bean.ver;
                            String updateDescription = Bean.updateDescription;
                            String downloadUrl = Bean.downloadUrl;
                            String codeVer = Bean.codeVer;
                            int force = Bean.force;

                            if (!TextUtils.isEmpty(codeVer)) {
                                newVersion = Integer.parseInt(codeVer);
                            }
//                            当本地的versioncode小于服务器传来的newVersion时,并且force>0,才会提示更新
                            if (version_code < newVersion) {
                                //后台数据force,force>1升级
                                if (force >0) {
                                    if (newVersionListener_ != null) {
                                        newVersionListener_.onNewVersion();
                                    }
                                    setUpdate(Bean);
                                } else {
                                    updateCancelListener_.onUpdateCancel();
                                }
                                //本地保存一个forcr_,用于判定是哪种类型的更新
                                sp.edit().putInt("updateversion",force).commit();
                            }else{
                                //force_==4,表示不属于任何一种更新
                                sp.edit().putInt("updateversion",4).commit();
                                updateCancelListener_.onUpdateCancel();
//                                ((Activity) context).finish();
                            }
                    }
                });
    }

    private static Dialog dialog;

    private void setUpdate(final SoftwareUpdate bean) {
        dialog = new Dialog(context, R.style.Dialog_bocop);
        dialog.setContentView(R.layout.updateversiondialog);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
        dialog.setCancelable(false);
        TextView tvtv=(TextView)dialog.findViewById(R.id.tvtv);
        tvtv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
        tv_content.setVisibility(View.VISIBLE);
        tv_title.setText("升级提示");
        tv_content.setText(bean.updateDescription);

        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);

        //强制升级,force=3;
        //中等升级,force=2;
        //普通升级:force=1;
        //不升级,force=0;没有提示,>0都有提示
        if ("3".equals(bean.force+"")) {
            btn_cancel.setVisibility(View.GONE);
        }
        btn_cancel.setText("暂不更新");
        btn_submit.setText("立刻下载");

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isUpdate = true;
                new DownLoadApkTask((Activity) context, "update.apk", bean.downloadUrl).execute();
                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (!isUpdate &&updateCancelListener_ != null) {
                    if ("3".equals(bean.force+"")) {
                        //强制,后台挂起与杀进程都会提示,且退出程序
                        System.exit(0);
                    }else if ("2".equals(bean.force+"")){
                        updateCancelListener_.onUpdateCancel();
                        //中等更新,杀进程,后台挂起都会提示

                    }else if ("1".equals(bean.force+"")){
                        updateCancelListener_.onUpdateCancel();
                        //普通更新,只有杀进程才会提示

                    }else{
                        updateCancelListener_.onUpdateCancel();
                    }

                }
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }
    private static String getMD5(HashMap<String, String> map) {

        ArrayList<String> keyList = new ArrayList<String>();
        for (String key : map.keySet()) {
            keyList.add(key);
        }
        Collections.sort(keyList);

        StringBuffer buffer = new StringBuffer();
        buffer.append(Preference.key_f);
        for (String key : keyList) {
            buffer.append(map.get(key));
        }
        buffer.append(Preference.key_b);
        return MD5Util.md5(buffer.toString());
    }


    public void cancel() {

    }

    public interface NewVersionListener {
        public void onNewVersion();
    }

    public interface UpdateCancelListener {
        public void onUpdateCancel();
    }

    public void setNewVersionListener(NewVersionListener newVersionListener) {
        newVersionListener_ = newVersionListener;
    }
    public void setUpdateCancelListener(UpdateCancelListener updateCancelListener) {
        updateCancelListener_ = updateCancelListener;
    }


}
