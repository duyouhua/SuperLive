package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/5/7.
 * 比赛时间
 */
public class MatchTime {
    private String time;
    private long timeLong;
    private String today;

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
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
