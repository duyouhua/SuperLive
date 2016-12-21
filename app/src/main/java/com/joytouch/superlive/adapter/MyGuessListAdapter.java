package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.MyGuess;

import java.util.List;

/**
 * Created by sks on 2016/4/8.
 */
public class MyGuessListAdapter extends BaseExpandableListAdapter {
    private List<MyGuess> myGuesses;
    private Context context;

    public MyGuessListAdapter(List<MyGuess> myGuesses, Context context) {
        this.myGuesses = myGuesses;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return myGuesses.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return myGuesses.get(groupPosition).getGuesses().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return myGuesses.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return myGuesses.get(groupPosition).getGuesses().get(childPosition);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_myguess_top,null);
            holder = new GroupViewHolder();
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_score = (TextView) convertView.findViewById(R.id.tv_score);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder cholder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_myguess,null);
            cholder= new ChildViewHolder();
            cholder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            cholder.tv_gold = (TextView) convertView.findViewById(R.id.tv_gold);
            cholder.tv_result = (TextView) convertView.findViewById(R.id.tv_result);
            cholder.iv_isWin = (ImageView) convertView.findViewById(R.id.iv_isWin);
            cholder.rl_divider = (RelativeLayout) convertView.findViewById(R.id.rl_divider);
            convertView.setTag(cholder);
        }else {
            cholder = (ChildViewHolder) convertView.getTag();
        }
        if (childPosition == myGuesses.get(groupPosition).getGuesses().size()-1){
            cholder.rl_divider.setVisibility(View.GONE);
        }else {
            cholder.rl_divider.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class GroupViewHolder{
        TextView tv_time;
        TextView tv_name;
        TextView tv_score;
    }
    class ChildViewHolder{
        TextView tv_content;
        TextView tv_gold;
        TextView tv_result;
        ImageView iv_isWin;
        RelativeLayout rl_divider;
    }
}
