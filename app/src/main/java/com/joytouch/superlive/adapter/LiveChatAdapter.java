package com.joytouch.superlive.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.joytouch.superlive.R;
import com.joytouch.superlive.activity.LotteryDetailsActivity;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.interfaces.FullScreenListener;
import com.joytouch.superlive.javabean.ChatInfo;
import com.joytouch.superlive.javabean.SendLotteryResult;
import com.joytouch.superlive.javabean.TableChatLottery;
import com.joytouch.superlive.utils.ConfigUtils;
import com.joytouch.superlive.utils.DBUtils;
import com.joytouch.superlive.utils.ImageLoaderOption;
import com.joytouch.superlive.utils.SPUtils;
import com.joytouch.superlive.widget.AnchorInfroDialog;
import com.joytouch.superlive.widget.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yj on 2016/5/3.
 * 直播详情聊天内容
 */
public class LiveChatAdapter extends AppBaseAdapter<ChatInfo>{
    public LiveChatAdapter(List<ChatInfo> list, Context context) {
        super(list, context);
    }

    @Override
    public View createView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.adapter_chat,null);
            holder = new ViewHolder();
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.lottery = (LinearLayout) convertView.findViewById(R.id.lottery);
            holder.lotterySendPeople = (TextView) convertView.findViewById(R.id.lotterysenduser);
            holder.isOdds = (TextView) convertView.findViewById(R.id.isodds);
            holder.normalLottery = (RelativeLayout) convertView.findViewById(R.id.normalLottery);
            holder.normalLotteryTitle = (TextView) convertView.findViewById(R.id.lottery_title);
            holder.normalanswerLeft = (Button) convertView.findViewById(R.id.normalleft);
            holder.normalanserRight = (Button) convertView.findViewById(R.id.normalrRight);
            holder.normalStopTime = (TextView) convertView.findViewById(R.id.normalStopTime);
            holder.normalDetail = (TextView) convertView.findViewById(R.id.normalDetail);
            holder.normalContainerLeft = (RelativeLayout) convertView.findViewById(R.id.container_left);
            holder.normalContainerRight = (RelativeLayout) convertView.findViewById(R.id.container_right);
            holder.oddsLottery = (RelativeLayout) convertView.findViewById(R.id.VSLottery);
            holder.oddsLotteryTitle = (TextView) convertView.findViewById(R.id.lottery_vs_title);
            holder.oddsanswerLeft = (Button) convertView.findViewById(R.id.vsleft);
            holder.oddsanswerRight = (Button) convertView.findViewById(R.id.vsright);
            holder.oddsStoptime = (TextView) convertView.findViewById(R.id.vsStopTime);
            holder.odds = (TextView) convertView.findViewById(R.id.vsodds);
            holder.oddsDetail = (TextView) convertView.findViewById(R.id.vsStopDetail);
            holder.oddsContainerLeft = (RelativeLayout) convertView.findViewById(R.id.vs_container_left);
            holder.oddscontainerRight = (RelativeLayout) convertView.findViewById(R.id.vs_container_right);
            holder.oddsLeftHeader = (CircleImageView) convertView.findViewById(R.id.vs_header_left);
            holder.oddsRightHeader = (CircleImageView) convertView.findViewById(R.id.vs_header_right);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ChatInfo info = list.get(position);
        holder.content.setEnabled(false);
        //判断是否是竞猜
        if("9".equals(info.getType())){
            holder.lottery.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
            holder.normalanswerLeft.setText(info.getAnswerLeft());
            holder.normalanserRight.setText(info.getAnswerRight());
            holder.normalLotteryTitle.setText(info.getTitle());
            holder.normalStopTime.setText(info.getStoptime() + "截止");
            holder.oddsanswerRight.setText(info.getAnswerRight());
            holder.oddsanswerLeft.setText(info.getAnswerLeft());
            holder.odds.setText(info.getOddsCount());
            holder.oddsStoptime.setText(info.getStoptime()+"截止");
            holder.lotterySendPeople.setText(info.getUsername()+"(管理员)：");

            if(info.isOdds()){
                holder.oddsLottery.setVisibility(View.VISIBLE);
                holder.normalLottery.setVisibility(View.GONE);
                holder.oddsanswerLeft.setEnabled(true);
                holder.oddsanswerRight.setEnabled(true);
                holder.oddsLeftHeader.setVisibility(View.GONE);
                holder.oddsRightHeader.setVisibility(View.GONE);
                holder.isOdds.setVisibility(View.VISIBLE);
                //判断是否自己选择
                if(!TextUtils.isEmpty(info.getAnswer())){
                    if("1".equals(info.getAnswer())){
                        holder.oddsanswerLeft.setEnabled(false);
                    }else {
                        holder.oddsanswerRight.setEnabled(false);
                    }
                }
                //判断是否已有擂主
                if(!TextUtils.isEmpty(info.getOddAnswer())) {
                    //判断擂主在哪边

                    if ("0".equals(info.getOddAnswer())) {
                        holder.oddsLeftHeader.setVisibility(View.VISIBLE);
                        if(TextUtils.isEmpty(info.getAnswer())){
                            holder.oddsanswerLeft.setEnabled(false);
                            holder.oddsLeftHeader.setBorderWidth(ConfigUtils.dip2px(context, 0));
                        }else {

                            //判断擂主是否是自己
                            if (holder.oddsanswerLeft.isEnabled()) {
                                holder.oddsLeftHeader.setBorderColor(context.getResources().getColor(R.color.color_blue));
                                holder.oddsLeftHeader.setBorderWidth(ConfigUtils.dip2px(context, 1));
                            } else {
                                holder.oddsLeftHeader.setBorderWidth(ConfigUtils.dip2px(context, 0));

                            }
                        }

                    } else {
                        holder.oddsRightHeader.setVisibility(View.VISIBLE);
                        if(TextUtils.isEmpty(info.getAnswer())){
                            holder.oddsanswerRight.setEnabled(false);
                            holder.oddsRightHeader.setBorderWidth(ConfigUtils.dip2px(context, 0));
                        }else {
                            if (holder.oddsanswerRight.isEnabled()) {
                                holder.oddsRightHeader.setBorderColor(context.getResources().getColor(R.color.color_blue));
                                holder.oddsRightHeader.setBorderWidth(ConfigUtils.dip2px(context, 1));
                            } else {
                                holder.oddsLeftHeader.setBorderWidth(ConfigUtils.dip2px(context, 0));
                            }
                        }
                    }
                }
                holder.oddsanswerRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(TextUtils.isEmpty(list.get(position).getAnswer())){
                            holder.oddsanswerLeft.setEnabled(false);
                            list.get(position).setOddAnswer("1");
                            list.get(position).setOdds(true);
                            list.get(position).setAnswer("1");
                            holder.oddsRightHeader.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage((String)SPUtils.get(context, Preference.headPhoto,"",Preference.preference)
                            ,holder.oddsRightHeader, ImageLoaderOption.optionsHeader);
                        }


                        ConfigUtils.flash(context,holder.oddscontainerRight,Preference.lotteryMoney);
                    }
                });
                holder.oddsanswerLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(TextUtils.isEmpty(list.get(position).getAnswer())){
                            holder.oddsanswerRight.setEnabled(false);
                            list.get(position).setOddAnswer("0");
                            list.get(position).setOdds(true);
                            list.get(position).setAnswer("0");
                            holder.oddsLeftHeader.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage((String)SPUtils.get(context, Preference.headPhoto,"",Preference.preference)
                                    ,holder.oddsLeftHeader, ImageLoaderOption.optionsHeader);
                        }
                        ConfigUtils.flash(context,holder.oddsContainerLeft,Preference.lotteryMoney);
                    }
                });
            }else {
                holder.oddsLottery.setVisibility(View.GONE);
                holder.normalLottery.setVisibility(View.VISIBLE);
                holder.normalanserRight.setEnabled(true);
                holder.normalanswerLeft.setEnabled(true);
                holder.isOdds.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(info.getAnswer())){
                    if("0".equals(info.getAnswer())){
                        holder.normalanserRight.setEnabled(false);
                    }else {
                        holder.normalanswerLeft.setEnabled(false);
                    }
                }
            }
            holder.normalanserRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ConfigUtils.sendLotteryResult((Activity) context, Preference.room_id,info.getLotteryid(), info.getAnswerRight(), Preference.lotteryMoney, new ConfigUtils.ResultListener() {
                        @Override
                        public void result(SendLotteryResult result) {
                            if(TextUtils.isEmpty(list.get(position).getAnswer())){
                                holder.normalanswerLeft.setEnabled(false);
                                list.get(position).setAnswer("1");
                            }
                            ConfigUtils.flash(context,holder.normalContainerRight,Preference.lotteryMoney);
                            DBUtils dbUtils = new DBUtils(context);
                            TableChatLottery lottery = new TableChatLottery();
                            lottery.setGuessid(list.get(position).getLotteryid());
                            lottery.setMyAnswer("1");
                            lottery.setRoomid(Preference.room_id);
                            lottery.setTime(System.currentTimeMillis());
                            dbUtils.save(lottery);
                            dbUtils.delete();
                        }

                        @Override
                        public void last(String islast) {

                        }
                    });
                }
            });
            holder.normalanswerLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ConfigUtils.sendLotteryResult((Activity) context, Preference.room_id,info.getLotteryid(), info.getAnswerLeft(), Preference.lotteryMoney, new ConfigUtils.ResultListener() {
                        @Override
                        public void result(SendLotteryResult result) {
                            if(TextUtils.isEmpty(list.get(position).getAnswer())) {
                                holder.normalanserRight.setEnabled(false);
                                list.get(position).setAnswer("0");
                            }
                            ConfigUtils.flash(context,holder.normalContainerLeft,Preference.lotteryMoney);
                            DBUtils dbUtils = new DBUtils(context);
                            TableChatLottery lottery = new TableChatLottery();
                            lottery.setGuessid(list.get(position).getLotteryid());
                            lottery.setMyAnswer("0");
                            lottery.setRoomid(Preference.room_id);
                            lottery.setTime(System.currentTimeMillis());
                            dbUtils.save(lottery);
                            dbUtils.delete();
                        }

                        @Override
                        public void last(String islast) {

                        }
                    });
                }
            });
            holder.normalDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SPUtils.put(context,"lotterydilog",true,Preference.preference);
                    Intent intent = new Intent(context, LotteryDetailsActivity.class);
                    intent.putExtra("qid",list.get(position).getLotteryid());
                    intent.putExtra("roomid", Preference.room_id);
                    intent.putExtra("anchorid", Preference.zhubo_id);
                    ((Activity)context).startActivity(intent);
                }
            });
            holder.oddsDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SPUtils.put(context, "lotterydilog", true, Preference.preference);
                    Intent intent = new Intent(context, LotteryDetailsActivity.class);
                    intent.putExtra("qid",list.get(position).getLotteryid());
                    intent.putExtra("roomid", Preference.room_id);
                    intent.putExtra("anchorid", Preference.zhubo_id);
                    ((Activity)context).startActivity(intent);
                }
            });
        }else{
            holder.lottery.setVisibility(View.GONE);
            holder.content.setVisibility(View.VISIBLE);
            String name = info.getUsername();
            String conten = info.getContent();
            holder.content.getPaint().setFlags(0);
            //普通用户聊天
            if("1".equals(info.getType())){
                //添加勋章标识
                if ("1".equals(info.getLevel1())) {
                    name = name + "［lev1］";
                }
                if ("1".equals(info.getLevel2())) {
                    name = name + "［lev2］";
                }
                if ("1".equals(info.getLevel3())) {
                    name = name + "［lev3］";
                }
                if ("1".equals(info.getTopic())) {
                    name = name + "［lev4］";
                }
                name = name+":";

                String result = name+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main)), 0, name.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //判断是否是三级勋章是的话，说话颜色改变
                if("1".equals(info.getLevel3())) {
                    int medal_color = Color.parseColor(info.getContentColor());
                    ss.setSpan(new ForegroundColorSpan(medal_color), name.length(), result.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else{
                    //判断是否是开摄像头直播，是 文字变成白色
                    if(info.isLive()){
                        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)), name.length(), result.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }else {
                        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.textcolor_1)), name.length(), result.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                String[] em ={"［lev1］","［lev2］","［lev3］","［lev4］"};
                //寻找是否有出题权限和拥有的勋章，lev4出题权限
                int[] draw ={R.drawable.xun_1, R.drawable.xun_2, R.drawable.xun_3, R.drawable.topic};
                //匹配权限标识将对应的权限换成图片
                for(int i = 0;i<em.length;i++) {
                    String rexgString = em[i];
                    Pattern pattern = Pattern.compile(rexgString);
                    Matcher matcher = pattern.matcher(result);
                    while (matcher.find()) {
                        Drawable d = context.getResources().getDrawable(draw[i]);
                        d.setBounds(0, 0, ConfigUtils.dip2px(context, 15), ConfigUtils.dip2px(context, 13));
                        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                        ss.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }
                holder.content.setText(ss);
                holder.content.setEnabled(true);
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if("admin".equals(info.getUserId())||Preference.isClock){
                            return;
                        }
                        AnchorInfroDialog dialog = new AnchorInfroDialog(context);
                        dialog.setUserid(info.getUserId());
                        if (info.getUserId().equals(SPUtils.get(context, Preference.myuser_id, "", Preference.preference))) {
                            dialog.setType(4);
                        } else {
                            if (info.isPrivate()) {
                                dialog.setType(5);
                            } else {
                                dialog.setType(3);
                            }
                        }
                        if (Preference.zhubo_id.equals(SPUtils.get(context, Preference.myuser_id, "", Preference.preference))){
                            dialog.setType(5);
                        }
                        if (Preference.zhubo_id.equals(SPUtils.get(context, Preference.myuser_id, "", Preference.preference))&&Preference.zhubo_id.equals(info.getUserId())){
                            dialog.setType(4);
                        }

                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if(!info.isLive()) {
                                    FullScreenListener listener = (FullScreenListener) context;
                                    listener.fullScren(false);
                                }
                            }
                        });
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                if(!info.isLive()) {
                                    FullScreenListener listener = (FullScreenListener) context;
                                    listener.fullScren(true);
                                }
                            }
                        });
                        dialog.show();
                    }
                });

            }
            //管理员用户
            if("2".equals(info.getType())){
                name = name+"[管理员]:";

                String result = name + conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.main)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(ss);
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if("admin".equals(info.getUserId())||Preference.isClock){
                            return;
                        }
                        AnchorInfroDialog dialog = new AnchorInfroDialog(context);
                        dialog.setUserid(info.getUserId());
                        if (info.getUserId().equals(SPUtils.get(context, Preference.myuser_id, "", Preference.preference))) {
                            dialog.setType(4);
                        } else {
                            if (info.isPrivate()) {
                                dialog.setType(5);
                            } else {
                                dialog.setType(3);
                            }
                        }
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if(!info.isLive()) {
                                    FullScreenListener listener = (FullScreenListener) context;
                                    listener.fullScren(false);
                                }
                            }
                        });
                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialogInterface) {
                                if(!info.isLive()) {
                                    FullScreenListener listener = (FullScreenListener) context;
                                    listener.fullScren(true);
                                }
                            }
                        });
                        dialog.show();
                    }
                });
            }
            //邀请加入聊天室
            if("3".equals(info.getType())){
                String result = conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_chat_alarm)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                String[] em ={info.getInvitionName1(),info.getInvitionName2()};
                int medal_color = Color.parseColor("#A78110");
                for(int i = 0;i<em.length;i++) {
                    String rexgString = em[i];
                    Pattern pattern = Pattern.compile(rexgString);
                    Matcher matcher = pattern.matcher(result);
                    while (matcher.find()) {
                        ss.setSpan(new ForegroundColorSpan(medal_color),  matcher.start(),matcher.end(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                holder.content.setText(ss);
            }
            //4 30秒补刀 7活动
            if("4".equals(info.getType())||"7".equals(info.getType())){
                String result = name+":"+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_yellow)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                holder.content.setText(ss);
                holder.content.setEnabled(true);
                holder.content.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                holder.content.getPaint().setAntiAlias(true);//抗锯齿
                holder.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if("4".equals(list.get(position).getType())){
                            SPUtils.put(context, "lotterydilog", true, Preference.preference);
                            Intent intent = new Intent(context, LotteryDetailsActivity.class);
                                intent.putExtra("qid",list.get(position).getLotteryid());
                            intent.putExtra("roomid", Preference.room_id);
                            intent.putExtra("anchorid", Preference.zhubo_id);
                            ((Activity)context).startActivity(intent);
                        }
                        if("7".equals(list.get(position).getType())){
                        }
                    }
                });

            }
            //游戏中奖 用户加入聊天室
            if("5".equals(info.getType())||"6".equals(info.getType())){
                Pattern pattern = Pattern.compile(info.getInvitionName1());
                String result = name+":"+conten;
                Matcher matcher = pattern.matcher(result);
                SpannableString ss = new SpannableString(result);
                int medal_color = Color.parseColor("#A78110");
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_chat_alarm)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                while (matcher.find()) {
                    ss.setSpan(new ForegroundColorSpan(medal_color),  matcher.start(),matcher.end(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.content.setText(ss);
            }

            //竞猜赢
            if("8".equals(info.getType())){
                String result = name+":"+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_chat_alarm)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(ss);
            }
            //打赏
            if("10".equals(info.getType())){
                String result = name+":"+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_yellow)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(ss);
            }
            //-------------------------------------------------------------------------------------------------------------------------------
            //礼物
            if("11".equals(info.getType())){
                String result = name+":"+info.getNickname()+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_yellow)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(ss);
            }
            if("12".equals(info.getType())){
                String result = name+":"+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_yellow)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(ss);
            }
            if("13".equals(info.getType())){
                String result = name+":"+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_yellow)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(ss);
            }
            if("14".equals(info.getType())){
                String result = name+":"+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_yellow)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(ss);
            }
            if("15".equals(info.getType())){
                String result = name+":"+conten;
                SpannableString ss = new SpannableString(result);
                ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.color_yellow)), 0, result.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.content.setText(ss);
            }
            //---------------------------------------------------------------------------------------------------------------------------------
        }
        return convertView;
    }
    private class ViewHolder{
        private TextView content;
        private LinearLayout lottery;
        private TextView lotterySendPeople;
        private TextView isOdds;
        private RelativeLayout normalLottery;
        private TextView normalLotteryTitle;
        private Button normalanswerLeft;
        private Button normalanserRight;
        private TextView normalStopTime;
        private TextView normalDetail;
        private RelativeLayout normalContainerLeft;
        private RelativeLayout normalContainerRight;
        private RelativeLayout oddsLottery;
        private TextView oddsLotteryTitle;
        private Button oddsanswerLeft;
        private Button oddsanswerRight;
        private TextView oddsStoptime;
        private TextView odds;
        private TextView oddsDetail;
        private RelativeLayout oddsContainerLeft;
        private RelativeLayout oddscontainerRight;
        private CircleImageView oddsLeftHeader;
        private CircleImageView oddsRightHeader;
    }

}
