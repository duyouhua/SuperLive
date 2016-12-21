package com.joytouch.superlive.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;

/**
 * Created by sks on 2016/4/18.
 */
public class RuleshaopDialog extends Dialog implements View.OnClickListener{
    private ImageView iv_close;
    private TextView tv_rules;
    private String rule;

    public RuleshaopDialog(Context context) {
        super(context, R.style.Dialog_bocop);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rule);
        initView();
    }

   
    private void initView() {
        iv_close = (ImageView) this.findViewById(R.id.iv_close);
        tv_rules = (TextView) this.findViewById(R.id.tv_rules);
        iv_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_close:
                this.dismiss();
                break;
        }
    }
}
