package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.InvitationFriendActivity;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ShareUtils;

/**
 * Created by yj on 2016/4/26.
 * 聊天邀请好友
 */
public class ShareDialog extends BaseDialog implements View.OnClickListener {
    private ImageView qq;
    private ImageView wx;
    private ImageView fans;
    private RelativeLayout bg;
    private String qiutanid;
    private String matchid;
    private String matchname;
    private String shareurl;
    private String isPrivate;
    private ImageView wxzone;
    private String contetnt;
    private boolean isBanner;
    private LinearLayout qqll;
    private LinearLayout fansLL;

    public void setIsBanner(boolean isBanner) {
        this.isBanner = isBanner;
    }

    public void setContetnt(String contetnt) {
        this.contetnt = contetnt;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void setMatchname(String matchname) {
        this.matchname = matchname;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public void setQiutanid(String qiutanid) {
        this.qiutanid = qiutanid;
    }

    public ShareDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invitionfriend);
        initView();
    }

    private void initView() {
        wxzone=(ImageView)findViewById(R.id.wxzone);
        qq = (ImageView) findViewById(R.id.qq);
        wx = (ImageView) findViewById(R.id.wx);
        fans = (ImageView) findViewById(R.id.fans);
        bg = (RelativeLayout) findViewById(R.id.bg);
        qqll = (LinearLayout) findViewById(R.id.qq_ll);
        fansLL = (LinearLayout) findViewById(R.id.fans_ll);
        wxzone.setOnClickListener(this);
        qq.setOnClickListener(this);
        wx.setOnClickListener(this);
        fans.setOnClickListener(this);
        bg.setOnClickListener(this);
        if(isBanner){

            qqll.setVisibility(View.INVISIBLE);
            fansLL.setVisibility(View.INVISIBLE);
        }else {
            qqll.setVisibility(View.VISIBLE);
            fansLL.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        ShareUtils utils = new ShareUtils();
        switch (view.getId()){
            case R.id.qq:
                utils.qqShare((Activity) context, matchname+ConfigUtils.getShareContent(), matchname,shareurl, "");
                break;
            case R.id.wx:
                if(!isBanner){
                    utils.wxFriend((Activity) context, 0, ConfigUtils.getShareContent(), matchname,shareurl, "");
                }else {
                    utils.wxFriend((Activity) context, 0, contetnt, matchname,shareurl, "");
                }

                break;
            case R.id.fans:
                Intent intent = new Intent(context,InvitationFriendActivity.class);
                intent.putExtra("qiutanid",qiutanid);
                intent.putExtra("matchid",matchid);
                intent.putExtra("isprivate",isPrivate);
                context.startActivity(intent);
                break;
            case R.id.bg:

                break;

            case R.id.wxzone:
                if(!isBanner){
                    utils.wxFriend((Activity) context, 1, ConfigUtils.getShareContent(), matchname,shareurl, "");
                }else {
                    utils.wxFriend((Activity) context, 1, contetnt, matchname,shareurl, "");
                }

                break;

        }
        dismiss();
    }
}
