package com.joytouch.superlive.javabean;

import java.util.List;

/**
 * Created by sks on 2016/4/8.
 */
public class GuessRank extends BaseBean{
    public MyRankInfo myrank;
    public List<rank_item> rank_list;
    private String name;
    private String image_;
    private String top;
    private String gold;
    private String level_;
    private String topic;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLevel_() {
        return level_;
    }

    public void setLevel_(String level_) {
        this.level_ = level_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_() {
        return image_;
    }

    public void setImage_(String image_) {
        this.image_ = image_;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }


}
