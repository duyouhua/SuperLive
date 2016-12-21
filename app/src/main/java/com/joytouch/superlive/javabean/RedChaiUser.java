package com.joytouch.superlive.javabean;

/**
 * Created by sks on 2016/7/24.
 */
public class RedChaiUser {
    private String id,redpacket_id,receiver_user_id,receiver_user_name,receiver_user_image,seq,is_max,create_by,update_by,remarks;
    private int money;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRedpacket_id() {
        return redpacket_id;
    }

    public void setRedpacket_id(String redpacket_id) {
        this.redpacket_id = redpacket_id;
    }

    public String getReceiver_user_id() {
        return receiver_user_id;
    }

    public void setReceiver_user_id(String receiver_user_id) {
        this.receiver_user_id = receiver_user_id;
    }

    public String getReceiver_user_name() {
        return receiver_user_name;
    }

    public void setReceiver_user_name(String receiver_user_name) {
        this.receiver_user_name = receiver_user_name;
    }

    public String getReceiver_user_image() {
        return receiver_user_image;
    }

    public void setReceiver_user_image(String receiver_user_image) {
        this.receiver_user_image = receiver_user_image;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getIs_max() {
        return is_max;
    }

    public void setIs_max(String is_max) {
        this.is_max = is_max;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
