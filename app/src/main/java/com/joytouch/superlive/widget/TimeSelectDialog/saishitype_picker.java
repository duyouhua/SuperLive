package com.joytouch.superlive.widget.TimeSelectDialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.google.gson.Gson;
import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.cityprovienceInfo;
import com.joytouch.superlive.javabean.provinceinfo;
import com.joytouch.superlive.utils.city.CityPicker;
import com.joytouch.superlive.utils.city.File_Util;
import com.joytouch.superlive.utils.city.ScrollerNumberPicker;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 5/25 0025.
 */
public class saishitype_picker extends LinearLayout {
    private Context context;
    private List<provinceinfo> province_list = new ArrayList<provinceinfo>();
    private ArrayList<String> province_content = new ArrayList<String>();
    private ArrayList<String> province_id = new ArrayList<String>();
    private ScrollerNumberPicker provincePicker;
    /** 选择监听 */
    private CityPicker.OnSelectingListener onSelectingListener;
    /** 刷新界面 */
    private static final int REFRESH_VIEW = 0x001;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };
    private String city_string;

    public void setOnSelectingListener(CityPicker.OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public interface OnSelectingListener {
        public void selected(boolean selected);
    }
    public saishitype_picker(Context context) {
        super(context);
        this.context = context;
        getaddressinfo();
    }

    public saishitype_picker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getaddressinfo();
    }

    public saishitype_picker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void getaddressinfo() {
        String area_str = File_Util.readAssets(context, "tag.json");
        Gson gson=new Gson();
        cityprovienceInfo info=gson.fromJson(area_str, cityprovienceInfo.class);
        province_list = info.province;

        for (int i=0;i<province_list.size();i++){
            province_content.add(province_list.get(i).area_name);
            province_id.add(province_list.get(i).area_id);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.saishitype_picker, this);
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
        //设置三个滚轮的数据源和默认位置
        provincePicker.setData(province_content,province_id);
        provincePicker.setDefault(1);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text, String areaid) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                Message message = new   Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }
            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
    }
    public String getPrivence_string() {
        city_string =provincePicker.getSelectedText();
        return city_string;
    }

}
