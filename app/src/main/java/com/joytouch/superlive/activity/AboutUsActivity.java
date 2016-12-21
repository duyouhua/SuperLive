package com.joytouch.superlive.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;

/**
 * 关于我们
 * 信息显示
 */
public class AboutUsActivity extends BaseActivity {
    private ImageView iv_finish;
    private TextView tv_title;
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    private void initView() {
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("关于我们");
        tv_version = (TextView) this.findViewById(R.id.tv_version);
        tv_version.setText(Preference.app_version);
        iv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
