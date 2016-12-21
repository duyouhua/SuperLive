package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.utils.KeyboardUtil;

/**
 * 全屏聊天,(解决软键盘弹出盖住edittext剩余的其它控件)
 * Created by sks on 2016/8/14.
 */
public class LiveChatDialog extends BaseDialog implements View.OnClickListener {
    private EditText et_input;
    private TextView tv_send;
    private SendClickListener clickListener;
    private Context context;

    public LiveChatDialog(Context context,SendClickListener clickListener) {
        super(context);
        this.context = context;
        this.clickListener = clickListener;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_livechat);
        initView();
    }

    private void initView() {
        et_input = (EditText) this.findViewById(R.id.et_input);
        tv_send = (TextView) this.findViewById(R.id.tv_send);
        findViewById(R.id.iv_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                KeyboardUtil.closeKeybord(et_input,context);
            }
        });
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(et_input.getText().toString())) {
                    Toast.makeText(context, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    clickListener.onSend(et_input, et_input.getText().toString());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftKeyboard(context,et_input);
            }
        },500);
    }

    public void showSoftKeyboard(Context context,View view) {
        Activity activity=(Activity) context;
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        view.setFocusable(true);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public interface SendClickListener{
        void onSend(EditText editor,String message);
    }

    public EditText getEt_input() {
        return et_input;
    }

    public void setMemoryMessage(String message){
        if (null!=et_input){
            et_input.setText(message);
        }
    }
}
