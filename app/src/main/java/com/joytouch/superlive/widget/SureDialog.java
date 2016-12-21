package com.joytouch.superlive.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;

/**
 * Created by Administrator on 2016/8/17.
 */
public class SureDialog extends BaseDialog {
    public SureDialog(Context context) {
        super(context);
    }
    private TextView content;
    private TextView sure;
    private TextView cancle;
    private boolean isSure;
    private String contents;

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isSure() {
        return isSure;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_surebanned);
        content = (TextView) findViewById(R.id.content);
        sure = (TextView) findViewById(R.id.sure);
        cancle = (TextView) findViewById(R.id.cancel);
        content.setText(contents);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSure = true;
                dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSure = false;
                dismiss();
            }
        });
    }
}
