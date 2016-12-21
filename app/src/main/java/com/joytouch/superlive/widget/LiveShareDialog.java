package com.joytouch.superlive.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ShareUtils;

/**
 * Created by sks on 2016/8/12.
 */
public class LiveShareDialog extends BaseDialog implements View.OnClickListener{
    private ImageView iv_finish;
    private RelativeLayout rl_qq;
    private RelativeLayout rl_wx;
    private RelativeLayout rl_pyq;
    private RelativeLayout rl_fensi;
    private Activity context;
    private String matchid;
    private ShareUtils utils = new ShareUtils();
    private String matchname="";
    private String URL="";

    public LiveShareDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_live_share);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        rl_qq = (RelativeLayout) this.findViewById(R.id.rl_qq);
        rl_qq.setOnClickListener(this);
        rl_wx = (RelativeLayout) this.findViewById(R.id.rl_wx);
        rl_wx.setOnClickListener(this);
        rl_pyq = (RelativeLayout) this.findViewById(R.id.rl_pyq);
        rl_pyq.setOnClickListener(this);
        rl_fensi = (RelativeLayout) this.findViewById(R.id.rl_fensi);
        rl_fensi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                dismiss();
                break;
            case R.id.rl_qq:
                utils.qqShare((Activity) context, matchname+ ConfigUtils.getShareContent(), matchname,URL, "");
                dismiss();
                break;
            case R.id.rl_wx:
                utils.wxFriend((Activity) context, 0, ConfigUtils.getShareContent(), matchname,URL, "");
                dismiss();
                break;
            case R.id.rl_pyq:
                utils.wxFriend((Activity) context, 1, ConfigUtils.getShareContent(), matchname,URL, "");
                dismiss();
                break;
            case R.id.rl_fensi:
                InvitationFansDialog fansDialog = new InvitationFansDialog(context);
                fansDialog.setMatchid(matchid);
                fansDialog.show();
                dismiss();
                break;

        }
    }

    public void setMatchid(String matchid,String url,String matchname) {
        this.matchid = matchid;
        this.URL=url;
        this.matchname=matchname;
    }

}
