package com.joytouch.superlive.widget;

import android.content.Context;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;

/**
 * Created by sks on 2016/5/10.
 */
public class TimePickerDialog extends BaseDialog {
    public TimePickerDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_timepicker);
    }
}
