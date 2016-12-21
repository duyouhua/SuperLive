package com.joytouch.superlive.javabean;

/**
 * Created by yj on 2016/5/17.
 * 房间排行
 */
public class RoomBank {
    private String image;
    private String useid;
    private boolean isMe;

    public String getUseid() {
        return useid;
    }

    public void setUseid(String useid) {
        this.useid = useid;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setIsMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
