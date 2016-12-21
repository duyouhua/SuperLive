package com.joytouch.superlive.javabean;

import java.util.ArrayList;

/**
 * Created by lzx on 2016/5/9.
 * 回顾选项基础类
 */
public class ReviewOptionBase {
    private String status;
    private String message;
    private ArrayList<ReviewOptionTime> date_conf;
    private ArrayList<ReviewOptionMatchBase> cat_conf;

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

    public ArrayList<ReviewOptionTime> getDate_conf() {
        return date_conf;
    }

    public void setDate_conf(ArrayList<ReviewOptionTime> date_conf) {
        this.date_conf = date_conf;
    }

    public ArrayList<ReviewOptionMatchBase> getCat_conf() {
        return cat_conf;
    }

    public void setCat_conf(ArrayList<ReviewOptionMatchBase> cat_conf) {
        this.cat_conf = cat_conf;
    }
}
