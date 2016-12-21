package com.joytouch.superlive.javabean;

import java.util.ArrayList;

/**
 * Created by lzx on 2016/5/16.
 * 调用源列表基类
 */
public class LiveSourceBase {
    private String status;
    private String message;
    private ArrayList<LiveSource> source;

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

    public ArrayList<LiveSource> getSource() {
        return source;
    }

    public void setSource(ArrayList<LiveSource> source) {
        this.source = source;
    }
}
