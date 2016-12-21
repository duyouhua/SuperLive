package com.joytouch.superlive.javabean;

import java.util.List;

/**
 * Created by sks on 2016/5/14.
 */
public class SquareRecommentList {
    private List<SquareRecommend> HotAnchor;
    private List<SquareRecommend> SeniorPlayer;
    private List<SquareRecommend> RichPeople;
    private List<SquareRecommend> WeekRank;
    private List<SquareRecommend> OnlineUser;

    public List<SquareRecommend> getOnlineUser() {
        return OnlineUser;
    }

    public void setOnlineUser(List<SquareRecommend> onlineUser) {
        OnlineUser = onlineUser;
    }

    public List<SquareRecommend> getHotAnchor() {
        return HotAnchor;
    }

    public void setHotAnchor(List<SquareRecommend> hotAnchor) {
        HotAnchor = hotAnchor;
    }

    public List<SquareRecommend> getSeniorPlayer() {
        return SeniorPlayer;
    }

    public void setSeniorPlayer(List<SquareRecommend> seniorPlayer) {
        SeniorPlayer = seniorPlayer;
    }

    public List<SquareRecommend> getRichPeople() {
        return RichPeople;
    }

    public void setRichPeople(List<SquareRecommend> richPeople) {
        RichPeople = richPeople;
    }

    public List<SquareRecommend> getWeekRank() {
        return WeekRank;
    }

    public void setWeekRank(List<SquareRecommend> weekRank) {
        WeekRank = weekRank;
    }
}
