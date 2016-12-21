package com.joytouch.superlive.javabean;

/**
 * Created by lzx on 2016/5/14.
 * 广场推荐基类
 */
public class SquareRecommendBase {
    private String status;
    private String message;
    private SquareRecommentList Recommend;

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

    public SquareRecommentList getRecommend() {
        return Recommend;
    }

    public void setRecommend(SquareRecommentList recommend) {
        Recommend = recommend;
    }
}
