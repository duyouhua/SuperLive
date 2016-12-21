package com.joytouch.superlive.activity;

import android.os.Bundle;
import android.util.Log;
import com.joytouch.superlive.R;
import com.joytouch.superlive.utils.city.CityPicker;
/**
 * Created by Administrator on 5/24 0024.
 */
public class addviceSelectActivity extends BaseActivity{
    private CityPicker cityPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //引用资源
        this.getClass().getClassLoader().getResourceAsStream("assets/"+"area");
        setContentView(R.layout.city_pick);

        cityPicker = (CityPicker) findViewById(R.id.citypicker);
        cityPicker.setOnSelectingListener(new CityPicker.OnSelectingListener() {
            @Override
            public void selected(boolean selected) {

                Log.e("city",  cityPicker.getCity_string());
            }
        });
    }
}
