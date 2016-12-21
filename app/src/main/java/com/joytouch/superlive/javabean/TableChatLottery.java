package com.joytouch.superlive.javabean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by yj on 2016/5/29.
 * 数据库表
 */
@Table(name = "TableChatLottery")
public class TableChatLottery {
    //记录的id
    @Column(name = "id", isId = true,autoGen=true)
    private int id;
    @Column(name = "guessid")
    private String guessid;
    @Column(name = "roomid")
    private String roomid;
    @Column(name = "time")
    private long time;
    @Column(name = "myAnswer")
    private String myAnswer = "";

    public String getGuessid() {
        return guessid;
    }

    public void setGuessid(String guessid) {
        this.guessid = guessid;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }


}
