package com.joytouch.superlive.javabean;

import java.util.List;

/**
 * Created by sks on 2016/4/13.
 * 投注榜个人数据
 */
public class GuessDetail extends BaseBean{
    public String my_option_money;
    public String predict_money;
    public QueInfo que_info;
    public List<guess_item> left;
    public List<guess_item> right;
    private String name;
    private String imgurl;
//    private String money;
    private String isMe;
    private String isLast;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

//    public String getMoney() {
//        return money;
//    }
//
//    public void setMoney(String money) {
//        this.money = money;
//    }

    public String getIsMe() {
        return isMe;
    }

    public void setIsMe(String isMe) {
        this.isMe = isMe;
    }

    public String getIsLast() {
        return isLast;
    }

    public void setIsLast(String isLast) {
        this.isLast = isLast;
    }

}
