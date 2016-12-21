package com.joytouch.superlive.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.BaseDialog;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.utils.HttpRequestUtils;
import com.joytouch.superlive.utils.KeyboardUtil;
import com.joytouch.superlive.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/7/19.
 */
public class UpdateScoreDialog extends BaseDialog {
    private String redname;
    private String bluename;
    private TextView rednametv;
    private TextView bluenametv;
    private EditText redScore;
    private EditText blueScore;
    private Button ok;
    private Button cancle;
    private boolean isRequest =false;
    private String bifenright;
    private String bifenleft;

    public void setRedname(String redname) {
        this.redname = redname;
    }

    public void setBluename(String bluename) {
        this.bluename = bluename;
    }

    public UpdateScoreDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_updatescore);
        init();
    }

    private void init() {
        rednametv = (TextView) findViewById(R.id.redname);
        bluenametv = (TextView) findViewById(R.id.bluename);
        redScore = (EditText) findViewById(R.id.redScore);
        blueScore = (EditText) findViewById(R.id.blueScore);
        ok = (Button) findViewById(R.id.ok);
        cancle = (Button) findViewById(R.id.cancle);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(redScore.getText().toString()) || TextUtils.isEmpty(redScore.getText().toString())) {
                    Toast.makeText(context, "请输入比分", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isRequest) {
                    isRequest = true;
                    updateScore();
                }
            }
        });
        redScore.setText(bifenleft);
        blueScore.setText(bifenright);
        redScore.setSelection(bifenleft.length());
        blueScore.setSelection(bifenright.length());
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        rednametv.setText(redname);
        bluenametv.setText(bluename);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                KeyboardUtil.closeKeybord(redScore, context);
                KeyboardUtil.closeKeybord(blueScore, context);
            }
        });
    }

    public void updateScore(){
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("phone", Preference.phone);
        builder.add("version", Preference.version);
        builder.add("team1_score", redScore.getText().toString());
        builder.add("team2_score", blueScore.getText().toString());
        builder.add("room_id", Preference.room_id);
        builder.add("token", (String) SPUtils.get(context, Preference.token, "", Preference.preference));
        HttpRequestUtils requestUtils = new HttpRequestUtils((Activity) context);
        requestUtils.httpPost(Preference.editScore, builder, new HttpRequestUtils.ResRultListener() {
            @Override
            public void onFailure(IOException e) {
                isRequest = false;
            }

            @Override
            public void onSuccess(String json) {
                isRequest = false;
                try {
                    JSONObject object = new JSONObject(json);
                    if ("_0000".equals(object.optString("status"))) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(context, object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public void setbifenright(String bifenright) {
        this.bifenright=bifenright;
    }

    public void setbifenlift(String bifenleft) {
        this.bifenleft=bifenleft;
    }
}
