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
 * 手势滑动引导
 */
public class GuideSlidingDialog extends BaseDialog {
    public GuideSlidingDialog(Context context) {
        super(context);
    }
    String key;
    private LinearLayout bg;
    public void setKey(String key) {
        this.key = key;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sliding);
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
                SPUtils.put(context, key, true, Preference.preference);
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
