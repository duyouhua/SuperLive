package com.joytouch.superlive.widget;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.cammerActivity;
import com.joytouch.superlive.interfaces.TagSelectBack;
import com.joytouch.superlive.utils.city.CityPicker;
import com.joytouch.superlive.widget.TimeSelectDialog.DialogBase;
import com.joytouch.superlive.widget.TimeSelectDialog.saishitype_picker;

/**
 * Created by Administrator on 8/12 0012.
 */
public class TagSelectDialog extends DialogBase {
    private  cammerActivity context;
    private  int XmlId;
    private String privencename="足球";

    public TagSelectDialog(cammerActivity activity,int XmlId) {
        super(activity);
        this.XmlId=XmlId;
        this.context=activity;
        this.setCancel(false);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);//外部点击无效
        this.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        this.getWindow().setWindowAnimations(R.style.servicescheduledialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(XmlId);
        final saishitype_picker cityPicker = (saishitype_picker) findViewById(R.id.citypicker);
        cityPicker.setOnSelectingListener(new CityPicker.OnSelectingListener() {
            @Override
            public void selected(boolean selected) {
                privencename = cityPicker.getPrivence_string();
            }
        });

        findViewById(R.id.positive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagSelectBack achor = (TagSelectBack) context;
                achor.selecttag(privencename,0);
                dismiss();
            }
        });
        findViewById(R.id.negtive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void onBuilding() {

    }

    @Override
    protected boolean OnClickPositiveButton() {
        return false;
    }

    @Override
    protected void OnClickNegativeButton() {

    }

    @Override
    protected void onDismiss() {

    }
}
