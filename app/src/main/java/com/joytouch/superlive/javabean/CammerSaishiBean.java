package com.joytouch.superlive.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 8/12 0012.
 */
public class CammerSaishiBean implements Serializable {
    public String match_id;
    public String message;
    public String room_id;
    public String share_url;
    public String status;
    public String stream_info;
    public Author_info author_info ;
    public List<rank_item> team_info;
    public String gift_of_blue;
    public String gift_of_red;
    public String match_name;
    public String online;
    public String room_score;

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
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

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStream_info() {
        return stream_info;
    }

    public void setStream_info(String stream_info) {
        this.stream_info = stream_info;
    }

    public Author_info getAuthor_info() {
        return author_info;
    }

    public void setAuthor_info(Author_info author_info) {
        this.author_info = author_info;
    }

    public List<rank_item> getTeam_info() {
        return team_info;
    }

    public void setTeam_info(List<rank_item> team_info) {
        this.team_info = team_info;
    }

    public String getGift_of_blue() {
        return gift_of_blue;
    }

    public void setGift_of_blue(String gift_of_blue) {
        this.gift_of_blue = gift_of_blue;
    }

    public String getGift_of_red() {
        return gift_of_red;
    }

    public void setGift_of_red(String gift_of_red) {
        this.gift_of_red = gift_of_red;
    }

    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getRoom_score() {
        return room_score;
    }

    public void setRoom_score(String room_score) {
        this.room_score = room_score;
    }
}
