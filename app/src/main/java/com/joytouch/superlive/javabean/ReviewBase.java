package com.joytouch.superlive.javabean;

import java.util.ArrayList;

/**
 * Created by lzx on 2016/4/10.
 */
public class ReviewBase {
    private String status;
    private String message;
    private ArrayList<ReviewList> list;

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

    public ArrayList<ReviewList> getList() {
        return list;
    }

    public void setList(ArrayList<ReviewList> list) {
        this.list = list;
    }
}
