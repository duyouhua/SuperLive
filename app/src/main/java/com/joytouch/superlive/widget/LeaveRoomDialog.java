package com.joytouch.superlive.widget;

import android.content.Context;
import android.os.Bundle;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;

/**
 * Created by lzx on 2016/6/28.
 * 开摄像头开房间 用户离开界面时 如果有未开奖的题的话 弹出对话框
 */
public class LeaveRoomDialog extends BaseDialog {
    public LeaveRoomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_leaveroom);
    }
}
