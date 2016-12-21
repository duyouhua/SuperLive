package com.joytouch.superlive.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.messagelistAll;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 5/28 0028.
 */
public class SystemAdapter extends AppBaseAdapter<messagelistAll>{
    private final List<messagelistAll> list;
    private final Context context;
    private final SharedPreferences sp;
    private final String time;

    public SystemAdapter(List<messagelistAll> list, Context context, String time) {
        super(list, context);
        this.list=list;
        this.context=context;
        this.time=time;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.system_info,null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.time_ok = (TextView) convertView.findViewById(R.id.time_ok);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final messagelistAll info=list.get(position);
        holder.time_ok.setText(time);
        ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/" +info.getImg_url(), holder.img, ImageLoaderOption.optionsHeader);
        holder.text.setText(info.getText());
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uri = Uri.parse(info.getIntent_url());
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(it);
            }
        });
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uri = Uri.parse(info.getIntent_url());
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(it);
            }
        });

        if (!info.getImg_url().equals("")){
            holder.img.setVisibility(View.VISIBLE);
            holder.text.setVisibility(View.GONE);
        }else{
            holder.img.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
    class ViewHolder{
        ImageView img;
        TextView time_ok;
        TextView text;
    }
}
