package com.joytouch.superlive.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;

/**
 * Created by Administrator on 5/13 0013.
 */
public class TaskDialog extends Dialog {
    private final Context context;
    private final String monry;
    private TextView tv_money;
    private ImageView close;

    public TaskDialog(Context context, String money) {
        super(context, R.style.Dialog_bocop);
        this.context = context;
        this.monry=money;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);
        initView();
    }

    private void initView() {
        close=(ImageView)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
