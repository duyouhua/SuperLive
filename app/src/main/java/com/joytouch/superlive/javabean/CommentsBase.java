package com.joytouch.superlive.javabean;

import java.util.ArrayList;

/**
 * Created by lzx on 2016/5/10.
 * 评论列表base类
 */
public class CommentsBase {
    private String status;
    private String message;
    private ArrayList<ReviewComment> Comment;

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

    public ArrayList<ReviewComment> getComment() {
        return Comment;
    }

    public void setComment(ArrayList<ReviewComment> comment) {
        Comment = comment;
    }
}
