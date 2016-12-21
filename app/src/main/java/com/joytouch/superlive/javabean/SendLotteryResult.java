package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/5/15.
 * 发送竞猜的返回结果
 */
public class SendLotteryResult {
    private String leftgold;
    private String rightgold;
    private String centergold;
    private String betting;
    private String earnings;
    private String last;


    public String getCentergold() {
        return centergold;
    }

    public void setCentergold(String centergold) {
        this.centergold = centergold;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getLeftgold() {
        return leftgold;
    }

    public void setLeftgold(String leftgold) {
        this.leftgold = leftgold;
    }

    public String getRightgold() {
        return rightgold;
    }

    public void setRightgold(String rightgold) {
        this.rightgold = rightgold;
    }

    public String getBetting() {
        return betting;
    }

    public void setBetting(String betting) {
        this.betting = betting;
    }

    public String getEarnings() {
        return earnings;
    }

    public void setEarnings(String earnings) {
        this.earnings = earnings;
    }
}
