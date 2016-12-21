package com.joytouch.superlive.javabean;

import java.util.List;

/**
 * Created by lzx on 2016/5/15.
 * 广场关注列表 基类
 */
public class SquareAttentionBase {
    private String status;
    private String message ;
    private List<SquareAttention> list;

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

    public List<SquareAttention> getList() {
        return list;
    }

    public void setList(List<SquareAttention> list) {
        this.list = list;
    }
}
