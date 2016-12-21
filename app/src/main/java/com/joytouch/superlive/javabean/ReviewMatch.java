package com.joytouch.superlive.javabean;

/**
 * Created by sks on 2016/4/27.
 * 回顾详情比分详情logo等
 */
public class ReviewMatch {
    private String match_name;
    private String starttime;
    private String result;
    private String qiutan_match_id;

    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getQiutan_match_id() {
        return qiutan_match_id;
    }

    public void setQiutan_match_id(String qiutan_match_id) {
        this.qiutan_match_id = qiutan_match_id;
    }
}
