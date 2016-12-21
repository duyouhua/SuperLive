package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.GoldWaterGroup;
import com.joytouch.superlive.widget.CircleImageView;

import java.util.List;

/**
 * Created by sks on 2016/4/18.
 */
public class GoldWaterAdapter extends BaseExpandableListAdapter {
    private List<GoldWaterGroup> groups;
    private Context context;

    public GoldWaterAdapter(List<GoldWaterGroup> groups, Context context) {
        this.groups = groups;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getGoldWaters().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).getGoldWaters().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_goldwater_group,null);
            holder = new GroupViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_gold = (TextView) convertView.findViewById(R.id.tv_gold);
            holder.iv_up = (ImageView) convertView.findViewById(R.id.iv_up);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gold_child,null);
            holder = new ChildViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.tv_gold = (TextView) convertView.findViewById(R.id.tv_gold);
            convertView.setTag(holder);
        }else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class GroupViewHolder{
        TextView tv_name;
        TextView tv_gold;
        ImageView iv_up;
    }
    class ChildViewHolder{
        CircleImageView iv_icon;
        TextView tv_name;
        TextView tv_sign;
        TextView tv_gold;
    }
}
