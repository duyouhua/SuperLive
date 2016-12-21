package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.SPUtils;

/**
 * Created by yj on 2016/5/25.
 */
public class FirstLotteryDialog extends BaseDialog {
    public FirstLotteryDialog(Context context) {
        super(context);
    }
    private LinearLayout bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lottery_rules);
       bg = (LinearLayout) findViewById(R.id.bg);
               bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                SPUtils.put(context, "isFistLiveLottery", true, Preference.preference);
            }
        });
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                WindowManager windowManager = ((Activity) context).getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.width = (int) (display.getWidth()); //设置宽度
                lp.height = (int) (display.getHeight());
                getWindow().setAttributes(lp);
            }
        });
    }
}
