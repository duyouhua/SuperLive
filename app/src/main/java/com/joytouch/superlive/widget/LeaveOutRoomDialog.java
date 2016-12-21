package com.joytouch.superlive.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;

/**
 * Created by lzx on 2016/7/3.
 * 开摄像头开房间 用户离开界面时 如果有未开奖的题的话 弹出对话框
 */
public class LeaveOutRoomDialog extends BaseDialog implements View.OnClickListener{
    private Button but_ok;
    private Button but_cancel;
    private Activity activity;
    public LeaveOutRoomDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_leaveroom);
        initView();
    }

    private void initView() {
        but_ok = (Button) this.findViewById(R.id.but_ok);
        but_cancel = (Button) this.findViewById(R.id.but_cancel);
        but_ok.setOnClickListener(this);
        but_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.but_ok:
                activity.finish();
                break;
            case R.id.but_cancel:
                dismiss();
                break;
        }
    }
}
