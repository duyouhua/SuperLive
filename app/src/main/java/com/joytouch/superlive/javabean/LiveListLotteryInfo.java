package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/5/4.
 * 直播列表页竞猜
 */
public class LiveListLotteryInfo {
 private MatchTime time;
    private String room_id;
    private String left;
    private String center;
    private String right;
    private String leftGold;
    private String centerGold;
    private String rightGold;
    private String myOption;
    private String stoptime;
    private String endTime;
    private String roombet;
    private String lotteryId;
    private String mode;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getLeftGold() {
        return leftGold;
    }

    public void setLeftGold(String leftGold) {
        this.leftGold = leftGold;
    }

    public String getCenterGold() {
        return centerGold;
    }

    public void setCenterGold(String centerGold) {
        this.centerGold = centerGold;
    }

    public String getRightGold() {
        return rightGold;
    }

    public void setRightGold(String rightGold) {
        this.rightGold = rightGold;
    }

    public String getMyOption() {
        return myOption;
    }

    public void setMyOption(String myOption) {
        this.myOption = myOption;
    }

    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRoombet() {
        return roombet;
    }

    public void setRoombet(String roombet) {
        this.roombet = roombet;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public MatchTime getTime() {
        return time;
    }

    public void setTime(MatchTime time) {
        this.time = time;
    }
}
