package com.cjzhibo.im;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.joytouch.superlive.app.Preference;
import com.joytouch.superlive.javabean.ChatInfo;
import com.joytouch.superlive.javabean.LiveMatchInfoJavabean;
import com.joytouch.superlive.javabean.RoomBank;
import com.joytouch.superlive.javabean.redInfo;
import com.joytouch.superlive.utils.EmojiReplace;
import com.joytouch.superlive.utils.LogUtils;
import com.joytouch.superlive.utils.TimeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImApi {
        static {
            System.loadLibrary("gloox");
    }
    
        public void start(String name,String nickname,String token){
            Log.e("ceshi...", name + "____" + token);
//    		startIm("im.cjzhibo.net", "im.cjzhibo.net", 6222, 1, 0);
//            startIm("im.cjzhibo.net", "124.65.163.254",5222, 1, 0);
                        startIm("im.cjzhibo.net", "124.65.163.254",8222, 1, 0);
            bindUser(name,token);
     	}


    public native void startIm(String server,String realServer, int port,int simple_mode, int compress);
    public native void stopIm();
    public native void bindUser(String username, String passwd);
    public native void unBindUser();
    public native int getStatus();

    public native boolean joinRoom(String roomName, String nick);
    public native boolean leaveRoom(String roomName);
    public native boolean sendUserMsg(String username, String msg);
    public native boolean sendRoomMsg(String roomName, String msg);
    private ReceiveMessage receiveMessageListener;
    private OneMessage receiveOneMessageListener;
    private OtherMessage otherMessageListener;
    private Match matchListener;

    public void setMatchListener(Match matchListener) {
        this.matchListener = matchListener;
    }

    public void setOtherMessageListener(OtherMessage otherMessageListener) {
        this.otherMessageListener = otherMessageListener;
    }

    public void setReceiveMessageListener(ReceiveMessage receiveMessageListener) {
        this.receiveMessageListener = receiveMessageListener;
    }

    public void setOneMessageListener(OneMessage receiveOneMessageListener) {
        this.receiveOneMessageListener = receiveOneMessageListener;
    }
    Handler mesage = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*
            * form 发送人
            * typ1 判断是单聊还是群聊，
            * typ2 判断消息的类型
            * body 消息体
            * */
           Bundle bundle =  msg.getData();
            String form =bundle.getString("form");
            String typ1 = bundle.getString("typ1");
            String typ2 = bundle.getString("typ2");

            String body ="" ;
//            try {
                body =  EmojiReplace.emojiRecovery2(bundle.getString("body"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            if("match_bifeng".equals(typ2)){
                LogUtils.e("ssssssssssss",""+test(form));
                matchParse(test(form),body);

            }

            if (receiveOneMessageListener != null) {
                //ChatActivity中使用,直接收chat类型和nor"mal类型
                if("message:chat".equals(typ1)&& "".equals(typ2)){
                    receiveOneMessageListener.chatMessage(form,body);
                }
                //通知中使用,接收所有的并且不在聊天室,但是在BaseActivity中调用监听
                if("message:chat".equals(typ1)){
                    receiveOneMessageListener.Messagelist(form,body,typ2);
                }
            }

            if(receiveMessageListener!=null){
                //ChatActivity中使用,直接收chat类型和normal类型
                if("message:chat".equals(typ1)&& "".equals(typ2)){
                    receiveMessageListener.chatMessage(form,body);
                }
                //通知中使用,接收所有的并且不在聊天室,但是在BaseActivity中调用监听
                if("message:chat".equals(typ1)){
                    receiveMessageListener.Messagelist(form,body,typ2);
                }
                String[] ss = form.split("@");
                String userid = ss[0];

                if ("message::groupchat".equals(typ1)&& Preference.room_id.equals(ss[0])){

                    try {
                        JSONObject object = null;
                        String[] ss3 = form.split("@");
                        String roomid = ss3[0];
                        if(!"top_bet".equals(typ2)) {
                            object = new JSONObject(body);
                        }

                        if("gc_v1".equals(typ2)&&!TextUtils.isEmpty(object.optString("user_name"))) {
                            String[] bb = form.split("/");
                            String userLable = bb[1];
                            ChatInfo info = new ChatInfo();
                            info.setRoomid(roomid);
                            info.setLotteryid(object.optString("ext"));
                            info.setOdds(false);
                            JSONArray reward = object.optJSONArray("medal");
                            if (reward != null) {
                                for (int i = 0; i < reward.length(); i++) {
                                    LogUtils.e("ssssss", "" + reward.optString(i));
                                    LogUtils.e("ssssss", "" + "1".equals(reward.optString(i)));

                                    if ("1".equals(reward.optString(i))) {
                                        info.setLevel1("1");
                                    }
                                    LogUtils.e("ssssss", "" + "2".equals(reward.optString(i)));
                                    if ("2".equals(reward.optString(i))) {
                                        info.setLevel2("1");
                                    }
                                    LogUtils.e("ssssss", "" + "3".equals(reward.optString(i)));
                                    if ("3".equals(reward.optString(i))) {
                                        info.setLevel3("1");
                                        info.setContentColor("#7158C0");
                                    }
                                }
                            }
                            if("topic".equals(object.optString("user_status"))){
                                info.setTopic("1");
                            }else{
                                info.setTopic("0");
                            }

                            if (!TextUtils.isEmpty(object.optString("type"))) {
                                switch (Integer.parseInt(object.optString("type"))) {
                                    case 0://普通用户
                                        info.setType("1");
                                        info.setUsername(object.optString("user_name"));
                                        info.setContent(object.optString("msg").trim());
                                        info.setChatType(object.optString("timestamp"));
                                        info.setUserId(userLable);
                                        break;
                                    case 1://管理员
                                        info.setType("2");
                                        info.setUsername("");
                                        info.setContent(object.optString("msg").trim());
                                        info.setChatType(object.optString("timestamp"));
                                        info.setUserId(userLable);
                                        break;
                                    case 2:
                                        info.setType("3");
                                        break;
                                    case 3://游戏
                                        info.setType("5");
                                        info.setUsername("提醒");
                                        //info.setContent("[" + object.optString("user_name") + "]佛光照顶，博得头彩！");
                                        info.setContent(object.optString("msg").trim());
                                        info.setInvitionName1("[" + object.optString("user_name") + "]");
                                        break;
                                    case 4://赢钱
                                        info.setType("8");
                                        info.setUsername("提醒");
                                        info.setContent("陛下，又赢钱了，赶紧数数吧！");
                                        break;
                                    case 5://加入聊天室
                                        info.setType("6");
                                        info.setUsername("提醒");
                                        info.setContent("热烈欢迎[" + object.optString("user_name") + "]加入聊天室！");
                                        info.setInvitionName1("[" + object.optString("user_name") + "]");
                                        break;
                                    case 6://补刀
                                        info.setType("4");
                                        info.setUsername("广播");
                                        info.setContent("“" + object.optString("msg") + "”" + "即将结束，速速点击，开启补刀模式！");
                                        break;
                                    case 7://打赏广播
                                        info.setType("10");
                                        info.setUsername("广播");
                                        info.setContent(object.optString("message"));
                                        break;
                                    case 8://礼物
                                        info.setType("11");
                                        info.setUsername("广播");
                                        info.setBlue(object.optString("blue"));
                                        info.setRed(object.optString("red"));
                                        info.setGift_id(object.optString("gift_id"));
                                        info.setCount(object.optString("count"));
                                        info.setImage(object.optString("image"));
                                        info.setNickname(object.optString("nick_name"));
                                        info.setContent(object.optString("msg"));
                                        info.setIdent(object.optString("ident"));
                                        break;
                                    case 9://审核通知
                                        info.setType("10");
                                        info.setUsername("广播");
                                        info.setContent(object.optString("message"));
                                        break;
                                }
                                info.setOdds(false);
                                receiveMessageListener.groupMessage(info);
                            }
                        }
                        //红包
                        if ("redpacket".equals(typ2)){
                            if (receiveMessageListener!=null){
                                Gson json=new Gson();
                                redInfo info_=json.fromJson(body,redInfo.class);
                                receiveMessageListener.redReceive(info_);
                            }
                        }
                        //授权消息
                        if("authorize_administrator".equals(typ2)){
                            if(receiveMessageListener!=null) {
                                ChatInfo info = parseLotteryLast(body);
                                info.setRoomid(roomid);
                                info.setType("12");
                                info.setUsername("广播");
                                if (userid.equals(Preference.myuser_id)){
                                    info.setContent("您已被主播设置为管理员");
                                }else{
                                    info.setContent(object.optString("msg"));
                                }
                                receiveMessageListener.groupMessage(info);
                            }
                        }
                        //主播离开
                        if("anchor_leave".equals(typ2)){
                            if(receiveMessageListener!=null) {
                                ChatInfo info = parseLotteryLast(body);
                                info.setRoomid(roomid);
                                info.setType("13");
                                info.setUsername("广播");
                                info.setContent(object.optString("msg"));
                                receiveMessageListener.groupMessage(info);
                            }
                        }
                        //打开红包广播
                        if("redpacket_snatched".equals(typ2)){
                            if(receiveMessageListener!=null) {
                                ChatInfo info = parseLotteryLast(body);
                                info.setRoomid(roomid);
                                info.setType("14");
                                info.setUsername("广播");
                                info.setContent(object.optString("msg"));
                                receiveMessageListener.groupMessage(info);
                            }
                        }
                        //手气王广播
                        if("redpacket_max_money".equals(typ2)){
                            if(receiveMessageListener!=null) {
                                ChatInfo info = parseLotteryLast(body);
                                info.setRoomid(roomid);
                                info.setType("15");
                                info.setUsername("广播");
                                //不是竞猜红包广播
                                if (object.optString("receiver_answer").equals("")){
                                    info.setContent("财神登基！"+object.optString("receiver_user_name")+"抢到"+object.optString("money")+"金币的红包登上运气王位.");
                                }else{//是精彩红包广播
                                    info.setContent("双喜临门！"+object.optString("receiver_user_name")+"荣升记忆王和运气王! "+object.optString("money")+"金币的红包奉上.");
                                }
                                receiveMessageListener.groupMessage(info);
                            }
                        }
                        if("guess_budao".equals(typ2)){
                            if(receiveMessageListener!=null) {
                                ChatInfo budao = parseLotteryLast(body);
                                budao.setRoomid(roomid);
                                receiveMessageListener.groupMessage(budao);
                            }
                        }
                        if("online_user_count".equals(typ2)) {
                            if(otherMessageListener!=null){
                                otherMessageListener.online(object.optString("online_user_count"),roomid);
                            }
                        }

                        if("top_bet".equals(typ2)) {

                            if(otherMessageListener!=null){

                                List<RoomBank> list = new ArrayList<>();
                                JSONArray jsonArray = new JSONArray(body);
                                if(jsonArray!=null){
                                    for (int i = 0;i<jsonArray.length();i++){
                                        JSONObject object1 = jsonArray.optJSONObject(i);
                                        RoomBank bank = new RoomBank();
                                        bank.setImage(object1.optString("img"));
                                        bank.setUseid(object1.getString("userid"));
                                        list.add(bank);
                                    }
                                }
                                otherMessageListener.roomRankTop(roomid,list);
                            }
                        }
                        if("guess_begin".equals(typ2)) {
                            ChatInfo begin = parseLottery(body);
                            begin.setRoomid(roomid);
                           receiveMessageListener.groupMessage(begin);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    };

    private void matchParse(boolean self,String body) {
        try {
            JSONArray object1 = new JSONArray(body);
            List<LiveMatchInfoJavabean> list = new ArrayList<>();
            if(matchListener!=null){
                for(int i = 0;i<object1.length();i++) {
                    JSONObject object = object1.optJSONObject(i);
                    LiveMatchInfoJavabean bean = new LiveMatchInfoJavabean();
                    String matchid = object.optString("match_id");
                    String matchState = object.optString("match_status");
                    String state = object.optString("show_state");
                    String colors=object.optString("colors");
                    String cat_type=object.optString("cat_type");
                    String score;
                    if(!TextUtils.isEmpty(object.optString("result"))) {
                        String[] ss = object.optString("result").split(",");
                         score = ss[0] + " - " + ss[1];
                    }else{
                        score = 0+ " - " +0;
                    }
                    bean.setStating(state);
                    bean.setMatchId(matchid);
                    bean.setMatchStatus(matchState);
                    bean.setColors(colors);
                    bean.setCat_type(cat_type);
                    bean.setScore(score);
                    list.add(bean);
                }
                matchListener.match(self,list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.e("match_bifeng","sssssssssssssssss异常");
        }
    }

    /*
    都是关于我的消息
     * type1:"message:chat单聊"/"message:groupchat群聊"
     * from:"xxxx@server/resource",值是: 对方的username或者userid
     * type2:自定义的type
     * body:接收到的消息主体。
     * http://www.xuebuyuan.com/1469657.html
     */
    public int receiveFromIm(String from,  String type1, String type2, String body)
    {
        //私信:  先判断type为chat,再判断from(username)
        Log.e("TEST", "from=" + from + "   type1=" + type1   +"   type2=" + type2 +"   body=" + body);
        Message ms = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("form",from);
        bundle.putString("typ1",type1);
        bundle.putString("typ2",type2);
        bundle.putString("body",body);
        ms.setData(bundle);
        mesage.sendMessage(ms);
        return 0;
    }

    public int statusFromIm(int status)
    {
        Log.i("TEST", "im status:" + status);
        if(receiveMessageListener!= null){
            receiveMessageListener.statusFormIm(status);
        }
        return 0;
    }




public interface ReceiveMessage{
    void Messagelist(String from, String body,String type2);
    void chatMessage(String from, String body);
    void groupMessage(ChatInfo info);
    void statusFormIm(int status);
    void redReceive(redInfo  info);
}
public interface OneMessage{
    void Messagelist(String from, String body,String type2);
    void chatMessage(String from, String body);
    void groupMessage(ChatInfo info);

}
public interface OtherMessage{
    void online(String count,String roomid);
    void roomRankTop(String roomid,List<RoomBank> list);
}

    public interface Match{
        void match(boolean b,List<LiveMatchInfoJavabean> infos);
    }

    private ChatInfo parseLottery(String json){
        ChatInfo info = new ChatInfo();
        try {
            JSONObject object = new JSONObject(json);

            info.setLotteryid(object.optString("id"));
            info.setType("9");
            info.setOdds(false);
            info.setTimestamp(object.optString("timestamp"));
            info.setTitle(object.optString("title"));
            String[] s1 = object.optString("item_list").split(",");
            info.setAnswerLeft(s1[0]);
            info.setAnswerRight(s1[1]);
            if(!TextUtils.isEmpty(object.optString("selected_item"))){
                info.setAnswer(object.optString("selected_item"));
            }
            info.setStoptime(TimeUtil.currentSplitTimeStringHm(object.optString("stop_time")));
            info.setUsername(object.optString("cjzb_user_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }
    private ChatInfo parseLotteryLast(String json){
        ChatInfo info = new ChatInfo();
        try {
            JSONObject object = new JSONObject(json);
            info.setLotteryid(object.optString("id"));
            info.setType("4");
            info.setUsername("广播");
            info.setContent("“" + object.optString("title") + "”" + "即将结束，速速点击，开启补刀模式！");
            info.setOdds(false);
            info.setTitle(object.optString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }
    private boolean test(String barcodeDesc) {
        Pattern p;
        p = Pattern.compile("conference.im.cjzhibo.net");//在这里，编译 成一个正则。
        Matcher m;
        m = p.matcher(barcodeDesc);//获得匹配
        String res = "";


        return m.find();
    }
}


