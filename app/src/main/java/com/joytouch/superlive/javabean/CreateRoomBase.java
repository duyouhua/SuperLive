package com.joytouch.superlive.javabean;

import java.util.ArrayList;

/**
 * Created by lzx on 2016/5/27.
 * 创建房间 基类
 */
public class CreateRoomBase {
    private String status;
    private String message;
    private String room_id;
    private String room_price;
    private AuchorInfo anchor_info;
    private String start;
    private String share_url;
    private ArrayList<Teaminfo> team_info;
    private String gift_of_red;
    private String gift_of_blue;
    private String room_score;
    private String online;

    public ArrayList<Teaminfo> getTeam_info() {
        return team_info;
    }

    public void setTeam_info(ArrayList<Teaminfo> team_info) {
        this.team_info = team_info;
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

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public AuchorInfo getAnchor_info() {
        return anchor_info;
    }

    public void setAnchor_info(AuchorInfo anchor_info) {
        this.anchor_info = anchor_info;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getRoom_price() {
        return room_price;
    }

    public void setRoom_price(String room_price) {
        this.room_price = room_price;
    }

    public String getGift_of_red() {
        return gift_of_red;
    }

    public void setGift_of_red(String gift_of_red) {
        this.gift_of_red = gift_of_red;
    }

    public String getGift_of_blue() {
        return gift_of_blue;
    }

    public void setGift_of_blue(String gift_of_blue) {
        this.gift_of_blue = gift_of_blue;
    }

    public String getRoom_score() {
        return room_score;
    }

    public void setRoom_score(String room_score) {
        this.room_score = room_score;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
