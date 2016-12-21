package com.joytouch.superlive.javabean;

import java.util.ArrayList;

/**
 * Created by sks on 2016/4/27.
 * 回顾详情
 */
public class ReviewDetail {
    private ReviewMatch match;
    private String h5_url;
    private ArrayList<ReviewTeam>  match_team;
    private ArrayList<ReviewPlayJijin> match_review;

    public ArrayList<ReviewTeam> getMatch_team() {
        return match_team;
    }

    public void setMatch_team(ArrayList<ReviewTeam> match_team) {
        this.match_team = match_team;
    }

    public ReviewMatch getMatch() {
        return match;
    }

    public void setMatch(ReviewMatch match) {
        this.match = match;
    }

    public String getH5_url() {
        return h5_url;
    }

    public void setH5_url(String h5_url) {
        this.h5_url = h5_url;
    }

    public ArrayList<ReviewPlayJijin> getMatch_review() {
        return match_review;
    }

    public void setMatch_review(ArrayList<ReviewPlayJijin> match_review) {
        this.match_review = match_review;
    }
}
