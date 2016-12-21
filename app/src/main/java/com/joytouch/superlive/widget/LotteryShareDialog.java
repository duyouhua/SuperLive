package com.joytouch.superlive.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.utils.ShareUtils;

/**
 * Created by sks on 2016/6/28.
 */
public class LotteryShareDialog extends BaseDialog implements View.OnClickListener {
    private LinearLayout ll_qq;
    private LinearLayout ll_wx;
    private LinearLayout ll_pyq;
    private RelativeLayout bg;
    private String shareurl;
    private ShareUtils utils = new ShareUtils();
    private Activity context;
    private String title;
    private String content;

    public LotteryShareDialog(Activity context,String zb_id,String room_id,String share_id,String qid,String title) {
        super(context);
        this.context = context;
        this.content = title;
        shareurl = Preference.shareUrl+"zb_id="+zb_id+"&room_id="+room_id+"&share_id="+share_id+"&qid="+qid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_lottery_share);
        initView();
    }

    private void initView() {
        ll_qq = (LinearLayout) this.findViewById(R.id.ll_qq);
        ll_wx = (LinearLayout) this.findViewById(R.id.ll_wx);
        ll_pyq = (LinearLayout) this.findViewById(R.id.ll_pyq);
        bg = (RelativeLayout) this.findViewById(R.id.bg);
        bg.setOnClickListener(this);
        ll_qq.setOnClickListener(this);
        ll_wx.setOnClickListener(this);
        ll_pyq.setOnClickListener(this);
        title = (String) SPUtils.get(context,Preference.matchname,"",Preference.preference);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_qq:
                utils.qqShare(context,content,title,shareurl, "");
                dismiss();
                break;
            case R.id.ll_wx:
                utils.wxFriend(context,0,content,title,shareurl,"");
                dismiss();
                break;
            case R.id.ll_pyq:
                utils.wxFriend(context,1,content,title,shareurl,"");
                dismiss();
                break;
            case R.id.bg:
                dismiss();
                break;
        }
    }
}
