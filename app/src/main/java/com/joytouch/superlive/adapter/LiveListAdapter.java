package com.joytouch.superlive.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.broadcast.V4_AlarmReceiver;
import com.joytouch.superlive.javabean.LiveMatchInfoJavabean;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.TimeUtil;
import com.joytouch.superlive.widget.CircleImageView;
import com.joytouch.superlive.widget.stickylistheaders.StickyListHeadersAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yj on 2016/4/8.
 */
public class LiveListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private  SharedPreferences sp;
    private SharedPreferences.Editor edit;
    private ArrayList<LiveMatchInfoJavabean> item;
    private Context context;
    public LayoutInflater inflater;
    private boolean b;

    public LiveListAdapter(Context context,ArrayList<LiveMatchInfoJavabean> item) {
        this.item = item;
        this.context = context;
        if(context!=null) {
            inflater = LayoutInflater.from(context);
            sp = context.getSharedPreferences(Preference.prefernce_alarms, Context.MODE_PRIVATE);
            edit = sp.edit();
        }

    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_livelist_group,null);
            holder = new GroupViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        if("1".equals(item.get(position).getMatchTime().getToday())){
            holder.tv.setVisibility(View.GONE);
        }else {
            holder.tv.setVisibility(View.VISIBLE);
            holder.tv.setText(item.get(position).getMatchTime().getTime());
            LogUtils.e("sssssssss@ssss",item.get(position).getMatchTime().getTime()+"     "+position);
        }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return item.get(position).getMatchTime().getTimeLong();
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ChildViewHolder holder;
        if(view == null){
            view = inflater.inflate(R.layout.adapter_livelist_child,null);
            holder = new ChildViewHolder();
            holder.nonewsll = (LinearLayout) view.findViewById(R.id.nonews);
            holder.teamnamerl = (RelativeLayout) view.findViewById(R.id.team_name_rl);
            holder.team1 = (TextView) view.findViewById(R.id.team1_name);
            holder.team2 = (TextView) view.findViewById(R.id.team2_name);
            holder.startTime = (TextView) view.findViewById(R.id.starttime);
            holder.matchVS = (RelativeLayout) view.findViewById(R.id.matchid_rl);
            holder.team1logo = (CircleImageView) view.findViewById(R.id.team1_logo);
            holder.team2logo = (ImageView) view.findViewById(R.id.team2_logo);
            holder.matchnostart = (LinearLayout) view.findViewById(R.id.match_nostart);
            holder.alarm = (CheckBox) view.findViewById(R.id.match_alarm);
            holder.matchstart = (LinearLayout) view.findViewById(R.id.match_start);
            holder.score = (TextView) view.findViewById(R.id.score);
            holder.matchState = (TextView) view.findViewById(R.id.state);
            holder.otherMatch = (LinearLayout) view.findViewById(R.id.nomatchid);
            holder.otherMatchLogo = (ImageView) view.findViewById(R.id.other_logo);
            holder.othername = (TextView) view.findViewById(R.id.matchname);
            holder.othergroup = (TextView) view.findViewById(R.id.matchgroup);
            holder.otheralarm = (CheckBox) view.findViewById(R.id.other_alarm);
            holder.otheralarmll = (LinearLayout) view.findViewById(R.id.other_alarm_ll);
            holder.online = (TextView) view.findViewById(R.id.online);
            holder.leaguename = (TextView) view.findViewById(R.id.match);
            holder.newsll = (LinearLayout) view.findViewById(R.id.news);
            holder.newslogo = (ImageView) view.findViewById(R.id.news_logo);
            holder.newsname = (TextView) view.findViewById(R.id.news_name);
            holder.newstime = (TextView) view.findViewById(R.id.news_time);
            holder.newsonline = (TextView) view.findViewById(R.id.news_online);
            holder.newsalarm = (CheckBox) view.findViewById(R.id.news_alarm);
            holder.starting = (ImageView) view.findViewById(R.id.other_starting);
            holder.newStartLL = (LinearLayout) view.findViewById(R.id.new_ll);
            holder.newState = (TextView) view.findViewById(R.id.newstate);
            holder.rward = (LinearLayout) view.findViewById(R.id.reward);
            holder.subsidies = (LinearLayout) view.findViewById(R.id.subsidies);
            holder.red = (ImageView) view.findViewById(R.id.red);
            view.setTag(holder);
        }else {
            holder = (ChildViewHolder) view.getTag();
        }
        LiveMatchInfoJavabean bean = item.get(i);
        String ss = "aa" + bean.getMatchId();
        final long matchtime = bean.getStartTime()*1000;
        if(bean.isReward()){
            holder.rward.setVisibility(View.VISIBLE);
        }else {
            holder.rward.setVisibility(View.GONE);
        }
        if(bean.isSubsidies()){
            holder.subsidies.setVisibility(View.VISIBLE);
        }else {
            holder.subsidies.setVisibility(View.GONE);
        }
        if(bean.isred()){
            holder.red.setVisibility(View.VISIBLE);
        }else {
            holder.red.setVisibility(View.GONE);
        }
        //判断是否是新闻类型的
        if("3".equals(bean.getType())){

                holder.newsll.setVisibility(View.VISIBLE);
                holder.nonewsll.setVisibility(View.GONE);
                holder.newsname.setText(bean.getMatchNameBottom());
                holder.newsonline.setText(bean.getOnLine()+"人");
                if("1".equals(bean.getMatchTime().getToday())) {
                    holder.newstime.setText(bean.getTime().substring(3));
                }else{
                    holder.newstime.setText(bean.getTime());
                }
            if("1".equals(bean.getMatchStatus())){
                holder.newsalarm.setVisibility(View.VISIBLE);
                holder.starting.setVisibility(View.GONE);
            }else {
                holder.newsalarm.setVisibility(View.GONE);
                holder.starting.setVisibility(View.VISIBLE);
            }
            LogUtils.e("------------------------", ""+Preference.photourl+"260x120/"+bean.getCat_imag());
            String imageurl = Preference.photourl+"260x120/"+bean.getCat_imag();
                ImageLoader.getInstance().displayImage(imageurl, holder.newslogo, ImageLoaderOption.optionsnewteamLogo);
            holder.newsalarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alarm(matchtime, i, holder.newsalarm, item.get(i).getMatchNameBottom());
                    }
                });

            if ((matchtime - 5 * 60 * 1000) < System.currentTimeMillis() ) {
                holder.newsalarm.setEnabled(false);
            }else{
                holder.newsalarm.setEnabled(true);
            }
            holder.newsalarm.setChecked(sp.getBoolean(ss, false));
        }else {
            holder.newsll.setVisibility(View.GONE);
            holder.nonewsll.setVisibility(View.VISIBLE);
            holder.startTime.setText(bean.getTime());
            holder.online.setText(bean.getOnLine()+"人");
            holder.leaguename.setText(bean.getClassification()+"   "+bean.getMatchGroup());
            //判断是否是对阵
            if("1".equals(bean.getType())){
                holder.team1.setVisibility(View.VISIBLE);
                holder.team2.setVisibility(View.VISIBLE);
                holder.matchVS.setVisibility(View.VISIBLE);
                holder.otherMatch.setVisibility(View.GONE);
                holder.teamnamerl.setVisibility(View.VISIBLE);
                holder.team1.setVisibility(View.VISIBLE);
                holder.team2.setVisibility(View.VISIBLE);
                holder.team1.setText(bean.getTeama1Name());
                holder.team2.setText(bean.getTeama2Name());
                ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/" + bean.getTeama1Logo(), holder.team1logo, ImageLoaderOption.optionsteamLogo);
                ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/"+bean.getTeama2Logo(),holder.team2logo, ImageLoaderOption.optionsteamLogo);
                    LogUtils.e("=======",bean.getMatchStatus());
                    if ("1".equals(bean.getMatchStatus())) {
                        holder.matchnostart.setVisibility(View.VISIBLE);
                        holder.matchstart.setVisibility(View.GONE);
                    } else {
                        LogUtils.e("=======","-----------");
                        holder.matchnostart.setVisibility(View.GONE);
                        holder.matchstart.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(bean.getScore())) {
                            holder.score.setVisibility(View.VISIBLE);
                            holder.score.setText(bean.getScore());
                        } else {
                            holder.score.setVisibility(View.GONE);
                        }
                        if(!TextUtils.isEmpty(bean.getStating())) {
                            holder.matchState.setText(bean.getStating());
                            holder.newState.setText(bean.getStating());
                        }else{
                            holder.matchState.setText("LIVE");
                            holder.newState.setText("LIVE" );
                        }
                    }

                holder.alarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alarm(matchtime, i, holder.alarm, item.get(i).getTeama1Name() + "VS" + item.get(i).getTeama2Name());
                    }
                });
                LogUtils.e("sssss", "=========");
                //判断是都设置了提醒功能
                if ((matchtime - 5 * 60 * 1000) < System.currentTimeMillis() ) {
                    holder.alarm.setEnabled(false);
                }else{
                    holder.alarm.setEnabled(true);
                }
                holder.alarm.setChecked(sp.getBoolean(ss, false));
            }
            if("2".equals(bean.getType())){
                holder.team1.setVisibility(View.GONE);
                holder.team2.setVisibility(View.GONE);
                holder.matchVS.setVisibility(View.GONE);
                holder.teamnamerl.setVisibility(View.VISIBLE);
                holder.team1.setVisibility(View.GONE);
                holder.team2.setVisibility(View.GONE);
                holder.otherMatch.setVisibility(View.VISIBLE);
                if(TextUtils.isEmpty(bean.getMatchNametop())){
                    holder.othername.setVisibility(View.GONE);
                    holder.othergroup.setText(bean.getMatchNameBottom());
                }else{
                    holder.othername.setVisibility(View.VISIBLE);
                    holder.othergroup.setText(bean.getMatchNameBottom());
                    holder.othername.setText(bean.getMatchNametop());
                }
                LogUtils.e("------------------------", ""+Preference.photourl+"100x100/"+bean.getLeague_imag());
                ImageLoader.getInstance().displayImage(Preference.photourl + "100x100/"+bean.getLeague_imag(),holder.otherMatchLogo, ImageLoaderOption.optionsteamLogo);
                if("1".equals(bean.getMatchStatus())){
                    holder.otheralarmll.setVisibility(View.VISIBLE);
                    holder.newStartLL.setVisibility(View.GONE);
                }else {
                    holder.otheralarmll.setVisibility(View.GONE);
                    holder.newStartLL.setVisibility(View.VISIBLE);

                }
                holder.otheralarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alarm(matchtime,i,holder.otheralarm,item.get(i).getMatchNameBottom());
                    }
                });
                if ((matchtime - 5 * 60 * 1000) < System.currentTimeMillis() ) {
                    holder.otheralarm.setEnabled(false);
                }else{
                    holder.otheralarm.setEnabled(true);
                }
                holder.otheralarm.setChecked(sp.getBoolean(ss, false));

            }
        }

        return view;
    }



    public class GroupViewHolder{
        TextView tv;
    }
    public class ChildViewHolder{

        private LinearLayout nonewsll;
        private RelativeLayout teamnamerl;
        private TextView team1;
        private TextView team2;
        private TextView startTime;
        private RelativeLayout matchVS;
        private CircleImageView team1logo;
        private ImageView team2logo;
        private LinearLayout matchnostart;
        private CheckBox alarm;
        private LinearLayout matchstart;
        private TextView score;
        private TextView matchState;
        private LinearLayout otherMatch;
        private ImageView otherMatchLogo;
        private TextView othername;
        private TextView othergroup;
        private CheckBox otheralarm;
        private LinearLayout otheralarmll;
        private TextView online;
        private TextView leaguename;
        private ImageView starting;

        private LinearLayout newsll;
        private ImageView newslogo;
        private TextView newsname;
        private TextView newstime;
        private TextView newsonline;
        private CheckBox newsalarm;
        private LinearLayout newStartLL;
        private TextView newState;
        private LinearLayout rward;
        private LinearLayout subsidies;
        private ImageView red;


    }

    public void alarm(long matchtime,int position,CheckBox view,String matchname){
        b = sp.getBoolean("aa" + item.get(position).getMatchId(), false);
        LogUtils.e("-----------",matchtime +"");
        LogUtils.e("-----------",(matchtime - 5 * 60 * 1000)+"");
        if ((matchtime - 5 * 60 * 1000) < System.currentTimeMillis() ) {
            view.setEnabled(false);
            view.setChecked(sp.getBoolean("aa" + item.get(position).getMatchId(), false));
            return;
        }


        LogUtils.e("onclick", "" + b);
        if (!b) {
            b = true;
            LogUtils.e("=============", "" + position);
            String ss = "aa" + item.get(position).getMatchId();
            edit.putBoolean(ss, true);
            edit.putString("name"+item.get(position).getMatchId(),matchname);
            edit.commit();
            //添加闹铃提醒
            Intent intent = new Intent(context, V4_AlarmReceiver.class);
            intent.setAction(item.get(position).getMatchId());
            LogUtils.e("==============", "" + item.get(position).getMatchId());
            intent.putExtra("roomid", item.get(position).getRoomid());
            intent.putExtra("notionid", matchtime - 5 * 60 * 1000+position);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            LogUtils.e("=====time===", "" + TimeUtil.getTimeString((matchtime - 5 * 60 * 1000), "yyyy-MM-dd HH:mm:ss"));
            alarm.set(AlarmManager.RTC_WAKEUP, (matchtime - 5 * 60 * 1000), pendingIntent);
        } else {
            b = false;
            edit.remove("aa" + item.get(position).getMatchId());
            edit.remove("name"+item.get(position).getMatchId());
            edit.commit();
            LogUtils.e("=======================", "" + sp.getBoolean(item.get(position).getMatchId(), false));
            //取消闹铃提醒
            Intent intent = new Intent(context, V4_AlarmReceiver.class);
            intent.setAction(item.get(position).getMatchId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            alarm.cancel(pendingIntent);
        }
    }
}
