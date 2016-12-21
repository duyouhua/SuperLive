package com.joytouch.superlive.utils.city;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 5/25 0025.
 */
public class ShopPicker extends LinearLayout {
    private Context context;
    private List<provinceinfo> province_list = new ArrayList<provinceinfo>();
    private ArrayList<String> province_content = new ArrayList<String>();
    private ArrayList<String> province_id = new ArrayList<String>();

    private List<provinceinfo> city_map = new ArrayList();
    private ArrayList<String> city_content = new ArrayList();
    private ArrayList<String> city_id = new ArrayList();

    private List<provinceinfo> couny_map = new ArrayList<>();
    private ArrayList<String> qu_content = new ArrayList<>();
    private ArrayList<String> qu_id = new ArrayList<>();

    private CitycodeUtil citycodeUtil;
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker counyPicker;
    /** 选择监听 */
    private CityPicker.OnSelectingListener onSelectingListener;
    /** 临时日期 */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempCounyIndex = -1;
    /** 刷新界面 */
    private static final int REFRESH_VIEW = 0x001;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
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
    public ShopPicker(Context context) {
        super(context);
        this.context = context;
        getaddressinfo();
    }


    public ShopPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getaddressinfo();
        // TODO Auto-generated constructor stub
    }

    public ShopPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // 获取城市信息
    private void getaddressinfo() {
        // TODO Auto-generated method stub
        // 读取城市信息string
        String area_str = File_Util.readAssets(context, "area.json");
        Gson gson=new Gson();
        cityprovienceInfo info=gson.fromJson(area_str, cityprovienceInfo.class);

        province_list = info.province;
        city_map = info.city;
        couny_map =info.qu;

        for (int i=0;i<province_list.size();i++){
            province_content.add(province_list.get(i).area_name);
            province_id.add(province_list.get(i).area_id);
        }
        for (int k=0;k<city_map.size();k++){
            city_content.add(city_map.get(k).area_name);
            city_id.add(city_map.get(k).area_id);
        }
        for (int j=0;j<couny_map.size();j++){
            qu_content.add(couny_map.get(j).area_name);
            qu_id.add(couny_map.get(j).area_id);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_picker, this);
        citycodeUtil = CitycodeUtil.getSingleton();
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city);
        counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny);

        ArrayList citycontent = new ArrayList<>();
        ArrayList cityid = new ArrayList<>();
        for (int i = 0; i < city_map.size(); i++) {
            if (city_map.get(i).area_parent_id.equals(province_list.get(0).area_id)) {
                citycontent.add(city_map.get(i).area_name);
                cityid.add(city_map.get(i).area_id);
            }
        }
        ArrayList qucontent = new ArrayList<>();
        ArrayList quutyid = new ArrayList<>();
        for (int i = 0; i < city_map.size(); i++) {
            if (couny_map.get(i).area_parent_id.equals(cityid.get(0))) {
                qucontent.add(couny_map.get(i).area_name);
                quutyid.add(couny_map.get(i).area_id);
            }
        }
        //设置三个滚轮的数据源和默认位置
        provincePicker.setData(province_content,province_id);
        provincePicker.setDefault(0);
        cityPicker.setData(citycontent,cityid);
        cityPicker.setDefault(0);
        counyPicker.setData(qucontent,quutyid);
        counyPicker.setDefault(1);

        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text, String areaid) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (tempProvinceIndex != id) {
                    String selectDay = cityPicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
//                    Log.e("省级测试2",selectDay+"___"+selectMonth+"___"+tempProvinceIndex);
                    // 城市数组
                    ArrayList city_sel_content = new ArrayList<>();
                    ArrayList city_sel_id = new ArrayList<>();
                    for (int i = 0; i < city_map.size(); i++) {
                        if (city_map.get(i).area_parent_id.equals(areaid)) {
                            city_sel_content.add(city_map.get(i).area_name);
                            city_sel_id.add(city_map.get(i).area_id);
                        }
                    }
                    cityPicker.setData(city_sel_content, city_sel_id);
                    cityPicker.setDefault(0);
                    // 区级数组
                    ArrayList quc = new ArrayList<>();
                    ArrayList quuid = new ArrayList<>();
                    for (int i = 0; i < couny_map.size(); i++) {
                        if (couny_map.get(i).area_parent_id.equals(city_sel_id.get(0))) {
                            quc.add(couny_map.get(i).area_name);
                            quuid.add(couny_map.get(i).area_id);
                        }
                    }
                    counyPicker.setData(quc, quuid);
                    counyPicker.setDefault(1);

                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new                    Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text, String areaid) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (temCityIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = counyPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;

                    // 区级数组
                    ArrayList qu_sel_content = new ArrayList<>();
                    ArrayList qu_sel_id = new ArrayList<>();
                    for (int i = 0; i < couny_map.size(); i++) {
                        if (couny_map.get(i).area_parent_id.equals(areaid)) {
                            qu_sel_content.add(couny_map.get(i).area_name);
                            qu_sel_id.add(couny_map.get(i).area_id);
                        }
                    }
                    counyPicker.setData(qu_sel_content, qu_sel_id);
                    counyPicker.setDefault(1);
                    int lastDay = Integer.valueOf(cityPicker.getListSize());
                    if (id > lastDay) {
                        cityPicker.setDefault(lastDay - 1);
                    }
                }
                temCityIndex = id;

                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text,String areaid) {
                // TODO Auto-generated method stub
                if (text.equals("") || text == null)
                    return;
                if (tempCounyIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = cityPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    int lastDay = Integer.valueOf(counyPicker.getListSize());
                    if (id > lastDay) {
                        counyPicker.setDefault(lastDay - 1);
                    }
                }
                tempCounyIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });

    }
    public String getCity_string() {
        city_string =provincePicker.getSelectedid();
        return city_string;
    }
    public String getPrivence_string() {
        city_string =cityPicker.getSelectedid();
        return city_string;
    }
    public String getQu_string() {
        city_string =counyPicker.getSelectedid();
        return city_string;
    }
    public String getaddress() {
        city_string =provincePicker.getSelectedText()+ cityPicker.getSelectedText() + counyPicker.getSelectedText();
        return city_string;
    }
}
