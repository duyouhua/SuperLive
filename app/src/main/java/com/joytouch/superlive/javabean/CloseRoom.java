package com.joytouch.superlive.javabean;

import java.io.Serializable;

/**
 * Created by lzx on 2016/5/29.
 * 关闭房间
 */
public class CloseRoom implements Serializable {
    private String status;
    private String message;
    private String watch_num;
    private String task_room;
    private String reward_balance;
    private String room_blue;
    private String room_red;
    private String room_all;

    public String getRoom_blue() {
        return room_blue;
    }

    public void setRoom_blue(String room_blue) {
        this.room_blue = room_blue;
    }

    public String getRoom_red() {
        return room_red;
    }

    public void setRoom_red(String room_red) {
        this.room_red = room_red;
    }

    public String getRoom_all() {
        return room_all;
    }

    public void setRoom_all(String room_all) {
        this.room_all = room_all;
    }

    public String getReward_balance() {
        return reward_balance;
    }

    public void setReward_balance(String reward_balance) {
        this.reward_balance = reward_balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWatch_num() {
        return watch_num;
    }

    public void setWatch_num(String watch_num) {
        this.watch_num = watch_num;
    }

    public String getTask_room() {
        return task_room;
    }

    public void setTask_room(String task_room) {
        this.task_room = task_room;
    }
}
