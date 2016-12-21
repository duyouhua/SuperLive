package com.joytouch.superlive.javabean;

/**
 * Created by sks on 2016/4/20.
 * 广场关注类
 */
public class SquareAttention {
    private String userid,nick_name,image,sign,level,is_con,behavitor_key,is_line;
    private SquareAttentionBehavitor behavitor_value;

    public String getIs_line() {
        return is_line;
    }

    public void setIs_line(String is_line) {
        this.is_line = is_line;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIs_con() {
        return is_con;
    }

    public void setIs_con(String is_con) {
        this.is_con = is_con;
    }

    public String getBehavitor_key() {
        return behavitor_key;
    }

    public void setBehavitor_key(String behavitor_key) {
        this.behavitor_key = behavitor_key;
    }

    public SquareAttentionBehavitor getBehavitor_value() {
        return behavitor_value;
    }

    public void setBehavitor_value(SquareAttentionBehavitor behavitor_value) {
        this.behavitor_value = behavitor_value;
    }
}
