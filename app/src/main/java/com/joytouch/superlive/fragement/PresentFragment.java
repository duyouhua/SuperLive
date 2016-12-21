package com.joytouch.superlive.fragement;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.joytouch.superlive.R;

/**
 * Created by Administrator on 7/15 0015.
 */
public class PresentFragment extends Fragment implements View.OnClickListener {
    private String type;
    private DisplayMetrics dm;
    private View view;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.present_item,null);
        initView();
//        PresentFragment videoFragment = new PresentFragment();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.present_viewpager, videoFragment).commit();
        return view;
    }

    private void initView() {
        tv_1= (TextView) view.findViewById(R.id.tv_1);
        tv_2=(TextView)view.findViewById(R.id.tv_2);
        tv_3=(TextView)view.findViewById(R.id.tv_3);
        tv_4=(TextView)view.findViewById(R.id.tv_4);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);
        if (type.equals("0")){
            tv_1.setText("1");
            tv_2.setText("2");
            tv_3.setText("3");
            tv_4.setText("4");
            tv_1.setTag("1");
            tv_2.setTag("2");
            tv_3.setTag("3");
            tv_4.setTag("4");
        }else if (type.equals("1")){
            tv_1.setText("5");
            tv_2.setText("6");
            tv_3.setText("7");
            tv_4.setText("8");
            tv_1.setTag("5");
            tv_2.setTag("6");
            tv_3.setTag("7");
            tv_4.setTag("8");
        }else if (type.equals("2")){
            tv_1.setText("9");
            tv_2.setText("10");
            tv_3.setText("11");
            tv_4.setText("12");
            tv_1.setTag("9");
            tv_2.setTag("10");
            tv_3.setTag("11");
            tv_4.setTag("12");
        }else if (type.equals("3")){
            tv_1.setText("13");
            tv_2.setText("14");
            tv_3.setText("15");
            tv_4.setText("16");
            tv_1.setTag("13");
            tv_2.setTag("14");
            tv_3.setTag("15");
            tv_4.setTag("16");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_1:
                Log.e("Tag::", tv_1.getTag() + "");
                break;
            case R.id.tv_2:
                Log.e("Tag::", tv_2.getTag() + "");
                break;
            case R.id.tv_3:
                Log.e("Tag::", tv_3.getTag() + "");
                break;
            case R.id.tv_4:
                Log.e("Tag::", tv_4.getTag() + "");
                break;
        }
    }
}
