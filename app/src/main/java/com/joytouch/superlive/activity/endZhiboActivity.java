package com.joytouch.superlive.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.CloseRoom;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 8/17 0017.
 */
public class endZhiboActivity extends BaseActivity implements View.OnClickListener {
    private int screenWidth;
    private int screenHeight;
    private TextView bluecont;
    private TextView redcount;
    private CloseRoom closeRoomInfo;
    private CircleImageView iv_image;
    private TextView tv_name;
    private TextView tv_level;
    private TextView tv_team1;
    private TextView tv_qiupiao_num1;
    private TextView tv_team2;
    private TextView tv_qiupiao_num2;
    private TextView tv_alllines;
    private TextView tv_allqiupiaonum;
    private TextView tv_back;
    private TextView tv_roomall;
    private SharedPreferences sp;
    private String team1name;
    private String team2name;
    private String team1logo;
    private String team2logo;
    private CircleImageView iv_tubiao1;
    private CircleImageView iv_tubiao2;
    private ProgressBar progress_horizontal_left;
    /**
     * 当前进度的值
     */
    private int mCount=0;
    private ProgressBar progress_horizontal_right;
    private int mRightCount=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endzhibo);
        sp = this.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        Intent intent=getIntent();
        team1name=intent.getStringExtra("team1");
        team2name=intent.getStringExtra("team2");
        team1logo=intent.getStringExtra("logo1");
        team2logo=intent.getStringExtra("logo2");
        Bundle bundle=intent.getExtras();
        closeRoomInfo= (CloseRoom) bundle.getSerializable("closeRoom");
        initUI();
    }

    private void initUI() {
        progress_horizontal_right=(ProgressBar)findViewById(R.id.progress_horizontal_right);
        progress_horizontal_left=(ProgressBar)findViewById(R.id.progress_horizontal_left);
        iv_tubiao2 =(CircleImageView)findViewById(R.id.iv_tubiao2);
        iv_tubiao1=(CircleImageView)findViewById(R.id.iv_tubiao1);
        tv_back=(TextView)findViewById(R.id.tv_back);
        tv_roomall=(TextView)findViewById(R.id.tv_roomall);
        tv_allqiupiaonum=(TextView)findViewById(R.id.tv_allqiupiaonum);
        tv_alllines=(TextView)findViewById(R.id.tv_alllines);
        tv_qiupiao_num2=(TextView)findViewById(R.id.tv_qiupiao_num2);
        tv_team2=(TextView)findViewById(R.id.tv_team2);
        tv_qiupiao_num1=(TextView)findViewById(R.id.tv_qiupiao_num1);
        tv_team1=(TextView)findViewById(R.id.tv_team1);
        tv_level=(TextView)findViewById(R.id.tv_level);
        tv_name=(TextView)findViewById(R.id.tv_name);
        iv_image=(CircleImageView)findViewById(R.id.iv_image);
        redcount=(TextView)this.findViewById(R.id.red_count);
        bluecont = (TextView) findViewById(R.id.blue_count);
        tv_back.setOnClickListener(this);

        tv_alllines.setText(closeRoomInfo.getWatch_num());
        int red=Integer.parseInt(closeRoomInfo.getRoom_red());
        int blue=Integer.parseInt(closeRoomInfo.getRoom_blue());
        int allcon=red+blue;
//        matchView(80, 20);
        matchleft(blue, allcon);//蓝
        matchright(blue, allcon);//红
        if (allcon==0){
            progress_horizontal_right.setVisibility(View.GONE);
            progress_horizontal_left.setVisibility(View.GONE);
        }else if (red==0){
            progress_horizontal_right.setVisibility(View.GONE);
        }else if (blue==0){
            progress_horizontal_left.setVisibility(View.GONE);
        }
        tv_roomall.setText(closeRoomInfo.getRoom_all()+"  ");
        tv_qiupiao_num1.setText(closeRoomInfo.getRoom_red() + "球票");
        tv_qiupiao_num2.setText(closeRoomInfo.getRoom_blue()+"球票");
        tv_team1.setText(team1name);
        tv_team2.setText(team2name);
        tv_name.setText(sp.getString(Preference.nickname, ""));
        ConfigUtils.level(tv_level, sp.getString(Preference.level, ""));
        ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + team1logo, iv_tubiao1, ImageLoaderOption.optionduiwu);
        ImageLoader.getInstance().displayImage(Preference.photourl + "200x200/" + team2logo, iv_tubiao2, ImageLoaderOption.optionduiwu);
        ImageLoader.getInstance().displayImage(Preference.photourl+"200x200/"+sp.getString(Preference.headPhoto,""), iv_image, ImageLoaderOption.optionsHeader);//optionduiwu
    }

    private void matchright(int i1, int all) {
        mRightCount = 100;
        progress_horizontal_right = (ProgressBar) findViewById(R.id.progress_horizontal_right);
        progress_horizontal_right.setMax(all);
        progress_horizontal_right.setProgress(i1);
        progress_horizontal_right.setIndeterminate(false);
//        new Thread()
//        {
//            public void run()
//            {
//                try
//                {
//                    while (mRightCount >= 0)
//                    {
//                        progress_horizontal_right.setProgress(mRightCount--);
//                        Thread.sleep(50);
//                    }
//                    if (mRightCount < 0)
//                    {
//                        return;
////                        mHandler.sendEmptyMessage(IntentUtils.HANDLER_RIGHT);
//                    }
//                }
//                catch (Exception ex)
//                {
//                }
//            }
//        }.start();
    }

    private void matchleft(final int i1, final int all) {
        mCount = 0;
        progress_horizontal_left.setMax(all);
        progress_horizontal_left.setProgress(i1);
        progress_horizontal_left.setIndeterminate(false);
//        new Thread()
//        {
//            public void run()
//            {
//                try
//                {
//                    while (mCount <= all)
//                    {
//                        progress_horizontal_left.setProgress(mCount++);
//                        Thread.sleep(50);
//                    }
//                    if (mCount > i1)
//                    {
//                        return;
////                        mHandler.sendEmptyMessage(IntentUtils.HANDLER_LEFT);
//                    }
//                }
//                catch (Exception ex)
//                {
//                }
//            }
//        }.start();
    }

    public  void matchView(int red,int blue){
        float redcon;
        float bluecon;
        if(red==0&&blue==0) {
            redcon = 0;
            bluecon = 0;
        }else {
            redcon = (float) (red * 1.0 / (red + blue));
            bluecon = (float) (blue * 1.0 / (red + blue));
        }
        int layout = (screenWidth- ConfigUtils.dip2px(endZhiboActivity.this, 176))/2;
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams((int) (layout*redcon),ConfigUtils.dip2px(endZhiboActivity.this,11));
        redcount.setLayoutParams(parms);
        redcount.setPadding(8, 0, 0, 0);
        redcount.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams parm = new LinearLayout.LayoutParams((int) (layout*bluecon),ConfigUtils.dip2px(endZhiboActivity.this,11));
        redcount.setPadding(0, 0, 8, 0);
        redcount.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        bluecont.setLayoutParams(parm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
