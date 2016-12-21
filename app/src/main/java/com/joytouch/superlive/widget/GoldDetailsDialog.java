package com.joytouch.superlive.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.GoldDetailsAdapter;
import com.joytouch.superlive.javabean.GoldDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sks on 2016/4/25.
 */
public class GoldDetailsDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private CircleImageView iv_icon;
    private TextView tv_name;
    private ImageView iv_level;
    private TextView tv_level;
    private TextView tv_sign;
    private ImageView iv_finish;
    private ListView listView;
    private List<GoldDetails> detailses;
    public GoldDetailsDialog(Context context) {
        super(context, R.style.Dialog_bocop);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gold_details);
        initView();
    }

    private void initView() {
        getDate();
        iv_icon = (CircleImageView) findViewById(R.id.iv_icon);
        tv_name = (TextView) this.findViewById(R.id.tv_name);
        iv_level = (ImageView) this.findViewById(R.id.iv_level);
        tv_level = (TextView) this.findViewById(R.id.tv_level);
        tv_sign = (TextView) this.findViewById(R.id.tv_sign);
        iv_finish = (ImageView) this.findViewById(R.id.iv_finish);
        iv_finish.setOnClickListener(this);
        listView = (ListView) this.findViewById(R.id.listView);
        listView.setAdapter(new GoldDetailsAdapter(detailses,context));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_finish:
                dismiss();
                break;
        }
    }
    private void getDate(){
        detailses = new ArrayList<>();
        for (int i = 0;i<10;i++){
            detailses.add(new GoldDetails());
        }
    }
}
