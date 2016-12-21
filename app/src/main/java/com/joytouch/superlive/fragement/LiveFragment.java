package com.joytouch.superlive.fragement;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.joytouch.superlive.R;

/**
 * Created by yj on 2016/4/7.
 */
public class LiveFragment extends Fragment{
    private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragmnet_live,null);
        return view;
    }
}
