package com.joytouch.superlive.javabean;

import java.io.Serializable;

/**
 * Created by lzx on 2016/5/27.
 * 创建房间时3显示的主播信息
 */
public class AuchorInfo implements Serializable{
    private String userid,nick_name;
    private String level,user_type;
    private String image;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
