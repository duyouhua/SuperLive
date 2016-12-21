package com.joytouch.superlive.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;

/**
 * Created by yj on 2016/7/30.
 */
public class CloseDialog extends BaseDialog {
    public CloseDialog(Context context) {
        super(context);
    }
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_close);
        back = (TextView) findViewById(R.id.exit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
