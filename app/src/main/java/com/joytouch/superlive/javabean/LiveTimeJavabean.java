package com.joytouch.superlive.javabean;

import java.util.ArrayList;

/**
 * Created by yj on 2016/4/8.
 * 直播时间
 */
public class LiveTimeJavabean {
    private String time;
    private long timeLong;
    private ArrayList<LiveMatchInfoJavabean> matchinfo;

    public ArrayList<LiveMatchInfoJavabean> getMatchinfo() {
        return matchinfo;
    }

    public void setMatchinfo(ArrayList<LiveMatchInfoJavabean> matchinfo) {
        this.matchinfo = matchinfo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public long getTimeLong() {
        return timeLong;
    }

    public void setTimeLong(long timeLong) {
        this.timeLong = timeLong;
    }
}
