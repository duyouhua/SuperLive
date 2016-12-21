package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.PackageUtils;
import com.joytouch.superlive.widget.updateVersion.UpDateVersion;

/**
 * 启动页
 * Created by Administrator on 5/11 0011.
 */
public class StartActivity extends BaseActivity {
    private TextView version;
    private SharedPreferences sp;
    //是否是第一次进入
    UpDateVersion updatever;
    private Handler handler=new Handler();
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            startMain();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        //安卓按home键之后，再次点击程序图标避免再次重新启动程序解决办法
        //在最先启动的activity的onCreate()方法里添加如下代码即可解决该问题：
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }


        sp = getSharedPreferences(Preference.preference, Context.MODE_PRIVATE);

        version = (TextView) findViewById(R.id.version);
        version.setText(Preference.app_version);

        //获取版本,如果线上版本的versionCode小于本地codeever,保存isFirst
        if (sp.getInt(Preference.versionCode, 0) < PackageUtils
                .getVersionCode(this)) {
        }
        updatever = new UpDateVersion(StartActivity.this);
        updatever.setNewVersionListener(new UpDateVersion.NewVersionListener() {
            @Override
            public void onNewVersion() {
                myHandler.removeCallbacksAndMessages(null);
            }
        });
        updatever.setUpdateCancelListener(new UpDateVersion.UpdateCancelListener() {
            @Override
            public void onUpdateCancel() {
                startMain();
            }
        });
        Runnable runnable=new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                updatever.update();
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void startMain() {
        if (sp.getBoolean("isFirst", true) == true) {
            //第一次,进入引导页
            Intent intent = new Intent(StartActivity.this,
                    IntroductionActivity.class);
            startActivity(intent);
        } else {
            //非第一次,进入主页面
            if (sp.getString(Preference.myuser_id,"").equals("")){
                Intent intent = new Intent(StartActivity.this,
                        LoadActivity.class);
                intent.putExtra("isstar","1");
                startActivity(intent);
            }else {
                Intent intent = new Intent(StartActivity.this,
                        MainActivity.class);
                startActivity(intent);
            }
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (updatever != null) {
            updatever.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setContentView(new View(this));
        System.gc();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int force=sp.getInt("updateversion",4);
            if (force==3){
//                System.exit(0);
            }else if (force==2){

            }else if (force==1){

            }else{

            }
        }
        return false;
    }
}