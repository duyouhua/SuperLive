package com.joytouch.superlive.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.adapter.GoldWaterAdapter;
import com.joytouch.superlive.javabean.GoldWater;
import com.joytouch.superlive.javabean.GoldWaterGroup;
import com.joytouch.superlive.widget.GoldDetailsDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yj on 2016/4/11.
 */
public class Goldwater extends BaseFragment implements ExpandableListView.OnChildClickListener,ExpandableListView.OnGroupClickListener{
    private View view;
    private ExpandableListView expandableListView;
    private List<GoldWaterGroup> groups;
    private GoldWaterAdapter goldWaterAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goldwater,null);
        initView();
        return view;
    }

    private void initView() {
        getDate();
        expandableListView = (ExpandableListView) view.findViewById(R.id.elv_goldwater);
        goldWaterAdapter = new GoldWaterAdapter(groups,getActivity());
        expandableListView.setAdapter(goldWaterAdapter);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
    }
    private void getDate(){
        groups = new ArrayList<>();
        for (int i = 0;i<3;i++){
            GoldWaterGroup goldWaterGroup = new GoldWaterGroup();
            ArrayList<GoldWater> goldWaters = new ArrayList<>();
            for (int j=0;j<5;j++){
                goldWaters.add(new GoldWater());
            }
            goldWaterGroup.setGoldWaters(goldWaters);
            groups.add(goldWaterGroup);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        GoldDetailsDialog dialog = new GoldDetailsDialog(getActivity());
        dialog.show();
        return false;
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (parent.isGroupExpanded(groupPosition)){
            ((ImageView)v.findViewById(R.id.iv_up)).setBackgroundResource(R.drawable.down_water);
        }else {
            ((ImageView)v.findViewById(R.id.iv_up)).setBackgroundResource(R.drawable.up_water);
        }
        return false;
    }
}
