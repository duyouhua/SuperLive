package com.joytouch.superlive.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;

/**
 * Created by lzx on 2016/5/11.
 * 回顾界面分享dialog
 */
public class ReviewShareDialog extends BaseDialog implements View.OnClickListener{
    private LinearLayout ll_qq;
    private LinearLayout ll_weixin;
    private LinearLayout ll_friend;
    private RelativeLayout bg;
    public ReviewShareDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_reviewshare);
        initView();
    }

    private void initView() {
        ll_qq = (LinearLayout) this.findViewById(R.id.ll_qq);
        ll_qq.setOnClickListener(this);
        ll_weixin = (LinearLayout) this.findViewById(R.id.ll_weixin);
        ll_weixin.setOnClickListener(this);
        ll_friend = (LinearLayout) this.findViewById(R.id.ll_friend);
        ll_friend.setOnClickListener(this);
        bg = (RelativeLayout) this.findViewById(R.id.bg);
        bg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_qq:
                break;
            case R.id.ll_weixin:
                break;
            case R.id.ll_friend:
                break;
        }
        dismiss();
    }
}
