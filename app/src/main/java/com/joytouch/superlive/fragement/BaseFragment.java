package com.joytouch.superlive.fragement;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.joytouch.superlive.app.Preference;

/**
 * Created by yj on 2016/4/11.
 */
public class BaseFragment extends Fragment {
    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    public void onResume() {
        super.onResume();
//        HomeAppstateutils.JiantingHome(getActivity());
    }
}
