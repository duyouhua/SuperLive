package com.joytouch.superlive.javabean;

/**
 * @author
 * @Description 礼物的实体类
 * @Date 2016/6/6.
 */
public class GiftVo {

    private String userId;//送礼物人的Name
    private int num;//送礼物的个数
    private String image;//送礼物的人的头像地址
    private String gift_id;//礼物id
    private String msg;//描述
    private String ident;//不同种类不同颜色

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
