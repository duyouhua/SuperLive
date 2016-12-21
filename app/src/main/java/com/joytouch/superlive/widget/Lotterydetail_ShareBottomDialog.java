package com.joytouch.superlive.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.joytouch.superlive.R;
import com.joytouch.superlive.utils.ViewFindUtils;

public class Lotterydetail_ShareBottomDialog extends BottomBaseDialog<Lotterydetail_ShareBottomDialog> {
    private LinearLayout ll_wechat_friend_circle;
    private LinearLayout ll_wechat_friend;
    private LinearLayout ll_qq;
    private View ll_qqzone;

    public Lotterydetail_ShareBottomDialog(Context context, View animateView) {
        super(context, animateView);
    }

    public Lotterydetail_ShareBottomDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        View inflate = View.inflate(context, R.layout.top_dialog_share, null);
        ll_wechat_friend_circle = ViewFindUtils.find(inflate, R.id.ll_wechat_friend_circle);
        ll_wechat_friend = ViewFindUtils.find(inflate, R.id.ll_wechat_friend);
        ll_qq = ViewFindUtils.find(inflate, R.id.ll_qq);
        ll_qqzone = ViewFindUtils.find(inflate, R.id.ll_qqzone);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {

        ll_wechat_friend_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "微信朋友圈", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        ll_wechat_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "微信朋友", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        ll_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "QQ", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        ll_qqzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "QQzone", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }
}
