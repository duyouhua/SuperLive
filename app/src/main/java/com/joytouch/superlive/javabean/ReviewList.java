package com.joytouch.superlive.javabean;

import java.util.ArrayList;

/**
 * Created by lzx on 2016/5/9.
 */
public class ReviewList {
    private ReviewInfo base_info;
    private ArrayList<ReviewTeamInfo> team_info;

    public ReviewInfo getBase_info() {
        return base_info;
    }

    public void setBase_info(ReviewInfo base_info) {
        this.base_info = base_info;
    }

    public ArrayList<ReviewTeamInfo> getTeam_info() {
        return team_info;
    }

    public void setTeam_info(ArrayList<ReviewTeamInfo> team_info) {
        this.team_info = team_info;
    }
}
