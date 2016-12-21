package com.joytouch.superlive.javabean;

/**
 * Created by sks on 2016/4/27.
 * 回顾详情基类
 */
public class ReviewDetailBase {
    private String status;
    private String message;
    private ReviewDetail list;

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

    public ReviewDetail getList() {
        return list;
    }

    public void setList(ReviewDetail list) {
        this.list = list;
    }
}
