package com.joytouch.superlive.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.joytouch.superlive.R;
import com.joytouch.superlive.javabean.AnchorInfo;

import java.util.List;

/**
 * Created by yj on 2016/4/13.
 */
public class FullScreenAchorAdapter extends AppBaseAdapter<AnchorInfo> {
    public FullScreenAchorAdapter(List<AnchorInfo> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_fullscreen_anchorinfo,null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    public class ViewHolder{

    }
}
