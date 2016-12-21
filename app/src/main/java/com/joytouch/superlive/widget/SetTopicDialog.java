package com.joytouch.superlive.widget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;

/**
 * Created by lzx on 2016/5/8.
 */
public class SetTopicDialog extends BaseDialog implements View.OnClickListener{
    private EditText et_content;
    private EditText et_option1;
    private EditText et_option2;
    private RelativeLayout rl_time;
    private TextView tv_time;
    private TextView tv_submit;
    private TextView tv_cancel;
    public SetTopicDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_settopic);
        initView();
    }

    private void initView() {
        et_content = (EditText) this.findViewById(R.id.et_content);
        et_option1 = (EditText) this.findViewById(R.id.et_option1);
        et_option2 = (EditText) this.findViewById(R.id.et_option2);
        rl_time = (RelativeLayout) this.findViewById(R.id.rl_time);
        rl_time.setOnClickListener(this);
        tv_time = (TextView) this.findViewById(R.id.tv_time);
        tv_submit = (TextView) this.findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(this);
        tv_cancel = (TextView) this.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_submit:
                if (!isEmpty(et_content)){
                    return;
                }
                if (!isEmpty(et_option1)){
                    return;
                }
                if (!isEmpty(et_option2)){
                    return;
                }
                //提交操作
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.rl_time:

                break;
        }
    }
    public boolean isEmpty(EditText et){
        if (et== null){
            return false;
        }
        if (et.getText() == null && et.getText().equals("")){
            return false;
        }else {
            return  true;
        }
    }
}
