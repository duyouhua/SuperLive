package com.joytouch.superlive.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.ChatActivity;
import com.joytouch.superlive.activity.LiveDetailActivity;
import com.joytouch.superlive.activity.SyatemInfoActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.messagelist;
import com.joytouch.superlive.javabean.messagelistAll;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.TimeUtil;
import com.joytouch.superlive.utils.xutil.XUtil;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by sks on 2016/4/7.
 */
public class MessageAdapter extends AppBaseAdapter<messagelist> {
    private final DbManager db2;
    private final SharedPreferences sp;
    private final DbManager db_all;
    private List<messagelist> messages;
    private Context context;
    private Dialog dialog;

    public MessageAdapter(List<messagelist> list, Context context) {
        super(list, context);
        this.messages = list;
        this.context = context;
        sp = context.getSharedPreferences(Preference.preference,
                Context.MODE_PRIVATE);
        //创建一个表存储需要在列表中填充数据的表   MessageTable
        DbManager.DaoConfig daoConfig_= XUtil.getMessageListDaoConfig();
        db2 = x.getDb(daoConfig_);

        DbManager.DaoConfig daoConfig_all_= XUtil.getMessageList();
        db_all = x.getDb(daoConfig_all_);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_message,null);
            holder = new ViewHolder();
            holder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            holder.iv_point = (ImageView) convertView.findViewById(R.id.iv_red_point);
            holder.iv_level = (ImageView) convertView.findViewById(R.id.iv_level);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.isRoom= (ImageView) convertView.findViewById(R.id.isRoom);
            holder.tv_message=(TextView)convertView.findViewById(R.id.tv_message);
            holder.ll_level=(LinearLayout)convertView.findViewById(R.id.ll_level);
            holder.level=(TextView)convertView.findViewById(R.id.anchor_fans);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final messagelist info=list.get(position);
        ImageLoader.getInstance().displayImage(Preference.img_url + "200x200/" + info.getImage(), holder.iv_icon, ImageLoaderOption.optionsHeader);
        holder.tv_time.setText(TimeUtil.currentLocalTimeString(info.getTime()).substring(5, TimeUtil.currentLocalTimeString(info.getTime()).length()));
        holder.tv_name.setText(info.getNickname());
        holder.tv_message.setText(info.getContent());
//        holder.level.setText(info.getLevel());
        ConfigUtils.level(holder.level,info.getLevel());
        if (info.getType().equals("1")){//粉丝
            holder.tv_name.setTextColor(Color.parseColor("#333333"));
            holder.isRoom.setVisibility(View.GONE);
            holder.ll_level.setVisibility(View.VISIBLE);
        }else if (info.getType().equals("2")){//正常私聊
            holder.tv_name.setTextColor(Color.parseColor("#333333"));
            holder.isRoom.setVisibility(View.GONE);
            holder.ll_level.setVisibility(View.VISIBLE);
        }else if (info.getType().equals("3")){//通知
            holder.tv_name.setText("系统消息");
            holder.tv_name.setTextColor(Color.parseColor("#333333"));
            holder.isRoom.setVisibility(View.GONE);
            holder.ll_level.setVisibility(View.GONE);
        }else if (info.getType().equals("4")){//分享
            holder.tv_name.setTextColor(Color.parseColor("#333333"));
            holder.isRoom.setVisibility(View.VISIBLE);
            holder.ll_level.setVisibility(View.VISIBLE);
            holder.tv_message.setText(info.getNickname() + "欢迎你进入房间");
        }else{
            holder.tv_name.setTextColor(Color.parseColor("#333333"));
            holder.isRoom.setVisibility(View.GONE);
            holder.ll_level.setVisibility(View.VISIBLE);
        }

        if (info.getStatus().equals("1")){//已看过
            holder.iv_point.setVisibility(View.GONE);
        }else{
            holder.iv_point.setVisibility(View.VISIBLE);
        }
        final ViewHolder finalHolder = holder;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果是1,2跳转到聊天室
                if (info.getType().equals("1") || info.getType().equals("2")){
                    Intent intent=new Intent(context,ChatActivity.class);
                    intent.putExtra("otherid",info.getUserid());
                    intent.putExtra("othernickname",info.getNickname());
                    intent.putExtra("otherimg", info.getImage());
                    intent.putExtra("other_level",info.getLevel());
                    if ("0".equals(info.getStatus())){
                        sp.edit().putInt("hongdian", sp.getInt("hongdian",0)-1).commit();
                    }
                    finalHolder.iv_point.setVisibility(View.GONE);
                    context.startActivity(intent);
                }
                //4代表进入房间
                /**
                 适配表的数据状态改变,红点消失
                 findNormalID(otherid);
                 */
                if (info.getType().equals("4")){
                    final Dialog dialog4 = new Dialog(context, R.style.Dialog_bocop);
                    dialog4.setContentView(R.layout.intoroomdialog);
                    TextView close = (TextView) dialog4.findViewById(R.id.cancle);
                    TextView sucess = (TextView) dialog4.findViewById(R.id.sucess);
                    dialog4.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                        }
                    });
                    sucess.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, LiveDetailActivity.class);
                            intent.putExtra("romid", info.getRoom_id());
                            intent.putExtra("matchid", info.getMatch_id());
                            intent.putExtra("qiutanid", info.getQiutan_id());
                            intent.putExtra("normal", "1");
                            finalHolder.iv_point.setVisibility(View.GONE);
                            if ("0".equals(info.getStatus())) {
                                sp.edit().putInt("hongdian", sp.getInt("hongdian", 0) - 1).commit();
                            }
                            context.startActivity(intent);
                            updateadapterContent("1", info.getId());
                            dialog4.dismiss();
                        }
                    });
                    dialog4.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失dialog
                    dialog4.setCancelable(true);//点击返回键取消
                    dialog4.show();
                }
                //3代表进入通知界面
                /**
                 适配表的数据状态改变,红点消失
                 findNormalID(otherid);
                 */
                if (info.getType().equals("3")){
                    Intent intent=new Intent(context,SyatemInfoActivity.class);
                    intent.putExtra("time", TimeUtil.currentLocalTimeString(info.getTime()));
                    intent.putExtra("img_url",info.getImage());
                    intent.putExtra("content", info.getContent());
                    intent.putExtra("text",info.getText());
                    intent.putExtra("my_userid",info.getMy_userid());
                    intent.putExtra("other_userid",info.getUserid());
                    finalHolder.iv_point.setVisibility(View.GONE);
                    if ("0".equals(info.getStatus())){
                        sp.edit().putInt("hongdian", sp.getInt("hongdian",0)-1).commit();
                    }
                    context.startActivity(intent);
                    updatestatet();
                    updateadapterContent("1", info.getId());
                }
            }
        });
        return convertView;
    }
    private void updatestatet() {
        messagelistAll stu;
        try {
            List<messagelistAll> stus = db_all.findAll(messagelistAll.class);
            for (int k=0;k<stus.size()-1;k++){
                stu = stus.get(k);
                stu.setStatus("1");
                db_all.update(stu);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 查找适配表中是否存在我和好友的聊天数据normal类型
     */
    private int findNormalID(String type,String otheruserid) {

        int id = 0;
        try {
            List<messagelist> users = db2.selector(messagelist.class)
                    .where("my_userid", "=", sp.getString(Preference.myuser_id, ""))
                    .and("userid", "=", otheruserid)
                    .and("type", "=", type)
                    .findAll();
            if(users == null || users.size() == 0){

            } else{
                id=users.get(0).getId();
                //修改适配表的关于我和他的离线消息状态
                updateadapterContent("1",id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    /**
     * 更新聊天类型的适配表中存在的数据
     * 修改id为position+1的status
     */
    private void updateadapterContent(String status,int id) {
        try {
            List<messagelist> stus = db2.findAll(messagelist.class);
            messagelist stu = stus.get(id-1);
            stu.setStatus(status);
            db2.update(stu);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    class ViewHolder{
        CircleImageView iv_icon;
        ImageView iv_point,isRoom;
        TextView tv_name;
        ImageView iv_level;
        TextView tv_sign;
        TextView tv_time,tv_message,level;
        LinearLayout ll_level;
    }
}
