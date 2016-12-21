package com.joytouch.superlive.javabean;

/**
 * Created by lzx on 2016/4/20.
 * 广场推荐类
 */
public class SquareRecommend {
    private String is_con,userid,username,image,level,sign,behavitor_key;
    private SquareAttentionBehavitor behavitor_value;

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

    public String getIs_con() {
        return is_con;
    }

    public void setIs_con(String is_con) {
        this.is_con = is_con;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
